package com.magdsoft.CarGo.ws.sockets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magdsoft.CarGo.ws.CarLocationDetailed;
import com.magdsoft.CarGo.ws.UserLocationDetailed;
import com.magdsoft.CarGo.ws.sockets.GenerateMessagesToUser.Status;
import com.magdsoft.CarGo.ws.sockets.LocationAwareObjectDAO.Distances;
import com.magdsoft.CarGo.ws.sockets.GenerateMessagesCommon.TripStatus;

@Component
public class WebSocketUserController extends TextWebSocketHandler {
	
	@Autowired
	Asyncs asyncs;
			
	
	@Autowired
	SocketDataStructure socketDataStructure;

	@Autowired
	GenerateMessagesToUser generateMessagesToUser;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	Sender sender;
	
	
	@Autowired
	public DatabaseHandler databaseHandler;
	
	@Autowired 
	GenerateMessagesCommon generateMessagesCommon;

	private void onIdMessage(Map<String, Object> obj, WebSocketSession session) {
		
// 		String apiToken =obj.get("apiToken").toString();
// 		int userId=databaseHandler.getUserId(apiToken);
		int userId=Integer.parseInt(obj.get("userId").toString());
		UserLocationDetailed user;
		// If there is an old user object
		if ((user = socketDataStructure.userDataStructure.getById(userId)) != null) {
			// Recycle it
			socketDataStructure.userDataStructure.setSessionForObject(user, session);
		} else {
			// Create new and add it
			user = new UserLocationDetailed();
			user.setUserId(userId);
			user.setTripId(null);
			user.setDriverId(null);
			user.setSession(session);
			socketDataStructure.userDataStructure.addObjectAsync(user);
		}
	}

	private void onChangeLocation(UserLocationDetailed user, double latitude, double longitude) {

		System.out.println(user.getId());
		System.out.println(latitude);
		System.out.println(longitude);
		if(user.getTripId()==null){
		//By ahmed elsayed 20_5_2017
//		if(!isTripForThisUser(user, user.getTripId())){
//			
//		
		socketDataStructure.userDataStructure.updateObjectLocationAsync(user.getId(), latitude, longitude, 0);
		
		List<Distances<CarLocationDetailed>> cars = socketDataStructure.getAllNearCars(latitude, longitude, true);

		List<Map<String, Object>> carsAsAMapMessage = new ArrayList<>();

		for(Distances<CarLocationDetailed> oneCar : cars) {
			Map<String, Object> singleMapMessage = generateMessagesToUser
				.prepareCarToUserMessage(oneCar.getValue(), Status.ADDED,0,oneCar.getValue().getUserId() != null);
			carsAsAMapMessage.add(singleMapMessage);
			
		}

		sender.sendJsonToWebSocketSessionAsync(user.getSession(), carsAsAMapMessage);
	//	}
		}
	}
	
	private boolean isTripForThisUser(UserLocationDetailed user, int tripId) {
		return (Integer.valueOf(tripId).equals(user.getTripId()));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		super.handleTextMessage(session, message);
		Map<String, Object> obj = null;
		try {
			System.out.println(message.getPayload());

			obj = objectMapper.readValue(message.getPayload(), Map.class);

		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		if (null == obj) {
			System.out.println("Message could not be parsed.");
			return;
		}
		System.out.println(obj);
		
		if (obj.containsKey("userId")) onIdMessage(obj, session);

		UserLocationDetailed user = socketDataStructure.userDataStructure.getBySession(session);

		try {
			if (obj.containsKey("longitude") && obj.containsKey("latitude")) {
				double latitude = Double.parseDouble(obj.get("latitude").toString());
				double longitude = Double.parseDouble(obj.get("longitude").toString());

				onChangeLocation(user, latitude, longitude);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		
		
		
		//updated from the same function from wared
		
		String status = obj.containsKey("status") ? obj.get("status").toString() : null;
		if ("cancel".equals(status)) {

			// Get trip id
			
			int tripIdFromJSON;
			try {
				tripIdFromJSON = ((Number) obj.get("tripId")).intValue();
			} catch(ClassCastException ex) {
				tripIdFromJSON = (Integer.parseInt(obj.get("tripId").toString()));
			}

			if (!isTripForThisUser(user, tripIdFromJSON)) {
				session.sendMessage(new TextMessage("{\"error\":\"trip-not-for-this-user\"}"));
				return;
			}

			// Get Car associated with the user (if a car has accepted it before)
			
			Integer driverId = user.getDriverId();
			CarLocationDetailed car = driverId == null ? null : socketDataStructure.carsDataStructure.getById(driverId);
			
			if ("cancel".equals(status)) {
			//	if (null != driverId) asyncs.cancelTrip(user.getId(), driverId);

				WebSocketSession otherSession = (null != car) ? car.getSession() : null;
				
				sender.sendJsonToWebSocketSessionAsync(otherSession, 
						generateMessagesCommon.prepareTripStatusMessage(
							TripStatus.CANCELLED.toString(),
							"cancelled-by-user",
							tripIdFromJSON
							));
				sender.sendJsonToWebSocketSessionAsync(session, 
						generateMessagesCommon.prepareTripStatusMessage(
							TripStatus.CANCELLED.toString(),
							"cancelled-by-user",
							tripIdFromJSON
							));
			}
		}
	}
	
	
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		super.handleTransportError(session, exception);
		exception.printStackTrace();
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		// Delete the user from the "in-memory database"
		socketDataStructure.userDataStructure.deleteObjectAsync(
				socketDataStructure.userDataStructure.getBySession(session).getId());
	}

}
