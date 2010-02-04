/**
 * Copyright (c) 2009 - 2010 AppWork UG(haftungsbeschränkt) <e-mail@appwork.org>
 * 
 * This file is part of org.appwork.utils.orm
 * 
 * This software is licensed under the Artistic License 2.0,
 * see the LICENSE file or http://www.opensource.org/licenses/artistic-license-2.0.php
 * for details
 */
package org.appwork.utils.orm.converter;

import java.sql.Connection;
import java.sql.SQLException;

import org.appwork.utils.orm.ORMapper;

/**
 * @author coalado
 * 
 */
public abstract class ClassConverter {

    protected Connection db;

    /**
     * @return the db
     */
    public Connection getDb() {
        return db;
    }

    /**
     * @param db
     *            the db to set
     */
    public void setDb(Connection db) {
        this.db = db;
    }

    protected ORMapper owner;

    public ClassConverter() {

    }

    /**
     * @param tableID
     * @return
     * @throws SQLException
     */
    public abstract String createTable(String tableID) throws SQLException;

    /**
     * @param tableID
     */
    public abstract void checkIntegrety(String tableID) throws SQLException;

    /**
     * @param tableID
     * @param item
     * @param instanceID
     * @return
     * @throws SQLException
     */
    public abstract int write(String tableID, Object item, String instanceID) throws SQLException;

    /**
     * @param tableID
     * @param item
     * @param instanceID
     * @return
     * @throws SQLException
     */
    public abstract int update(String tableID, Object item, String instanceID) throws SQLException;

    /**
     * @param clazz
     * @param id
     * @return
     * @throws SQLException
     */
    public abstract Object get(Class<?> clazz, String where) throws SQLException;

    /**
     * @param orMapper
     */
    public void setOwner(ORMapper orMapper) {
        owner = orMapper;

    }

}
