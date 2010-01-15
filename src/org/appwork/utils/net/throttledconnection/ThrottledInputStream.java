/**
 * Copyright (c) 2009 - 2010 AppWork UG(haftungsbeschränkt) <e-mail@appwork.org>
 * 
 * This file is part of org.appwork.utils.net.throttledconnection
 * 
 * This software is licensed under the Artistic License 2.0,
 * see the LICENSE file or http://www.opensource.org/licenses/artistic-license-2.0.php
 * for details
 */
package org.appwork.utils.net.throttledconnection;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author daniel
 * 
 */
public class ThrottledInputStream extends InputStream {

    private ThrottledConnectionManager manager;
    private InputStream in;
    private long transferedCounter = 0;
    private long limitCurrent = 0;
    private long limitManaged = 0;
    private long limitCustom = 0;
    private long limitCounter = 0;
    private long lastLimitReached = 0;
    private int lastRead;

    /**
     * constructor for not managed ThrottledInputStream
     * 
     * @param in
     */
    public ThrottledInputStream(InputStream in) {
        this.in = in;
    }

    /**
     * constructor for not managed ThrottledInputStream with given limit(see
     * setCustomLimit)
     * 
     * @param in
     * @param kpsLimit
     */
    public ThrottledInputStream(InputStream in, long kpsLimit) {
        this(in);
        setCustomLimit(kpsLimit);
    }

    /**
     * constructor for managed ThrottledInputStream
     * 
     * @param in
     * @param manager
     */
    protected ThrottledInputStream(InputStream in, ThrottledConnectionManager manager) {
        this.manager = manager;
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        /* TODO: move to other read function to reduce overhead */
        lastRead = in.read();
        transferedCounter++;
        if (limitCurrent != 0) {
            limitCounter++;
            if (limitCounter > limitCurrent && lastRead != -1) {
                if (lastLimitReached == 0) {
                    /* our first read */
                    lastLimitReached = System.currentTimeMillis();
                }
                /* we set limit in kbyte per second */
                long pause = 1000 - (System.currentTimeMillis() - lastLimitReached);
                if (pause >= 0) {
                    if (pause == 0) pause = 1000;
                    synchronized (this) {
                        try {
                            wait(pause);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                lastLimitReached = System.currentTimeMillis();
                limitCounter = 0;
            }
        }
        return lastRead;
    }

    @Override
    public int available() throws IOException {
        return in.available();
    }

    @Override
    public synchronized void mark(int readlimit) {
        in.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        in.reset();
    }

    @Override
    public boolean markSupported() {
        return in.markSupported();
    }

    @Override
    public long skip(long n) throws IOException {
        return in.skip(n);
    }

    /**
     * return how many bytes got transfered till now and reset counter
     * 
     * @return
     */
    public long resetTransferdCounted() {
        try {
            return transferedCounter;
        } finally {
            transferedCounter = 0;
        }
    }

    @Override
    public void close() throws IOException {
        synchronized (this) {
            notify();
        }
        /* remove this stream from manager */
        if (manager != null) {
            manager.removeManagedThrottledInputStream(this);
            manager = null;
        }
        in.close();
    }

    /**
     * sets managed limit 0: no limit >0: use managed limit
     * 
     * @param kpsLimit
     */
    protected void setManagedLimit(long kpsLimit) {
        if (kpsLimit == limitManaged) return;
        if (kpsLimit <= 0) {
            limitManaged = 0;
            if (limitCustom == 0) changeCurrentLimit(0);
        } else {
            limitManaged = kpsLimit;
            if (limitCustom == 0) changeCurrentLimit(kpsLimit);
        }
    }

    /**
     * sets custom speed limit -1 : no limit 0 : use managed limit >0: use
     * custom limit
     * 
     * @param kpsLimit
     */
    public void setCustomLimit(long kpsLimit) {
        if (limitCustom == kpsLimit) return;
        if (kpsLimit < 0) {
            limitCustom = -1;
            changeCurrentLimit(0);
        } else if (kpsLimit == 0) {
            limitCustom = 0;
            changeCurrentLimit(limitManaged);
        } else {
            limitCustom = kpsLimit;
            changeCurrentLimit(kpsLimit);
        }
    }

    /**
     * get custom set limit
     * 
     * @return
     */
    public long getCustomLimit() {
        return limitCustom;
    }

    /**
     * change current limit
     * 
     * @param kpsLimit
     */
    private void changeCurrentLimit(long kpsLimit) {
        if (kpsLimit == limitCurrent) return;
        /* TODO: maybe allow little jitter here */
        limitCurrent = kpsLimit;
        System.out.println("new limit " + limitCurrent);
        synchronized (this) {
            notify();
        }
    }
}