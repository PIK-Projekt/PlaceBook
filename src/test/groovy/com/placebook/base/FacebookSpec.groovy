package com.placebook.base

import com.placebook.PlacebookApplication
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.social.facebook.api.TestUser
import org.springframework.social.facebook.api.impl.FacebookTemplate
import org.springframework.social.facebook.connect.FacebookServiceProvider
import org.springframework.social.oauth2.AccessGrant
import org.springframework.social.oauth2.OAuth2Operations
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

@SpringApplicationConfiguration(classes = PlacebookApplication)
@Ignore
public abstract class FacebookSpec extends Specification {

    static final String APP_ID = "############";
    static final String APP_SECRET = "############################";
    @Shared
    private List<String> testUserIds = new ArrayList<String>();
    @Shared
    protected FacebookTemplate clientFacebook;

   def setupSpec() {
        OAuth2Operations oauth = new FacebookServiceProvider(APP_ID, APP_SECRET, null).getOAuthOperations();
        AccessGrant clientGrant = oauth.authenticateClient();
        clientFacebook = new FacebookTemplate(clientGrant.getAccessToken(), "", APP_ID);
    }

    public TestUser createTestUser(boolean installed, String permissions, String name) {
        TestUser testUser = clientFacebook.testUserOperations().createTestUser(installed, permissions, name);
        testUserIds.add(testUser.getId());
        return testUser;
    }

    def cleanupSpec() {
        for (String testUserId : testUserIds) {
            clientFacebook.testUserOperations().deleteTestUser(testUserId);
        }
    }



}
