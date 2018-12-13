package za.co.lbnkosi.firebase_login.firebase.verification;

import android.app.Activity;

public interface VerificationContract {
    interface View{
        void onVerificationSuccess();
        void onVerificationFailure(String message);
    }

    interface Presenter{
        void register(Activity activity, String email, String password);
    }

    interface Intractor{
        void performFirebaseVerification();
    }

    interface onVerificationListener{
        void onSuccess();
        void onFailure(String message);
    }
}
