package hitrash.jumismo.android.uoc.edu.hitrash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.HikingTrail;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.HttpUtils;

public class CleaningClaimsActivity extends AppCompatActivity {

    private List<HikingTrail> hikingTrailsWithClaimsList;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_claims);

        hikingTrailsWithClaimsList = new ArrayList<HikingTrail>();

        recycler = (RecyclerView) findViewById(R.id.cleaning_claims_recycler_view);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        HttpUtils.get(Constants.URI_CLEANING_CLAIMS, null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Get Claims", "Claims got");
                try {
                    JSONArray data =  (JSONArray) response.get("data");
                    int size = data.length();

                    for(int i = 0; i < size; i++){
                        HikingTrail hikingTrail = new HikingTrail();
                        hikingTrail.parseFromJSON(data.getJSONObject(i));
                        hikingTrailsWithClaimsList.add(hikingTrail);
                    }

                    adapter = new CleaningClaimsArrayAdapter(hikingTrailsWithClaimsList);
                    recycler.setAdapter(adapter);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                Log.d("Get Users", "Entra en array");
            }
        });


    }
}
