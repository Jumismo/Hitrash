package hitrash.jumismo.android.uoc.edu.hitrash;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cz.msebera.android.httpclient.Header;

import hitrash.jumismo.android.uoc.edu.hitrash.Model.User;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.HttpUtils;


public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private ImageButton confirm;

    private User userLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.editUsername);
        password = (EditText) findViewById(R.id.editPassword);
        confirm = (ImageButton) findViewById(R.id.confirm);

        confirm.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (username.getText().toString() != "" && password.getText().toString() != "") {
                    RequestParams rp = new RequestParams();
                    rp.add("name", username.getText().toString());
                    rp.add("password", password.getText().toString());

                    HttpUtils.post(Constants.URI_AUTHENTICATION, rp, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // If the response is JSONObject instead of expected JSONArray
                            try {
                                JSONObject serverResp = new JSONObject(response.toString());
                                Map<String, String> data = (Map<String, String>) serverResp.get("data");
                                userLogged = new User();
                                userLogged.parseFromJSON(data);

                                if(userLogged.getId()!= ""){
                                    SharedPreferences settings = getSharedPreferences("Preference", 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("IdUser", userLogged.getId());
                                    editor.putBoolean("IsAdmin", userLogged.getAdmin());
                                    // Commit the edits!
                                    editor.commit();
                                }

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                            // Pull out the first event on the public timeline

                        }
                    });
                }




            }
        });


    }
}
