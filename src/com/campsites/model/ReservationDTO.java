package com.campsites.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class ReservationDTO {

	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
	
	private Integer id;
	private Integer campsiteId;
	private String username;
	private LocalDate startDate;
	private LocalDate endDate;
	
	public ReservationDTO() {
		
	}
	
	public ReservationDTO(Reservation res) throws ParseException {
		this.id = res.getId();
		this.campsiteId = res.getCampsiteId();
		this.username = res.getUsername();
		//this.startDate = formatter.parse(res.getStartDate());
		//this.endDate = formatter.parse(res.getEndDate());
		this.startDate = LocalDate.parse(res.getStartDate());
		this.endDate = LocalDate.parse(res.getEndDate());
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCampsiteId() {
		return campsiteId;
	}
	public void setCampsiteId(Integer campsiteId) {
		this.campsiteId = campsiteId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
}
