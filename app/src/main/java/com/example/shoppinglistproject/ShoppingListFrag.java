package com.example.shoppinglistproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class ShoppingListFrag extends Fragment{
    private RecyclerView recyclerView;
    private Button backBtn;
    SLFragListener listener;
    private FloatingActionButton plusBtn;
    private Adapter adapter;

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    int hour = calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);

    private MainActivityViewModel mainActivityViewModel ;
   // private ItemDataBase db = Room.databaseBuilder(getActivity().getApplicationContext(),ItemDataBase.class,"item-database").build();
   private List<Item> items = new ArrayList<>();
    @Override
    public void onAttach(@NonNull Context context) {
        try{
            this.listener = (ShoppingListFrag.SLFragListener)context;
        }catch(ClassCastException e){
            throw new ClassCastException("the class " +
                    context.getClass().getName() +
                    " must implements the interface 'FragAListener'");
        }
        super.onAttach(context);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu,menu);
    }

    public ShoppingListFrag() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // items = db.itemDao().getAllItems();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.resetlistBtn:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DefaultDialog alertDialog = new DefaultDialog();
                alertDialog.show(fm,"");
                break;
            case R.id.setReminderBtn:
                int newDay;
                DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        month = i1;
                        day = i2;
                        openTimePickDialog();
                    }
                },year, month, day);
                datePicker.show();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void openTimePickDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hour = i;
                minute = i1;
                Toast.makeText(getActivity().getApplicationContext(), "Reminder Set!",Toast.LENGTH_SHORT).show();
                listener.setReminder(month,day,hour,minute);

            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void loadData(View view) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ShoppingListSharedPreferences ", Context.MODE_PRIVATE);
        boolean state = sharedPreferences.getBoolean("state",false);
        if(state) view.setBackgroundColor(Color.parseColor("#F2AAE7"));
        else view.setBackgroundColor(Color.parseColor("#7CEAF8"));

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewid);
        backBtn = view.findViewById(R.id.backBtn);
        plusBtn = view.findViewById(R.id.floatingActionButton);
        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        mainActivityViewModel.init(getContext());
        loadData(view);
        mainActivityViewModel.getItems().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items2) {
                if(items2.isEmpty())
                    view.findViewById(R.id.emptyTxt).setVisibility(view.VISIBLE);
                else
                    view.findViewById(R.id.emptyTxt).setVisibility(view.GONE);
                adapter.getItems(items2);
                adapter.notifyDataSetChanged();

            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true).replace(R.id.mainFragmentContainerView, MainFrag.class, null,"MAINFRAG")
                        .commit();
                getParentFragmentManager().executePendingTransactions(); //starts the swap
            }
        });
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open create item dialog
                listener.onPlusClick();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //Read the existing items from data using ROOM
        adapter = new Adapter(getActivity().getSupportFragmentManager());
   //     Log.i("itemsPrint",tempItems.toString());
        recyclerView.setAdapter(adapter);



        return view;
    }


    public interface SLFragListener {
        public void onPlusClick();

        public void onResetClick();
        public void setReminder(int month,int day,int hour,int minute);

    }
}