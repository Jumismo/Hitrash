package hitrash.jumismo.android.uoc.edu.hitrash;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hitrash.jumismo.android.uoc.edu.hitrash.Model.HikingTrail;

class HikingTrailsArrayAdapter extends RecyclerView.Adapter<HikingTrailsArrayAdapter.ViewHolder> {
    private List<HikingTrail> hikingTrailsList;
    private String user_id;

    public HikingTrailsArrayAdapter(List<HikingTrail> hikingTrailsList, String id) {
        this.hikingTrailsList = hikingTrailsList;
        this.user_id = id;
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

        viewHolder.hikingTrail = hikingTrail;
        viewHolder.name.setText(hikingTrail.getName());
        viewHolder.province.setText(hikingTrail.getProvince());
        viewHolder.hardness.setText(hikingTrail.getHardness());

        if(hikingTrail != null) {
            viewHolder.acceptButton.setVisibility(View.GONE);
            viewHolder.blockButton.setVisibility(View.GONE);

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            name = (TextView) itemView.findViewById(R.id.nameManageHikingTrail);
            province = (TextView) itemView.findViewById(R.id.provinceManageHikingTrail);
            hardness = (TextView) itemView.findViewById(R.id.hardnessManageHikingTrail);

            guideImage = (ImageView) itemView.findViewById(R.id.guideManageHikingTrail);
            signalizeImage = (ImageView) itemView.findViewById(R.id.signalizeManageHikingTrail);
            informationOfficeImage = (ImageView) itemView.findViewById(R.id.informationOfficeMHT);

            acceptButton = (ImageButton) itemView.findViewById(R.id.acceptButtonMHT);
            blockButton = (ImageButton) itemView.findViewById(R.id.blockButtonMHT);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ShowHikingTrailActivity.class);
                    intent.putExtra("id_hiking_trail", hikingTrail.getId());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
