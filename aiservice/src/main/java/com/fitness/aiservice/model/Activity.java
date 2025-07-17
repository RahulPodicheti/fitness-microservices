package com.fitness.aiservice.model;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Activity {
	
	@Id
	private String id;
	private String userId;
	private Integer duration;
	private Integer calariesBurned;
	private Map<String, Object> additionalMetrices;
	private LocalDateTime startTime;
	private LocalDateTime startedAt;
	private LocalDateTime updatedAt;
}
