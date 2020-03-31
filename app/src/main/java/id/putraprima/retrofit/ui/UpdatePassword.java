package id.putraprima.retrofit.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.API_Error;
import id.putraprima.retrofit.api.models.DataKey;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.UpdatePasswordRequest;
import id.putraprima.retrofit.api.models.UpdatePasswordResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePassword extends AppCompatActivity {
    private EditText passInput,cpassInput;
    private DataKey dataKey;
    private String pass,cpass,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        passInput = findViewById(R.id.newpasswordInput);
        cpassInput = findViewById(R.id.newcpasswordInput);
        dataKey = new DataKey(this);
        token = dataKey.getTokenType() + " " + dataKey.getToken();
    }

    public void handleSubmitPass(View view){
        pass = passInput.getText().toString();
        cpass = cpassInput.getText().toString();
//        boolean status = pass.equals("") && cpass.equals("");
//        boolean confirm = pass.equals(cpass);
//        boolean dataLength = pass.length() < 8 && cpass.length() < 8;
//        if (status){
//            Toast.makeText(this, "Lengkapi Data", Toast.LENGTH_SHORT).show();
//            if (confirm) Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show();
//            if (dataLength) Toast.makeText(this, "character minimal 8", Toast.LENGTH_SHORT).show();
//        }else{
            editPass();
//        }
    }

    public void editPass(){
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<UpdatePasswordResponse> call = service.updatePassword(token,new UpdatePasswordRequest(pass,cpass));
        call.enqueue(new Callback<UpdatePasswordResponse>() {
            @Override
            public void onResponse(Call<UpdatePasswordResponse> call, Response<UpdatePasswordResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(UpdatePassword.this,"Update Password Berhasil", Toast.LENGTH_SHORT).show();

                }else{
                    API_Error error = ErrorUtils.parseError(response);
                    int i = 0;
                    StringBuilder sb = new StringBuilder();
                    while (i < error.getError().getPassword().size()){

                        Toast.makeText(UpdatePassword.this, error.getError().getPassword().get(i), Toast.LENGTH_SHORT).show();
                            i++;
                    }
                }

            }

            @Override
            public void onFailure(Call<UpdatePasswordResponse> call, Throwable t) {
                Toast.makeText(UpdatePassword.this, "Gagal Koneksi ke Server", Toast.LENGTH_SHORT).show();
            }
        });

}

}
