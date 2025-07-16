package com.fitness.activityservice.dto;

import java.time.LocalDateTime;
import java.util.Map;


import com.fitness.activityservice.model.ActivityType;

import lombok.Data;

@Data
public class ActivityResponse {
	private String id;
	private String userId;
	private ActivityType type;
	private Integer duration;
	private Integer calariesBurned;
	private Map<String, Object> additionalMetrices;
	private LocalDateTime startTime;
	private LocalDateTime startedAt;
	private LocalDateTime updatedAt;
}
