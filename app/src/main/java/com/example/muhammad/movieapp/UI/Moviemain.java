package com.example.muhammad.movieapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;

import com.example.muhammad.movieapp.Model.Movie;
import com.example.muhammad.movieapp.Utilities.NameListener;
import com.example.muhammad.movieapp.R;


public class Moviemain extends AppCompatActivity implements NameListener {

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



