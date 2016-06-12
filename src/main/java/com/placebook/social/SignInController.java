package com.placebook.social;

import com.placebook.places.*;
import com.placebook.places.Place;
import com.placebook.social.utils.UserProfileConverter;
import com.placebook.users.Person;
import com.placebook.users.PersonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.social.connect.*;
import org.springframework.social.facebook.api.*;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/signin")
public class SignInController {

    private FacebookConnectionFactory facebookConnectionFactory;
    private UsersConnectionRepository usersConnectionRepository;
    private PersonController personController;
    private PlaceController placeController;

    @Autowired
    public SignInController(FacebookConnectionFactory facebookConnectionFactory, UsersConnectionRepository usersConnectionRepository
            , PersonController personController, PlaceController placeController) {
        this.facebookConnectionFactory = facebookConnectionFactory;
        this.usersConnectionRepository = usersConnectionRepository;
        this.personController = personController;
        this.placeController = placeController;
    }


    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HttpStatus fb(@RequestBody String token) {

        AccessGrant accessGrant = new AccessGrant(token);
        Connection<Facebook> connection = facebookConnectionFactory.createConnection(accessGrant);
        UserProfile userProfile = connection.fetchUserProfile();
        usersConnectionRepository.createConnectionRepository(userProfile.getId());

        Facebook facebook = connection.getApi();
        User user = facebook.userOperations().getUserProfile();
        List<PlaceTag> fbTaggedPlaces = facebook.userOperations().getTaggedPlaces();
        List<String> fbFriendsIds = facebook.friendOperations().getFriendIds();

        Person person;
        Place place;
        String mongoUserId = personController.getMongoIdByFbId(user.getId());
        String mongoPlaceId;

        List<Place> listOfPlace = new ArrayList();
        for (PlaceTag placeTag : fbTaggedPlaces) {
            mongoPlaceId = placeController.GetMongoIdByFbId(placeTag.getPlace().getId());
            if (mongoPlaceId != null) {
                place = UserProfileConverter.ConvertPlace(placeTag, mongoPlaceId);
                placeController.updatePlace(place);
            } else {
                place = UserProfileConverter.ConvertPlace(placeTag, null);
                placeController.addPlace(place);
            }
            listOfPlace.add(place);
        }

        if (mongoUserId != null) {
            person = UserProfileConverter.ConvertUser(user, listOfPlace, fbFriendsIds, mongoUserId);
            personController.updatePerson(person);
        } else {
            person = UserProfileConverter.ConvertUser(user, listOfPlace, fbFriendsIds, null);
            personController.addPerson(person);
        }

        return HttpStatus.OK;
    }

}
