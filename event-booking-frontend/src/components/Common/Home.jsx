import React, { useState, useEffect } from 'react';
import { Calendar, MapPin, Users, Star, Ticket, TrendingUp, Award, Zap, ArrowRight, Play, Sparkles } from 'lucide-react';
import GlassCard from './GlassCard';
import Button3D from './Button3D';
import AnimatedBackground from './AnimatedBackground';
import api from '../../services/api';

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

// 3D Floating Element Component
const FloatingElement = ({ children, className = "", delay = 0 }) => (
  <div 
    className={`animate-bounce ${className}`}
    style={{ 
      animationDelay: `${delay}s`,
      animationDuration: '3s'
    }}
  >
    {children}
  </div>
);

// Hero Section Component
const HeroSection = ({ onLoginRequired }) => (
  <section className="relative overflow-hidden py-12 sm:py-20 lg:py-32 min-h-screen flex items-center">
    <div className="container mx-auto px-4 sm:px-6 lg:px-8 w-full">
      <div className="text-center relative z-10">
        {/* 3D Floating Icons */}
        <div className="absolute inset-0 pointer-events-none">
          <FloatingElement className="absolute top-10 left-10 text-blue-400" delay={0}>
            <Star className="w-8 h-8 sm:w-12 sm:h-12" />
          </FloatingElement>
          <FloatingElement className="absolute top-20 right-10 text-purple-400" delay={1}>
            <Ticket className="w-6 h-6 sm:w-10 sm:h-10" />
          </FloatingElement>
          <FloatingElement className="absolute bottom-20 left-20 text-pink-400" delay={2}>
            <Calendar className="w-8 h-8 sm:w-12 sm:h-12" />
          </FloatingElement>
          <FloatingElement className="absolute bottom-10 right-20 text-green-400" delay={0.5}>
            <Sparkles className="w-6 h-6 sm:w-10 sm:h-10" />
          </FloatingElement>
        </div>

        <div className="mb-8 sm:mb-12">
          <h1 className="text-4xl sm:text-6xl lg:text-8xl font-bold bg-gradient-to-r from-white via-blue-200 to-purple-200 bg-clip-text text-transparent mb-4 sm:mb-6 leading-tight">
            Welcome to the Future
          </h1>
          <h2 className="text-2xl sm:text-4xl lg:text-6xl font-bold bg-gradient-to-r from-purple-400 via-pink-400 to-blue-400 bg-clip-text text-transparent mb-6 sm:mb-8">
            of Event Booking
          </h2>
          <p className="text-lg sm:text-xl lg:text-2xl text-white/80 max-w-4xl mx-auto leading-relaxed">
            Discover amazing events, book instantly, and create unforgettable memories. 
            Experience the magic of attending events like never before.
          </p>
        </div>

        <div className="flex flex-col sm:flex-row gap-4 sm:gap-6 justify-center items-center mb-12 sm:mb-16">
          <Button3D onClick={onLoginRequired} className="text-lg sm:text-xl px-8 sm:px-12 py-4 sm:py-6">
            <Play className="w-5 h-5 sm:w-6 sm:h-6 mr-3" />
            Explore Events
            <ArrowRight className="w-5 h-5 sm:w-6 sm:h-6 ml-3" />
          </Button3D>
          <Button3D onClick={onLoginRequired} variant="success" className="text-lg sm:text-xl px-8 sm:px-12 py-4 sm:py-6">
            <Calendar className="w-5 h-5 sm:w-6 sm:h-6 mr-3" />
            Browse Events Now
          </Button3D>
        </div>

        {/* 3D Stats */}
        <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 sm:gap-8 max-w-6xl mx-auto">
          {[
            { icon: Calendar, label: 'Events', value: '1000+', color: 'from-blue-500 to-cyan-500' },
            { icon: Users, label: 'Happy Users', value: '50K+', color: 'from-green-500 to-emerald-500' },
            { icon: Star, label: 'Reviews', value: '25K+', color: 'from-yellow-500 to-orange-500' },
            { icon: TrendingUp, label: 'Success Rate', value: '99%', color: 'from-purple-500 to-pink-500' }
          ].map((stat, index) => (
            <GlassCard key={index} className="p-4 sm:p-6 text-center transform hover:scale-110 transition-all duration-500">
              <div className={`w-12 h-12 sm:w-16 sm:h-16 bg-gradient-to-r ${stat.color} rounded-full flex items-center justify-center mx-auto mb-3 sm:mb-4`}>
                <stat.icon className="w-6 h-6 sm:w-8 sm:h-8 text-white" />
              </div>
              <div className="text-2xl sm:text-3xl font-bold text-white mb-1 sm:mb-2">{stat.value}</div>
              <div className="text-sm sm:text-base text-white/70">{stat.label}</div>
            </GlassCard>
          ))}
        </div>
      </div>
    </div>
  </section>
);

