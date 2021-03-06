package com.campsites.adapter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.campsites.advice.EntityNotFoundException;
import com.campsites.mapper.ReservationMapper;
import com.campsites.model.Reservation;
import com.campsites.model.Reservation;

@Component
public class ReservationAdapterImpl implements ReservationAdapter {

	@Autowired
	private ReservationMapper reservationMapper;
	
	public Reservation makeReservation(Reservation reservation) {

		validateReservation(reservation);
		
		//This currently does *not* handle concurrent requests cleanly
		//What I would do is have an event handler (Kafka perhaps) partitioned by campsiteId, to handle incoming events per campsite sequentially
		//However, this feels a bit beyond scope for an example project.
		try {
			reservationMapper.makeReservation(reservation);
		} catch(TooManyResultsException e) {
			throw new ValidationException("Reservation conflicts with an existing reservation");
		}
		
		return reservationMapper.getReservationByNameAndDate(reservation.getName(), reservation.getStartDate());
		
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
		
		checkDateRange(res.getStartDate(), res.getEndDate());
		
		//Check if any overlap from existing reservations
		if(!reservationMapper.getReservationsInDateRange(res.getCampsiteId(), res.getStartDate(), res.getEndDate()).isEmpty()) {
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
		
		LocalDate inAMonth = today.plus(31, ChronoUnit.DAYS);
		
		if(startDate.isEqual(inAMonth) || startDate.isAfter(inAMonth)) {
			throw new ValidationException("Reservations can only be made a month in advance");
		}
		
		if(endDate.isEqual(inAMonth) || endDate.isAfter(inAMonth)) {
			throw new ValidationException("Reservations can only be made a month in advance");
		}
		
	}

	public List<Reservation> getReservations() {
		List<Reservation> reses = reservationMapper.getReservations();
		List<Reservation> response = new ArrayList<>();
		
		for(Reservation res : reses) {
			response.add(res);
		}
		return response;
	}

	public Reservation getReservation(Integer reservationId) {
		return getReservationById(reservationId);
	}

	@Override
	public void deleteReservation(Integer reservationId) {
		
		//Check if requested reservation exists
		getReservationById(reservationId);
		
		reservationMapper.deleteReservation(reservationId);
		
	}
	
	private Reservation getReservationById(Integer reservationId) {
		
		Reservation res = reservationMapper.getReservation(reservationId);
		
		if(res == null) throw new EntityNotFoundException(String.format("Reservation with id %d not found", reservationId));
		
		return res;
		
	}

	@Override
	public Reservation updateReservation(Integer reservationId, Reservation reservation) {

		if(reservation.getStartDate() == null && reservation.getEndDate() == null) {
			throw new ValidationException("startDate or endDate are required for reservation update");
		}
		
		Reservation res = getReservationById(reservationId);
		
		if(res == null) {
			throw new EntityNotFoundException(String.format("Reservation with id %d not found", reservationId));
		}
		
		LocalDate start = null;
		LocalDate end = null;
		
		try {
			//Need to compare the 'new' date range. Only start *or* end might be provided.
			start = reservation.getStartDate() == null ? res.getStartDate() : reservation.getStartDate();
			end = reservation.getEndDate() == null ? res.getEndDate() : reservation.getEndDate();
		} catch(DateTimeParseException e) {
			throw new ValidationException("Date format is 'YYYY-MM-DD'");
		}
		
		checkDateRange(start, end);
		
		//Check if any overlap from existing reservations, that are not itself
		List<Reservation> reses = reservationMapper.getReservationsInDateRange(res.getCampsiteId(), start, end);
		if(!reses.isEmpty() && (reses.get(0).getId().equals(res.getId()))) {
			throw new ValidationException("Reservation conflicts with existing reservation - Please check available dates");
		}
		
		reservationMapper.updateReservationDates(reservationId, start, end);
		
		return getReservationById(reservationId);
		
	}

}
