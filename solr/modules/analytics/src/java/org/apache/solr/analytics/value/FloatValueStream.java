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
package org.apache.solr.analytics.value;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import org.apache.solr.analytics.util.function.FloatConsumer;

/**
 * A multi-valued analytics value that can be represented as a float.
 *
 * <p>The back-end production of the value can change inbetween calls to {@link #streamFloats},
 * resulting in different values on each call.
 */
public interface FloatValueStream extends AnalyticsValueStream {
  /**
   * Stream the float representations of all current values, if any exist.
   *
   * @param cons The consumer to accept the values
   */
  void streamFloats(FloatConsumer cons);

  /**
   * An interface that represents all of the types a {@link FloatValueStream} should be able to cast
   * to.
   */
  public static interface CastingFloatValueStream
      extends FloatValueStream, DoubleValueStream, StringValueStream {}

  /**
   * An abstract base for {@link CastingFloatValueStream} that automatically casts to all types if
   * {@link #streamFloats} is implemented.
   */
  public abstract static class AbstractFloatValueStream implements CastingFloatValueStream {
    @Override
    public void streamDoubles(DoubleConsumer cons) {
      streamFloats((float val) -> cons.accept(val));
    }

    @Override
    public void streamStrings(Consumer<String> cons) {
      streamFloats((float val) -> cons.accept(Float.toString(val)));
    }

    @Override
    public void streamObjects(Consumer<Object> cons) {
      streamFloats((float val) -> cons.accept(val));
    }

    @Override
    public AnalyticsValueStream convertToConstant() {
      return this;
    }
  }
}
