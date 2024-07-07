/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.jmap.cassandra.change.tables;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public interface CassandraMailboxChangeTable {
    String TABLE_NAME = "mailbox_change";

    CqlIdentifier ACCOUNT_ID = CqlIdentifier.fromCql("account_id");
    CqlIdentifier STATE = CqlIdentifier.fromCql("state");
    CqlIdentifier DATE = CqlIdentifier.fromCql("date");
    CqlIdentifier IS_DELEGATED = CqlIdentifier.fromCql("is_delegated");
    CqlIdentifier IS_COUNT_CHANGE = CqlIdentifier.fromCql("is_count_change");
    CqlIdentifier CREATED = CqlIdentifier.fromCql("created");
    CqlIdentifier UPDATED = CqlIdentifier.fromCql("updated");
    CqlIdentifier DESTROYED = CqlIdentifier.fromCql("destroyed");
}
