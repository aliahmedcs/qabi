package com.magdsoft.CarGo.ws.controller;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.validation.Valid;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
//import com.magdsoft.CarGo.ws.controller.WebSocketController.Asyncs;
import com.magdsoft.CarGo.ws.forms.AddNewSearch;
import com.magdsoft.CarGo.ws.forms.AddOrUpdateRate;
import com.magdsoft.CarGo.ws.forms.AddPlaceForUser;
import com.magdsoft.CarGo.ws.forms.CancelTrip;
import com.magdsoft.CarGo.ws.forms.EndTrip;
import com.magdsoft.CarGo.ws.forms.NewHelp;
import com.magdsoft.CarGo.ws.forms.NewSuggestionForm;
import com.magdsoft.CarGo.ws.forms.PagedUserForm;
import com.magdsoft.CarGo.ws.forms.ReportComplaint;
import com.magdsoft.CarGo.ws.forms.ReportLost;
import com.magdsoft.CarGo.ws.forms.RequestTrip;
import com.magdsoft.CarGo.ws.forms.StartTrip;
import com.magdsoft.CarGo.ws.forms.TimedLocation;
import com.magdsoft.CarGo.ws.forms.UserWithImages;
import com.magdsoft.CarGo.ws.helpers.password.DefaultPasswordHasher;
import com.magdsoft.CarGo.ws.model.Car;
import com.magdsoft.CarGo.ws.model.CarType;
import com.magdsoft.CarGo.ws.model.Driver;
import com.magdsoft.CarGo.ws.model.FreeRide;
//import com.magdsoft.CarGo.ws.model.DriverPaymentMethod;
//import com.magdsoft.CarGo.ws.model.DriverRate;
import com.magdsoft.CarGo.ws.model.Help;
import com.magdsoft.CarGo.ws.model.LostItem;
import com.magdsoft.CarGo.ws.model.Promotion;
import com.magdsoft.CarGo.ws.model.Rates;
import com.magdsoft.CarGo.ws.model.Reason;
import com.magdsoft.CarGo.ws.model.Suggestion;
import com.magdsoft.CarGo.ws.model.TripCanceled;
import com.magdsoft.CarGo.ws.model.User;
import com.magdsoft.CarGo.ws.model.UserComplaint;
import com.magdsoft.CarGo.ws.model.UserComplaintImage;
import com.magdsoft.CarGo.ws.model.UserComplaintType;
//import com.magdsoft.CarGo.ws.model.UserPaymentMethod;
import com.magdsoft.CarGo.ws.model.UserPlace;
//import com.magdsoft.CarGo.ws.model.UserRate;
import com.magdsoft.CarGo.ws.model.UserSearch;
import com.magdsoft.CarGo.ws.model.UserTrip;
import com.magdsoft.CarGo.ws.model.UserTrip.Status;
import com.magdsoft.CarGo.ws.model.Utility;
import com.magdsoft.CarGo.ws.sockets.Asyncs;
//import com.magdsoft.CarGo.ws.controller.DistanceWebService;
import com.magdsoft.CarGo.ws.sockets.WebSocketController;
@Controller
@RequestMapping("/api")
public class Register {

	@Autowired
	FileUploader fileUploader;
 	///////////////////////////
 	// SOCKET INTEGRATION
 	///////////////////////////
 	@Autowired
 	Asyncs asyncs;
 	///////////////////////////
 	// END SOCKET INTEGRATION
 	///////////////////////////
 	
 	public static final int PAGINATION = 10;

	public static final int PAGINATION_GET_HELP = PAGINATION;
	public static final int PAGINATION_GET_HISTORY = PAGINATION;

	public static final String PATH = "/home/cargom/public_html/public/uploads";
	// public static final String PATH = "/Users/mac/Uploads";


	@Autowired
	private EntityManagerFactory entityManagerFactory;
	@Autowired
	WebSocketController webSocketController;

