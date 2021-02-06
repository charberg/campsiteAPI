package com.campsites.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.campsites.model.ReservationDTO;

public interface ReservationMapper {
	
	@Select("SELECT * FROM RESERVATIONS")
	public List<ReservationDTO> getReservations();
	
	@Select("SELECT * FROM RESERVATIONS WHERE id=#{id}")
	public ReservationDTO getReservation(@Param("id") Integer id);
	
	@Select("SELECT * FROM RESERVATIONS WHERE name=#{name} AND startDate=#{start}")
	public ReservationDTO getReservationByNameAndDate(@Param("name") String name, @Param("start") LocalDate startDate);
	
	@Insert("INSERT INTO RESERVATIONS (campsiteId, name, email, startDate, endDate) VALUES (#{res.campsiteId}, #{res.name}, #{res.email}, #{res.startDate}, #{res.endDate})")
	public void makeReservation(@Param("res") ReservationDTO res);
	
	@Update("UPDATE RESERVATIONS SET startDate = #{start}, endDate = #{end} WHERE id = #{id}")
	public void updateReservationDates(@Param("id") Integer id, @Param("start") LocalDate start, @Param("end") LocalDate end);
	
	@Delete("DELETE FROM RESERVATIONS WHERE id=#{id}")
	public void deleteReservation(@Param("id") Integer id);
	
	@Select("SELECT * FROM RESERVATIONS WHERE campsiteId = #{campsiteId} AND (startDate BETWEEN #{start} AND #{end}) OR (endDate BETWEEN #{start} AND #{end}) ORDER BY startDate ASC")
	public List<ReservationDTO> getReservationsInDateRange(@Param("campsiteId") Integer campsiteId, @Param("start") LocalDate start, @Param("end") LocalDate end);
	
}
