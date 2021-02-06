package com.campsites.adapter;

import java.util.List;

import com.campsites.model.Reservation;

public interface ReservationAdapter {

	public List<Reservation> getReservations();
	
	public Reservation getReservation(Integer reservationId);
	
	public Reservation makeReservation(Reservation reservation);
	
}
