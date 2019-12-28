package com.example.readcontacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ArrayList<Contact> mArrayList;
    ArrayList<String> contactName = new ArrayList<>();
    ArrayList<String> contactNumber = new ArrayList<>();


    private RecyclerView.LayoutManager layoutManager;
    private ContactAdpter contactAdpter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mArrayList = new ArrayList<>();
        mArrayList.clear();

        getContactList();
//        for(int i=0;i<contactName.size();i++){
//            Contact contact=new Contact();
//
//            contact.setName(contactName.get(i));
//            contact.setPhoneNumber(contactNumber.get(i));
//            mArrayList.add(contact);
//
//            Log.i("contact_name",contactName.get(i));
//
//        }


        recyclerView = findViewById(R.id.contact_id);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        contactAdpter = new ContactAdpter(mArrayList, this);
        recyclerView.setAdapter(contactAdpter);


    }


    private void getContactList() {
        String name;
        String phoneNo;
        mArrayList.clear();

        // Contact contact=new Contact();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                Contact contact = new Contact();
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                contactName.add(name);


                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactNumber.add(phoneNo);


                        Log.i("name", "Name: " + name);
                        Log.i("phone", "Phone Number: " + phoneNo);
                        contact.setName(name);

                        contact.setPhoneNumber(phoneNo);
                        mArrayList.add(contact);

                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);
       MenuItem searchItem=menu.findItem(R.id.action_search);
       SearchView searchView=(SearchView) searchItem.getActionView();
       searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               contactAdpter.getFilter().filter(newText);
               return false;
           }
       });

        return true;
    }
}
