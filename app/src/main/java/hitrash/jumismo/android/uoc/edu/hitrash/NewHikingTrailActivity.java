package hitrash.jumismo.android.uoc.edu.hitrash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.HikingTrail;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;

public class NewHikingTrailActivity extends AppCompatActivity {

    private EditText nameHikingTrailEditText;
    private EditText provinceHikingTrailEditText;
    private EditText distanceHikingTrailEditText;

    private CheckBox signalizeHikingTrailCheckbox;
    private CheckBox informationOfficeHikingTrailCheckbox;
    private CheckBox guideHikingTrailCheckbox;

    private Spinner hardnessTypeHikingTrailSpinner;

    private ImageButton acceptButtonHikingTrail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_hiking_trail);

        nameHikingTrailEditText = (EditText) findViewById(R.id.nameHikingTrailEditText);
        provinceHikingTrailEditText = (EditText) findViewById(R.id.provinceHikingTrailEditText);
        distanceHikingTrailEditText = (EditText) findViewById(R.id.distanceHikingTrailEditText);

        signalizeHikingTrailCheckbox = (CheckBox) findViewById(R.id.signalizeHikingTrailCheckbox);
        informationOfficeHikingTrailCheckbox = (CheckBox) findViewById(R.id.informationOfficeHikingTrailCheckbox);
        guideHikingTrailCheckbox = (CheckBox) findViewById(R.id.guideHikingTrailCheckbox);

        hardnessTypeHikingTrailSpinner = (Spinner) findViewById(R.id.hardnessTypeHikingTrailSpinner);

        acceptButtonHikingTrail = (ImageButton) findViewById(R.id.acceptButtonHikingTrail);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id_user");

        acceptButtonHikingTrail.setOnClickListener(new ImageButton.OnClickListener(){

            @Override
            public void onClick(final View v) {
                RequestParams rp = new RequestParams();
                rp.add("name", nameHikingTrailEditText.getText().toString());
                rp.add("province", provinceHikingTrailEditText.getText().toString());
                rp.add("distance", distanceHikingTrailEditText.getText().toString());
                rp.add("signalize", String.valueOf(signalizeHikingTrailCheckbox.isChecked()));
                rp.add("informationOffice", String.valueOf(informationOfficeHikingTrailCheckbox.isChecked()));
                rp.add("guide", String.valueOf(guideHikingTrailCheckbox.isChecked()));
                rp.add("hardness", hardnessTypeHikingTrailSpinner.getSelectedItem().toString());
                rp.add("location", "0.0, 0.0");
                rp.add("id_user", id);

                AsyncHttpUtils.post(Constants.URI_NEW_HIKING_TRAILS, rp, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            HikingTrail hikingTrailNew = new HikingTrail();
                            hikingTrailNew.parseFromJSON(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(v.getContext(), HikingTrailsActivity.class);
                        intent.putExtra("id_user", id);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });

            }
        });
    }
}
