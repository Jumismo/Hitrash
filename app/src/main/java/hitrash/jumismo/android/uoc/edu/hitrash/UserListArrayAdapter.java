package hitrash.jumismo.android.uoc.edu.hitrash;

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
import hitrash.jumismo.android.uoc.edu.hitrash.Model.User;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;

public class UserListArrayAdapter extends  RecyclerView.Adapter<UserListArrayAdapter.ViewHolder> {

    public List<User> userList;

    public UserListArrayAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserListArrayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_fragment, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListArrayAdapter.ViewHolder viewHolder, int i) {
        User user = userList.get(i);
        viewHolder.user = user;
        viewHolder.name.setText(userList.get(i).getName());

        if(user != null) {
            if (user.getIsActive()) {
                viewHolder.acceptButton.setVisibility(View.GONE);
                viewHolder.blockButton.setVisibility(View.VISIBLE);
            } else {
                viewHolder.acceptButton.setVisibility(View.VISIBLE);
                viewHolder.blockButton.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private User user;
        private TextView name;
        private ImageButton acceptButton, blockButton;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.textUsername);

            acceptButton = (ImageButton) itemView.findViewById(R.id.acceptButton);
            blockButton = (ImageButton) itemView.findViewById(R.id.blockButton);

            acceptButton.setOnClickListener(new ImageButton.OnClickListener(){
                @Override
                public void onClick(final View v) {
                    RequestParams rp = new RequestParams();
                    rp.add("isActive", "true");


                    AsyncHttpUtils.put(Constants.URI_UPDATE_USER + user.getId(), rp, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                JSONObject data = (JSONObject) response.get("data");
                                User userUpdated = new User();
                                userUpdated.parseFromJSON(data);
                                userList.set(userList.indexOf(user), userUpdated);

                                if(!userUpdated.getIsActive().equals(true)){
                                    Toast.makeText(v.getContext(), v.getContext().getString(R.string.userNotUpdated), Toast.LENGTH_SHORT);
                                }
                                else{
                                    notifyDataSetChanged();
                                    Toast.makeText(v.getContext(), v.getContext().getString(R.string.userUpdated), Toast.LENGTH_SHORT);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(v.getContext(), v.getContext().getString(R.string.errorParseObject), Toast.LENGTH_SHORT);
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Toast.makeText(v.getContext(), v.getContext(). getString(R.string.errorRequest) + ": " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

            blockButton.setOnClickListener(new ImageButton.OnClickListener(){
                @Override
                public void onClick(final View v) {
                    RequestParams rp = new RequestParams();
                    rp.add("isActive", "false");

                    AsyncHttpUtils.put(Constants.URI_UPDATE_USER + user.getId(), rp, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                JSONObject data = (JSONObject) response.get("data");
                                User userUpdated = new User();
                                userUpdated.parseFromJSON(data);
                                userList.set(userList.indexOf(user), userUpdated);

                                if(!userUpdated.getIsActive().equals(false)){
                                    Toast.makeText(v.getContext(), v.getContext().getString(R.string.userNotUpdated), Toast.LENGTH_SHORT);
                                }
                                else{
                                    notifyDataSetChanged();
                                    Toast.makeText(v.getContext(), v.getContext().getString(R.string.userUpdated), Toast.LENGTH_SHORT);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(v.getContext(), v.getContext().getString(R.string.errorParseObject), Toast.LENGTH_SHORT);
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Toast.makeText(v.getContext(), v.getContext(). getString(R.string.errorRequest) + ": " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });
        }
    }
}
