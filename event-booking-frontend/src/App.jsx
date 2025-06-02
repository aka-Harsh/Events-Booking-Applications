import { useState, useEffect } from 'react';
import { AppProvider, useApp } from './context/AppContext';
import Login from './components/Auth/Login';
import Header from './components/Common/Header';
import Navigation from './components/Common/Navigation';
import Home from './components/Common/Home';
import EventsList from './components/Events/EventsList';
import Bookings from './components/Bookings/Bookings';
import Reviews from './components/Reviews/Reviews';
import Dashboard from './components/Admin/Dashboard';
import QRScanner from './components/Admin/QRScanner';
import AnimatedBackground from './components/Common/AnimatedBackground';

function AppContent() {
  const { user } = useApp();
  const [currentTab, setCurrentTab] = useState('home');
  const [showLogin, setShowLogin] = useState(false);

  useEffect(() => {
    // Reset to appropriate tab when user logs in
    if (user?.role === 'ADMIN') {
      setCurrentTab('dashboard');
    } else if (user?.role === 'USER') {
      setCurrentTab('events');
    }
  }, [user]);

  // Show home page by default (no login required)
  if (!user && !showLogin) {
    return <Home onLoginRequired={() => setShowLogin(true)} />;
  }

  // Show login page when requested
  if (!user && showLogin) {
    return <Login onBack={() => setShowLogin(false)} />;
  }

  const renderContent = () => {
    switch (currentTab) {
      case 'dashboard':
        return <Dashboard />;
      case 'events':
        return <EventsList />;
      case 'bookings':
        return <Bookings />;
      case 'reviews':
        return <Reviews />;
      case 'qr-scanner':
        return <QRScanner />;
      default:
        return <EventsList />;
    }
  };

  return (
    <div className="min-h-screen w-full">
      <AnimatedBackground />
      <div className="relative z-10 w-full min-h-screen flex flex-col">
        <Header />
        <Navigation currentTab={currentTab} setCurrentTab={setCurrentTab} />
        <main className="flex-1 w-full">
          {renderContent()}
        </main>
      </div>
    </div>
  );
}

function App() {
  return (
    <AppProvider>
      <AppContent />
    </AppProvider>
  );
}

export default App;