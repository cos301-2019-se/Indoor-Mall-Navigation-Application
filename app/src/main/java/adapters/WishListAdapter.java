package adapters;

import entities.CartProduct;
import entities.WishListProduct;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.widget.Toast;

import com.example.navigator.Product;
import com.example.navigator.R;
import com.example.navigator.utils.DatabaseConn;
import com.example.navigator.utils.Installation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.example.navigator.R.layout.wish_list_product_layout;

public class
WishListAdapter extends ArrayAdapter<CartProduct>{

    private Context context;
    private List<CartProduct> products;

    //Get device ID
    final String deviceId = Installation.id(getContext());

    DatabaseReference cartDBRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(deviceId);
    DatabaseReference wishDBRef = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(deviceId);

    //Query myQuery =
    // 0tempDBRef.orderByChild("id").equalTo(false);


    public WishListAdapter(Context context, List<CartProduct> products) {
        super(context, R.layout.wish_list_product_layout, products);
        this.context = context;
        this.products = products;
    }

    @NonNull
    //@Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(wish_list_product_layout, parent, false);
            viewHolder.textViewName = view.findViewById(R.id.textViewNameWL);
            viewHolder.textViewPrice = view.findViewById(R.id.textViewPriceWL);
            viewHolder.imageViewPhoto = view.findViewById(R.id.imageViewPhotoWL);
            viewHolder.deleteFromWL = view.findViewById(R.id.deleteWishListItem);
            viewHolder.addToCart = view.findViewById(R.id.addToCart);
            viewHolder.storeResult = view.findViewById(R.id.ShopResultWL);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final CartProduct product = products.get(position);
        viewHolder.textViewName.setText(product.getName());
        viewHolder.textViewPrice.setText("R " + product.getPrice());
        //viewHolder.imageViewPhoto.setImageResource(product.getPhoto());

        int shopImage = R.drawable.ic_store_black_24dp;
        final String tester = product.getStoreResult();

        if(tester.equals("Woolworths"))
        {
            shopImage = R.drawable.woolworths;
        }
        else if(tester.equals("Pick 'n Pay"))
        {
            shopImage = R.drawable.pnp;
        }
        else if(tester.equals("CNA"))
        {
            shopImage = R.drawable.cna;
        }

        viewHolder.storeResult.setImageResource(shopImage);

        Picasso.with(context).load(product.getImageUrl()).into(viewHolder.imageViewPhoto);
        //new DownloadImageTask(viewHolder.imageViewPhoto).execute(product.getImageUrl());
        final CartProduct currProduct = products.get(position);

        viewHolder.deleteFromWL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wishDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            DataSnapshot deviceSnapshot = dataSnapshot;
                            //Unique Key in database
                            Iterable<DataSnapshot> deviceChildren = deviceSnapshot.getChildren();
                            String sessionId = product.getId();
                            for (DataSnapshot productItem : deviceChildren) {
                                if(productItem.child("shopResult").exists())
                                {
                                    //Toast.makeText(getApplicationContext(),"It's set. " , Toast.LENGTH_LONG).show();
                                    String store = productItem.child("shopResult").getValue().toString();
                                    String productId = productItem.child("id").getValue().toString();
                                    if(store.equals(product.getStoreResult()) && productId.equals(sessionId))
                                    {
                                        String toDelete = productItem.getKey();
                                        DatabaseConn data = DatabaseConn.open();
                                        Toast.makeText(getContext(),product.getName()+ " removed from Cart ", Toast.LENGTH_LONG).show();
                                        data.delete("Wishlist",deviceId+"/"+toDelete);
                                        removeFromList(currProduct);


                                    }
                                }
                                else if(productItem.child("storeResult").exists())
                                {
                                    //Toast.makeText(getApplicationContext(),"It's set. " , Toast.LENGTH_LONG).show();
                                    String store = productItem.child("storeResult").getValue().toString();
                                    String productId = productItem.child("id").getValue().toString();
                                    if(store.equals(product.getStoreResult()) && productId.equals(sessionId))
                                    {
                                        String toDelete = productItem.getKey();
                                        DatabaseConn data = DatabaseConn.open();
                                        Toast.makeText(getContext(),product.getName()+ " removed from Cart ", Toast.LENGTH_LONG).show();
                                        data.delete("Wishlist",deviceId+"/"+toDelete);
                                        removeFromList(currProduct);
                                    }
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        viewHolder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartDBRef.push().setValue(product);
                Toast.makeText(getContext(),product.getName()+ " added to Cart. ", Toast.LENGTH_LONG).show();

                //Remove from Wishlist
                wishDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            DataSnapshot deviceSnapshot = dataSnapshot;
                            //Unique Key in database
                            Iterable<DataSnapshot> deviceChildren = deviceSnapshot.getChildren();
                            String sessionId = product.getId();
                            for (DataSnapshot productItem : deviceChildren) {
                                if(productItem.child("shopResult").exists())
                                {
                                    //Toast.makeText(getApplicationContext(),"It's set. " , Toast.LENGTH_LONG).show();
                                    String store = productItem.child("shopResult").getValue().toString();
                                    String productId = productItem.child("id").getValue().toString();
                                    if(store.equals(product.getStoreResult()) && productId.equals(product.getId()))
                                    {
                                        String toDelete = productItem.getKey();
                                        DatabaseConn data = DatabaseConn.open();

                                        data.delete("Wishlist",deviceId+"/"+toDelete);
                                        removeFromList(currProduct);
                                    }
                                }
                                else if(productItem.child("storeResult").exists())
                                {
                                    Toast.makeText(context.getApplicationContext(),"found it " , Toast.LENGTH_LONG).show();
                                    String store = productItem.child("storeResult").getValue().toString();
                                    String productId = productItem.child("id").getValue().toString();
                                    if(store.equals(product.getStoreResult()) && productId.equals(sessionId))
                                    {
                                        String toDelete = productItem.getKey();
                                        DatabaseConn data = DatabaseConn.open();

                                        data.delete("Wishlist",deviceId+"/"+toDelete);
                                        removeFromList(currProduct);

                                    }
                                }
                            }
                        }
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
        public static TextView textViewPrice;
        public static ImageView imageViewPhoto;
        public static Button deleteFromWL;
        public static Button addToCart;

        public static ImageView storeResult;
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
}
