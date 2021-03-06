/**
 * Copyright (c) 2009 - 2011 AppWork UG(haftungsbeschränkt) <e-mail@appwork.org>
 * 
 * This file is part of org.appwork.utils.net
 * 
 * This software is licensed under the Artistic License 2.0,
 * see the LICENSE file or http://www.opensource.org/licenses/artistic-license-2.0.php
 * for details
 */
package org.appwork.utils.net;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.appwork.utils.logging.Log;

/**
 * @author daniel
 * 
 */
public class HeaderCollection implements Iterable<HTTPHeader> {
    private final LinkedList<HTTPHeader>      headers;
    private final HashMap<String, HTTPHeader> headersMap;
    private static HashMap<String, Boolean>   DUPES_ALLOWED = new HashMap<String, Boolean>();
    static {
        HeaderCollection.DUPES_ALLOWED.put("Set-Cookies", true);
    }

    public HeaderCollection() {
        this.headers = new LinkedList<HTTPHeader>();
        this.headersMap = new HashMap<String, HTTPHeader>();
    }

    public void add(final HTTPHeader header) {
        if (!HeaderCollection.DUPES_ALLOWED.containsKey(header.getKey()) && this.headersMap.containsKey(header.getKey())) {
            // overwrite
            Log.L.warning("Overwrite Header: " + header);
            for (final Iterator<HTTPHeader> it = this.headers.iterator(); it.hasNext();) {
                if (it.next().getKey().equals(header.getKey())) {
                    it.remove();
                    break;
                }
            }
        }
        this.headers.add(header);
        this.headersMap.put(header.getKey(), header);
    }

    public HTTPHeader get(final String key) {
        return this.headersMap.get(key);
    }

    public String getValue(final String key) {
        final HTTPHeader ret = this.headersMap.get(key);
        if (ret != null) { return ret.getValue(); }
        return null;
    }

    @Override
    public Iterator<HTTPHeader> iterator() {
        return this.headers.iterator();
    }
}
