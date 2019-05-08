package hitrash.jumismo.android.uoc.edu.hitrash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.Comment;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.Group;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.User;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;

public class ConversationActivity extends AppCompatActivity {

    private Comment comment;
    private TextView nameConversation;
    private TextView dateConversation;
    private TextView locationConversation;
    private List<Comment> commentsList;
    private EditText insertCommentUserGroup;
    private ImageButton insertButtonCommentUserGroup;

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    private String id_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        nameConversation = findViewById(R.id.nameConversation);
        dateConversation = findViewById(R.id.dateConversation);
        locationConversation = findViewById(R.id.locationConversation);
        insertCommentUserGroup = findViewById(R.id.insertCommentUserGroup);
        insertButtonCommentUserGroup = findViewById(R.id.insertButtonCommentUserGroup);

        Intent intent = getIntent();
        id_group = intent.getStringExtra("id_group");

        commentsList = new ArrayList<Comment>();

        recycler = (RecyclerView) findViewById(R.id.conversation_recycler_view);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        updateComments();


        insertButtonCommentUserGroup.setOnClickListener(new ImageButton.OnClickListener(){

            @Override
            public void onClick(final View v) {
                RequestParams rq = new RequestParams();
                rq.add("comment", insertCommentUserGroup.getText().toString());

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                Date date = new Date();
                rq.add("publicationDate", dateFormat.format(date));

                rq.add("id_group", id_group);

                SharedPreferences settings = getSharedPreferences("Preference", 0);
                String idUser = settings.getString("IdUser", "");
                rq.add("id_author", idUser);

                AsyncHttpUtils.post(Constants.URI_NEW_COMMENT, rq, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            Comment commentNew = new Comment();
                            commentNew.parseFromJSON(data);
                            comment = commentNew;

                            RequestParams rq2 = new RequestParams();
                            rq2.add("comments[]", comment.getId());

                            AsyncHttpUtils.put(Constants.URI_UPDATE_USER_GROUP + id_group, rq2, new JsonHttpResponseHandler(){

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    updateComments();
                                }
                            });


                            if(data == null){
                                Toast.makeText(v.getContext(), "Not insert object", Toast.LENGTH_LONG);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }

    public void updateComments(){
        AsyncHttpUtils.get(Constants.URI_USER_GROUP + id_group, null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONObject data = response.getJSONObject("data");
                    Group group = new Group();
                    group.parseFromJSON(data);

                    nameConversation.setText(group.getName());
                    dateConversation.setText(group.getDate().toString());
                    locationConversation.setText(group.getLocation());

                    adapter = new CommentConversationArrayAdapter(group.getComments());
                    recycler.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
