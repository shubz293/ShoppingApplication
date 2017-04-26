package com.majorproject.groceryshopping.shoppingapplication;

import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shubhank Dubey on 31-03-2017.
 */

class MyListItemsAdapter extends RecyclerView.Adapter<MyListItemsAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;
    private AddableInCart addableInCart;

    public MyListItemsAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    private void listItemOnClick(final ListItem listItem)
    {
        View mView = LayoutInflater.from(context).inflate(R.layout.selected_item_view, null) ;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = builder.setView(mView).create();
        alertDialog.show();

        TextView textViewName = (TextView) mView.findViewById(R.id.view_item_name);
        TextView textViewPrice = (TextView) mView.findViewById(R.id.view_item_price);
        TextView textViewOffer = (TextView) mView.findViewById(R.id.view_item_offer);
        ImageView imageViewImage = (ImageView) mView.findViewById(R.id.view_item_image);

        textViewName.setText(listItem.getItemName());
        textViewOffer.setText(listItem.getOffer());
        textViewPrice.setText(listItem.getPrice());
        Picasso.with(context)
                .load(listItem.getImageUrl())
                .fit().centerCrop()
                .into(imageViewImage);



        Button backButton = (Button) mView.findViewById(R.id.button_back);
        Button addToCartButton = (Button) mView.findViewById(R.id.button_addToCart);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddableInCart().addToCart(listItem);
                alertDialog.dismiss();
            }
        });

    }

    public void registerCart(AddableInCart obj)
    {
        setAddableInCart(obj);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);

        holder.textViewName.setText(listItem.getItemName());
        holder.textViewOffer.setText(listItem.getOffer());
        holder.textViewPrice.setText(listItem.getPrice());
        Picasso.with(context)
                .load(listItem.getImageUrl())
                .fit().centerCrop()
                .into(holder.imageViewImage);


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemOnClick(listItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName , textViewPrice, textViewOffer;
        ImageView imageViewImage;
        private LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.list_item_name);
            textViewPrice = (TextView) itemView.findViewById(R.id.list_item_price);
            textViewOffer = (TextView) itemView.findViewById(R.id.list_item_offer);
            imageViewImage = (ImageView) itemView.findViewById(R.id.list_item_image_view);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.list_item_linear_layout);

        }
    }


// setter and getter
    public AddableInCart getAddableInCart() {
        return addableInCart;
    }

    public void setAddableInCart(AddableInCart addableInCart) {
        this.addableInCart = addableInCart;
    }


}
