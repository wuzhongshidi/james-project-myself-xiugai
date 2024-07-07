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

package org.apache.james.events;

import static java.time.Duration.ZERO;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Durations.ONE_HUNDRED_MILLISECONDS;

import java.time.Duration;

import org.awaitility.core.ConditionFactory;

public interface EventBusContract {

    enum EnvironmentSpeedProfile {
        SLOW(Duration.ofSeconds(2), Duration.ofSeconds(10)),
        FAST(Duration.ofMillis(200), Duration.ofSeconds(5));

        private final Duration shortWaitTime;
        private final Duration longWaitTime;

        EnvironmentSpeedProfile(Duration shortWaitTime, Duration longWaitTime) {
            this.shortWaitTime = shortWaitTime;
            this.longWaitTime = longWaitTime;
        }

        public Duration getShortWaitTime() {
            return shortWaitTime;
        }

        public Duration getLongWaitTime() {
            return longWaitTime;
        }

        public ConditionFactory shortWaitCondition() {
            return await().pollDelay(ZERO)
                .pollInterval(ONE_HUNDRED_MILLISECONDS)
                .timeout(getShortWaitTime());
        }

        public ConditionFactory longWaitCondition() {
            return await().pollDelay(ZERO)
                .pollInterval(ONE_HUNDRED_MILLISECONDS)
                .timeout(getLongWaitTime());
        }
    }

    EnvironmentSpeedProfile getSpeedProfile();

    interface MultipleEventBusContract extends EventBusContract {

        EventBus eventBus2();
    }

    EventBus eventBus();
}