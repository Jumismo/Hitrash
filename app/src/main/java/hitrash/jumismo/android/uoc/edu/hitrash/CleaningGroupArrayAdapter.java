package hitrash.jumismo.android.uoc.edu.hitrash;

import android.content.Intent;
import android.support.annotation.NonNull;
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

class CleaningGroupArrayAdapter extends RecyclerView.Adapter<CleaningGroupArrayAdapter.ViewHolder> {

    private List<Group> cleaningGroupList;
    private String id_user;

    public CleaningGroupArrayAdapter(List<Group> cleaningGroupList, String id) {
        this.cleaningGroupList = cleaningGroupList;
        this.id_user = id;
    }

    @NonNull
    @Override
    public CleaningGroupArrayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cleaning_group_fragment, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CleaningGroupArrayAdapter.ViewHolder viewHolder, int i) {
        Group cleaningGroup = cleaningGroupList.get(i);
        viewHolder.group = cleaningGroup;
        viewHolder.nameCleaningGroup.setText(cleaningGroup.getName());

        for (User user: cleaningGroup.getUsers()) {
            if(user.getId().equals(id_user))
            {
                viewHolder.joinGroup.setVisibility(View.GONE);
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return cleaningGroupList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Group group;
        private TextView nameCleaningGroup;
        private ImageButton joinGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameCleaningGroup = (TextView) itemView.findViewById(R.id.nameCleaningGroup);
            joinGroup = (ImageButton) itemView.findViewById(R.id.buttonJoinCleaningGroup);

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
                                cleaningGroupList.set(cleaningGroupList.indexOf(group), groupUpdated);

                                if(groupUpdated.getUsers().size()<= userCount){
                                    Toast.makeText(v.getContext(), v.getContext().getString(R.string.userNotUpdated), Toast.LENGTH_SHORT);
                                }
                                else{
                                    notifyDataSetChanged();
                                    Toast.makeText(v.getContext(), v.getContext().getString(R.string.userUpdated), Toast.LENGTH_SHORT);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(v.getContext(),v.getContext().getString(R.string.errorParseObject), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Toast.makeText(v.getContext(),v.getContext().getString(R.string.errorRequest) + ": " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(joinGroup.getVisibility() == View.GONE)
                    {
                        Intent intent = new Intent(v.getContext(), ConversationActivity.class);
                        intent.putExtra("id_group", group.getId());
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}
