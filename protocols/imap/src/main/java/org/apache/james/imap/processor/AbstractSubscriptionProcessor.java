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
package org.apache.james.imap.processor;

import org.apache.james.imap.api.message.request.ImapRequest;
import org.apache.james.imap.api.message.response.StatusResponseFactory;
import org.apache.james.imap.api.process.ImapSession;
import org.apache.james.mailbox.MailboxManager;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.SubscriptionManager;
import org.apache.james.metrics.api.MetricFactory;

import reactor.core.publisher.Mono;

/**
 * Abstract base class which should be used by implementations which need to
 * access the {@link SubscriptionManager}
 */
public abstract class AbstractSubscriptionProcessor<R extends ImapRequest> extends AbstractMailboxProcessor<R> {

    private final SubscriptionManager subscriptionManager;

    public AbstractSubscriptionProcessor(Class<R> acceptableClass, MailboxManager mailboxManager, SubscriptionManager subscriptionManager, StatusResponseFactory factory,
                                         MetricFactory metricFactory) {
        super(acceptableClass, mailboxManager, factory, metricFactory);
        this.subscriptionManager = subscriptionManager;
    }

    /**
     * Return the {@link SubscriptionManager}
     * 
     * @return subscriptionManager
     */
    protected SubscriptionManager getSubscriptionManager() {
        return subscriptionManager;
    }

    @Override
    protected final Mono<Void> processRequestReactive(R request, ImapSession session, Responder responder) {

        // take care of calling the start/end processing
        MailboxSession mSession = session.getMailboxSession();

        return Mono.fromRunnable(() -> getSubscriptionManager().startProcessingRequest(mSession))
            .then(doProcessRequest(request, session, responder))
            .then(Mono.fromRunnable(() -> getSubscriptionManager().endProcessingRequest(mSession)));
    }

    /**
     * Process the request
     */
    protected abstract Mono<Void> doProcessRequest(R request, ImapSession session, Responder responder);

}
