import React, { useState, useEffect } from 'react';
import { useApp } from '../../context/AppContext';
import { ArrowLeft, Calendar, MapPin, Ticket, Users, QrCode, XCircle } from 'lucide-react';
import api from '../../services/api';
import GlassCard from '../Common/GlassCard';
import Button3D from '../Common/Button3D';

const Bookings = () => {
  const { user } = useApp();
  const [bookings, setBookings] = useState([]);
  const [selectedBooking, setSelectedBooking] = useState(null);

  useEffect(() => {
    loadBookings();
  }, []);

  const loadBookings = async () => {
    try {
      const data = user.role === 'ADMIN' 
        ? await api.getAllBookings()
        : await api.getUserBookings(user.id);
      setBookings(data);
    } catch (error) {
      console.error('Error loading bookings:', error);
    }
  };

  const handleCancelBooking = async (bookingId) => {
    if (window.confirm('Are you sure you want to cancel this booking?')) {
      try {
        await api.cancelBooking(bookingId);
        loadBookings();
      } catch (error) {
        console.error('Error canceling booking:', error);
      }
    }
  };

  const showQRCode = async (booking) => {
    try {
      const qrData = await api.getBookingQR(booking.id);
      setSelectedBooking({ ...booking, qrImage: qrData.qrCodeImage });
    } catch (error) {
      console.error('Error loading QR code:', error);
    }
  };

  if (selectedBooking) {
    return (
      <div className="w-full">
        <div className="container mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8">
          <Button3D onClick={() => setSelectedBooking(null)} variant="secondary" className="mb-6 sm:mb-8">
            <ArrowLeft className="w-4 h-4 sm:w-5 sm:h-5 mr-2" />
            Back to Bookings
          </Button3D>

          <div className="max-w-md mx-auto">
            <GlassCard className="p-6 sm:p-8 text-center">
              <div className="w-16 h-16 sm:w-20 sm:h-20 bg-gradient-to-r from-green-500 to-emerald-600 rounded-full flex items-center justify-center mx-auto mb-4 sm:mb-6">
                <Ticket className="w-8 h-8 sm:w-10 sm:h-10 text-white" />
              </div>
              
              <h2 className="text-2xl sm:text-3xl font-bold text-white mb-2">Your Ticket</h2>
              <h3 className="text-lg sm:text-xl text-white/80 mb-6 sm:mb-8">{selectedBooking.event?.name}</h3>
              
              <div className="space-y-3 text-white/80 mb-6 sm:mb-8 text-sm sm:text-base">
                <div className="flex justify-between">
                  <span>Booking Reference:</span>
                  <span className="font-mono text-white">{selectedBooking.bookingReference}</span>
                </div>
                <div className="flex justify-between">
                  <span>Tickets:</span>
                  <span className="text-white">{selectedBooking.ticketsBooked}</span>
                </div>
                <div className="flex justify-between">
                  <span>Total Amount:</span>
                  <span className="text-white font-bold">₹{selectedBooking.totalAmount}</span>
                </div>
                <div className="flex justify-between">
                  <span>Status:</span>
                  <span className={`px-3 py-1 rounded-full text-sm ${
                    selectedBooking.status === 'CONFIRMED' ? 'bg-green-500/20 text-green-300 border border-green-400/30' :
                    selectedBooking.status === 'CANCELLED' ? 'bg-red-500/20 text-red-300 border border-red-400/30' :
                    'bg-blue-500/20 text-blue-300 border border-blue-400/30'
                  }`}>
                    {selectedBooking.status}
                  </span>
                </div>
              </div>

              {selectedBooking.qrImage && (
                <div className="mb-6 sm:mb-8">
                  <h4 className="text-lg sm:text-xl font-bold text-white mb-4">QR Code</h4>
                  <div className="bg-white p-4 rounded-2xl mx-auto inline-block">
                    <img 
                      src={`data:image/png;base64,${selectedBooking.qrImage}`}
                      alt="QR Code"
                      className="w-40 h-40 sm:w-48 sm:h-48 mx-auto"
                    />
                  </div>
                  <p className="text-white/60 text-sm mt-4">
                    Show this QR code at the venue for entry
                  </p>
                </div>
              )}
            </GlassCard>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="w-full">
      <div className="container mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8">
        <h2 className="text-3xl sm:text-4xl font-bold bg-gradient-to-r from-white to-blue-200 bg-clip-text text-transparent mb-6 sm:mb-8">
          {user.role === 'ADMIN' ? 'All Bookings' : 'My Bookings'}
        </h2>

        <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-4 sm:gap-6">
          {bookings.map(booking => (
            <GlassCard key={booking.id} className="p-4 sm:p-6">
              <div className="mb-4">
                <h3 className="text-lg sm:text-xl font-bold text-white mb-2 line-clamp-2">{booking.event?.name}</h3>
                
                <div className="space-y-2 text-white/80 text-sm sm:text-base">
                  <div className="flex items-center space-x-2">
                    <Calendar className="w-4 h-4 text-blue-400 flex-shrink-0" />
                    <span>{booking.event?.date} at {booking.event?.time}</span>
                  </div>
                  <div className="flex items-start space-x-2">
                    <MapPin className="w-4 h-4 text-green-400 flex-shrink-0 mt-0.5" />
                    <span className="line-clamp-2">{booking.event?.location}</span>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Ticket className="w-4 h-4 text-purple-400 flex-shrink-0" />
                    <span>{booking.ticketsBooked} ticket{booking.ticketsBooked > 1 ? 's' : ''}</span>
                  </div>
                  <div className="flex items-center space-x-2">
                    <span className="text-xl">💰</span>
                    <span className="font-bold">₹{booking.totalAmount}</span>
                  </div>
                  <div className="flex items-center space-x-2">
                    <span className="text-lg">📋</span>
                    <span className="font-mono text-xs sm:text-sm">{booking.bookingReference}</span>
                  </div>
                  {user.role === 'ADMIN' && (
                    <div className="flex items-center space-x-2">
                      <Users className="w-4 h-4 text-yellow-400 flex-shrink-0" />
                      <span className="text-sm">{booking.user?.name} ({booking.user?.email})</span>
                    </div>
                  )}
                </div>

                <div className="mt-4">
                  <span className={`inline-block px-3 py-1 rounded-full text-sm font-semibold ${
                    booking.status === 'CONFIRMED' ? 'bg-green-500/20 text-green-300 border border-green-400/30' :
                    booking.status === 'CANCELLED' ? 'bg-red-500/20 text-red-300 border border-red-400/30' :
                    'bg-blue-500/20 text-blue-300 border border-blue-400/30'
                  }`}>
                    {booking.status}
                  </span>
                </div>
              </div>

              {booking.status === 'CONFIRMED' && (
                <div className="flex flex-col sm:flex-row gap-2 mt-4 sm:mt-6">
                  <Button3D
                    onClick={() => showQRCode(booking)}
                    className="flex-1 text-sm"
                  >
                    <QrCode className="w-4 h-4 mr-2" />
                    Show QR
                  </Button3D>
                  {user.role === 'USER' && (
                    <Button3D
                      onClick={() => handleCancelBooking(booking.id)}
                      variant="danger"
                      className="text-sm"
                    >
                      <XCircle className="w-4 h-4 mr-2" />
                      Cancel
                    </Button3D>
                  )}
                </div>
              )}
            </GlassCard>
          ))}
        </div>

        {bookings.length === 0 && (
          <div className="text-center py-12 sm:py-16">
            <div className="text-4xl sm:text-6xl mb-4">🎫</div>
            <p className="text-white/60 text-lg sm:text-xl">No bookings found.</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Bookings;