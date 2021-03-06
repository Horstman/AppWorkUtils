/**
 * Copyright (c) 2009 - 2010 AppWork UG(haftungsbeschränkt) <e-mail@appwork.org>
 * 
 * This file is part of org.appwork.utils.event
 * 
 * This software is licensed under the Artistic License 2.0,
 * see the LICENSE file or http://www.opensource.org/licenses/artistic-license-2.0.php
 * for details
 */
package org.appwork.utils.event;

import java.util.ArrayList;
import java.util.EventListener;

/**
 * The Eventsenderclass is the core of the Eventsystem. it can be used to design
 * new Eventbroadcaster Systems easily.
 * 
 * 
 * 
 * @author $Author: unknown$
 * 
 */

public abstract class Eventsender<T extends EventListener, TT extends DefaultEvent> {

    transient protected ArrayList<T>          addRequestedListeners    = null;
    /**
     * List of registered Eventlistener
     */
    // TODO: DO we really need ArrayLists here?
    transient volatile protected ArrayList<T> listeners                = null;
    private final Object                      LOCK                     = new Object();
    private volatile long                     readR                    = 0;
    /**
     * List of Listeners that are requested for removal
     * 
     */
    // We use a removeList to avoid threating problems
    transient protected ArrayList<T>          removeRequestedListeners = null;

    private volatile long                     writeR                   = 0;

    /**
     * Creates a new Eventsender Instance
     */
    public Eventsender() {
        this.listeners = new ArrayList<T>();
        this.removeRequestedListeners = new ArrayList<T>();
        this.addRequestedListeners = new ArrayList<T>();
    }

    /**
     * Adds a list of listeners
     * 
     * @param listener
     */
    public void addAllListener(final ArrayList<T> listener) {
        for (final T l : listener) {
            this.addListener(l);
        }

    }

    /**
     * Add a single Listener
     * 
     * @param listener
     */
    public void addListener(final T t) {
        synchronized (this.LOCK) {
            /* decrease WriteCounter in case we remove the removeRequested */
            if (this.removeRequestedListeners.contains(t)) {
                this.removeRequestedListeners.remove(t);
                this.writeR--;
            }
            /*
             * increase WriteCounter in case we add addRequestedListeners and t
             * is not in current listeners list
             */
            if (!this.addRequestedListeners.contains(t) && !this.listeners.contains(t)) {
                this.addRequestedListeners.add(t);
                this.writeR++;
            }
        }
    }

    /**
     * Fires an Event to all registered Listeners
     * 
     * @param event
     * @return
     */
    final public void fireEvent(final int id, final Object... parameters) {

        ArrayList<T> listeners;
        synchronized (this.LOCK) {
            if (this.writeR == this.readR) {
                /* nothing changed, we can use old pointer to listeners */
                if (this.listeners.size() == 0) { return; }
                listeners = this.listeners;
            } else {
                /* create new list with copy of old one */
                listeners = new ArrayList<T>(this.listeners);
                /* remove and add wished items */
                listeners.removeAll(this.removeRequestedListeners);
                this.removeRequestedListeners.clear();
                listeners.addAll(this.addRequestedListeners);
                this.addRequestedListeners.clear();
                /* update ReadCounter and pointer to listeners */
                this.readR = this.writeR;
                this.listeners = listeners;
                if (this.listeners.size() == 0) { return; }
            }
        }
        for (final T t : listeners) {
            this.fireEvent(t, id, parameters);
        }
        synchronized (this.LOCK) {
            if (this.writeR != this.readR) {
                /* something changed, lets update the list */
                /* create new list with copy of old one */
                listeners = new ArrayList<T>(this.listeners);
                /* remove and add wished items */
                listeners.removeAll(this.removeRequestedListeners);
                this.removeRequestedListeners.clear();
                listeners.addAll(this.addRequestedListeners);
                this.addRequestedListeners.clear();
                /* update ReadCounter and pointer to listeners */
                this.readR = this.writeR;
                this.listeners = listeners;
            }
        }
    }

    /**
     * 
     * @param t
     * @param id
     * @param parameters
     */
    protected void fireEvent(final T listener, final int id, final Object... parameters) {
        throw new RuntimeException("Not implemented. Overwrite org.appwork.utils.event.Eventsender.fireEvent(T, int, Object...) to use this");

    }

    /**
     * Abstract fire Event Method.
     * 
     * @param listener
     * @param event
     */
    protected abstract void fireEvent(T listener, TT event);

    final public void fireEvent(final TT event) {
        if (event == null) { return; }
        ArrayList<T> listeners;
        synchronized (this.LOCK) {
            if (this.writeR == this.readR) {
                /* nothing changed, we can use old pointer to listeners */
                if (this.listeners.size() == 0) { return; }
                listeners = this.listeners;
            } else {
                /* create new list with copy of old one */
                listeners = new ArrayList<T>(this.listeners);
                /* remove and add wished items */
                listeners.removeAll(this.removeRequestedListeners);
                this.removeRequestedListeners.clear();
                listeners.addAll(this.addRequestedListeners);
                this.addRequestedListeners.clear();
                /* update ReadCounter and pointer to listeners */
                this.readR = this.writeR;
                this.listeners = listeners;
                if (this.listeners.size() == 0) { return; }
            }
        }
        for (final T t : listeners) {
            // final long tt = System.currentTimeMillis();

            this.fireEvent(t, event);
            // System.out.println(t + " " + (System.currentTimeMillis() - tt));
        }
        synchronized (this.LOCK) {
            if (this.writeR != this.readR) {
                /* something changed, lets update the list */
                /* create new list with copy of old one */
                listeners = new ArrayList<T>(this.listeners);
                /* remove and add wished items */
                listeners.removeAll(this.removeRequestedListeners);
                this.removeRequestedListeners.clear();
                listeners.addAll(this.addRequestedListeners);
                this.addRequestedListeners.clear();
                /* update ReadCounter and pointer to listeners */
                this.readR = this.writeR;
                this.listeners = listeners;
            }
        }
    }

    public ArrayList<T> getListener() {
        synchronized (this.LOCK) {
            return new ArrayList<T>(this.listeners);
        }
    }

    public boolean hasListener() {
        synchronized (this.LOCK) {
            return listeners.size() > 0;
        }
    }

    public void removeListener(final T t) {
        synchronized (this.LOCK) {
            /* decrease WriteCounter in case we remove the addRequest */
            if (this.addRequestedListeners.contains(t)) {
                this.addRequestedListeners.remove(t);
                this.writeR--;
            }
            /*
             * increase WriteCounter in case we add removeRequest and t is in
             * current listeners list
             */
            if (!this.removeRequestedListeners.contains(t) && this.listeners.contains(t)) {
                this.removeRequestedListeners.add(t);
                this.writeR++;
            }
        }
    }
}
