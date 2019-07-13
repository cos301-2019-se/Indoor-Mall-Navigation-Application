package com.example.navigator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Search extends Fragment {
    SearchView searchView;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String > adapter;

    public Search() {
        // Required empty public constructor
        //EditText edittext = findViewById(R.id.editText);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = (SearchView) view.findViewById(R.id.searchView);
        listView = (ListView) view.findViewById(R.id.lv1);

        list = new ArrayList<>();
        list.add("Apple");
        list.add("Banana");
        list.add("Pineapple");
        list.add("Orange");
        list.add("Lychee");
        list.add("Gavava");
        list.add("Peech");
        list.add("Melon");
        list.add("Watermelon");
        list.add("Papaya");

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

                /*if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(getContext(), "No Match found", Toast.LENGTH_LONG).show();
                }*/
                return false;
            }
        });

        return view;
    }
}


