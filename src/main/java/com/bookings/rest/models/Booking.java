package com.bookings.rest.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private UUID id;

	private String name;

	@Column(nullable = false)
	private LocalDateTime start;

	@Column(nullable = false)
	private LocalDateTime end;

	private boolean isBlocked;

	private boolean isCanceled;

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean blocked) {
		isBlocked = blocked;
	}

	public boolean isCanceled() {
		return isCanceled;
	}

	public void setCanceled(boolean canceled) {
		isCanceled = canceled;
	}
}
