/**
 * File Name: DatabaseConn.java (path: app/src/main/java/com/example/navigator/DatabaseConn.java)
 * Version: 1.0
 * Author: Brute Force - Database Connection Management
 * Project: Indoor Mall Navigation
 * Organisation: DVT
 * Copyright: (c) Copyright 2019 University of Pretoria
 * Related Documents:
 * Update History:
 * Date Author Changes
 * --------------------------------------------
 * 02/08/2019 Thomas Original
 * Functional Description: This program file establishes the abstract class for all database connection adapter implementations
 * Assumptions: It is assumed that the database class will be provided with a concrete class
 */

package com.example.navigator.utils;

/**
 * Purpose: This class is an abstract database connection class
 *
 * Description : Provide a polymorphic parent class to use throughout the program
 */

public abstract class DatabaseConn {

//    protected Object database;
    protected Object query = null;
    protected Object[] vars = null;
    private static DatabaseConn connection = null;


    public DatabaseConn(){
        connect();
    }

    /**
     * Singleton static access, this is entirely trust based
     *
     * @return The open connection to the database
     */
    public static DatabaseConn open()
    {
        return connection;
    }

    /**
     * A singleton setter. Because abstract and static don't mix very well
     *
     * @param database The new database connection
     * @return the opened connection
     */
    public static DatabaseConn open(DatabaseConn database)
    {
        connection = database;
        return connection;
    }



    /**
     * Open a database connection
     *
     * @return true on success, false on failure
     */
    public abstract boolean connect();

    /**
     * Close a database connection
     *
     * @return true on success, false on failure
     */
    public abstract boolean close();

    /**
     * Sets the query string to be used in queries
     *
     * @param _query The query to be set
     */
    public void setQuery(Object _query){query = _query;}

    /**
     * Sets the variable array to be used in queries
     *
     * @param _vars The variable array to set
     */
    public void setVars(Object[] _vars){vars = _vars;}
    /**
     * Sets the variable array to be used in queries but allows single string input
     *
     * @param _vars The variable array to set
     */
    public void setVars(String _vars)
    {
        String[] varArray = {_vars};
        setVars(varArray);
    }

    /**
     * Unsets the variable array
     */
    public void unsetVars(){vars = null;}

    /**
     * Sets the query string as well as the variable array to be used in queries
     *
     * @param _query The query string to be loaded
     * @param _vars The variable array to be loaded
     */
    public void setQueryAndVars(Object _query, Object[] _vars)
    {
        setQuery(_query);
        setVars(_vars);
    }

    /**
     * Sets the query string to be used and clears the variable array
     *
     * @param _query The query string to be loaded
     */
    public void setQueryAndVars(Object _query)
    {
        setQuery(_query);
        unsetVars();
    }

    /**
     * Runs a query using the loaded query string and vars array
     *
     * @return An array of string values as returned from the database, null on failure
     */
    protected abstract Object[] query();

    /**
     * Runs a query using the loaded query and vars, specifically for Create/Update/Delete operations that don't provide output
     *
     * @return true on success, false on failure
     */
    public abstract boolean exec();

    /**
     * Runs a query while setting the variable array to the provided array
     *
     * @param _vars The variable array to load
     * @return A string array containing the query results
     */
    public Object[] query(Object[] _vars)
    {
        setVars(_vars);
        return query();
    }

    /**
     * Runs an asynchronous query, using datacall object to provide output
     *
     * @param datacall An object containing a defined callback function
     */
    public abstract void asyncQuery(DatabaseCallback datacall);

    /**
     * Sets the active variables and then runs an asynchronous query
     *
     * @param _vars The vars to be set for the query
     * @param datacall The object containing the callback function
     */
    public void asyncQuery(Object[] _vars, DatabaseCallback datacall)
    {
        setVars(_vars);
        asyncQuery(datacall);
    };


    /**
     * Deletes an item from the provided document, delete by ID supported only
     *
     * @param document The document/table to be deleted from
     * @param item The item to be deleted
     * @return true on success, false on failure
     */
    public abstract boolean delete(String document, Object item);

    /**
     * Deletes all items from a given document
     *
     * @param document The document/table to be truncated
     * @return true on success, false on failure
     */
    public abstract boolean truncate(String document);

    /**
     * Inserts an element into the given document
     *
     * @param document The document/table to be inserted into
     * @param item The item to be inserted
     * @return true on success, false on failure
     */
    public abstract boolean insert(String document, Object item);

    /**
     * Inserts a collection of elements into the given document
     *
     * @param document The document/table to be inserted into
     * @param items The array of items to be inserted.
     * @return true on full success, false on failure
     */
    public boolean insert(String document, Object[] items)
    {
        for(int i = 0; i < items.length; i++)
        {
            if(!insert(document, items[i]))
            {
                return false;
            }
        }
        return true;
    }


    /**
     * Updates an element within the given document with the values given
     *
     * @param document The document/table in which the item resides
     * @param id The id of the item to update
     * @param item The new values to be used
     * @return true on success, false on failure
     */
    public abstract boolean update(String document, String id, Object item);

    /**
     * Update multiple elements within a given document using the given ids
     *
     * @param document The document/table in which the items reside
     * @param ids The array of ids to be inserted
     * @param items The new values to be used
     * @return true on full success, false on any failure
     */
    public boolean update(String document, String[] ids, Object[] items)
    {
//        boolean success = true;
        for (int i = 0; i < ids.length; i++)
        {
            if(!update(document, ids[i], items[i]))
            {
                return false;
            }
        }
        return true;
    }

    public interface DatabaseCallback
    {
        public void onCallback(Object results);
    }







}
