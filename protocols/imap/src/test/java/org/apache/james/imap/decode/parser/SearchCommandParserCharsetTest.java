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

package org.apache.james.imap.decode.parser;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.james.imap.ImapFixture.TAG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.james.imap.api.ImapConstants;
import org.apache.james.imap.api.ImapMessage;
import org.apache.james.imap.api.display.HumanReadableText;
import org.apache.james.imap.api.message.request.SearchKey;
import org.apache.james.imap.api.message.response.StatusResponse;
import org.apache.james.imap.api.message.response.StatusResponseFactory;
import org.apache.james.imap.decode.ImapRequestLineReader;
import org.apache.james.imap.decode.ImapRequestStreamLineReader;
import org.apache.james.imap.decode.parser.SearchCommandParser.Context;
import org.apache.james.imap.encode.FakeImapSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchCommandParserCharsetTest {
    private static final String ASCII_SEARCH_TERM = "A Search Term";

    private static final String NON_ASCII_SEARCH_TERM = "как Дела?";

    private static final byte[] BYTES_NON_ASCII_SEARCH_TERM = NioUtils.toBytes(
            NON_ASCII_SEARCH_TERM, UTF_8);

    private static final byte[] BYTES_UTF8_NON_ASCII_SEARCH_TERM = NioUtils
            .add(NioUtils.toBytes(" {16}\r\n", US_ASCII),
                    BYTES_NON_ASCII_SEARCH_TERM);

    private static final byte[] CHARSET = NioUtils.toBytes("CHARSET UTF-8 ",
        US_ASCII);

    SearchCommandParser parser;
    StatusResponseFactory mockStatusResponseFactory;
    ImapMessage message;

    @BeforeEach
    void setUp() {
        mockStatusResponseFactory = mock(StatusResponseFactory.class);
        parser = new SearchCommandParser(mockStatusResponseFactory);
        message = mock(ImapMessage.class);
    }

    @Test
    void testBadCharset() throws Exception {
        ImapRequestLineReader reader = new ImapRequestStreamLineReader(
                new ByteArrayInputStream("CHARSET BOGUS ".getBytes(US_ASCII)),
                new ByteArrayOutputStream());
        parser.decode(reader, TAG, false, new FakeImapSession());

        verify(mockStatusResponseFactory, times(1)).taggedNo(
            eq(TAG),
            same(ImapConstants.SEARCH_COMMAND),
            eq(HumanReadableText.BAD_CHARSET),
            eq(StatusResponse.ResponseCode.badCharset()));

        verifyNoMoreInteractions(mockStatusResponseFactory);
    }

    @Test
    void testBCCShouldConvertCharset() throws Exception {
        SearchKey key = SearchKey.buildBcc(NON_ASCII_SEARCH_TERM);
        checkUTF8Valid("BCC".getBytes(US_ASCII), key);
    }

    @Test
    void testBODYShouldConvertCharset() throws Exception {
        SearchKey key = SearchKey.buildBody(NON_ASCII_SEARCH_TERM);
        checkUTF8Valid("BODY".getBytes(US_ASCII), key);
    }

    @Test
    void testCCShouldConvertCharset() throws Exception {
        SearchKey key = SearchKey.buildCc(NON_ASCII_SEARCH_TERM);
        checkUTF8Valid("CC".getBytes(US_ASCII), key);
    }

    @Test
    void testFROMShouldConvertCharset() throws Exception {
        SearchKey key = SearchKey.buildFrom(NON_ASCII_SEARCH_TERM);
        checkUTF8Valid("FROM".getBytes(US_ASCII), key);
    }

    @Test
    void testHEADERShouldConvertCharset() throws Exception {
        SearchKey key = SearchKey
                .buildHeader("whatever", NON_ASCII_SEARCH_TERM);
        checkUTF8Valid("HEADER whatever".getBytes(US_ASCII), key);
    }

    @Test
    void testSUBJECTShouldConvertCharset() throws Exception {
        SearchKey key = SearchKey.buildSubject(NON_ASCII_SEARCH_TERM);
        checkUTF8Valid("SUBJECT".getBytes(US_ASCII), key);
    }

    @Test
    void testTEXTShouldConvertCharset() throws Exception {
        SearchKey key = SearchKey.buildText(NON_ASCII_SEARCH_TERM);
        checkUTF8Valid("TEXT".getBytes(US_ASCII), key);
    }

    @Test
    void testTOShouldConvertCharset() throws Exception {
        SearchKey key = SearchKey.buildTo(NON_ASCII_SEARCH_TERM);
        checkUTF8Valid("TO".getBytes(US_ASCII), key);
    }

    @Test
    void testASCIICharset() throws Exception {
        SearchKey key = SearchKey.buildBcc(ASCII_SEARCH_TERM);
        checkValid("CHARSET US-ASCII BCC \"" + ASCII_SEARCH_TERM + "\"", key,
                true, "US-ASCII");
    }

    @Test
    void testSimpleUTF8Charset() throws Exception {
        SearchKey key = SearchKey.buildBcc(ASCII_SEARCH_TERM);
        checkValid("CHARSET UTF-8 BCC \"" + ASCII_SEARCH_TERM + "\"", key,
                true, "US-ASCII");
    }

    private void checkUTF8Valid(byte[] term, SearchKey key)
            throws Exception {
        ImapRequestLineReader reader = new ImapRequestStreamLineReader(
                new ByteArrayInputStream(NioUtils.add(NioUtils.add(CHARSET,
                        term), BYTES_UTF8_NON_ASCII_SEARCH_TERM)),
                new ByteArrayOutputStream());
        final SearchKey searchKey = parser.searchKey(null, reader, new Context(), true);
        assertThat(searchKey).isEqualTo(key);
    }

    private void checkValid(String input, SearchKey key, boolean isFirst,
            String charset) throws Exception {
        ImapRequestLineReader reader = new ImapRequestStreamLineReader(
                new ByteArrayInputStream(input.getBytes(charset)),
                new ByteArrayOutputStream());

        final SearchKey searchKey = parser.searchKey(null, reader, new Context(), isFirst);
        assertThat(searchKey).isEqualTo(key);
    }

}
