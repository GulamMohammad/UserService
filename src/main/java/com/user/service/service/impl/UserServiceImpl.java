package com.user.service.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.user.service.entity.Hotel;
import com.user.service.entity.Rating;
import com.user.service.entity.User;
import com.user.service.repository.UserRespository;
import com.user.service.service.FeignClientHotelService;
import com.user.service.service.FeignClientRatingService;
import com.user.service.service.UserService;
import com.user.service.service.exceptions.ResourceNotFoundException;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRespository repo;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private FeignClientRatingService ratingService;
	
	@Autowired
	private FeignClientHotelService hotelService;

	@Override
	public User createUser(User user) {
		String userId = UUID.randomUUID().toString();
		user.setUserId(userId);
		return repo.save(user);
	}

	@Override
	public User getSingleUser(String userId) {
		
		User user = repo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not exist with given id:"+userId));
		
		//To get the Ratings details based on UserId 
		
		//String ratingServiceUrl = "http://RATING-SERVICE/ratings/users/"+user.getUserId();
		
		//Rating[] ratingsList = restTemplate.getForObject(ratingServiceUrl, Rating[].class);
		//using FeignClient
		List<Rating> ratingList = ratingService.ratingList();
		
		//List<Rating> ratings = Arrays.stream(ratingsList).toList();
		
		//To get the Hotel details based on hotelId
		ratingList = ratingList.stream().map(rating ->{
				
		//Api call to get HotelDetails Using HOTEL-SERVICE
		//url : http://localhost:8082/hotel/hotelId
			
		//ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
		ResponseEntity<Hotel> hotelEntity = hotelService.getHotelgByHotelId(rating.getHotelId());
		
		Hotel hotel = hotelEntity.getBody();
		rating.setHotel(hotel);
		return rating;
		
		}).toList();
		
		user.setRatings(ratingList);
		
		return user;
	}

	//Get the Details Using FeignClientS
	@Override
	public List<User> getAllUsers() {
		
		List<User> userList = repo.findAll();
		
		
		return userList.stream().map(user ->{
			
			//Get the All Rating Details Associated by Users 
			
			List<Rating> ratings = ratingService.ratingList();
			
			
			//Convert to the array to list
			List<Rating> ratingList = ratings.stream().map(rating ->{
				try {
					//Get the Hotel Details based on hotelId
				 ResponseEntity<Hotel> hotelEntity = hotelService.getHotelgByHotelId(rating.getHotelId());
				 Hotel hotel = hotelEntity.getBody();
				
				rating.setHotel(hotel);
				}catch (Exception e) {
					System.err.println("Error fetching hotel details: " + e.getMessage());
	                rating.setHotel(null); // Set to null if fetching fails
				}
				return rating;
			}).toList();
			user.setRatings(ratingList);
			return user;
		}).toList();
	}

	/*
	@Override
	public List<User> getAllUsers() {
		
		List<User> userList = repo.findAll();
		
		return userList.stream().map(user ->{
			
			//Get the All Rating Details Associated by Users 
			//Rating[] ratingArray = restTemplate.getForObject("http://RATING-SERVICE/ratings"+user, Rating[].class);
			Rating[] ratingArray = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(), Rating[].class);
			
			
			//Convert to the array to list
			List<Rating> ratingList = Arrays.stream(ratingArray).map(rating ->{
				try {
					//Get the Hotel Details based on hotelId
				 ResponseEntity<Hotel> hotelObject = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
				 Hotel hotel = hotelObject.getBody();
				
				rating.setHotel(hotel);
				}catch (Exception e) {
					System.err.println("Error fetching hotel details: " + e.getMessage());
	                rating.setHotel(null); // Set to null if fetching fails
				}
				return rating;
			}).toList();
			user.setRatings(ratingList);
			return user;
		}).toList();
	}
	*/

}
