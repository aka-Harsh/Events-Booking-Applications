import React, { useState, useEffect } from 'react';
import { useApp } from '../../context/AppContext';
import { ArrowLeft, Calendar, MapPin, Users, Star, ShoppingCart } from 'lucide-react';
import api from '../../services/api';
import BookingForm from './BookingForm';
import ReviewForm from '../Reviews/ReviewForm';
import GlassCard from '../Common/GlassCard';
import Button3D from '../Common/Button3D';

// Event Images Mapping
const eventImages = {
  'Avengers: Endgame': 'https://images.unsplash.com/photo-1635805737707-575885ab0820?w=800&h=600&fit=crop',
  'Weekend Trek to Munnar': 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop',
  'AR Rahman Live Concert': 'https://images.unsplash.com/photo-1501281668745-f7f57925c3b4?w=800&h=600&fit=crop',
  'Digital Marketing Masterclass': 'https://images.unsplash.com/photo-1552664730-d307ca884978?w=800&h=600&fit=crop',
  'Baahubali 2: The Conclusion': 'https://images.unsplash.com/photo-1489599162163-3fb64a5b2e12?w=800&h=600&fit=crop',
  'Goa Beach Party Weekend': 'https://images.unsplash.com/photo-1519046904884-53103b34b206?w=800&h=600&fit=crop',
  'Stand-up Comedy Night': 'https://images.unsplash.com/photo-1527224857830-43a7acc85260?w=800&h=600&fit=crop',
  'Web Development Bootcamp': 'https://images.unsplash.com/photo-1517077304055-6e89abbf09b0?w=800&h=600&fit=crop'
};

const getEventImage = (eventName) => {
  return eventImages[eventName] || 'https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=800&h=600&fit=crop';
};

