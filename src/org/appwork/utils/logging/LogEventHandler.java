/**
 * Copyright (c) 2009 - 2010 AppWork UG(haftungsbeschränkt) <e-mail@appwork.org>
 * 
 * This file is part of org.appwork.utils.logging
 * 
 * This software is licensed under the Artistic License 2.0,
 * see the LICENSE file or http://www.opensource.org/licenses/artistic-license-2.0.php
 * for details
 */
package org.appwork.utils.logging;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LogEventHandler extends Handler {
    private static final LogEventHandler INSTANCE = new LogEventHandler();
    private LogEventSender eventSender;

    private LogEventHandler() {
        super();
        synchronized (lock) {
            cache = new ArrayList<LogRecord>();
        }
        eventSender = new LogEventSender();

    }

    /**
     * @return the {@link LogEventHandler#eventSender}
     * @see LogEventHandler#eventSender
     */
    public LogEventSender getEventSender() {
        return eventSender;
    }

    public static LogEventHandler getInstance() {
        return INSTANCE;
    }

    private ArrayList<LogRecord> cache;
    private Object lock = new Object();;

    public ArrayList<LogRecord> getCache() {
        synchronized (lock) {
            return new ArrayList<LogRecord>(cache);
        }
    }

    public void close() {
    }

    public void flush() {
    }

    public void publish(LogRecord logRecord) {
        this.cache.add(logRecord);
        getEventSender().fireEvent(new LogEvent(this, LogEvent.NEW_RECORD, logRecord));

    }

}
