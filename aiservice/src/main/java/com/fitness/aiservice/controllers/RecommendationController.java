package com.fitness.aiservice.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.services.RecommendationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/recommendations")
@AllArgsConstructor
public class RecommendationController {
	
	private RecommendationService recommendationService;
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Recommendation>> getUserRecommendations(@PathVariable String userId){
		return ResponseEntity.ok(recommendationService.findByUserId(userId));
	}
	
	@GetMapping("/activity/{activityId}")
	public ResponseEntity<Recommendation> getActivityRecommendations(@PathVariable String activityId){
		return ResponseEntity.ok(recommendationService.findByActivityId(activityId));
	}

}
