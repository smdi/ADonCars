package adoncars.com.adoncars.view;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import adoncars.com.adoncars.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FogotPasswordFragment extends Fragment {

    private TextView signup;
    private EditText mobile;
    private Button send;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgotpassword, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initialisations(view);

    }

    private void initialisations(View view) {

        signup = (TextView) view.findViewById(R.id.signup);

        mobile = (EditText) view.findViewById(R.id.mobileno);

        send = (Button) view.findViewById(R.id.send);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startplayer();
                loadFragment(new RegistrationFragment());
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startplayer();
                validations();
            }
        });


    }

    private void validations() {

        String mail = mobile.getText().toString();

        if(mail.length() > 0 && Patterns.PHONE.matcher(mail).matches()){

            //code to process
        }
        else {
            mobile.setError("Invalid mobile number");
        }

    }


    public boolean loadFragment(Fragment fragment)
    {
        if(fragment!=null)
        {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.forgotpassword, fragment).addToBackStack("tag");
            ft.commit();
            return  true;
        }
        return false;

    }
    private void startplayer() {

        final MediaPlayer mp = MediaPlayer.create(getActivity() ,R.raw.knock);
        mp.start();
    }

}
