package com.magdsoft.CarGo.ws.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.transaction.Transactional;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
//import org.springframework.boot.autoconfigure.web.ServerProperties.Session;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonJsonParser;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.magdsoft.CarGo.ws.forms.TimedLocation;
import com.magdsoft.CarGo.ws.model.Car;
import com.magdsoft.CarGo.ws.controller.DistanceWebService;
import com.magdsoft.CarGo.ws.controller.WebSocketController.SocketDatabaseHandler;
import com.magdsoft.CarGo.ws.model.User;


public class WebSocketController extends TextWebSocketHandler {
	
	@Service("socketDatabaseHandler")
//	@Scope(scopeName = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public static class SocketDatabaseHandler {
		
		@Autowired
		EntityManager entityManager;
		
		@Transactional
		@Async
		public void updateCarLocation(Integer carId, Double longitude, Double latitude) {
			System.out.println(carId);
			Car car = entityManager.find(Car.class, carId);
			car.setLatitude(latitude);
			car.setLongitude(longitude);
			entityManager.persist(car);
		}
		
		public TextMessage sendRequest(Map<String,Object> userData){
			entityManager.getTransaction().begin();
			User user=entityManager.find(User.class, userData.get("userId"));
			
			Map<String,Object> request=new HashMap<>();
			request.put("name",user.getName());
			request.put("from",userData.get("startAtAddress"));
			request.put("to",userData.get("EndAtAddress"));
			TextMessage text=new TextMessage(request.toString());
			return text;
		}
		
		
	}
	

	
	@Autowired
	SocketDatabaseHandler socketDatabaseHandler;
	
	@Autowired
	DataSource dataSource;
	
	Map<String, WebSocketSession> webSocketSessionById = new HashMap<>();
	Map<String, Integer> carIdsForSessionIds = new HashMap<>();
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message )
			throws Exception {
		super.handleTextMessage(session, message);
		Map<String, Object> obj = null;
		try {
			System.out.println(message.getPayload());
			

			obj = new JacksonJsonParser().parseMap(message.getPayload());
		} catch(Exception ex) {
			ex.printStackTrace();
			return;
		}
		
		System.out.println(obj);
		
		System.out.println(dataSource);

		webSocketSessionById.put(session.getId(), session);
		
		if (obj.containsKey("carId")) {
			carIdsForSessionIds.put(session.getId(), Integer.parseInt(obj.get("carId").toString()));
		}
		
		try {
			if (obj.containsKey("longitude") && obj.containsKey("latitude")) {
				System.out.println(carIdsForSessionIds.get(session.getId()));
				System.out.println(Double.parseDouble(obj.get("longitude").toString()));
				System.out.println(Double.parseDouble(obj.get("latitude").toString()));
				socketDatabaseHandler.updateCarLocation(carIdsForSessionIds.get(session.getId())
						, Double.parseDouble(obj.get("longitude").toString())
						, Double.parseDouble(obj.get("latitude").toString()));
			}
		  
			TimedLocation timedLocation=new TimedLocation();
			timedLocation.setLongitude(Double.parseDouble(obj.get("longitude").toString()));
			timedLocation.setLatitude(Double.parseDouble(obj.get("latitude").toString()));
//			Map<String,Object> userData=new HashMap();
//			userData.put("userId", obj.get("userId"));
//			userData.put("startAtAddress", obj.get("startAtAddress"));
//			userData.put("endAtAddress", obj.get("endAtAddress"));

		   // carGet(timedLocation,userData);
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// TODO Auto-generated method stub
		super.handleTransportError(session, exception);
		exception.printStackTrace();
	}
	
	
//	public void sendMessage(TextMessage message){
//		Map<String, Object> obj = null;
//		
//		WebSocketSession session=null;
//		try {
//			System.out.println(message.getPayload());
//			
////			obj = new JSONObject(message.getPayload());
//			obj = new JacksonJsonParser().parseMap(message.getPayload());
//		} catch(Exception ex) {
//			ex.printStackTrace();
//			return;
//		}
//		
//		System.out.println(obj);
//		
//		System.out.println(dataSource);
//
//		webSocketSessionById.put(session.getId(), session);
//		
//		if (obj.containsKey("carId")) {
//			carIdsForSessionIds.put(session.getId(), Integer.parseInt(obj.get("carId").toString()));
//		}
//		
//		try {
//			if (obj.containsKey("longitude") && obj.containsKey("latitude")) {
//				System.out.println(carIdsForSessionIds.get(session.getId()));
//				System.out.println(Double.parseDouble(obj.get("longitude").toString()));
//				System.out.println(Double.parseDouble(obj.get("latitude").toString()));
//				socketDatabaseHandler.updateCarLocation(carIdsForSessionIds.get(session.getId())
//						, Double.parseDouble(obj.get("longitude").toString())
//						, Double.parseDouble(obj.get("latitude").toString()));
//			}
//		  
//			TimedLocation timedLocation=new TimedLocation();
//			timedLocation.setLongitude(Double.parseDouble(obj.get("longitude").toString()));
//			timedLocation.setLatitude(Double.parseDouble(obj.get("latitude").toString()));
//			Map<String,Object> userData=new HashMap();
//			userData.put("userId", obj.get("userId"));
//			userData.put("startAtAddress", obj.get("startAtAddress"));
//			userData.put("endAtAddress", obj.get("endAtAddress"));
////			RequestTrip requestTrip=new RequestTrip();
////			requestTrip.setUserId(Integer.parseInt(obj.get("userId").toString()));
////			requestTrip.setStartAtAddress(obj.get("startAtAddress").toString());
////			requestTrip.setStartAtAddress(obj.get("endAtAddress").toString());
////			requestTrip=(RequestTrip) obj.get("requestTrip");
////			socketDatabaseHandler.carGet(message,session,timedLocation,webSocketSessionById,userData);
//		} catch(Exception ex) {
//			ex.printStackTrace();
//		}
//		
//	}
	
	public void carGet(TimedLocation timedLocation,
			Map<String,Object> userData){
		Map<String, Object> obj = null;
		Map<Double, Car> cars=new HashMap<>();
	    cars=DistanceWebService.getNearCars(socketDatabaseHandler.entityManager,timedLocation);
	   
//	    obj = new JacksonJsonParser().parseMap(message.getPayload());
	   // entityManager.getTransaction().begin();
	    
	   // entityManager.find(User.class, )
	    for (Entry<Double, Car> entry : cars.entrySet()){
	    
	    	for(Entry<String, WebSocketSession> sId:webSocketSessionById.entrySet()) {
//	    		if(!session.getId().equals(sId.getKey()))
					try {
						//TextMessage sendingMessage=new TextMessage("I need a car for atrip");
						//session.sendMessage(sendingMessage);
						  sId.getValue().sendMessage(socketDatabaseHandler.sendRequest( userData));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			      //  System.out.println(entry.getKey() + "/" + entry.getValue());
	    	}
	       if(obj.get("status").equals("accept"))
	    	   break;
	      
	    	
	    }
	    
	  
	}

	

}
