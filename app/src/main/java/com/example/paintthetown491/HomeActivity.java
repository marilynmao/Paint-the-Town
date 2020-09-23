package com.example.paintthetown491;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends Fragment
{
    private EditText srchBar;
    private Spinner srchFilter;
    private ImageButton srchButton;
    private String selectedFltr;
    private TextView resText;

    // Yelp API key
    final String token = "ju1f5-H-moICivrIt7vJynoLtBo9yB20u3_A8iq4i7rw2x7aYsYk5Kl6QP5WFqD1ELwd3dlOiLGR157KQwUcIU1Kq0r9l66uU0EoxWd5z3daERREQpymXCuRiGRYX3Yx";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.frag_main, container, false);

        // bind UI elements to variable
        srchBar = view.findViewById(R.id.searchBar);
        srchFilter = view.findViewById(R.id.searchFilter);
        srchButton = view.findViewById(R.id.searchButton);
        resText = view.findViewById(R.id.resultText);

        // array list stores filter options for dropdown list
        ArrayList<String> filterOptions = new ArrayList<>();
        filterOptions.add("Places");
        filterOptions.add("People");

        // set dropdown list with items from arraylist
        ArrayAdapter<String> srchAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, filterOptions);
        srchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        srchFilter.setAdapter(srchAdapter);

        // listener for dropdown selection
        srchFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // get the selected filter
                selectedFltr = srchFilter.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // set on click listener for search button
        srchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if search bar is empty
                if(TextUtils.isEmpty(srchBar.getText()))
                {
                    // set error
                    srchBar.setError("Search field cannot be blank!");
                    return;
                }
                else
                {
                    // if "places" is selected from dropdown list, call yelpSearch method
                    if(selectedFltr.equals("Places"))
                    {
                        yelpSearch(srchBar.getText().toString());
                    }
                    else
                    {
                        // testing
                        resText.setText(srchBar.getText().toString());

                        // ----- still need to retrieve user from DB to display in search results -----
                    }
                }
            }
        });

        return view;
    }

    // calls API to retrieve JSON data
    public void yelpSearch(String searchInput) {
        // location is temporarily set to long beach
        String url = "https://api.yelp.com/v3/businesses/search?term=" + searchInput + "&location=long beach";

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
    }
}