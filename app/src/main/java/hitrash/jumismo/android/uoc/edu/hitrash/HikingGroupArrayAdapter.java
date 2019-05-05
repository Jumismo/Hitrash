package hitrash.jumismo.android.uoc.edu.hitrash;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.Group;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.User;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;

public class HikingGroupArrayAdapter extends RecyclerView.Adapter<HikingGroupArrayAdapter.ViewHolder> {
    private List<Group> hikingGroupList;
    private String id_user;


    public HikingGroupArrayAdapter(List<Group> userGroupList, String id_user) {
        this.hikingGroupList = userGroupList;
        this.id_user = id_user;
    }

    @NonNull
    @Override
    public HikingGroupArrayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_hiking_group_fragment, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HikingGroupArrayAdapter.ViewHolder viewHolder, int i) {
        Group hikingGroup = hikingGroupList.get(i);
        viewHolder.group = hikingGroup;
        viewHolder.nameHikingGroup.setText(hikingGroup.getName());

        for (User user: hikingGroup.getUsers()) {
            if(user.getId().equals(id_user))
            {
                viewHolder.joinGroup.setVisibility(View.GONE);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return hikingGroupList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Group group;
        private TextView nameHikingGroup;
        private ImageButton joinGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameHikingGroup = (TextView) itemView.findViewById(R.id.nameHikingGroup);
            joinGroup = (ImageButton) itemView.findViewById(R.id.buttonJoinHikingGroup);

            joinGroup.setOnClickListener(new ImageButton.OnClickListener(){

                @Override
                public void onClick(final View v) {

                    final int userCount = group.getUsers().size();
                    RequestParams rq = new RequestParams();
                    rq.add("users[]", id_user);

                    AsyncHttpUtils.put(Constants.URI_UPDATE_USER_GROUP + group.getId(), rq, new JsonHttpResponseHandler(){

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                JSONObject data = response.getJSONObject("data");
                                Group groupUpdated = new Group();
                                groupUpdated.parseFromJSON(data);
                                hikingGroupList.set(hikingGroupList.indexOf(group), groupUpdated);

                                if(groupUpdated.getUsers().size()<= userCount){
                                    Toast.makeText(v.getContext(), "No se ha actualizado el usuario", Toast.LENGTH_SHORT);
                                }
                                else{
                                    notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                }
            });


        }
    }
}
