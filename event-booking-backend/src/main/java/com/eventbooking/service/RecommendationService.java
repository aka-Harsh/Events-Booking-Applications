package com.eventbooking.service;

import com.eventbooking.model.Event;
import com.eventbooking.model.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private EventService eventService;

    // Get recommended events based on event similarity (simple implementation)
    public List<Event> getRecommendedEvents(Long eventId, int limit) {
        Optional<Event> baseEventOpt = eventService.getEventById(eventId);
        if (!baseEventOpt.isPresent()) {
            return Collections.emptyList();
        }

        Event baseEvent = baseEventOpt.get();
        List<Event> allEvents = eventService.getAllEvents();

        // Remove the base event from recommendations
        allEvents = allEvents.stream()
                .filter(event -> !event.getId().equals(eventId))
                .collect(Collectors.toList());

        // Calculate similarity scores
        Map<Event, Double> similarityScores = new HashMap<>();

        for (Event event : allEvents) {
            double similarity = calculateSimilarity(baseEvent, event);
            if (similarity > 0) {
                similarityScores.put(event, similarity);
            }
        }

        // Sort by similarity score and return top recommendations
        return similarityScores.entrySet().stream()
                .sorted(Map.Entry.<Event, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Get recommended events for user based on their booking history
    public List<Event> getPersonalizedRecommendations(Long userId, int limit) {
        // This is a simplified version - in a real system, you'd analyze user's booking patterns
        List<Event> upcomingEvents = eventService.getUpcomingEvents();
        List<Event> popularEvents = eventService.getPopularEvents(30.0); // 30% or more sold

        // Combine and remove duplicates
        Set<Event> recommendedEvents = new LinkedHashSet<>();
        recommendedEvents.addAll(popularEvents);
        recommendedEvents.addAll(upcomingEvents);

        return recommendedEvents.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Get trending events (popular recent events)
    public List<Event> getTrendingEvents(int limit) {
        return eventService.getPopularEvents(25.0).stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Get events by similar type
    public List<Event> getSimilarEventsByType(Long eventId, int limit) {
        Optional<Event> baseEventOpt = eventService.getEventById(eventId);
        if (!baseEventOpt.isPresent()) {
            return Collections.emptyList();
        }

        Event baseEvent = baseEventOpt.get();
        return eventService.getEventsByType(baseEvent.getType()).stream()
                .filter(event -> !event.getId().equals(eventId))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Get events in same location
    public List<Event> getEventsInSameLocation(Long eventId, int limit) {
        Optional<Event> baseEventOpt = eventService.getEventById(eventId);
        if (!baseEventOpt.isPresent()) {
            return Collections.emptyList();
        }

        Event baseEvent = baseEventOpt.get();
        return eventService.searchEventsByLocation(baseEvent.getLocation()).stream()
                .filter(event -> !event.getId().equals(eventId))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Calculate similarity between two events (simplified TF-IDF approach)
    private double calculateSimilarity(Event event1, Event event2) {
        double similarity = 0.0;

        // Type similarity (40% weight)
        if (event1.getType() == event2.getType()) {
            similarity += 0.4;
        }

        // Location similarity (20% weight)
        if (event1.getLocation() != null && event2.getLocation() != null) {
            if (event1.getLocation().equalsIgnoreCase(event2.getLocation())) {
                similarity += 0.2;
            } else if (containsCommonWords(event1.getLocation(), event2.getLocation())) {
                similarity += 0.1;
            }
        }

        // Tags similarity (30% weight)
        if (event1.getTags() != null && event2.getTags() != null) {
            similarity += calculateTagsSimilarity(event1.getTags(), event2.getTags()) * 0.3;
        }

        // Name similarity (10% weight)
        if (event1.getName() != null && event2.getName() != null) {
            similarity += calculateTextSimilarity(event1.getName(), event2.getName()) * 0.1;
        }

        return similarity;
    }

    // Calculate tags similarity
    private double calculateTagsSimilarity(String tags1, String tags2) {
        if (tags1 == null || tags2 == null) return 0.0;

        Set<String> tagSet1 = Arrays.stream(tags1.toLowerCase().split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

        Set<String> tagSet2 = Arrays.stream(tags2.toLowerCase().split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

        // Calculate Jaccard similarity
        Set<String> intersection = new HashSet<>(tagSet1);
        intersection.retainAll(tagSet2);

        Set<String> union = new HashSet<>(tagSet1);
        union.addAll(tagSet2);

        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }

    // Calculate basic text similarity
    private double calculateTextSimilarity(String text1, String text2) {
        if (text1 == null || text2 == null) return 0.0;

        String[] words1 = text1.toLowerCase().split("\\s+");
        String[] words2 = text2.toLowerCase().split("\\s+");

        Set<String> wordSet1 = new HashSet<>(Arrays.asList(words1));
        Set<String> wordSet2 = new HashSet<>(Arrays.asList(words2));

        Set<String> intersection = new HashSet<>(wordSet1);
        intersection.retainAll(wordSet2);

        Set<String> union = new HashSet<>(wordSet1);
        union.addAll(wordSet2);

        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }

    // Check if two strings contain common words
    private boolean containsCommonWords(String text1, String text2) {
        if (text1 == null || text2 == null) return false;

        String[] words1 = text1.toLowerCase().split("\\s+");
        String[] words2 = text2.toLowerCase().split("\\s+");

        Set<String> wordSet1 = new HashSet<>(Arrays.asList(words1));
        Set<String> wordSet2 = new HashSet<>(Arrays.asList(words2));

        wordSet1.retainAll(wordSet2);
        return !wordSet1.isEmpty();
    }

    // Get recommendation explanation
    public String getRecommendationExplanation(Event baseEvent, Event recommendedEvent) {
        List<String> reasons = new ArrayList<>();

        if (baseEvent.getType() == recommendedEvent.getType()) {
            reasons.add("same category (" + baseEvent.getType().toString().toLowerCase().replace("_", " ") + ")");
        }

        if (baseEvent.getLocation() != null && recommendedEvent.getLocation() != null &&
                baseEvent.getLocation().equalsIgnoreCase(recommendedEvent.getLocation())) {
            reasons.add("same location");
        }

        if (baseEvent.getTags() != null && recommendedEvent.getTags() != null &&
                calculateTagsSimilarity(baseEvent.getTags(), recommendedEvent.getTags()) > 0.3) {
            reasons.add("similar interests");
        }

        if (reasons.isEmpty()) {
            return "Popular choice";
        }

        return "Recommended because of " + String.join(" and ", reasons);
    }
}