package com.example.shoppinglistproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class MainFrag extends Fragment {
    TextView header;
    Button shoppinglistbtn, exitbtn;
    View view;
    Switch aSwitch;
    MainFragListener listener;
    boolean state;
    private SharedPreferences sharedPreferences;



    @Override
    public void onAttach(@NonNull Context context) {
        try{
            this.listener = (MainFragListener)context;
        }catch(ClassCastException e){
            throw new ClassCastException("the class " +
                    context.getClass().getName() +
                    " must implements the interface 'FragAListener'");
        }
        super.onAttach(context);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("ShoppingListSharedPreferences ", Context.MODE_PRIVATE);
    }

    private void loadData() {
       state = sharedPreferences.getBoolean("state",false);
       aSwitch.setChecked(state);
       if(state) view.setBackgroundColor(Color.parseColor("#F2AAE7"));
       else view.setBackgroundColor(Color.parseColor("#7CEAF8"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        aSwitch = view.findViewById(R.id.themeSwitch);
        loadData();
        // Inflate the layout for this fragment
        ImageView shoppingCartimg= view.findViewById(R.id.ShoppingCartimg2);
        shoppingCartimg.setImageResource(R.drawable.icons8);
        shoppinglistbtn = view.findViewById(R.id.SoppingListsbtn);
        exitbtn = view.findViewById(R.id.Exitbtn);
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                boolean newState = aSwitch.isChecked();
                if (newState) view.setBackgroundColor(Color.parseColor("#F2AAE7"));
                else view.setBackgroundColor(Color.parseColor("#7CEAF8"));
                saveData(newState);
            }
        });
        exitbtn.setOnClickListener(new HandleClick());
        shoppinglistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onShoppingListClick();
            }
        });
        header = view.findViewById(R.id.Welcometxtv);
        return view;
    }

    private void saveData(boolean newState) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("state",newState);
        editor.apply();
    }

    private class HandleClick implements View.OnClickListener {
        public void onClick(View arg0) {
            //****insert a dialog before exit
            FragmentManager fm = getActivity().getSupportFragmentManager();
            ExitDialog alertDialog = new ExitDialog();
            alertDialog.show(fm,"");
        }
    }


    public interface MainFragListener {
        public void onShoppingListClick();
    }
}