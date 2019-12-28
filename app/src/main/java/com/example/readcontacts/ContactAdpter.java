package com.example.readcontacts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class ContactAdpter extends RecyclerView.Adapter<ContactAdpter.ContactHolder> implements Filterable {

    private ArrayList<Contact> contact_array;
    private ArrayList<Contact> contact_array_full;
    private Activity mcontext;

    public ContactAdpter(ArrayList<Contact> contact_array, Activity mcontext) {
        this.contact_array = contact_array;
        this.mcontext = mcontext;
        contact_array_full =new ArrayList<>(contact_array);
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact, parent, false);
        ContactHolder holder = new ContactHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactHolder holder, int position) {
        final Contact contact = contact_array.get(position);
        holder.txt_name.setText(contact.getName());
        holder.txt_phone.setText(contact.getPhoneNumber());

        holder.btn_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:" + holder.txt_phone.getText()));
                sendIntent.putExtra("sms_body", "Check out MoveTogether, I use it to  travel. Get it for free at https://movetogether.com/dl/");
                mcontext.startActivity(sendIntent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return contact_array.size();
    }


    class ContactHolder extends RecyclerView.ViewHolder {


        public TextView txt_name;

        public TextView txt_phone;
        public Button btn_invite;


        public ContactHolder(@NonNull View itemView) {
            super(itemView);

            //bt_main.setVisibility(View.GONE);

            txt_name = itemView.findViewById(R.id.contact_name);

            txt_phone = itemView.findViewById(R.id.contact_phone);
            btn_invite = itemView.findViewById(R.id.app_invite);


        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Contact> filter_array_list=new ArrayList<>();
            if(constraint==null||constraint.length()==0){
                filter_array_list.addAll(contact_array_full);

            }else {
                String filterPattern=constraint.toString().toLowerCase().trim();
                for(Contact item: contact_array_full){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filter_array_list.add(item);

                    }

                }
            }
            FilterResults results=new FilterResults();
            results.values=filter_array_list;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contact_array.clear();
            contact_array.addAll((ArrayList)results.values);
            notifyDataSetChanged();

        }
    };
}
