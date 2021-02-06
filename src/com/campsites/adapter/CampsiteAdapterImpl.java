package com.campsites.adapter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.campsites.advice.EntityNotFoundException;
import com.campsites.mapper.CampsiteMapper;
import com.campsites.mapper.ReservationMapper;
import com.campsites.model.AvailabilityRequest;
import com.campsites.model.ReservationDTO;

@Component
public class CampsiteAdapterImpl implements CampsiteAdapter {

	@Autowired
	private CampsiteMapper campsiteMapper;
	
	@Autowired
	private ReservationMapper reservationMapper;
	
	@Override
	public List<LocalDate> getCampsiteAvailability(AvailabilityRequest request) {
		
		if(request.getCampsiteId() == null) {
			throw new ValidationException("campsiteId is a required field");
		}
		
		if(campsiteMapper.getCampsite(request.getCampsiteId()) == null) {
			throw new EntityNotFoundException(String.format("Campsite with id %d not found", request.getCampsiteId()));
		}
		
		//Default to startDate of tomorrow (as today cannot be booked)
		LocalDate today = LocalDate.now();
		LocalDate start = request.getStartDate() == null ? LocalDate.now().plus(1, ChronoUnit.DAYS): request.getStartDate();
		LocalDate end = request.getEndDate();
		
		//End date can be specified, a specified interval away, or default to 30 days after tomorrow
		if(end == null && request.getDays() != null) {
			if(request.getDays() > 0 && request.getDays() <= 30) {
				end = start.plus(request.getDays(), ChronoUnit.DAYS);
			} else {
				throw new ValidationException("Days must be a positive number, with a max value of 30");
			}
		} else if(end == null){
			end = start.plus(31, ChronoUnit.DAYS);
		}
		
		if(start.isBefore(today) || start.isEqual(today)) {
			throw new ValidationException("startDate must be in the future");
		}
		
		if(end.isBefore(today) || end.isEqual(today)) {
			throw new ValidationException("endDate must be in the future");
		}
		
		//Get reservations in date range, to find the difference
		//This method returns in a sorted order, by startDate, ascending
		List<ReservationDTO> reses = reservationMapper.getReservationsInDateRange(request.getCampsiteId(), start, end);
		
		//Iterate through the reservations. If no reservation on a date in our range, add date to our response
		//If there is a reservation, skip
		
		Iterator<ReservationDTO> it = reses.iterator();
		ReservationDTO nextRes = it.hasNext() ? it.next() : null;
		List<LocalDate> response = new ArrayList<>();
		
		while(!start.isAfter(end)) {
			
			if(nextRes != null) {	//If there is an upcoming reservation, check if the current date looked at conflicts
				if(hasConflict(start, nextRes.getStartDate(), nextRes.getEndDate())) {	//If conflict, up our current date to check, and move on
					if(start.isEqual(nextRes.getEndDate())) {
						nextRes = it.hasNext() ? it.next() : null;	//If the current checked date is the end of the reservation, grab the next reservation (if there is one)
					}
					start = start.plus(1, ChronoUnit.DAYS);
					continue;
				}
			}
			
			response.add(start);
			start = start.plus(1, ChronoUnit.DAYS);
			
		}
		
		return response;
		
	}
	
	private boolean hasConflict(LocalDate date, LocalDate start, LocalDate end) {
		
		return (date.isEqual(start) || date.isEqual(end) || (date.isAfter(start) && date.isBefore(end)));
		
	}
	
}
