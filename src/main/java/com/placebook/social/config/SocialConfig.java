package com.placebook.social.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

@Configuration
public class SocialConfig {

    @Value("${facebook.appKey}")
    private String facebookClientId;

    @Value("${facebook.appSecret}")
    private String facebookClientSecret;

    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new FacebookConnectionFactory(facebookClientId,
                facebookClientSecret));
        return registry;
    }

    @Bean
    public UsersConnectionRepository usersConnectionRepository() {
        InMemoryUsersConnectionRepository repository = new InMemoryUsersConnectionRepository(connectionFactoryLocator());
        repository.setConnectionSignUp(new SimpleConnectionSignUp());
        return repository;
    }


    @Bean
    public FacebookConnectionFactory facebookConnectionFactory() {
        FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory(facebookClientId,
                facebookClientSecret);
        facebookConnectionFactory.setScope("public_profile,email");
        return facebookConnectionFactory;
    }


    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository() {
        User user = SecurityContext.getCurrentUser();
        return usersConnectionRepository().createConnectionRepository(user.getId());
    }


    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public Facebook facebook() {
        // Connection<Facebook> facebook =  connectionRepository().findPrimaryConnection(Facebook.class);
        // return facebook != null ? facebook.getApi() : new FacebookTemplate();
        return connectionRepository().getPrimaryConnection(Facebook.class).getApi();
    }


    @Bean
    public ProviderSignInController providerSignInController() {
        return new ProviderSignInController(connectionFactoryLocator(), usersConnectionRepository(),
                new SimpleSignInAdapter());
    }


}
