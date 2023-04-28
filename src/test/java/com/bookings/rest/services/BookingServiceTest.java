package com.bookings.rest.services;

import com.bookings.rest.exceptions.OverlappingBookingException;
import com.bookings.rest.models.Booking;
import com.bookings.rest.repositories.BookingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class BookingServiceTest {

	@Autowired
	private BookingRepository repository;

	@Autowired
	private BookingService service;


	@BeforeEach
	public void setUp() {
		Booking booking = new Booking();
		booking.setName("Test Booking");
		booking.setStart(LocalDateTime.of(2023, 1, 1, 7, 0));
		booking.setEnd(LocalDateTime.of(2023, 1, 1, 8, 0));
		repository.save(booking);

		Booking block = new Booking();
		block.setStart(LocalDateTime.of(2023, 2, 1, 7, 0));
		block.setEnd(LocalDateTime.of(2023, 2, 1, 8, 0));
		block.setBlocked(true);
		repository.save(block);
	}

	@Test
	public void testGetAllBookings() {
		Assertions.assertEquals(2, this.service.getAllBookings().size());
	}

	@Test
	public void testExactlyOverlapping() {
		Booking overlap = new Booking();
		overlap.setName("Overlap");
		overlap.setStart(LocalDateTime.of(2023, 1, 1, 7, 0));
		overlap.setEnd(LocalDateTime.of(2023, 1, 1, 8, 0));
		Assertions.assertThrows(OverlappingBookingException.class, () -> {
			this.service.createBooking(overlap);
		});
	}
	@Test
	public void testStartOverlapping() {
		Booking overlap = new Booking();
		overlap.setName("Overlap");
		overlap.setStart(LocalDateTime.of(2023, 1, 1, 6, 30));
		overlap.setEnd(LocalDateTime.of(2023, 1, 1, 7, 30));
		Assertions.assertThrows(OverlappingBookingException.class, () -> {
			this.service.createBooking(overlap);
		});
	}

	@Test
	public void testEndOverlapping() {
		Booking overlap = new Booking();
		overlap.setName("Overlap");
		overlap.setStart(LocalDateTime.of(2023, 1, 1, 7, 30));
		overlap.setEnd(LocalDateTime.of(2023, 1, 1, 8, 30));
		Assertions.assertThrows(OverlappingBookingException.class, () -> {
			this.service.createBooking(overlap);
		});
	}

	@Test
	public void testInnerOverlapping() {
		Booking overlap = new Booking();
		overlap.setName("Overlap");
		overlap.setStart(LocalDateTime.of(2023, 1, 1, 7, 15));
		overlap.setEnd(LocalDateTime.of(2023, 1, 1, 8, 45));
		Assertions.assertThrows(OverlappingBookingException.class, () -> {
			this.service.createBooking(overlap);
		});
	}

	@Test
	public void testOuterOverlapping() {
		Booking overlap = new Booking();
		overlap.setName("Overlap");
		overlap.setStart(LocalDateTime.of(2023, 1, 1, 6, 0));
		overlap.setEnd(LocalDateTime.of(2023, 1, 1, 9, 0));
		Assertions.assertThrows(OverlappingBookingException.class, () -> {
			this.service.createBooking(overlap);
		});
	}


	@Test
	public void testCreateRightAfter() {
		Booking overlap = new Booking();
		overlap.setName("New Booking");
		overlap.setStart(LocalDateTime.of(2023, 1, 1, 8, 0));
		overlap.setEnd(LocalDateTime.of(2023, 1, 1, 9, 0));
		this.service.createBooking(overlap);
		Assertions.assertEquals(3, this.service.getAllBookings().size());
	}

	@Test
	public void testCreateRightBefore() {
		Booking overlap = new Booking();
		overlap.setName("New Booking");
		overlap.setStart(LocalDateTime.of(2023, 1, 1, 6, 0));
		overlap.setEnd(LocalDateTime.of(2023, 1, 1, 7, 0));
		this.service.createBooking(overlap);
		Assertions.assertEquals(3, this.service.getAllBookings().size());
	}
}
