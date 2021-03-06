/**
 * Copyright (c) 2009 - 2010 AppWork UG(haftungsbeschränkt) <e-mail@appwork.org>
 * 
 * This file is part of org.appwork.remotecall.server
 * 
 * This software is licensed under the Artistic License 2.0,
 * see the LICENSE file or http://www.opensource.org/licenses/artistic-license-2.0.php
 * for details
 */
package org.appwork.remotecall.server;

import org.appwork.storage.Storable;

/**
 * @author thomas
 */
public class ServerInvokationException extends Exception implements Storable {

    private static final long serialVersionUID = -3140111161245241758L;
    private final String      remoteID;

    public ServerInvokationException(final String handleRequestError, final String remoteID) {
        super(handleRequestError);
        this.remoteID = remoteID;

    }

    public String getRemoteID() {
        return this.remoteID;
    }

}
