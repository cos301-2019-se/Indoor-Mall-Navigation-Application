/**
 * File Name: FirebaseConn.java (path: app/src/main/java/com/example/navigator/utils/FirebaseConn.java)
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
 * Functional Description: Implements firebase for the database connection abstract class
 * Assumptions:
 */

package com.example.navigator.utils;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FirebaseConn extends DatabaseConn {
    private DatabaseReference firebaseConnection;

    /**
     * Opens the firebase connection and stores it for querying or resumes firebase connection
     *
     * @return true on success, false on fail
     */
    @Override
    public boolean connect() {
        if(firebaseConnection == null)
        {
            firebaseConnection = FirebaseDatabase.getInstance().getReference();
        }else
        {
            firebaseConnection.goOnline();
        }
        return (firebaseConnection != null);
    }

    /**
     * Disconnects from firebase database connection
     *
     * @return true always to satisfy super class
     */
    @Override
    public boolean close() {
        firebaseConnection.goOffline();
        return true;
    }

    @Override
    public void setQuery(Object _query) {
        super.setQuery(_query);
    }

    @Override
    public void setVars(Object[] _vars) {
        super.setVars(_vars);
    }

    @Override
    protected Object[] query() {
        return new Object[0];
    }

    @Override
    public boolean exec() {
        return false;
    }

    /**
     * Runs an asynchronous query and processes results using a callback function
     *
     * @param datacall An object containing a defined callback function
     */
    @Override
    public void asyncQuery(final DatabaseCallback datacall) {
        if(vars != null)
        {
            firebaseConnection.child(varChild()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    datacall.onCallback(dataSnapshot.getChildren());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else
        {
            //Throw an error maybe? I'm not sure
        }

    }

    /**
     * Deletes a single element from a provided document
     *
     * @param document The document/table to be deleted from
     * @param item The item to be deleted
     * @return
     */
    @Override
    public boolean delete(String document, Object item) {
        DatabaseReference doc = firebaseConnection.child(document);//set active document
        if(doc != null)
        {
            DatabaseReference child = doc.child(item.toString());//set active child
            if(child != null)
            {
                child.removeValue();//Delete it
                return true;
            }else
            {
                return false;
            }
        }else
        {
            return false;
        }
    }

    /**
     * Deletes a document
     *
     * @param document The document/table to be truncated
     * @return true on success, false on failure
     */
    @Override
    public boolean truncate(String document) {
        DatabaseReference doc = firebaseConnection.child(document);//Set active document
        if(doc != null)
        {
            doc.removeValue();//DELETE EVERYTHING (Sometimes referred to as a Bandelete)
            return true;
        }else
        {
            return false;
        }
    }

    /**
     * Inserts a single item into the specified document by selecting the document, creating a clean item, and setting that item to the provided array
     *
     * @param document The document/table to be inserted into
     * @param item The array of element attributes to be inserted
     * @return
     */
    @Override
    public boolean insert(String document, Object item) {
        DatabaseReference doc = firebaseConnection.child(document);//set active document
        if(doc != null)
        {
            DatabaseReference child = doc.push();//create new child
            if(child != null)
            {
                child.setValue(item);//set child values
                return true;
            }else
            {
                return false;
            }
        }else
        {
            return false;
        }


    }

    /**
     * Updates an element within the given document using the provided ID
     *
     * @param document The document/table in which the item resides
     * @param id The id of the item to update
     * @param item The new values to be used
     * @return
     */
    @Override
    public boolean update(String document, String id, Object item) {
        HashMap<String, Object> update = new HashMap<>();
        DatabaseReference doc = firebaseConnection.child(document);
        update.put("/" + id, item);
        doc.updateChildren(update);

        return true;

    }

    /**
     * Updates multiple elements within the given document using the provided ID
     *
     * @param document The document/table in which the item resides
     * @param id The ids of the items to update
     * @param item The new values to be used
     * @return
     */
    @Override
    public boolean update(String document, String id[], Object[] item) {
        HashMap<String, Object> update = new HashMap<>();
        DatabaseReference doc = firebaseConnection.child(document);
        for(int i = 0; i < id.length; i++)
        {
            update.put("/" + id[i], item[i]);
        }

        doc.updateChildren(update);

        return true;

    }

    /**
     * Helper function to convert var into a child directory string
     *
     * @return The string of vars concatenated with / between them
     */
    private String varChild()
    {
        String out = "";
        for(int i = 0; i < vars.length; i++)
        {
            out += vars[i].toString();
            if(i != vars.length -1)
            {
                out += "/";
            }
        }

        return out;
    }

}
