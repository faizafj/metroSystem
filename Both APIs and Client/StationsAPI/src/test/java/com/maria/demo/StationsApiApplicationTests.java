package com.maria.demo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.maria.entity.Station;
import com.maria.persistence.StationDao;
import com.maria.service.StationServiceImpl;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class StationsApiApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@InjectMocks
	private StationServiceImpl service;
	@Mock
	private StationDao dao;
	private AutoCloseable autoCloseable;
	
	@BeforeEach
	void setUp() throws Exception {
		autoCloseable = MockitoAnnotations.openMocks(this);

	}

	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}
	
	@Test
	@Order(1)
	public void testGetAllStations() {
		Collection<Station> myCollection = null;
		when(dao.findAll()).thenReturn((List<Station>) myCollection);
		assertEquals(service.getAllStations(), myCollection);
	}

}
