package com.magdsoft.CarGo.ws.sockets;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.web.ServerProperties.Session;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magdsoft.CarGo.ws.CarLocationDetailed;
import com.magdsoft.CarGo.ws.LatLngBearing;
import com.magdsoft.CarGo.ws.UserLocationDetailed;
import com.magdsoft.CarGo.ws.forms.StartTrip;
import com.magdsoft.CarGo.ws.model.Car;
import com.magdsoft.CarGo.ws.model.Driver;
import com.magdsoft.CarGo.ws.model.User;
import com.magdsoft.CarGo.ws.model.UserTrip;
import com.magdsoft.CarGo.ws.sockets.GenerateMessagesToUser.Status;
import com.magdsoft.CarGo.ws.sockets.LocationAwareObjectDAO.Distances;
import com.magdsoft.CarGo.ws.sockets.Asyncs;
import com.magdsoft.CarGo.ws.sockets.GenerateMessagesCommon;
import com.magdsoft.CarGo.ws.sockets.SocketDataStructure;
import com.magdsoft.CarGo.ws.sockets.GenerateMessagesCommon.TripStatus;

@Component
public class WebSocketController extends TextWebSocketHandler {

	@Autowired
	Asyncs asyncs;
	
	@Autowired
	SocketDataStructure socketDataStructure;

	@Autowired
	DatabaseHandler databaseHandler;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	Sender sender;
	
	@Autowired
	GenerateMessagesCommon generateMessagesCommon;

	@Autowired
	GenerateMessagesToUser generateMessagesToUser;
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	



