import React, { useState, useEffect } from 'react';
import { useApp } from '../../context/AppContext';
import { Star } from 'lucide-react';
import api from '../../services/api';
import GlassCard from '../Common/GlassCard';

const Reviews = () => {
  const { user } = useApp();
  const [reviews, setReviews] = useState([]);

  useEffect(() => {
    loadReviews();
  }, []);

  const loadReviews = async () => {
    try {
      const data = user.role === 'ADMIN' 
        ? await api.getAllReviews()
        : await api.getUserReviews(user.id);
      setReviews(data);
    } catch (error) {
      console.error('Error loading reviews:', error);
    }
  };

  return (
    <div className="w-full">
      <div className="container mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8">
        <h2 className="text-3xl sm:text-4xl font-bold bg-gradient-to-r from-white to-yellow-200 bg-clip-text text-transparent mb-6 sm:mb-8">
          {user.role === 'ADMIN' ? 'All Reviews' : 'My Reviews'}
        </h2>

        <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-4 sm:gap-6">
          {reviews.map(review => (
            <GlassCard key={review.id} className="p-4 sm:p-6">
              <div className="flex justify-between items-start mb-4">
                <div className="flex-1 pr-4">
                  <h3 className="text-lg sm:text-xl font-bold text-white mb-1 line-clamp-2">{review.event?.name}</h3>
                  {user.role === 'ADMIN' && (
                    <p className="text-white/60 mb-3 text-sm sm:text-base">By: {review.user?.name}</p>
                  )}
                </div>
                <div className="text-right flex-shrink-0">
                  <div className="flex items-center mb-1">
                    {[...Array(5)].map((_, i) => (
                      <Star 
                        key={i} 
                        className={`w-4 h-4 sm:w-5 sm:h-5 ${
                          i < review.rating ? 'text-yellow-400 fill-current' : 'text-gray-400'
                        }`} 
                      />
                    ))}
                  </div>
                  <div className="text-xs text-white/60">
                    {new Date(review.reviewDate).toLocaleDateString()}
                  </div>
                </div>
              </div>
              
              {review.comment && (
                <p className="text-white/80 leading-relaxed text-sm sm:text-base">{review.comment}</p>
              )}
            </GlassCard>
          ))}
        </div>

        {reviews.length === 0 && (
          <div className="text-center py-12 sm:py-16">
            <div className="text-4xl sm:text-6xl mb-4">⭐</div>
            <p className="text-white/60 text-lg sm:text-xl">No reviews found.</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Reviews;