package com.example.navigator;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.DEFAULT_KEYS_DIALER;
import static com.example.navigator.MainActivity.TAG;

/**
 * A simple {@link Fragment} subclass.
 */

public class Cart extends Fragment {
    private Context context = null;
    Button  fetch;
    private FirebaseAuth firebaseAuth;
    TextView demoValue;
    ListView cartList;

    DatabaseReference rootRef,demoRef;
    public Cart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        fetch = (Button) view.findViewById(R.id.fetch);
        demoValue = (TextView) view.findViewById(R.id.tvValue);
        rootRef = FirebaseDatabase.getInstance().getReference();
        //cartList = view.findViewById(R.id.cartList);

        //database reference pointing to Product node
        demoRef = rootRef.child("Product");
        //final TableLayout myTable = (TableLayout)view.findViewById(R.id.);

        final TableLayout myTable = (TableLayout) view.findViewById(R.id.myTableLayout);
        demoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String productName = snapshot.child("name").getValue().toString();
                    String price = snapshot.child("price").getValue().toString();
                    String priceProduct = productName + " R"+ price;
                    //String ShopName = snapshot.child("name").toString(); returns {key: name,value : ABSA
                    //list.add(priceProduct);

                    //for (int i = 0; i <2; i++) {

                        TableRow tableRow = new TableRow(getContext());

                        // Set new table row layout parameters.
                        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                        tableRow.setLayoutParams(layoutParams);

                        // Add a TextView in the first column.
                        TextView name = new TextView(getContext());
                        name.setText(productName);
                        tableRow.addView(name);

                        // Add a TextView in the first column.
                        TextView aPrice = new TextView(getContext());
                        aPrice.setText(priceProduct);
                        tableRow.addView(aPrice);

                        // Add a button in the second column
                        Button button = new Button(getContext());
                        button.setText("Delete");
                        tableRow.addView(button);

                        // Add a checkbox in the third column.
                        //CheckBox checkBox = new CheckBox(context);
                        //checkBox.setText("Check it");
                        //myTable.addView(checkBox, 2);

                        myTable.addView(tableRow,count);
                        count++;
                    //}



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                demoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List list = new ArrayList<>();
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                        for(int i = 0; i < map.size(); i++){
                            list.add(map);
                            i++;
                        }



                        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_list_item_1, list);
                        cartList.setAdapter(adapter);

                        Log.d(TAG, "Barcodes: " + list);

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }

                });
            }


        });
        return view;
    }


}
