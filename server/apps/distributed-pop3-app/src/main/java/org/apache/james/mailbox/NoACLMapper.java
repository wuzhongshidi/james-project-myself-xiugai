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

package org.apache.james.mailbox;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.james.mailbox.acl.ACLDiff;
import org.apache.james.mailbox.cassandra.ids.CassandraId;
import org.apache.james.mailbox.cassandra.mail.ACLMapper;
import org.apache.james.mailbox.model.MailboxACL;

import reactor.core.publisher.Mono;

public class NoACLMapper implements ACLMapper {
    @Override
    public Mono<MailboxACL> getACL(CassandraId cassandraId) {
        return Mono.empty();
    }

    @Override
    public Mono<ACLDiff> updateACL(CassandraId cassandraId, MailboxACL.ACLCommand command) {
        return Mono.error(new NotImplementedException());
    }

    @Override
    public Mono<ACLDiff> setACL(CassandraId cassandraId, MailboxACL mailboxACL) {
        return Mono.error(new NotImplementedException());
    }

    @Override
    public Mono<Void> delete(CassandraId cassandraId) {
        return Mono.empty();
    }
}
