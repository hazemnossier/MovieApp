package com.example.muhammad.movieapp;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Moviemain extends AppCompatActivity implements NameListener{

    public ArrayAdapter<String> movieAdapter;
    public static boolean IsTwoPane = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviemain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TheMovieFragment movieFragment = new TheMovieFragment();

          movieFragment.setNameListener(this);
        if (getSupportFragmentManager().findFragmentById(R.id.Left_Frame) != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.Left_Frame,movieFragment ).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.Left_Frame, movieFragment).commit();
        }
        if (null != findViewById(R.id.Right_Frame)) {
            IsTwoPane = true;
        }


    }


    @Override
    public void setSelectedName(Movie mov) {
        if (!IsTwoPane) {

            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("movie", mov);
            startActivity(intent);
        } else {

            DetailActivityFragment mDetailsFragment= new DetailActivityFragment();
            Bundle extras= new Bundle();
            extras.putParcelable("movie", mov);
            mDetailsFragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction().replace(R.id.Right_Frame,mDetailsFragment,"").commit();
        }

    }
}



