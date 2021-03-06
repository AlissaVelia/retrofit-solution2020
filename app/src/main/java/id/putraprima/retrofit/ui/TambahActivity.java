package id.putraprima.retrofit.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.services.ApiInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahActivity extends AppCompatActivity {

    private Uri imageUri;
    private Bitmap imageBitmap;
    private ImageButton mRecipeImage;

    private String nama_resep, deskripsi, bahan, langkah_pembuatan;
    private EditText mBahanText;
    private EditText mLangkahText;
    private EditText mNamaResepText;
    private EditText mDeskripsiText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);
        mRecipeImage = findViewById(R.id.buttonImage);
        mBahanText = findViewById(R.id.bahanText);
        mLangkahText = findViewById(R.id.langkahText);
        mNamaResepText = findViewById(R.id.namaResepText);
        mDeskripsiText = findViewById(R.id.deskripsiText);
    }

    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() +"_image.png");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG,0, bos);
        byte[] bitmapdata = bos.toByteArray();
        //write the bytes in file

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MultipartBody.FORM, descriptionString);
    }

    public void doUpload() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        nama_resep = mNamaResepText.getText().toString();
        deskripsi = mDeskripsiText.getText().toString();
        bahan = mBahanText.getText().toString();
        langkah_pembuatan = mLangkahText.getText().toString();

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("fk_user", createPartFromString("1"));
        map.put("nama_resep", createPartFromString(nama_resep));
        map.put("deskripsi", createPartFromString(deskripsi));
        map.put("bahan", createPartFromString(bahan));
        map.put("langkah_pembuatan", createPartFromString(langkah_pembuatan));
        map.put("token", createPartFromString(preference.getString("token", null)));

        File file = createTempFile(imageBitmap);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("foto", file.getName(), reqFile);

        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<ResponseBody> call = service.doUpload(body,map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    ProgressDialog dialog = ProgressDialog.show(TambahActivity.this, "",
                            "Sedang menambahkan resep, Sabar...");

                    dialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                            Intent i = new Intent(TambahActivity.this, ResepActivity.class);
                            startActivity(i);

                        }
                    }, 4000);

                }else{
                    ApiError error = ErrorUtils.parseError(response);
                    if (error.getError().getNama_resep()!=null){
                        Toast.makeText(TambahActivity.this, error.getError().getNama_resep().get(0), Toast.LENGTH_SHORT).show();
                    }else if (error.getError().getDeskripsi()!=null){
                        Toast.makeText(TambahActivity.this, error.getError().getDeskripsi().get(0), Toast.LENGTH_SHORT).show();
                    }else if (error.getError().getBahan()!=null){
                        Toast.makeText(TambahActivity.this, error.getError().getBahan().get(0), Toast.LENGTH_SHORT).show();
                    }else if (error.getError().getLangkah_pembuatan()!=null){
                        Toast.makeText(TambahActivity.this, error.getError().getLangkah_pembuatan().get(0), Toast.LENGTH_SHORT).show();
                    }else if (error.getError().getFoto()!=null){
                        Toast.makeText(TambahActivity.this, error.getError().getFoto().get(0), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(TambahActivity.this, "Koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            return;
        }
        if (requestCode == 1) {
            if (data != null) {
                imageUri = data.getData();
                imageBitmap = (Bitmap) data.getExtras().get("data");
                mRecipeImage.setImageBitmap(imageBitmap);
            }
        }
    }

    public void handleChooseImage(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    public void handleUpload(View view) {
        if (imageBitmap!=null){
            doUpload();
        }else{
            Toast.makeText(this, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
    }
}
