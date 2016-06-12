package com.placebook.acceptance

import com.placebook.base.FacebookSpec
import org.springframework.social.facebook.api.FamilyMember
import org.springframework.social.facebook.api.FriendOperations
import org.springframework.social.facebook.api.PagedList
import org.springframework.social.facebook.api.TestUser
import org.springframework.social.facebook.api.User
import org.springframework.social.facebook.api.impl.FacebookTemplate


class FriendOperationsSpec extends FacebookSpec {

    def "we can control the database with users"() {
        given: "there are  testUsers created"
        TestUser testUser1 = createTestUser(true, "manage_friendlists", "Emanuel Szengolat");
        TestUser testUser2 = createTestUser(true, "manage_friendlists", "Damian Lorenc");

        "we have permission to perform facebook operation "
        FriendOperations friendOps1 = new FacebookTemplate(testUser1.getAccessToken()).friendOperations();
        FriendOperations friendOps2 = new FacebookTemplate(testUser2.getAccessToken()).friendOperations();

        when: "we exchange friends confirmation"
        clientFacebook.testUserOperations().sendConfirmFriends(testUser1, testUser2);
        clientFacebook.testUserOperations().sendConfirmFriends(testUser2, testUser1);

        and: "we get friendIds of the one of them "
        List<String> testUser1FriendIds = friendOps1.getFriendIds();

        then: "we can check if he has one friend and is it correct user "
        assert testUser1FriendIds.size() == 1
        assert testUser1FriendIds.get(0) == testUser2.getId()

        when: "we get friends of one of them"
        PagedList<Reference> testUser1Friends = friendOps1.getFriends()

        then: " we can check if confirmation went fine"
        assert  testUser1Friends.size() == 1
        assert testUser1Friends.get(0).getId() == testUser2.getId()

        when: "we get friend profiles one of them"
        PagedList<User> testUser1FriendProfiles = friendOps1.getFriendProfiles()

        then: "we can check if it profiles works fine too"
        assert  testUser1FriendProfiles.size() == 1
        assert  testUser2.getId() == testUser1FriendProfiles.get(0).getId()

        when: "we get friendIds of the second "
        List<String> testUser2FriendIds = friendOps2.getFriendIds();

        then: "we check if this is first person "
        assert  friendOps2.getFriendIds().size() == 1
        assert  testUser2FriendIds.get(0) == testUser1.getId()

        when: "we get friends of the second"
        List<Reference> testUser2Friends = friendOps2.getFriends()

        then: " we can check if confirmation went fine"
        assert  testUser2Friends.size() == 1
        assert  testUser1.getId() == testUser2Friends.get(0).getId()

        when: "we get friend profiles one of them"
        PagedList<User> testUser2FriendProfiles = friendOps2.getFriendProfiles();

        then: "we can check if it profiles works fine too"
        assert  testUser2FriendProfiles.size() == 1
        assert  testUser1.getId() ==  testUser2FriendProfiles.get(0).getId()

        when: "we get a friend's family members "
        PagedList<FamilyMember> family = friendOps1.getFamily()

        then: "we can check how many family members it found"
        assert family.size() == 0

    }
}