	public static double getRanges(int type) {
		switch(type) {
			case 0:
				return 0.0125;
			case 1:
				return 0.0125;
			case 2:
				return 0.5; 
			default:
				return 0;
		}
	}

//	@Override
//	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//		super.afterConnectionClosed(session, status);
//		try {
//			CarLocationDetailed carLocation = socketDataStructure.carsDataStructure.getBySession(session);
//			
//			// Send removal message to near cars
//			if (null != carLocation.getLocation())
//				socketDataStructure.sendCarLocationChangeToNearUsersAsync(carLocation,
//						carLocation.getLocation().getBearing(),
//						carLocation.getLocation().getLatitude(),
//						carLocation.getLocation().getLongitude(),
//						Status.REMOVED);
//			
//			databaseHandler.closeCarForId(carLocation.getCarId());
//			socketDataStructure.carsDataStructure.deleteObject(carLocation.getDriverId());
//		} catch(Exception ex) {
//			ex.printStackTrace();
//		}
//	}
	
	
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		try {
			CarLocationDetailed carLocation = socketDataStructure.carsDataStructure.getBySession(session);
			
			// Send removal message to near cars
			if (null != carLocation && null != carLocation.getLocation())
				socketDataStructure.sendCarLocationChangeToNearUsers(carLocation,
						carLocation.getLocation().getBearing(),
						carLocation.getLocation().getLatitude(),
						carLocation.getLocation().getLongitude(),
						0,
						Status.REMOVED,
						carLocation.getUserId() != null);
			
			socketDataStructure.carsDataStructure.deleteObject(carLocation.getDriverId());
			databaseHandler.closeCarForId(carLocation.getCarId());
			

			UserLocationDetailed user = (null == carLocation.getUserId()) ? null : socketDataStructure.userDataStructure.getById(carLocation.getUserId());
			int tripId = user.getTripId();

			SocketDataStructure.dissociateTrip(carLocation,
					user);

			if (null != user) {
				sender.sendJsonToWebSocketSessionAsync(
						user.getSession(),
						generateMessagesCommon.prepareTripStatusMessage(TripStatus.CANCELLED.toString(),
							"cancelled-by-driver",
							tripId));
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private void onIdMessage(Map<String, Object> obj, WebSocketSession session) {
		int driverId = Integer.parseInt(obj.get("driverId").toString());
		CarLocationDetailed car = new CarLocationDetailed();
		Car carFromDB = databaseHandler.getCarData(driverId);
		car.setCarId(carFromDB.getId());
		car.setCarTypeId(carFromDB.getCarType().getId());
		car.setDriverId(driverId);
		car.setSession(session);
		socketDataStructure.carsDataStructure.addObject(car);
	}

	
	private void onLocationChanged(CarLocationDetailed car, Map<String, Object> obj) {
		double latitude = Double.parseDouble(obj.get("latitude").toString());
		double longitude = Double.parseDouble(obj.get("longitude").toString());
		int bearing = Integer.parseInt(obj.get("bearing").toString());
		
		// Updates the data in the database (still not in the file)
		databaseHandler.updateCarLocation(car.getCarId(), bearing, latitude, longitude);
		
		System.out.println(car.getId());
		System.out.println(latitude);
		System.out.println(longitude);
		// If not associated with a trip, send location change to near cars
				if (null == car.getTripId()) {
		// Send the old car location to near cars in its time, if there is
		if (null != car.getLocation()) {

			socketDataStructure.sendCarLocationChangeToNearUsers(car,
				       	car.getLocation().getBearing(),
				       	car.getLocation().getLatitude(),
				       	car.getLocation().getLongitude(),
				       	0,
						Status.REMOVED,
						false);
		
		}
		
		// Send the new car location to near cars
		socketDataStructure.sendCarLocationChangeToNearUsers(car,
			       	bearing,
			       	latitude,
			       	longitude,
			       	0,
					Status.ADDED,
					false
				);
				}else{
					
					socketDataStructure.sendCarLocationChangeToAssociatedUser(car,
							bearing,
							latitude,
							longitude,
							0,
							Status.ADDED,
							true);

				}
		// Change the location in data structure
		socketDataStructure.carsDataStructure.updateObjectLocationAsync(car.getId(), latitude, longitude, bearing);
	}


	public UserLocationDetailed isTripRequestForThisCar(CarLocationDetailed car, int tripId) {
		for(Map.Entry<Integer, Date> oneRequest : car.currentRequestsWithIssuingTime.entrySet()) {
			int userId = oneRequest.getKey();
			UserLocationDetailed user = socketDataStructure.userDataStructure.getById(userId);
//			return user;

			if ((null != user.getTripId()) && (Integer.valueOf(tripId).equals(user.getTripId()))) {
				return user;
			}
		}
		return null;
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		super.handleTextMessage(session, message);
		Map<String, Object> obj = null;
		try {
			System.out.println(message.getPayload());

			obj = new JacksonJsonParser().parseMap(message.getPayload());

		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		System.out.println(obj);

		if (obj.containsKey("driverId")){
			onIdMessage(obj, session);
			return;
		}

		CarLocationDetailed car = socketDataStructure.carsDataStructure.getBySession(session);

		try {
			if (obj.containsKey("longitude") && obj.containsKey("latitude") && obj.containsKey("bearing"))
				onLocationChanged(car, obj);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		String status = (String) obj.get("status");
		
		if ("accept".equals(status) || "reject".equals(status)) {
			
			
			System.out.println("Trip accepted/rejected");
			int tripIdFromJSON;
			try {
				tripIdFromJSON = ((Number) obj.get("tripId")).intValue();
			} catch(ClassCastException ex) {
				tripIdFromJSON = (Integer.parseInt(obj.get("tripId").toString()));
			}
			System.out.println("TripId(" + tripIdFromJSON + ")");


			UserLocationDetailed user = isTripRequestForThisCar(car, tripIdFromJSON);
			if (null == user) {
				System.out.println("User is null");
				session.sendMessage(new TextMessage("{\"error\":\"trip-not-for-this-user\"}"));
				return;
			}
			
			if ("accept".equals(status)) {
				car.associateWithATripAndUser(user);
				int userId = user.getUserId();
				UserLocationDetailed userObject = socketDataStructure.userDataStructure.getById(userId);
				WebSocketSession userSession = userObject.getSession();
				int driverId = car.getDriverId();
				int tripId = user.getTripId();
				User user1 = databaseHandler.getUser(userId);
				Driver driver=databaseHandler.getDriverData(driverId);
				Car carObject=databaseHandler.getCarData(driverId);
			   // databaseHandler.UpdateUserTrip(tripId, driver, carObject, user1);
				
				System.out.println("UserId =" + userId + " driverId = " + driverId + " tripId =" + tripId);
//				Car carObject=se.find(Car.class,car.getCarId());
//				User us=se.find(User.class,userId);
//				Query q=se.createQuery("from UserTrip where user_id.id=:userId");
//				q.setParameter("userId", userId);
//				UserTrip userTrip=(UserTrip) q.getSingleResult();
//				userTrip.setUser_id(us);
//				userTrip.setCar_id(carObject);
//				Driver driver=se.find(Driver.class, driverId);
//				userTrip.setDriver_id(driver);
//				se.persist(userTrip);
				Map<String, Object> response = generateMessagesToUser.prepareDriverToUserByTripMessage(driverId, tripId);
				//if user null 
				sender.sendJsonToWebSocketSessionAsync(userSession, response);
		       	} else {
			//	car.removeATripRequest(user);
		       		
		       	    Integer driverId = user.getDriverId();
					CarLocationDetailed carloc = driverId == null ? null : socketDataStructure.carsDataStructure.getById(driverId);
					
					
						if (null != driverId) asyncs.cancelTrip(user.getUserId(), driverId);
		
						WebSocketSession otherSession = (null != carloc) ? carloc.getSession() : null;
						
						sender.sendJsonToWebSocketSessionAsync(otherSession, 
								generateMessagesCommon.prepareTripStatusMessage(
									TripStatus.CANCELLED.toString(),
									"cancelled-by-user",
									tripIdFromJSON
									));
			}
			synchronized(car) {
				car.notify();
			}
		}
		
//		
//		
//		if ("accept".equals(status) || "reject".equals(status) ) {
//
//			int tripIdFromJSON;
//			try {
//				tripIdFromJSON = ((Number) obj.get("tripId")).intValue();
//			} catch(ClassCastException ex) {
//				tripIdFromJSON = (Integer.parseInt(obj.get("tripId").toString()));
//			}
//			
//			// Still on request
//			//
//			// Get user from trip requsts
//			UserLocationDetailed user = isTripRequestForThisCar(car, tripIdFromJSON);
//			if (null != user) {
//				// This trip is not on the request list
//				if ("accept".equals(status)) {
//
//					//double time=(double)obj.get("time");
//					car.associateWithATripAndUser(user);
//
//             if(null !=user.getSession() && user.getSession().isOpen() ){
//					sender.sendJsonToWebSocketSessionAsync(
//							user.getSession(),
////							socketDataStructure.userDataStructure.getById(user.getUserId()).getSession(),
//							generateMessagesToUser.prepareDriverToUserByTripMessage(car.getDriverId(), user.getTripId()));
//					//databaseHandler.assignOrderToDriver(user.getTripId(), user.getDriverId());
//					
//					socketDataStructure.sendCarLocationChangeToNearUsers(car,
//						car.getLocation().getBearing(),
//						car.getLocation().getLatitude(),
//						car.getLocation().getLongitude(),
//						0,
//						Status.REMOVED,
//						false);
//             }
//             else 
//             {
//                Integer driverId = user.getDriverId();
//			CarLocationDetailed carloc = driverId == null ? null : socketDataStructure.carsDataStructure.getById(driverId);
//			
//			
//				if (null != driverId) asyncs.cancelTrip(user.getId(), driverId);
//
//				WebSocketSession otherSession = (null != carloc) ? carloc.getSession() : null;
//				
//				sender.sendJsonToWebSocketSessionAsync(otherSession, 
//						generateMessagesCommon.prepareTripStatusMessage(
//							TripStatus.CANCELLED.toString(),
//							"cancelled-by-user",
//							tripIdFromJSON
//							));
//    
//              }
//
//
//	
//				} else {
//					if (null != user) car.removeATripRequest(user);
//				}
//				synchronized(car) {
//					car.notify();
//				}
//			} else {
//				// If on trip
//				// Get user for this trip
//				user = socketDataStructure.userDataStructure.getById(car.getUserId());
//				if ("cancel".equals(status)) {
//					databaseHandler.stopTheTrip(user.getTripId(), TripStatus.CANCELLED);
//					sender.sendJsonToWebSocketSessionAsync(
//							socketDataStructure.userDataStructure.getById(user.getUserId()).getSession(),
//							generateMessagesCommon.prepareTripStatusMessage(TripStatus.CANCELLED.toString(),
//							       	"cancelled-by-driver",
//							       	user.getTripId()));
//					SocketDataStructure.dissociateTrip(car, user);
//				}
//				if ("reach".equals(status)) {
//					databaseHandler.stopTheTrip(user.getTripId(), TripStatus.REACHED);
//					sender.sendJsonToWebSocketSessionAsync(
//							socketDataStructure.userDataStructure.getById(user.getUserId()).getSession(),
//							generateMessagesCommon.prepareTripStatusMessage(TripStatus.REACHED.toString(), "Reached", user.getTripId()));
//					SocketDataStructure.dissociateTrip(car, user);
//				}
//				if ("accept".equals(status)) {
//					// If on trip, no other trips can be accepted.
//					session.sendMessage(new TextMessage("{\"error\":\"trip-not-for-this-user\"}"));
//				}
//			}
//		}
//
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// TODO Auto-generated method stub
		super.handleTransportError(session, exception);
		exception.printStackTrace();
	}
}
