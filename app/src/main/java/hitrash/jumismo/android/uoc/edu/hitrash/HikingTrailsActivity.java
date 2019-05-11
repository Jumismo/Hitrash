package hitrash.jumismo.android.uoc.edu.hitrash;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.HikingTrail;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;

public class HikingTrailsActivity extends AppCompatActivity {

    private List<HikingTrail> hikingTrailsList;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    private ImageButton newHikingTrailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiking_trails);

        hikingTrailsList = new ArrayList<HikingTrail>();

        newHikingTrailButton = (ImageButton) findViewById(R.id.newHikingTrailButton);

        recycler = (RecyclerView) findViewById(R.id.hiking_trail_list_recycler_view);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id_user");

        AsyncHttpUtils.get(Constants.URI_ACTIVE_HIKING_TRAIL, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = (JSONArray) response.get("data");
                    int size = data.length();

                    for (int i = 0; i < size; i++) {
                        HikingTrail hikingTrail = new HikingTrail();
                        hikingTrail.parseFromJSON(data.getJSONObject(i));
                        hikingTrailsList.add(hikingTrail);
                    }



                    adapter = new HikingTrailsArrayAdapter(hikingTrailsList, id);
                    recycler.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getString(R.string.errorParseObject), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), getString(R.string.errorRequest) + ": " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        newHikingTrailButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewHikingTrailActivity.class);
                intent.putExtra("id_user", id);
                startActivity(intent);
            }
        });

    }
}
