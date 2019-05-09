package hitrash.jumismo.android.uoc.edu.hitrash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.HikingTrail;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;

public class ShowHikingTrailActivity extends AppCompatActivity {

    private TextView nameHikingTrailLabel;
    private TextView provinceLabel;
    private TextView hardnessLabel;
    private TextView distanceLabel;

    private ImageButton startRouteButton;
    private ImageButton cleaningClaimButton;

    private ImageView imageSignalize;
    private ImageView imageInformationOffice;
    private ImageView imageGuide;

    private ImageButton imageUserGroupButton;
    private ImageButton imageCleaningGroupButton;

    private String id_hiking_trail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_hiking_trail);

        nameHikingTrailLabel = (TextView) findViewById(R.id.nameHikingTrailLabel);
        provinceLabel = (TextView) findViewById(R.id.provinceLabel);
        hardnessLabel = (TextView) findViewById(R.id.hardnessLabel);
        distanceLabel = (TextView) findViewById(R.id.distanceLabel);

        startRouteButton = (ImageButton) findViewById(R.id.startRouteButton);
        cleaningClaimButton = (ImageButton) findViewById(R.id.cleaningClaimButton);

        imageSignalize = (ImageView) findViewById(R.id.imageSignalize);
        imageInformationOffice = (ImageView) findViewById(R.id.imageInformationOffice);
        imageGuide = (ImageView) findViewById(R.id.imageGuide);

        imageUserGroupButton = (ImageButton) findViewById(R.id.imageUserGroupButton);
        imageCleaningGroupButton = (ImageButton) findViewById(R.id.imageCleaningGroupButton);

        Intent intent = getIntent();
        id_hiking_trail = intent.getStringExtra("id_hiking_trail");

        AsyncHttpUtils.get(Constants.URI_SHOW_HIKING_TRAIL + id_hiking_trail, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    HikingTrail hikingTrail = new HikingTrail();
                    hikingTrail.parseFromJSON(data);

                    nameHikingTrailLabel.setText(hikingTrail.getName());
                    provinceLabel.setText(hikingTrail.getProvince());
                    hardnessLabel.setText(hikingTrail.getHardness());
                    distanceLabel.setText(hikingTrail.getDistance().toString());

                    if(hikingTrail.getSignalize()){
                        imageSignalize.setVisibility(View.VISIBLE);
                    }else{
                        imageSignalize.setVisibility(View.GONE);
                    }

                    if(hikingTrail.getInformationOffice())
                    {
                        imageInformationOffice.setVisibility(View.VISIBLE);
                    }else{
                        imageInformationOffice.setVisibility(View.GONE);
                    }

                    if(hikingTrail.getGuide())
                    {
                        imageGuide.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        imageGuide.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        imageUserGroupButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewGroupActivity.class);
                intent.putExtra("id_hiking_trail", id_hiking_trail);
                intent.putExtra("isCleaningClaim", false);
                startActivity(intent);
            }
        });

        imageCleaningGroupButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewGroupActivity.class);
                intent.putExtra("id_hiking_trail", id_hiking_trail);
                intent.putExtra("isCleaningClaim", true);
                startActivity(intent);
            }
        });
    }
}
