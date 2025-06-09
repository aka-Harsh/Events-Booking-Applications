import React, { useState } from 'react';
import { useApp } from '../../context/AppContext';
import { ArrowLeft } from 'lucide-react';
import api from '../../services/api';
import GlassCard from '../Common/GlassCard';
import Button3D from '../Common/Button3D';

const ReviewForm = ({ event, onBack }) => {
  const { user } = useApp();
  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState('');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  const handleReview = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');

    try {
      const reviewData = {
        userId: user.id,
        eventId: event.id,
        rating: parseInt(rating),
        comment: comment || ''
      };

      const response = await api.createReview(reviewData);

      if (response.success) {
        setMessage('Review submitted successfully!');
        setTimeout(() => onBack(), 2000);
      } else {
        setMessage(response.message || 'Review submission failed');
      }
    } catch (error) {
      console.error('Review submission error:', error);
      setMessage('Review submission failed: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="w-full">
      <div className="container mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8">
        <Button3D onClick={onBack} variant="secondary" className="mb-6 sm:mb-8">
          <ArrowLeft className="w-4 h-4 sm:w-5 sm:h-5 mr-2" />
          Back
        </Button3D>

        <div className="max-w-md mx-auto">
          <GlassCard className="p-6 sm:p-8">
            <h2 className="text-2xl sm:text-3xl font-bold text-white mb-2">Write Review</h2>
            <h3 className="text-lg sm:text-xl text-white/80 mb-6 sm:mb-8">{event.name}</h3>

            <form onSubmit={handleReview} className="space-y-4 sm:space-y-6">
              <div>
                <label className="block text-white font-semibold mb-3 text-sm sm:text-base">Rating</label>
                <div className="flex items-center space-x-2 mb-4">
                  {[1, 2, 3, 4, 5].map((star) => (
                    <button
                      key={star}
                      type="button"
                      onClick={() => setRating(star)}
                      className={`text-2xl sm:text-3xl transition-all duration-300 hover:scale-125 ${
                        star <= rating ? 'text-yellow-400' : 'text-gray-400'
                      }`}
                    >
                      ⭐
                    </button>
                  ))}
                </div>
                <p className="text-white/60 text-sm sm:text-base">
                  {rating === 5 ? 'Excellent!' : rating === 4 ? 'Good' : rating === 3 ? 'Average' : rating === 2 ? 'Poor' : 'Terrible'}
                </p>
              </div>

              <div>
                <label className="block text-white font-semibold mb-3 text-sm sm:text-base">Comment (Optional)</label>
                <textarea
                  value={comment}
                  onChange={(e) => setComment(e.target.value)}
                  rows="4"
                  className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white placeholder-white/50 focus:outline-none focus:border-blue-400 focus:bg-white/20 transition-all duration-300 resize-none text-sm sm:text-base"
                  placeholder="Share your experience..."
                />
              </div>

              {message && (
                <div className={`p-3 sm:p-4 rounded-2xl border text-sm sm:text-base ${
                  message.includes('successful') 
                    ? 'text-green-300 bg-green-500/20 border-green-400/30' 
                    : 'text-red-300 bg-red-500/20 border-red-400/30'
                }`}>
                  {message}
                </div>
              )}

              <Button3D 
                type="submit" 
                disabled={loading} 
                className="w-full"
              >
                {loading ? 'Submitting...' : 'Submit Review'}
              </Button3D>
            </form>
          </GlassCard>
        </div>
      </div>
    </div>
  );
};

export default ReviewForm;