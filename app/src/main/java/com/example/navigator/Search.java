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
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Search extends Fragment {
    SearchView searchView;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String > adapter;
    Button sub;
    DatabaseReference ref;
    //Shop ObjShop, ObjShop1,ObjShop2,ObjShop3,ObjShop4,ObjShop5,ObjShop6,ObjShop7,ObjShop8,ObjShop9,ObjShop10,ObjShop11,ObjShop12,ObjShop13,ObjShop14,ObjShop15,ObjShop16,ObjShop17,ObjShop18,ObjShop19,ObjShop20,ObjShop21;

    public Search() {
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = (SearchView) view.findViewById(R.id.searchView);
        listView = (ListView) view.findViewById(R.id.lv1);
        list = new ArrayList<>();
        //list.add("Zara");
        ref = FirebaseDatabase.getInstance().getReference();

        //WRITING TO DATABASE
        /*ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String shopId = "";

                /*ObjShop = new Shop("Clothing","Exact",60);
                ObjShop1 = new Shop("Books","Exclusive Books",61);
                ObjShop2 = new Shop("Optometrists","EyeQ Optometrists",62);
                ObjShop3 = new Shop("Clothing","Fabiani",63);
                ObjShop4 = new Shop("Clothing","Factorie",64);
                ObjShop5 = new Shop("Clothing","Falco Milano",65);
                ObjShop6 = new Shop("Bank","FNB",66);
                ObjShop7 = new Shop("Food","Food Lover's Market",67);
                ObjShop8 = new Shop("Jewellery","Fossil",68);
                ObjShop9 = new Shop("Clothing","G Star",69);
                ObjShop10 = new Shop("Jewellery","Galaxy & Co",70);
                ObjShop11 = new Shop("Electronics","Game",71);
                ObjShop12 = new Shop("Liquor","Game Liquor",72);
                ObjShop13 = new Shop("Clothing","Gant",73);
                ObjShop14 = new Shop("Clothing","Guess",74);
                ObjShop15 = new Shop("Clothing","H&M",54);
                ObjShop16 = new Shop("Electronics","House & Home",75);
                ObjShop17 = new Shop("Electronics","HP Store",76);
                ObjShop18 = new Shop("Clothing","Identity",77);
                ObjShop19 = new Shop("Electronics","iStore",78);
                ObjShop20 = new Shop("Clothing","Jeep",79);
                ObjShop21 = new Shop("Clothing","Jet",80);

                ref.push().setValue(ObjShop);
                ref.push().setValue(ObjShop1);
                ref.push().setValue(ObjShop2);
                ref.push().setValue(ObjShop3);
                ref.push().setValue(ObjShop4);
                ref.push().setValue(ObjShop5);
                ref.push().setValue(ObjShop6);
                ref.push().setValue(ObjShop7);
                ref.push().setValue(ObjShop8);
                ref.push().setValue(ObjShop9);
                ref.push().setValue(ObjShop10);
                ref.push().setValue(ObjShop11);
                ref.push().setValue(ObjShop12);
                ref.push().setValue(ObjShop13);
                ref.push().setValue(ObjShop14);
                ref.push().setValue(ObjShop15);
                ref.push().setValue(ObjShop16);
                ref.push().setValue(ObjShop17);
                ref.push().setValue(ObjShop18);
                ref.push().setValue(ObjShop19);
                ref.push().setValue(ObjShop20);
                ref.push().setValue(ObjShop21);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        /**
         * Purpose: onCreateView function that pulls all the shop names from the Database
         *
         * Description: When the page is loaded, the function pulls all the names from the Database and
         * populates the Search drop down.
         *
         */
        ref.child("Shop").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String ShopName = snapshot.child("name").getValue().toString();
                    //String ShopName = snapshot.child("name").toString(); returns {key: name,value : ABSA
                    list.add(ShopName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /**
         * Purpose: onQueryTextSubmit is a function that searches the whole list for the typed out shop
         *
         * Description: This is a function that searches the whole list for the typed out shop
         *
         */
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                for(int i = 0; i < list.size(); i++){
                    if(list.get(i).toLowerCase().contains(query.toLowerCase())){
                        adapter.getFilter().filter(query);
                        listView.setAdapter(adapter);
                        break;
                    }
                    else{
                        listView.setAdapter(null);
                        Toast.makeText(getContext(), "No Match found", Toast.LENGTH_LONG).show();
                    }
                }

                /*if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(getContext(), "No Match found", Toast.LENGTH_LONG).show();
                }*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                for(int i = 0; i < list.size(); i++){
                    if(list.get(i).toLowerCase().contains(newText.toLowerCase())){
                        adapter.getFilter().filter(newText);
                        listView.setAdapter(adapter);
                        break;
                    }
                    else{
                        listView.setAdapter(null);
                        Toast.makeText(getContext(), "No Match found", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });

        return view;
    }
}


