package com.majorproject.groceryshopping.shoppingapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Shubhank Dubey on 07-04-2017.
 */

class MyPlaceItemsAdapter extends RecyclerView.Adapter<MyPlaceItemsAdapter.ViewHolder> {

    private PlacedOrder placedOrder;
    private Context context;

    public MyPlaceItemsAdapter(PlacedOrder placedOrder, Context context) {
        this.placedOrder = placedOrder;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.placed_order, parent, false);

        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        List<ListItem> items = placedOrder.getCartItem();
        ListItem item = items.get(position);
        holder.textViewName.setText(item.getItemName());
        holder.textViewPrice.setText(item.getPrice());

    }


    @Override
    public int getItemCount() {
        return placedOrder.getCartItem().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.card_view_name);
            textViewPrice = (TextView) itemView.findViewById(R.id.card_view_price);

        }
    }
}