const EventDetails = ({ event, onBack }) => {
  const { user } = useApp();
  const [reviews, setReviews] = useState([]);
  const [recommendations, setRecommendations] = useState([]);
  const [showBookingForm, setShowBookingForm] = useState(false);
  const [showReviewForm, setShowReviewForm] = useState(false);

  useEffect(() => {
    loadReviews();
    loadRecommendations();
  }, [event.id]);

  const loadReviews = async () => {
    try {
      const data = await api.getEventReviews(event.id);
      setReviews(data);
    } catch (error) {
      console.error('Error loading reviews:', error);
    }
  };

  const loadRecommendations = async () => {
    try {
      const data = await api.getRecommendations(event.id);
      setRecommendations(data.recommendations || []);
    } catch (error) {
      console.error('Error loading recommendations:', error);
    }
  };

  if (showBookingForm) {
    return <BookingForm event={event} onBack={() => setShowBookingForm(false)} />;
  }

  if (showReviewForm) {
    return <ReviewForm event={event} onBack={() => setShowReviewForm(false)} />;
  }

  const eventImage = getEventImage(event.name);

  return (
    <div className="w-full">
      <div className="container mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8">
        <Button3D onClick={onBack} variant="secondary" className="mb-6 sm:mb-8">
          <ArrowLeft className="w-4 h-4 sm:w-5 sm:h-5 mr-2" />
          Back to Events
        </Button3D>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 sm:gap-8">
          {/* Main Event Details */}
          <div className="lg:col-span-2">
            <GlassCard className="overflow-hidden mb-6 sm:mb-8">
              <div className="relative h-64 sm:h-80 lg:h-96">
                <img 
                  src={eventImage} 
                  alt={event.name}
                  className="w-full h-full object-cover"
                />
                <div className="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent" />
                <div className="absolute bottom-4 sm:bottom-6 left-4 sm:left-6 right-4 sm:right-6">
                  <h1 className="text-3xl sm:text-4xl lg:text-5xl font-bold text-white mb-2">{event.name}</h1>
                  <p className="text-white/80 text-base sm:text-lg lg:text-xl">{event.description}</p>
                </div>
              </div>
              
              <div className="p-4 sm:p-6 lg:p-8">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6 sm:gap-8">
                  <div>
                    <h3 className="text-xl sm:text-2xl font-bold text-white mb-4">Event Details</h3>
                    <div className="space-y-3 text-white/80">
                      <div className="flex items-center space-x-3">
                        <Calendar className="w-5 h-5 text-blue-400 flex-shrink-0" />
                        <span className="text-sm sm:text-base">{event.date} at {event.time}</span>
                      </div>
                      <div className="flex items-start space-x-3">
                        <MapPin className="w-5 h-5 text-green-400 flex-shrink-0 mt-0.5" />
                        <span className="text-sm sm:text-base">{event.location}</span>
                      </div>
                      <div className="flex items-center space-x-3">
                        <Users className="w-5 h-5 text-purple-400 flex-shrink-0" />
                        <span className="text-sm sm:text-base">{event.type.replace('_', ' ')}</span>
                      </div>
                      {event.tags && (
                        <div className="flex flex-wrap gap-2 mt-4">
                          {event.tags.split(',').map((tag, index) => (
                            <span key={index} className="px-3 py-1 bg-blue-500/20 text-blue-300 rounded-full text-xs sm:text-sm border border-blue-400/30">
                              #{tag.trim()}
                            </span>
                          ))}
                        </div>
                      )}
                    </div>
                  </div>

                  <div>
                    <h3 className="text-xl sm:text-2xl font-bold text-white mb-4">Booking Info</h3>
                    <div className="space-y-4">
                      <div className="bg-gradient-to-r from-green-500/20 to-emerald-500/20 p-4 rounded-2xl border border-green-400/30">
                        <div className="text-2xl sm:text-3xl font-bold text-white">₹{event.currentPrice}</div>
                        {event.currentPrice > event.basePrice && (
                          <div className="text-red-400 text-sm">
                            Price increased by ₹{(event.currentPrice - event.basePrice).toFixed(2)}
                          </div>
                        )}
                      </div>
                      
                      <div className="bg-white/10 p-4 rounded-2xl border border-white/20">
                        <div className="flex justify-between items-center mb-2">
                          <span className="text-white/70 text-sm sm:text-base">Available Tickets</span>
                          <span className="text-white font-bold">{event.availableTickets} / {event.totalTickets}</span>
                        </div>
                        <div className="w-full bg-white/20 rounded-full h-3">
                          <div 
                            className="bg-gradient-to-r from-blue-500 to-purple-600 h-3 rounded-full transition-all duration-500"
                            style={{ width: `${((event.totalTickets - event.availableTickets) / event.totalTickets) * 100}%` }}
                          />
                        </div>
                        <div className="text-xs text-white/60 mt-1">
                          {(((event.totalTickets - event.availableTickets) / event.totalTickets) * 100).toFixed(1)}% sold
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                {user?.role === 'USER' && event.availableTickets > 0 && (
                  <div className="mt-6 sm:mt-8 flex flex-col sm:flex-row gap-4">
                    <Button3D onClick={() => setShowBookingForm(true)} variant="success" className="flex-1">
                      <ShoppingCart className="w-4 h-4 sm:w-5 sm:h-5 mr-2" />
                      Book Tickets
                    </Button3D>
                    <Button3D onClick={() => setShowReviewForm(true)} className="flex-1">
                      <Star className="w-4 h-4 sm:w-5 sm:h-5 mr-2" />
                      Write Review
                    </Button3D>
                  </div>
                )}
              </div>
            </GlassCard>

            {/* Reviews Section */}
            <GlassCard className="p-4 sm:p-6 lg:p-8">
              <h2 className="text-2xl sm:text-3xl font-bold text-white mb-4 sm:mb-6">Reviews ({reviews.length})</h2>
              {reviews.length > 0 ? (
                <div className="space-y-4 sm:space-y-6">
                  {reviews.slice(0, 5).map(review => (
                    <div key={review.id} className="border-b border-white/20 pb-4 sm:pb-6 last:border-b-0">
                      <div className="flex justify-between items-start mb-3">
                        <div>
                          <span className="text-white font-bold text-sm sm:text-base">{review.user?.name}</span>
                          <div className="flex items-center mt-1">
                            {[...Array(5)].map((_, i) => (
                              <Star 
                                key={i} 
                                className={`w-4 h-4 ${i < review.rating ? 'text-yellow-400 fill-current' : 'text-gray-400'}`} 
                              />
                            ))}
                          </div>
                        </div>
                        <span className="text-white/60 text-xs sm:text-sm">
                          {new Date(review.reviewDate).toLocaleDateString()}
                        </span>
                      </div>
                      <p className="text-white/80 text-sm sm:text-base">{review.comment}</p>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="text-center py-6 sm:py-8">
                  <Star className="w-12 h-12 sm:w-16 sm:h-16 text-white/30 mx-auto mb-4" />
                  <p className="text-white/60 text-base sm:text-lg">No reviews yet. Be the first to review!</p>
                </div>
              )}
            </GlassCard>
          </div>

          {/* Sidebar - Recommendations */}
          <div className="lg:col-span-1">
            {recommendations.length > 0 && (
              <GlassCard className="p-4 sm:p-6">
                <h2 className="text-xl sm:text-2xl font-bold text-white mb-4 sm:mb-6">Recommended Events</h2>
                <div className="space-y-4">
                  {recommendations.map(rec => (
                    <div key={rec.id} className="bg-white/10 rounded-2xl p-4 border border-white/20 hover:bg-white/20 transition-all duration-300">
                      <h3 className="text-white font-bold mb-2 line-clamp-2 text-sm sm:text-base">{rec.name}</h3>
                      <div className="space-y-2 text-xs sm:text-sm text-white/70">
                        <div className="flex items-center space-x-2">
                          <Calendar className="w-4 h-4 flex-shrink-0" />
                          <span>{rec.date}</span>
                        </div>
                        <div className="flex items-start space-x-2">
                          <MapPin className="w-4 h-4 flex-shrink-0 mt-0.5" />
                          <span className="line-clamp-2">{rec.location}</span>
                        </div>
                        <div className="flex justify-between items-center mt-3 pt-2 border-t border-white/20">
                          <span className="text-green-400 font-bold">₹{rec.currentPrice}</span>
                          <span className="text-xs text-white/60">
                            {rec.availableTickets} tickets left
                          </span>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </GlassCard>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default EventDetails;