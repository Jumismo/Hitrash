package hitrash.jumismo.android.uoc.edu.hitrash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.User;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.MyBounceInterpolator;

public class ProfileActivity extends AppCompatActivity {

    private EditText editProfileUsername;
    private EditText editProfilePassword;
    private EditText editProfileEmail;

    private ImageButton confirmProfile;

    private String id_user;

    private ProgressDialog dialog;

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

        // Add animation
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        confirmProfile.startAnimation(myAnim);

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
            public void onClick(final View v) {
                RequestParams rp = new RequestParams();
                rp.add("name", editProfileUsername.getText().toString());
                rp.add("password", editProfilePassword.getText().toString());
                rp.add("email", editProfileEmail.getText().toString());
                AsyncHttpUtils.put(Constants.URI_UPDATE_USER + id_user, rp, new JsonHttpResponseHandler(){

                    @Override
                    public void onStart() {
                        super.onStart();
                        dialog = ProgressDialog.show(v.getContext(), "",
                                "Loading. Please wait...", true);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try{
                            JSONObject data = response.getJSONObject("data");
                            User user = new User();
                            user.parseFromJSON(data);
                            dialog.dismiss();
                            Toast.makeText(v.getContext(), v.getContext().getString(R.string.userUpdated), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            dialog.dismiss();
                            Toast.makeText(v.getContext(), v.getContext().getString(R.string.errorParseObject), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        dialog.dismiss();
                        Toast.makeText(v.getContext(), v.getContext().getString(R.string.errorRequest) + ": " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
