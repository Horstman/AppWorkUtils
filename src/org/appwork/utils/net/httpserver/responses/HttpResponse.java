/**
 * Copyright (c) 2009 - 2011 AppWork UG(haftungsbeschränkt) <e-mail@appwork.org>
 * 
 * This file is part of org.appwork.utils.net.httpserver.requests
 * 
 * This software is licensed under the Artistic License 2.0,
 * see the LICENSE file or http://www.opensource.org/licenses/artistic-license-2.0.php
 * for details
 */
package org.appwork.utils.net.httpserver.responses;

import java.io.IOException;
import java.io.OutputStream;

import org.appwork.net.protocol.http.HTTPConstants;
import org.appwork.net.protocol.http.HTTPConstants.ResponseCode;
import org.appwork.utils.net.HTTPHeader;
import org.appwork.utils.net.HeaderCollection;
import org.appwork.utils.net.httpserver.HttpConnection;

/**
 * @author daniel
 * 
 */
public class HttpResponse {

    private final HeaderCollection responseHeaders;
    public static final byte[]     NEWLINE      = "\r\n".getBytes();
    public static final byte[]     HTTP11       = "HTTP/1.1 ".getBytes();
    public static final byte[]     _0           = "0".getBytes();
    private ResponseCode           responseCode = ResponseCode.SUCCESS_NO_CONTENT;
    private final HttpConnection   connection;
    private OutputStream           outputStream = null;

    public HttpResponse(final HttpConnection connection) {
        this.connection = connection;
        this.responseHeaders = new HeaderCollection();
        this.responseHeaders.add(new HTTPHeader(HTTPConstants.HEADER_REQUEST_CONNECTION, "Close"));
    }

    /**
     * returns this HttpResonse's OutputStream. NOTE: set ResponseHeaders/Code
     * before first call of this. once the OutputStream is available you cannot
     * change ResponseHeaders/Code anymore
     * 
     * @return
     * @throws IOException
     */
    public OutputStream getOutputStream() throws IOException {
        if (this.outputStream == null) {
            this.outputStream = this.connection.getOutputStream();
        }
        return this.outputStream;
    }

    /**
     * @return the responseCode
     */
    public ResponseCode getResponseCode() {
        return this.responseCode;
    }

    /**
     * @return the responseHeaders
     */
    public HeaderCollection getResponseHeaders() {
        return this.responseHeaders;
    }

    /**
     * @param responseCode
     *            the responseCode to set
     */
    public void setResponseCode(final ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

}
