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

package org.apache.solr.cloud;

import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.ClusterProperties;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestClusterProperties extends SolrCloudTestCase {

  private ClusterProperties props;

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(1).configure();
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    props = new ClusterProperties(zkClient());
  }

  @Test
  public void testSetPluginClusterProperty() throws Exception {
    String propertyName = ClusterProperties.EXT_PROPRTTY_PREFIX + "pluginA.propertyA";
    CollectionAdminRequest.setClusterProperty(propertyName, "valueA")
        .process(cluster.getSolrClient());
    assertEquals("valueA", props.getClusterProperty(propertyName, null));
  }

  @Test(expected = SolrException.class)
  public void testSetInvalidPluginClusterProperty() throws Exception {
    String propertyName = "pluginA.propertyA";
    CollectionAdminRequest.setClusterProperty(propertyName, "valueA")
        .process(cluster.getSolrClient());
  }
}
