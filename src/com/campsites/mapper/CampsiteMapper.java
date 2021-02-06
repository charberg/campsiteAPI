package com.campsites.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.campsites.model.Campsite;

public interface CampsiteMapper {

	@Select("SELECT * FROM CAMPSITES WHERE id = #{id}")
	public Campsite getCampsite(@Param("id") Integer id);
	
	@Select("SELECT * FROM CAMPSITES")
	public List<Campsite> getCampsites();
	
}
