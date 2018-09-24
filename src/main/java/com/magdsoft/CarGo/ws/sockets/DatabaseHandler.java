package com.magdsoft.CarGo.ws.sockets;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.magdsoft.CarGo.ws.model.Car;
import com.magdsoft.CarGo.ws.model.Driver;
import com.magdsoft.CarGo.ws.model.DriverCar;
import com.magdsoft.CarGo.ws.model.User;
import com.magdsoft.CarGo.ws.model.UserTrip;

@Service
public class DatabaseHandler {
	@Autowired
	public EntityManager entityManager;

	@Transactional
	public Car getCarData(int driverId) {
		// Get the last car "started" for this driver
		Query q = entityManager.createQuery("select cardriver from DriverCar cardriver where cardriver.driver_id.id = :id "
			       + " order by cardriver.starteAt desc", DriverCar.class);
		// Make the query get the first result only
		q.setFirstResult(0);
		q.setMaxResults(1);
		
		q.setParameter("id", driverId);
		try {
			// Get the results
			List<DriverCar> driverCars = q.getResultList();
			// No results? (Empty list). So car is null!
			if (driverCars.size() == 0) return null;

			// Return the first result in the list, it represents the last car "started"
			return driverCars.get(0).getCar_id();
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Transactional
	public Driver getDriverData(int driverId) {
		return entityManager.find(Driver.class, driverId);
	}

	@Transactional
	public int getUserForTrip(int tripId) {
		UserTrip trip = entityManager.find(UserTrip.class, tripId);
		return trip.getUser_id().getId();
	}
	@Transactional
	public void closeCarForId(int carId) {
		Car car = entityManager.find(Car.class, carId);
		car.setOnLine(false);
		entityManager.persist(car);
	}

	@Transactional
	public Car getCarForId(int carId) {
		return entityManager.find(Car.class, carId);
	}

	@Transactional
	public Car openCarForId(int carId) {
		try {
			Car ret = entityManager.find(Car.class, carId);
			ret.setOnLine(true);
			entityManager.persist(ret);
			return ret;
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}


	@Transactional
	@Async
	public void assignTripToUser(int carId, int tripId) {
		UserTrip trip = entityManager.find(UserTrip.class, tripId);
		trip.setCar_id(entityManager.find(Car.class, carId));
		entityManager.persist(trip);
	}
	
	@Transactional
	@Async
	public void setTripStartingTime(int tripId) {
		UserTrip trip = entityManager.find(UserTrip.class, tripId);
		trip.setStartAt(Date.from(Instant.now()));
		entityManager.persist(trip);
	}
	
	@Transactional
	@Async
	public void setTripEndTime(int tripId) {
		UserTrip trip = entityManager.find(UserTrip.class, tripId);
		trip.setEndAt(Date.from(Instant.now()));
		entityManager.persist(trip);
	}

	@Transactional
	@Async
	public void updateCarLocation(Integer carId, Integer bearing, Double latitude, Double longitude) {
		System.out.println(carId);
		Car car = entityManager.find(Car.class, carId);
		car.setLatitude(latitude);
		car.setLongitude(longitude);
		car.setBearing(bearing);
		entityManager.persist(car);
	}
	
	
	@Transactional
	public int getUserId(String apiToken) {
		Query q=entityManager.createQuery("from User where apiToken=:api");
		q.setParameter("api", apiToken);
		User user1=(User) q.getSingleResult();
		User u = entityManager.find(User.class, user1.getId());
		return u.getId();
	}

	
	@Transactional
	public void UpdateUserTrip(int tripId, Driver driver, Car car, User user) {

		UserTrip u = entityManager.find(UserTrip.class, tripId);
		u.setDriver_id(driver);
		u.setCar_id(car);
		u.setUser_id(user);
		entityManager.persist(u);
		
	}
	
	@Transactional
	public User getUser(int userId){
		Query q = entityManager.createQuery("from User where id=:id");
		q.setParameter("id", userId);
		User user=(User)q.getSingleResult();
		return user;
	}


}