// Featured Events Component
const FeaturedEvents = ({ events, onLoginRequired }) => (
  <section className="py-12 sm:py-20">
    <div className="container mx-auto px-4 sm:px-6 lg:px-8">
      <div className="text-center mb-12 sm:mb-16">
        <h2 className="text-3xl sm:text-5xl font-bold bg-gradient-to-r from-white to-blue-200 bg-clip-text text-transparent mb-4 sm:mb-6">
          Featured Events
        </h2>
        <p className="text-lg sm:text-xl text-white/70 max-w-3xl mx-auto">
          Discover the most popular and trending events happening near you
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 sm:gap-8">
        {events.slice(0, 6).map((event, index) => (
          <GlassCard key={event.id} className="overflow-hidden group">
            <div className="relative h-48 sm:h-64 overflow-hidden">
              <img 
                src={getEventImage(event.name)} 
                alt={event.name}
                className="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110"
              />
              <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-black/20 to-transparent" />
              
              {/* 3D Floating Badge */}
              <div className="absolute top-4 right-4 transform rotate-12 hover:rotate-0 transition-transform duration-300">
                <span className="px-3 py-1 bg-gradient-to-r from-pink-500 to-purple-600 text-white text-xs font-bold rounded-full shadow-lg">
                  {event.type.replace('_', ' ')}
                </span>
              </div>

              <div className="absolute bottom-4 left-4 right-4">
                <h3 className="text-white font-bold text-lg sm:text-xl mb-2 line-clamp-2">{event.name}</h3>
                <div className="flex items-center text-white/80 text-sm mb-2">
                  <Calendar className="w-4 h-4 mr-2" />
                  <span>{event.date}</span>
                </div>
                <div className="flex items-center text-white/80 text-sm">
                  <MapPin className="w-4 h-4 mr-2" />
                  <span className="line-clamp-1">{event.location}</span>
                </div>
              </div>
            </div>
            
            <div className="p-4 sm:p-6">
              <div className="flex justify-between items-center mb-4">
                <div className="text-2xl sm:text-3xl font-bold text-white">₹{event.currentPrice}</div>
                <div className="flex items-center text-white/70">
                  <Users className="w-4 h-4 mr-1" />
                  <span className="text-sm">{event.availableTickets} left</span>
                </div>
              </div>

              <Button3D 
                onClick={onLoginRequired} 
                className="w-full"
              >
                <Zap className="w-4 h-4 mr-2" />
                View Details
              </Button3D>
            </div>
          </GlassCard>
        ))}
      </div>
    </div>
  </section>
);

