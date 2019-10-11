package com.example.navigator.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.navigator.R;
import com.example.navigator.Scan;

import java.util.ArrayList;

public class SearchDialog extends Dialog implements
        android.view.View.OnClickListener{

    private Context context;
    private Dialog d;
    private SearchView searchView;
    private ListView listView;
    private TextView close;
    private ListAdapter adapter;
    private ArrayList<String> list;
    private Button shopResult;

    public SearchDialog(Context a, ArrayList<String> list, Button shopResult){
        super(a);
        this.context = a;
        this.list = list;
        this.shopResult = shopResult;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_dialog);

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(searchList(query, list))
                {
                    listView.setAdapter(adapter);
                    return true;
                }
                else
                {
                    listView.setAdapter(null);
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(searchList(newText, list))
                {
                    listView.setAdapter(adapter);
                    return true;
                }
                else
                {
                    listView.setAdapter(null);
                    return false;
                }
            }
        });

        listView = findViewById(R.id.lv1);
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedShop = listView.getItemAtPosition(position).toString() ;
                Scan.activeShopIndex = position;
                //navigateButton.setVisibility(View.VISIBLE);
                shopResult.setText(selectedShop);
                dismiss();
            }
        });

        close = findViewById(R.id.dismiss_search);
        close.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dismiss_search:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    public boolean searchList(String needle, ArrayList<String> haystack)
    {
        boolean foundNeedle = false;
        ArrayList<String> needleStack = new ArrayList<>();

        for(int i = 0; i < haystack.size(); i++){
            if(haystack.get(i).toLowerCase().contains(needle.toLowerCase())){

                if(!needleStack.contains(haystack.get(i))){
                    needleStack.add(haystack.get(i));
                }
                foundNeedle = true;
            }
        }

        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, needleStack);

        return foundNeedle;
    }

}
