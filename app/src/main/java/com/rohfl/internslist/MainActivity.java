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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        internshipRv = findViewById(R.id.internship_rv);
//        progressBarLayout = findViewById(R.id.progress_bar_layout);
//        progressBarLayout.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        list = new ArrayList<>();
        internshipsAdapter = new InternshipsAdapter(this, list);
        internshipsAdapter.setOnLastItemListener(new OnLastItemListener() {
            @Override
            public void onLastItem(int position) {
                if(!reachedLastPage)
                    getInternships();
                else {

                }
            }
        });
        internshipRv.setLayoutManager(linearLayoutManager);
        internshipRv.setAdapter(internshipsAdapter);

        getInternships();
    }

    private void getInternships(){
        String URL = "https://internshala.com/json/internships/page-" + currentPage;
        currentPage++;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray items = response.optJSONArray("internship_ids") ;
//                    reachedLastPage = response.getBoolean("isLastPage");
                    List<Internship> tempList = new ArrayList<>();
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
                    if(!reachedLastPage) {
                        internshipsAdapter.removeNull();
                        tempList.add(null);
                    } else {
                        internshipsAdapter.removeNull();
                    }
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
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

}