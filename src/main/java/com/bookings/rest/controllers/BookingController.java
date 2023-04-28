package com.bookings.rest.controllers;

import com.bookings.rest.exceptions.OverlappingBookingException;
import com.bookings.rest.models.Booking;
import com.bookings.rest.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/bookings")
public class BookingController {

	private final BookingService service;

	@Autowired
	public BookingController(BookingService service) {
		this.service = service;
	}

	@GetMapping("")
	public List<Booking> getAllBookings() {
		return this.service.getAllBookings();
	}

	@GetMapping("/{id}")
	public Booking getBookingById(@PathVariable UUID id) {
		try {
			return this.service.getBookingById(id);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Booking not found", e);
		}
	}

	@PostMapping("")
	public Booking createBooking(@RequestBody Booking booking) {
		try {
			return this.service.createBooking(booking);
		} catch (OverlappingBookingException e) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "There is already a booking for this period", e);
		}
	}

	@PostMapping("/{id}")
	public Booking updateBooking(@PathVariable UUID id, @RequestBody Booking booking) {
		try {
			return this.service.updateBooking(id, booking);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Booking not found", e);
		} catch (RuntimeException e) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "There is already a booking for this period", e);
		}
	}

	@PostMapping("/{id}/cancel")
	public Booking cancelBooking(@PathVariable UUID id) {
		try {
			return this.service.cancelBooking(id);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Booking not found", e);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteBooking(@PathVariable UUID id) {
		try {
			this.service.deleteBooking(id);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Booking not found", e);
		}
		return ResponseEntity.ok().build();
	}


}
