/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.prometheus.collector;

import org.apache.solr.prometheus.exporter.MetricsQuery;
import org.apache.solr.prometheus.scraper.SolrScraper;

public class PingCollector implements MetricCollector {

  private final SolrScraper solrScraper;
  private final MetricsQuery metricsQuery;

  public PingCollector(SolrScraper solrScraper, MetricsQuery metricsQuery) {
    this.solrScraper = solrScraper;
    this.metricsQuery = metricsQuery;
  }

  @Override
  public MetricSamples collect() throws Exception {
    MetricSamples results = new MetricSamples();

    solrScraper
        .pingAllCollections(metricsQuery)
        .forEach((collection, metrics) -> results.addAll(metrics));

    solrScraper
        .pingAllCores(metricsQuery)
        .forEach((collection, metrics) -> results.addAll(metrics));

    return results;
  }
}
