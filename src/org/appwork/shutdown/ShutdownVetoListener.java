/**
 * Copyright (c) 2009 - 2011 AppWork UG(haftungsbeschränkt) <e-mail@appwork.org>
 * 
 * This file is part of org.appwork.shutdown
 * 
 * This software is licensed under the Artistic License 2.0,
 * see the LICENSE file or http://www.opensource.org/licenses/artistic-license-2.0.php
 * for details
 */
package org.appwork.shutdown;

import java.util.ArrayList;

/**
 * @author thomas
 * 
 */
public interface ShutdownVetoListener {
    public boolean onShutdownRequest() throws ShutdownVetoException;

    /**
     * @param vetos
     */
    public void onShutdownVeto(ArrayList<ShutdownVetoException> vetos);
}
