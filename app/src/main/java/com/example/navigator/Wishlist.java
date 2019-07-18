/**
 *
 *  File Name: Navigation.js (path: component/app/navigation.js)
 *  Version: 1.0
 *  Author: Brute Force - Database Management
 *  Project: Indoor Mall Navigation
 *  Organisation: DVT
 *  Copyright: (c) Copyright 2019 University of Pretoria
 *  Update History:*
 *
 *  Date        Author           Changes
 *  --------------------------------------------
 *  09/07/2019  Thabo Ntsoane    Original
 *
 *  Functional Description: This program file searches and navigates user to a specific shop
 *  Error Messages: Shop does not exist
 *  Constraints: Can only be used to navigate
 *  Assumptions: It is assumed that the user will be navigated to destination appropriately
 *
*/
package com.example.navigator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Wishlist extends Fragment {
    SearchView searchView;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String > adapter;
    Button sub;
    DatabaseReference ref;
    //Shop ObjShop, ObjShop1,ObjShop2,ObjShop3,ObjShop4,ObjShop5,ObjShop6,ObjShop7,ObjShop8,ObjShop9,ObjShop10,ObjShop11,ObjShop12,ObjShop13,ObjShop14,ObjShop15,ObjShop16,ObjShop17,ObjShop18,ObjShop19,ObjShop20,ObjShop21;

    public Wishlist() {
        // Required empty public constructor
    }

    /**
     * Purpose: onCreateView is a function that is created when the search view is loaded
     *
     * Description: When the page is loaded, the function pushes all the shop names to the Database and
     * populates the Database.
     *
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        return view;
    }
}


