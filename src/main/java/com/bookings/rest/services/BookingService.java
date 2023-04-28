package com.bookings.rest.services;

import com.bookings.rest.exceptions.OverlappingBookingException;
import com.bookings.rest.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bookings.rest.models.Booking;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BookingService {

	private final BookingRepository repository;

	@Autowired
	public BookingService(BookingRepository repository) {
		this.repository = repository;
	}


	public void deleteBooking(UUID id) {
		Booking booking = this.repository.findById(id).orElseThrow(NoSuchElementException::new);
		this.repository.delete(booking);
	}

	public Booking updateBooking(UUID id, Booking booking) {
		Booking existingBooking = this.repository.findById(id).orElseThrow(NoSuchElementException::new);
		int totalOverlapping = this.repository.countBookingsOverlapping(booking.getId(), booking.getStart(), booking.getEnd());
		if (totalOverlapping > 0) {
			throw new OverlappingBookingException();
		}
		existingBooking.setStart(booking.getStart());
		existingBooking.setEnd(booking.getEnd());
		existingBooking.setName(booking.getName());
		return this.repository.save(existingBooking);
	}

	public Booking createBooking(Booking booking) {
		int totalOverlapping = this.repository.countBookingsOverlapping(null, booking.getStart(), booking.getEnd());
		if (totalOverlapping > 0) {
			throw new OverlappingBookingException();
		}
		return this.repository.save(booking);
	}

	public Booking getBookingById(UUID id) {
		return this.repository.findById(id).orElseThrow();
	}

	public List<Booking> getAllBookings() {
		return this.repository.findAll();
	}

	public Booking cancelBooking(UUID id) {
		Booking booking = this.repository.findById(id).orElseThrow(NoSuchElementException::new);
		booking.setCanceled(true);
		return this.repository.save(booking);
	}
}
