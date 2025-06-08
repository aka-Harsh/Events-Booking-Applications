import React, { useState, useEffect } from 'react';
import { Users, Calendar, Ticket, Star } from 'lucide-react';
import api from '../../services/api';
import GlassCard from '../Common/GlassCard';

const Dashboard = () => {
  const [dashboardData, setDashboardData] = useState(null);

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    try {
      const data = await api.getDashboard();
      setDashboardData(data);
    } catch (error) {
      console.error('Error loading dashboard:', error);
    }
  };

  if (!dashboardData) {
    return (
      <div className="w-full">
        <div className="container mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8">
          <div className="flex items-center justify-center h-64">
            <div className="animate-spin rounded-full h-12 w-12 sm:h-16 sm:w-16 border-b-2 border-white"></div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="w-full">
      <div className="container mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8">
        <h2 className="text-3xl sm:text-4xl font-bold bg-gradient-to-r from-white to-purple-200 bg-clip-text text-transparent mb-6 sm:mb-8">
          Admin Dashboard
        </h2>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 sm:gap-6 mb-6 sm:mb-8">
          <GlassCard className="p-4 sm:p-6 text-center">
            <div className="w-12 h-12 sm:w-16 sm:h-16 bg-gradient-to-r from-blue-500 to-blue-600 rounded-full flex items-center justify-center mx-auto mb-3 sm:mb-4">
              <Users className="w-6 h-6 sm:w-8 sm:h-8 text-white" />
            </div>
            <h3 className="text-lg sm:text-xl font-bold text-white mb-2">Users</h3>
            <p className="text-2xl sm:text-3xl font-bold text-blue-400 mb-2">{dashboardData.userStats?.totalUsers}</p>
            <p className="text-xs sm:text-sm text-white/60">
              {dashboardData.userStats?.totalAdmins} Admins, {dashboardData.userStats?.totalRegularUsers} Users
            </p>
          </GlassCard>

          <GlassCard className="p-4 sm:p-6 text-center">
            <div className="w-12 h-12 sm:w-16 sm:h-16 bg-gradient-to-r from-green-500 to-green-600 rounded-full flex items-center justify-center mx-auto mb-3 sm:mb-4">
              <Calendar className="w-6 h-6 sm:w-8 sm:h-8 text-white" />
            </div>
            <h3 className="text-lg sm:text-xl font-bold text-white mb-2">Events</h3>
            <p className="text-2xl sm:text-3xl font-bold text-green-400 mb-2">{dashboardData.eventStats?.totalEvents}</p>
            <p className="text-xs sm:text-sm text-white/60">
              {dashboardData.eventStats?.upcomingEvents} Upcoming
            </p>
          </GlassCard>

          <GlassCard className="p-4 sm:p-6 text-center">
            <div className="w-12 h-12 sm:w-16 sm:h-16 bg-gradient-to-r from-purple-500 to-purple-600 rounded-full flex items-center justify-center mx-auto mb-3 sm:mb-4">
              <Ticket className="w-6 h-6 sm:w-8 sm:h-8 text-white" />
            </div>
            <h3 className="text-lg sm:text-xl font-bold text-white mb-2">Bookings</h3>
            <p className="text-2xl sm:text-3xl font-bold text-purple-400 mb-2">{dashboardData.bookingStats?.totalBookings}</p>
            <p className="text-xs sm:text-sm text-white/60">
              {dashboardData.bookingStats?.confirmedBookings} Confirmed
            </p>
          </GlassCard>

          <GlassCard className="p-4 sm:p-6 text-center">
            <div className="w-12 h-12 sm:w-16 sm:h-16 bg-gradient-to-r from-red-500 to-red-600 rounded-full flex items-center justify-center mx-auto mb-3 sm:mb-4">
              <span className="text-lg sm:text-2xl">💰</span>
            </div>
            <h3 className="text-lg sm:text-xl font-bold text-white mb-2">Revenue</h3>
            <p className="text-2xl sm:text-3xl font-bold text-red-400 mb-2">₹{dashboardData.bookingStats?.totalRevenue}</p>
            <p className="text-xs sm:text-sm text-white/60">Total Earnings</p>
          </GlassCard>
        </div>

        {/* Recent Activity */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 sm:gap-8">
          {/* Recent Bookings */}
          <GlassCard className="p-4 sm:p-6">
            <h3 className="text-xl sm:text-2xl font-bold text-white mb-4 sm:mb-6">Recent Bookings</h3>
            <div className="space-y-3 sm:space-y-4">
              {dashboardData.recentBookings?.map(booking => (
                <div key={booking.id} className="bg-white/10 rounded-2xl p-3 sm:p-4 border border-white/20">
                  <p className="font-semibold text-white mb-1 text-sm sm:text-base line-clamp-1">{booking.event?.name}</p>
                  <p className="text-xs sm:text-sm text-white/70">
                    {booking.user?.name} - {booking.ticketsBooked} tickets - ₹{booking.totalAmount}
                  </p>
                </div>
              ))}
            </div>
          </GlassCard>

          {/* Recent Reviews */}
          <GlassCard className="p-4 sm:p-6">
            <h3 className="text-xl sm:text-2xl font-bold text-white mb-4 sm:mb-6">Recent Reviews</h3>
            <div className="space-y-3 sm:space-y-4">
              {dashboardData.recentReviews?.map(review => (
                <div key={review.id} className="bg-white/10 rounded-2xl p-3 sm:p-4 border border-white/20">
                  <p className="font-semibold text-white mb-1 text-sm sm:text-base line-clamp-1">{review.event?.name}</p>
                  <div className="flex items-center mb-2">
                    {[...Array(5)].map((_, i) => (
                      <Star 
                        key={i} 
                        className={`w-3 h-3 sm:w-4 sm:h-4 ${
                          i < review.rating ? 'text-yellow-400 fill-current' : 'text-gray-400'
                        }`} 
                      />
                    ))}
                    <span className="text-white/70 text-xs sm:text-sm ml-2">by {review.user?.name}</span>
                  </div>
                  <p className="text-xs text-white/60 line-clamp-2">{review.comment?.substring(0, 100)}...</p>
                </div>
              ))}
            </div>
          </GlassCard>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;