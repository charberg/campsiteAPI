package com.campsites.adapter;

import java.time.LocalDate;
import java.util.List;

import com.campsites.model.AvailabilityRequest;

public interface CampsiteAdapter {

	public List<LocalDate> getCampsiteAvailability(AvailabilityRequest request);
	
}
