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

package org.apache.james.jmap;

import java.util.Objects;

import io.netty.handler.codec.http.HttpMethod;
import reactor.netty.http.server.HttpServerRequest;

public class Endpoint {
    public static Endpoint ofFixedPath(HttpMethod method, String path) {
        return new Endpoint(method, path, new UriMatcher.Fixed(path));
    }

    private final HttpMethod method;
    private final String path;
    private final UriMatcher uriMatcher;

    public Endpoint(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
        this.uriMatcher = UriMatcher.Impl.of(path);
    }

    private Endpoint(HttpMethod method, String path, UriMatcher uriMatcher) {
        this.method = method;
        this.path = path;
        this.uriMatcher = uriMatcher;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public boolean matches(HttpServerRequest request) {
        return method.equals(request.method())
            && uriMatcher.matches(request.uri());
    }

    UriMatcher getUriMatcher() {
        return uriMatcher;
    }

    @Override
    public final boolean equals(Object o) {
        if (o instanceof Endpoint) {
            Endpoint endpoint = (Endpoint) o;

            return Objects.equals(this.method, endpoint.method)
                && Objects.equals(this.path, endpoint.path);
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(method, path);
    }
}
