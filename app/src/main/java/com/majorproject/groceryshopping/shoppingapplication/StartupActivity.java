package com.majorproject.groceryshopping.shoppingapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class StartupActivity extends AppCompatActivity {

    // initialization
    private ViewPager viewPager;
    private int[] layouts;
    private LinearLayout dotsLayout;
    private Button next, skip;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_startup);


        ViewPagerAdapter viewPagerAdapter;

        layouts = new int[]
                {
                        R.layout.activity_best_product,
                        R.layout.activity_free_delivery,
                        R.layout.activity_premium_quality,
                        R.layout.activity_login_signup
                };
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        dotsLayout = (LinearLayout)findViewById(R.id.layoutDots);
        skip = (Button)findViewById(R.id.btnSkip);
        next = (Button)findViewById(R.id.btnNext);

        /*// check if user already loged in
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override //check user already logedin
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null)
                {
                    // user loged in


                }
                else
                {
                    // user not loged in
                    //start Login/signup activity
                    finish();
                    startActivity(new Intent(StartupActivity.this, SelectLoginSignupActivity.class));

                }
            }
        };*/


        // adding bottom dots
        addBottomDots(0);
        // change status bar color (write code)
        changeStatusBarColor();


        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(veiwListener);

        // on click listners for buttons
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               skipOnClickListner();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                nextOnClickListner();
            }
        });
    }

    //skip.onclick
    private void skipOnClickListner()
    {
        Intent intent = new Intent(StartupActivity.this,SelectLoginSignupActivity.class);
        startActivity(intent);
        finish();
    }

    // next.onclick
    private void nextOnClickListner()
    {
        int current = getItem(+1);
        if(current<layouts.length)
        {
            viewPager.setCurrentItem(current);
        }
        else
        {
            Intent intent = new Intent(StartupActivity.this,SelectLoginSignupActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //viewListner
    private ViewPager.OnPageChangeListener veiwListener = new ViewPager.OnPageChangeListener()
    {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addBottomDots(position);
            if(position == layouts.length-1)
            {
                next.setText("Proceed");
                skip.setVisibility(View.GONE);
            }
            else
            {
                next.setText("Next");
                skip.setVisibility(View.VISIBLE);
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    // addBottomDots()
    private void addBottomDots(int position)
    {
        TextView[] dots = new TextView[layouts.length];
        int colorActive, colorInactive;
        colorActive = getResources().getColor(R.color.dot_active_color);
        colorInactive = getResources().getColor(R.color.dot_inactive_color);
        dotsLayout.removeAllViews();

        for(int i = 0; i< dots.length; i++)
        {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive);
            dotsLayout.addView(dots[i]);
        }
        if(dots.length>0)
        {
            dots[position].setTextColor(colorActive);
        }
    }

    //changeStatusBarColor()
    private void changeStatusBarColor()
    {
        //write code
    }

    // getItem()
    private int getItem(int i)
    {
        //get item
        return viewPager.getCurrentItem() + i;
    }

    //ViewPagerAdapter Class
    private class ViewPagerAdapter extends PagerAdapter
    {
    private LayoutInflater layoutInflater;


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layouts[position],container,false);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View)object;
        container.removeView(view);
    }
}
    /*@Override
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
    }*/

}
