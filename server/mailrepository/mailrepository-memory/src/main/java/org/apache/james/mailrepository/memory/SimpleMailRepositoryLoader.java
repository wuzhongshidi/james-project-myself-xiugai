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

package org.apache.james.mailrepository.memory;

import org.apache.james.mailrepository.api.MailRepository;
import org.apache.james.mailrepository.api.MailRepositoryLoader;
import org.apache.james.mailrepository.api.MailRepositoryStore;
import org.apache.james.mailrepository.api.MailRepositoryUrl;

public class SimpleMailRepositoryLoader implements MailRepositoryLoader {
    @Override
    public MailRepository load(String fullyQualifiedClassName, MailRepositoryUrl url) throws MailRepositoryStore.MailRepositoryStoreException {
        if (fullyQualifiedClassName.equals(MemoryMailRepository.class.getCanonicalName())) {
            return new MemoryMailRepository();
        }
        throw new MailRepositoryStore.UnsupportedRepositoryStoreException(fullyQualifiedClassName + " is not supported");
    }
}
