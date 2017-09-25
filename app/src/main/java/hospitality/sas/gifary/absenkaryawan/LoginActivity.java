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

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import hospitality.sas.gifary.absenkaryawan.net.RestClient;
import hospitality.sas.gifary.absenkaryawan.net.RestResponseHandler;
import hospitality.sas.gifary.absenkaryawan.utils.UserUtil;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    // UI references.
    private EditText mPasswordView,mEmailView;
    private View mProgressView;
    private View mLoginFormView;
    private JsonHttpResponseHandler loginHandler;
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
        if (loginHandler != null) {
            return;
        }

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
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            loginHandler = new RestResponseHandler(TAG){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (Constants.DEBUG) Log.d(TAG, "login response: " + response);

                    try {
                        if (isSuccess(response)) {
                            JSONObject data = response.getJSONObject("data");
                            Toast.makeText(getBaseContext(),"Sukses Login",Toast.LENGTH_LONG).show();
                            finish(true, data,"");
                        } else {
                            Toast.makeText(getBaseContext(),response.getString("message")+"",Toast.LENGTH_LONG).show();
                            loginHandler = null;
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "login response: " + response, e);
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.d(TAG, "response is string: " + responseString);
                }

                @Override
                public void onStart() {
                    super.onStart();
                    mProgressView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    mProgressView.setVisibility(View.GONE);
                }

                @Override
                public void onCancel() {
                    super.onCancel();
                    loginHandler = null;
                }
            };
            RestClient.getInstance(this, loginHandler).postLogin(email, password, true);
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


    private void finish(boolean ok, JSONObject data,String token) {
        if (ok) {
            try {
                UserUtil.getInstance(this).signIn(data,token);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

}

