package com.campsites.model;

import java.time.LocalDate;

public class AvailabilityRequest {

	private Integer campsiteId;
	private LocalDate startDate;
	private LocalDate endDate;
	private Integer days;
	
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
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public Integer getCampsiteId() {
		return campsiteId;
	}
	public void setCampsiteId(Integer campsiteId) {
		this.campsiteId = campsiteId;
	}
	
}
