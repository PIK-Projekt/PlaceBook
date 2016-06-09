package com.placebook.base

import com.placebook.PlacebookApplication
import com.placebook.places.Place
import com.placebook.users.Person
import org.springframework.boot.test.SpringApplicationConfiguration
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification


@SpringApplicationConfiguration(classes = PlacebookApplication)
@Ignore
abstract class IntegrationSpec extends Specification {

    @Shared
    protected ArrayList<Person> persistedUsers = []
    @Shared
    protected ArrayList<Place> persistedPlaces = []

    void setup() {
        usersArePresent()
        placesArePresent()
    }

    protected usersArePresent() {
        persistedUsers = TestData.users
    }

    protected placesArePresent() {
        persistedPlaces = TestData.places
    }

}