	@RequestMapping("/login")
	public @ResponseBody Map<String, Object> checkLogin(User user, @RequestBody(required = false) User userBody) {
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			if (userBody != null) {
				user = userBody;
			}

			boolean isUserAvailable = false;
			se.getTransaction().begin();
			Query q = se.createQuery("from User where phone=?");
			q.setParameter(1, user.getPhone());
			// q.setParameter(1, password);
			String user_id = null;
			List<User> use = q.getResultList();
			for (User u : use) {
				if (u != null) {
					if (DefaultPasswordHasher.getInstance().isPasswordValid(user.getPassword(), u.getPassword())
							&& u.isActive()) {
						// code to return error message
						// DATE_FORMAT = new
						// SimpleDateFormat("dd-MM-yy:HH:mm:SS");
						// Date createdDate =
						// DATE_FORMAT.format(u.getCreatedAt());
						// DATE_FORMAT = new
						// SimpleDateFormat("dd-MM-yy:HH:mm:SS");
						// Date updatedDate =
						// DATE_FORMAT.format(u.getUpdatedAt());
						isUserAvailable = true;
						return Utility.constructJSON("done",u.getId(), u.getApiToken(), u.getName(), u.getEmail(), u.getPhone(),
								user.getPassword(), u.getActivationCode(), u.getFriendCode(), u.getPoints(),
								u.getUserCode(), u.getUserImage(), u.isActive(), u.getCreatedAt(), u.getUpdatedAt(),
							u.getPaymentMethod());
					} else if (u.isActive() != true && DefaultPasswordHasher.getInstance()
							.isPasswordValid(user.getPassword(), u.getPassword())) {
						return Utility.constructJSONValidate("You not activated yet", "" + u.getApiToken(),
								u.getActivationCode());
					}

				}

			}
			return Utility.constructJSON("your phone or password are not exist", null, null);
		} catch (Exception e) {
			se.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}
		return null;
	}

	@RequestMapping("/doRegister")

	// @Produces(MediaType.APPLICATION_JSON)
	// @Consumes({MediaType.APPLICATION_JSON,
	// MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA})
	public @ResponseBody Map<String, Object> insertUser(User user, @RequestBody(required = false) User userBody) { // @Param
		EntityManager se = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		try { // name,
			if (userBody != null) {
				user = userBody;
			}
			String apiToken=null;
			System.out.println(user.getEmail());
			// @QueryParam("email") String email, @QueryParam("phone") String
			// phone,
			// @QueryParam("password") String password,
			// @QueryParam("friendCode")
			// String friendCode) {

			// if (user == null) { return "{\"error\" : \"User is NULL\"}"; }

			boolean insertStatus = false;
			// SessionFactory sf = ((SessionFactory)
			// context.getAttribute("sf"));

			se.getTransaction().begin();
			boolean phoneExist = false;
			Query qp = se.createQuery("from User where  phone=?");
			qp.setParameter(1, user.getPhone());
			List<User> usep = qp.getResultList();
			for (User u : usep) {
				phoneExist = true;
			}

			boolean emailExist = false;
			Query qe = se.createQuery("from User where  email=?");
			qe.setParameter(1, user.getEmail());
			List<User> usee = qe.getResultList();
			for (User u : usee) {
				emailExist = true;
			}

			if (phoneExist == false && emailExist == false) {
				SecureRandom random = new SecureRandom();
				apiToken = new BigInteger(500, random).toString(32);
				User nuser = new User();
				nuser.setApiToken(apiToken);
				nuser.setName(user.getName());
				nuser.setPhone(user.getPhone());
				nuser.setPassword(DefaultPasswordHasher.getInstance().hashPassword(user.getPassword()));
				nuser.setEmail(user.getEmail());
				nuser.setFriendCode(user.getFriendCode());

				// generated
				String userCode1 = null;

				// generated
				SecureRandom rand = new SecureRandom();
				int num = rand.nextInt(100000);
				String formatted = String.format("%05d", num);
				int activationCode1 = Integer.valueOf(formatted);
				nuser.setActivationCode(activationCode1);
				se.persist(nuser);
				if (0 != verifyPhoneNumber(user.getPhone(), String.valueOf(activationCode1))) {
					se.getTransaction().rollback();
					map.put("status", "not registered try again");
					return map;
				}

				se.getTransaction().commit();
				String user_id = null;
				se.getTransaction().begin();
				Query q = se.createQuery("from User where  phone=?");
				q.setParameter(1, user.getPhone());
				List<User> use = q.getResultList();
				for (User u : use) {
					user_id = u.getApiToken();
					String sub = user.getName().substring(0, 3);
					num = rand.nextInt(100000);
					formatted = String.format("%04d", num);
					userCode1 = sub + formatted;
				}
				q = se.createQuery("update User set userCode=? where  phone=?");
				q.setParameter(1, userCode1);
				q.setParameter(2, user.getPhone());
				int modifications = q.executeUpdate();
				se.getTransaction().commit();
				if (modifications != 0) {
					insertStatus = true;
				}

				if (insertStatus) {
					// System.out.println("true" + user_id +userCode1 );
					return Utility.constructJSON("done", user_id, userCode1);

				} else {
					map.put("status", "not registered try again");
					return map;
				}
			} else if (phoneExist == true && emailExist == true) {
				map.put("status", "your phone and email is exist");
				return map;
			} else if (emailExist == true && phoneExist == false) {
				map.put("status", "your Email is exist");
				return map;
			} else if (emailExist == false && phoneExist == true) {
				map.put("status", "your phone is exist");
				return map;
			}
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e;
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}

		return null;
	}

	@RequestMapping("/doValidate")
	public @ResponseBody Map<String, Object> validateCode(User user, @RequestBody(required = false) User userBody) {
		// SessionFactory sf = ((SessionFactory) context.getAttribute("sf"));
		EntityManager se = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		if (userBody != null) {
			user = userBody;
		}
		
		System.out.println("Received: " + user + " & activationCode is " + user.getActivationCode());
		try {
			se.getTransaction().begin();
			boolean validate = false;
			User theUser = null;
			
			/* If the result is empty, check if the client sent an api_token */
			List<User> results = new ArrayList<>();
			if (null == user.getPhone() || user.getPhone().isEmpty()) {
				System.out.println("Using api_token");
				Query q2 = se.createQuery("from User where api_token=?");
				q2.setParameter(1, user.getApiToken());
				results = q2.getResultList();
			} else {
				System.out.print("Using phone");
				Query q = se.createQuery("from User where phone=?");
				q.setParameter(1, user.getPhone());
				results = q.getResultList();
			}
			
			System.out.println("Found " + results.size() + " User matches in the database");
			if (!results.isEmpty()) {
				User u = results.get(0);
				System.out.println("Real activationCode is " + u.getActivationCode());
				if (user.getActivationCode().equals(u.getActivationCode())) {
					Query q1 = se.createQuery("update User set isActive=true,activationCode=null where phone=?");
					q1.setParameter(1, u.getPhone());
					int modification = q1.executeUpdate();
					se.getTransaction().commit();
					if (modification != 0) {
						validate = true;
						theUser = u;
					}
				}
			}
			if (validate) {
				User u = theUser;
				return Utility.constructJSON("done",u.getId() ,u.getApiToken(), u.getName(), u.getEmail(), u.getPhone(),
						u.getPassword(), u.getActivationCode(), u.getFriendCode(), u.getPoints(), u.getUserCode(),
						u.getUserImage(), u.isActive(), u.getCreatedAt(), u.getUpdatedAt(), u.getPaymentMethod()
						);
			} else {
				map.put("status", "error");
				return map;
			}
		} catch (Exception e) {
			se.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}

	}

	@RequestMapping("/doForgetPassword")
	public @ResponseBody Map<String, Object> forgetPassword(User user, @RequestBody(required = false) User userBody) {
		EntityManager se = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		try {
			if (userBody != null) {
				user = userBody;
			}
			String phone = user.getPhone();
			boolean isUserAvailable = false;
			// SessionFactory sf = ((SessionFactory)
			// context.getAttribute("sf"));

			se.getTransaction().begin();
			Query q = se.createQuery("from User where  phone=?");
			q.setParameter(1, phone);

			String user_id =null;

			List<User> use = q.getResultList();

			// insertStatus=checkLogin(context,email,password);
			for (User u : use) {
				user_id = u.getApiToken();
				isUserAvailable = true;
			}
			if (isUserAvailable) {
				// System.out.println("true" + user_id);

				return Utility.constructJSON("done", user_id);

			} else {
				// System.out.println("false" + user_id);
				map.put("status", "error");
				return map;
				// return Utility.constructJSON("error", null, null);
			}
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e;// return
					// Utility.constructJSON(e.getStackTrace().toString(), null,
					// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}
	}

	@RequestMapping("/setNewPassword")
	public @ResponseBody Map<String, Object> newPassword(User user, @RequestBody(required = false) User userBody) {
		EntityManager se = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		try {
			if (userBody != null) {
				user = userBody;
			}
			boolean isUserAvailable = false;
			// SessionFactory sf = ((SessionFactory)
			// context.getAttribute("sf"));

			se.getTransaction().begin();

			Query q = se.createQuery("update User set password=? where apiToken=?");
			q.setParameter(1, DefaultPasswordHasher.getInstance().hashPassword(user.getPassword()));
			q.setParameter(2, user.getApiToken());
			int modifications = q.executeUpdate();
			se.getTransaction().commit();
			int user_id = 0;
			if (modifications > 0) {
				// System.out.println("true" + user_id);
				map.put("status", "done");
				return map;
				// return Utility.constructJSON("done", null, null);

			} else {
				// System.out.println("false" + user_id);
				map.put("status", "error");
				return map;
				// return Utility.constructJSON("error", null, null);
			}
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e;// return
					// Utility.constructJSON(e.getStackTrace().toString(), null,
					// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}
	}

	//
	@RequestMapping("/loginSocial")
	public @ResponseBody Map<String, Object> loginSocial(User user, @RequestBody(required = false) User userBody) {
		EntityManager se = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		try {

			boolean isUserAvailable = false;
			if (userBody != null) {
				user = userBody;
			}

			se.getTransaction().begin();
			Query q = se.createQuery("from User u where u.email=?");
			q.setParameter(1, user.getEmail());
			// q.setParameter(1, password);
			String user_id = null;
			List<User> use = q.getResultList();
			for (User u : use) {
				// if (u != null) {
				if (!u.isActive()) {
					map.put("status", "your email isn't active");
					map.put("apiToken", u.getApiToken());
					return map;
					// return Utility.constructJSON("your email isn't active",
					// u.getId(), null, null, u.getPhone(), null,
					// null, null, null, null, null, null, null, null, null,
					// null);
				}
				if (u.getEmail() != null) {
					// code to return error message
					// DATE_FORMAT = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
					// Date createdDate = DATE_FORMAT.format(u.getCreatedAt());
					// DATE_FORMAT = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
					// Date updatedDate = DATE_FORMAT.format(u.getUpdatedAt());
					// isUserAvailable = true;
					return Utility.constructJSON("done",u.getId() ,u.getApiToken(), u.getName(), u.getEmail(), u.getPhone(),
							u.getPassword(), u.getActivationCode(), u.getFriendCode(), u.getPoints(), u.getUserCode(),
							u.getUserImage(), u.isActive(), u.getCreatedAt(), u.getUpdatedAt(), u.getPaymentMethod());

				}

				// }

			}
			map.put("status", "your email isn't exist");
			return map;
			// return Utility.constructJSON("your email isn't exist", null,
			// null);
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e;// return
					// Utility.constructJSON(e.getStackTrace().toString(), null,
					// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}
	}

	@RequestMapping("/addNewSuggestion")
	public @ResponseBody Map<String, Object> addNewSuggestion(NewSuggestionForm newSuggestionForm,
			@RequestBody(required = false) NewSuggestionForm newSuggestionFormBody) {
		Map<String, Object> map = new HashMap<>();
		boolean isUserAvailable = false;
		EntityManager se = entityManagerFactory.createEntityManager();
		if (newSuggestionFormBody != null) {
			newSuggestionForm = newSuggestionFormBody;
		}
		try {
			// SessionFactory sf = ((SessionFactory)
			// context.getAttribute("sf"));
            
			se.getTransaction().begin();
			Query q=se.createQuery("select id from User where apiToken=:api");
			q.setParameter("api", newSuggestionFormBody.getApiToken());
			
			int use=q.getFirstResult();
			Suggestion suggestion = new Suggestion();
			User user = se.find(User.class, use);
			suggestion.setSuggest(newSuggestionForm.getSuggest());
			suggestion.setUser_id(user);
			se.persist(suggestion);
			se.getTransaction().commit();
			map.put("status", "done");
			return map;
			// return Utility.constructJSON("done", null, null);
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e;// return
					// Utility.constructJSON(e.getStackTrace().toString(), null,
					// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}

	}

	@RequestMapping("/addNewHelp")
	public @ResponseBody Map<String, Object> addNewHelp(NewHelp newHelp,
			@RequestBody(required = false) NewHelp newHelpBody) {
		Map<String, Object> map = new HashMap<>();
		if (newHelpBody != null) {
			newHelp = newHelpBody;
		}
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			System.out.println("ssssssssssssssssss" + newHelp.getApiToken());

			se.getTransaction().begin();
			Query q=se.createQuery("select id from User where apiToken=:api");
			q.setParameter("api", newHelp.getApiToken());
			int use=(int) q.getSingleResult();
			User user = se.find(User.class, use);
			Help help = new Help();
			help.setUser_id(user);
			help.setQuestion(newHelp.getQuestion());
			help.setQuestionTitle(newHelp.getQuestionTitle());
			help.setType("user");
			se.persist(help);
			//Integer questionId= help.getId();
			se.getTransaction().commit();
			map.put("status", "done");
			map.put("questionId", help.getId());
			return map;
			// return Utility.constructJSON("done", null, null);
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e;// return
					// Utility.constructJSON(e.getStackTrace().toString(), null,
					// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}
	}

	@RequestMapping("/getHelp")
	public @ResponseBody Map<String, Object> getHelp(PagedUserForm getHelpForms,
			@RequestBody(required = false) PagedUserForm getHelpFormsBody) {
		Map<String, Object> map = new HashMap<>();
		if (getHelpFormsBody != null) {
			getHelpForms = getHelpFormsBody;
		}
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			Boolean isFound = false;
			// Map<String, Object> globalMap = new HashMap();
			// Map<String, Object> questMap = new HashMap();

			se.getTransaction().begin();
			// User user = se.find(User.class, user.getId());
			Query q = se.createQuery("from Help help where help.user_id.apiToken=? and help.isGlobal=true and type='user' ");
			q.setParameter(1, getHelpForms.getApiToken());
			q.setFirstResult((getHelpForms.getPage() - 1) * PAGINATION_GET_HELP);
			q.setMaxResults(PAGINATION_GET_HELP);
			List<Help> globalQuestion = q.getResultList();

			List<Map<String, Object>> globalQus = new ArrayList<>();
			List<Map<String, Object>> questions = new ArrayList<>();
			for (Help u : globalQuestion) {
				isFound = true;
				Map<String, Object> oneMap = new HashMap<>();
				oneMap.put("id", u.getId());
				oneMap.put("question_title", u.getQuestionTitle());
				oneMap.put("answer", u.getAnswer());
				globalQus.add(oneMap);
			}
			Query q2 = se
					.createQuery("from Help help where help.user_id.apiToken=? and help.isGlobal=false and type='user' ");
			q2.setParameter(1, getHelpForms.getApiToken());
			q2.setFirstResult((getHelpForms.getPage() - 1) * PAGINATION_GET_HELP);
			q2.setMaxResults(PAGINATION_GET_HELP);
			List<Help> notGlobalQuestion = q2.getResultList();
			for (Help u1 : notGlobalQuestion) {
				isFound = true;
				Map<String, Object> questMap = new HashMap<>();
				questMap.put("id", u1.getId());
				questMap.put("question title", u1.getQuestionTitle());
				questMap.put("answer", u1.getAnswer());
				questions.add(questMap);
			}
			try {
				se.getTransaction().commit();
			} catch (RollbackException ex) {
				se.getTransaction().rollback();
			}
			if (isFound) {
				map.put("status", "done");
				map.put("global", globalQus);
				map.put("questions", questions);
				return map;
				// return Utility.constructJSONGetHelp("done", globalQus,
				// questions);
			} else {
				map.put("status", "there is no help");
				return map;
				// return Utility.constructJSONGetHelp("there is no help", null,
				// null);
			}

		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e; // return
						// Utility.constructJSONGetHelp(e.getStackTrace().toString(),
						// null, null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}

	}

	@RequestMapping("/addNewSearch")
	public @ResponseBody Map<String, Object> addNewSearch(AddNewSearch addNewSearch,
			@RequestBody(required = false) AddNewSearch addNewSearchBody) {
		Map<String, Object> map = new HashMap<>();
		if (addNewSearchBody != null) {
			addNewSearch = addNewSearchBody;
		}
		if (addNewSearch.getLatitude() == null || addNewSearch.getLongitude() == null) {
			return Utility.constructJSON("Error you aren't insert longitude and latitude", null, null);
		}

		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			Boolean insert = false;

			se.getTransaction().begin();
			Query q = se.createQuery(
					"from UserSearch where user_id.apiToken=:id and longitude=:long and latitude=:lat and search=:se");
			q.setParameter("id", addNewSearch.getApiToken());
			q.setParameter("long", addNewSearch.getLongitude());
			q.setParameter("lat", addNewSearch.getLatitude());
			q.setParameter("se", addNewSearch.getSearchName());
			List<UserSearch> list = q.getResultList();
			if (list.size() >= 1) {
				map.put("status", "done");
				return map;
				// return Utility.constructJSON("done", null, null);
			}
			q=se.createQuery("from User where apiToken=:api");
			q.setParameter("api", addNewSearch.getApiToken());
			
			User user=(User) q.getSingleResult();
			UserSearch search = new UserSearch();
			//User user = se.find(User.class, addNewSearch.getApiToken());
			search.setUser_id(user);
			search.setSearsh(addNewSearch.getSearchName());
			search.setLatitude(addNewSearch.getLatitude());
			search.setLongitude(addNewSearch.getLongitude());
			se.persist(search);
			se.getTransaction().commit();
			insert = true;
			if (insert) {
				map.put("status", "done");
				return map;
				// return Utility.constructJSON("done", null, null);
			} else {
				map.put("status", "Erro occurred try please again");
				return map;
			} // return Utility.constructJSON("Erro occurred try please again",
				// null, null);
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e;// return
					// Utility.constructJSON(e.getStackTrace().toString(), null,
					// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}

	}

	/*
	 * @RequestMapping("/getHistoryForUser") public @ResponseBody Map<String,
	 * Object> getHistoryUser(User user, @RequestBody(required = false) User
	 * userBody) { try { if (userBody != null) { user = userBody; } Map<String,
	 * Object> map = new HashMap<String, Object>(); Date date; int rateValue,
	 * driverId, carId; Driver driver = new Driver(); Car car = new Car();
	 * boolean isGet = false;
	 * 
	 * String time; // UserPaymentMethod userPaymentMethod; EntityManager se =
	 * entityManagerFactory.createEntityManager(); se.getTransaction().begin();
	 * User newUser = se.find(User.class, user.getId()); Query q =
	 * se.createQuery("from UserTrip as uT where uT.user_id.id=?");
	 * q.setParameter(1, user.getId()); List<UserTrip> userTrip =
	 * q.getResultList(); for (UserTrip t : userTrip) { isGet = true;
	 * map.put("id", t.getId()); map.put("startAdd", t.getStartAtAddress());
	 * map.put("endAdd", t.getEndAtAddress()); map.put("rate", t.getRate());
	 * 
	 * map.put("userPaymentMethodName",
	 * t.getPayment_Method().getPayment_type().getName()); map.put("realCost",
	 * t.getRealCost()); map.put("date", t.getStartAt()); date = t.getStartAt();
	 * SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm:SS"); time =
	 * timeFormate.format(date); map.put("driverId", t.getDriver_id().getId());
	 * map.put("driverName", t.getDriver_id().getName()); map.put("driverImage",
	 * t.getDriver_id().getProfileImage()); map.put("driverPhone",
	 * t.getDriver_id().getPhone()); map.put("carId", t.getCar_id().getId());
	 * map.put("carName", t.getCar_id().getName()); map.put("carManufacturer",
	 * t.getCar_id().getManufacturer()); map.put("carModel",
	 * t.getCar_id().getModel()); map.put("carColor", t.getCar_id().getColor());
	 * } if (isGet) return Utility.constructJSONGetHelp("done", map, null); else
	 * return Utility.constructJSONGetHelp("there is no trip", null, null); }
	 * catch (Exception e) { // return Utility.constructJSONGetHelp("error
	 * occcurred please try // again",null, null); e.printStackTrace(); } return
	 * null; }
	 */

	@RequestMapping("/getHistoryForUser")
	public @ResponseBody Map<String, Object> getHistoryUser(PagedUserForm pagedUserForm,
			@RequestBody(required = false) PagedUserForm pagedUserFormBody) {
		Map<String,Object>obj=new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			if (pagedUserFormBody != null) {
				pagedUserForm = pagedUserFormBody;
			}

			Date date;
			int rateValue, driverId, carId;
			Driver driver = new Driver();
			Car car = new Car();
			boolean isGet = false;

			String time;
			// UserPaymentMethod userPaymentMethod;

			se.getTransaction().begin();
			Query q=se.createQuery("from User where apiToken=:api");
			q.setParameter("api", pagedUserFormBody.getApiToken());
			User newUser=(User) q.getSingleResult();
			
			//User newUser = se.find(User.class, pagedUserFormBody.getApiToken());
		    q = se.createQuery("from UserTrip as uT where uT.user_id.apiToken=? order by uT.createdAt desc");
			q.setParameter(1, pagedUserFormBody.getApiToken());
			q.setFirstResult((pagedUserFormBody.getPage() - 1) * PAGINATION_GET_HISTORY);
			q.setMaxResults(PAGINATION_GET_HISTORY);
			List<UserTrip> userTrip = q.getResultList();
			List<Map<String, Object>> ret = new ArrayList<>();
			for (UserTrip t : userTrip) {
				isGet = true;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", t.getId());
				map.put("startAdd", t.getStartAtAddress());
				map.put("endAdd", t.getEndAtAddress());
				map.put("rate", t.getRate());

//				String name = "";

//				try {
//					name = t.getPayment_Method().getPayment_type().getName();
//				} catch (NullPointerException ex) {

//				}

//				map.put("userPaymentMethodName", name);
				map.put("realCost", t.getRealCost());
				map.put("date", t.getStartAt());
				date = t.getStartAt();
			//	SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm:SS");
			//	time = timeFormate.format(date);

				if (t.getDriver_id() != null) {

					map.put("driverId", t.getDriver_id().getId());
					map.put("driverName", t.getDriver_id().getName());
					map.put("driverImage", t.getDriver_id().getProfileImage());
					map.put("driverPhone", t.getDriver_id().getPhone());
				} else {
					map.put("driverId", "-1");
					map.put("driverName", "NO ONE");
					map.put("driverImage", "");
					map.put("driverPhone", "");
				}
				if (t.getCar_id() != null) {
					map.put("carId", t.getCar_id().getId());
					map.put("carName", t.getCar_id().getName());
					map.put("carManufacturer", t.getCar_id().getManufacturer());
					map.put("carModel", t.getCar_id().getModel());
					map.put("carColor", t.getCar_id().getColor());
				} else {
					map.put("carId", -1);
					map.put("carName", "NO ONE");
					map.put("carManufacturer", "");
					map.put("carModel", "");
					map.put("carColor", "");
				}
				ret.add(map);
			}
			if (isGet){
				//return Utility.constructJSONGetHistory("done", ret);
				obj.put("status", "done");
				obj.put("trips", ret);
				return obj;
			}else{
				//return Utility.constructJSONGetHistory("there is no trip", null);
				obj.put("status", "there is no trip");
				return obj;
			}
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e; //return Utility.constructJSONGetHelp(e.getStackTrace().toString(), null, null);

		} finally {
			if (se.getTransaction().isActive()) { se.getTransaction().commit(); }
			se.close();
		}

	}


	@RequestMapping("/reportLost")
	public @ResponseBody Map<String, Object> reportLost(ReportLost lost,
			@RequestBody(required = false) ReportLost userBody) {
		Map<String, Object> map = new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			if (userBody != null) {
				lost = userBody;
			}
			System.out.println("dddddddddddddddddddddddddd" + lost.getTripId());
			se.getTransaction().begin();
			LostItem lostItem = new LostItem();
			UserTrip userTrip = se.find(UserTrip.class, lost.getTripId());
			Query q=se.createQuery("from User where apiToken=:api");
			q.setParameter("api", lost.getApiToken());
			User user=(User) q.getSingleResult();
			//User user = se.find(User.class, lost.getApiToken());
			if (userTrip == null) {
				map.put("status", "Trip-not-found");
				return map;
				// return Utility.constructJSON("Trip-not-found", null, null);
			}
			if (user == null) {
				map.put("status", "User-not-found");
				return map;
				// return Utility.constructJSON("User-not-found", null, null);
			}
			lostItem.setUser_id(user);
			lostItem.setTrip_Id(userTrip);
			se.persist(lostItem);
			se.getTransaction().commit();
			map.put("status", "done");
			return map;
			// return Utility.constructJSON("done", null, null);
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e;// return
					// Utility.constructJSON(e.getStackTrace().toString(), null,
					// null);

		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}

	}

	@RequestMapping("/reportComplaint")
	public @ResponseBody Map<String, Object> reportComplaint(ReportComplaint complaint) {
		Map<String,Object>map=new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
//			if (complaintBody != null) {
//				complaint = complaintBody;
//			}
			se.getTransaction().begin();
			boolean insert = false;
			UserComplaint userComplaint = new UserComplaint();
			Query q=se.createQuery("from User where apiToken=:api");
			q.setParameter("api", complaint.getApiToken());
			User user=(User) q.getSingleResult();
			//User user = se.find(User.class, complaint.getApiToken());
			UserTrip userTrip = se.find(UserTrip.class, complaint.getTripId());
			UserComplaintType userComplaintType = se.find(UserComplaintType.class, complaint.getComplaintTypeId());
			userComplaint.setTrip_id(userTrip);
			//userComplaint.setUser_id(user);
			userComplaint.setComplaint_type(userComplaintType);
			userComplaint.setType("user");
			userComplaint.setActive(false);
			se.persist(userComplaint);
			List<MultipartFile> image = new ArrayList<>();
			image = complaint.getImage();
			if (image == null) {
				image = new ArrayList<>();
			}
			List<String> ret = new ArrayList<>();
			for (MultipartFile fl : image) {
				UserComplaintImage complaintImage = new UserComplaintImage();
//				complaintImage.setPath(complaint.getComplaintDescription());
				complaintImage.setUser_complaint(userComplaint);
				complaintImage.setImage(fileUploader.uploadFile(fl));
				se.persist(complaintImage);
			}
			// se.getTransaction().commit();
			// for (String str : image) {
			// complaintImage.setImage(str);
			// }

			// se.persist(userComplaint);
			// se.persist(complaintImage);
			se.getTransaction().commit();
			insert = true;
			if (insert){
				//return Utility.constructJSONGetHelp("done", null, null);
				map.put("status", "done");
				return map;
			}else{
				map.put("status", "Error occcurred please try again");
				return map;
				//return Utility.constructJSONGetHelp("Error occcurred please try again", null, null);
			}
			} catch (Exception e) {
			se.getTransaction().rollback();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(bos));
			return Utility.constructJSON(bos.toString(), null, null);
		} finally {
			if (se.getTransaction().isActive()) { se.getTransaction().commit(); }
			se.close();
		}

	}


	@SuppressWarnings("unchecked")
	@RequestMapping("/complaintTypes")
	public @ResponseBody Map<String, Object> complaintTypes() {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		try {

			boolean insert = false;

			se.getTransaction().begin();
			Query q = se.createQuery("from UserComplaintType where type='user'");
			List<UserComplaintType> list = new ArrayList<>();
			insert = true;
			list = q.getResultList();
			List<Map<String, Object>> ret = new ArrayList<>();
			for (UserComplaintType uct : list) {
				// Map<String, Object> map = new HashMap<>();
				map.put("id", uct.getId());
				map.put("complaint title", uct.getName());
				ret.add(map);
			}
			if (insert) {
				map2.put("status", "done");
				map2.put("complaints", ret);
				return map2;
				// return Utility.constructJSONGetComplaints("done", ret);
			} else {
				map2.put("status", "Error occurred please try again");
				return map2;
				// return Utility.constructJSONGetComplaints("Error occurred
				// please try again", null);
			}
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e; // return
						// Utility.constructJSONGetComplaints(e.getStackTrace().toString(),
						// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}

	}

	@RequestMapping("/addPlaceForUser")
	public @ResponseBody Map<String, Object> addPlaceForUser(AddPlaceForUser place,
			@RequestBody(required = false) AddPlaceForUser placeBody) {
		Map<String, Object> map = new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			if (placeBody != null) {
				place = placeBody;
			}
			boolean insert = false;

			se.getTransaction().begin();
			UserPlace userPlace = new UserPlace();
			
			Query q = se.createQuery("from UserPlace as up where up.user_id.apiToken=? and up.latitude=? and up.longitude=?");
			q.setParameter(1, place.getApiToken());
			q.setParameter(2, place.getLatitude());
			q.setParameter(3, place.getLongitude());
			Integer placeId = null;
			List<UserPlace> list = new ArrayList();
			list = q.getResultList();
			for (UserPlace up : list) {
				insert = true;
				placeId=up.getId();
			}
			if (insert) {
				map.put("status", "done");
				map.put("placeId", String.valueOf(placeId));
				return map;
				// return Utility.constructJSONGetHelp("done", null, null);
			} else if (insert == false) {
			    q=se.createQuery("from User where apiToken=:api");
				q.setParameter("api", place.getApiToken());
				User user=(User) q.getSingleResult();
				//User user = se.find(User.class, place.getApiToken());
				userPlace.setUser_id(user);
				userPlace.setCategory(place.getCategory());
				userPlace.setName(place.getName());
				userPlace.setLongitude(place.getLongitude());
				userPlace.setLatitude(place.getLatitude());
				se.persist(userPlace);
				placeId=userPlace.getId();
				se.getTransaction().commit();
				map.put("status", "done");
				map.put("placeId", String.valueOf(placeId));
				return map;
				// return Utility.constructJSONGetHelp("done", null, null);
			} else {
				map.put("status", "Error please try again");
				return map;
				// return Utility.constructJSONGetHelp("Error please try again",
				// null, null);
			}
		} catch (Exception e) {
			UserPlace userPlace=new UserPlace();
			Query q=se.createQuery("from User where apiToken=:api");
				q.setParameter("api", place.getApiToken());
				User user=(User) q.getSingleResult();
			userPlace.setUser_id(user);
			userPlace.setCategory(place.getCategory());
			userPlace.setName(place.getName());
			userPlace.setLongitude(place.getLongitude());
			userPlace.setLatitude(place.getLatitude());
			se.persist(userPlace);
			se.getTransaction().commit();
//			se.getTransaction().rollback();
//			throw e;// return
					// Utility.constructJSONGetHelp(e.getStackTrace().toString(),
					// null, null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}
		return null;
	}

	                    
	@RequestMapping("/getUserPlaces")
	public @ResponseBody Map<String, Object> getUserPlaces(AddPlaceForUser place,
			@RequestBody(required = false) AddPlaceForUser placeBody) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map1 = new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		try {

			if (placeBody != null) {
				place = placeBody;
			}
			boolean insert = false;

			se.getTransaction().begin();
			Query q = se.createQuery("from UserPlace as up where up.user_id.apiToken=?");
			q.setParameter(1, place.getApiToken());
			List<UserPlace> list = new ArrayList();
			list = q.getResultList();
			List<Map<String, Object>> places = new ArrayList<>();
			for (UserPlace up : list) {
				insert = true;
				// Map<String, Object> map = new HashMap();
				map.put("id", up.getId());
//				map.put("name", up.getName());
//				map.put("category", up.getCategory());
				
				map.put("address", up.getName());
				map.put("name", up.getCategory());

				map.put("latitude", up.getLatitude());
				map.put("longitude", up.getLongitude());
				places.add(map);
			}
			se.getTransaction().commit();
			if (insert) {
				map1.put("status", "done");
				map1.put("places", places);
				return map1;
				// return Utility.constructJSONGetPlaces("done", places);
			} else {
				map.put("status", "There is no places");
				return map;
				// return Utility.constructJSONGetPlaces("There is no places",
				// null);
			}
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e; // return
						// Utility.constructJSONGetPlaces(e.getStackTrace().toString(),
						// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}

	}

	@RequestMapping("/deleteUserPlaces")
	public @ResponseBody Map<String, Object> deleteUserPlaces(AddPlaceForUser place,
			@RequestBody(required = false) AddPlaceForUser placeBody) {
		Map<String, Object> map = new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			if (placeBody != null) {
				place = placeBody;
			}
			boolean insert = false;

			se.getTransaction().begin();
			
//			Query q = se.createQuery("delete UserPlace as up join up.user_id as u where u.apiToken=:api and up.placeId=:pi");
			Query q = se.createQuery(
					"select place from UserPlace place where place.user_id.apiToken = :api and place.id = :pi",
					UserPlace.class);
			q.setParameter("api", place.getApiToken());
			q.setParameter("pi",Integer.valueOf(place.getPlaceId()));
			if(q.getSingleResult()!=null) {
				UserPlace up = (UserPlace) q.getSingleResult();
				Query q2 = se.createQuery("delete from UserPlace up where up.id = :id");
				q2.setParameter("id", up.getId());
				q2.executeUpdate();
//				se.remove(up);
				map.put("status", "done");
				se.getTransaction().commit();
				return map;
			}else{
				map.put("status", "There is no places");
				se.getTransaction().commit();
				return map;
			}
			
////			int mdification = q.executeUpdate();
//			if (mdification != 0) {
//				map.put("status", "done");
//				return map;
//				// return Utility.constructJSONGetHelp("done", null, null);
//			} else {
//				map.put("status", "There is no places");
//				return map;
//				// return Utility.constructJSONGetHelp("There is no places",
//				// null, null);
//			}
		} catch (Exception e) {
			
			map.put("status", "There is no places");
			return map;
			
//			se.getTransaction().rollback();
//			throw e;
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}
		// return null;
	}

	@RequestMapping("/sendCodeToPhone")
	public @ResponseBody Map<String, Object> sendCodeToPhone(User user, @RequestBody(required = false) User userBody) {
		Map<String, Object> map = new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			if (userBody != null) {
				user = userBody;
			}
			boolean insert = false;
			
			se.getTransaction().begin();
			Query q = se.createQuery("from User where phone=:ph");
			q.setParameter("ph", user.getPhone());
			String userCode = null;
			List<User> list = new ArrayList<>();
			list = q.getResultList();
			se.getTransaction().commit();
			if (null != list && !list.isEmpty()) {
				if (0 == verifyPhoneNumber(user.getPhone(), 
						String.valueOf(list.get(0).getActivationCode()))) {
					map.put("status", "done");
				}
			}
		} catch (Exception e) {
			se.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if (map.isEmpty()) {
				se.getTransaction().rollback();
			}
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}
		
		if (map.isEmpty()) {
			map.put("status", "failed");
		}
		return map;
	}
	
	private static int verifyPhoneNumber(String phone, String code) {
		String region = "QA";
		
		/* Parse and validate phone number. */
		PhoneNumberUtil util = PhoneNumberUtil.getInstance();
		Phonenumber.PhoneNumber pn;
		try {
			pn = util.parse(phone, region);
		} catch (NumberParseException e){
			e.printStackTrace();
			return 1;
		}
		
		String phoneString = "+" + pn.getCountryCode() + pn.getNationalNumber();
		System.out.println("Phone Number: " + phoneString);
		int status = 0;
		
		if (util.isValidNumberForRegion(pn, region)) {
			/* Check for non empty result. */
				String msg = "Your QABI verification code: " + code;
				System.out.println("Sending SMS to " + phoneString + ": " + msg);
				
				/* Send an SMS to the user with the activation code. */
				if (0 == sendSms(phoneString, msg)) {
					System.out.println("Successfully sent sms");
				} else {
					status = 1;
				}
		} else {
			System.out.println("Phone number (" + phoneString + ") not valid for region: " + region);
			status = 1;
		}
		return status;
	}
	
	private static int sendSms(String phoneNumber, String msg) {
		String site = "https://platform.clickatell.com/messages";
		String json = "{\"content\":\"" + msg + "\",\"to\":[\"" + phoneNumber + "\"]}";
		StringEntity data = new StringEntity(json, ContentType.APPLICATION_JSON);
		
		System.out.println("Sending (" + site + "): " + json);
		Request req = Request.Post(site)
		.body(data)
		.addHeader("Content-Type", "application/json")
		.addHeader("Accept", "application/json")
		.addHeader("Authorization", "DW2shB3ORr6aLvtOKym7CQ==");
		
		try {
		    Content res = req.execute().returnContent();
			System.out.println(res.asString());
		} catch (IOException e) {
			System.out.println("Unable to execute http request");
			e.printStackTrace();
			return 1;	
		}
		return 0;
	}

	@RequestMapping("/startTrip")
	public @ResponseBody Map<String,Object> startTrip(StartTrip startTrip,@RequestBody(required = false) StartTrip startTripBody){
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			if (startTripBody != null) {
				startTrip = startTripBody;
			}
			///////////////////////////
			// SOCKET BINDING
			///////////////////////////
			asyncs.startTrip(startTrip, webSocketController);
			///////////////////////////
			//
			///////////////////////////
			
			se.getTransaction().begin();
			//User user=se.find(User.class,startTrip.getClientId());
			
			Driver driver=se.find(Driver.class, startTrip.getUserId());
			//UserTrip userTrip=new UserTrip();
			UserTrip userTrip=se.find(UserTrip.class , startTrip.getTripId());
			//userTrip=se.find(UserTrip.class, startTrip.getTripId());
			userTrip.setDriver_id(driver);
			User user=se.find(User.class,userTrip.getUser_id().getId());
			//System.out.print("userId"+user.getId());
			Car car=se.find(Car.class,startTrip.getCarId());
			userTrip.setUser_id(user);
			userTrip.setCar_id(car);
			userTrip.setDriver_id(driver);
			userTrip.setStartAtAddress(startTrip.getStartAddress());
			userTrip.setStartAtLatitude(startTrip.getStartLatitude());
			userTrip.setStartAtLongitude(startTrip.getStartLongitude());
			userTrip.setStartAt(new Date());
			se.persist(userTrip);
			
			System.out.println("carId"+userTrip.getCar_id());
			System.out.print("driverId"+userTrip.getDriver_id());
			
			Map<String,Object> map=new HashMap<>();
			 map.put("status", "done");
			 return map;
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e; //return Utility.constructJSONGetComplaints(e.getStackTrace().toString(), null);
		} finally {
			if (se.getTransaction().isActive()) { se.getTransaction().commit(); }
			se.close();
		}
				
	
				
	}


	@RequestMapping("/updateUserData")
	public @ResponseBody Map<String, Object> updateUserData(@Valid UserWithImages userWithImages, BindingResult res) {
		Map<String, Object> map = new HashMap<>();
		if (res.hasErrors()) {
			Map<String, Object> ret = new HashMap<>();

			ret.put("errors", res.getAllErrors());
			return ret;
		}

		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			UserWithImages user = userWithImages;
			boolean insert = false;

			se.getTransaction().begin();
			
			Query q=se.createQuery("from User where apiToken=:api");
			q.setParameter("api", user.getApiToken());
			User user1=(User) q.getSingleResult();
			User u = se.find(User.class, user1.getId());
			if (user.getName() != null)
				u.setName(user.getName());
			if (user.getPhone() != null)
				u.setPhone(user.getPhone());
			try {
				if (user.getImage() != null)
					u.setUserImage(fileUploader.uploadFile(user.getImage()));
			} catch (Exception ex) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ex.printStackTrace(new PrintWriter(os));
				Map<String, Object> json = new HashMap<>();
				json.put("status", "upload error");
				json.put("error", os.toString());
				return json;
			}
			if (user.getEmail() != null)
				u.setEmail(user.getEmail());
			se.persist(u);

			// se.getTransaction().commit();
			// if (modification != 0)
			// return Utility.constructJSONGetList("done", null);
			map.put("status", "done");
			return map;
			// else if (modification == 0)
			// return Utility.constructJSONGetList("please try again", null);
			// else
			// return null;
		} catch (Exception e) {
			se.getTransaction().rollback();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(bos));
			return Utility.constructJSONGetList(bos.toString(), null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}
	}

	@RequestMapping("/addOrUpdateRate")
	public @ResponseBody Map<String, Object> addOrUpdateRate(AddOrUpdateRate addOrUpdateRate,
			@RequestBody(required = false) AddOrUpdateRate addOrUpdateRateBody) {
		Map<String, Object> map = new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			if (addOrUpdateRateBody != null) {
				addOrUpdateRate = addOrUpdateRateBody;
			}
			boolean update = false;
			se.getTransaction().begin();
			
			
			UserTrip userTrip = se.find(UserTrip.class, addOrUpdateRate.getTripId());

			if (userTrip == null) {
				// return Utility.constructJSON("Trip-not-found", null, null);
				map.put("status", "Trip-not-found");
				return map;
			}
			
			
            Rates rate=new Rates();
            rate .setUserRate(addOrUpdateRate.getRate());
            rate.setUserComment(addOrUpdateRate.getComment());
            rate.setTrip_id(userTrip);
            se.persist(rate);
            map.put("status","done");
      		return map;
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e;// return
					// Utility.constructJSON(e.getStackTrace().toString(), null,
					// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}

	}


	@RequestMapping("/cancelTrip")
	public @ResponseBody Map<String, Object> cancelTrip(CancelTrip cancelTrip,
			@RequestBody(required = false) CancelTrip cancelTripBody) {
		Map<String, Object> map = new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		UserTrip userTrip ;
		if (cancelTripBody != null) {
			cancelTrip = cancelTripBody;
		}
		userTrip = se.find(UserTrip.class, cancelTrip.getTripId());
		try {
			
			boolean update = false;

			se.getTransaction().begin();
			
		//	if(cancelTrip.getTripId()!=null){}
			
			if(userTrip == null){
				// return Utility.constructJSON("error", "There is no such
				// trip", null);
				map.put("status", "error");
				map.put("userId", "there is no such trip");
				return map;
			}
				
			
				
			
			if (userTrip.getUser_id().getApiToken().equals(cancelTrip.getApiToken())) {
				TripCanceled tripCanceled = new TripCanceled();
				tripCanceled.setTrip_id(userTrip);
				tripCanceled.setCutMoney(cancelTrip.getCutMoney());
				tripCanceled.setReason(cancelTrip.getReason());
				//Reason reason=se.find(Reason.class, cancelTrip.getReason());
				//tripCanceled.setReason_id(reason);
				//tripCanceled.setComment(cancelTrip.getReason());
				tripCanceled.setCanceledByUser(cancelTrip.getIsCanceledByUser());
				se.persist(tripCanceled);
				se.getTransaction().commit();
				map.put("status", "done");
			//	asyncs.cancelTrip(userTrip, cancelTrip, webSocketController);
				asyncs.cancelTrip(userTrip.getUser_id().getId(), userTrip.getDriver_id().getId());
				return map;
				// return Utility.constructJSON("done", null, null);
			} else {
				// return Utility.constructJSON("error", "There is no trip
				// related with this id", null);
				map.put("status", "error");
				map.put("userId", "There is no trip related with this id");
				return map;
			}
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e;// return
					// Utility.constructJSON(e.getStackTrace().toString(), null,
					// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}

	}


	@RequestMapping("/endTrip")
	public @ResponseBody Map<String, Object> endTrip(EndTrip endTrip,
			@RequestBody(required = false) EndTrip endTripBody) {
		EntityManager se = entityManagerFactory.createEntityManager();
		Map<String,Object> obj=new HashMap<>();
		try {
			if (endTripBody != null) {
				endTrip = endTripBody;
			}
			boolean update = false;

			se.getTransaction().begin();
			// Query q=se.createQuery("update UserTrip set
			// endAtLatitude=:d1,endLongtude=:d2,endAt=:now,distance=:d3,realCost=:d4,status=Status.finished
			// where"
			// + " user_id.id=:d4 and id=:d5");
            UserTrip userTrip;
            userTrip=se.find(UserTrip.class, endTrip.getTripId());
            float kmPrice =userTrip.getCar_type().getKMPrice();
            float minutePrice =userTrip.getCar_type().getMinutePrice();
            float basefare =userTrip.getCar_type().getBasefare();
            Date d1=userTrip.getStartAt();
//            Date d2=userTrip.getEndAt();
            Date d2=new Date();
            long diff = Math.abs(d1.getTime() - d2.getTime());
            long diffMinutes = diff / ( 60 * 1000 );
           
			///////////////////////////
			// SOCKET BINDING
			///////////////////////////
			asyncs.endTrip(userTrip, endTrip, webSocketController);
			///////////////////////////
			//
			///////////////////////////
            Query q= se.createQuery("select sum(moneyValue) from FreeRide where user_id.id=:u"
            		+ " and expiredDate >=:d");
            q.setParameter("u", userTrip.getUser_id().getId());
            q.setParameter("d", new Date());
            long freeRide=0;
            if( q.getSingleResult()==null)
            	freeRide=0;
            else
            freeRide=  (long) q.getSingleResult();
            float cost=((endTrip.getDistance()*kmPrice)+(diffMinutes*minutePrice)+basefare)-freeRide;
//		    q = se.createQuery(
//					"update UserTrip set endAtLatitude=:d1,endAtLongitude=:d2,endAt=:now,"
//					+ "distance=:d3,realCost=:d4,status=:status where"
//							+ " driver_id.apiToken=:d5 and id=:d6");
            
		UserTrip trip = se.find(UserTrip.class, endTrip.getTripId());
		//q=se.createQuery("update UserTrip set endAtLatitude=:d1,endAtLongitude=:d2,endAt=:now"
            	//	+ ",distance=:d3,realCost=:d4,status=:status where driver_id.apiToken=:d5"
            	//	+ " and id=:d6");
			trip.setEndAtLatitude(endTrip.getEndLatitude());
			trip.setEndAtLongitude(endTrip.getEndLongtude());
			trip.setEndAt(new Date());
			trip.setDistance(endTrip.getDistance());
			trip.setRealCost(endTrip.getRealCost());
			trip.setStatus(Status.finished);
			//int modification = q.executeUpdate();
			se.persist(trip);
			se.getTransaction().commit();
			Map<String,Object> map=new HashMap<>();
			map.put("status", "done");
			map.put("cost", cost);
//			if (modification != 0)
				return map;
//			else
//				//return Utility.constructJSON("Error occurred please try agian", null, null);
//				obj.put("status", "Error occurred please try agian");
//				return obj;
			
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e;//return Utility.constructJSON(e.getStackTrace().toString(), null, null);
		} finally {
			if (se.getTransaction().isActive()) { se.getTransaction().commit(); }
			se.close();
		}

	}

	
	@RequestMapping("/addPromoCode")
	public @ResponseBody Map<String, Object> addPromoCode(User user, @RequestBody(required = false) User userBody) {
		Map<String, Object> map = new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			if (userBody != null) {
				user = userBody;
			}
			
			System.out.println("userCode" + user.getUserCode());
			boolean Found = false;
			User userFriend = null;
			se.getTransaction().begin();
			int length = (user.getUserCode() != null ? user.getUserCode().length() : 0);

			if (length == 7) {
				 Query q=se.createQuery("from User where apiToken=:api");
				 q.setParameter("api", user.getApiToken());
				 User user1=(User) q.getSingleResult();
				User currentUser = se.find(User.class, user1.getId());
				if (currentUser.getFriendCode() != null) {
					se.getTransaction().commit();
					map.put("status", "You have afriend code from before");
					return map;
					// return Utility.constructJSON("You have afriend code from
					// before", null, null);

				} else {
				    q = se.createQuery("from User where userCode=:uc");
					q.setParameter("uc", user.getUserCode());
					List<User> list = q.getResultList();
					for (User u : list) {
						Found = true;
						userFriend = u;
					}
					if (!Found) {
						se.getTransaction().commit();
						map.put("status", "User code not found");
						return map;
						// return Utility.constructJSON("User code not found",
						// null, null);
					} else {
						q = se.createQuery("from User where userCode=:uc");
						q.setParameter("uc", user.getUserCode());
						List<User> list2 = q.getResultList();
						for (User u : list2) {
							Found = true;
							userFriend = u;
						}
						currentUser.setPoints(currentUser.getPoints() + DistanceWebService.pointsForfriendCode(se));
						userFriend.setPoints(userFriend.getPoints() + DistanceWebService.pointsForfriendCode(se));
						currentUser.setFriendCode(user.getUserCode());
						se.persist(currentUser);
						se.persist(userFriend);
						// se.getTransaction().commit();

						DistanceWebService.addFreeRide(currentUser, se);
						DistanceWebService.addFreeRide(userFriend, se);
						//q = se.createQuery("from FreeRide where user_id.id=:id ");
						q = se.createQuery("from FreeRide where user_id.apiToken=:id ");
						q.setParameter("id", user.getApiToken());
						List<FreeRide> freeRides = q.getResultList();

						List<Map<String, Object>> objList = new ArrayList();
						// freeRide.put("TotalPoints", currentUser.getPoints());
						for (FreeRide f : freeRides) {
							Map<String,Object> freeRide = new HashMap<>();
							freeRide.put("id", f.getUser_id().getId());
							freeRide.put("moneyValue", f.getMoneyValue());
							freeRide.put("expireDate", f.getExpiredDate());
							objList.add(freeRide);
						}
						Map<String, Object> ret = new HashMap<>();
						ret.put("status", "done");
						ret.put("totalpoints", currentUser.getPoints());
						ret.put("freeRides", objList);
						se.getTransaction().commit();
						return ret;
					}
				}

			} else {
				int count = 0;
				Promotion promo = null;
				Boolean repeated = false;
				Query q = se.createQuery("from Promotion where code=:c");
				q.setParameter("c", user.getUserCode());
				List<Promotion> list = q.getResultList();
				for (Promotion p : list) {
					promo = p;
				}
				q = se.createQuery("select distinct prm from Promotion prm "
						+ "join prm.user_id u where prm.code = :c and u.apiToken = :api");
				// q=se.createQuery("from Promotion p where
				// p.user_id.user_id=:id");
				q.setParameter("api", user.getApiToken());
				q.setParameter("c", user.getUserCode());
				List<Promotion> list2 = q.getResultList();
				for (Promotion p : list2) {
					repeated = true;
					count++;
				}
				if (repeated) {
					se.getTransaction().commit();
					// return Utility.constructJSON("You get this promo from
					// before", null, null);
					map.put("status", "You get this promo from before");
					return map;
				}

				// Promotion promo=se.find(Promotion.class, user.getUserCode());
				if (promo != null && promo.getExpDate().after(new Date())) {
				    q=se.createQuery("from User where apiToken=:api");
					q.setParameter("api", user.getApiToken());
					User user1=(User) q.getSingleResult();
					//User u = se.find(User.class, user1.getId());
					User currentUser = se.find(User.class, user1.getId());
					currentUser.setPoints(promo.getPoint() + currentUser.getPoints());
					se.persist(currentUser);

					DistanceWebService.addFreeRide(currentUser, se);
					q = se.createQuery("from FreeRide where user_id.id=:id ");
					q.setParameter("id", user1.getId());
					List<FreeRide> freeRides = q.getResultList();

					List<Map<String, Object>> objList = new ArrayList();
					// freeRide.put("TotalPoints", currentUser.getPoints());
					for (FreeRide f : freeRides) {
						Map<String,Object> freeRide = new HashMap<>();
						freeRide.put("id", f.getUser_id().getId());
						freeRide.put("moneyValue", f.getMoneyValue());
						freeRide.put("expireDate", f.getExpiredDate());
						objList.add(freeRide);
					}
					Map<String, Object> ret = new HashMap<>();
					ret.put("totalpoints", currentUser.getPoints());
					ret.put("freeRides", objList);
                    currentUser.getPromotion_id().add(promo);
					promo.getUser_id().add(currentUser);
					promo.setCountNow(promo.getCountNow() - 1);
					se.persist(promo);

					se.getTransaction().commit();
					return ret;
				} else {
					se.getTransaction().commit();
					// return Utility.constructJSON("promo is expired or not
					// found", null, null);
					map.put("status", "promo is expired or not found");
					return map;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (se.getTransaction().isActive()) {
				se.getTransaction().rollback();
			}
			// throw e;//return
			// Utility.constructJSON(e.getStackTrace().toString(), null, null);
		} finally {
			// if (se.getTransaction().isActive()) {
			// se.getTransaction().commit(); }
			se.close();
		}
		return null;
	}

	@RequestMapping("/reasons")
	public @ResponseBody Map<String, Object> reasons() {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> mapret = new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		try {

			boolean insert = false;

			se.getTransaction().begin();
			Query q = se.createQuery("from Reason");
			List<Reason> list = new ArrayList<>();

			list = q.getResultList();
			List<Map<String, Object>> ret = new ArrayList<>();
			if (list.isEmpty()) {
				mapret.put("status", "There is no reason");
				return mapret;
				
			}
			for (Reason ur : list) {
				 // return Utility.constructJSONGetComplaints("There is no
					// reason", null);
				insert = true;
				// Map<String, Object> map = new HashMap<>();
				map.put("id", ur.getId());
				map.put("reason", ur.getReason());
				ret.add(map);
			}
			if (insert) {
				// return Utility.constructJSONGetComplaints("done", ret);
				mapret.put("status", "done");
				mapret.put("complaints", ret);
				return mapret;
			} else
				mapret.put("status", "Error occurred please try again");
			return mapret;
			
			// return Utility.constructJSONGetComplaints("Error occurred please
			// try again", null);
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e; // return
						// Utility.constructJSONGetComplaints(e.getStackTrace().toString(),
						// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}

	}
	@RequestMapping("/requestTrip")
	public @ResponseBody Map<String, Object> requestTrip(RequestTrip requestTrip,
			@RequestBody(required = false) RequestTrip requestTripBody) {
		Map<String,Object> obj=new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			if (requestTripBody != null) {
				requestTrip = requestTripBody;
			}
			se.getTransaction().begin();	
			Boolean insert=false;
			Query q=se.createQuery("from User where apiToken=:api");
			
			q.setParameter("api", requestTrip.getApiToken());
			User user=(User) q.getSingleResult();
			
			User currentUser=se.find(User.class,user.getId());
			CarType carType=se.find(CarType.class,requestTrip.getCarTypeId());
//*			UserPaymentMethod userPaymentMethod=se.find(UserPaymentMethod.class, requestTrip.getPaymentMethodId());

			UserTrip userTrip=new UserTrip();

			userTrip.setUser_id(currentUser);
			userTrip.setCar_type(carType);
//*			userTrip.setPayment_Method(userPaymentMethod);
			userTrip.setCurrentLatitude(requestTrip.getCurrentLatitude());
			userTrip.setCurrentLongitude(requestTrip.getCurrentLongitude());
			userTrip.setStartAtLatitude(requestTrip.getStartLatitude());
			userTrip.setStartAtLongitude(requestTrip.getStartLongitude());
			userTrip.setEndAtLongitude(requestTrip.getEstimatedEndLongitude());
			userTrip.setEndAtLatitude(requestTrip.getEstimatedEndLatitude());
			userTrip.setEstimatedDistance(requestTrip.getEstimatedDistance());
			userTrip.setStartAtAddress(requestTrip.getStartAtAddress());
			userTrip.setEndAtAddress(requestTrip.getEndAtAddress());
			userTrip.setEstimatedCost(requestTrip.getEstimatedCost());
			userTrip.setTripeCostRate(requestTrip.getTripCostRate());
			userTrip.setStatus(Status.oppened);
			se.persist(userTrip);

			Map<String,Object> map=new HashMap<>();
			//map.put("carId", requestTrip.getCarId());
			map.put("carTypeId", requestTrip.getCarTypeId());
			map.put("userId", user.getId());
			map.put("startLatitude", requestTrip.getStartLatitude());
			map.put("startLongitude", requestTrip.getStartLongitude());
			map.put("startAtAddress", requestTrip.getStartAtAddress());
			map.put("endAtAddress", requestTrip.getEndAtAddress());
			map.put("tripId", userTrip.getId());
			
			
			//endLongitude , endLatitude
			map.put("endLongitude", userTrip.getEndAtLongitude());
			map.put("endLatitude", userTrip.getEndAtLatitude());
			// webSocketController=new WebSocketController();
			TimedLocation timedLocation=new TimedLocation();
			timedLocation.setLatitude(Double.parseDouble(requestTrip.getStartLatitude()));
			timedLocation.setLongitude(Double.parseDouble(requestTrip.getStartLongitude()));

			//webSocketController.carGet(se, timedLocation, map);

			Map<String, Object> ret=new HashMap<>();
			
			insert=true;
			se.flush();
			if(insert){
				se.getTransaction().commit();
				ret = Utility.constructJSONGetRequestTrip("done",requestTrip.getApiToken());
				
			}				
			else{
				se.getTransaction().commit();
				ret.put("status", "error");
				ret.put("message", "error occurred try again");
				
				
			}
			///////////////////////
			//
			///////////////////////
			asyncs.carGet(map, webSocketController);
            		return ret;
			///////////////////////
			//
			///////////////////////
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e; //return Utility.constructJSONGetComplaints(e.getStackTrace().toString(), null);
		} finally {
			if (se.getTransaction().isActive()) { se.getTransaction().commit(); }
			se.close();
		}

	}


	// =============================================================================================
	// Driver

	@RequestMapping("/driverLogin")
	public @ResponseBody Map<String, Object> driverLogin(Driver driver,
			@RequestBody(required = false) Driver driverBody) {
		Map<String, Object> map = new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			if (driverBody != null) {
				driver = driverBody;
			}

			// System.out.println(new
			// BCryptPasswordEncoder(16).encode("123456"));

			boolean isUserAvailable = false;
			se.getTransaction().begin();
			// Query q = se.createQuery("select distinct d from Driver d "
			// + " left join d.payment_method method "
			// + " join d.driver_car car where d.phone=?");
			Query q = se.createQuery("from Driver where phone=:ph");
			q.setParameter("ph", driver.getPhone());
			// q.setParameter(1, password);
			String driver_id = null;
			List<Driver> drive = q.getResultList();
//*		List<DriverPaymentMethod> payment_method = new ArrayList();
			// List<Map<object,String>> driver_car=new ArrayList();
			for (Driver u : drive) {
				if (u != null) {
					if (DefaultPasswordHasher.getInstance().isPasswordValid(driver.getPassword(), u.getPassword())
							&& u.isActive()) {

						isUserAvailable = true;
						Map<String, Object> obj = new HashMap<>();
						q=se.createQuery("select avg(rateValue) from DriverRate where driver_id.id=:id");
						q.setParameter("id", u.getId());
						Double rating=(Double) q.getSingleResult();
						
						///u.getPayment_method().get(0);
						obj.put("status", "done");
						obj.put("apiToken", u.getApiToken());
						obj.put("id", String.valueOf(u.getId()));
						obj.put("name", u.getName());
						obj.put("email", u.getEmail());
						obj.put("phone", u.getPhone());
						//obj.put("password", u.getPassword());
						obj.put("profileImage", u.getProfileImage());
						obj.put("isActive", u.isActive());
						obj.put("job", u.getJob());
						obj.put("age", u.getAge());
						obj.put("address", u.getAddress());
						obj.put("militaryStatus", u.getCreatedAt());
						obj.put("socialStatus", u.getSocialStatus());
						obj.put("idNumber", u.getIdNumber());
					    obj.put("rating", rating);
					    obj.put("profileImage", u.getProfileImage());
						//obj.put("payment_method","null");

//						List<Map<String, Object>> list = new ArrayList();
//						Iterator<DriverCar> iterator = u.getDriver_car().iterator();
//						int i = 0;
//						while (iterator.hasNext()) {
//							DriverCar curr = iterator.next();
//							Map<String, Object> asMap = new HashMap<>();
//							asMap.put("id", curr.getId());
//							asMap.put("startAt", curr.getStarteAt());
//							asMap.put("carId", curr.getCar_id().getId());
//							asMap.put("updatedAt", curr.getUpdatesAt());
//							asMap.put("createdAt", curr.getCreatedAt());
//							list.add(asMap);
//							i++;
//							if (i > 10) {
//								break;
//							}

//						}
						//obj.put("driver_car", list);
						obj.put("createdAt", u.getCreatedAt());
						obj.put("updatedAt", u.getUpdatedAt());
						obj.put("startAt", u.getStartAt());
						obj.put("endAt", u.getEndAt());
						obj.put("activationCode", u.getActivationCode());
						// u.setStatus("done");

						// return u;
						return obj;

					} else if (u.isActive() != true && DefaultPasswordHasher.getInstance()
							.isPasswordValid(driver.getPassword(), u.getPassword())) {
						// return Utility.constructJSONValidate("You not
						// activated yet", "" + u.getId(),
						// u.getActivationCode());
						map.put("status", "You not activated yet");
						map.put("userId", u.getId());
						map.put("userCode", u.getActivationCode());
						return map;
						// Driver driver2 = new Driver();
						// driver2.setStatus("You not activated yet");

						// return driver2;
					}
				}

			}
			// Driver driver2 = new Driver();
			// driver2.setStatus("your phone or password are not exist");
			// return driver2;
			// return Utility.constructJSON("your phone or password are not
			// exist", null, null);
			map.put("status", "your phone or password are not exist");
			return map;
		} catch (Exception e) {
			se.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}
		return null;
	}

	@RequestMapping("/ValidateDriverCode")
	public @ResponseBody Map<String, Object> ValidateDriverCode(Driver driver,
			@RequestBody(required = false) Driver driverBody) {
		// SessionFactory sf = ((SessionFactory) context.getAttribute("sf"));
		Map<String, Object> map = new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			if (driverBody != null) {
				driver = driverBody;
			}

			se.getTransaction().begin();
			Query q = se.createQuery("from Driver where apiToken=:api");
			q.setParameter("api", driver.getApiToken());
			
			List<Driver> driv = q.getResultList();
			boolean validate = false;
			Driver theDriver = null;
			for (Driver u : driv) {
				if (u.getActivationCode().equals(driver.getActivationCode())) {

					Query q1 = se.createQuery("update Driver set isActive=true where id=?");
					q1.setParameter(1, u.getId());
					int modification = q1.executeUpdate();
					se.getTransaction().commit();
					if (modification != 0) {
						validate = true;
						theDriver = u;
					}
					System.out.println(u.getActivationCode());
					System.out.println(u.getEmail());
					System.out.println(u.getName());
				}
			}
			if (validate) {
				//Driver u = theDriver;
				// return Utility.constructJSON("done", null, null);
				map.put("status", "done");
				return map;
			} else {
				// return Utility.constructJSON("error not the same", null,
				// null);
				map.put("status", "error not the same");
				return map;
			}
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e;// return
					// Utility.constructJSON(e.getStackTrace().toString(), null,
					// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}
	}

	@RequestMapping("/driverForgetPassword")
	public @ResponseBody Map<String, Object> driverForgetPassword(Driver driver,
			@RequestBody(required = false) Driver driverBody) {
		Map<String, Object> map = new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			if (driverBody != null) {
				driver = driverBody;
			}
			String phone = driver.getPhone();
			boolean isUserAvailable = false;
			// SessionFactory sf = ((SessionFactory)
			// context.getAttribute("sf"));

			se.getTransaction().begin();
			Query q = se.createQuery("from Driver where  phone=?");
			q.setParameter(1, phone);

			int driver_apiToken = 0;

			List<Driver> driv = q.getResultList();

			// insertStatus=checkLogin(context,email,password);
			for (Driver u : driv) {
				driver_apiToken = u.getId();
				isUserAvailable = true;
			}
			if (isUserAvailable) {
				System.out.println("true" + driver_apiToken);
				// return Utility.constructJSON("done", driver_id);
				map.put("status", "done");
				map.put("apiToken", driver_apiToken);
				return map;

			} else {
				System.out.println("false" + driver_apiToken);
				// return Utility.constructJSON("error", null, null);
				map.put("status", "error");
				return map;
			}
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e;// return
					// Utility.constructJSON(e.getStackTrace().toString(), null,
					// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}
	}

	@RequestMapping("/driverResetPassword")
	public @ResponseBody Map<String, Object> driverResetPassword(Driver driver,
			@RequestBody(required = false) Driver driverBody) {
		Map<String, Object> map = new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			if (driverBody != null) {
				driver = driverBody;
			}
			boolean isUserAvailable = false;
			// SessionFactory sf = ((SessionFactory)
			// context.getAttribute("sf"));

			se.getTransaction().begin();

			Query q = se.createQuery("update Driver set password=? where apiToken=?");
			q.setParameter(1, DefaultPasswordHasher.getInstance().hashPassword(driver.getPassword()));
			q.setParameter(2, driver.getApiToken());
			int modifications = q.executeUpdate();
			se.getTransaction().commit();
			int driver_id = 0;
			if (modifications > 0) {
				System.out.println("true" + driver_id);
				// return Utility.constructJSON("done", null, null);
				map.put("status", "done");
				return map;

			} else {
				System.out.println("false" + driver_id);
				// return Utility.constructJSON("error occurred please try
				// again", null, null);
				map.put("status", "error occurred please try again");
				return map;
			}
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e;// return
					// Utility.constructJSON(e.getStackTrace().toString(), null,
					// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}
	}

	@RequestMapping("/driverAddNewHelp")
	public @ResponseBody Map<String, Object> driverAddNewHelp(NewHelp newHelp,
			@RequestBody(required = false) NewHelp newHelpBody) {
		Map<String, Object> map = new HashMap<>();
		if (newHelpBody != null) {
			newHelp = newHelpBody;
		}
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			System.out.println("ssssssssssssssssss" + newHelp.getApiToken());

			se.getTransaction().begin();
			Query q = se.createQuery("from Driver where apiToken=:api");
			q.setParameter("api", newHelp.getApiToken());
			Driver driv=(Driver) q.getSingleResult();
			Driver driver = se.find(Driver.class, driv.getId());
			Help help = new Help();
			help.setDriver_id(driver);
			help.setQuestion(newHelp.getQuestion());
			help.setQuestionTitle(newHelp.getQuestionTitle());
			help.setType("driver");
			se.persist(help);
			se.getTransaction().commit();
			map.put("status", "done");
			map.put("questionId", help.getId());
			return map;

			// return Utility.constructJSON("done", null, null);
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e;// return
					// Utility.constructJSON(e.getStackTrace().toString(), null,
					// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}
	}

	@RequestMapping("/driverGetHelp")
	public @ResponseBody Map<String, Object> driverGetHelp(PagedUserForm getHelpForms,
			@RequestBody(required = false) PagedUserForm getHelpFormsBody) {
		Map<String, Object> map = new HashMap<>();
		if (getHelpFormsBody != null) {
			getHelpForms = getHelpFormsBody;
		}
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			Boolean isFound = false;
			// Map<String, Object> globalMap = new HashMap();
			// Map<String, Object> questMap = new HashMap();

			se.getTransaction().begin();
			// User user = se.find(User.class, user.getId());
			Query q = se
					.createQuery("from Help help where help.driver_id.apiToken=? and help.isGlobal=true and type='driver' ");
			q.setParameter(1, getHelpForms.getApiToken());
			q.setFirstResult((getHelpForms.getPage() - 1) * PAGINATION_GET_HELP);
			q.setMaxResults(PAGINATION_GET_HELP);
			List<Help> globalQuestion = q.getResultList();

			List<Map<String, Object>> globalQus = new ArrayList<>();
			List<Map<String, Object>> questions = new ArrayList<>();
			for (Help u : globalQuestion) {
				isFound = true;
				Map<String, Object> oneMap = new HashMap<>();
				oneMap.put("id", u.getId());
				oneMap.put("question_title", u.getQuestionTitle());
				oneMap.put("answer", u.getAnswer());
				globalQus.add(oneMap);
			}
			Query q2 = se
					.createQuery("from Help help where help.driver_id.apiToken=? and help.isGlobal=false and type='driver' ");
			q2.setParameter(1, getHelpForms.getApiToken());
			q2.setFirstResult((getHelpForms.getPage() - 1) * PAGINATION_GET_HELP);
			q2.setMaxResults(PAGINATION_GET_HELP);
			List<Help> notGlobalQuestion = q2.getResultList();
			for (Help u1 : notGlobalQuestion) {
				isFound = true;
				Map<String, Object> questMap = new HashMap<>();
				questMap.put("id", u1.getId());
				questMap.put("question title", u1.getQuestionTitle());
				questMap.put("answer", u1.getAnswer());
				questions.add(questMap);
			}
			try {
				se.getTransaction().commit();
			} catch (RollbackException ex) {
				se.getTransaction().rollback();
			}
			if (isFound) {
				map.put("status", "done");
				map.put("global", globalQus);
				map.put("questions", questions);
				return map;
				// return Utility.constructJSONGetHelp("done", globalQus,
				// questions);
			} else {
				// return Utility.constructJSONGetHelp("there is no help", null,
				// null);
				map.put("status", "there is no help");
				return map;
			}

		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e; // return
						// Utility.constructJSONGetHelp(e.getStackTrace().toString(),
						// null, null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}

	}

	@RequestMapping("/getHistoryForDriver")
	public @ResponseBody Map<String, Object> getHistoryForDriver(PagedUserForm pagedUserForm,
			@RequestBody(required = false) PagedUserForm pagedUserFormBody) {
		Map<String, Object> map2 = new HashMap<>();

		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			if (pagedUserFormBody != null) {
				pagedUserForm = pagedUserFormBody;
			}

			Date date;
			int rateValue, driverId, carId;
			Driver driver = new Driver();
			Car car = new Car();
			boolean isGet = false;

			String time;
			// UserPaymentMethod userPaymentMethod;

			se.getTransaction().begin();
//			Query q = se.createQuery("from Driver where apiToken=:api");
//			q.setParameter("api", pagedUserForm.getApiToken());
		
			//Driver driver=new Driver();
			//Driver newUser = se.find(Driver.class, driver.getId());
		    Query q = se.createQuery("from UserTrip as uT where uT.driver_id.apiToken=?");
			q.setParameter(1, pagedUserFormBody.getApiToken());
			q.setFirstResult((pagedUserFormBody.getPage() - 1) * PAGINATION_GET_HISTORY);
			q.setMaxResults(PAGINATION_GET_HISTORY);
			List<UserTrip> userTrip = q.getResultList();
			List<Map<String, Object>> ret = new ArrayList<>();
			for (UserTrip t : userTrip) {
				isGet = true;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", t.getId());
				map.put("startAdd", t.getStartAtAddress());
				map.put("endAdd", t.getEndAtAddress());
				
				// map.put("rate", t.getRate());

				String name = "";

//				try {
//*					name = t.getPayment_Method().getPayment_type().getName();
//				} catch (NullPointerException ex) {
//
//				}

				map.put("userPaymentMethodName", name);
				map.put("realCost", t.getRealCost());
				map.put("date", t.getStartAt());
				date = t.getStartAt();
				SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm:SS");
				//time = timeFormate.format(date);

				if (t.getUser_id() != null) {

					map.put("userId", t.getUser_id().getApiToken());
					map.put("userName", t.getUser_id().getName());
					map.put("userImage", t.getUser_id().getUserImage());
					map.put("userPhone", t.getUser_id().getPhone());
				} else {
					map.put("userId", "-1");
					map.put("userName", "NO ONE");
					map.put("userImage", "");
					map.put("userPhone", "");
				}
				
				ret.add(map);
			}
			if (isGet) {
				// return Utility.constructJSONGetHistory("done", ret);
				map2.put("status", "done");
				map2.put("trips", ret);
				return map2;
			} else {
				// return Utility.constructJSONGetHistory("there is no trip",
				// null);
				map2.put("status", "there is no trip");
				return map2;
			}
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e; // return
						// Utility.constructJSONGetHelp(e.getStackTrace().toString(),
						// null, null);

		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}

	}

	@RequestMapping("/driverReportComplaint")
	public @ResponseBody Map<String, Object> driverReportComplaint(ReportComplaint complaint) {
		EntityManager se = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap();
		try {
			// if (complaintBody != null) {
			// complaint = complaintBody;
			// }
			se.getTransaction().begin();
			boolean insert = false;
			UserComplaint userComplaint = new UserComplaint();
			Query q = se.createQuery("from Driver where apiToken=:api");
			q.setParameter("api", complaint.getApiToken());
			Driver driver=(Driver) q.getSingleResult();
			List<User> use = q.getResultList();
			//Driver driver = se.find(User.class, driver.getId());
			UserTrip userTrip = se.find(UserTrip.class, complaint.getTripId());
			UserComplaintType userComplaintType = se.find(UserComplaintType.class, complaint.getComplaintTypeId());
			userComplaint.setTrip_id(userTrip);
     		//userComplaint.setDriver_id(driver);
			userComplaint.setComplaint_type(userComplaintType);
			userComplaint.setType("driver");
			userComplaint.setActive(false);
			se.persist(userComplaint);
			List<MultipartFile> image = new ArrayList<>();
			image = complaint.getImage();
			if (image == null) {
				image = new ArrayList<>();
			}
			List<String> ret = new ArrayList<>();
			for (MultipartFile fl : image) {
				UserComplaintImage complaintImage = new UserComplaintImage();
//*				complaintImage.setPath(complaint.getComplaintDescription());
				complaintImage.setUser_complaint(userComplaint);
				complaintImage.setImage(fileUploader.uploadFile(fl));
				se.persist(complaintImage);
			}
			// se.getTransaction().commit();
			// for (String str : image) {
			// complaintImage.setImage(str);
			// }

			// se.persist(userComplaint);
			// se.persist(complaintImage);
			se.getTransaction().commit();
			insert = true;
			if (insert) {
				// return Utility.constructJSONGetHelp("done", null, null);
				map.put("status", "done");
				return map;
			} else {
				// return Utility.constructJSONGetHelp("Error occcurred please
				// try again", null, null);
				map.put("status", "Error occcurred please try again");
				return map;
			}
		} catch (Exception e) {
			se.getTransaction().rollback();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(bos));
			return Utility.constructJSON(bos.toString(), null, null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}

	}

	@RequestMapping("/driverComplaintTypes")
	public @ResponseBody Map<String, Object> driverComplaintTypes() {
		EntityManager se = entityManagerFactory.createEntityManager();
		Map<String, Object> map2 = new HashMap<>();
		try {

			boolean insert = false;

			se.getTransaction().begin();
			Query q = se.createQuery("from UserComplaintType where type='driver'");
			List<UserComplaintType> list = new ArrayList<>();
			insert = true;
			list = q.getResultList();
			List<Map<String, Object>> ret = new ArrayList<>();
			for (UserComplaintType uct : list) {
				Map<String, Object> map = new HashMap<>();
				map.put("id", uct.getId());
				map.put("complaint title", uct.getName());
				ret.add(map);
			}
			if (insert) {
				// return Utility.constructJSONGetComplaints("done", ret);
				map2.put("status", "done");
				map2.put("complaints", ret);
				return map2;
			}

			else {
				// return Utility.constructJSONGetComplaints("Error occurred
				// please try again", null);
				map2.put("status", "Error occurred please try again");
				return map2;
			}
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e; // return
						// Utility.constructJSONGetComplaints(e.getStackTrace().toString(),
						// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}

	}

	@RequestMapping("/changeStatus")
	public @ResponseBody Map<String, Object> changeStatus(Driver driver,
			@RequestBody(required = false) Driver driverBody) {
		EntityManager se = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		try {
			if (driverBody != null) {
				driver = driverBody;
			}
			boolean insert = false;
			se.getTransaction().begin();
			Query q = se
					.createQuery("select car from DriverCar c left join c.car_id car where" + " c.driver_id.apiToken=:Did");
			q.setParameter("Did", driver.getApiToken());
			Car car = (Car) q.getSingleResult();

			// Car currentCar=se.find(Car.class, car.getId());
			if (car.isOnLine()) {
				car.setOnLine(false);
				se.persist(car);
				se.getTransaction().commit();
				map.put("status", "done not Active");
				return map;
				// return Utility.constructJSON("done not Active", null, null);

			} else {
				car.setOnLine(true);
				se.persist(car);
				se.getTransaction().commit();
				// return Utility.constructJSON("done Active", null, null);
				map.put("status", "done Active");
				return map;
			}
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e; // return
						// Utility.constructJSONGetComplaints(e.getStackTrace().toString(),
						// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}

	}

	@RequestMapping("/getCarInfo")
	public @ResponseBody Map<String, Object> getCarInfo(Driver driver,
			@RequestBody(required = false) Driver driverBody) {
		EntityManager se = entityManagerFactory.createEntityManager();

		try {
			if (driverBody != null) {
				driver = driverBody;
			}

			se.getTransaction().begin();
			Query q = se
					.createQuery("select car from DriverCar c left join c.car_id car where" + " c.driver_id.apiToken=:Did");
			q.setParameter("Did", driver.getApiToken());
			Car car = (Car) q.getSingleResult();
			Map<String, Object> map = new HashMap<>();
			if (car != null) {

				map.put("status", "done");
				map.put("carNumber", car.getCarNumber());
				map.put("model", car.getModel());
				map.put("manufacturer", car.getManufacturer());
				map.put("licenseNumber", car.getLicenseNumber());
				map.put("carType", car.getCarType());
				
				return map;
			} else {
				map.put("error", "threre no car");
				return map;
			}
		} catch (Exception e) {
//			se.getTransaction().rollback();
//			throw e; 
			Map<String,Object>map1=new HashMap<>();
			map1.put("error", "threre no car");
			return map1;
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}
		
	}
	
	@RequestMapping("/driverAddOrUpdateRate")
	public @ResponseBody Map<String, Object> driverAddOrUpdateRate(AddOrUpdateRate addOrUpdateRate,
			@RequestBody(required = false) AddOrUpdateRate addOrUpdateRateBody) {
		Map<String, Object> map = new HashMap<>();
		EntityManager se = entityManagerFactory.createEntityManager();
		try {
			if (addOrUpdateRateBody != null) {
				addOrUpdateRate = addOrUpdateRateBody;
			}
			boolean update = false;
			se.getTransaction().begin();
			
			
			UserTrip userTrip = se.find(UserTrip.class, addOrUpdateRate.getTripId());

			if (userTrip == null) {
				// return Utility.constructJSON("Trip-not-found", null, null);
				map.put("status", "Trip-not-found");
				return map;
			}
			
			
            Rates rate=new Rates();
            rate .setDriverRate(addOrUpdateRate.getRate());
            rate.setDriverComment(addOrUpdateRate.getComment());
            rate.setTrip_id(userTrip);
            se.persist(rate);
            map.put("status","done");
      		return map;
		} catch (Exception e) {
			se.getTransaction().rollback();
			throw e;// return
					// Utility.constructJSON(e.getStackTrace().toString(), null,
					// null);
		} finally {
			if (se.getTransaction().isActive()) {
				se.getTransaction().commit();
			}
			se.close();
		}

	}
	
		@RequestMapping("/hash")
		public @ResponseBody String hash() {
			//EntityManager se = entityManagerFactory.createEntityManager();
			System.out.println(DefaultPasswordHasher.getInstance().hashPassword("123456"));
			return DefaultPasswordHasher.getInstance().hashPassword("123456");
	}

}
