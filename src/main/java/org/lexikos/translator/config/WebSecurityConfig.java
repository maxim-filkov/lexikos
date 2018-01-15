/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lexikos.translator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author Maksim Filkov
 */
@Configuration
@EnableResourceServer
public class WebSecurityConfig extends ResourceServerConfigurerAdapter {

   private static final String RESOURCE_ID = "search";

   private final JwtAccessTokenConverter jwtAccessTokenConverter;

   @Autowired
   public WebSecurityConfig(JwtAccessTokenConverter jwtAccessTokenConverter) {
      this.jwtAccessTokenConverter = jwtAccessTokenConverter;
   }

   @Override
   public void configure(final ResourceServerSecurityConfigurer resources) {
      resources.resourceId(RESOURCE_ID).tokenStore(new JwtTokenStore(jwtAccessTokenConverter));
   }

   /**
    * Defines which application paths are secured.
    *
    * @param http HttpSecurity instance.
    * @throws Exception
    */
   @Override
   public void configure(final HttpSecurity http) throws Exception {
      http
            .csrf().disable()
            .authorizeRequests().antMatchers("/search/**").authenticated();
   }

}