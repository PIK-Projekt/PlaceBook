package com.placebook.acceptance

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


class PersonSpec extends MvcIntegrationSpec {
    @Autowired
    PersonRepository personRepository

    def "we can control the database with users"() {
        given: "there is a person in database"
        Person user = persistedUsers[0]
        Person userToUpdate = persistedUsers[1]

        when: "adding a person"
        ResultActions resultActions = addUser(user)

        then: "we get HTTP status 200 + id"
        resultActions.
                andExpect(status().isOk()).
                andExpect(jsonPath('id').value(any(String)));

        when: "we search for the person"
        String userID = getUserID(resultActions)
        resultActions = mockMvc.perform(get("/user/$userID"))

        then: "we get HTTP status 200 + the person we wanted"
        expectProperUser(userID, user, resultActions)

        when: "we update the person"
        resultActions = updateUser(userID, userToUpdate )

        then: "we get HTTP status 200 + it's the person we updated"
        expectProperUser(userID, userToUpdate, resultActions)

        when: "we remove the person"
        resultActions = mockMvc.perform(delete("/user/$userID"))

        then: "we get HTTP status 200"
        resultActions.
                andExpect(status().isOk())
    }


    private ResultActions addUser(Person person) {
        ResultActions resultActions = mockMvc.perform(post('/user').
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).
                content(new JSONObject().put("id", person.getId()).put("fbId", person.getFbId()).put("name", person.getName())
                        .put("email", person.getEmail()).put("phone", person.getPhone())
                        .put("friendsFbIds", person.getFriendsFbIds()).put("places", person.getPlaces()).toString()))
        return resultActions
    }

    private ResultActions updateUser(String personID, Person userToUpdate ) {
        ResultActions resultActions = mockMvc.perform(post('/user').
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).
                content(new JSONObject().put("id", personID ).put("fbId", userToUpdate.getFbId()).put("name", userToUpdate.getName())
                        .put("email", userToUpdate.getEmail()).put("phone", userToUpdate.getPhone())
                        .put("friendsFbIds", userToUpdate.getFriendsFbIds()).put("places", userToUpdate.getPlaces()).toString()))
        return resultActions
    }

    private String getUserID(ResultActions resultActions) {
        def jsonResult = new JsonSlurper().parseText(resultActions.andReturn().response.getContentAsString())
        String userId = jsonResult.id
        return userId
    }

    private void expectProperUser(String personID, Person person, ResultActions resultActions) {
        resultActions.
                andExpect(status().isOk()).
                andExpect(jsonPath('id').value(equalTo(personID))).
                andExpect(jsonPath('fbId').value(equalTo(person.getFbId()))).
                andExpect(jsonPath('name').value(equalTo(person.getName()))).
                andExpect(jsonPath('email').value(equalTo(person.getEmail()))).
                andExpect(jsonPath('phone').value(equalTo(person.getPhone()))).
                andExpect(jsonPath('friendsFbIds').value(equalTo(person.getFriendsFbIds()))).
                andExpect(jsonPath('places').value(equalTo(person.getPlaces())));
    }

}