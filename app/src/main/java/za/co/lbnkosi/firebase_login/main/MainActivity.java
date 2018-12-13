package za.co.lbnkosi.firebase_login.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import za.co.lbnkosi.firebase_login.R;
import za.co.lbnkosi.firebase_login.base.BaseActivity;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends BaseActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        securityPreference();
        Switch simpleSwitch = findViewById(R.id.simpleSwitch1);
        Switch simpleSwitch3 = findViewById(R.id.simpleSwitch3);

        simpleSwitch3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // do something, the isChecked will be
            // true if the switch is in the On position

            if(simpleSwitch3.isChecked()){
                fingerprintAuthentication2();
            }
            else {
                fingerprintAuthentication();

            }

        });

        simpleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // do something, the isChecked will be
            // true if the switch is in the On position

            if(simpleSwitch.isChecked()){
                requireAuthFalse();
            }
            else {
                requireAuthTrue();
            }

        });

        Button button = findViewById(R.id.signOutButton);
        button.setOnClickListener(v-> {
           mAuth.signOut();
           startActivity(new Intent(MainActivity.this, LoginActivity.class));
           finish();
        });

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Button button2 = findViewById(R.id.deleteAccountButton);

        button2.setOnClickListener(v-> {
            assert user != null;
            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                } else {
                    //Handle the exception
                    task.getException();
                }
            });
        });

    }

    public void requireAuthFalse(){

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("keep_me_signed_in", "true");

        db.collection("user_settings").document(Objects.requireNonNull(mAuth.getCurrentUser().getEmail())).collection("settings").document("security_settings")
                .set(credentials, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));

    }

    public void requireAuthTrue(){

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("keep_me_signed_in", "false");

        db.collection("user_settings").document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail())).collection("settings").document("security_settings")
                .set(credentials, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));

    }

    public void fingerprintAuthentication(){

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("fingerprint_unlock", "false");

        db.collection("user_settings").document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail())).collection("settings").document("security_settings")
                .set(credentials, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));

    }

    public void fingerprintAuthentication2(){

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("fingerprint_unlock", "true");

        db.collection("user_settings").document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail())).collection("settings").document("security_settings")
                .set(credentials, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));

    }

    public void securityPreference() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                db.collection("user_settings").document(Objects.requireNonNull(mAuth.getCurrentUser().getEmail())).collection("settings").document("security_settings");
            }

            DocumentReference docRef = db.collection("user_settings").document(Objects.requireNonNull(mAuth.getCurrentUser().getEmail())).collection("settings").document("security_settings");
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        Log.d("", "DocumentSnapshot data: " + document.getData());
                        //name.setText(document.getString("name") + " "+ document.getString("surname"));

                        String userPr,userPr2;
                        userPr = document.getString("keep_me_signed_in");

                        assert userPr != null;
                        if (userPr.equals("true")){
                            Switch simpleSwitch = findViewById(R.id.simpleSwitch1);
                            simpleSwitch.setChecked(true);
                        }

                        userPr2 = document.getString("fingerprint_unlock");

                        assert userPr2 != null;
                        if (userPr2.equals("true")){
                            Switch simpleSwitch3 = findViewById(R.id.simpleSwitch3);
                            simpleSwitch3.setChecked(true);
                        }

                    }
                }
            });
        }

    }

}
