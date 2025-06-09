import React, { useState } from 'react';
import { useApp } from '../../context/AppContext';
import { ArrowLeft } from 'lucide-react';
import api from '../../services/api';
import GlassCard from '../Common/GlassCard';
import Button3D from '../Common/Button3D';

const BookingForm = ({ event, onBack }) => {
  const { user } = useApp();
  const [tickets, setTickets] = useState(1);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  const totalAmount = tickets * event.currentPrice;

  const handleBooking = async (e) => {
    e.preventDefault();
    setLoading(true);
    
    try {
      const response = await api.createBooking({
        userId: user.id,
        eventId: event.id,
        ticketsRequested: tickets
      });

      if (response.success) {
        setMessage('Booking successful! Check your bookings to see the QR code.');
        setTimeout(() => onBack(), 2000);
      } else {
        setMessage(response.message || 'Booking failed');
      }
    } catch (error) {
      setMessage('Booking failed');
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
            <h2 className="text-2xl sm:text-3xl font-bold text-white mb-2">Book Tickets</h2>
            <h3 className="text-lg sm:text-xl text-white/80 mb-6 sm:mb-8">{event.name}</h3>

            <form onSubmit={handleBooking} className="space-y-4 sm:space-y-6">
              <div>
                <label className="block text-white font-semibold mb-3 text-sm sm:text-base">Number of Tickets</label>
                <select
                  value={tickets}
                  onChange={(e) => setTickets(parseInt(e.target.value))}
                  className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white focus:outline-none focus:border-blue-400 focus:bg-white/20 transition-all duration-300 text-sm sm:text-base"
                >
                  {[...Array(Math.min(10, event.availableTickets))].map((_, i) => (
                    <option key={i + 1} value={i + 1} className="bg-gray-800">
                      {i + 1} Ticket{i > 0 ? 's' : ''}
                    </option>
                  ))}
                </select>
              </div>

              <GlassCard className="p-4 sm:p-6 bg-gradient-to-r from-blue-500/20 to-purple-500/20">
                <div className="space-y-3">
                  <div className="flex justify-between text-white/80 text-sm sm:text-base">
                    <span>Price per ticket:</span>
                    <span>₹{event.currentPrice}</span>
                  </div>
                  <div className="flex justify-between text-white/80 text-sm sm:text-base">
                    <span>Number of tickets:</span>
                    <span>{tickets}</span>
                  </div>
                  <div className="border-t border-white/20 pt-3">
                    <div className="flex justify-between text-white font-bold text-lg sm:text-xl">
                      <span>Total Amount:</span>
                      <span>₹{totalAmount}</span>
                    </div>
                  </div>
                </div>
              </GlassCard>

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
                variant="success" 
                className="w-full"
              >
                {loading ? 'Processing...' : `Book ${tickets} Ticket${tickets > 1 ? 's' : ''} - ₹${totalAmount}`}
              </Button3D>
            </form>
          </GlassCard>
        </div>
      </div>
    </div>
  );
};

export default BookingForm;