package hitrash.jumismo.android.uoc.edu.hitrash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class PrincipalUserActivity extends AppCompatActivity {

    private Button buttonHikingGroup;
    private Button buttonHikingTrails;
    private Button buttonClean;
    private Button buttonProfilePrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_user);

        buttonHikingGroup = findViewById(R.id.buttonHikingGroup);
        buttonHikingTrails = findViewById(R.id.buttonHikingTrails);
        buttonClean = findViewById(R.id.buttonClean);
        buttonProfilePrincipal = findViewById(R.id.buttonProfilePrincipal);

        SharedPreferences settings = getSharedPreferences("Preference", 0);
        final String idUser = settings.getString("IdUser", "");

        buttonProfilePrincipal.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                intent.putExtra("id_user", idUser);
                startActivity(intent);
            }
        });

        buttonHikingGroup.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HikingGroupActivity.class);
                intent.putExtra("id_user", idUser);
                startActivity(intent);
            }
        });

        buttonHikingTrails.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HikingTrailsActivity.class);
                intent.putExtra("id_user", idUser);
                startActivity(intent);
            }
        });

        buttonClean.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CleaningGroupActivity.class);
                intent.putExtra("id_user", idUser);
                startActivity(intent);
            }
        });
    }
}
