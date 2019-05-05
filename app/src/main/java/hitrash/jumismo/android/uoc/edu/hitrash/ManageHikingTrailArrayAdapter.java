package hitrash.jumismo.android.uoc.edu.hitrash;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.HikingTrail;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;

public class ManageHikingTrailArrayAdapter extends RecyclerView.Adapter<ManageHikingTrailArrayAdapter.ViewHolder> {

    public List<HikingTrail> hikingTrailsList;
    public int currentPosition;

    public ManageHikingTrailArrayAdapter(List<HikingTrail> hikingTrailsList)
    {
        this.hikingTrailsList = hikingTrailsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_hiking_trail_fragment, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        HikingTrail hikingTrail = hikingTrailsList.get(i);
        currentPosition = i;
        viewHolder.hikingTrail = hikingTrail;
        viewHolder.name.setText(hikingTrail.getName());
        viewHolder.province.setText(hikingTrail.getProvince());
        viewHolder.hardness.setText(hikingTrail.getHardness());

        if(hikingTrail != null) {
            if (hikingTrail.getActive()) {
                viewHolder.acceptButton.setVisibility(View.GONE);
                viewHolder.blockButton.setVisibility(View.VISIBLE);
            } else {
                viewHolder.acceptButton.setVisibility(View.VISIBLE);
                viewHolder.blockButton.setVisibility(View.GONE);
            }

            if (hikingTrail.getGuide()) {
                viewHolder.guideImage.setVisibility(View.VISIBLE);
            } else {
                viewHolder.guideImage.setVisibility(View.GONE);
            }

            if (hikingTrail.getInformationOffice()) {
                viewHolder.informationOfficeImage.setVisibility(View.VISIBLE);
            } else {
                viewHolder.informationOfficeImage.setVisibility(View.GONE);
            }

            if (hikingTrail.getSignalize()) {
                viewHolder.signalizeImage.setVisibility(View.VISIBLE);
            } else {
                viewHolder.signalizeImage.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return hikingTrailsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public HikingTrail hikingTrail;

        public TextView name;
        public TextView province;
        public TextView hardness;

        public ImageView guideImage;
        public ImageView signalizeImage;
        public ImageView informationOfficeImage;

        public ImageButton acceptButton;
        public ImageButton blockButton;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.nameManageHikingTrail);
            province = (TextView) itemView.findViewById(R.id.provinceManageHikingTrail);
            hardness = (TextView) itemView.findViewById(R.id.hardnessManageHikingTrail);

            guideImage = (ImageView) itemView.findViewById(R.id.guideManageHikingTrail);
            signalizeImage = (ImageView) itemView.findViewById(R.id.signalizeManageHikingTrail);
            informationOfficeImage = (ImageView) itemView.findViewById(R.id.informationOfficeMHT);

            acceptButton = (ImageButton) itemView.findViewById(R.id.acceptButtonMHT);
            blockButton = (ImageButton) itemView.findViewById(R.id.blockButtonMHT);

            acceptButton.setOnClickListener(new ImageButton.OnClickListener(){
                @Override
                public void onClick(final View v) {
                    RequestParams rp = new RequestParams();
                    rp.add("isActive", "true");

                    AsyncHttpUtils.put(Constants.URI_UPDATE_HIKING_TRAIL + hikingTrail.getId(), rp, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                JSONObject data = (JSONObject) response.get("data");
                                HikingTrail hikingTrailUpdated = new HikingTrail();
                                hikingTrailUpdated.parseFromJSON(data);
                                hikingTrailsList.set(currentPosition, hikingTrailUpdated);

                                if(!hikingTrailUpdated.getActive().equals(true)){
                                    Toast.makeText(v.getContext(), "No se ha actualizado el Hiking Trail", Toast.LENGTH_SHORT);
                                }
                                else{
                                    notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });

            blockButton.setOnClickListener(new ImageButton.OnClickListener(){
                @Override
                public void onClick(final View v) {
                    RequestParams rp = new RequestParams();
                    rp.add("isActive", "false");


                    AsyncHttpUtils.put(Constants.URI_UPDATE_HIKING_TRAIL + hikingTrail.getId(), rp, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                JSONObject data = (JSONObject) response.get("data");
                                HikingTrail hikingTrailUpdated = new HikingTrail();
                                hikingTrailUpdated.parseFromJSON(data);
                                hikingTrailsList.set(currentPosition, hikingTrailUpdated);

                                if(!hikingTrailUpdated.getActive().equals(false)){
                                    Toast.makeText(v.getContext(), "No se ha actualizado la Hiking Trail", Toast.LENGTH_SHORT);
                                }
                                else{

                                    notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });

                }
            });


        }
    }
}
