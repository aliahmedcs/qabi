package com.magdsoft.CarGo.ws.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.magdsoft.CarGo.ws.controller.FileUploader;
import com.magdsoft.CarGo.ws.controller.Register;
import com.magdsoft.CarGo.ws.model.Driver.MilitaryStatus;

import net.minidev.json.JSONArray;
//import JSONException;
import net.minidev.json.JSONObject;

public class Utility {
	
	
	/**
	 * Null check Method
	 * 
	 * @param txt
	 * @return
	 */
	public static boolean isNotNull(String txt) {
		// System.out.println("Inside isNotNull");
		return txt != null && txt.trim().length() >= 0 ? true : false;
	}

//	public static String constructJSON(String done, int userId) {
//		JSONObject obj = new JSONObject();
//		JSONArray jArray = new JSONArray();
//		try {
//			obj.put("status", done);
//			obj.put("userId", userId);
//			// jArray.put(obj);
//		} catch (RuntimeException e) {
//			// TODO Auto-generated catch block
//		}
//		return obj.toString();
//	}

	public static Map<String, Object> constructJSON(String done, String userId) {
		Map<String, Object> ret = new HashMap<>();
		ret.put("status", "done");
		return ret;
	}

//	public static String constructJSON(String done, String userId, String userCode) {
//		JSONObject obj = new JSONObject();
//		// JSONArray jArray=new JSONArray();
//		try {
//			obj.put("status", done);
//			obj.put("userId", userId);
//			obj.put("userCode", userCode);
//
//		} catch (RuntimeException e) {
//			// TODO Auto-generated catch block
//		}
//		return obj.toString();
//	}

	public static Map<String, Object> constructJSON(String done, String userId, String userCode) {
//		JSONObject obj = new JSONObject();
		// JSONArray jArray=new JSONArray();
		Map<String, Object> ret = new HashMap<>();
		try {
			ret.put("status", done);
			ret.put("userId", userId);
			ret.put("userCode", userCode);

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
		}
		return ret;
	}
	public static Map<String, Object> constructJSONStack(Object done, String userId, String userCode) {
//		JSONObject obj = new JSONObject();
		// JSONArray jArray=new JSONArray();
		Map<String, Object> ret = new HashMap<>();
		try {
			ret.put("status", done);
			ret.put("userId", userId);
			ret.put("userCode", userCode);

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
		}
		return ret;
	}
	
	public static Map<String, Object> constructJSONValidate(String done, String userId, int activationCode) {
//		JSONObject obj = new JSONObject();
		// JSONArray jArray=new JSONArray();
		Map<String, Object> ret = new HashMap<>();
		try {
			ret.put("status", done);
			ret.put("apiToken", userId);
			ret.put("activationCode", activationCode);

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
		}
		return ret;
	}

//	public static String constructJSON(String done, int Id, String name, String email, String phone, String password,
//			int activationCode, String friendCode, int points, String userCode, String userImage, boolean isActive,
//			Date createdAt, Date updatedAt, int paymentMethod, int rate) {
//
//		JSONObject obj = new JSONObject();
//		JSONArray jArray = new JSONArray();
//		try {
//			obj.put("status", done);
//			obj.put("userId", String.valueOf(Id));
//			obj.put("name", name);
//			obj.put("email", email);
//			obj.put("phone", phone);
//			obj.put("password", password);
//			obj.put("activationCode", String.valueOf(activationCode));
//			obj.put("friendCode", friendCode);
//			obj.put("points", String.valueOf(points));
//			obj.put("userCode", userCode);
//			obj.put("userImage", userImage);
//			obj.put("isActive", String.valueOf(isActive));
//			obj.put("createdAt", createdAt);
//			obj.put("updatedAt", updatedAt);
//			obj.put("paymentMethod", paymentMethod);
//			obj.put("rate", rate);
//			// jArray.put(obj);
//		} catch (RuntimeException e) {
//			// TODO Auto-generated catch block
//		}
//		return obj.toString();
//	}

	public static Map<String, Object> constructJSON(String done,Integer id, String apiToken, String name, String email, String phone, String password,
			Integer activationCode, String friendCode, Integer points, String userCode, String userImage, Boolean isActive,
			Date createdAt, Date updatedAt, Integer paymentMethod) {

		Map<String, Object> obj = new HashMap<>();
		try {
			obj.put("status", done);
			obj.put("id", String.valueOf(id));
			obj.put("apiToken", apiToken);
			obj.put("name", name);
			obj.put("email", email);
			obj.put("phone", phone);
			//obj.put("password", password);
			obj.put("activationCode", String.valueOf(activationCode));
			obj.put("friendCode", friendCode);
			obj.put("points", String.valueOf(points));
			obj.put("userCode", userCode);
			obj.put("userImage", userImage);
			obj.put("isActive", String.valueOf(isActive));
			obj.put("createdAt", createdAt);
			obj.put("updatedAt", updatedAt);
			obj.put("paymentMethod", paymentMethod);
//*			obj.put("rate", rate);
			// jArray.put(obj);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
		}
		return obj;
	}

	
	

//	public static String constructJSONActivationCode(int activationCode) {
//		JSONObject obj = new JSONObject();
//		// JSONArray jArray=new JSONArray();
//		try {
//			obj.put("status", String.valueOf(activationCode));
//		} catch (RuntimeException e) {
//			// TODO Auto-generated catch block
//		}
//		return obj.toString();
//	}
	public static Map<String, Object> constructJSONActivationCode(int activationCode) {
		//JSONObject obj = new JSONObject();
		// JSONArray jArray=new JSONArray();
		Map<String, Object> obj = new HashMap<>();
		try {
			//obj.put("status", done);
			obj.put("activationCode", String.valueOf(activationCode));
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
		}
		return obj;
	}
	public static Map<String, Object> constructJSONGetHelp(String done,List<Map<String, Object>> glob,List<Map<String, Object>> quest){
		Map<String, Object> obj = new HashMap<>();
		
		try {
			obj.put("status", done);
			obj.put("global", glob);
			obj.put("questions", quest);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
		}
		return obj;
		
	}
	
	public static Map<String, Object> constructJSONGetHistory(String done,List<Map<String, Object>> trips){
		Map<String, Object> obj = new HashMap<>();
		
		try {
			obj.put("status", done);
			obj.put("trips", trips);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
		}
		return obj;
		
	}
	
	public static Map<String, Object> constructJSONGetComplaints(String done,List<Map<String, Object>> complaints){
		Map<String, Object> obj = new HashMap<>();
		
		try {
			obj.put("status", done);
			obj.put("complaints", complaints);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
		}
		return obj;
		
	}

	public static Map<String, Object> constructJSONGetPlaces(String done,List<Map<String, Object>> places){
		Map<String, Object> obj = new HashMap<>();
		
		try {
			obj.put("status", done);
			obj.put("places", places);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
		}
		return obj;
		
	}
	
	public static Map<String, Object> constructJSONGetList(String done,List glob){
		Map<String, Object> obj = new HashMap<>();
		
		try {
			obj.put("status", done);
			obj.put("global", glob);
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
		}
		return obj;
		
	}

	
	public static Map<String, Object> constructJSONGetRequestTrip(String done,String userId){
		Map<String, Object> obj = new HashMap<>();
		
		try {
			obj.put("status", done);
			obj.put("userId", userId);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
		}
		return obj;
		
	}

}
