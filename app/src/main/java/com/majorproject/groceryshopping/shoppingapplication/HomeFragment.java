package com.majorproject.groceryshopping.shoppingapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyListItemsAdapter adapter;

    DatabaseReference databaseReference;

    private List<ListItem> listItems = new ArrayList<>();
    private ProgressBar progressBar ;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment getInstance()
    {
        HomeFragment homeFragment = new HomeFragment();


        return  homeFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.nav_home_recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = (ProgressBar) view.findViewById(R.id.myItemProgressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);


        getDataSnapshot();

        return view;
    }



    private void setListData(DataSnapshot snap)
    {
        System.out.println(snap);
        try {
//
            for (int i=0; i<= snap.getChildrenCount(); i++)
            {
                String s=String.valueOf(i);
                ListItem item = snap.child(s).getValue(ListItem.class);
                if(item != null){
                ListItem listItem = new ListItem();
                listItem.setSellerName(item.getSellerName());
                listItem.setOffer(item.getOffer());
                listItem.setPrice(item.getPrice());
                listItem.setItemName(item.getItemName());
                listItem.setImageUrl(item.getImageUrl());
                listItems.add(listItem);
                }
            }
        } catch (Exception e) {
            System.out.println("List Data Exception   " + snap);
            // makeTextToast("List Data Error");
            e.printStackTrace();
        }
        finally {
            // if(listItems == null)
            //   makeTextToast("Some Error List Empty");
            makeRecyclerView();
        }

    }

    private void getDataSnapshot()
    {
        final String SUBCLASS = getString(R.string.firebase_items_subclass);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        System.out.println("read block");
        databaseReference.child(SUBCLASS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("data block");
                setListData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        System.out.println("return block");
    }

    private void makeRecyclerView()
    {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        if(listItems != null) {
            adapter = new MyListItemsAdapter(listItems, getActivity());
            AddableInCart obj = (MainStoreActivity)getActivity();
            adapter.registerCart(obj);
            recyclerView.setAdapter(adapter);
        }
    }




}
