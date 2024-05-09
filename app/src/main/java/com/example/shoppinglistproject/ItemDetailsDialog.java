package com.example.shoppinglistproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ItemDetailsDialog extends DialogFragment {
        EditText itemName;
        EditText quantity;
        IDListener listener;
        String newName,oldName;
        View view;
        String quant;
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.items_details_layout, container);
        }
        @Override
        public void onAttach(@NonNull Context context) {
            try{
                this.listener = (ItemDetailsDialog.IDListener)context;
            }catch(ClassCastException e){
                throw new ClassCastException("the class " +
                        context.getClass().getName() +
                        " must implements the interface 'FragAListener'");
            }
            super.onAttach(context);
        }

    public ItemDetailsDialog(String itemName, String quantity,View v ) {
        newName = itemName;
        quant = quantity;
        view = v;
    }

    @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

        }

        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            View mylayout = getActivity().getLayoutInflater().inflate(R.layout.items_details_layout,null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setView(mylayout);
            itemName = mylayout.findViewById(R.id.editItemName);
            quantity = mylayout.findViewById(R.id.editQuantity);
            itemName.setText(newName);
            oldName = itemName.getText().toString();
            quantity.setText(quant);
            alertDialogBuilder.setTitle("Update an Item");
            alertDialogBuilder.setPositiveButton("UPDATE",  new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //update i
                    String newName2 = itemName.getText().toString();
                    String quan = quantity.getText().toString();
                    listener.updateItem(oldName,newName2,quan);


                }
            });
            alertDialogBuilder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //delete the item from room
                    String newName2 = itemName.getText().toString();
                    listener.deleteItem(newName2);
                }

            });
            alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Do nothing
                }

            });

            return alertDialogBuilder.create();
            // return super.onCreateDialog(savedInstanceState);
        }


    public interface IDListener {
            public void updateItem(String oldName,String newName,String quantity);
            public void deleteItem(String itemName);
    }
}


