package com.placebook.users;

import java.util.*;

import com.placebook.places.Place;
import com.placebook.places.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class PersonController {

    private PersonRepository personRepository;

    @Autowired
    PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @RequestMapping(value="/friends/{fbId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Person> getFriendsByUserFbId(@PathVariable("fbId") String fbId) {
        Person person = personRepository.findByFbId(fbId);
        List<String> friendsFbIds = person.getFriendsFbIds();
        List<Person> friends = new ArrayList<>();
        for (String friendFbId : friendsFbIds) {
            friends.add(personRepository.findByFbId(friendFbId));
        }
        return friends;
    }


    public String getMongoIdByFbId(String userId){
        Person person = personRepository.findByFbId(userId);
        if(person == null)
            return null;
        else
            return person.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Person findPerson(@PathVariable String id) {
        return personRepository.findOne(id);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Person addPerson(@RequestBody Person person) {
        return personRepository.save(person);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void updatePerson(@RequestBody Person person) {
        personRepository.save(person);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void removePerson(@PathVariable String id) {
        personRepository.delete(id);
    }

}