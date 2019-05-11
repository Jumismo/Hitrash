package hitrash.jumismo.android.uoc.edu.hitrash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.HikingTrail;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;

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

        AsyncHttpUtils.get(Constants.URI_CLEANING_CLAIMS, null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

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
                    Toast.makeText(getApplicationContext(), getString(R.string.errorParseObject), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), getString(R.string.errorRequest) + ": " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
}
