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
import android.widget.Toast;

import adoncars.com.adoncars.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class RegistrationFragment extends Fragment {

    private TextView login;
    private EditText username, password, repassword, mobile, vehicleno, vehiclemodel, location;
    private Button signup;
    private int valid = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initialise(view);


    }

    private void initialise(View view) {

        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        repassword = (EditText) view.findViewById(R.id.repassword);
        mobile = (EditText) view.findViewById(R.id.mobileno);
        vehicleno = (EditText)view.findViewById(R.id.vehicleno);
        vehiclemodel = (EditText) view.findViewById(R.id.vehiclemodel);
        location = (EditText) view.findViewById(R.id.location);
        signup = (Button) view.findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startplayer();
                validations();
            }
        });

        login = (TextView) view.findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startplayer();
                loadFragment(new LoginFragment());
            }
        });

    }

    private void validations() {

        String uname = username.getText().toString();
        String upass = password.getText().toString();
        String urepass = repassword.getText().toString();
        String umobile =  mobile.getText().toString();
        String uvehicleno = vehicleno.getText().toString();
        String uvehiclemodel = vehiclemodel.getText().toString();
        String uloc = location.getText().toString();


        if(uname.length() >= 8  ){

            ++valid;
        }
        else {
            username.setError("username must be atleast eight characters");
        }
        if(upass.length()>= 8){
            ++valid;
        }
        else {
            password.setError("password must be atleast eight characters");
        }
        if(upass.length() == urepass.length()){

            if(upass.equals(urepass)){
                ++valid;
            }
            else {
                //error
                repassword.setError("password's are not matching");
            }
        }
        else {
            //error
            repassword.setError("password's are not matching");
        }
        if(Patterns.PHONE.matcher(umobile).matches()){
            ++valid;
        }
        else {
            mobile.setError("Invalid mobile number");
        }
        if(uvehiclemodel.length()>0){
            ++valid;
        }
        else {
            vehiclemodel.setError("please enter vehicle model");
        }
        if(uvehicleno.length() > 0){
            ++valid;
        }
        else {
            vehicleno.setError("please enter vehicle number");
        }
        if(uloc.length() > 0){
            ++valid;
        }
        else {
            location.setError("please enter location");
        }

        if(valid == 7){
            //proces the data
            Toast.makeText(getActivity(),"valid",Toast.LENGTH_SHORT).show();
        }
        else {
            valid =0;
        }

    }

    public boolean loadFragment(Fragment fragment)
    {
        if(fragment!=null)
        {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.registration, fragment).addToBackStack("tag");
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