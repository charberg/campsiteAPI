package com.campsites.adapter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.campsites.mapper.ReservationMapper;
import com.campsites.model.Reservation;
import com.campsites.model.ReservationDTO;

@Component
public class ReservationAdapterImpl implements ReservationAdapter {

	@Autowired
	private ReservationMapper reservationMapper;
	
	public Reservation makeReservation(Reservation reservation) {

		validateReservation(reservation);
		
		Integer id;
		try {
			id = reservationMapper.makeReservation(new ReservationDTO(reservation));
		} catch (ParseException e) {
			throw new RuntimeException("Unexpected date parse exception");
		}
		
		reservationMapper.getReservation(id);
		
		return new Reservation(reservationMapper.getReservationByUsernameAndDate(reservation.getUsername(), LocalDate.parse(reservation.getStartDate())));
		
	}
	
	private void validateReservation(Reservation res) {
		
		if(StringUtils.isBlank(res.getUsername())) {
			throw new ValidationException("username field cannot be blank");
		}
		
		if(res.getCampsiteId() == null) {
			throw new ValidationException("campsiteId field cannot be blank");
		}
		
		if(res.getStartDate() == null) {
			throw new ValidationException("startDate field cannot be blank");
		}
		
		if(res.getEndDate() == null) {
			throw new ValidationException("endDate field cannot be blank");
		}
		
		//Cut off times if specified
		LocalDate today = LocalDate.now();
		LocalDate startDate = null;
		LocalDate endDate = null;
		try {
			startDate = LocalDate.parse(res.getStartDate());
			endDate = LocalDate.parse(res.getEndDate());
		} catch(DateTimeParseException e) {
			throw new ValidationException("Date format is 'YYYY-MM-DD'");
		}
		
		if(startDate.isBefore(today)) {
			throw new ValidationException("startDate cannot be in the past");
		}
		
		if(endDate.isBefore(startDate)) {
			throw new ValidationException("endDate cannot be before startDate");
		}
		
		if(ChronoUnit.DAYS.between(startDate, endDate) >= 3) {
			throw new ValidationException("Reservations have a maximum length of 3 days");
		}
		
	}

	public List<Reservation> getReservations() {
		List<ReservationDTO> reses = reservationMapper.getReservations();
		List<Reservation> response = new ArrayList<>();
		
		for(ReservationDTO res : reses) {
			response.add(new Reservation(res));
		}
		return response;
	}

	public Reservation getReservation(Integer reservationId) {
		return new Reservation(reservationMapper.getReservation(reservationId));
	}

}
