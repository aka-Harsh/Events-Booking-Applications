import React, { useState } from 'react';
import { useApp } from '../../context/AppContext';
import Register from './Register';
import AnimatedBackground from '../Common/AnimatedBackground';
import GlassCard from '../Common/GlassCard';
import Button3D from '../Common/Button3D';

const Login = ({ onBack }) => {
  const { login } = useApp();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [showRegister, setShowRegister] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    const result = await login(email, password);
    if (!result.success) {
      setMessage(result.message);
    }
  };

  if (showRegister) {
    return <Register onBack={() => setShowRegister(false)} />;
  }

  return (
    <div className="min-h-screen w-full flex items-center justify-center p-4">
      <AnimatedBackground />
      <div className="w-full max-w-md mx-auto">
        <GlassCard className="p-6 sm:p-8">
          <div className="text-center mb-6 sm:mb-8">
            <h1 className="text-3xl sm:text-4xl font-bold bg-gradient-to-r from-white to-blue-200 bg-clip-text text-transparent mb-2">
              Event Booking Pro
            </h1>
            <p className="text-white/70 text-sm sm:text-base">Your gateway to amazing experiences</p>
          </div>

          <div className="mb-6 p-4 bg-blue-500/20 rounded-2xl border border-blue-400/30">
            <h3 className="text-white font-semibold mb-2 text-sm sm:text-base">Demo Accounts:</h3>
            <div className="text-xs sm:text-sm text-white/80 space-y-1">
              <p><strong>Admin:</strong> admin@eventbooking.com / admin123</p>
              <p><strong>User:</strong> john@example.com / password123</p>
            </div>
          </div>

          <form onSubmit={handleLogin} className="space-y-4 sm:space-y-6">
            <div>
              <label className="block text-white font-semibold mb-2 text-sm sm:text-base">Email</label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white placeholder-white/50 focus:outline-none focus:border-blue-400 focus:bg-white/20 transition-all duration-300 text-sm sm:text-base"
                placeholder="Enter your email"
                required
              />
            </div>

            <div>
              <label className="block text-white font-semibold mb-2 text-sm sm:text-base">Password</label>
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white placeholder-white/50 focus:outline-none focus:border-blue-400 focus:bg-white/20 transition-all duration-300 text-sm sm:text-base"
                placeholder="Enter your password"
                required
              />
            </div>

            {message && (
              <div className="text-red-300 text-sm bg-red-500/20 p-3 rounded-2xl border border-red-400/30">
                {message}
              </div>
            )}

            <Button3D type="submit" className="w-full">
              Login to Continue
            </Button3D>
          </form>

          <div className="mt-6 text-center">
            <Button3D 
              onClick={() => setShowRegister(true)}
              variant="secondary"
              className="text-sm mr-4"
            >
              Don't have an account? Register here
            </Button3D>
            <br></br>
            {onBack && (
              <Button3D 
                onClick={onBack}
                variant="secondary"
                className="text-sm"
              >
                Back to Home
              </Button3D>
            )}
          </div>
        </GlassCard>
      </div>
    </div>
  );
};

export default Login;