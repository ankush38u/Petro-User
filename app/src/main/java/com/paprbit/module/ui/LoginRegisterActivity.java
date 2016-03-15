package com.paprbit.module.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paprbit.module.R;
import com.paprbit.module.retrofit.gson_pojo.LoginResponse;
import com.paprbit.module.retrofit.gson_pojo.Message;
import com.paprbit.module.retrofit.utility.ServiceGenerator;
import com.paprbit.module.retrofit.utility.Storage;
import com.paprbit.module.utility.UserIntraction;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginRegisterActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.scrollView_register)
    ScrollView registerLayout;
    @Bind(R.id.scrollView_login)
    ScrollView loginLayout;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.btn_fb)
    LoginButton btnFb;
    @Bind(R.id.btn_register)
    Button btnRegister;
    @Bind(R.id.btn_login)
    Button btnLogin;


    @Bind(R.id.et_email_login)
    EditText etEmailLogin;
    @Bind(R.id.et_password_login)
    EditText etPassworfLogin;
    @Bind(R.id.et_name_register)
    EditText etNameRegister;
    @Bind(R.id.et_email_register)
    EditText etEmailRegister;
    @Bind(R.id.et_carno_register)
    EditText etCarnoRegister;
    @Bind(R.id.et_pass_register)
    EditText etPassRegister;

    ProgressDialog pd;

    //fb stuff
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    String TAG;


    //Google Login stuff
    //Signin button
    @Bind(R.id.btn_google)
    SignInButton googleSignInButton;
    //Signing Options
    private GoogleSignInOptions gso;
    //google api client
    private GoogleApiClient mGoogleApiClient;
    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;

    public LoginRegisterActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register_dcoder);
        ButterKnife.bind(this);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        TAG = getLocalClassName();
        setUpTabs();
        setUpFBLogin();
        setUpGLogin();

        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }

    private void setUpGLogin() {
        //G+ logic
        //Initializing google signin option
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInButton.setSize(SignInButton.SIZE_WIDE);
        googleSignInButton.setScopes(gso.getScopeArray());
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleSignInButton.setOnClickListener(this);
    }

    private void setUpFBLogin() {
        //fb logic
        callbackManager = CallbackManager.Factory.create();
        btnFb.setReadPermissions(Arrays.asList("public_profile, email"));
        btnFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Ankush", "login ho gya");
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    String fb_id = object.getString("id");
                                    String name = object.getString("name");
                                    String email = object.getString("email");
                                    String gender = object.getString("gender");
                                    Toast.makeText(LoginRegisterActivity.this, name + " " + email, Toast.LENGTH_LONG).show();
                                    //     LoginManager.getInstance().logOut(); //programically logout
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginRegisterActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginRegisterActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        profileTracker = new ProfileTracker() {
            //for fb
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                //profile changed update photo or name etc
                // currentProfile.getProfilePictureUri(240,320);
            }
        };
        accessTokenTracker = new AccessTokenTracker() {
            //for fb
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };
    }

    private void setUpTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Login"), 0);
        tabLayout.addTab(tabLayout.newTab().setText("Register"), 1);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Login")) {
                    loginLayout.setVisibility(View.VISIBLE);
                    registerLayout.setVisibility(View.GONE);
                }
                if (tab.getText().equals("Register")) {
                    loginLayout.setVisibility(View.GONE);
                    registerLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void updateWithToken(AccessToken currentAccessToken) {
        //for fb
        if (currentAccessToken != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    //do nothing much open up the home activity
                }
            }, 100);
        } else {
            Toast.makeText(LoginRegisterActivity.this, "Logout clicked", Toast.LENGTH_LONG).show();
            recreate();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data); //for fb

        //for g+
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_google:
                googleSignIn();
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.btn_login:
                login();
                break;

        }
    }

    private void login() {
        if (pd != null) pd.show();

        Call<ResponseBody> call = ServiceGenerator.getService().login(etEmailLogin.getText().toString(), etPassworfLogin.getText().toString()); //retrofit service call
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if (pd != null) pd.dismiss();

                if (response.isSuccess()) {
                    String res = null;
                    LoginResponse lr = null;
                    try {
                        res = response.body().string();

                        if (res != null) {
                            if (res.startsWith("{") && new JSONObject(res).has("message") && new JSONObject(res).has("type") && (!new JSONObject(res).has("session_id"))) {
                                //we will reach here in case any error on server related to session or database
                                UserIntraction.makeSnack(coordinatorLayout, new JSONObject(res).getString("message"));
                            } else {
                                lr = new Gson().fromJson(res, new TypeToken<LoginResponse>() {
                                }.getType());

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (lr != null && lr.isType()) {
                        //login successful
                        Storage.saveStringToPrefs(LoginRegisterActivity.this, getString(R.string.uid), etEmailLogin.getText().toString()); //save session to sharedprefs
                        ActivityCompat.finishAffinity(LoginRegisterActivity.this);
                        startActivity(new Intent(LoginRegisterActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else {
                        if (lr != null)
                            UserIntraction.makeSnack(coordinatorLayout, lr.getMessage());
                    }

                } else {
                    //handle all error like 404,500 etc server errors based on request
                    UserIntraction.makeSnack(coordinatorLayout, "Error Code: " + response.raw().code() + "  " + response.raw().message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (pd != null) pd.dismiss();
                //  Log.d(TAG, t.getMessage());
                UserIntraction.makeSnack(coordinatorLayout, getString(R.string.internet_error));
            }
        });
    }

    private void register() {
        if (pd != null) pd.show();

        Call<Message> call = ServiceGenerator.getService().registerUser(etNameRegister.getText().toString(), etEmailRegister.getText().toString(), etCarnoRegister.getText().toString(), etPassRegister.getText().toString()); //retrofit service call
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Response<Message> response, Retrofit retrofit) {
                if (pd != null) pd.dismiss();
                if (response.isSuccess()) {

                    Message msg = response.body();
                    if (msg.isType()) {
                        // if response has a type true means registration is successful then show msg in green and redirect to the login register activity
                        UserIntraction.makeSnack(coordinatorLayout, msg.getMessage());
                    } else {
                        UserIntraction.makeSnack(coordinatorLayout, msg.getMessage());
                    }

                    Log.d("Anki", msg.toString());
                } else {
                    if (pd != null) pd.dismiss();
                    Snackbar.make(coordinatorLayout, response.raw().message().toString(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (pd != null) pd.dismiss();
                Snackbar.make(coordinatorLayout, t.getMessage().toString(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(LoginRegisterActivity.this, acct.getDisplayName() + " " + acct.getEmail() + " " + acct.getPhotoUrl(), Toast.LENGTH_LONG).show();
            //  updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //  updateUI(false);
        }
    }


    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        //if connection error with g+ login
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            GoogleApiAvailability.getInstance().getErrorDialog(LoginRegisterActivity.this, result.getErrorCode(), 100211);
            mResolvingError = true;
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        for (int i = 0; i < googleSignInButton.getChildCount(); i++) {
            View v = googleSignInButton.getChildAt(i);
            //changing text on google button based on space or size available to button
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                Log.d("width", String.valueOf(googleSignInButton.getWidth()));
                if (googleSignInButton.getWidth() > 500) {
                    tv.setText(getString(R.string.google_signin_button_text_long));
                } else {
                    tv.setText(getString(R.string.google_signin_button_text_small));
                }
                tv.setPadding(2, 2, 2, 2);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.3f);
                tv.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                return;
            }
        }
    }

}
