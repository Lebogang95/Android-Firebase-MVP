package za.co.lbnkosi.firebase_login.firebase.register;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */

public class RegistrationInteractor implements RegistrationContract.Intractor {

    private static final String TAG = RegistrationInteractor.class.getSimpleName();
    private RegistrationContract.onRegistrationListener mOnRegistrationListener;

    public RegistrationInteractor(RegistrationContract.onRegistrationListener onRegistrationListener){
        this.mOnRegistrationListener = onRegistrationListener;
    }

    @Override
    public void validateCredentials(Activity activity, String email, String password, String confirmPassword, String name, String surname, String phoneNumber){

        if (name.equals("") || surname.equals("") || email.equals("") || password.equals("") || confirmPassword.equals("")|| phoneNumber.equals("")) {
            mOnRegistrationListener.onFailure("Please Fill In The Empty Fields");
            return;
        }

        if (!email.contains("@")){
            mOnRegistrationListener.onFailure("Invalid Email");
            return;
        }

        // Password validations
        if (password.equals("")){
            mOnRegistrationListener.onFailure("Invalid Password");
            return;
        }

        if (password.length()<6){
            mOnRegistrationListener.onFailure("Short Password");
            return;
        }

        if (!confirmPassword.equals(password)) {
            mOnRegistrationListener.onFailure("Passwords Do Not Match");
            return;
        }

        storeCredentials(activity,email,password,name,surname,phoneNumber);

    }

    @Override
    public void storeCredentials(final Activity activity, final String email, final String password, String name, String surname, String phoneNumber){

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("name", name);
        credentials.put("surname", surname);
        credentials.put("email", email);
        credentials.put("phoneNumber", phoneNumber);

        db.collection("users").document(email)
                .set(credentials)
                .addOnSuccessListener(aVoid -> {
                    Log.d("", "DocumentSnapshot successfully written!");
                    performFirebaseRegistration(activity,email,password);
                })
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));

        writeSecuritySettings(email);
    }

    @Override
    public void performFirebaseRegistration(Activity activity, String email, String password) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
                        mOnRegistrationListener.onFailure(task.getException().getMessage());
                    }else{
                        mOnRegistrationListener.onSuccess("");
                    }
                });
    }

    private void writeSecuritySettings(String email){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> stats = new HashMap<>();
        stats.put("fingerprint_unlock", "no");
        stats.put("keep_me_signed_in", "no");

        db.collection("user_settings").document(email).collection("settings").document("security_settings")
                .set(stats)
                .addOnSuccessListener(aVoid -> Log.d("", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));
    }

}
