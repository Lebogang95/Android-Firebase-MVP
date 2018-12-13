package za.co.lbnkosi.firebase_login.firebase.register;

import android.app.Activity;

/**
 *
 */

public interface RegistrationContract {
    interface View{
        void onRegistrationSuccess(String message);
        void onRegistrationFailure(String message);
    }

    interface Presenter{
        void register(Activity activity, String email, String password, String confirmPassword, String name, String surname, String phoneNumber);
    }

    interface Intractor{
        void performFirebaseRegistration(Activity activity, String email, String password);

        void validateCredentials(Activity activity, String email, String password, String confirmPassword, String name, String surname, String phoneNumber);

        void storeCredentials(final Activity activity, final String email, final String password, String name, String surname, String phoneNumber);
    }

    interface onRegistrationListener{
        void onSuccess(String message);
        void onFailure(String message);
    }
}
