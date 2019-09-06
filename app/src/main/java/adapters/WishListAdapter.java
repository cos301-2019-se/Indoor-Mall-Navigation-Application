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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.example.navigator.R.layout.wish_list_product_layout;

public class WishListAdapter extends ArrayAdapter<CartProduct>{

    private Context context;
    private List<CartProduct> products;

    //Get device ID
    final String deviceId = Installation.id(getContext());

    DatabaseReference cartDBRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(deviceId);
    DatabaseReference wishDBRef = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(deviceId);

    //Query myQuery = tempDBRef.orderByChild("id").equalTo(false);


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

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final CartProduct product = products.get(position);
        viewHolder.textViewName.setText(product.getName());

        viewHolder.textViewPrice.setText("R " + product.getPrice());
        //viewHolder.imageViewPhoto.setImageResource(product.getPhoto());

        new DownloadImageTask(viewHolder.imageViewPhoto).execute(product.getImageUrl());




        final CartProduct currProduct = products.get(position);

        viewHolder.deleteFromWL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query myQuery = wishDBRef.orderByChild("id").equalTo(product.getId());

                myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnap : dataSnapshot.getChildren())
                        {
                            String toDelete = dataSnap.getKey();
                            DatabaseConn data = DatabaseConn.open();
                            Toast.makeText(getContext(),product.getName()+ " removed from Wishlist", Toast.LENGTH_LONG).show();
                            data.delete("Wishlist",deviceId+"/"+toDelete);
                        }

                        removeFromList(currProduct);

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
                Query myQuery = wishDBRef.orderByChild("id").equalTo(product.getId());

                myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnap : dataSnapshot.getChildren())
                        {
                            String toDelete = dataSnap.getKey();
                            DatabaseConn data = DatabaseConn.open();
                            //Toast.makeText(getContext(),product.getName()+ " removed from Wishlist", Toast.LENGTH_LONG).show();
                            data.delete("Wishlist",deviceId+"/"+toDelete);
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
        public static TextView textViewPrice;
        public static ImageView imageViewPhoto;
        public static Button deleteFromWL;
        public static Button addToCart;

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
