package com.magdsoft.CarGo.ws;

public interface LocationAware extends Comparable<LocationAware> {
	public Integer getId();
	public LatLngBearing getLocation();
	public void setLocation(LatLngBearing value);

	
	public int hashCode();
	public boolean equals(Object obj);

}
