package com.oauth2.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	private static final String SERVER_RESOURCE_ID = "oauth2-server";
	private static InMemoryTokenStore tokenStore = new InMemoryTokenStore();
	
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("client-id").secret("client-secret").authorizedGrantTypes("authorization_code")
            .scopes("read").resourceIds(SERVER_RESOURCE_ID).authorities("CLIENT");
    }
    
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore).resourceId(SERVER_RESOURCE_ID);
    }
    
    @Override 
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception { 
        oauthServer.checkTokenAccess("permitAll()"); 
    }
}