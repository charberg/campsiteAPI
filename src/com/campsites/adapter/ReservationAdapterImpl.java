package com.campsites.adapter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.campsites.advice.EntityNotFoundException;
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
		
		return new Reservation(reservationMapper.getReservationByNameAndDate(reservation.getName(), LocalDate.parse(reservation.getStartDate())));
		
	}
	
	private void validateReservation(Reservation res) {
		
		if(StringUtils.isBlank(res.getName())) {
			throw new ValidationException("name field cannot be blank");
		}
		
		if(StringUtils.isBlank(res.getEmail())) {
			throw new ValidationException("email field cannot be blank");
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
		
		LocalDate start = null;
		LocalDate end = null;
		
		try {
			start = LocalDate.parse(res.getStartDate());
			end = LocalDate.parse(res.getEndDate());
		} catch(DateTimeParseException e) {
			throw new ValidationException("Date format is 'YYYY-MM-DD'");
		}
		
		checkDateRange(start, end);
		
		//Check if any overlap from existing reservations
		if(!reservationMapper.getReservationsInDateRange(start, end).isEmpty()) {
			throw new ValidationException("Reservation conflicts with existing reservation - Please check available dates");
		}
		
	}
	
	public void checkDateRange(LocalDate startDate, LocalDate endDate) {
		//Cut off times if specified
		LocalDate today = LocalDate.now();
		
		if(startDate.isBefore(today) || startDate.isEqual(today)) {
			throw new ValidationException("startDate must be ahead of today");
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
		return new Reservation(getReservationById(reservationId));
	}

	@Override
	public void deleteReservation(Integer reservationId) {
		
		//Check if requested reservation exists
		getReservationById(reservationId);
		
		reservationMapper.deleteReservation(reservationId);
		
	}
	
	private ReservationDTO getReservationById(Integer reservationId) {
		
		ReservationDTO res = reservationMapper.getReservation(reservationId);
		
		if(res == null) throw new EntityNotFoundException(String.format("Reservation with id %d not found", reservationId));
		
		return res;
		
	}

	@Override
	public Reservation updateReservation(Integer reservationId, Reservation reservation) {

		if(StringUtils.isBlank(reservation.getStartDate()) && StringUtils.isBlank(reservation.getEndDate())) {
			throw new ValidationException("startDate or endDate are required for reservation update");
		}
		
		ReservationDTO res = getReservationById(reservationId);
		
		LocalDate start = null;
		LocalDate end = null;
		
		try {
			//Need to compare the 'new' date range. Only start *or* end might be provided.
			start = StringUtils.isBlank(reservation.getStartDate()) ? res.getStartDate() : LocalDate.parse(reservation.getStartDate());
			end = StringUtils.isBlank(reservation.getEndDate()) ? res.getEndDate() : LocalDate.parse(reservation.getEndDate());
		} catch(DateTimeParseException e) {
			throw new ValidationException("Date format is 'YYYY-MM-DD'");
		}
		
		checkDateRange(start, end);
		
		//Check if any overlap from existing reservations, that are not itself
		List<ReservationDTO> reses = reservationMapper.getReservationsInDateRange(start, end);
		if(!reses.isEmpty() && (reses.get(0).getId().equals(res.getId()))) {
			throw new ValidationException("Reservation conflicts with existing reservation - Please check available dates");
		}
		
		reservationMapper.updateReservationDates(reservationId, start, end);
		
		return new Reservation(getReservationById(reservationId));
		
	}

}
