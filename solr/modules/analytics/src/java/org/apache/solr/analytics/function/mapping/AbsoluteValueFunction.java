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
package org.apache.solr.analytics.function.mapping;

import org.apache.solr.analytics.ExpressionFactory.CreatorFunction;
import org.apache.solr.analytics.value.AnalyticsValueStream;
import org.apache.solr.analytics.value.DoubleValueStream;
import org.apache.solr.analytics.value.FloatValueStream;
import org.apache.solr.analytics.value.IntValueStream;
import org.apache.solr.analytics.value.LongValueStream;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;

/**
 * An absolute value mapping function.
 *
 * <p>Takes a numeric ValueStream or Value and returns a ValueStream or Value of the same numeric
 * type.
 */
public class AbsoluteValueFunction {
  public static final String name = "abs";
  public static final CreatorFunction creatorFunction =
      (params -> {
        if (params.length != 1) {
          throw new SolrException(
              ErrorCode.BAD_REQUEST,
              "The " + name + " function requires 1 paramaters, " + params.length + " found.");
        }
        AnalyticsValueStream param = params[0];
        if (param instanceof IntValueStream) {
          return LambdaFunction.createIntLambdaFunction(
              name, x -> (x < 0) ? x * -1 : x, (IntValueStream) param);
        }
        if (param instanceof LongValueStream) {
          return LambdaFunction.createLongLambdaFunction(
              name, x -> (x < 0) ? x * -1 : x, (LongValueStream) param);
        }
        if (param instanceof FloatValueStream) {
          return LambdaFunction.createFloatLambdaFunction(
              name, x -> (x < 0) ? x * -1 : x, (FloatValueStream) param);
        }
        if (param instanceof DoubleValueStream) {
          return LambdaFunction.createDoubleLambdaFunction(
              name, x -> (x < 0) ? x * -1 : x, (DoubleValueStream) param);
        }
        throw new SolrException(
            ErrorCode.BAD_REQUEST,
            "The "
                + name
                + " function requires a numeric parameter, "
                + param.getExpressionStr()
                + " found.");
      });
}
