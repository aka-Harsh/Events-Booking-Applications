import React from 'react';
import { Calendar, Ticket, Star, BarChart3, QrCode } from 'lucide-react';
import { useApp } from '../../context/AppContext';

const Navigation = ({ currentTab, setCurrentTab }) => {
  const { user } = useApp();
  
  const userTabs = [
    { id: 'events', name: 'Events', icon: Calendar },
    { id: 'bookings', name: 'My Bookings', icon: Ticket },
    { id: 'reviews', name: 'My Reviews', icon: Star }
  ];

  const adminTabs = [
    { id: 'dashboard', name: 'Dashboard', icon: BarChart3 },
    { id: 'events', name: 'Manage Events', icon: Calendar },
    { id: 'bookings', name: 'All Bookings', icon: Ticket },
    { id: 'reviews', name: 'All Reviews', icon: Star },
    { id: 'qr-scanner', name: 'QR Scanner', icon: QrCode }
  ];

  const tabs = user?.role === 'ADMIN' ? adminTabs : userTabs;

  return (
    <nav className="w-full sticky top-16 sm:top-20 z-40 backdrop-blur-xl bg-white/5 border-b border-white/10">
      <div className="w-full max-w-none mx-auto px-4 sm:px-6 lg:px-8 py-3 sm:py-4">
        <div className="flex space-x-1 sm:space-x-2 overflow-x-auto scrollbar-hide">
          {tabs.map(tab => {
            const Icon = tab.icon;
            return (
              <button
                key={tab.id}
                onClick={() => setCurrentTab(tab.id)}
                className={`
                  flex items-center space-x-1 sm:space-x-2 px-3 sm:px-6 py-2 sm:py-3 rounded-2xl font-semibold transition-all duration-300 whitespace-nowrap flex-shrink-0 text-xs sm:text-base
                  ${currentTab === tab.id
                    ? 'bg-gradient-to-r from-blue-500 to-purple-600 text-white shadow-lg scale-105'
                    : 'text-black/70 hover:text-white hover:bg-white/10'
                  }
                `}
              >
                <Icon className="w-4 h-4 sm:w-5 sm:h-5 flex-shrink-0" />
                <span className="hidden sm:inline">{tab.name}</span>
                <span className="sm:hidden">{tab.name.split(' ')[0]}</span>
              </button>
            );
          })}
        </div>
      </div>
    </nav>
  );
};

export default Navigation;