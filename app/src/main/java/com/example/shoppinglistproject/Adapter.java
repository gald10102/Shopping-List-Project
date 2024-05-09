package com.example.shoppinglistproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{

   public List<Item> items;

   FragmentManager fm;
   View SLF ;

   private Integer selectedPos= RecyclerView.NO_POSITION;

   public Adapter(FragmentManager f){
       fm=f;
   }
    public class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener{
        // Define the views in your list item layout
        TextView itemName;
        TextView itemQuantity;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            // Initialize the views
            itemName = itemView.findViewById(R.id.ItemNameTxt);
            itemQuantity = itemView.findViewById(R.id.quantitytxt);
        }

        @Override
        public boolean onLongClick(View view) {
            //open item details dialog
            ItemDetailsDialog createDialog = new ItemDetailsDialog(itemName.getText().toString(),itemQuantity.getText().toString(),view);
            createDialog.show(fm,"");
            return true;
        }
    }
    @NonNull
    @Override
    public Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.singlerow, parent, false);
        SLF = inflater.inflate(R.layout.fragment_shopping_list, parent, false);
        Adapter.MyViewHolder vh = new MyViewHolder(itemView);
        // Create and return a new instance of MyViewHolder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
       //holder.itemView.setBackgroundColor(selectedPos == position ? Color.WHITE : Color.TRANSPARENT);
       //holder.itemView.setSelected(selectedPos==position);
       holder.itemName.setText(items.get(position).getName());
       holder.itemQuantity.setText(items.get(position).getQuantity());
//       if(items.isEmpty())
//           SLF.findViewById(R.id.emptyTxt).setVisibility(View.VISIBLE);
//       else
//           SLF.findViewById(R.id.emptyTxt).setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
       if(items==null) return 0;
       else return items.size();
    }

    public void getItems(List<Item> items2){
       items = items2;
    }
}
