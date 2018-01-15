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

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.convert.MappingSolrConverter;
import org.springframework.data.solr.core.mapping.SimpleSolrMappingContext;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.server.SolrClientFactory;
import org.springframework.data.solr.server.support.MulticoreSolrClientFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration for multicore Solr server.
 *
 * @author Maksim Filkov
 */
@Profile({ "prod" })
@Configuration
@EnableSolrRepositories(value = "org.lexikos.app.dao", multicoreSupport = true)
public class SolrConfig {

   static Logger log = Logger.getLogger(SolrConfig.class.getName());

   @Value("#{'${search.translation.directions}'.split(',')}")
   private List<String> translationDirections;

   @Value("${solr.server.url}")
   private String solrUrl;

   @Value(value = "#{environment.SOLR_PORT_8983_TCP_ADDR}")
   private String solrHost;

   @Value("#{environment.SOLR_PORT_8983_TCP_PORT}")
   private String solrPort;

   @Bean
   public SolrClientFactory solrClientFactory() {
      final String url = MessageFormat.format(solrUrl, solrHost, solrPort);
      log.debug("Solr URL: " + url);
      return new MulticoreSolrClientFactory(new HttpSolrClient(url));
   }

   /**
    * List of Solr cores for each translation direction (enru, deru, etc.).
    * @return List of Solr cores.
    */
   @Bean
   public List<SolrTemplate> solrTemplates() {
      final List<SolrTemplate> templates = new ArrayList<>();
      translationDirections.forEach(translationDirection -> {
         final SolrTemplate template = new SolrTemplate(solrClientFactory());
         template.setSolrCore(translationDirection);
         template.setSolrConverter(new MappingSolrConverter(new SimpleSolrMappingContext()));
         template.setMappingContext(new SimpleSolrMappingContext());
         templates.add(template);
      });
      return templates;
   }

}
