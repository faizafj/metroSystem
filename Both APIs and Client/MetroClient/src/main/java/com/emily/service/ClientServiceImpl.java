package com.emily.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.emily.entity.StationList;
import com.emily.entity.Trip;
import com.emily.persistence.TripDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.emily.entity.Bill;
import com.emily.entity.Customer;
import com.emily.entity.Station;

@Service
public class ClientServiceImpl implements ClientService {
	
	// Import RestTemplate to call Rest API - to connect to customer database
	@Autowired
	private RestTemplate restTemplate;
	
	// Instantiate dao layer
	@Autowired
	private TripDao dao;

	@Override
	public Customer loginCheck(int id) {	
		try {
			// Call API to search for customer with inputed Id
			Customer customer = restTemplate.getForObject("http://localhost:8089/customers/"+id, Customer.class);
			return customer;
		} catch(Exception exception) {
			return null;
		}	
	}

	@Override
	public Customer addNewCustomer(Customer customer) {
		try {
			Customer newCustomer = restTemplate.postForObject("http://localhost:8089/customers", customer, Customer.class);
			return newCustomer;
		} catch(Exception exception) {
			return null;
		}
	}
	
	//tapIn method-return new Trip object or null, if the customer does not have enough balance
	public Trip tapIn(Customer customer, int swipeInStationId) {
			
		if(customer.getCustomerBalance()<5) {
			return null;
		}
				
		Trip trip = new Trip(customer.getCustomerId(), swipeInStationId, LocalDateTime.now());
		System.out.print("Swipe in trip: " + trip);
		dao.save(trip);
		return trip;
	}
			
			
	// Get all Stations
	@Override
	public Collection<Station> getAllStations() {
		try {
			StationList allStations = restTemplate.getForObject("http://localhost:8082/allStations", StationList.class);
			Collection<Station> listStations = allStations.getStationList();
			return listStations;
		} catch(Exception exception) {
			return null;
		}
	}
			
	@Override
	public Customer deductCustomerBalance(int customerId, double amount) {
		try {
			Customer customer = loginCheck(customerId);
			restTemplate.put("http://localhost:8089/customers/deduct/"+customerId+ "/" + amount, Customer.class);
			Customer newCustomer = loginCheck(customerId);
				
			if(customer.getCustomerBalance()<newCustomer.getCustomerBalance()) {
				return newCustomer;
			}
		}catch(Exception e) {
			return null;	
		}
		return null;
	}
			
	//get station by station id
	public Station getStationById(int stationId) {
		Collection<Station> allStations = getAllStations();
				
		for(Station station: allStations) {
			if(station.getStationId()==stationId) {
				return station;
			}
		}
		return null;
	}

	//calculate total travel price
	@Override
	public double calculatePrice(int startingPoint, int finishingPoint, double price) {
		System.out.println(startingPoint);
		System.out.println(finishingPoint);
		return ((startingPoint - finishingPoint)*price);
	}

	//finish the trip and produce customer bill for displaying
	@Override
	public Bill tapOut(int swipeOutStationId,Customer customer,int tripId) {
			
//		List<Trip> tripList = dao.findTripsByCustomerId(customer.getCustomerId());
		Trip trip=dao.findById(tripId).orElse(null);
		System.out.print("Trip object: tap out from service" + trip);
//		for(Trip trip:tripList) {
			if (trip!=null) {
				
				int swipeInStationId = trip.getSwipeInStationId();
				String swipeInStationName = getStationById(swipeInStationId).getStationName();
				String swipeOutStationName=getStationById(swipeOutStationId).getStationName();
				
				double amountToPay = calculatePrice(swipeOutStationId,swipeInStationId ,5);

//				amountToPay = amountToPay * -1;
				System.out.println("This is the amount: " + amountToPay);
				
				Customer newCustomer = deductCustomerBalance(customer.getCustomerId(), amountToPay);
				if(newCustomer !=null) {
				double newBalance = newCustomer.getCustomerBalance();
				
				Bill bill = new Bill(customer.getCustomerFirstName(), customer.getCustomerSurname(),swipeInStationName,swipeOutStationName , amountToPay, newBalance);
				trip.setSwipeOutStationId(swipeOutStationId);
				trip.setSwipeOutDateAndTime(LocalDateTime.now());
				trip.setTripFare(amountToPay);
				
				dao.save(trip);
				return bill;

				}//end of if customer !=null
				
			}//end of if trip.getTripId()==tripId
					
		//end of for loop
		return null;				
	}
			
//  Emily's code			
//	@Override
//	public StationList getAllStations() {
//		try {
//			StationList allStations = restTemplate.getForObject("http://localhost:8082/allStations", StationList.class);
//			return allStations;
//		} catch(Exception exception) {
//			return null;
//		}
//	}
}