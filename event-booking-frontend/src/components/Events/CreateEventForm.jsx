import React, { useState } from 'react';
import { ArrowLeft } from 'lucide-react';
import api from '../../services/api';
import GlassCard from '../Common/GlassCard';
import Button3D from '../Common/Button3D';

const CreateEventForm = ({ onBack, onCreate }) => {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    type: 'MOVIE',
    tags: '',
    date: '',
    time: '',
    location: '',
    totalTickets: 100,
    basePrice: 0,
    eventImage: 'img2'
  });
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const eventData = {
        name: formData.name.trim(),
        description: formData.description.trim(),
        type: formData.type,
        tags: formData.tags.trim(),
        date: formData.date,
        time: formData.time,
        location: formData.location.trim(),
        totalTickets: parseInt(formData.totalTickets),
        basePrice: parseFloat(formData.basePrice),
        eventImage: formData.eventImage || 'img2'
      };

      if (!eventData.name || !eventData.location || !eventData.date || !eventData.time) {
        setMessage('Please fill in all required fields');
        setLoading(false);
        return;
      }

      if (isNaN(eventData.totalTickets) || eventData.totalTickets <= 0) {
        setMessage('Total tickets must be a positive number');
        setLoading(false);
        return;
      }

      if (isNaN(eventData.basePrice) || eventData.basePrice < 0) {
        setMessage('Base price must be a valid number');
        setLoading(false);
        return;
      }

      const response = await api.createEvent(eventData);
      if (response.success) {
        setMessage('Event created successfully!');
        onCreate();
        setTimeout(() => onBack(), 2000);
      } else {
        setMessage(response.message || 'Event creation failed');
      }
    } catch (error) {
      console.error('Event creation error:', error);
      setMessage('Event creation failed. Please check all fields.');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const today = new Date().toISOString().split('T')[0];

  return (
    <div className="w-full">
      <div className="container mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8">
        <Button3D onClick={onBack} variant="secondary" className="mb-6 sm:mb-8">
          <ArrowLeft className="w-4 h-4 sm:w-5 sm:h-5 mr-2" />
          Back
        </Button3D>

        <div className="max-w-4xl mx-auto">
          <GlassCard className="p-6 sm:p-8">
            <h2 className="text-3xl sm:text-4xl font-bold bg-gradient-to-r from-white to-green-200 bg-clip-text text-transparent mb-6 sm:mb-8">
              Create New Event
            </h2>

            <form onSubmit={handleSubmit} className="space-y-4 sm:space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4 sm:gap-6">
                <div>
                  <label className="block text-white font-semibold mb-2 text-sm sm:text-base">Event Name *</label>
                  <input
                    type="text"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white placeholder-white/50 focus:outline-none focus:border-green-400 focus:bg-white/20 transition-all duration-300 text-sm sm:text-base"
                    placeholder="Enter event name"
                    required
                  />
                </div>

                <div>
                  <label className="block text-white font-semibold mb-2 text-sm sm:text-base">Type *</label>
                  <select
                    name="type"
                    value={formData.type}
                    onChange={handleChange}
                    className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white focus:outline-none focus:border-green-400 focus:bg-white/20 transition-all duration-300 text-sm sm:text-base"
                    required
                  >
                    <option value="MOVIE" className="bg-gray-800">Movie</option>
                    <option value="WEEKEND_PLAN" className="bg-gray-800">Weekend Plan</option>
                    <option value="LIVE_SHOW" className="bg-gray-800">Live Show</option>
                    <option value="WORKSHOP" className="bg-gray-800">Workshop</option>
                  </select>
                </div>

                <div>
                  <label className="block text-white font-semibold mb-2 text-sm sm:text-base">Date *</label>
                  <input
                    type="date"
                    name="date"
                    value={formData.date}
                    onChange={handleChange}
                    min={today}
                    className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white focus:outline-none focus:border-green-400 focus:bg-white/20 transition-all duration-300 text-sm sm:text-base"
                    required
                  />
                </div>

                <div>
                  <label className="block text-white font-semibold mb-2 text-sm sm:text-base">Time *</label>
                  <input
                    type="time"
                    name="time"
                    value={formData.time}
                    onChange={handleChange}
                    className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white focus:outline-none focus:border-green-400 focus:bg-white/20 transition-all duration-300 text-sm sm:text-base"
                    required
                  />
                </div>

                <div>
                  <label className="block text-white font-semibold mb-2 text-sm sm:text-base">Total Tickets *</label>
                  <input
                    type="number"
                    name="totalTickets"
                    value={formData.totalTickets}
                    onChange={handleChange}
                    className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white focus:outline-none focus:border-green-400 focus:bg-white/20 transition-all duration-300 text-sm sm:text-base"
                    min="1"
                    max="10000"
                    required
                  />
                </div>

                <div>
                  <label className="block text-white font-semibold mb-2 text-sm sm:text-base">Base Price (₹) *</label>
                  <input
                    type="number"
                    name="basePrice"
                    value={formData.basePrice}
                    onChange={handleChange}
                    className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white focus:outline-none focus:border-green-400 focus:bg-white/20 transition-all duration-300 text-sm sm:text-base"
                    min="0"
                    step="0.01"
                    placeholder="0.00"
                    required
                  />
                </div>
              </div>

              <div>
                <label className="block text-white font-semibold mb-2 text-sm sm:text-base">Location *</label>
                <input
                  type="text"
                  name="location"
                  value={formData.location}
                  onChange={handleChange}
                  className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white placeholder-white/50 focus:outline-none focus:border-green-400 focus:bg-white/20 transition-all duration-300 text-sm sm:text-base"
                  placeholder="Enter event location"
                  required
                />
              </div>

              <div>
                <label className="block text-white font-semibold mb-2 text-sm sm:text-base">Tags (comma-separated)</label>
                <input
                  type="text"
                  name="tags"
                  value={formData.tags}
                  onChange={handleChange}
                  className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white placeholder-white/50 focus:outline-none focus:border-green-400 focus:bg-white/20 transition-all duration-300 text-sm sm:text-base"
                  placeholder="e.g., fun, adventure, outdoor"
                />
              </div>

              <div>
                <label className="block text-white font-semibold mb-2 text-sm sm:text-base">Description</label>
                <textarea
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  rows="4"
                  className="w-full px-4 py-3 rounded-2xl bg-white/10 border border-white/20 text-white placeholder-white/50 focus:outline-none focus:border-green-400 focus:bg-white/20 transition-all duration-300 resize-none text-sm sm:text-base"
                  placeholder="Event description..."
                />
              </div>

              {message && (
                <div className={`p-3 sm:p-4 rounded-2xl border text-sm sm:text-base ${
                  message.includes('successful') 
                    ? 'text-green-300 bg-green-500/20 border-green-400/30' 
                    : 'text-red-300 bg-red-500/20 border-red-400/30'
                }`}>
                  {message}
                </div>
              )}

              <div className="flex flex-col sm:flex-row gap-4 pt-4">
                <Button3D 
                  type="button"
                  onClick={onBack}
                  variant="secondary"
                  className="flex-1 order-2 sm:order-1"
                >
                  Cancel
                </Button3D>
                <Button3D 
                  type="submit"
                  disabled={loading}
                  variant="success"
                  className="flex-1 order-1 sm:order-2"
                >
                  {loading ? 'Creating...' : 'Create Event'}
                </Button3D>
              </div>
            </form>
          </GlassCard>
        </div>
      </div>
    </div>
  );
};

export default CreateEventForm;