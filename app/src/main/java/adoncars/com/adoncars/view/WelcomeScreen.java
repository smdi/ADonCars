package adoncars.com.adoncars.view;

import android.media.MediaPlayer;


import android.os.Bundle;
import android.view.View;

import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import adoncars.com.adoncars.R;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class WelcomeScreen extends AppCompatActivity {

    private Button getstarted;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

    }

    @Override
    protected void onStart() {
        super.onStart();

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            loadFragment(new RegistrationFragment());
        }
        else{
            getstarted = (Button) findViewById(R.id.getstarted);

            getstarted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startplayer();
                    loadFragment(new LoginFragment());

                }
            });
        }

    }

    public boolean loadFragment(Fragment fragment){

        if(fragment!=null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.welcomescreen, fragment).addToBackStack("tag");
            ft.commit();
            return  true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void startplayer() {

        final MediaPlayer mp = MediaPlayer.create(getApplicationContext() ,R.raw.knock);
        mp.start();
    }
}
