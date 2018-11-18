package id.ac.poltek.mhafidzilham.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import android.util.Log;

import id.ac.poltek.mhafidzilham.retrofit.Rest.ApiClient;
import id.ac.poltek.mhafidzilham.retrofit.Rest.ApiInterface;
import id.ac.poltek.mhafidzilham.retrofit.Adapters.MovieAdapter;
import id.ac.poltek.mhafidzilham.retrofit.Models.Movie;
import id.ac.poltek.mhafidzilham.retrofit.Models.MovieResponse;


import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final static String API_KEY = "384e685b932cf36cfc37d8a8a3f610cc";
    RecyclerView recyclerView;
    List<Movie> movies;
    String judul,subtitle,rating,deskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY first from themoviedb.org",
                    Toast.LENGTH_LONG).show();
            return;
        }

        recyclerView =  findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadData();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(MainActivity.this, " Posisi Film ke : "+position, Toast.LENGTH_LONG).show();
//                Intent i = new Intent(getApplicationContext(),DetailActivity.class);
//                startActivity(i);
            }
            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, " Posisi Film ke : "+position, Toast.LENGTH_LONG).show();
            }
        }));
    }

    private void loadData(){
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse>call, Response<MovieResponse> response) {
                int statusCode = response.code();
                movies = response.body().getResults();
                recyclerView.setAdapter(new MovieAdapter(movies, R.layout.list_item_movie, getApplicationContext()));
            }
            @Override
            public void onFailure(Call<MovieResponse>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
}
