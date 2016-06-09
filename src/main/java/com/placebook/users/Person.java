package com.placebook.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.placebook.places.Place;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

public class Person {
    @Id
    private String id;
    private String fbId;
    private String name;
    private String email;
    private String phone;
    private List<String> friendsFbIds;
    @DBRef
    private List<Place> places;

    @JsonCreator
    public Person(@JsonProperty("id")String id, @JsonProperty("fbId") String fbId,
                  @JsonProperty("name") String name, @JsonProperty("email") String email, @JsonProperty("phone") String phone,
                  @JsonProperty("friendsFbIds") List<String> friendsFbIds, @JsonProperty("places") List<Place> places) {
        this.id = id;
        this.fbId = fbId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.friendsFbIds = friendsFbIds;
        this.places = places;
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

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getFriendsFbIds() {
        return friendsFbIds;
    }

    public List<Place> getPlaces() {
        return places;
    }
}