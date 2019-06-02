package hitrash.jumismo.android.uoc.edu.hitrash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import hitrash.jumismo.android.uoc.edu.hitrash.Utils.MyBounceInterpolator;

public class MainActivity extends AppCompatActivity {

    private Button buttonLogin;
    private Button buttonRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Evento onClick del botón login para iniciar la actividad LoginActivity
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        // Add animation
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        buttonLogin.startAnimation(myAnim);
        buttonLogin.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(v.getContext(), LoginActivity.class);
                startActivity(i);
                Animatoo.animateSlideUp(MainActivity.this);
            }
        });

        // Evento onClick del botón register para iniciar la actividad RegisterActivity
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.startAnimation(myAnim);
        buttonRegister.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(i);
                Animatoo.animateSlideDown(MainActivity.this);
            }
        });
    }
}
