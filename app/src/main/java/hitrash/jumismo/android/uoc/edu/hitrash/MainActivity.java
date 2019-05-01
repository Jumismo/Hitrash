package hitrash.jumismo.android.uoc.edu.hitrash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonLogin;
    private Button buttonRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add event onclick to login button
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(v.getContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        // Add event onclick to register button
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(i);
            }
        });


    }


}
