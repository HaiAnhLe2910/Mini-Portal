package com.example.fhict_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TokenFragment.OnFragmentInteractionListener {

    private DrawerLayout drawer;
    NavigationView navigationView;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       this.setTitle("FHICT");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (token == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TokenFragment()).commit();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_people:
                if(token!=null) {
                    navigationView.setCheckedItem(R.id.nav_people);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PeopleFragment()).commit();

                }
                else
                {
                    new AlertDialog.Builder(this)
                        .setTitle("Warning").setMessage("Please grant the necessary permissions to the application")
                            .setPositiveButton(android.R.string.yes, null).show();
                }

                break;
            case R.id.nav_schedule:
                if(token!=null) {
                    navigationView.setCheckedItem(R.id.nav_schedule);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScheduleFragment()).commit();

                }
                else {
                    new AlertDialog.Builder(this)
                            .setTitle("Warning").setMessage("Please grant the necessary permissions to the application")
                            .setPositiveButton(android.R.string.yes, null).show();
                }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true; //false-no item will be selected
    }

    @Override
    public void onFragmentInteraction(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }


}
