package com.magdsoft.CarGo.ws.sockets;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.magdsoft.CarGo.ws.CarLocationDetailed;
import com.magdsoft.CarGo.ws.UserLocationDetailed;
import com.magdsoft.CarGo.ws.forms.CancelTrip;
import com.magdsoft.CarGo.ws.forms.EndTrip;
import com.magdsoft.CarGo.ws.forms.StartTrip;
import com.magdsoft.CarGo.ws.model.UserTrip;
import com.magdsoft.CarGo.ws.sockets.GenerateMessagesCommon.TripStatus;

@Service("asyncs")
public class Asyncs {
	@Autowired
	public SocketDataStructure socketDataStructure;

	@Autowired
	public GenerateMessagesCommon generateMessagesCommon;

	@Autowired
	public GenerateMessagesToUser generateMessagesToUser;

	@Autowired
	public DatabaseHandler databaseHandler;

	@Autowired
	public Sender sender;
	
	@Autowired
	public GenerateMessagesToDriver GenerateMessagesToDriver;

	public static int getTimeToWait(int type) {
		switch(type) {
			case 0:
				return 15000;
			case 1:
				return 15000;
			case 2:
				return 60000;
			default:
				return 0;
		}
	}
	
	@Transactional
	@Async
	public void carGet(final Map<String, Object> userData, 
			WebSocketController webSocketController) {
		int tripId = ((Number) userData.get("tripId")).intValue();
		int userId = ((Number) userData.get("userId")).intValue();
		
		// Associate trip with user
		socketDataStructure.userDataStructure.getById(userId).setTripId(tripId);
		
		// Instantiate a new car picker and tell it what to send to user, or car according to
		//   the data from the web service
		new CarPicker() {

			int tripId = ((Number) userData.get("tripId")).intValue();
			
			@Override
			public void sendToUser(int userId, TripStatus tripStatusChange, String reason) {
				sender.sendJsonToWebSocketSessionAsync(
					socketDataStructure.userDataStructure.getById(userId).getSession(),
					generateMessagesCommon.prepareTripStatusMessage(reason, tripStatusChange.toString(), tripId)
				);
			}
			
			@Override
			public void sendToCar(CarLocationDetailed car) {
				//generateMessagesToCar
				sender.sendJsonToWebSocketSessionAsync(
					car.getSession(),
					GenerateMessagesToDriver.generateRequestMessage(userData)
				);
			}
			
			@Override
			public SocketDataStructure getSocketDataStructure() {
				return socketDataStructure;
			}
			
			@Override
			public int getCarType() {
				return ((Number) userData.get("carTypeId")).intValue();
			}
			
			@Override
			public int durationToWait() {
				// Vans are to be waited for acceptance/cancellation in a minute
				// Others have only 15 seconds to respond
				return getCarType() == 2 ? 60000 : 15000;
			}
			
			@Override
			public double carRanges() {
				return getCarType() == 2 ? 0.5 : 0.3;
			}
		}.sendRequestToNearCars(socketDataStructure.userDataStructure.getById(((Number) userData.get("userId")).intValue()));
	}
	
	@Async
	public void startTrip(StartTrip startTrip, WebSocketController webSocketController) {
		try {	
			// ID for car is the driver id
			CarLocationDetailed car = socketDataStructure.carsDataStructure.getById(startTrip.getUserId());
						
			UserLocationDetailed user = socketDataStructure.userDataStructure.getById(car.getUserId());

			
			
			databaseHandler.setTripStartingTime(startTrip.getTripId());

			Map<String, Object> dataToSend = generateMessagesCommon.prepareTripStatusMessage(TripStatus.STARTED.toString(), null, startTrip.getTripId());

			sender.sendJsonToWebSocketSessionAsync(user.getSession(), dataToSend);
			sender.sendJsonToWebSocketSessionAsync(car.getSession(), dataToSend);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void endOrCancelTrip(UserTrip userTrip, Map<String, Object> dataToSend, int tripId,
			WebSocketController webSocketController) {
		try {

			UserLocationDetailed user = socketDataStructure.userDataStructure.getById(userTrip.getUser_id().getId());
			// ID for car is the driver id
			CarLocationDetailed car = socketDataStructure.carsDataStructure.getById(userTrip.getDriver_id().getId());
//			UserLocationDetailed user = socketDataStructure.userDataStructure.getById(databaseHandler.getUserForTrip(tripId));
//			// ID for car is the driver id
//			CarLocationDetailed car = socketDataStructure.carsDataStructure.getById(user.getDriverId());
			
			databaseHandler.setTripEndTime(tripId);

			user.setTripId(null);
			user.setDriverId(null);
			car.setTripId(null);
			car.setUserId(null);

			sender.sendJsonToWebSocketSessionAsync(user.getSession(), dataToSend);
			sender.sendJsonToWebSocketSessionAsync(car.getSession(), dataToSend);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Async
	public void endTrip(UserTrip userTrip, EndTrip endTrip, WebSocketController webSocketController) {
		Map<String, Object> dataToSend = generateMessagesCommon.prepareTripStatusMessage(TripStatus.END.toString(), null, endTrip.getTripId());
	
		endOrCancelTrip(userTrip, dataToSend, endTrip.getTripId(), webSocketController);
	}
	
	
//	
//	
	@Async
	public void cancelTrip(int userId, int driverId) {
		endOrCancelTrip(userId, driverId, TripStatus.CANCELLED);
	}
//
//	
	public void endOrCancelTrip(int userId, int driverId, TripStatus status) {
		try {	
			CarLocationDetailed car = socketDataStructure.carsDataStructure.getById(userId);
			
			UserLocationDetailed user = socketDataStructure.userDataStructure.getById(car.getUserId());

			int tripId = car.getTripId();
			
			user.setTripId(null);
			user.setDriverId(null);
			car.setTripId(null);
			car.setUserId(null);

			Map<String, Object> dataToSend = generateMessagesCommon.prepareTripStatusMessage(status.toString(), null, tripId);

			sender.sendJsonToWebSocketSessionAsync(user.getSession(), dataToSend);
			sender.sendJsonToWebSocketSessionAsync(car.getSession(), dataToSend);

			//databaseHandler.stopTheTrip(tripId, status);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
//commented by ahmed elsayed 13_5	
//	@Async
//	public void cancelTrip(UserTrip userTrip, CancelTrip cancelTrip, WebSocketController webSocketController) {
//		Map<String, Object> dataToSend = generateMessagesCommon.prepareTripStatusMessage(TripStatus.CANSELLED_BY_DRIVER.toString(), null, cancelTrip.getTripId());
//	
//	endOrCancelTrip(userTrip, dataToSend, cancelTrip.getTripId(), webSocketController);
//	}
	
//	@Async
//	public void cancelTrip(int , WebSocketController webSocketController) {
//		Map<String, Object> dataToSend = generateMessagesCommon.prepareTripStatusMessage(TripStatus.END.toString(), null, cancelTrip.getTripId());
//	
//		endOrCancelTrip(dataToSend, cancelTrip.getTripId(), webSocketController);
//	}
}


