package com.youber.cmput301f16t15.youber;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Reem on 2016-10-13.
 */
public class RequestCollection extends HashMap<UUID,Request>{

    private HashMap<UUID, Request> requestCollection;

    public RequestCollection()
    {
        this.requestCollection = new HashMap<UUID, Request>();
    }

    public void driverAccept() {

    }

    public Request getByGeolocation(GeoLocation geoLocation) {
        return null;
    }

    public RequestCollection getByKeyword(String s) {
        return null;
    }

    public boolean contains(RequestCollection request) {
        return false;
    }

    public RequestCollection getAcceptedAcceptedRequests() {
        return null;
    }

    public RequestCollection getRequestsForRiders(Rider currentUser) {
        return null;
    }

    public RequestCollection getAcceptedRequestsForDrivers(Driver currentUser) {
        return null;
    }

    public void add(Request request1) {

    }
}