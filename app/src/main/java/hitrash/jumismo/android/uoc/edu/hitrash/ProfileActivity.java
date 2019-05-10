package hitrash.jumismo.android.uoc.edu.hitrash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.User;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;

public class ProfileActivity extends AppCompatActivity {

    private EditText editProfileUsername;
    private EditText editProfilePassword;
    private EditText editProfileEmail;

    private ImageButton confirmProfile;

    private String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        id_user = intent.getStringExtra("id_user");

        editProfileUsername = (EditText) findViewById(R.id.editProfileUsername);
        editProfilePassword = (EditText) findViewById(R.id.editProfilePassword);
        editProfileEmail = (EditText) findViewById(R.id.editProfileEmail);

        confirmProfile = (ImageButton) findViewById(R.id.confirmProfile);

        AsyncHttpUtils.get(Constants.URI_USER + id_user, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    User user = new User();
                    user.parseFromJSON(data);

                    if(user.getId()!= "")
                    {
                        editProfileUsername.setText(user.getName());
                        editProfilePassword.setText(user.getPassword());
                        editProfileEmail.setText(user.getEmail());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        confirmProfile.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                RequestParams rp = new RequestParams();
                rp.add("name", editProfileUsername.getText().toString());
                rp.add("password", editProfilePassword.getText().toString());
                rp.add("email", editProfileEmail.getText().toString());
                AsyncHttpUtils.put(Constants.URI_UPDATE_USER + id_user, rp, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try{
                            JSONObject data = response.getJSONObject("data");
                            User user = new User();
                            user.parseFromJSON(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });




    }
}
