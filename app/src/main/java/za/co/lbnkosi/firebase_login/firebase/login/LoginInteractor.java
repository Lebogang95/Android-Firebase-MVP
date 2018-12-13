package za.co.lbnkosi.firebase_login.firebase.login;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

/**
 * Created by Lebogang Nkosi
 */

public class LoginInteractor implements LoginContract.Intractor {

    private LoginContract.onLoginListener mOnLoginListener;

    LoginInteractor(LoginContract.onLoginListener onLoginListener){
        this.mOnLoginListener = onLoginListener;
    }

    // Method called to login the user
    @Override
    public void performFirebaseLogin(Activity activity, String email, String password) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        mOnLoginListener.onSuccess(Objects.requireNonNull(task.getResult()).toString());
                    }
                    else {
                        mOnLoginListener.onFailure(Objects.requireNonNull(task.getException()).toString());
                    }
                });
    }

    //Method to check if the credentials are correct
    @Override
    public void validateCredentials(Activity activity, String email, String password){
        if (email.equals("")){
            mOnLoginListener.onFailure(email);
            return;
        }

        if (!email.contains("@")){
            mOnLoginListener.onFailure(email);
            return;
        }

        if (password.equals("")){
            mOnLoginListener.onFailure("Password is empty");
            return;
        }

        if (password.length()<6){
            mOnLoginListener.onFailure("Short password");
            return;
        }

        performFirebaseLogin(activity,email,password);

    }


}
