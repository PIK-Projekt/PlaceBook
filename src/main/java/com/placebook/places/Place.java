package com.placebook.places;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;


public class Place {
    @Id
    private String id;
    private String fbId;
    private String name;
    private String city;
    private String country;
    private String latitude;
    private String longitude;

    @JsonCreator
    public Place(@JsonProperty("id")String id, @JsonProperty("fbId") String fbId,
                 @JsonProperty("name")String name, @JsonProperty("city")String city, @JsonProperty("country") String country,
                 @JsonProperty("latitude") String latitude, @JsonProperty("longitude") String longitude) {
        this.id = id;
        this.fbId = fbId;
        this.name = name;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getFbId() {
        return fbId;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}