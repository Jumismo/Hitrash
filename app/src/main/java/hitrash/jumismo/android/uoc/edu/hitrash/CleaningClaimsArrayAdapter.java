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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.HikingTrail;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;

public class CleaningClaimsArrayAdapter extends RecyclerView.Adapter<CleaningClaimsArrayAdapter.ViewHolder> {

    public List<HikingTrail> hikingTrailsClaimsList;

    public CleaningClaimsArrayAdapter(List<HikingTrail> hikingTrailsWithClaimsList) {
        this.hikingTrailsClaimsList = hikingTrailsWithClaimsList;
    }

    @NonNull
    @Override
    public CleaningClaimsArrayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_claim_fragment, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CleaningClaimsArrayAdapter.ViewHolder viewHolder, int i) {
        HikingTrail hikingTrail = hikingTrailsClaimsList.get(i);
        viewHolder.hikingTrail = hikingTrail;
        viewHolder.nameHikingTrail.setText(hikingTrail.getName());
    }

    @Override
    public int getItemCount() {
        return hikingTrailsClaimsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private HikingTrail hikingTrail;

        private TextView nameHikingTrail;
        private ImageButton sendMail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameHikingTrail = (TextView) itemView.findViewById(R.id.nameHikingTrail);
            sendMail = (ImageButton) itemView.findViewById(R.id.sendMail);

            sendMail.setOnClickListener(new ImageButton.OnClickListener(){
                @Override
                public void onClick(final View v) {

                    RequestParams rp = new RequestParams();
                    hikingTrail.setClaims(0);
                    rp.add("claims", String.valueOf(hikingTrail.getClaims()));
                    AsyncHttpUtils.put(Constants.URI_UPDATE_HIKING_TRAIL + hikingTrail.getId(), rp, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            JSONObject data = null;
                            try {
                                data = response.getJSONObject("data");
                                HikingTrail hikingTrail = new HikingTrail();
                                hikingTrail.parseFromJSON(data);

                                Toast.makeText(v.getContext(), v.getContext().getText(R.string.restoreClaims), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(v.getContext(), v.getContext().getText(R.string.hikingTrailNotUpdated), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Toast.makeText(v.getContext(), v.getContext().getString(R.string.errorRequest) + ": " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "jumisanbeg@gmail.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Claims of " + nameHikingTrail.getText().toString());
                    email.putExtra(Intent.EXTRA_TEXT, "Please clean this hiking trail");
                    email.setType("message/rfc822");

                    v.getContext().startActivity(Intent.createChooser(email, "Choose an Email client :"));
                }
            });
        }
    }
}
