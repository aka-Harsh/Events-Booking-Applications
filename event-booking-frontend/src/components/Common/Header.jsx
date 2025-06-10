import React from 'react';
import { Camera } from 'lucide-react';
import { useApp } from '../../context/AppContext';
import Button3D from './Button3D';

const Header = () => {
  const { user, logout } = useApp();

  return (
    <header className="w-full sticky top-0 z-50 backdrop-blur-xl bg-white/10 border-b border-white/20">
      <div className="w-full max-w-none mx-auto px-4 sm:px-6 lg:px-8 py-4">
        <div className="flex justify-between items-center w-full">
          <div className="flex items-center space-x-3 sm:space-x-4 flex-shrink-0">
            <div className="w-8 h-8 sm:w-10 sm:h-10 bg-gradient-to-r from-blue-500 to-purple-600 rounded-2xl flex items-center justify-center flex-shrink-0">
              <Camera className="w-4 h-4 sm:w-6 sm:h-6 text-white" />
            </div>
            <h1 className="text-lg sm:text-2xl font-bold bg-gradient-to-r from-white to-blue-200 bg-clip-text text-transparent">
              Event Booking Pro
            </h1>
          </div>
          
          <div className="flex items-center space-x-2 sm:space-x-4 flex-shrink-0">
            <div className="text-right hidden sm:block">
              <p className="text-white font-semibold text-sm sm:text-base">Welcome, {user?.name}</p>
              <span className="text-xs px-2 sm:px-3 py-1 bg-gradient-to-r from-blue-500 to-purple-600 text-white rounded-full">
                {user?.role}
              </span>
            </div>
            <div className="text-right block sm:hidden">
              <p className="text-white font-semibold text-xs">{user?.name}</p>
              <span className="text-xs px-2 py-1 bg-gradient-to-r from-blue-500 to-purple-600 text-white rounded-full">
                {user?.role}
              </span>
            </div>
            <Button3D onClick={logout} variant="danger" className="text-xs sm:text-sm">
              Logout
            </Button3D>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;