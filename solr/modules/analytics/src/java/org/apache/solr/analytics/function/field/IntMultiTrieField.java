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
package org.apache.solr.analytics.function.field;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.SortedSetDocValues;
import org.apache.solr.analytics.util.function.FloatConsumer;
import org.apache.solr.analytics.value.IntValueStream.CastingIntValueStream;
import org.apache.solr.legacy.LegacyNumericUtils;
import org.apache.solr.schema.TrieIntField;

/**
 * An analytics wrapper for a multi-valued {@link TrieIntField} with DocValues enabled.
 *
 * @deprecated Trie fields are deprecated as of Solr 7.0
 */
@Deprecated
public class IntMultiTrieField extends AnalyticsField implements CastingIntValueStream {
  private SortedSetDocValues docValues;
  private int count;
  private int[] values;

  public IntMultiTrieField(String fieldName) {
    super(fieldName);
    count = 0;
    values = new int[initialArrayLength];
  }

  @Override
  public void doSetNextReader(LeafReaderContext context) throws IOException {
    docValues = DocValues.getSortedSet(context.reader(), fieldName);
  }

  @Override
  public void collect(int doc) throws IOException {
    count = 0;
    if (docValues.advanceExact(doc)) {
      int term;
      while ((term = (int) docValues.nextOrd()) != SortedSetDocValues.NO_MORE_ORDS) {
        if (count == values.length) {
          resizeValues();
        }
        values[count++] = LegacyNumericUtils.prefixCodedToInt(docValues.lookupOrd(term));
      }
    }
  }

  private void resizeValues() {
    int[] newValues = new int[values.length * 2];
    for (int i = 0; i < count; ++i) {
      newValues[i] = values[i];
    }
    values = newValues;
  }

  @Override
  public void streamInts(IntConsumer cons) {
    for (int i = 0; i < count; ++i) {
      cons.accept(values[i]);
    }
  }

  @Override
  public void streamLongs(LongConsumer cons) {
    streamInts(value -> cons.accept((long) value));
  }

  @Override
  public void streamFloats(FloatConsumer cons) {
    streamInts(value -> cons.accept((float) value));
  }

  @Override
  public void streamDoubles(DoubleConsumer cons) {
    streamInts(value -> cons.accept((double) value));
  }

  @Override
  public void streamStrings(Consumer<String> cons) {
    streamInts(value -> cons.accept(Integer.toString(value)));
  }

  @Override
  public void streamObjects(Consumer<Object> cons) {
    streamInts(value -> cons.accept(value));
  }
}
