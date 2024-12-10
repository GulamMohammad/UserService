package com.user.service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.user.service.entity.Hotel;

@FeignClient("HOTEL-SERVICE")
public interface FeignClientHotelService {
	
	@GetMapping("/hotels/{hotelId}")
	public ResponseEntity<Hotel> getHotelgByHotelId(@PathVariable String hotelId);

}
