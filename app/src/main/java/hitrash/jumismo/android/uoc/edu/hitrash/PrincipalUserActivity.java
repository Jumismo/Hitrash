package hitrash.jumismo.android.uoc.edu.hitrash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import hitrash.jumismo.android.uoc.edu.hitrash.Utils.MyBounceInterpolator;

public class PrincipalUserActivity extends AppCompatActivity {

    private Button buttonHikingGroup;
    private Button buttonHikingTrails;
    private Button buttonClean;
    private Button buttonProfilePrincipal;
    private Button buttonExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_user);

        buttonHikingGroup = findViewById(R.id.buttonHikingGroup);
        buttonHikingTrails = findViewById(R.id.buttonHikingTrails);
        buttonClean = findViewById(R.id.buttonClean);
        buttonProfilePrincipal = findViewById(R.id.buttonProfilePrincipal);
        buttonExit = findViewById(R.id.buttonExit);

        // Add animation
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        buttonHikingTrails.startAnimation(myAnim);
        buttonClean.startAnimation(myAnim);
        buttonHikingGroup.startAnimation(myAnim);
        buttonExit.startAnimation(myAnim);
        buttonProfilePrincipal.startAnimation(myAnim);

        final SharedPreferences settings = getSharedPreferences("Preference", 0);
        final String idUser = settings.getString("IdUser", "");

        buttonExit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("IdUser");
                editor.remove("IsAdmin");
                editor.commit();

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
                Animatoo.animateSlideUp(PrincipalUserActivity.this);
            }
        });

        buttonProfilePrincipal.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                intent.putExtra("id_user", idUser);
                startActivity(intent);
                Animatoo.animateSlideUp(PrincipalUserActivity.this);

            }
        });

        buttonHikingGroup.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HikingGroupActivity.class);
                intent.putExtra("id_user", idUser);
                startActivity(intent);
                Animatoo.animateSlideRight(PrincipalUserActivity.this);

            }
        });

        buttonHikingTrails.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HikingTrailsActivity.class);
                intent.putExtra("id_user", idUser);
                startActivity(intent);
                Animatoo.animateSlideLeft(PrincipalUserActivity.this);

            }
        });

        buttonClean.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CleaningGroupActivity.class);
                intent.putExtra("id_user", idUser);
                startActivity(intent);
                Animatoo.animateSlideDown(PrincipalUserActivity.this);
            }
        });
    }
}
