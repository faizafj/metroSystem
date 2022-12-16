package com.emily.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emily.entity.Trip;

@Repository
public interface TripDao extends JpaRepository<Trip, Integer>{

	public List<Trip> findTripsByCustomerId(int customerId);
}