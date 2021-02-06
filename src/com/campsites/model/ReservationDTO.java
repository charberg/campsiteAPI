package com.campsites.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class ReservationDTO {
	
	private Integer id;
	private Integer campsiteId;
	private String name;
	private String email;
	private LocalDate startDate;
	private LocalDate endDate;
	
	public ReservationDTO() {
		
	}
	
	public ReservationDTO(Reservation res) throws ParseException {
		this.id = res.getId();
		this.campsiteId = res.getCampsiteId();
		this.name = res.getName();
		this.email = res.getEmail();
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