// Why Choose Us Component
const WhyChooseUs = () => (
  <section className="py-12 sm:py-20">
    <div className="container mx-auto px-4 sm:px-6 lg:px-8">
      <div className="text-center mb-12 sm:mb-16">
        <h2 className="text-3xl sm:text-5xl font-bold bg-gradient-to-r from-white to-purple-200 bg-clip-text text-transparent mb-4 sm:mb-6">
          Why Choose Event Booking Pro?
        </h2>
        <p className="text-lg sm:text-xl text-white/70 max-w-3xl mx-auto">
          Experience the difference with our cutting-edge platform
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 sm:gap-8">
        {[
          {
            icon: Zap,
            title: 'Lightning Fast',
            description: 'Book tickets in seconds with our optimized booking system',
            color: 'from-yellow-500 to-orange-500'
          },
          {
            icon: Award,
            title: 'Premium Experience',
            description: 'Enjoy exclusive events and VIP treatment',
            color: 'from-purple-500 to-pink-500'
          },
          {
            icon: Star,
            title: '5-Star Rated',
            description: 'Trusted by thousands with excellent reviews',
            color: 'from-blue-500 to-cyan-500'
          },
          {
            icon: Ticket,
            title: 'QR Code Tickets',
            description: 'Secure digital tickets with QR code verification',
            color: 'from-green-500 to-emerald-500'
          },
          {
            icon: Users,
            title: 'Community',
            description: 'Join a vibrant community of event enthusiasts',
            color: 'from-indigo-500 to-purple-500'
          },
          {
            icon: TrendingUp,
            title: 'Growing Platform',
            description: 'Constantly adding new events and features',
            color: 'from-pink-500 to-red-500'
          }
        ].map((feature, index) => (
          <GlassCard key={index} className="p-6 sm:p-8 text-center transform hover:scale-105 transition-all duration-500">
            <div className={`w-16 h-16 sm:w-20 sm:h-20 bg-gradient-to-r ${feature.color} rounded-full flex items-center justify-center mx-auto mb-4 sm:mb-6 transform hover:rotate-12 transition-transform duration-300`}>
              <feature.icon className="w-8 h-8 sm:w-10 sm:h-10 text-white" />
            </div>
            <h3 className="text-xl sm:text-2xl font-bold text-white mb-3 sm:mb-4">{feature.title}</h3>
            <p className="text-white/70 text-sm sm:text-base leading-relaxed">{feature.description}</p>
          </GlassCard>
        ))}
      </div>
    </div>
  </section>
);

// Main Home Component
const Home = ({ onLoginRequired }) => {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadEvents();
  }, []);

  const loadEvents = async () => {
    try {
      const data = await api.getAllEvents();
      setEvents(data);
    } catch (error) {
      console.error('Error loading events:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen w-full flex items-center justify-center">
        <AnimatedBackground />
        <div className="text-center relative z-10">
          <div className="animate-spin rounded-full h-16 w-16 sm:h-20 sm:w-20 border-b-2 border-white mx-auto mb-4"></div>
          <p className="text-white/70 text-lg sm:text-xl">Loading amazing events...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="w-full min-h-screen">
      <AnimatedBackground />
      <div className="relative z-10 w-full min-h-screen">
        <HeroSection onLoginRequired={onLoginRequired} />
        <FeaturedEvents events={events} onLoginRequired={onLoginRequired} />
        <WhyChooseUs />
        
        {/* Call to Action */}
        <section className="py-12 sm:py-20">
          <div className="container mx-auto px-4 sm:px-6 lg:px-8 text-center">
            <GlassCard className="p-8 sm:p-12 lg:p-16">
              <h2 className="text-3xl sm:text-5xl font-bold bg-gradient-to-r from-white to-blue-200 bg-clip-text text-transparent mb-4 sm:mb-6">
                Ready to Start Your Journey?
              </h2>
              <p className="text-lg sm:text-xl text-white/70 mb-8 sm:mb-12 max-w-3xl mx-auto">
                Join thousands of happy customers and discover your next favorite event today!
              </p>
              <div className="flex flex-col sm:flex-row gap-4 sm:gap-6 justify-center">
                <Button3D onClick={onLoginRequired} className="text-lg sm:text-xl px-8 sm:px-12 py-4 sm:py-6">
                  <Calendar className="w-5 h-5 sm:w-6 sm:h-6 mr-3" />
                  Browse Events Now
                </Button3D>
                <Button3D onClick={onLoginRequired} variant="success" className="text-lg sm:text-xl px-8 sm:px-12 py-4 sm:py-6">
                  <Star className="w-5 h-5 sm:w-6 sm:h-6 mr-3" />
                  Get Started
                </Button3D>
              </div>
            </GlassCard>
          </div>
        </section>
      </div>
    </div>
  );
};

export default Home;