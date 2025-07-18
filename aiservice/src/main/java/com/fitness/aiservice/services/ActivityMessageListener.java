package com.fitness.aiservice.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repositories.RecommendationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {
	
	private final ActivityAiService aiService;
	private final RecommendationRepository recommendationRepository;
	
	@RabbitListener(queues = "activity.queue")
	public void processActivity(Activity activity) {
		log.info("Recieed actiity for processing: {}", activity.getId());
//		log.info("Generated Recommendation: {}", aiService.generateRecommendation(activity));
		Recommendation recommendation = aiService.generateRecommendation(activity);
		recommendationRepository.save(recommendation);
	}
}
