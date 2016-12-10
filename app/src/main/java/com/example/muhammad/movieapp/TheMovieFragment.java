package com.example.muhammad.movieapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
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

public class TheMovieFragment extends Fragment {

    GridView gridView;
    private NameListener mListener;
    private MovieAdapter mMovieAdapter;
    private ArrayList<Movie> Mmovie = new ArrayList<>();
    ArrayList<Movie>arrayList_Favs=new ArrayList<>();
    private View view;

    String MYAPI ="42eed9bb6fe907c5e968b63395392f31";


    public TheMovieFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    void setNameListener(NameListener nameListener) {
        this.mListener = nameListener;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_moviemain, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mMovieAdapter = new MovieAdapter(getActivity(),Mmovie);


        gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(mMovieAdapter);
        FetchMovieURL movieTask = new FetchMovieURL(mMovieAdapter, rootView);
        movieTask.execute("https://api.themoviedb.org/3/movie/popular?api_key=" + MYAPI);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie;
                if(Mmovie.size()==0)
                    movie = arrayList_Favs.get(position);
                else
                    movie = Mmovie.get(position);
                mListener.setSelectedName(movie);
            }
        });
        return rootView;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FetchMovieURL mmovietask = new FetchMovieURL(mMovieAdapter, view);
        int id = item.getItemId();


        Mmovie.clear();
        gridView.setAdapter(mMovieAdapter);
        mMovieAdapter.notifyDataSetChanged();

       /* if (id == R.id.action_favourite) {
            i will add the data base stuff here
        }*/

        if (id == R.id.action_favourite) {
            MovieDatabase movieDatabase = new MovieDatabase(getActivity());
            Mmovie.clear();
            mMovieAdapter.notifyDataSetChanged();
            ArrayList<Movie> test = movieDatabase.getFavsMovies();
            for (int i = 0; i < test.size(); i++) {
                Mmovie.add(test.get(i));
            }
            mMovieAdapter = new MovieAdapter(getContext(), Mmovie);
            mMovieAdapter.notifyDataSetChanged();
            gridView.setAdapter(mMovieAdapter);
        }


        if (id == R.id.action_sort_by_toprated) {
            mmovietask.execute("https://api.themoviedb.org/3/movie/top_rated?api_key=" + MYAPI);
        } else if (id == R.id.action_sort_by_popular) {
            mmovietask.execute("https://api.themoviedb.org/3/movie/popular?api_key=" +   MYAPI);
        }



        return super.onOptionsItemSelected(item);
    }




    public class FetchMovieURL extends AsyncTask<String, Void, ArrayList<Movie>> {

        private final String LOG_TAG = FetchMovieURL.class.getSimpleName();
        private MovieAdapter mMovieAdapter;
        private View view;

        FetchMovieURL(MovieAdapter movieadapter, View view)
        {
            this.mMovieAdapter = movieadapter;
            this.view = view;
        }

        private ArrayList<Movie> getMovieDataFromJson(String MovieJsonStr)
                throws JSONException {


            final String OWM_RESULT = "results";
            final String OWM_ID = "id";
            final String OWM_POSTER_PATH = "poster_path";
            final String OWM_DESCRIPTION = "overview";
            final String OWM_RELEASE_DATE = "release_date";
            final String OWM_RATE = "vote_average";
            final String OWM_TITLE = "title";




            JSONObject movieJson = new JSONObject(MovieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OWM_RESULT);



            int numMovies = movieArray.length();
            for (int i = 0; i < numMovies; i++) {
                // Get the JSON object representing the movie
                JSONObject Data = movieArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setTitle(Data.getString(OWM_TITLE));
                movie.setId(Data.getString(OWM_ID));
                movie.setRating(Data.getString(OWM_RATE));
                movie.setReleaseDate(Data.getString(OWM_RELEASE_DATE));
                movie.setSynopsis(Data.getString(OWM_DESCRIPTION));
                movie.setPosterPath(Data.getString(OWM_POSTER_PATH));
                Mmovie.add(movie);
            }
            return Mmovie;

        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
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
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
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

                    return null;
                }
                movieJsonStr = buffer.toString();
            } catch (Exception e) {
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
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the movie list.
            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            if (result != null) {
                super.onPostExecute(result);
                mMovieAdapter.notifyDataSetChanged();
            }
        }
    }


}


