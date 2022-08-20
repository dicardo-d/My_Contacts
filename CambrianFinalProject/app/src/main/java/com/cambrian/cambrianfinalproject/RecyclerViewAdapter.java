package com.cambrian.cambrianfinalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private MainActivity context;
    private List<Contact> contacts;
    private RecyclerAdapterInterface adapterInterface;

    public RecyclerViewAdapter(MainActivity context) {
        this.context = context;
        adapterInterface = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false));
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        final Contact deletedContact = contacts.get(position);
        contacts.remove(position);
        adapterInterface.deleteContact(deletedContact);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Contact model = contacts.get(position);
        holder.contactTextView.setText(model.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterInterface.contactDidTap(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts == null ? 0 : contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView contactTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactTextView = itemView.findViewById(R.id.textView);
        }
    }
}
