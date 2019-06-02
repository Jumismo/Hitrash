package hitrash.jumismo.android.uoc.edu.hitrash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import hitrash.jumismo.android.uoc.edu.hitrash.Utils.MyBounceInterpolator;

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

        // Add animation
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        buttonCleaningClaims.startAnimation(myAnim);
        buttonManageHikingTrail.startAnimation(myAnim);
        buttonUserList.startAnimation(myAnim);
        buttonExit.startAnimation(myAnim);

        buttonExit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("Preference", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("IdUser");
                editor.remove("IsAdmin");
                editor.commit();

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
                Animatoo.animateSlideUp(PrincipalAdminActivity.this);
            }
        });

        buttonUserList.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), UserListActivity.class);
                startActivity(i);
                Animatoo.animateSlideRight(PrincipalAdminActivity.this);
            }
        });

        buttonManageHikingTrail.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ManagerHikingTrailActivity.class);
                startActivity(i);
                Animatoo.animateSlideLeft(PrincipalAdminActivity.this);
            }
        });

        buttonCleaningClaims.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), CleaningClaimsActivity.class);
                startActivity(i);
                Animatoo.animateSlideDown(PrincipalAdminActivity.this);
            }
        });
    }

    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
    }
}
