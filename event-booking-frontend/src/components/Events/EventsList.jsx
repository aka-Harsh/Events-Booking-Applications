import React, { useState, useEffect } from 'react';
import { useApp } from '../../context/AppContext';
import { Search, Filter, Plus, Eye, Trash2, Calendar, MapPin, Users } from 'lucide-react';
import api from '../../services/api';
import EventDetails from './EventDetails';
import CreateEventForm from './CreateEventForm';
import GlassCard from '../Common/GlassCard';
import Button3D from '../Common/Button3D';

// Event Images Mapping
const eventImages = {
  'Avengers: Endgame': 'https://images.unsplash.com/photo-1635805737707-575885ab0820?w=400&h=300&fit=crop',
  'Weekend Trek to Munnar': 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=300&fit=crop',
  'AR Rahman Live Concert': 'https://images.unsplash.com/photo-1501281668745-f7f57925c3b4?w=400&h=300&fit=crop',
  'Digital Marketing Masterclass': 'https://images.unsplash.com/photo-1552664730-d307ca884978?w=400&h=300&fit=crop',
  'Baahubali 2: The Conclusion': 'https://images.unsplash.com/photo-1489599162163-3fb64a5b2e12?w=400&h=300&fit=crop',
  'Goa Beach Party Weekend': 'https://images.unsplash.com/photo-1519046904884-53103b34b206?w=400&h=300&fit=crop',
  'Stand-up Comedy Night': 'https://images.unsplash.com/photo-1527224857830-43a7acc85260?w=400&h=300&fit=crop',
  'Web Development Bootcamp': 'https://images.unsplash.com/photo-1517077304055-6e89abbf09b0?w=400&h=300&fit=crop'
};

const getEventImage = (eventName) => {
  return eventImages[eventName] || 'https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=400&h=300&fit=crop';
};

// Event Card Component
const EventCard = ({ event, onSelect, onDelete, isAdmin }) => {
  const eventImage = getEventImage(event.name);
  
  return (
    <GlassCard className="overflow-hidden group">
      <div className="relative h-48 sm:h-56 lg:h-64 overflow-hidden">
        <img 
          src={eventImage} 
          alt={event.name}
          className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
        />
        <div className="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent" />
        <div className="absolute top-4 right-4">
          <span className="px-2 sm:px-3 py-1 bg-white/20 backdrop-blur-sm text-white text-xs rounded-full border border-white/30">
            {event.type.replace('_', ' ')}
          </span>
        </div>
        <div className="absolute bottom-4 left-4 right-4">
          <h3 className="text-white font-bold text-lg sm:text-xl mb-1 line-clamp-1">{event.name}</h3>
          <p className="text-white/80 text-sm line-clamp-2">{event.description}</p>
        </div>
      </div>
      
      <div className="p-4 sm:p-6">
        <div className="flex items-center justify-between text-sm text-white/70 mb-4">
          <div className="flex items-center space-x-1">
            <Calendar className="w-4 h-4" />
            <span className="text-xs sm:text-sm">{event.date}</span>
          </div>
          <div className="flex items-center space-x-1">
            <MapPin className="w-4 h-4" />
            <span className="line-clamp-1 text-xs sm:text-sm max-w-24 sm:max-w-none">{event.location}</span>
          </div>
        </div>

        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center space-x-1 text-white/70">
            <Users className="w-4 h-4" />
            <span className="text-sm">{event.availableTickets} / {event.totalTickets}</span>
          </div>
          <div className="text-right">
            <div className="text-xl sm:text-2xl font-bold text-white">₹{event.currentPrice}</div>
            {event.currentPrice > event.basePrice && (
              <div className="text-xs text-red-400">
                ↑ +₹{(event.currentPrice - event.basePrice).toFixed(2)}
              </div>
            )}
          </div>
        </div>

        <div className="flex gap-2">
          <Button3D onClick={() => onSelect(event)} className="flex-1 text-sm">
            <Eye className="w-4 h-4 mr-2" />
            <span className="hidden sm:inline">View Details</span>
            <span className="sm:hidden">View</span>
          </Button3D>
          {isAdmin && (
            <Button3D 
              onClick={() => onDelete(event.id)} 
              variant="danger" 
              className="text-sm p-2 sm:p-3"
            >
              <Trash2 className="w-4 h-4" />
            </Button3D>
          )}
        </div>
      </div>
    </GlassCard>
  );
};

