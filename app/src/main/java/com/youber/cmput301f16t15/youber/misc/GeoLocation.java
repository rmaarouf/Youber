package com.youber.cmput301f16t15.youber.misc;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * Used http://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
 * Created by Reem on 2016-10-13.
 *
 * <p>
 *     This class specifies locations in coordinate form for requests
 * </p>
 * @see com.youber.cmput301f16t15.youber.gui.MainActivity
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

    /**
     * Display the coordinates as strings
     *
     */
    @Override
    public String toString() {
        return "Lat " + Double.toString(lat) + ", " + "Lon " + Double.toString(lon);
    }

    public String getAddress(Context c) {
        Geocoder geocoder = new Geocoder(c, Locale.getDefault());
        String addr = "";

        try {
            List<Address> address = geocoder.getFromLocation(lat, lon, 1);

            for (int i = 0; i < address.get(0).getMaxAddressLineIndex(); i++) {
                addr += address.get(0).getAddressLine(i) + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return addr;
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
