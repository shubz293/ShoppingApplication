package com.majorproject.groceryshopping.shoppingapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainStoreActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
AddableInCart{



    private RecyclerView recyclerView;
    private MyListItemsAdapter adapter;

    private RecyclerView recyclerViewCart;
    private MyPlaceItemsAdapter adapterCart;
    private AlertDialog alertDialog;


    private TextView navHeaderName;
    private TextView navHeaderEmail;
    private TextView cartTotal;
    private NavigationView navigationView;
    private NavigationView navigationViewCart;
    private FloatingActionButton fab;
    private ProgressDialog progressDialog;
    private Button buttonPlaceOrder;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    private UserInformation userData;
    private DrawerLayout drawer;

    private Long orderID = 0L;

    private List<ListItem> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main_store);


        //app_bar_main store.xml
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emptyCart(view);
            }
        });


        // initilization
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationViewCart = (NavigationView) findViewById(R.id.nav_view_cart);
        navigationView.setNavigationItemSelectedListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("customer");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading User Data..");
        progressDialog.show();


        View cartView = navigationViewCart.getHeaderView(0);
        recyclerView = (RecyclerView) cartView.findViewById(R.id.cart_item_recycler_view);
        cartTotal = (TextView) cartView.findViewById(R.id.textViewTotal);
        buttonPlaceOrder = (Button) cartView.findViewById(R.id.button_place_order);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        // check user login state
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override //check user already logedin
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                } else {
                    finish();
                    startActivity(new Intent(MainStoreActivity.this, SelectLoginSignupActivity.class));
                }
            }
        };


        // get view from navigation view to access the elements in navHeader
        View view = navigationView.getHeaderView(0);
        navHeaderName = (TextView) view.findViewById(R.id.header_name);
        navHeaderEmail = (TextView) view.findViewById(R.id.header_email);
        // read data from database
        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateHeader(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPlaceOrderOnClick();
            }
        });


    }
    // update navigation header
    private void updateHeader(DataSnapshot dataSnapshot)
    {
        userData = dataSnapshot.getValue(UserInformation.class);

        FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.firebase_orderID))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        orderID = (Long) dataSnapshot.getValue();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        navHeaderName.setText(userData.getUserName());
        navHeaderEmail.setText(userData.getUserEmail());
        userData.printAll();
        navHomeClicked();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_store, menu);
        return true;
    }

    // option selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            navSettingsClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override // nav drawer button click actionPerformed
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) { navHomeClicked();
            // Handle the camera action
        } else if (id == R.id.nav_cart) { navCartClicked();

        } else if (id == R.id.nav_settings) { navSettingsClicked();

        } else if (id == R.id.nav_logout) { navLogoutClicked();

        } else if (id == R.id.nav_share) { navShareClicked();

        } else if (id == R.id.nav_faq) {  navFaqClicked();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navLogoutClicked()
    {
        if(firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();
            Toast.makeText(this, "Logout successful",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this, SelectLoginSignupActivity.class));
        }
    }

    private void navShareClicked()
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = getString(R.string.share_message);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    private void navHomeClicked()
    {
        fab.setVisibility(View.VISIBLE);
        HomeFragment homeFragment= HomeFragment.getInstance();
        FragmentManager manager= getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_store_relative_layout,homeFragment).commit();
    }

    private void navCartClicked()
    {
        if(!cartItems.isEmpty())
        {
            drawer.openDrawer(GravityCompat.END);
            adapter = new MyListItemsAdapter(cartItems, this);
            recyclerView.setAdapter(adapter);
        }

        else
            makeTextToast(" Cart is Empty ");
    }

    private void navSettingsClicked()
    {
        fab.setVisibility(View.INVISIBLE);
        SettingFragment settingFragment= SettingFragment.getInstance(userData);
        FragmentManager manager= getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_store_relative_layout,settingFragment).commit();
    }

    private void navFaqClicked()
    {
        fab.setVisibility(View.INVISIBLE);
        FaqFragment FAQFragment= FaqFragment.getInstance();
        FragmentManager manager= getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_store_relative_layout,FAQFragment).commit();
    }

    private void buttonPlaceOrderOnClick()
    {
        final PlacedOrder order = new PlacedOrder(String.valueOf(orderID),userData.getUserName(),userData.getUserAdderss(), getTotalIntegerPrice(), false, cartItems);


        View orderView = LayoutInflater.from(this).inflate(R.layout.dialog_place_order, null) ;
        recyclerViewCart = (RecyclerView) orderView.findViewById(R.id.placed_item_recycler_view);
        recyclerViewCart.setHasFixedSize(true);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        TextView textViewName = (TextView) orderView.findViewById(R.id.textPlaceOrderName);
        final EditText editTextAddress = (EditText) orderView.findViewById(R.id.editTextPlaceOrderAddress);

        textViewName.setText(userData.getUserName());
        editTextAddress.setText(userData.getUserAdderss());

        Button buttonBack = (Button) orderView.findViewById(R.id.placeOrderBack);
        final Button buttonPlaceOrder = (Button) orderView.findViewById(R.id.placeOrderPlace);

        adapterCart = new MyPlaceItemsAdapter(order, this);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        recyclerViewCart.setAdapter(adapterCart);

        dialog.setView(orderView);
        alertDialog = dialog.create();
        alertDialog.show();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setCustomerAddress(editTextAddress.getText().toString().trim());
                buttonCartPlaceOrderOnClick(order);
            }
        });
    }

    private void buttonCartPlaceOrderOnClick(PlacedOrder placedOrder)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog.setMessage("Saving Data..");
        progressDialog.show();
        databaseReference
                .child(getString(R.string.firebase_order_subclass))
                .child(orderID.toString())
                .setValue(placedOrder)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            makeTextToast("Item Saved Successfully");
                            progressDialog.dismiss();
                        }
                        else
                        {
                            makeTextToast("Data Upload Error");
                            progressDialog.dismiss();
                        }
                    }
                });
        orderID = orderID + 1;
        databaseReference.child(getString(R.string.firebase_orderID)).setValue(orderID);

    }

    // show snackbar on empty cart
    private void emptyCart(View view)
    {
        if(!cartItems.isEmpty())
        {
            drawer.openDrawer(GravityCompat.END);
            adapter = new MyListItemsAdapter(cartItems, this);
            recyclerView.setAdapter(adapter);
        }

        else
        Snackbar.make(view, "Cart is Empty", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void makeTextToast(String text)
    {
        System.out.println("  "+ text);
        Toast.makeText(this , text , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addToCart(ListItem item) {
        item.printAll();
        cartItems.add(item);
        makeTextToast("Item Added");
        cartTotal.setText(getTotalPrice());
    }

    private String getTotalPrice()
    {

        return String.format("Rs. %s.", String.valueOf(getTotalIntegerPrice()));
    }

    private int getTotalIntegerPrice()
    {
        int price = 0;
        for (int i =0; i < cartItems.size(); i++)
        {
            ListItem item = cartItems.get(i);
            if(item != null)
                price += Integer.parseInt(item.getPrice().replaceAll("[^0-9]", ""));
        }
        return price;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

}
