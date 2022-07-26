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

package org.apache.solr.core.backup;

import static org.apache.solr.core.backup.BackupId.TRADITIONAL_BACKUP;

import org.apache.solr.SolrTestCase;
import org.junit.Test;

/** Unit tests for {@link BackupId} */
public class BackupIdTest extends SolrTestCase {

  @Test
  public void testZero() {
    final BackupId id = BackupId.zero();
    assertEquals(0, id.getId());
  }

  @Test
  public void testTraditionalBackupId() {
    final BackupId id = BackupId.traditionalBackup();
    assertEquals(TRADITIONAL_BACKUP, id.getId());
  }

  @Test
  public void testBackupIdIncrementing() {
    final BackupId initialId = new BackupId(3);
    final BackupId nextId = initialId.nextBackupId();

    assertEquals(3, initialId.getId());
    assertEquals(4, nextId.getId());
    assertTrue(initialId.compareTo(nextId) < 0);
  }
}
