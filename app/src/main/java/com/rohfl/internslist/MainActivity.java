package com.rohfl.internslist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rohfl.internslist.adapter.InternshipsAdapter;
import com.rohfl.internslist.adapter.OnLastItemListener;
import com.rohfl.internslist.model.Internship;
import com.rohfl.internslist.singleton.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    long currentPage = 1;
    RecyclerView internshipRv;
    InternshipsAdapter internshipsAdapter;

    List<Internship> list;
    boolean reachedLastPage = false;

    // this is a sample comment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // the recycler view for the internships objects
        internshipRv = findViewById(R.id.internship_rv);
        // the layout manager for the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        list = new ArrayList<>();
        internshipsAdapter = new InternshipsAdapter(this, list);
        internshipsAdapter.setOnLastItemListener(new OnLastItemListener() {
            @Override
            public void onLastItem(int position) {
                // if we haven't reached the last page then again hit the api
                if(!reachedLastPage)
                    getInternships();
                else {

                }
            }
        });
        internshipRv.setLayoutManager(linearLayoutManager);
        internshipRv.setAdapter(internshipsAdapter);

        // getting the list for the first time
        getInternships();
    }

    private void getInternships(){
        // now this is the url and at the end i'm attaching the current requested page
        String URL = "https://internshala.com/json/internships/page-" + currentPage;
        // incrementing current page because as we scroll we are requesting new data
        currentPage++;
        // jsonObjectRequest for volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    // taking out the json array from the response, as the internship_ids are ints,
                    // i have used optJSONArray which gives a JSONArray with int values
                    JSONArray items = response.optJSONArray("internship_ids") ;
                    // commented the below line because i was getting some error the isLastPage not found like that
//                    reachedLastPage = response.getBoolean("isLastPage");
                    List<Internship> tempList = new ArrayList<>();

                    // iterating the items and getting the data from it, and at the end making the object,
                    // and adding it to the list to pass to the adapter
                    for(int i = 0 ; i < items.length() ; i++) {
                        int internshipId = items.optInt(i) ;

                        JSONObject internshipListItem = response.getJSONObject("internships_meta") ;
                        JSONObject internshipDetail = internshipListItem.getJSONObject(String.valueOf(internshipId)) ;
                        String profileName = internshipDetail.getString("profile_name");
                        String companyName = internshipDetail.getString("company_name");
                        String stipendProvided = internshipDetail.getJSONObject("stipend").getString("salary");
                        String internshipDuration = internshipDetail.getString("duration");
                        String typeOfJob = internshipDetail.getString("labels_app");
                        String expiresOn = internshipDetail.getString("expiring_in");
                        boolean isPartTimeAllowed = internshipDetail.getBoolean("part_time");
                        boolean isWorkFromHome = internshipDetail.getBoolean("work_from_home");

                        tempList.add(new Internship(profileName, companyName, stipendProvided, internshipDuration,
                                typeOfJob, expiresOn, isPartTimeAllowed, isWorkFromHome));
                    }
                    // if we haven't reached the last page, i am appending a null to show a progressbar
                    if(!reachedLastPage) {
                        internshipsAdapter.removeNull();
                        tempList.add(null);
                    } else {
                        // if we have reached to the last page we will remove the null so we don't show the progress bar
                        internshipsAdapter.removeNull();
                    }
                    // updating the list
                    internshipsAdapter.updateList(tempList);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // as we are using the singleton class for the volley request queue we add the object request to the queue
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

}