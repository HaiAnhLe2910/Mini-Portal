package com.example.fhict_app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class ScheduleFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{


    private String token;
    private List<CourseItem> courses;
    DatePickerDialog datePickerDialog;
    private String startDate;
    private Button btnDatePicker;
    private TextView tvStartDate;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ScheduleAdapter mAdapter;

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        startDate  = date.getDateInstance().format(c.getTime());
        Log.d("to be shown", startDate);
        tvStartDate.setText(startDate);
        courses.clear();
        new JSONTask().execute();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.schedule_fragment, container, false);
        getActivity().setTitle("My schedule");
        final Calendar myCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(), this, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

        btnDatePicker = (Button)rootView.findViewById(R.id.btnDatePicker);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        //start Json

        tvStartDate=(TextView)rootView.findViewById(R.id.tvStartDate);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_course);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        courses=new ArrayList<>();
        mAdapter = new ScheduleAdapter(courses, getActivity());
        recyclerView.setAdapter(mAdapter);

        return rootView;
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
                URL url = new URL("https://api.fhict.nl/schedule/Class/e-s72?days=1&start=" + startDate);
                Log.d("START DATE", startDate);
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
                if(jsonString !=null) {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject courseObject = jsonArray.getJSONObject(i);
                        String subject = courseObject.getString("subject");
                        String room = courseObject.getString("room");
                        String teacher = courseObject.getString("teacherAbbreviation");
                        String start = courseObject.getString("start");
                        String end = courseObject.getString("end");
                        courses.add(new CourseItem(subject, room, teacher, start, end));

                    }

                    mAdapter.notifyDataSetChanged();
                }
                else
                {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("No content").setMessage("No schedule for class e-S72 today").setPositiveButton(android.R.string.yes, null).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


}
