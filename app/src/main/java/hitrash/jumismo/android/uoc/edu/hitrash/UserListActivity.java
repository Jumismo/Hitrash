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
import hitrash.jumismo.android.uoc.edu.hitrash.Model.User;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.HttpUtils;

public class UserListActivity extends AppCompatActivity {

    private List<User> userList;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        userList = new ArrayList<User>();

        recycler = (RecyclerView) findViewById(R.id.user_list_recycler_view);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        HttpUtils.get(Constants.URI_USERS, null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Get Users", "Entra en object");
                try {
                    JSONArray data =  (JSONArray) response.get("data");
                    int size = data.length();

                    for(int i = 0; i < size; i++){
                        User user = new User();
                        user.parseFromJSON(data.getJSONObject(i));
                        userList.add(user);
                    }

                    adapter = new UserListArrayAdapter(userList);
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