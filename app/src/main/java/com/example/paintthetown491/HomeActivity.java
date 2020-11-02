package com.example.paintthetown491;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends Fragment
{
    private EditText srchBar, locationInput;
    private Spinner srchFilter;
    private Button srchButton;
    private String selectedFltr, location;
    private RecyclerView locResults;
    private ArrayList<Location> locations;
    private LocationResultsAdapter locationAdapter=null;

    // Yelp API key
    final String token = "ju1f5-H-moICivrIt7vJynoLtBo9yB20u3_A8iq4i7rw2x7aYsYk5Kl6QP5WFqD1ELwd3dlOiLGR157KQwUcIU1Kq0r9l66uU0EoxWd5z3daERREQpymXCuRiGRYX3Yx";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.frag_main, container, false);

        // bind UI elements to variable
        srchBar = view.findViewById(R.id.searchBar);
        locationInput = view.findViewById(R.id.locationInput);
        srchFilter = view.findViewById(R.id.searchFilter);
        srchButton = view.findViewById(R.id.searchButton);
        locResults=view.findViewById(R.id.results);
        locResults.setHasFixedSize(true);
        locResults.setLayoutManager(new LinearLayoutManager(getActivity()));

        //array to hold locations for recycler
        locations=new ArrayList<Location>();

        //location adapter for our recycler
        locationAdapter=new LocationResultsAdapter(locations);
        locResults.setAdapter(locationAdapter);
        //handles clicks for locations
        locationAdapter.setOnItemClickListener(new LocationResultsAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                //prepare location to be sent to popup
                Location loc=locations.get(position);
                Intent popup=new Intent(getContext(),LocationPopUp.class);
                popup.putExtra("location",loc);
                startActivity(popup);
            }
        });

        // array list stores filter options for dropdown list
        ArrayList<String> filterOptions = new ArrayList<>();
        filterOptions.add("Places");
        filterOptions.add("People");

        // set dropdown list with items from arraylist
        ArrayAdapter<String> srchAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, filterOptions);
        srchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        srchFilter.setAdapter(srchAdapter);

        // listener for dropdown selection
        srchFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // get the selected filter
                selectedFltr = srchFilter.getSelectedItem().toString();
                if (selectedFltr.equals("Places"))
                {
                    // make the location input field visible
                    locationInput.setVisibility(View.VISIBLE);
                }
                if (selectedFltr.equals("People"))
                {
                    // expand the width of search bar
                    expandSearchBarWidth();
                    // hide the location input field
                    locationInput.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // set on click listener for search button
        srchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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
                        // check if input field is empty
                        if(TextUtils.isEmpty(locationInput.getText()))
                        {
                            // set error
                            locationInput.setError("Location field cannot be blank!");
                            return;
                        }
                        // get the location entered
                        location = locationInput.getText().toString();
                        yelpSearch(srchBar.getText().toString(), location);
                    }
                    else
                    {
                        // search people
                        ProfileSearchActivity profileSearch = new ProfileSearchActivity();
                        Bundle searchText = new Bundle();
                        searchText.putString("Search Input", srchBar.getText().toString());
                        profileSearch.setArguments(searchText);
                        getParentFragmentManager().beginTransaction().replace(R.id.container_frag, profileSearch).commit();
                    }
                }
            }
        });
        return view;
    }

    public void expandSearchBarWidth() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) srchBar.getLayoutParams();
        layoutParams.width = 1050;
        srchBar.setLayoutParams(layoutParams);
    }

    // calls API to retrieve JSON data
    public void yelpSearch(String searchInput, String location)
    {

        String url = "https://api.yelp.com/v3/businesses/search?term=" + searchInput + "&location=" + location;

        OkHttpClient client = new OkHttpClient();
        // make a request
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + token)
                .build();

        // call Yelp API to fetch JSON data
        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                if (response.isSuccessful())
                {
                    //clears the list for each search
                    locations.clear();
                    //raw JSON string
                    final String responseData = response.body().string();

                    try
                    {
                        //create a JSON Object with the raw string
                        JSONObject json=new JSONObject(responseData);
                        //create array of businesses
                        JSONArray arr=json.getJSONArray("businesses");
                        //we need to place the name of each business in the resultNames arraylist
                        for(int i=0;i<arr.length();i++)
                        {
                            JSONObject loc=arr.getJSONObject(i).getJSONObject("location");
                            locations.add(new Location(arr.getJSONObject(i).getString("id"), arr.getJSONObject(i).getString("name"), arr.getJSONObject(i).getString("image_url"),loc.getString("address1")+" "+ loc.getString("address2")+","+loc.getString("city")+","+loc.getString("zip_code"),0,arr.getJSONObject(i).getString("phone")));
                        }
                    }

                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    // update textview UI
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if(locationAdapter!=null)
                            {
                                locationAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });
    }
}