package za.co.lbnkosi.firebase_login.firebase.verification;

import android.app.Activity;

public class VerificationPresenter implements VerificationContract.Presenter, VerificationContract.onVerificationListener {
    private VerificationContract.View mVerificationView;
    private VerificationInteractor mVerificationInteractor;

    public VerificationPresenter(VerificationContract.View registerView){
        this.mVerificationView = registerView;
        mVerificationInteractor = new VerificationInteractor(this);
    }

    @Override
    public void register(Activity activity, String email, String password) {
        mVerificationInteractor.performFirebaseVerification();
    }

    @Override
    public void onSuccess() {
        mVerificationView.onVerificationSuccess();
    }

    @Override
    public void onFailure(String message) {
        mVerificationView.onVerificationFailure(message);

    }
}

