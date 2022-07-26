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
package org.apache.solr.analytics.function;

import org.apache.solr.analytics.function.field.AnalyticsField;
import org.apache.solr.analytics.function.reduction.data.ReductionDataCollector;

/** The {@link ReductionCollectionManager} used for distributed requests. */
public class MergingReductionCollectionManager extends ReductionCollectionManager {

  public MergingReductionCollectionManager() {
    super();
  }

  public MergingReductionCollectionManager(
      final ReductionDataCollector<?>[] reductionDataCollectors,
      final Iterable<AnalyticsField> fields) {
    super(reductionDataCollectors, fields);
  }

  @Override
  protected ReductionCollectionManager createNewManager(
      final ReductionDataCollector<?>[] reductionDataCollectors,
      final Iterable<AnalyticsField> fields) {
    return new MergingReductionCollectionManager(reductionDataCollectors, fields);
  }

  @Override
  public void setData(ReductionDataCollection dataCollection) {
    for (int i = 0; i < reductionDataCollectors.length; i++) {
      reductionDataCollectors[i].setMergedData(dataCollection.dataArr[i]);
    }
  }
}
