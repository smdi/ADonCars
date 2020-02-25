package adoncars.com.adoncars.view;

import android.os.Bundle;
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

public class LoginFragment extends Fragment {

    private EditText username, password;
    private Button login;
    private TextView signup,forgot;
    private int valid = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initialize(view);


    }

    private void initialize(View view) {

        signup = (TextView) view.findViewById(R.id.signup);

        forgot = (TextView) view.findViewById(R.id.forgot);

        username = (EditText) view.findViewById(R.id.username);

        password = (EditText) view.findViewById(R.id.password);

        login = (Button) view.findViewById(R.id.signin);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new RegistrationFragment());
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new FogotPasswordFragment());
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validations();

            }
        });

    }

    private void validations() {

        String uname = username.getText().toString();
        String upass = password.getText().toString();

        if(uname.length() > 0 ){
            ++valid;
        }
        else{
            username.setError("enter username");
        }
        if(upass.length()>0) {
            ++valid;
        }
        else {
            password.setError("enter password");
        }
        if(valid == 2){
            //process the data
            Toast.makeText(getActivity(),"valid",Toast.LENGTH_SHORT).show();
        }
        else {
            valid = 0;
        }

    }

    public boolean loadFragment(Fragment fragment)
    {
        if(fragment!=null)
        {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.login, fragment).addToBackStack("tag");
            ft.commit();
            return  true;
        }
        return false;

    }

}
