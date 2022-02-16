package com.example.fa_mayurpatel_c0828800_android;

import android.os.Parcel;
import android.os.Parcelable;

public class DataBaseModel implements Parcelable {
    private int id;
    private String placeName;
    private String  lat;
    private String lng;

    public DataBaseModel(int id, String placeName, String lat, String lng) {
        this.id = id;
        this.placeName = placeName;
        this.lat = lat;
        this.lng = lng;
    }

    public DataBaseModel(){

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public static final String TABLE_NAME = "places";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_LAT + " TEXT,"
                    + COLUMN_LNG + " TEXT"
                    + ")";

    protected DataBaseModel(Parcel in) {
        id = in.readInt();
        placeName = in.readString();
        lat = in.readString();
        lng = in.readString();
    }

    public static final Creator<DataBaseModel> CREATOR = new Creator<DataBaseModel>() {
        @Override
        public DataBaseModel createFromParcel(Parcel in) {
            return new DataBaseModel(in);
        }

        @Override
        public DataBaseModel[] newArray(int size) {
            return new DataBaseModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(placeName);
        dest.writeString(lat);
        dest.writeString(lng);
    }
}