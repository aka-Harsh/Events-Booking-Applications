import React, { useState } from 'react';
import { useApp } from '../../context/AppContext';
import AnimatedBackground from '../Common/AnimatedBackground';
import GlassCard from '../Common/GlassCard';
import Button3D from '../Common/Button3D';

const Register = ({ onBack }) => {
  const { register } = useApp();
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    role: 'USER'
  });
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    const result = await register(formData);
    if (result.success) {
      setMessage('Registration successful! You can now login.');
      setTimeout(() => onBack(), 2000);
    } else {
      setMessage(result.message);
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  return (
    <div className="min-h-screen w-full flex items-center justify-center p-4">
      <AnimatedBackground />
      <div className="w-full max-w-md mx-auto">
        <GlassCard className="p-6 sm:p-8">
          <h2 className="text-2xl sm:text-3xl font-bold text-center bg-gradient-to-r from-white to-green-200 bg-clip-text text-transparent mb-6 sm:mb-8">
            Create Account
          </h2>

          <form onSubmit={handleSubmit} className="space-y-4 sm:space-y-6">
            <div>
              <label className="block text-white font-semibold mb-2 text-sm sm:text-base">Name</label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white placeholder-white/50 focus:outline-none focus:border-green-400 focus:bg-white/20 transition-all duration-300 text-sm sm:text-base"
                placeholder="Enter your full name"
                required
              />
            </div>

            <div>
              <label className="block text-white font-semibold mb-2 text-sm sm:text-base">Email</label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white placeholder-white/50 focus:outline-none focus:border-green-400 focus:bg-white/20 transition-all duration-300 text-sm sm:text-base"
                placeholder="Enter your email"
                required
              />
            </div>

            <div>
              <label className="block text-white font-semibold mb-2 text-sm sm:text-base">Password</label>
              <input
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white placeholder-white/50 focus:outline-none focus:border-green-400 focus:bg-white/20 transition-all duration-300 text-sm sm:text-base"
                placeholder="Create a strong password"
                required
              />
            </div>

            <div>
              <label className="block text-white font-semibold mb-2 text-sm sm:text-base">Role</label>
              <select
                name="role"
                value={formData.role}
                onChange={handleChange}
                className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white focus:outline-none focus:border-green-400 focus:bg-white/20 transition-all duration-300 text-sm sm:text-base"
              >
                <option value="USER" className="bg-gray-800">User</option>
                <option value="ADMIN" className="bg-gray-800">Admin</option>
              </select>
            </div>

            {message && (
              <div className={`text-sm p-3 rounded-2xl border ${
                message.includes('successful') 
                  ? 'text-green-300 bg-green-500/20 border-green-400/30' 
                  : 'text-red-300 bg-red-500/20 border-red-400/30'
              }`}>
                {message}
              </div>
            )}

            <Button3D type="submit" variant="success" className="w-full">
              Create Account
            </Button3D>
          </form>

          <div className="mt-6 text-center">
            <Button3D onClick={onBack} variant="secondary" className="text-sm">
              Back to Login
            </Button3D>
          </div>
        </GlassCard>
      </div>
    </div>
  );
};

export default Register;