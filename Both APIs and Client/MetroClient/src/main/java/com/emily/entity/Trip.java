package com.emily.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Trip {	

	@Id
    private int tripId;

    private int customerId;
    private int swipeInStationId;
    private int swipeOutStationId;
    private LocalDateTime swipeInDateAndTime;
    private LocalDateTime swipeOutDateAndTime;
    private double tripFare;
    
    
	public Trip(int customerId,int swipeInStation, LocalDateTime swipeInDateAnTime) {
		
		this.customerId = customerId;
		this.swipeInStationId = swipeInStation;
		this.swipeInDateAndTime = swipeInDateAndTime;
	}


}
