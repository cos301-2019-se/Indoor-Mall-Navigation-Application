package adapters;

import entities.CartProduct;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import android.content.Context;
import android.widget.Toast;

import com.example.navigator.R;
import com.example.navigator.utils.DatabaseConn;
import com.example.navigator.utils.Installation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import static com.example.navigator.R.layout.cart_product_list_layout;



public class CartProductListAdapter extends ArrayAdapter<CartProduct> {
    private Context context;
    private List<CartProduct> products;


    //Get device ID
    final String deviceId = Installation.id(getContext());

    DatabaseReference cartDBRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(deviceId);
    DatabaseReference wishDBRef = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(deviceId);



    public CartProductListAdapter(Context context, List<CartProduct> products) {
        super(context, R.layout.cart_product_list_layout, products);
        this.context = context;
        this.products = products;
    }

    @NonNull
    //@Override
    public View getView(final int position, View view, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(cart_product_list_layout, parent, false);
            viewHolder.textViewName = view.findViewById(R.id.textViewName);
            viewHolder.textViewPrice = view.findViewById(R.id.textViewPrice);
            viewHolder.textViewQuantity = view.findViewById(R.id.textQuantity);
            viewHolder.imageViewPhoto =  view.findViewById(R.id.imageViewPhoto);
            viewHolder.totalPrice = view.findViewById(R.id.totalPrice);
            viewHolder.incrementQuantity = view.findViewById(R.id.incrementQuantity);
            viewHolder.decrementQuantity = view.findViewById(R.id.decrementQuantity);
            viewHolder.deleteCartProduct = view.findViewById(R.id.deleteCartItem);
            viewHolder.addToWishList = view.findViewById(R.id.addToWishlist);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final CartProduct product = products.get(position);

        viewHolder.textViewName.setText(product.getName());
        viewHolder.textViewQuantity.setText(product.getQuantity());
        viewHolder.textViewPrice.setText("R " + product.getPrice());
        viewHolder.totalPrice.setText(product.getTotalPrice());

        //Trying Drawable Method
        //viewHolder.imageViewPhoto.setImageDrawable(LoadImageFromUrl(product.getImageUrl()));
        Picasso.with(context).load(product.getImageUrl()).into(viewHolder.imageViewPhoto);
        //new DownloadImageTask(viewHolder.imageViewPhoto).execute(product.getImageUrl());

        final CartProduct currProduct = products.get(position);

        //Increasing Quantity through button
        viewHolder.incrementQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Updated quantity on display
                viewHolder.textViewQuantity.setText(product.increaseQuantity());
                viewHolder.totalPrice.setText(product.getTotalPrice());
                notifyDataSetChanged();

                //Query to find the ID
                Query myQuery = cartDBRef.orderByChild("id").equalTo(product.getId());

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnap : dataSnapshot.getChildren())
                        {
                            dataSnap.child("quantity").getRef().setValue(product.getQuantity());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                myQuery.addListenerForSingleValueEvent(valueEventListener);


            }
        });

        //Decrementing the quality
        viewHolder.decrementQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Updated quantity on display
                viewHolder.textViewQuantity.setText(product.decreaseQuantity());
                viewHolder.totalPrice.setText(product.getTotalPrice());
                notifyDataSetChanged();
                //Query to find the ID
                Query myQuery = cartDBRef.orderByChild("id").equalTo(product.getId());


                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnap : dataSnapshot.getChildren())
                        {
                            dataSnap.child("quantity").getRef().setValue(product.getQuantity());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                myQuery.addListenerForSingleValueEvent(valueEventListener);




            }
        });


        //final CartProduct currProduct = products.get(position);

        viewHolder.deleteCartProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Query myQuery = cartDBRef.orderByChild("id").equalTo(product.getId());

                myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnap : dataSnapshot.getChildren())
                        {
                            String toDelete = dataSnap.getKey();
                            DatabaseConn data = DatabaseConn.open();
                            Toast.makeText(getContext(),product.getName()+ " removed from Cart ", Toast.LENGTH_LONG).show();
                            data.delete("Cart",deviceId+"/"+toDelete);
                        }

                        removeFromList(currProduct);

                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





            }
        });

        viewHolder.addToWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wishDBRef.push().setValue(product);
                Toast.makeText(getContext(),product.getName()+ " added to WishList ", Toast.LENGTH_LONG).show();

                Query myQuery = cartDBRef.orderByChild("id").equalTo(product.getId());

                myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnap : dataSnapshot.getChildren())
                        {
                            String toDelete = dataSnap.getKey();
                            DatabaseConn data = DatabaseConn.open();

                            data.delete("Cart",deviceId+"/"+toDelete);
                        }
                        removeFromList(currProduct);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return view;
    }

    private void removeFromList(CartProduct cp) {
        int position = products.indexOf(cp);
        products.remove(position);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public static TextView textViewName;
        public static TextView textViewQuantity;
        public static TextView textViewPrice;
        public static ImageView imageViewPhoto;
        public static TextView totalPrice;
        public static Button incrementQuantity;
        Button decrementQuantity;
        Button deleteCartProduct;
        Button addToWishList;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String[] urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private Drawable LoadImageFromUrl(String url)
    {
        try{

            //Toast.makeText(context.getApplicationContext(),url, Toast.LENGTH_LONG).show();
            InputStream inStream = (InputStream) new URL(url).getContent();
            Drawable drawable = Drawable.createFromStream(inStream,"product name");
            return drawable;

        } catch (Exception E)
        {
            return null;
        }
    }


}
