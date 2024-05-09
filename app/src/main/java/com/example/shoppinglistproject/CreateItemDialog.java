package com.example.shoppinglistproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CreateItemDialog extends DialogFragment {
    EditText itemName;
    EditText quantity;
    CIDListener listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_item_layout, container);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        try{
            this.listener = (CreateItemDialog.CIDListener)context;
        }catch(ClassCastException e){
            throw new ClassCastException("the class " +
                    context.getClass().getName() +
                    " must implements the interface 'FragAListener'");
        }
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View mylayout = getActivity().getLayoutInflater().inflate(R.layout.create_item_layout,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(mylayout);
        itemName = mylayout.findViewById(R.id.itemNameTxt);
        quantity = mylayout.findViewById(R.id.quantityField);
        alertDialogBuilder.setTitle("Add an Item");
        alertDialogBuilder.setPositiveButton("Create",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //add new item to the list
                String name = itemName.getText().toString();
                String quan = quantity.getText().toString();
                if (name.isEmpty() || quan.isEmpty()) return;
                else listener.newItemCreated(name,quan);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
            }

        });

        return alertDialogBuilder.create();
        // return super.onCreateDialog(savedInstanceState);
    }

    public interface CIDListener {
        public void newItemCreated(String Name,String Quantity);
    }
}

