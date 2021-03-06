/*
 * Copyright 2015-2016 ksyun.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://ksyun.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.ksc.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.ksc.KscServiceException;
import com.ksc.DefaultRequest;
import com.ksc.KscServiceException.ErrorType;
import com.ksc.http.HttpResponse;
import com.ksc.http.HttpResponseHandler;
import com.ksc.http.JsonErrorResponseHandler;
import com.ksc.internal.http.JsonErrorCodeParser;
import com.ksc.internal.http.JsonErrorMessageParser;
import com.ksc.transform.JsonErrorUnmarshaller;
import com.ksc.util.StringInputStream;
import com.ksc.util.StringUtils;

public class JsonErrorResponseHandlerTest {

    private static final String SERVICE_NAME = "someService";
    private static final String ERROR_CODE = "someErrorCode";
    private JsonErrorResponseHandler responseHandler;
    private HttpResponse httpResponse;

    @Mock
    private JsonErrorUnmarshaller unmarshaller;

    @Mock
    private JsonErrorCodeParser errorCodeParser;

    @Before
    public void setup() throws UnsupportedEncodingException {
        MockitoAnnotations.initMocks(this);
        when(errorCodeParser
                     .parseErrorCode(anyMapOf(String.class, String.class), (JsonNode) anyObject()))
                .thenReturn(ERROR_CODE);

        httpResponse = new HttpResponse(new DefaultRequest<String>(SERVICE_NAME), null);
        httpResponse.setContent(new StringInputStream("{}"));

        responseHandler = new JsonErrorResponseHandler(Arrays.asList(unmarshaller), errorCodeParser,
                                                       JsonErrorMessageParser.DEFAULT_ERROR_MESSAGE_PARSER,
                                                       new JsonFactory());
    }

    @Test
    public void handle_NoUnmarshallersAdded_ReturnsGenericAmazonServiceException() throws
                                                                                   Exception {
        responseHandler = new JsonErrorResponseHandler(new ArrayList<JsonErrorUnmarshaller>(),
                                                       new JsonErrorCodeParser(),
                                                       JsonErrorMessageParser.DEFAULT_ERROR_MESSAGE_PARSER,
                                                       new JsonFactory());

        KscServiceException ase = responseHandler.handle(httpResponse);

        assertNotNull(ase);
    }

    @Test
    public void handle_NoMatchingUnmarshallers_ReturnsGenericAmazonServiceException() throws
                                                                                      Exception {
        expectUnmarshallerDoesNotMatch();

        KscServiceException ase = responseHandler.handle(httpResponse);

        assertNotNull(ase);
    }

    @Test
    public void handle_NullContent_ReturnsGenericAmazonServiceException() throws Exception {
        httpResponse.setStatusCode(500);
        httpResponse.setContent(null);

        KscServiceException ase = responseHandler.handle(httpResponse);

        // We assert these common properties are set again to make sure that code path is exercised
        // for unknown AmazonServiceExceptions as well
        assertEquals(ERROR_CODE, ase.getErrorCode());
        assertEquals(500, ase.getStatusCode());
        assertEquals(SERVICE_NAME, ase.getServiceName());
        assertEquals(ErrorType.Service, ase.getErrorType());
    }

    @Test
    public void handle_EmptyContent_ReturnsGenericAmazonServiceException() throws Exception {
        httpResponse.setStatusCode(500);
        httpResponse.setContent(new StringInputStream(""));

        KscServiceException ase = responseHandler.handle(httpResponse);

        assertNotNull(ase);
    }

    @Test
    public void handle_UnmarshallerReturnsNull_ReturnsGenericAmazonServiceException() throws
                                                                                      Exception {
        expectUnmarshallerMatches();

        KscServiceException ase = responseHandler.handle(httpResponse);

        assertNotNull(ase);
        assertEquals(ERROR_CODE, ase.getErrorCode());
    }

    @Test
    public void handle_UnmarshallerThrowsException_ReturnsGenericAmazonServiceException() throws
                                                                                          Exception {
        expectUnmarshallerMatches();
        when(unmarshaller.unmarshall((JsonNode) anyObject())).thenThrow(new RuntimeException());

        KscServiceException ase = responseHandler.handle(httpResponse);

        assertNotNull(ase);
        assertEquals(ERROR_CODE, ase.getErrorCode());
    }

    @Test
    public void handle_UnmarshallerReturnsException_ClientErrorType() throws Exception {
        httpResponse.setStatusCode(400);
        expectUnmarshallerMatches();
        when(unmarshaller.unmarshall((JsonNode) anyObject()))
                .thenReturn(new CustomException("error"));

        KscServiceException ase = responseHandler.handle(httpResponse);

        assertEquals(ERROR_CODE, ase.getErrorCode());
        assertEquals(400, ase.getStatusCode());
        assertEquals(SERVICE_NAME, ase.getServiceName());
        assertEquals(ErrorType.Client, ase.getErrorType());
    }

    @Test
    public void handle_UnmarshallerReturnsException_ServiceErrorType() throws Exception {
        httpResponse.setStatusCode(500);
        expectUnmarshallerMatches();
        when(unmarshaller.unmarshall((JsonNode) anyObject()))
                .thenReturn(new CustomException("error"));

        KscServiceException ase = responseHandler.handle(httpResponse);

        assertEquals(ErrorType.Service, ase.getErrorType());
    }

    @Test
    public void handle_UnmarshallerReturnsException_WithRequestId() throws Exception {
        httpResponse.setStatusCode(500);
        httpResponse.addHeader(HttpResponseHandler.X_KSC_REQUEST_ID_HEADER, "1234");
        expectUnmarshallerMatches();
        when(unmarshaller.unmarshall((JsonNode) anyObject()))
                .thenReturn(new CustomException("error"));

        KscServiceException ase = responseHandler.handle(httpResponse);

        assertEquals("1234", ase.getRequestId());
    }

    /**
     * Headers are case insensitive so the request id should still be parsed in this test
     */
    @Test
    public void handle_UnmarshallerReturnsException_WithCaseInsensitiveRequestId() throws
                                                                                   Exception {
        httpResponse.setStatusCode(500);
        httpResponse.addHeader(StringUtils.upperCase(HttpResponseHandler.X_KSC_REQUEST_ID_HEADER),
                               "1234");
        expectUnmarshallerMatches();
        when(unmarshaller.unmarshall((JsonNode) anyObject()))
                .thenReturn(new CustomException("error"));

        KscServiceException ase = responseHandler.handle(httpResponse);

        assertEquals("1234", ase.getRequestId());
    }

    private void expectUnmarshallerMatches() throws Exception {
        when(unmarshaller.matchErrorCode(anyString())).thenReturn(true);
    }

    private void expectUnmarshallerDoesNotMatch() throws Exception {
        when(unmarshaller.matchErrorCode(anyString())).thenReturn(false);
    }

    private static class CustomException extends KscServiceException {

        private static final long serialVersionUID = 1305027296023640779L;

        public CustomException(String errorMessage) {
            super(errorMessage);
        }
    }
}
