package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.AppVersion;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    private TextView appName, appVersion;
    private EditText edtEmail, edtPassword;
    private Button BtnLoad;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtEmail = findViewById(R.id.mainEdtEmail);
        edtPassword = findViewById(R.id.mainEdtPassword);
        BtnLoad = findViewById(R.id.btnLoad);
        setupLayout();
        checkAppVersion();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        // Reload AdListener
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
        //When user click again


        // Add Click
        BtnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    // Reload Ads
                    showInterstitial();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });
        // Ad Events
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Toast.makeText(MainActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(MainActivity.this,
                        "onAdFailedToLoad() with error code: " + errorCode,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//              permissions();
                changePage();

            }
        });


    }
    //
//     showing ads Intersitial
    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupLayout(){
        appName = findViewById(R.id.mainTxtAppName);
        appVersion = findViewById(R.id.mainTxtAppVersion);
    }

    private void checkAppVersion(){
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<AppVersion> call = service.getAppVersion();
        call.enqueue(new Callback<AppVersion>(){

            @Override
            public void onResponse(Call<AppVersion> call, Response<AppVersion> response) {
                AppVersion app = response.body();
                appName.setText(app.getApp());
                appVersion.setText(app.getVersion());
            }
            @Override
            public void onFailure(Call<AppVersion> call, Throwable t) {
                Toast.makeText(MainActivity.this, "failed to connect server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String token;
    public void handleLogin(View view){
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        LoginRequest login = new LoginRequest(email,password);
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<LoginResponse> call = service.doLogin(login);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse respon = response.body();
                if(response.isSuccessful()){
                    SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(token, respon.getToken());
                    editor.apply();
                    Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(i);
                }else{
                    ApiError error = ErrorUtils.parseError(response);
                    if (email.length() == 0 && password.length() == 0) {
                        Toast.makeText(MainActivity.this, error.getError().getEmail().get(0) + "and" + error.getError().getPassword().get(0),  Toast.LENGTH_SHORT).show();

                    }
                    else if(email.length() == 0){
                        Toast.makeText(MainActivity.this, error.getError().getEmail().get(0),Toast.LENGTH_SHORT).show();
                    }else if(password.length() == 0) {
                        Toast.makeText(MainActivity.this, error.getError().getPassword().get(0),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, error.getError().getEmail().get(0),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "login is not correct", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void moveRegisterActivity(View view) {
        Intent i = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    public void handleResep(View view) {
        Intent intent = new Intent(this, ResepActivity.class);
        startActivity(intent);
    }

    private void changePage() {
        Intent intent = new Intent(this, ResepActivity.class);
        startActivity(intent);
    }
}
