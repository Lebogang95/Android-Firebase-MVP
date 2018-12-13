package za.co.lbnkosi.firebase_login.firebase.verification;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class VerificationInteractor implements VerificationContract.Intractor {

    private static final String TAG = VerificationInteractor.class.getSimpleName();
    private VerificationContract.onVerificationListener mOnVerificationListener;

    public VerificationInteractor(VerificationContract.onVerificationListener onVerificationListener){
        this.mOnVerificationListener = onVerificationListener;
    }

    @Override
    public void performFirebaseVerification() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        if (user.isEmailVerified()) {
            mOnVerificationListener.onSuccess();
        }

        else{
           mOnVerificationListener.onFailure("");
        }
    }
}
