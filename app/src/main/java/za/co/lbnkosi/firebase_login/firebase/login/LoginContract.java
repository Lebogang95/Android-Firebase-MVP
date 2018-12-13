package za.co.lbnkosi.firebase_login.firebase.login;

import android.app.Activity;

public interface LoginContract {
    interface View{
        void onLoginSuccess(String message);
        void onLoginFailure(String message);
    }

    interface Presenter{
        void login(Activity activity, String email, String password);

        void checkCredentials(Activity activity, String email, String password);

    }

    interface Intractor{
        void performFirebaseLogin(Activity activity, String email, String password);

        void validateCredentials(Activity activity, String email, String password);

    }

    interface onLoginListener{
        void onSuccess(String message);
        void onFailure(String message);
    }
}
