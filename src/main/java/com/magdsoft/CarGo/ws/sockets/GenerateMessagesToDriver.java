package com.magdsoft.CarGo.ws.sockets;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.stereotype.Service;

import com.magdsoft.CarGo.ws.model.User;

@Service
public class GenerateMessagesToDriver {
	@Autowired
	public EntityManager entityManager;
	
	public Map<String, Object> generateRequestMessage(Map<String, Object> userData) {
//		entityManager.getTransaction().begin();
		User user = entityManager.find(User.class, userData.get("userId"));

		Map<String, Object> request = new HashMap<>();
		request.put("userName", user.getName());
		request.put("userRate", user.getRate());
		request.put("userPhone", user.getPhone());
		request.put("userImage", user.getUserImage());
		request.put("tripId", userData.get("tripId"));
		request.put("startLatitude", userData.get("startLatitude"));
		request.put("startLongitude", userData.get("startLongitude"));

		// TODO: Correct the web service to include the end coordinates in
		// the data sent to the socket
	
		request.put("endLatitude", userData.get("endLatitude"));
		request.put("endLongitude", userData.get("endLongitude"));
		
		request.put("from", userData.get("startAtAddress"));
		request.put("to", userData.get("endAtAddress"));
		return request;
	}
	
	
}
