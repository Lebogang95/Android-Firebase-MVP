package za.co.lbnkosi.firebase_login.main;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    public FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    @Override
    public void onStart() {
        super.onStart();
            // Checks if there isn't a user registered to use the app
            if (user == null){
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity .class));
                finish();
            }
            else {
                IsEmailVerified();
            }
    }

    private void IsEmailVerified() {
        if (user.isEmailVerified()){
            // If the users email has been verified, then it starts the main activity
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fingerPrint();
            }
            else {
                noFingerprint();
            }
        }
        else {
            // If the users email has not been verified, it starts the verification activity
            startActivity(new Intent(SplashScreenActivity.this, VerificationActivity.class));
            finish();
        }
    }

    public void securityPreference() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
         DocumentReference docRef = db.collection("user_settings").document(Objects.requireNonNull(mAuth.getCurrentUser().getEmail())).collection("settings").document("security_settings");
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        Log.d("", "DocumentSnapshot data: " + document.getData());
                        //name.setText(document.getString("name") + " "+ document.getString("surname"));

                        String userPr;
                        userPr = document.getString("keep_me_signed_in");

                        assert userPr != null;
                        if (userPr.equals("false")){
                            //startActivity(new Intent(SplashScreenActivity.this, FingerprintActivity.class));
                            String userPr2;
                            userPr2 = document.getString("fingerprint_unlock");

                            assert userPr2 != null;
                            if (userPr2.equals("true")){
                                startActivity(new Intent(SplashScreenActivity.this, FingerprintActivity.class));
                            }
                            else {
                                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                            }
                        }
                        else {
                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                        }

                    }
                }
            });
        }
    }

    public void noFingerprint() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("user_settings").document(Objects.requireNonNull(mAuth.getCurrentUser().getEmail())).collection("settings").document("security_settings");
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        Log.d("", "DocumentSnapshot data: " + document.getData());
                        //name.setText(document.getString("name") + " "+ document.getString("surname"));

                        String userPr;
                        userPr = document.getString("keep_me_signed_in");

                        assert userPr != null;
                        if (userPr.equals("true")){
                            //startActivity(new Intent(SplashScreenActivity.this, FingerprintActivity.class));
                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));

                        }
                        else {
                            if (userPr.equals("false")){
                                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                            }
                        }
                    }
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fingerPrint(){
        // Initializing both Android Keyguard Manager and Fingerprint Manager
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        // Check whether the device has a Fingerprint sensor.
        if(!fingerprintManager.isHardwareDetected()){

            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
        }
        else {
            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            }else{
                // Check whether at least one fingerprint is registered
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                }else{
                    // Checks whether lock screen security is enabled or not
                    if (!keyguardManager.isKeyguardSecure()) {
                        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    }else{
                        securityPreference();
                    }
                }
            }
        }
    }
}
