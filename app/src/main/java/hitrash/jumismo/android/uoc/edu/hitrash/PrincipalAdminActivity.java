package hitrash.jumismo.android.uoc.edu.hitrash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PrincipalAdminActivity extends AppCompatActivity {

    private Button buttonExit, buttonUserList, buttonManageHikingTrail, buttonCleaningClaims;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_admin);

        buttonExit = findViewById(R.id.buttonExitAdmin);
        buttonUserList = findViewById(R.id.buttonUserList);
        buttonManageHikingTrail = findViewById(R.id.buttonManageHikingTrails);
        buttonCleaningClaims = findViewById(R.id.buttonCleaningClaims);

        buttonUserList.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), UserListActivity.class);
                startActivity(i);
            }
        });

        buttonManageHikingTrail.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ManagerHikingTrailActivity.class);
                startActivity(i);
            }
        });

        buttonCleaningClaims.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), CleaningClaimsActivity.class);
                startActivity(i);
            }
        });
    }
}