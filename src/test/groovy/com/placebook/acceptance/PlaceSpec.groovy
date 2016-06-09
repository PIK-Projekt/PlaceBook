package com.placebook.acceptance

import com.placebook.places.Place
import com.placebook.users.Person
import com.placebook.users.PersonRepository
import com.placebook.base.MvcIntegrationSpec
import groovy.json.JsonSlurper
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActions

import static org.hamcrest.CoreMatchers.any
import static org.hamcrest.CoreMatchers.equalTo
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class PlaceSpec extends MvcIntegrationSpec {
    @Autowired
    PersonRepository placeRepository

    def "we can control the database with places"() {
        given: "there is a place in database"
        Place place = persistedPlaces[0]
        Place placeToUpdate = persistedPlaces[1]

        when: "adding a place"
        ResultActions resultActions = addPlace(place)

        then: "we get HTTP status 200 + id"
        resultActions.
                andExpect(status().isOk()).
                andExpect(jsonPath('id').value(any(String)));

        when: "we search for the place"
        String placeID = getPlaceID(resultActions)
        resultActions = mockMvc.perform(get("/places/$placeID"))

        then: "we get HTTP status 200 + the place we wanted"
        expectProperPlace(placeID, place, resultActions)

        when: "we update the place"
        resultActions = updatePlace(placeID, placeToUpdate)

        then: "we get HTTP status 200 + it's the place we updated"
        expectProperPlace(placeID, placeToUpdate, resultActions)

        when: "we search for the place by FbId"
        String placeFbID = placeToUpdate.getFbId()
        placeID = place.getId()
        resultActions = mockMvc.perform(get("/places/fb/$placeFbID"))

        then: "we get HTTP status 200 + the place we wanted"
        expectProperPlace(placeID, placeToUpdate, resultActions)

        when: "we remove the place"
        resultActions = mockMvc.perform(delete("/places/$placeID"))

        then: "we get HTTP status 200"
        resultActions.
                andExpect(status().isOk())

    }


    private ResultActions addPlace(Place place) {
        ResultActions resultActions = mockMvc.perform(post('/places').
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).
                content(new JSONObject().put("id", place.getId()).put("fbId", place.getFbId())
                        .put("name", place.getName()).put("city", place.getCity())
                        .put("country", place.getCountry()).put("latitude", place.getLatitude())
                        .put("longitude", place.getLongitude()).toString()))
        return resultActions
    }

    private ResultActions updatePlace(String placeID, Place placeToUpdate ) {
        ResultActions resultActions = mockMvc.perform(post('/places').
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).
                content(new JSONObject().put("id", placeID).put("fbId", placeToUpdate.getFbId())
                        .put("name", placeToUpdate.getName()).put("city", placeToUpdate.getCity())
                        .put("country", placeToUpdate.getCountry()).put("latitude", placeToUpdate.getLatitude())
                        .put("longitude", placeToUpdate.getLongitude()).toString()))
        return resultActions
    }

    private String getPlaceID(ResultActions resultActions) {
        def jsonResult = new JsonSlurper().parseText(resultActions.andReturn().response.getContentAsString())
        String placeId = jsonResult.id
        return placeId
    }

    private void expectProperPlace(String placeID, Place placeToUpdate, ResultActions resultActions) {
        resultActions.
                andExpect(status().isOk()).
                andExpect(jsonPath('id').value(equalTo(placeID))).
                andExpect(jsonPath('fbId').value(equalTo(placeToUpdate.getFbId()))).
                andExpect(jsonPath('name').value(equalTo(placeToUpdate.getName()))).
                andExpect(jsonPath('city').value(equalTo(placeToUpdate.getCity()))).
                andExpect(jsonPath('country').value(equalTo(placeToUpdate.getCountry()))).
                andExpect(jsonPath('latitude').value(equalTo(placeToUpdate.getLatitude()))).
                andExpect(jsonPath('longitude').value(equalTo(placeToUpdate.getLongitude())));
    }

}
