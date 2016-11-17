package com.youber.cmput301f16t15.youber.requests;

import com.youber.cmput301f16t15.youber.users.Driver;
import com.youber.cmput301f16t15.youber.misc.GeoLocation;
import com.youber.cmput301f16t15.youber.exceptions.InvalidRequestException;
import com.youber.cmput301f16t15.youber.misc.Payment;
import com.youber.cmput301f16t15.youber.users.Rider;

import java.io.Serializable;
import java.util.UUID;

import io.searchbox.annotations.JestId;

/**
 * Created by Reem on 2016-10-13.
 *
 * <p>
 *     The request class handles new or preexisting requests from a user.
 * </p>
 *
 * @see Rider
 * @see Driver
 * @see com.youber.cmput301f16t15.youber.gui.RequestActivity
 * @see RequestCollection
 */
public class Request implements Serializable {

    @JestId
    private UUID uuID;
    /**
     * The Start location.
     */
    double [] startLocation =new double[2];
    double [] endLocation= new double[2];

    private boolean status = true; //open is true
    private String description = "";
    private String routeDistLen = "";

    private Payment payment = new Payment(0);
    private Driver driver;

    public Driver getDriver() {
        return driver;
    }

    /**
     * The enum Request status.
     * Opened request status
     * Accepted by driver(s) status
     * Confirmed by rider status
     * Finalized by driver status
     * Payed/Completed
     */
    public enum RequestStatus {
        opened,closed, acceptedByDrivers, riderSelectedDriver, paid
    }

    private RequestStatus currentStatus;

    /**
     * Instantiates a new Request.
     *
     * @param location1 the starting location
     * @param location2 the end location
     */
    public Request(GeoLocation location1, GeoLocation location2) {
        if (location1.equals(location2)) throw new RuntimeException(new InvalidRequestException());

        startLocation[0] = location1.getLat();
        startLocation[1] = location1.getLon();
        endLocation[0] = location2.getLat();
        endLocation[1] = location2.getLon();

        uuID = UUID.randomUUID();
        currentStatus = RequestStatus.opened;
    }

    /**
     * Instantiates a new Request.
     *
     * @param geoLocation1 the geo location 1
     * @param geoLocation2 the geo location 2
     * @param s            the string of keywords
     */
    public Request(GeoLocation geoLocation1, GeoLocation geoLocation2, String s) {
        uuID = UUID.randomUUID();
        description = s;
    }

    @Override
    public String toString() {
        String currentStat = currentStatus.toString(); // TODO FIX THIS
        GeoLocation start = new GeoLocation(startLocation[0], startLocation[1]);
        String startStr = start.toString();
        GeoLocation end = new GeoLocation(endLocation[0], endLocation[1]);
        String endStr = end.toString();
        return currentStat + "\nStart: " + startStr + "\nEnd: " + endStr;
    }

    public Rider addRider(Rider rider) {
        return null;
    }

    public UUID getUUID() {
        return this.uuID;
    }

    /**
     * Gets start location.
     * @return the start location
     */
    public GeoLocation getStartLocation() {
        return new GeoLocation(startLocation[0],startLocation[1]);
    }

    /**
     * Gets end location.
     * @return the end location
     */
    public GeoLocation getEndLocation() {

        return new GeoLocation(endLocation[0],endLocation[1]);
    }

    /**
     * Close.
     */
//    public void close() {
//    }

    /**
     * Is closed boolean.
     *
     * @return the boolean
     */
    public boolean isClosed() {
        if (status) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Accept.
     */
//    public void accept() {
//        this.accepted = true;
//    }

    /**
     * Is accepted boolean.
     * @return the boolean
     */
//    public boolean isAccepted() {
//        return this.accepted;
//    }

    /**
     * Complete.
     */
    public void complete() {
    }

    /**
     * Is complete boolean.
     * @return the boolean
     */
    public boolean isComplete() {
        return false;
    }

    /**
     * Gets cost settles upon by the rider and driver.
     * @return the cost
     */
    public Double getCost() {
        return this.payment.getActualCost();
    }

    public void setPayment(double payAmt) {
        this.payment = new Payment(payAmt);
    }


    /**
     * Add driver driver.
     *
     * @param driver the driver
     * @return the driver
     */
    public Driver addDriver(Driver driver) {
        return null;
    }

    /**
     * Confirm by driver.
     * @param driver the driver
     */
    public void confirmByDriver(Driver driver) {
        //this.confirmationStage = 1;
        currentStatus = RequestStatus.acceptedByDrivers;
    }

    /**
     * Finalize by driver.
     */
//    public void finalizeByDriver() {
//        this.confirmationStage = 3;
//        currentStatus = RequestStatus.finalizedByDriver;
//    }

    /**
     * Is confirmed boolean.
     *
     * @return the boolean
     */
    public boolean isConfirmed() {
        return false;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String s) {
        description = s;
    }

    public String getRouteDistLen() {
        return routeDistLen;
    }

    public void setRouteDistLen(String str) {
        this.routeDistLen = str;
    }


    public RequestStatus getCurrentStatus() {
        return currentStatus;
    }
    //opened, acceptedByDrivers, riderSelectedDriver, paid
    public void setClosed(){
        this.currentStatus=RequestStatus.closed;
    }
    public void setAcceptedByDrivers(){
        this.currentStatus=RequestStatus.acceptedByDrivers;
    }

    public void setRiderSelectedDriver(){
        this.currentStatus=RequestStatus.riderSelectedDriver;
    }

    public void setPaid(){
        this.currentStatus=RequestStatus.paid;
    }


}
