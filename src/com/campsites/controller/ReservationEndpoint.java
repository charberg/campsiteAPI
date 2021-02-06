package com.campsites.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campsites.adapter.ReservationAdapter;
import com.campsites.model.Reservation;

@RestController
@RequestMapping("/reservations")
public class ReservationEndpoint {
	
	@Autowired
	private ReservationAdapter reservationAdapter;
	
	@GetMapping()
	public List<Reservation> getReservations() {
		return reservationAdapter.getReservations();
	}
	
	@GetMapping("{id}")
	public Reservation getReservation(@PathVariable Integer id) {
		return reservationAdapter.getReservation(id);
	}
	
	@PostMapping()
	public Reservation makeReservation(@RequestBody Reservation reservation) {
		return reservationAdapter.makeReservation(reservation);
	}
	
}
