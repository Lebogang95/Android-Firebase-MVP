package za.co.lbnkosi.firebase_login.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import za.co.lbnkosi.firebase_login.R;
import za.co.lbnkosi.firebase_login.base.BaseActivity;
import za.co.lbnkosi.firebase_login.firebase.register.RegistrationContract;
import za.co.lbnkosi.firebase_login.firebase.register.RegistrationPresenter;

public class RegisterActivity extends BaseActivity implements  RegistrationContract.View {

    private RegistrationPresenter mRegisterPresenter;

    private String name,surname,email,password,confirm_password,phone_number;
    private EditText nameEditText, surnameEditText, emailEditText, passwordEditText, confirmPasswordEditText, numberEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegisterPresenter = new RegistrationPresenter(this);
        Button button1 = findViewById(R.id.complete_button);
        button1.setOnClickListener(view -> {

            nameEditText = findViewById(R.id.name_edit);
            surnameEditText = findViewById(R.id.surname_edit);
            emailEditText = findViewById(R.id.email_edit);
            passwordEditText = findViewById(R.id.password_edit);
            confirmPasswordEditText = findViewById(R.id.passwordConfirm_edit);
            numberEditText = findViewById(R.id.number_edit);


            name = nameEditText.getText().toString();
            surname = surnameEditText.getText().toString();
            email = emailEditText.getText().toString();
            password = passwordEditText.getText().toString();
            confirm_password = confirmPasswordEditText.getText().toString();
            phone_number = numberEditText.getText().toString();


            showLoadingScreen();
            initRegistration(name,surname,email,password,confirm_password,phone_number);
        });

    }

    private void initRegistration(String name, String surname,String email, String password, String confirmPassword, String phonenumber) {
        mRegisterPresenter.register(this, email, password, confirmPassword, name, surname, phonenumber);
    }


    @Override
    public void onRegistrationSuccess(String message) {
        hideLoadingScreen();
        startActivity(new Intent(RegisterActivity.this, VerificationActivity.class));
        finish();
    }

    @Override
    public void onRegistrationFailure(String message) {
        hideLoadingScreen();
        onError(message);
    }

    @Override
    public void onBackPressed(){
        showLoadingScreen();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}
