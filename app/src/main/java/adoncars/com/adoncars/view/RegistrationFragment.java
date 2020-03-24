package adoncars.com.adoncars.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

import adoncars.com.adoncars.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class RegistrationFragment extends Fragment {

    private TextView login;
    private TextInputLayout otpWrapper;
    private TextInputLayout username,profilePic, mobile, vehicleno, vehiclemodel, location;
    private Button signup;
    private ImageView profilePiciv;
    private static final int RC_PHOTO_PICKER = 1;
    private static final int RESULT_OK = -1;
    private  Bitmap bitmap , decoded;
    private Uri photouri;
    private ByteArrayOutputStream out;
    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private ProgressDialog dialog;
    private FirebaseAuth auth;
    private Button send, verify;

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

        dialog = new ProgressDialog(getActivity());

        dialog.setMessage("processing please wait!");

        otpWrapper = (TextInputLayout) view.findViewById(R.id.passwordwrapper);

        send = (Button) view.findViewById(R.id.sendotp);

        verify = (Button) view.findViewById(R.id.verify);


        username = (TextInputLayout) view.findViewById(R.id.usernamewrapper);
        mobile = (TextInputLayout) view.findViewById(R.id.mobilenowrapper);
        vehicleno = (TextInputLayout) view.findViewById(R.id.vehiclenowrapper);
        vehiclemodel = (TextInputLayout) view.findViewById(R.id.vehiclemodelwrapper);
        location = (TextInputLayout) view.findViewById(R.id.locationwrapper);
        profilePiciv = (ImageView) view.findViewById(R.id.profilepic);

        profilePiciv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Image Chooser"), RC_PHOTO_PICKER);
            }
        });

        signup = (Button) view.findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startplayer();
                validations();
                startPushData();
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

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startplayer();
                String mob = mobile.getEditText().getText().toString();
                Toast.makeText(getActivity(),""+mob,Toast.LENGTH_SHORT).show();
                if(mob.length() == 10){
                    disableError(mobile);
                    dialog.show();
                    sendCode(mob);
                }
                else{
                    mobile.setError("enter valid mobile no");
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

    }

    private void startPushData() {


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
                            signup.setEnabled(true);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {

            TastyToast.makeText(getActivity(), "pick an image", Toast.LENGTH_SHORT, TastyToast.INFO).show();


            photouri = data.getData();
            bitmap = null;
            decoded = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photouri);
                out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            profilePiciv.setImageBitmap(decoded);
        }
    }


    private void validations() {

        String uname = username.getEditText().getText().toString();
        String uvehicleno = vehicleno.getEditText().getText().toString();
        String uvehiclemodel = vehiclemodel.getEditText().getText().toString();
        String uloc = location.getEditText().getText().toString();

        boolean unameFlag = false, uvehicleModelFlag = false, uvehicleNumberFlag = false, locationFlag = false;

        if(uname.length() >= 8  ){

            unameFlag = true;
        }
        else {
            username.setError("username must be atleast eight characters");
        }

        if(uvehiclemodel.length()>0){
            uvehicleModelFlag = true;
        }
        else {
            vehiclemodel.setError("please enter vehicle model");
        }
        if(uvehicleno.length() > 0){
            uvehicleNumberFlag = true;
        }
        else {
            vehicleno.setError("please enter vehicle number");
        }
        if(uloc.length() > 0){
            locationFlag = true;
        }
        else {
            location.setError("please enter location");
        }

        if(unameFlag & uvehicleModelFlag & uvehicleNumberFlag & locationFlag){
            //proces the data
            disableError(username);
            disableError(mobile);
            disableError(vehiclemodel);
            disableError(vehicleno);
            disableError(location);
            Toast.makeText(getActivity(),"valid",Toast.LENGTH_SHORT).show();
        }

    }

    private void disableError(TextInputLayout Wrapper) {
        if(Wrapper.isErrorEnabled()){
            Wrapper.setErrorEnabled(false);
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