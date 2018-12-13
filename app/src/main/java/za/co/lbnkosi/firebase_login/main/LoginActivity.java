package za.co.lbnkosi.firebase_login.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import za.co.lbnkosi.firebase_login.R;
import za.co.lbnkosi.firebase_login.base.BaseActivity;
import za.co.lbnkosi.firebase_login.firebase.login.LoginContract;
import za.co.lbnkosi.firebase_login.firebase.login.LoginPresenter;

public class LoginActivity extends BaseActivity implements LoginContract.View , View.OnClickListener {

    private LoginPresenter mLoginPresenter;
    private FirebaseAuth mAuth;

    @BindView(R.id.email_edit) EditText signInEmail;
    @BindView(R.id.password_edit) EditText signInPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mLoginPresenter = new LoginPresenter(this);

        Button buttonSignin = findViewById(R.id.signin_button);
        TextView buttonRegister = findViewById(R.id.register_text);
        TextView buttonForgotPassword = findViewById(R.id.passwordForgot_text);

        buttonSignin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        buttonForgotPassword.setOnClickListener(this);

        TextView textView = findViewById(R.id.register_text);
        textView.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

    }

    // Finish this activity when a user presses the back button. This is the initial activity of the app
    @Override
    public void onBackPressed(){
        finish();
    }

    //Calls the method which checks if the credentials the user entered are correct
    private void initLogin(String email, String password) {
        mLoginPresenter.checkCredentials(this, email, password);
    }

    //If the credentials entered are correct it automatically signs in the user and calls a method to check for verification
    @Override
    public void onLoginSuccess(String message) {
        hideLoadingScreen();
        IsEmailVerified();
    }

    //If the credentials are incorrect then it displays a dialog error. The method can be found here
    @Override
    public void onLoginFailure(String message) {
        hideLoadingScreen();
        onError(message);
    }

    //Method called by onLoginSuccess. You may modify the method but do not delete this method as it will cause the app to crash
    private void IsEmailVerified() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        if (user.isEmailVerified()){
            // If the users email has been verified, then it starts the main activity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        else {
            // If the users email has not been verified, it starts the verification activity
            startActivity(new Intent(LoginActivity.this, VerificationActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_button:
                initLogin(String.valueOf(signInEmail.getText().toString()), String.valueOf(signInPassword.getText().toString()));
                break;
        }
    }

}
