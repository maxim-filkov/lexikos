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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.validation.Validator;
import java.util.List;

/**
 * Java configuration equivalent for web-context.xml that is intended for web service configuration.
 *
 * @author Maksim Filkov
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

   private static final String MESSAGE_SOURCE_BASE_NAME = "locale/messages";

   @Value("${server.image.upload.dir}")
   private String serverImageUploadDir;

   @Value("${server.image.upload.context}")
   private String serverImageUploadContext;

   @Override
   public void addResourceHandlers(final ResourceHandlerRegistry registry) {
      registry
            .addResourceHandler(serverImageUploadContext + "/**")
            .addResourceLocations("file:" + serverImageUploadDir);
   }

   /**
    * Equivalent to <mvc:argument-resolvers>.
    */
   @Override
   public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
      argumentResolvers.add(pageable());
   }

   /**
    * Creates PageableHandlerMethodArgumentResolver bean needed for pagination support through HTTP parameters.
    *
    * @return PageableHandlerMethodArgumentResolver bean.
    */
   @Bean
   public PageableHandlerMethodArgumentResolver pageable() {
      return new PageableHandlerMethodArgumentResolver();
   }

   /**
    * Creates MessageSource bean needed for localization.
    *
    * @return MessageSource bean.
    */
   @Bean
   public MessageSource messageSource() {
      final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

      messageSource.setBasename(MESSAGE_SOURCE_BASE_NAME);
      messageSource.setUseCodeAsDefaultMessage(true);

      return messageSource;
   }

   /**
    * Creates RestTemplate bean needed for communication with 3rd party services through REST.
    *
    * @return RestTemplate bean
    */
   @Bean
   public RestTemplate restTemplate() {
      return new RestTemplate();
   }

   @Bean
   public Validator validator() {
      return new LocalValidatorFactoryBean();
   }

   @Bean
   public MethodValidationPostProcessor methodValidationPostProcessor() {
      final MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
      methodValidationPostProcessor.setValidator(validator());
      return methodValidationPostProcessor;
   }

   @Override
   public void addViewControllers(final ViewControllerRegistry registry) {
      registry.addViewController("/login").setViewName("login");
   }

   @Bean
   public InternalResourceViewResolver viewResolver() {
      final InternalResourceViewResolver resolver = new InternalResourceViewResolver();
      resolver.setPrefix("/WEB-INF/jsp/");
      resolver.setSuffix(".jsp");
      return resolver;
   }

}
