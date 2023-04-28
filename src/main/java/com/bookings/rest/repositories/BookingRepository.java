package com.bookings.rest.repositories;

import com.bookings.rest.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

	@Query("SELECT COUNT(b) " +
			"FROM Booking b " +
			"WHERE " +
			"(b.isCanceled = false) AND " +
			"(:id IS NULL OR :id != b.id) AND (" +
			"(:start <= b.start AND :end > b.start)" +
			" OR " +
			"(:start < b.end AND :end >= b.end)" +
			" OR " +
			"(:start >= b.start AND :end <= b.end)" +
			" OR " +
			"(:start < b.start AND :end > b.end))")
	int countBookingsOverlapping(UUID id, LocalDateTime start, LocalDateTime end);
}
