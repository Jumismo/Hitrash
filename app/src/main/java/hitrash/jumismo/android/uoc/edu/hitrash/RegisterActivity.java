package hitrash.jumismo.android.uoc.edu.hitrash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.User;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, password, email;
    private ImageButton confirmRegister;

    private User userRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.editRegisterUsername);
        password = findViewById(R.id.editRegisterPassword);
        email = findViewById(R.id.editRegisterEmail);
        confirmRegister = findViewById(R.id.confirmRegister);

        confirmRegister.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (username.getText().toString() != "" && password.getText().toString() != "" && email.getText().toString() != "") {
                    RequestParams rp = new RequestParams();
                    rp.add("name", username.getText().toString());
                    rp.add("password", password.getText().toString());
                    rp.add("email", email.getText().toString());

                    AsyncHttpUtils.post(Constants.URI_NEW_USER, rp, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                JSONObject data =  (JSONObject) response.get("data");
                                userRegister = new User();
                                userRegister.parseFromJSON(data);

                                if(userRegister.getId()!= "") {
                                    SharedPreferences settings = getSharedPreferences("Preference", 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("IdUser", userRegister.getId());
                                    editor.putBoolean("IsAdmin", userRegister.getAdmin());
                                    // Commit the edits!
                                    editor.commit();

                                    if (userRegister.getAdmin()) {
                                        Intent intent = new Intent(v.getContext(), PrincipalAdminActivity.class);
                                        startActivity(intent);

                                    } else {
                                        Intent intent = new Intent(v.getContext(), PrincipalUserActivity.class);
                                        startActivity(intent);
                                    }
                                }

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {


                        }

                    });

                }
            }
        });


    }
}
