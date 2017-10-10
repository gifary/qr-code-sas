package hospitality.sas.gifary.absenkaryawan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import hospitality.sas.gifary.absenkaryawan.model.Karyawan;
import hospitality.sas.gifary.absenkaryawan.model.KaryawanReponse;

import hospitality.sas.gifary.absenkaryawan.rest.ApiClient;
import hospitality.sas.gifary.absenkaryawan.rest.ApiInterface;
import hospitality.sas.gifary.absenkaryawan.utils.UserUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    // UI references.
    private EditText mPasswordView,mEmailView;
    private View mProgressView;
    private View mLoginFormView;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (UserUtil.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        mProgressView.setVisibility(View.VISIBLE);

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            mProgressView.setVisibility(View.GONE);
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

            Call<KaryawanReponse>  call = apiservice.login(mEmailView.getText().toString(), mPasswordView.getText().toString());
            call.enqueue(new Callback<KaryawanReponse>() {
                @Override
                public void onResponse(Call<KaryawanReponse> call, Response<KaryawanReponse> response) {
                    Log.d(TAG, response.body().toString());
                    int statuscode = response.body().getCode();
                    if(statuscode==200){  //sukses login
                        mProgressView.setVisibility(View.GONE);
                        Toast.makeText(getBaseContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        finish(response.body().getData(),"");
                    }else{
                        mProgressView.setVisibility(View.GONE);
                        Toast.makeText(getBaseContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<KaryawanReponse> call, Throwable t) {
                    Toast.makeText(getBaseContext(),"Sometihing worng",Toast.LENGTH_SHORT).show();
                    mProgressView.setVisibility(View.GONE);
                    if(Constants.DEBUG){
                        Log.e(TAG, t.toString());
                    }
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    private void finish(Karyawan karyawan, String token) {
        UserUtil.getInstance(this).signIn(karyawan,token);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

