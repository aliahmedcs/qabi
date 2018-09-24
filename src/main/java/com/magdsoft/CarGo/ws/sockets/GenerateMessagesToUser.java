package com.magdsoft.CarGo.ws.sockets;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magdsoft.CarGo.ws.CarLocationDetailed;
import com.magdsoft.CarGo.ws.LatLngBearing;
import com.magdsoft.CarGo.ws.model.Car;
import com.magdsoft.CarGo.ws.model.Driver;
//import com.magdsoft.wared.ws.sockets.GenerateMessagesToUser.Status;

@Service
public class GenerateMessagesToUser {

	public enum Status {
		ADDED,
		REMOVED;

		@Override
		public String toString() {
			switch(this) {
			case ADDED:
				return "New location";
			case REMOVED:
				return "Old location";
			default:
				return "NOTHING";
			}
		}
	}

	/**
	 * The object which handles database processes related to the sockets
	 */
	@Autowired
	private DatabaseHandler databaseHandler;

	/**
	 * Prepare car location update to send to user by given id and LatLngBearing
	 *
	 * @param carSentStatus The status whether is it a new location or an old one
	 * @param location      The car location to send
	 * @param carLocation   The car data structure
	 * @return Message to be sent by using the Sender service
	 */
	public Map<String, Object> prepareCarByLocationToUserMessage(CarLocationDetailed carLocation,
		       	LatLngBearing location, Status carSentStatus,int time, boolean onTrip) {

		Map<String, Object> dataToSend = new HashMap<>();

		// If user id is not null, car is on a trip. 
		//boolean onTrip = carLocation.getUserId() != null;
		
		if (!onTrip) {
			dataToSend.put("type", 0);
			dataToSend.put("status", carSentStatus.toString());
			dataToSend.put("carId", carLocation.getCarId());
			dataToSend.put("driverId", carLocation.getDriverId());
			dataToSend.put("carTypeId", carLocation.getCarTypeId());
			dataToSend.put("bearing", location.getBearing());
			dataToSend.put("longitude", location.getLongitude());
			dataToSend.put("latitude", location.getLatitude());
			return dataToSend;
		} else {
			// Delegate to the other method
			return prepareLocationChangeMessage(
					   carLocation.getCarId(),
					   carLocation.getDriverId(),
					   carLocation.getUserId(),
				       carLocation.getLocation().getBearing(),
				       carLocation.getLocation().getLatitude(),
				       carLocation.getLocation().getLongitude(),
				       0);
		}
	}

	/**
	 * Prepare car location update to send to user
	 *
	 * @param carLocation   The car location to send
	 * @param carSentStatus The status whether is it a new location or an old one
	 * @return Message to be sent by using the Sender service
	 */
	public Map<String, Object> prepareCarToUserMessage(CarLocationDetailed carLocation,
	       	Status carSentStatus, int time, boolean onTrip) {
	return prepareCarByLocationToUserMessage(carLocation,
		       	carLocation.getLocation(),
		       	carSentStatus, time, onTrip);
}

	/**
	 * Prepare message to be sent at driver acceptance of a trip
	 *
	 * @param driverId Driver ID
	 * @param tripId   Trip ID
	 * @return Message to be sent by using the Sender service
	 */
	public Map<String, Object> prepareDriverToUserByTripMessage(int driverId, int tripId) {
		Car car = databaseHandler.getCarData(driverId);
		Driver driver = databaseHandler.getDriverData(driverId);
		Map<String, Object> dataToSend = new HashMap<>();
		dataToSend.put("type", 1);
		dataToSend.put("tripId", tripId);
		dataToSend.put("status", "accepted");
		dataToSend.put("carId", car.getId());
		dataToSend.put("carNumber", car.getCarNumber());
		dataToSend.put("carColor", car.getColor());
		dataToSend.put("carManufacturer", car.getManufacturer());
		dataToSend.put("carModel", car.getModel());

		dataToSend.put("driverId", driver.getId());
		dataToSend.put("driverAge", driver.getAge());
		dataToSend.put("driverName", driver.getName());
		dataToSend.put("driverPhone", driver.getPhone());
		dataToSend.put("driverRating", driver.getRating());
		dataToSend.put("driverProfileImage", driver.getProfileImage());

		return dataToSend;
	}

	/**
	 * Prepare location change message to send to the user associated with the given trip id
	 * This happens during a trip only
	 *
	 * @param userId	The user id
	 * @param bearing	The bearing
	 * @param longitude	The location's longitude
	 * @param latitude	The location's latitude
	 * @param time		The time estimated to reach the user (in Wared only)
	 * @return Message to be sent by using the Sender service
	 */

	public Map<String, Object> prepareLocationChangeMessage(int carId,
			int driverId,
			int userId,
			double bearing,
			double latitude,
			double longitude,
			int time) {

		// Make the message as a map for Spring to generate as JSON
		Map<String, Object> theMap = new HashMap<>();
		theMap.put("bearing", bearing);
		theMap.put("longitude", longitude);
		theMap.put("latitude", latitude);
		theMap.put("time", time);
		theMap.put("type", 10);
		theMap.put("carId",carId);
		theMap.put("driverId",driverId);
		// Return the prepared message
		return theMap;

	}

}
