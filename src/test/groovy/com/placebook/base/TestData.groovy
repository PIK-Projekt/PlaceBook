package com.placebook.base

import com.placebook.places.Place
import com.placebook.users.Person

class TestData {

    static  ArrayList<String> IdsOfFriend = new ArrayList<String>(){
    {
        add( "232323768678" );
        add( "232322132344" );
        add( "948098054222" );
    }};

    static Person user0 = new Person( "1", "12324231423", "Krzysztof Rawa" , "interia@interia.pl", "644504699", IdsOfFriend, places )
    static Person user1 = new Person( "2", "65865765677", "Anna Wieszchołek" , "imaguska@interia.pl", "504989753", IdsOfFriend, places )
    static Person user2 = new Person( "3", "22457876767", "Michał Mańka" , "igimbazunia@interia.pl", "5564322558", IdsOfFriend, places )
    static ArrayList<Person> users = [user0, user1, user2]

    static Place place0 = new Place( "1",  "12343243243",  "Warka",  "Warka",  "Poland", "51.1212", "52.2131231")
    static Place place1 = new Place( "2",  "54745334565",  "New York",  "New York",  "United States", "31.1212",  "57.2131231")
    static Place place2 = new Place( "3",  "76798076455", "Serock",  "Warka",  "Poland", "51.1212",  "52.2345231")
    static ArrayList<Place> places = [place0, place1, place2]

}
