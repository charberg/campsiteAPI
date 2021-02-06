package com.campsites.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.campsites.model.ReservationDTO;

public interface ReservationMapper {
	
	@Select("SELECT * FROM RESERVATIONS")
	public List<ReservationDTO> getReservations();
	
	@Select("SELECT * FROM RESERVATIONS WHERE id=#{id}")
	public ReservationDTO getReservation(@Param("id") Integer id);
	
	@Select("SELECT * FROM RESERVATIONS WHERE username=#{username} AND startDate=#{start}")
	public ReservationDTO getReservationByUsernameAndDate(@Param("username") String username, @Param("start") LocalDate startDate);
	
	@Insert("INSERT INTO RESERVATIONS (campsiteId, username, startDate, endDate) VALUES (#{res.campsiteId}, #{res.username}, #{res.startDate}, #{res.endDate})")
	public Integer makeReservation(@Param("res") ReservationDTO res);
	
}
