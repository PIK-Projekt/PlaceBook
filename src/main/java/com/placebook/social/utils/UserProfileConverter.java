package com.placebook.social.utils;


import com.placebook.places.Place;
import com.placebook.users.Person;
import org.springframework.social.facebook.api.PlaceTag;
import org.springframework.social.facebook.api.User;

import java.util.List;

public class UserProfileConverter {

    public static Person ConvertUser(User usr, List<Place> placeList, List<String> friendList, String mongoId) {

        Person person = new Person(mongoId, usr.getId(), usr.getFirstName() + " " + usr.getLastName()
                , usr.getEmail(), null, friendList, placeList);
        return person;

    }


    public static Place ConvertPlace(PlaceTag placeTag, String mongoPlaceId) {
        Place place = new Place(mongoPlaceId, placeTag.getPlace().getId(), placeTag.getPlace().getName()
                , placeTag.getPlace().getLocation().getCity(), placeTag.getPlace().getLocation().getCountry()
                , Double.toString(placeTag.getPlace().getLocation().getLatitude())
                , Double.toString(placeTag.getPlace().getLocation().getLongitude()));
        return place;

    }
}