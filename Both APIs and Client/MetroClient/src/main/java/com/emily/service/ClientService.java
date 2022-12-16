package com.emily.service;

import java.util.Collection;

import com.emily.entity.Bill;
import com.emily.entity.Customer;
import com.emily.entity.Trip;
import com.emily.entity.Station;

public interface ClientService {

	Customer loginCheck(int id);
	Customer addNewCustomer(Customer customer);
	Collection<Station> getAllStations();
	double calculatePrice(int startingPoint, int finishingPoint,double price);
	Bill tapOut(int stationId, Customer customer,int tripId,double price);
	Customer deductCustomerBalance(int customerId, double amount);
	Station getStationById(int stationId);
	Trip tapIn(Customer customer, int swipeInStationId);

}