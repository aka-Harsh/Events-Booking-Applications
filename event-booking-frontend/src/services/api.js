// API Base URL
const API_BASE = "https://localhost:8080/api";

// Helper function for API calls with better error handling
const apiCall = async (url, options = {}) => {
  try {
    console.log(`Making API call to: ${url}`);
    const response = await fetch(url, {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers
      },
      ...options
    });
    
    console.log(`API Response status: ${response.status}`);
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    const data = await response.json();
    console.log('API Response data:', data);
    return data;
  } catch (error) {
    console.error(`API call failed for ${url}:`, error);
    throw error;
  }
};

// API Service
const api = {
  // User APIs
  login: (email, password) => 
    apiCall(`${API_BASE}/users/login`, {
      method: 'POST',
      body: JSON.stringify({ email, password })
    }),

  register: (userData) =>
    apiCall(`${API_BASE}/users/register`, {
      method: 'POST',
      body: JSON.stringify(userData)
    }),

  getUser: (id) =>
    apiCall(`${API_BASE}/users/${id}`),

  // Event APIs
  getAllEvents: () =>
    apiCall(`${API_BASE}/events`),

  getEvent: (id) =>
    apiCall(`${API_BASE}/events/${id}`),

  getEventsByType: (type) =>
    apiCall(`${API_BASE}/events/type/${type}`),

  searchEvents: (query) =>
    apiCall(`${API_BASE}/events/search?query=${query}`),

  getRecommendations: (eventId) =>
    apiCall(`${API_BASE}/events/${eventId}/recommendations`),

  createEvent: (eventData) =>
    apiCall(`${API_BASE}/events`, {
      method: 'POST',
      body: JSON.stringify(eventData)
    }),

  updateEvent: (id, eventData) =>
    apiCall(`${API_BASE}/events/${id}`, {
      method: 'PUT',
      body: JSON.stringify(eventData)
    }),

  deleteEvent: (id) =>
    apiCall(`${API_BASE}/events/${id}`, {
      method: 'DELETE'
    }),

  // Booking APIs
  createBooking: (bookingData) =>
    apiCall(`${API_BASE}/bookings`, {
      method: 'POST',
      body: JSON.stringify(bookingData)
    }),

  getUserBookings: (userId) =>
    apiCall(`${API_BASE}/bookings/user/${userId}`),

  cancelBooking: (bookingId) =>
    apiCall(`${API_BASE}/bookings/${bookingId}/cancel`, {
      method: 'PUT'
    }),

  getBookingQR: (bookingId) =>
    apiCall(`${API_BASE}/bookings/${bookingId}/qr-image`),

  verifyQR: (qrCode) =>
    apiCall(`${API_BASE}/bookings/verify-qr`, {
      method: 'POST',
      body: JSON.stringify({ qrCode })
    }),

  getAllBookings: () =>
    apiCall(`${API_BASE}/bookings`),

  // Review APIs
  createReview: (reviewData) =>
    apiCall(`${API_BASE}/reviews`, {
      method: 'POST',
      body: JSON.stringify(reviewData)
    }),

  getEventReviews: (eventId) =>
    apiCall(`${API_BASE}/reviews/event/${eventId}`),

  getReviewSummary: (eventId) =>
    apiCall(`${API_BASE}/reviews/event/${eventId}/summary`),

  getUserReviews: (userId) =>
    apiCall(`${API_BASE}/reviews/user/${userId}`),

  getAllReviews: () =>
    apiCall(`${API_BASE}/reviews`),

  // Admin APIs
  getDashboard: () =>
    apiCall(`${API_BASE}/admin/dashboard`),

  getRevenue: () =>
    apiCall(`${API_BASE}/admin/revenue`),

  getBookingByReference: (reference) =>
    apiCall(`${API_BASE}/bookings/reference/${reference}`)
};

export default api;