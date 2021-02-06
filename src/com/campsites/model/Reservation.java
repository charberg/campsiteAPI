package com.campsites.model;

public class Reservation {

	private Integer id;
	private String username;
	private Integer campsiteId;
	private String startDate;
	private String endDate;
	
	public Reservation() {
		
	}
	
	public Reservation(ReservationDTO res) {
		this.id = res.getId();
		this.campsiteId = res.getCampsiteId();
		this.username = res.getUsername();
		this.startDate = res.getStartDate().toString();
		this.endDate = res.getEndDate().toString();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getCampsiteId() {
		return campsiteId;
	}
	public void setCampsiteId(Integer campsiteId) {
		this.campsiteId = campsiteId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
}