const EventsList = () => {
  const { user } = useApp();
  const [events, setEvents] = useState([]);
  const [filteredEvents, setFilteredEvents] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedType, setSelectedType] = useState('ALL');
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [showCreateForm, setShowCreateForm] = useState(false);

  useEffect(() => {
    loadEvents();
  }, []);

  useEffect(() => {
    filterEvents();
  }, [events, searchQuery, selectedType]);

  const loadEvents = async () => {
    try {
      const data = await api.getAllEvents();
      setEvents(data);
    } catch (error) {
      console.error('Error loading events:', error);
    }
  };

  const filterEvents = () => {
    let filtered = events;

    if (searchQuery) {
      filtered = filtered.filter(event =>
        event.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        event.description.toLowerCase().includes(searchQuery.toLowerCase())
      );
    }

    if (selectedType !== 'ALL') {
      filtered = filtered.filter(event => event.type === selectedType);
    }

    setFilteredEvents(filtered);
  };

  const handleDeleteEvent = async (eventId) => {
    if (window.confirm('Are you sure you want to delete this event?')) {
      try {
        await api.deleteEvent(eventId);
        loadEvents();
      } catch (error) {
        console.error('Error deleting event:', error);
      }
    }
  };

  if (selectedEvent) {
    return <EventDetails event={selectedEvent} onBack={() => setSelectedEvent(null)} />;
  }

  if (showCreateForm) {
    return <CreateEventForm onBack={() => setShowCreateForm(false)} onCreate={loadEvents} />;
  }

  return (
    <div className="w-full">
      <div className="container mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8">
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 sm:mb-8 gap-4">
          <h2 className="text-3xl sm:text-4xl font-bold bg-gradient-to-r from-white to-blue-200 bg-clip-text text-transparent">
            {user?.role === 'ADMIN' ? 'Manage Events' : 'Discover Events'}
          </h2>
          {user?.role === 'ADMIN' && (
            <Button3D onClick={() => setShowCreateForm(true)} variant="success" className="w-full sm:w-auto">
              <Plus className="w-4 h-4 sm:w-5 sm:h-5 mr-2" />
              Create Event
            </Button3D>
          )}
        </div>

        {/* Filters */}
        <div className="mb-6 sm:mb-8 flex flex-col sm:flex-row gap-4">
          <div className="relative flex-1">
            <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 text-white/50 w-5 h-5" />
            <input
              type="text"
              placeholder="Search events..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-12 pr-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white placeholder-white/50 focus:outline-none focus:border-blue-400 focus:bg-white/20 transition-all duration-300"
            />
          </div>
          <div className="relative w-full sm:w-auto sm:min-w-48">
            <Filter className="absolute left-4 top-1/2 transform -translate-y-1/2 text-white/50 w-5 h-5" />
            <select
              value={selectedType}
              onChange={(e) => setSelectedType(e.target.value)}
              className="w-full pl-12 pr-8 py-3 rounded-2xl bg-white/10 border border-white/20 text-white focus:outline-none focus:border-blue-400 focus:bg-white/20 transition-all duration-300 appearance-none"
            >
              <option value="ALL" className="bg-gray-800">All Types</option>
              <option value="MOVIE" className="bg-gray-800">Movies</option>
              <option value="WEEKEND_PLAN" className="bg-gray-800">Weekend Plans</option>
              <option value="LIVE_SHOW" className="bg-gray-800">Live Shows</option>
              <option value="WORKSHOP" className="bg-gray-800">Workshops</option>
            </select>
          </div>
        </div>

        {/* Events Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4 sm:gap-6 lg:gap-8">
          {filteredEvents.map(event => (
            <EventCard 
              key={event.id} 
              event={event} 
              onSelect={setSelectedEvent}
              onDelete={handleDeleteEvent}
              isAdmin={user?.role === 'ADMIN'}
            />
          ))}
        </div>

        {filteredEvents.length === 0 && (
          <div className="text-center py-12 sm:py-16">
            <div className="text-4xl sm:text-6xl mb-4">🎭</div>
            <p className="text-white/60 text-lg sm:text-xl">No events found matching your criteria.</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default EventsList;