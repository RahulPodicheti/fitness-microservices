package com.fitness.activityservice.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repositories.ActivityRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

	private final ActivityRepository activityRepository;
	private final UserValidationService userValidationService;
	private final RabbitTemplate rabbitTemplate;
	
	@Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

	public ActivityResponse trackActivity(ActivityRequest request) {
		
		boolean isValidUser = userValidationService.validateUser(request.getUserId());
		if(!isValidUser) {
			throw new RuntimeException("Invalid User: "+request.getUserId());
		}
		
		Activity activity = Activity.builder()
				.additionalMetrices(request.getAdditionalMetrices())
				.calariesBurned(request.getCalariesBurned())
				.duration(request.getDuration())
				.startTime(request.getStartTime())
				.type(request.getType())
				.userId(request.getUserId())
				.build();
		Activity savedActivity = activityRepository.save(activity);
		
//		Publish to RabbitMq for AI Processing
		try {
			rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);
		} catch (Exception e) {
			log.error("Faliled to publish activity to RabbitMq : ", e);
		}
		
		return mapToResponse(savedActivity);
	}

	private ActivityResponse mapToResponse(Activity savedActivity) {
		ActivityResponse activityResponse = new ActivityResponse();
		activityResponse.setAdditionalMetrices(savedActivity.getAdditionalMetrices());
		activityResponse.setCalariesBurned(savedActivity.getCalariesBurned());
		activityResponse.setDuration(savedActivity.getDuration());
		activityResponse.setId(savedActivity.getId());
		activityResponse.setStartedAt(savedActivity.getStartedAt());
		activityResponse.setStartTime(savedActivity.getStartTime());
		activityResponse.setType(savedActivity.getType());
		activityResponse.setUpdatedAt(savedActivity.getUpdatedAt());
		activityResponse.setUserId(savedActivity.getUserId());
		return activityResponse;
	}

	public List<ActivityResponse> getUserActivities(String userId) {
		List<Activity> activities = activityRepository.findByUserId(userId);
		return activities.stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	public ActivityResponse getActivityById(String activityId) {
		return activityRepository.findById(activityId)
				.map(this::mapToResponse)
				.orElseThrow(()->new RuntimeException("Activity not found with id: "+activityId));
	}

	public List<ActivityResponse> getActivitiesByUsedId(String userId) {
		List<Activity> activities = activityRepository.findByUserId(userId);
		return activities.stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}
}
