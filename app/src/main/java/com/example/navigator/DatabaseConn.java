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

package com.example.navigator;

/**
 * Purpose: This class is an abstract database connection class
 *
 * Description : Provide a polymorphic parent class to use throughout the program
 */

public abstract class DatabaseConn {

//    protected Object database;
    protected String query;
    protected String[] vars;

    public DatabaseConn(){};

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
    public void setQuery(String _query){query = _query;}

    /**
     * Sets the variable array to be used in queries
     *
     * @param _vars The variable array to set
     */
    public void setVars(String[] _vars){vars = _vars;}

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
    public void setQueryAndVars(String _query, String[] _vars)
    {
        setQuery(_query);
        setVars(_vars);
    }

    /**
     * Sets the query string to be used and clears the variable array
     *
     * @param _query The query string to be loaded
     */
    public void setQueryAndVars(String _query)
    {
        setQuery(_query);
        unsetVars();
    }

    /**
     * Runs a query using the loaded query string and vars array
     *
     * @return An array of string values as returned from the database, null on failure
     */
    protected abstract String[] query();

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
    public String[] query(String[] _vars)
    {
        setVars(_vars);
        return query();
    }

    /**
     * Deletes an item from the provided document, delete by ID supported only
     *
     * @param document The document/table to be deleted from
     * @param item The item to be deleted
     * @return true on success, false on failure
     */
    public abstract boolean delete(String document, String item);

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
     * @param item The array of element attributes to be inserted
     * @return true on success, false on failure
     */
    public abstract boolean insert(String document, String[] item);

    /**
     * Inserts a collection of elements into the given document
     *
     * @param document The document/table to be inserted into
     * @param items The multidimensional array of items. items[0] is the first item, items[3][0] is the first attribute of the fourth item
     * @return true on success, false on failure
     */
    public abstract boolean insert(String document, String[][] items);

    /**
     * Updates an element within the given document with the values given
     *
     * @param document The document/table in which the item resides
     * @param id The id of the item to update
     * @param item The new values for the item to be set to
     * @return true on success, false on failure
     */
    public abstract boolean update(String document, String id, String[] item);

    /**
     * Update multiple elements within a given document using the given ids
     *
     * @param document The document/table in which the items reside
     * @param ids The array of ids to be inserted
     * @param items The multidimensional array of items. items[0] is the first item, items[3][0] is the first attribute of the fourth item
     * @return true on full success, false on any failure
     */
    public boolean update(String document, String[] ids, String[][] items)
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

    




}
