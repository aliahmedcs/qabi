package com.magdsoft.CarGo.ws.sockets;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.magdsoft.CarGo.ws.CarLocationDetailed;
import com.magdsoft.CarGo.ws.LatLngBearing;
import com.magdsoft.CarGo.ws.UserLocationDetailed;
import com.magdsoft.CarGo.ws.sockets.LocationAwareObjectDAO.Distances;
import com.magdsoft.CarGo.ws.sockets.GenerateMessagesToUser.Status;
//import com.magdsoft.wared.ws.sockets.GenerateMessagesToUser;
//import com.magdsoft.wared.ws.sockets.SocketDataStructure;
import com.magdsoft.CarGo.ws.sockets.GenerateMessagesToUser;
import com.magdsoft.CarGo.ws.sockets.SocketDataStructure;

@Service
public class SocketDataStructure {
	
	@Autowired
	GenerateMessagesToUser generateMessagesToUser;

	@Autowired
	Sender sender;
	
	/**
	 * A "repository" of the ranges (distances) to search using according to the
	 * car type. Car type of 2 is a van, vans are searched using a wider range.
	 *
	 * @param type Car type
	 * @return The range to use in search
	 */
	public static double getRanges(int type) {
		switch(type) {
			case 0:
				return 0.3;
			case 1:
				return 0.3;
			case 2:
				return 0.5; 
			default:
				return 0.5;
		}
	}

	public final LocationAndSessionAwareObjectDAO<CarLocationDetailed> carsDataStructure = new LocationAndSessionAwareObjectDAO<>();
	public final LocationAndSessionAwareObjectDAO<UserLocationDetailed> userDataStructure = new LocationAndSessionAwareObjectDAO<>();

	/**
	 * Get near cars by type to a point
	 *
	 * @param carType   The type to find
	 * @param latitude
	 * @param longitude
	 * @return A sorted list of the cars with distances
	 */
	public List<Distances<CarLocationDetailed>> getNearCars(int carType, double latitude, double longitude) {
		return carsDataStructure.getNearObjectFromMemory(getRanges(carType), latitude, longitude);
	}

	/**
	 * Get all near cars regardless of its type
	 *
	 * @param latitude
	 * @param longitude
	 * @param orderedByType True if the list is wanted to be ordered by type,
	 *                      False if the list is wanted to be ordered by distance regardless of type.
	 * @return A sorted list of cars with distances
	 */
	public List<Distances<CarLocationDetailed>> getAllNearCars(double latitude, double longitude, boolean orderedByType) {
		List<Distances<CarLocationDetailed>> carX   = carsDataStructure.getNearObjectFromMemory(0, latitude, longitude);
		List<Distances<CarLocationDetailed>> carY   = carsDataStructure.getNearObjectFromMemory(1, latitude, longitude);
		List<Distances<CarLocationDetailed>> carVan = carsDataStructure.getNearObjectFromMemory(2, latitude, longitude);
	
		carX.addAll(carY);
		carX.addAll(carVan);

		if (orderedByType) carX.sort(Distances.<CarLocationDetailed>getComparator());

		return carX;
	}
	
	
	public void sendCarLocationChangeToAssociatedUser(CarLocationDetailed car,
	       	int bearing,
		double latitude,
		double longitude,
		int timeEstimated,
		GenerateMessagesToUser.Status carChangeStatus,
		boolean onTrip) {
	// Not associated with a trip?
	if (null == car.getTripId()) {
		return;
	} else {
		// Send only if this is the new location
		if (!Status.ADDED.equals(carChangeStatus)) return;

		// Send only to the car associated with this trip
		UserLocationDetailed user = userDataStructure.getById(car.getUserId());

		sender.sendJsonToWebSocketSession(user.getSession(), generateMessagesToUser.prepareCarByLocationToUserMessage(
					car,
					new LatLngBearing(bearing,
					latitude,
					longitude),
				       	carChangeStatus,
					timeEstimated,
					onTrip));
	}
}


//	public void sendCarLocationChangeToNearUsers(CarLocationDetailed car,
//			    int bearing,
//	        double latitude,
//			double longitude,
//			GenerateMessagesToUser.Status carChangeStatus) {
//		List<Distances<UserLocationDetailed>> users = userDataStructure.getNearObjectFromMemory(
//				SocketDataStructure.getRanges(car.getCarTypeId()),
//				latitude,
//				longitude
//				);
//		for(Distances<UserLocationDetailed> distancedUser : users) {
//			sender.sendJsonToWebSocketSession(distancedUser.getValue().getSession(), generateMessagesToUser.prepareCarByLocationToUserMessage(
//						car,
//						new LatLngBearing(bearing,
//						latitude,
//						longitude
//						), carChangeStatus));
//		}
//	}
//	
	
	public void sendCarLocationChangeToNearUsers(CarLocationDetailed car,
	       	int bearing,
		double latitude,
		double longitude,
		int timeEstimated,
		GenerateMessagesToUser.Status carChangeStatus,
		boolean onTrip) {
	List<Distances<UserLocationDetailed>> users = null;
	users = userDataStructure.getNearObjectFromMemory(
			SocketDataStructure.getRanges(car.getCarTypeId()),
			latitude,
			longitude);

	for(Distances<UserLocationDetailed> distancedUser : users) {
		sender.sendJsonToWebSocketSession(distancedUser.getValue().getSession(), generateMessagesToUser.prepareCarByLocationToUserMessage(
				car,
				new LatLngBearing(bearing,
				latitude,
				longitude),
				carChangeStatus,
				timeEstimated,
				onTrip));
	}
}
	
//	@Async
//	public void sendCarLocationChangeToNearUsersAsync(CarLocationDetailed car, int bearing,
//			double latitude,
//			double longitude,
//			GenerateMessagesToUser.Status carChangeStatus) {
//		sendCarLocationChangeToNearUsers(car, bearing, latitude, longitude, carChangeStatus);
//	}
	
	public static void dissociateTrip(CarLocationDetailed car, UserLocationDetailed user) {
		if (null != car) {
			car.setUserId(null);
			car.setTripId(null);
		}

		if (null != user) {
			user.setDriverId(null);
			user.setTripId(null);
		}
	}
}	

