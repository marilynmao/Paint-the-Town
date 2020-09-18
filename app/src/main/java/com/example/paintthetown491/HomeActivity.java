package com.example.paintthetown491;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends Fragment
{
    private SearchView sv;
    private TextView resText;
    // Yelp API key
    final String token = "ju1f5-H-moICivrIt7vJynoLtBo9yB20u3_A8iq4i7rw2x7aYsYk5Kl6QP5WFqD1ELwd3dlOiLGR157KQwUcIU1Kq0r9l66uU0EoxWd5z3daERREQpymXCuRiGRYX3Yx";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.frag_main, container, false);

        sv = view.findViewById(R.id.searchView);
        resText = view.findViewById(R.id.resultText);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // location is temporarily set to long beach
                String url = "https://api.yelp.com/v3/businesses/search?term="+ query +"&location=long beach";

                OkHttpClient client = new OkHttpClient();
                // make a request
                Request request = new Request.Builder()
                        .url(url)
                        .header("Authorization", "Bearer " + token)
                        .build();

                // call Yelp API to fetch JSON data
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String responseData = response.body().string();

                            // update textview UI
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    resText.setText(responseData);
                                }
                            });
                        }
                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }
}