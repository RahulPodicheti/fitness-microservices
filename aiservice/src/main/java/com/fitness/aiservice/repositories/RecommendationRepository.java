package com.fitness.aiservice.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fitness.aiservice.model.Recommendation;

public interface RecommendationRepository extends MongoRepository<Recommendation, String> {

	List<Recommendation> findByUserId(String userId);

	Recommendation findByActivityId(String activityId);

}
