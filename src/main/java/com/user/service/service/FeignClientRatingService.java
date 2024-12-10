package com.user.service.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.user.service.entity.Hotel;
import com.user.service.entity.Rating;

@FeignClient("RATING-SERVICE")
public interface FeignClientRatingService {
	
	
	@GetMapping("/ratings")
	public List<Rating> ratingList();
	
	@GetMapping("/ratings/users/{userId}")
	public Rating getRatingByRatingId(@PathVariable String userId);


}
