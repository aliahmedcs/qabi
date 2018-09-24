package com.magdsoft.CarGo.ws.sockets;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import com.magdsoft.CarGo.ws.LocationAndSessionAware;

/**
 * DAO data structure (Add, Update, Delete) for the in-memory location aware objects database
 * Indexes the added (or updated) data by id and by location
 */
public class LocationAndSessionAwareObjectDAO<T extends LocationAndSessionAware> extends LocationAwareObjectDAO<T> {

	private final Map<String, T> objectsBySession = new HashMap<>();

	@Override
	public synchronized void addObject(T object) {
		super.addObject(object);
		objectsBySession.put(object.getSession().getId(), object);
	}

	@Override
	public void deleteObject(int id) {
		objectsBySession.remove(this.getById(id).getSession().getId());
		super.deleteObject(id);
	}

	public T getBySession(WebSocketSession session) {
		return objectsBySession.get(session.getId());
	}
	
	public void setSessionForObject(T object, WebSocketSession session) {
		WebSocketSession oldSession = (object.getSession());
		T obj = objectsBySession.get(oldSession.getId());

		synchronized(this) {
			objectsBySession.remove(oldSession.getId());
			object.setSession(session);
			objectsBySession.put(session.getId(), obj);
			if (null != object.getLocation()) {
				this.updateObjectLocation(object.getId(), object.getLocation().getLatitude(), object.getLocation().getLongitude(),
						object.getLocation().getBearing());
			}
		}
	}
}
