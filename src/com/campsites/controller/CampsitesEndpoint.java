package com.campsites.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.campsites.mapper.CampsiteMapper;
import com.campsites.model.Campsite;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/campsites")
public class CampsitesEndpoint {

	@Autowired
	private CampsiteMapper campsiteMapper;
	
	@GetMapping("{id}")
	Campsite getCampsite(@PathVariable Integer id) {
		return campsiteMapper.getCampsite(id);
	}
	
	@GetMapping()
	List<Campsite> getCampsites() {
		return campsiteMapper.getCampsites();
	}
	
}
