package hitrash.jumismo.android.uoc.edu.hitrash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import cz.msebera.android.httpclient.HttpResponse;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.User;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;


public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private ImageButton confirm;

    private User userLogged;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.editUsername);
        password = (EditText) findViewById(R.id.editPassword);
        confirm = (ImageButton) findViewById(R.id.confirm);

        confirm.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(final View v) {

                if (username.getText().toString() != "" && password.getText().toString() != "") {
                    RequestParams rp = new RequestParams();
                    rp.add("name", username.getText().toString());
                    rp.add("password", password.getText().toString());

                    AsyncHttpUtils.post(Constants.URI_AUTHENTICATION, rp, new JsonHttpResponseHandler() {

                        @Override
                        public void onStart() {
                            super.onStart();
                            dialog = ProgressDialog.show(v.getContext(), "",
                                    "Loading. Please wait...", true);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            try {
                                JSONObject data =  (JSONObject) response.get("data");
                                userLogged = new User();
                                userLogged.parseFromJSON(data);

                                if(userLogged.getIsActive()){
                                    SharedPreferences settings = getSharedPreferences("Preference", 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("IdUser", userLogged.getId());
                                    editor.putBoolean("IsAdmin", userLogged.getAdmin());
                                    editor.commit();

                                    dialog.dismiss();
                                    if(userLogged.getAdmin()){
                                        Intent intent = new Intent(v.getContext(), PrincipalAdminActivity.class);
                                        startActivity(intent);

                                    }else {
                                        Intent intent = new Intent(v.getContext(), PrincipalUserActivity.class);
                                        startActivity(intent);
                                    }
                                }
                                else
                                {
                                    dialog.dismiss();
                                    Toast.makeText(v.getContext(), v.getContext().getString(R.string.errorLogin), Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                //e.printStackTrace();
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
            }
        });
    }
}
