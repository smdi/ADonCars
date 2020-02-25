package adoncars.com.adoncars.view;

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
    private EditText email;
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

        email = (EditText) view.findViewById(R.id.email);

        send = (Button) view.findViewById(R.id.send);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new RegistrationFragment());
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validations();
            }
        });


    }

    private void validations() {

        String mail = email.getText().toString();

        if(mail.length() > 0 && Patterns.EMAIL_ADDRESS.matcher(mail).matches()){

            //code to process
        }
        else {
            email.setError("Invalid email id");
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

}
