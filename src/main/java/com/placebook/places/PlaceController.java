package com.placebook.places;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/places")
public class PlaceController {

    private PlaceRepository placeRepository;

    @Autowired
    PlaceController(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @RequestMapping(value="/fb/{fbId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Place getPlaceByFbId(@PathVariable("fbId") String fbId) {
        Place place = placeRepository.findByFbId(fbId);
        return place;
    }

    public String GetMongoIdByFbId(String userId){
        Place  place = placeRepository.findByFbId(userId);
        if(place == null)
            return null;
        else
            return place.getId();
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Place findPlace(@PathVariable String id) {
        return placeRepository.findOne(id);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Place addPlace(@RequestBody Place place) {
        return placeRepository.save(place);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void updatePlace(@RequestBody Place place) {
        placeRepository.save(place);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void removePlace(@PathVariable String id) {
        placeRepository.delete(id);
    }

}





