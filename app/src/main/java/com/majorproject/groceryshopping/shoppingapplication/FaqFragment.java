package com.majorproject.groceryshopping.shoppingapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FaqFragment extends Fragment {


    public FaqFragment() {
        // Required empty public constructor
    }
  public static FaqFragment getInstance()
  {
      FaqFragment FAQFragment=new FaqFragment();

      return FAQFragment;
  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faq, container, false);
    }

}
