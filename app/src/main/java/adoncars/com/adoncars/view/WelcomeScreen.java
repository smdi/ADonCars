package adoncars.com.adoncars.view;

import android.os.Build;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import adoncars.com.adoncars.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class WelcomeScreen extends AppCompatActivity {

    private Button getstarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

    }

    @Override
    protected void onStart() {
        super.onStart();

        getstarted = (Button) findViewById(R.id.getstarted);

        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadFirstFragment(new LoginFragment());

            }
        });

    }

    public boolean loadFirstFragment(Fragment fragment){

        if(fragment!=null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.welcomescreen, fragment);
            ft.commit();
            return  true;
        }
        return false;
    }

}
