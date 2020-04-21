package id.putraprima.retrofit.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import net.khirr.android.privacypolicy.PrivacyPolicyDialog;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.AppVersion;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    TextView lblAppName, lblAppTittle, lblAppVersion;
    ConstraintLayout view;
    Context context;
    private SharedPreferences preferences;

    public static final String SHARED_PREFS = "sharedPrefs";
    private String app, version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        setupLayout();
        if (checkInternetConnection() == true) {
            privacyCheck();
        }
        setAppInfo();
    }

    private void setupLayout() {
        lblAppName = findViewById(R.id.lblAppName);
        lblAppTittle = findViewById(R.id.lblAppTittle);
        lblAppVersion = findViewById(R.id.lblAppVersion);

        //Sembunyikan lblAppName dan lblAppVersion pada saat awal dibuka
        //lblAppVersion.setVisibility(View.INVISIBLE);
        //lblAppName.setVisibility(View.INVISIBLE);
    }

    private boolean checkInternetConnection() {
        //TODO : 1. Implementasikan proses pengecekan koneksi internet, berikan informasi ke user jika tidak terdapat koneksi internet
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }else{
            //Toast.makeText(SplashActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            view = findViewById(R.id.splashConstrain);
            Snackbar.make(view, "Failed to get connection to the server", Snackbar.LENGTH_LONG).show();
            return false;
        }
    }


    private void setAppInfo() {
        //TODO : 5. Implementasikan proses setting app info, app info pada fungsi ini diambil dari shared preferences
        //lblAppVersion dan lblAppName dimunculkan kembali dengan data dari shared preferences
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        app = preferences.getString("appName", "");
        version = preferences.getString("appVersion", "");
        lblAppName.setText(app);
        lblAppName.setVisibility(View.VISIBLE);
        lblAppVersion.setText(version);
        lblAppVersion.setVisibility(View.VISIBLE);
    }

    private void mainActivity() {
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void checkAppVersion() {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<AppVersion> call = service.getAppVersion();
        call.enqueue(new Callback<AppVersion>() {
            @Override
            public void onResponse(Call<AppVersion> call, Response<AppVersion> response) {
                AppVersion app = response.body();
                Toast.makeText(SplashActivity.this, app.getApp(), Toast.LENGTH_SHORT).show();

                //Todo : 2. Implementasikan Proses Simpan Data Yang didapat dari Server ke SharedPreferences
                //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("appName", app.getApp());
                editor.putString("appVersion", app.getVersion());
                editor.apply();

                //Todo : 3. Implementasikan Proses Pindah Ke MainActivity Jika Proses getAppVersion() sukses
                mainActivity();
            }

            @Override
            public void onFailure(Call<AppVersion> call, Throwable t) {
                //Toast.makeText(SplashActivity.this, "Gagal Koneksi Ke Server", Toast.LENGTH_SHORT).show();
                //Todo : 4. Implementasikan Cara Notifikasi Ke user jika terjadi kegagalan koneksi ke server silahkan googling cara yang lain selain menggunakan TOAST
                Snackbar.make(view, "Failed to get connection to the server", Snackbar.LENGTH_LONG).show();
            }
        });
    }
    private void privacyCheck() {
        PrivacyPolicyDialog dialog = new PrivacyPolicyDialog(this,
                "https://localhost/terms",
                "https://localhost/privacy");

        // Dialog
        dialog.addPoliceLine("This application uses a unique user identifier for advertising purposes, it is shared with third-party companies.");
        dialog.addPoliceLine("This application sends error reports, installation and send it to a server of the Fabric.io company to analyze and process it.");
        dialog.addPoliceLine("This application requires internet access and must collect the following information: Installed applications and history of installed applications, ip address, unique installation id, token to send notifications, version of the application, time zone and information about the language of the device.");
        dialog.addPoliceLine("All details about the use of data are available in our Privacy Policies, as well as all Terms of Service links below.");

        // Title Dialog
        dialog.setTitle("Terms Of Services");

        // Terms
        dialog.setTermsOfServiceSubtitle("If you click on (accept), you acknowledge that is makes the bla bla");
        // Dialog set
        dialog.setTitleTextColor(Color.parseColor("#222222"));
        dialog.setAcceptButtonColor(ContextCompat.getColor(this, R.color.colorAccent));
        dialog.show();

        final Intent intent = new Intent(this, MainActivity.class);

        dialog.setOnClickListener(new PrivacyPolicyDialog.OnClickListener() {
            @Override
            public void onAccept(boolean b) {
                Log.e("SplashActivity", "Policies accepted");
                startActivity(intent);
                finish();

            }

            @Override
            public void onCancel() {
                Log.e("SplashActivity", "Policies not accepted");
                finish();
            }
        });
    }


}
