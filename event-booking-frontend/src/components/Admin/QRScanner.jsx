import React, { useState } from 'react';
import { QrCode, CheckCircle, XCircle } from 'lucide-react';
import api from '../../services/api';
import GlassCard from '../Common/GlassCard';
import Button3D from '../Common/Button3D';

const QRScanner = () => {
  const [qrCode, setQrCode] = useState('');
  const [bookingReference, setBookingReference] = useState('');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('qr');

  const handleScan = async (e) => {
    e.preventDefault();
    if (!qrCode.trim()) return;
    
    setLoading(true);
    setResult(null);
    
    try {
      const response = await api.verifyQR(qrCode);
      setResult(response);
    } catch (error) {
      setResult({ 
        valid: false, 
        message: `QR verification failed: ${error.message}` 
      });
    } finally {
      setLoading(false);
    }
  };

  const handleBookingReferenceCheck = async (e) => {
    e.preventDefault();
    if (!bookingReference.trim()) return;
    
    setLoading(true);
    setResult(null);
    
    try {
      const response = await api.getBookingByReference(bookingReference);
      
      if (response.success && response.booking) {
        setResult({
          valid: response.booking.status === 'CONFIRMED',
          booking: response.booking,
          message: response.booking.status === 'CONFIRMED' 
            ? 'Valid booking - Entry allowed' 
            : `Booking status: ${response.booking.status}`
        });
      } else {
        setResult({ valid: false, message: 'Booking reference not found' });
      }
    } catch (error) {
      setResult({ 
        valid: false, 
        message: `Booking reference verification failed: ${error.message}` 
      });
    } finally {
      setLoading(false);
    }
  };

  const switchTab = (tab) => {
    setActiveTab(tab);
    setResult(null);
    setQrCode('');
    setBookingReference('');
  };

  return (
    <div className="w-full">
      <div className="container mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8">
        <h2 className="text-3xl sm:text-4xl font-bold bg-gradient-to-r from-white to-green-200 bg-clip-text text-transparent mb-6 sm:mb-8">
          Entry Verification System
        </h2>

        {/* Tab Navigation */}
        <div className="mb-6 sm:mb-8">
          <div className="flex space-x-1 sm:space-x-2 p-1 bg-white/10 rounded-2xl backdrop-blur-sm border border-white/20 max-w-md mx-auto">
            <button
              onClick={() => switchTab('qr')}
              className={`flex-1 px-3 sm:px-4 py-2 sm:py-3 rounded-xl font-semibold transition-all duration-300 text-sm sm:text-base ${
                activeTab === 'qr'
                  ? 'bg-gradient-to-r from-blue-500 to-purple-600 text-white shadow-lg'
                  : 'text-white/70 hover:text-white hover:bg-white/10'
              }`}
            >
              QR Code
            </button>
            <button
              onClick={() => switchTab('reference')}
              className={`flex-1 px-3 sm:px-4 py-2 sm:py-3 rounded-xl font-semibold transition-all duration-300 text-sm sm:text-base ${
                activeTab === 'reference'
                  ? 'bg-gradient-to-r from-blue-500 to-purple-600 text-white shadow-lg'
                  : 'text-white/70 hover:text-white hover:bg-white/10'
              }`}
            >
              Reference
            </button>
          </div>
        </div>

        <div className="max-w-md mx-auto">
          <GlassCard className="p-6 sm:p-8">
            {activeTab === 'qr' ? (
              <div>
                <div className="text-center mb-6">
                  <div className="w-12 h-12 sm:w-16 sm:h-16 bg-gradient-to-r from-blue-500 to-purple-600 rounded-full flex items-center justify-center mx-auto mb-4">
                    <QrCode className="w-6 h-6 sm:w-8 sm:h-8 text-white" />
                  </div>
                  <h3 className="text-xl sm:text-2xl font-bold text-white">QR Code Verification</h3>
                </div>
                
                {/* Sample QR Code Format */}
                <div className="mb-6 p-4 bg-yellow-500/20 border border-yellow-400/30 rounded-2xl">
                  <p className="text-xs sm:text-sm text-yellow-200">
                    <strong>Expected QR Format:</strong><br/>
                    BOOKING_ID_USER_X_EVENT_Y_TICKETS_Z_REF_BKXXXXXXXXXX
                  </p>
                </div>
                
                <form onSubmit={handleScan} className="space-y-4 sm:space-y-6">
                  <div>
                    <label className="block text-white font-semibold mb-2 text-sm sm:text-base">
                      Enter QR Code Data
                    </label>
                    <textarea
                      value={qrCode}
                      onChange={(e) => setQrCode(e.target.value)}
                      rows="4"
                      className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white placeholder-white/50 focus:outline-none focus:border-blue-400 focus:bg-white/20 transition-all duration-300 resize-none text-sm sm:text-base"
                      placeholder="Paste QR code data here..."
                    />
                  </div>

                  <Button3D
                    type="submit"
                    disabled={loading || !qrCode.trim()}
                    className="w-full"
                  >
                    {loading ? 'Verifying...' : 'Verify QR Code'}
                  </Button3D>
                </form>
              </div>
            ) : (
              <div>
                <div className="text-center mb-6">
                  <div className="w-12 h-12 sm:w-16 sm:h-16 bg-gradient-to-r from-green-500 to-emerald-600 rounded-full flex items-center justify-center mx-auto mb-4">
                    <span className="text-lg sm:text-2xl">📋</span>
                  </div>
                  <h3 className="text-xl sm:text-2xl font-bold text-white">Booking Reference</h3>
                </div>
                
                <form onSubmit={handleBookingReferenceCheck} className="space-y-4 sm:space-y-6">
                  <div>
                    <label className="block text-white font-semibold mb-2 text-sm sm:text-base">
                      Booking Reference Number
                    </label>
                    <input
                      type="text"
                      value={bookingReference}
                      onChange={(e) => setBookingReference(e.target.value.toUpperCase())}
                      className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white placeholder-white/50 focus:outline-none focus:border-green-400 focus:bg-white/20 transition-all duration-300 uppercase font-mono text-sm sm:text-base"
                      placeholder="e.g., BK1234567890"
                    />
                    <p className="text-xs text-white/60 mt-2">
                      Enter the booking reference number (starts with BK)
                    </p>
                  </div>

                  <Button3D
                    type="submit"
                    disabled={loading || !bookingReference.trim()}
                    variant="success"
                    className="w-full"
                  >
                    {loading ? 'Checking...' : 'Verify Booking'}
                  </Button3D>
                </form>
              </div>
            )}

            {/* Verification Result */}
            {result && (
              <div className={`mt-6 sm:mt-8 p-4 sm:p-6 rounded-2xl border ${
                result.valid 
                  ? 'bg-green-500/20 border-green-400/30' 
                  : 'bg-red-500/20 border-red-400/30'
              }`}>
                <div className={`font-bold text-lg sm:text-xl mb-3 ${
                  result.valid ? 'text-green-300' : 'text-red-300'
                }`}>
                  {result.valid ? (
                    <div className="flex items-center">
                      <CheckCircle className="w-5 h-5 sm:w-6 sm:h-6 mr-2" />
                      VALID ENTRY
                    </div>
                  ) : (
                    <div className="flex items-center">
                      <XCircle className="w-5 h-5 sm:w-6 sm:h-6 mr-2" />
                      INVALID ENTRY
                    </div>
                  )}
                </div>
                <p className={`mb-4 text-sm sm:text-base ${result.valid ? 'text-green-200' : 'text-red-200'}`}>
                  {result.message}
                </p>
                
                {result.booking && (
                  <div className="bg-white/10 p-3 sm:p-4 rounded-xl border border-white/20">
                    <h4 className="font-bold text-white mb-3 text-sm sm:text-base">Booking Details:</h4>
                    <div className="space-y-2 text-xs sm:text-sm text-white/80">
                      <div className="flex justify-between">
                        <span>Event:</span>
                        <span className="text-white">{result.booking.event?.name}</span>
                      </div>
                      <div className="flex justify-between">
                        <span>Customer:</span>
                        <span className="text-white">{result.booking.user?.name}</span>
                      </div>
                      <div className="flex justify-between">
                        <span>Email:</span>
                        <span className="text-white text-xs">{result.booking.user?.email}</span>
                      </div>
                      <div className="flex justify-between">
                        <span>Tickets:</span>
                        <span className="text-white">{result.booking.ticketsBooked}</span>
                      </div>
                      <div className="flex justify-between">
                        <span>Amount:</span>
                        <span className="text-white font-bold">₹{result.booking.totalAmount}</span>
                      </div>
                      <div className="flex justify-between">
                        <span>Reference:</span>
                        <span className="text-white font-mono text-xs">{result.booking.bookingReference}</span>
                      </div>
                      <div className="flex justify-between">
                        <span>Date:</span>
                        <span className="text-white">{new Date(result.booking.bookingDate).toLocaleDateString()}</span>
                      </div>
                    </div>
                  </div>
                )}
              </div>
            )}
          </GlassCard>

          {/* Instructions */}
          <GlassCard className="mt-4 sm:mt-6 p-4 sm:p-6 bg-blue-500/20 border-blue-400/30">
            <h4 className="font-bold text-blue-200 mb-3 text-sm sm:text-base">Instructions:</h4>
            <ul className="text-xs sm:text-sm text-blue-100 space-y-2">
              <li className="flex items-start">
                <span className="text-blue-300 mr-2">•</span>
                Use QR code tab to scan/upload QR codes from tickets
              </li>
              <li className="flex items-start">
                <span className="text-blue-300 mr-2">•</span>
                Use booking reference tab to verify using booking numbers
              </li>
              <li className="flex items-start">
                <span className="text-blue-300 mr-2">•</span>
                Only CONFIRMED bookings allow entry
              </li>
            </ul>
          </GlassCard>
        </div>
      </div>
    </div>
  );
};

export default QRScanner;