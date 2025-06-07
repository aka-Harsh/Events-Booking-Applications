package com.eventbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventBookingApplication.class, args);
		System.out.println("🎉 Event Booking Application Started Successfully!");
		System.out.println("🌐 Application is running on: http://localhost:8080");
		System.out.println("🗄️ H2 Database Console: http://localhost:8080/h2-console");
		System.out.println("📊 API Documentation: http://localhost:8080/api/");
	}
}