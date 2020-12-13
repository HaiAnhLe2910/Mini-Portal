package com.example.fhict_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PeopleFragment extends Fragment implements PeopleAdapter.ItemClickListener {

    private Button mCallButton;
    private String token;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private PeopleAdapter mAdapter;
    private ItemTouchHelper itemTouchHelper;
    private List<PersonItem> arrayList;

    private ItemTouchHelper.SimpleCallback simpleCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT) {
                mAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ mAdapter.getPhoneNumber(position)));
                startActivity(intent);
            }
        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.people_fragment, container, false);
        getActivity().setTitle("Fontys staff");
        EditText searchPerson = (EditText) rootView.findViewById(R.id.searchPerson);
        searchPerson.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        new JSONTask().execute();
        arrayList = new ArrayList<>();
        mAdapter = new PeopleAdapter(arrayList, getActivity());
        mAdapter.setClickListener((PeopleAdapter.ItemClickListener) this);
        recyclerView.setAdapter(mAdapter);

        itemTouchHelper = new ItemTouchHelper(simpleCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return rootView;
    }

    private void filter(String text) {
        ArrayList<PersonItem> filteredList = new ArrayList<>();
        for(PersonItem item: arrayList) {
            if(item.getName().toLowerCase().contains(text.toLowerCase()))
                filteredList.add(item);
        }
        mAdapter.filterList(filteredList);
    }




    @Override
    public void onItemClick(View view, int position) {
       // Toast.makeText(getActivity(),  mAdapter.getItem(position) + "'s office is " + mAdapter.getOffice(position), Toast.LENGTH_SHORT).show();
        String email = "Email: " + mAdapter.getEmail(position);
        String office = "Office: " + mAdapter.getOffice(position);


        new AlertDialog.Builder(getActivity())
                .setTitle(mAdapter.getItem(position)).setMessage(email + "\n" + office)
                .setPositiveButton(android.R.string.yes, null).show();
    }

    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
        token = ((MainActivity) getActivity()).getToken();
    }




    private class JSONTask extends AsyncTask<String, Void, String> {
        String jsonString;
        HttpURLConnection connection = null;


        @Override
        protected String doInBackground(String... strings) {

            jsonString = null;
            try {
                URL url = new URL("https://api.fhict.nl/people");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.connect();
                InputStream is = connection.getInputStream();
                Scanner scn = new Scanner(is);
                jsonString = scn.useDelimiter("\\Z").next();
                return jsonString;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }


        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);

            try {
               // JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = new JSONArray(jsonString);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject personObject = jsonArray.getJSONObject(i);
                    if(personObject.has("telephoneNumber") & (personObject.has("office"))) {
                        String telephoneNumber = personObject.getString("telephoneNumber");
                        String givenName = personObject.getString("givenName");
                        String surName = personObject.getString("surName");
                        String office = personObject.getString("office");
                        String email = personObject.getString("mail");

                        arrayList.add(new PersonItem(givenName, surName, telephoneNumber, office, email));
                    }
                }

                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}

