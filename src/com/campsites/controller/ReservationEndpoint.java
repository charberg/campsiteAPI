package com.campsites.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	//Patch endpoint, as I'm only allowing start/end dates to be updated
	//Figure since users wouldn't be able to modify each others reservations in a real system with auth
	//Only let them update what they would actually be able to here
	@PatchMapping("{id}")
	public Reservation updateReservation(@PathVariable Integer id, @RequestBody Reservation reservation) {
		return reservationAdapter.updateReservation(id, reservation);
	}
	
	@DeleteMapping("{id}")
	public void deleteReservation(@PathVariable Integer id) {
		reservationAdapter.deleteReservation(id);
	}
	
}
