/**
 * Copyright (c) 2009 - 2011 AppWork UG(haftungsbeschränkt) <e-mail@appwork.org>
 * 
 * This file is part of org.appwork.storage.config
 * 
 * This software is licensed under the Artistic License 2.0,
 * see the LICENSE file or http://www.opensource.org/licenses/artistic-license-2.0.php
 * for details
 */
package org.appwork.storage.config;

import org.appwork.utils.event.SimpleEvent;

/**
 * @author thomas
 * 
 */
public class ConfigEvent<T extends ConfigInterface> extends SimpleEvent<T, Object, ConfigEvent.Types> {
    public static enum Types {
        VALUE_UPDATED

    }

    /**
     * @param caller
     * @param type
     * @param parameters
     */
    public ConfigEvent(final T caller, final Types type, final Object... parameters) {
        super(caller, type, parameters);
        // TODO Auto-generated constructor stub
    }

}
