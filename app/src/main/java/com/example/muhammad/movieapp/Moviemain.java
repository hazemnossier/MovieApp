package com.example.muhammad.movieapp;

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


public class Moviemain extends AppCompatActivity {

    public ArrayAdapter<String> movieAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviemain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FetchMovieURL Movietask = new FetchMovieURL();
        Movietask.execute("20");


        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w185//e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg")
                .into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_moviemain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchMovieURL extends AsyncTask<String, Void, Movie[]> {

        private final String LOG_TAG = FetchMovieURL.class.getSimpleName();


        private Movie[] getMovieDataFromJson(String MovieJsonStr, int Numberofmovies)
                throws JSONException {


            final String OWM_RESULT = "results";
            final String OWM_POSTER_PATH = "poster_path";
            final String OWM_DESCRIPTION = "overview";
            final String OWM_RATE = "vote_average";
            final String OWM_TITLE = "title";



            JSONObject movieJson = new JSONObject(MovieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OWM_RESULT);



            Movie[] resultStrs = new Movie[Numberofmovies];

            for (int i = 0; i < movieArray.length(); i++) {
                

                JSONObject movieOBJ = movieArray.getJSONObject(i);

                Movie m = new Movie();

                m.setImageUARL(movieOBJ.getString(OWM_POSTER_PATH));
                m.setDescraption(movieOBJ.getString(OWM_DESCRIPTION));
                m.setRate(movieOBJ.getString(OWM_RATE));
                m.setTitle(movieOBJ.getString(OWM_TITLE));

                resultStrs[i] = m;



            }

            for (Movie s : resultStrs) {
                   Log.v(LOG_TAG, "Movie entry: " + s);
            }
            return resultStrs;

        }


        protected Movie[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.

            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            String movieJsonStr = null;

             //http://api.themoviedb.org/3/movie/popular?api_key=42eed9bb6fe907c5e968b63395392f31
            String mode = "popular";
            String apiKey = "42eed9bb6fe907c5e968b63395392f31"; //BuildConfig.OPEN_WEATHER_MAP_API_KEY;

            try {

                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/"+mode;


                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon().appendQueryParameter(APPID_PARAM, BuildConfig.Movie_API_Key).build();

                URL url = new URL(builtUri.toString());


                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Movie JSON: " + movieJsonStr);
            } catch (IOException e) {

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }

            try {
                return getMovieDataFromJson(movieJsonStr, 20);
            } catch (JSONException e) {
                // Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Movie[] result) {
            if (result != null) {
                movieAdapter.clear();
                for (Movie movieStr : result) {
                    movieAdapter.add(movieStr);
                }
            }
        }
    }
}


