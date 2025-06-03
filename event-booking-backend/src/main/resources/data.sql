-- Insert sample users
INSERT INTO users (name, email, password, role, created_date, profile_image) VALUES
('Admin User', 'admin@eventbooking.com', 'admin123', 'ADMIN', CURRENT_TIMESTAMP, 'img3'),
('John Doe', 'john@example.com', 'password123', 'USER', CURRENT_TIMESTAMP, 'img1'),
('Jane Smith', 'jane@example.com', 'password123', 'USER', CURRENT_TIMESTAMP, 'img4'),
('Mike Johnson', 'mike@example.com', 'password123', 'USER', CURRENT_TIMESTAMP, 'img1'),
('Sarah Wilson', 'sarah@example.com', 'password123', 'USER', CURRENT_TIMESTAMP, 'img5');

-- Insert sample events
INSERT INTO events (name, description, type, tags, date, time, location, total_tickets, tickets_sold, base_price, current_price, event_image) VALUES
('Avengers: Endgame', 'The epic conclusion to the Marvel Cinematic Universe saga. Join the Avengers in their final battle against Thanos.', 'MOVIE', 'action,superhero,marvel,adventure', '2025-06-15', '19:30:00', 'PVR Cinemas, Phoenix Mall', 200, 45, 350.00, 350.00, 'img6'),

('Weekend Trek to Munnar', 'Experience the breathtaking beauty of Munnar hills. Perfect weekend getaway with tea gardens and scenic views.', 'WEEKEND_PLAN', 'nature,trekking,adventure,hills,tea gardens', '2025-07-20', '06:00:00', 'Munnar, Kerala', 50, 12, 2500.00, 2500.00, 'img7'),

('AR Rahman Live Concert', 'Experience the magic of AR Rahman live in concert. An evening of soulful music and unforgettable melodies.', 'LIVE_SHOW', 'music,concert,rahman,live,entertainment', '2025-08-10', '19:00:00', 'Nehru Indoor Stadium, Chennai', 5000, 2800, 1500.00, 1650.00, 'img8'),

('Digital Marketing Masterclass', 'Learn advanced digital marketing strategies from industry experts. Boost your career with practical insights.', 'WORKSHOP', 'marketing,digital,career,business,education', '2025-06-25', '10:00:00', 'ITC Grand Chola, Chennai', 100, 23, 1200.00, 1200.00, 'img9'),

('Baahubali 2: The Conclusion', 'The spectacular sequel to Baahubali. Epic battles, stunning visuals, and an incredible storyline.', 'MOVIE', 'epic,drama,action,indian cinema,prabhas', '2025-07-05', '20:00:00', 'Sathyam Cinemas, Express Avenue', 300, 150, 400.00, 440.00, 'img10'),

('Goa Beach Party Weekend', 'Ultimate beach party experience in Goa. Sun, sand, music, and endless fun with like-minded people.', 'WEEKEND_PLAN', 'party,beach,goa,fun,music,dance', '2025-08-25', '18:00:00', 'Baga Beach, Goa', 80, 65, 3500.00, 4200.00, 'img11'),

('Stand-up Comedy Night', 'Laugh your heart out with top comedians. An evening full of humor, wit, and entertainment.', 'LIVE_SHOW', 'comedy,humor,entertainment,stand-up,laughter', '2025-06-30', '20:30:00', 'The Comedy Store, Bangalore', 200, 45, 800.00, 800.00, 'img12'),

('Web Development Bootcamp', 'Intensive 2-day bootcamp covering modern web development. From basics to advanced concepts.', 'WORKSHOP', 'programming,web development,coding,tech,career', '2025-07-15', '09:00:00', 'Tech Park, Hyderabad', 60, 35, 2000.00, 2200.00, 'img13');

-- Insert sample bookings
INSERT INTO bookings (user_id, event_id, tickets_booked, total_amount, qr_code, booking_date, status, booking_reference) VALUES
(2, 1, 2, 700.00, 'BOOKING_1_USER_2_EVENT_1_TICKETS_2_REF_BK1234567890', CURRENT_TIMESTAMP, 'CONFIRMED', 'BK1234567890'),
(3, 3, 4, 6600.00, 'BOOKING_2_USER_3_EVENT_3_TICKETS_4_REF_BK1234567891', CURRENT_TIMESTAMP, 'CONFIRMED', 'BK1234567891'),
(4, 2, 1, 2500.00, 'BOOKING_3_USER_4_EVENT_2_TICKETS_1_REF_BK1234567892', CURRENT_TIMESTAMP, 'CONFIRMED', 'BK1234567892'),
(5, 4, 2, 2400.00, 'BOOKING_4_USER_5_EVENT_4_TICKETS_2_REF_BK1234567893', CURRENT_TIMESTAMP, 'CONFIRMED', 'BK1234567893'),
(2, 5, 3, 1320.00, 'BOOKING_5_USER_2_EVENT_5_TICKETS_3_REF_BK1234567894', CURRENT_TIMESTAMP, 'CONFIRMED', 'BK1234567894');

-- Insert sample reviews
INSERT INTO reviews (user_id, event_id, rating, comment, review_date) VALUES
(2, 1, 5, 'Amazing movie! The action sequences were incredible and the storyline was perfect.', CURRENT_TIMESTAMP),
(3, 3, 5, 'AR Rahman was absolutely mesmerizing! Best concert I have ever attended.', CURRENT_TIMESTAMP),
(4, 2, 4, 'Beautiful trek to Munnar. The views were breathtaking, well organized trip.', CURRENT_TIMESTAMP),
(5, 4, 4, 'Very informative workshop. Learned a lot about digital marketing strategies.', CURRENT_TIMESTAMP),
(2, 5, 5, 'Baahubali 2 exceeded all expectations. Truly a cinematic masterpiece!', CURRENT_TIMESTAMP);