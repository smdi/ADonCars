package adoncars.com.adoncars.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.concurrent.TimeUnit;

import adoncars.com.adoncars.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class LoginFragment extends Fragment {


    private TextInputLayout mobileWrapper, otpWrapper;
    private Button login, send, verify;
    private FirebaseAuth auth;
    private static final String TAG = "PhoneAuth";
    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private ProgressDialog dialog;
    private TextView signUp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            loadFragment(new RegistrationFragment());
        }
        else{
            initialize(view);
        }

    }

    private void initialize(View view) {

        dialog = new ProgressDialog(getActivity());

        dialog.setMessage("processing please wait!");

        mobileWrapper = (TextInputLayout) view.findViewById(R.id.usernamewrapper);

        otpWrapper = (TextInputLayout) view.findViewById(R.id.passwordwrapper);

        send = (Button) view.findViewById(R.id.sendotp);

        verify = (Button) view.findViewById(R.id.verify);

        login = (Button) view.findViewById(R.id.signin);

        signUp = (TextView) view.findViewById(R.id.signup);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadFragment(new RegistrationFragment());

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startplayer();
                String mob = mobileWrapper.getEditText().getText().toString();
                Toast.makeText(getActivity(),""+mob,Toast.LENGTH_SHORT).show();
                if(mob.length() == 10){
                    disableError(mobileWrapper);
                    dialog.show();
                    sendCode(mob);
                }
                else{
                    mobileWrapper.setError("enter valid mobile no");
                    dialog.dismiss();
                }

            }
        });


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startplayer();
                String otp = otpWrapper.getEditText().getText().toString();
                if(otp.length() == 6){
                    disableError(otpWrapper);
                    dialog.show();
                    verifyCode(otp);
                }
                else{
                    otpWrapper.setError("enter valid otp");
                    dialog.dismiss();
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startplayer();

            }
        });

    }

    public void verifyCode(String otp) {

        String c = otp;

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, c);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            dialog.dismiss();
                            TastyToast.makeText(getActivity(),"Code verfified",Toast.LENGTH_SHORT,TastyToast.SUCCESS).show();
                            //start fragment
//                            loadFragment(new RegistrationFragment());

                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                dialog.dismiss();
                                otpWrapper.setError("Invalid OTP");
                            }
                        }
                    }
                });
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

    private void disableError(TextInputLayout Wrapper) {
        if(Wrapper.isErrorEnabled()){
            Wrapper.setErrorEnabled(false);
        }
    }

    private void startplayer() {

        final MediaPlayer mp = MediaPlayer.create(getActivity() ,R.raw.knock);
        mp.start();
    }

    public void sendCode(String mob) {


        String phoneNumber = mob;
        phoneNumber = "+91 "+phoneNumber;
        setUpVerificatonCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60,TimeUnit.SECONDS
                ,getActivity(),verificationCallbacks);


    }

    private void setUpVerificatonCallbacks() {

        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(
                            PhoneAuthCredential credential) {
                        TastyToast.makeText(getActivity(),"Number verfified",Toast.LENGTH_SHORT,TastyToast.SUCCESS).show();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            TastyToast.makeText(getActivity(),"Invalid Request",Toast.LENGTH_SHORT,TastyToast.ERROR).show();
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                            TastyToast.makeText(getActivity(),"Quota Exceeded",Toast.LENGTH_SHORT,TastyToast.ERROR).show();
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {

                        phoneVerificationId = verificationId;
                        dialog.dismiss();
                        TastyToast.makeText(getActivity(),"Enter code",Toast.LENGTH_SHORT,TastyToast.DEFAULT).show();
                    }
                };
    }
}
