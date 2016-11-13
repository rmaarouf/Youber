package com.youber.cmput301f16t15.youber;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Used http://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
 * Created by Reem on 2016-10-13.
 */
public class GeoLocation implements Serializable, Parcelable {
    private double lat;
    private double lon;

    /**
     * Instantiates a new Geo location.
     */
    public GeoLocation() {

    }

    /**
     * Instantiates a new Geo location.
     *
     * @param lat the lat
     * @param lon the lon
     */
    public GeoLocation(double lat, double lon)
    {
        this.lat=lat;
        this.lon=lon;
    }

    /**
     * Gets lat.
     *
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * Gets lon.
     *
     * @return the lon
     */
    public double getLon() {
        return lon;
    }

    @Override
    public boolean equals(Object o){
        if(o==null){
            return false;
        }
        if(!GeoLocation.class.isAssignableFrom(o.getClass())){
            return false;
        }
        final GeoLocation otherGeoLocation= (GeoLocation) o;
        if(Math.abs(otherGeoLocation.getLat()-this.lat)<0.00001&&Math.abs(otherGeoLocation.getLon()-this.lon)<0.00001){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Lat " + Double.toString(lat) + ", " + "Lon " + Double.toString(lon);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(lat);
        parcel.writeDouble(lon);
    }


    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<GeoLocation> CREATOR = new Parcelable.Creator<GeoLocation>() {
        public GeoLocation createFromParcel(Parcel in) {
            return new GeoLocation(in);
        }

        public GeoLocation[] newArray(int size) {
            return new GeoLocation[size];
        }
    };

    private GeoLocation(Parcel in){
        lat=in.readDouble();
        lon=in.readDouble();
    }
}
