package com.fitness.activityservice.model;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "activities")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
	
	@Id
	private String id;
	private String userId;
	private ActivityType type;
	private Integer duration;
	private Integer calariesBurned;
	
	@Field("metrics")
	private Map<String, Object> additionalMetrices;
	
	private LocalDateTime startTime;
	
	@CreatedDate
	private LocalDateTime startedAt;
	
	@LastModifiedDate
	private LocalDateTime updatedAt;
}
