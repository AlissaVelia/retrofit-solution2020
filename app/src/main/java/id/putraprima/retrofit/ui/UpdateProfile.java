package id.putraprima.retrofit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.Session;
import id.putraprima.retrofit.api.models.UpdateProfileRequest;
import id.putraprima.retrofit.api.models.UpdateProfileResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfile extends AppCompatActivity {
    private Session session;
    private EditText emailInput,nameInput;
    private String email,name,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        session = new Session(this);
        emailInput = findViewById(R.id.newEmailInput);
        nameInput = findViewById(R.id.newNameInput);
        token = session.getTokenType() + " " + session.getToken();
    }

    public void handleSubmitProf(View view){
        email = emailInput.getText().toString();
        name = nameInput.getText().toString();
        boolean status = email.equals("") && name.equals("");
        if(status)
            Toast.makeText(this, "Isi data terlebih dahulu", Toast.LENGTH_SHORT).show();
        else
            editprofile();
    }

    public void editprofile(){

        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        System.out.println("email : "+email);
        System.out.println("name : "+name);
        Call<UpdateProfileResponse> call = service.updateProfile(token,new UpdateProfileRequest(email,name));
        call.enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {

                if(response.body() != null){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfile.this);

                    Intent intent = new Intent(UpdateProfile.this, ProfileActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                Toast.makeText(UpdateProfile.this, "Data gagal", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

