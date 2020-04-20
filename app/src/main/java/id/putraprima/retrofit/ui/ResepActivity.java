package id.putraprima.retrofit.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.adapter.ResepAdapter;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.Data;
import id.putraprima.retrofit.api.models.Resep;
import id.putraprima.retrofit.api.models.ResepList;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResepActivity extends AppCompatActivity {

    ArrayList<Resep> resep;
    ResepAdapter adapter;
    int i = 1;


    int n = 0, x=1, y=1;
    private ConstraintLayout modelResep;
    private Button btNext, btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resep);

        resep = new ArrayList<>();

        RecyclerView recipeView = findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recipeView.setLayoutManager(layoutManager);

        adapter = new ResepAdapter(resep);
        recipeView.setAdapter(adapter);
        modelResep = findViewById(R.id.recipeLayout);
        btNext = findViewById(R.id.btnNext);
        btBack = findViewById(R.id.btnBack);



        doLoadList();
        btBack.setEnabled(false);
        btNext.setEnabled(false);


    }

    public void doRecipe() {

        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Data<List<ResepList>>> call = service.doRecipe();

        call.enqueue(new Callback<Data<List<ResepList>>>() {
            @Override
            public void onResponse(Call<Data<List<ResepList>>> call, Response<Data<List<ResepList>>> response) {

                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getData().size(); i++) {

                        int id = response.body().getData().get(i).getId();
                        String namaResep = response.body().getData().get(i).getNama_resep();
                        String deskripsi = response.body().getData().get(i).getDeskripsi();
                        String bahan = response.body().getData().get(i).getBahan();
                        String langkahPembuatan = response.body().getData().get(i).getLangkah_pembuatan();
                        String foto = response.body().getData().get(i).getFoto();
                        resep.add(new Resep(id, namaResep, deskripsi, bahan, langkahPembuatan, foto));

                    }

                    ProgressDialog dialog = ProgressDialog.show(ResepActivity.this, "",
                            "Loading. Please wait...");

                    dialog.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                            btBack.setEnabled(false);
                            btNext.setEnabled(true);
                        }
                    }, 2500);

                }
                else if(response.body() == null) {
                    Snackbar snackbar = Snackbar.make(modelResep, "Data Kosong", Snackbar.LENGTH_SHORT);
                    snackbar.show();
//                else {
//
//
//                    new AlertDialog.Builder(ResepActivity.this)
//                            .setTitle("Loading Data Gagal!")
//                            .setMessage("cek koneksi anda")
//                            .setCancelable(false)
//                            .setPositiveButton("reload", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    doLoadList();
//                                }
//                            }).show();
                }

            }


            @Override
            public void onFailure(Call<Data<List<ResepList>>> call, Throwable t) {

                new AlertDialog.Builder(ResepActivity.this)
                        .setTitle("Loading Data Gagal!")
                        .setMessage("cek koneksi anda")
                        .setCancelable(false)
                        .setPositiveButton("reload", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doLoadList();
                            }
                        }).show();
            }
        });

    }

    public void doNextPage() {

        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);

        Call<Data<List<ResepList>>> call = service.doNextPage(i+=1);

        call.enqueue(new Callback<Data<List<ResepList>>>() {
            @Override
            public void onResponse(Call<Data<List<ResepList>>> call, Response<Data<List<ResepList>>> response) {

               if (response.isSuccessful()) {

                    for (y = 0; y < response.body().getData().size(); y++) {
                        int id = response.body().getData().get(y).getId();
                        String namaResep = response.body().getData().get(y).getNama_resep();
                        String deskripsi = response.body().getData().get(y).getDeskripsi();
                        String bahan = response.body().getData().get(y).getBahan();
                        String langkahPembuatan = response.body().getData().get(y).getLangkah_pembuatan();
                        String foto = response.body().getData().get(y).getFoto();
                        resep.add(new Resep(id, namaResep, deskripsi, bahan, langkahPembuatan, foto));
                        n=x+y;

                    }




                    ProgressDialog dialog = ProgressDialog.show(ResepActivity.this, "",
                            "Loading. Please wait...");

                    dialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                            btBack.setEnabled(true);
                            btNext.setEnabled(true);
                        }
                    }, 2500);



                }

                if(y == 0) {


                    new AlertDialog.Builder(ResepActivity.this)
                            .setTitle("Data Kosong!")
                            .setCancelable(false)
                            .setPositiveButton("BACK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    doLoadBack();
                                }
                            }).show();
                }
            }
     ;


            @Override
            public void onFailure(Call<Data<List<ResepList>>> call, Throwable t) {

                new AlertDialog.Builder(ResepActivity.this)
                        .setTitle("Loading Data Gagal!")
                        .setMessage("cek koneksi anda")
                        .setCancelable(false)
                        .setPositiveButton("reload", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doLoadList();
                            }
                        }).show();
            }
        });

    }

    public void doLoadBack() {

        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);

        Call<Data<List<ResepList>>> call = service.doNextPage(i-=1);

        call.enqueue(new Callback<Data<List<ResepList>>>() {
            @Override
            public void onResponse(Call<Data<List<ResepList>>> call, Response<Data<List<ResepList>>> response) {

                if (response.isSuccessful()) {

                    for (y = 0; y < response.body().getData().size(); y++) {
                        int id = response.body().getData().get(y).getId();
                        String namaResep = response.body().getData().get(y).getNama_resep();
                        String deskripsi = response.body().getData().get(y).getDeskripsi();
                        String bahan = response.body().getData().get(y).getBahan();
                        String langkahPembuatan = response.body().getData().get(y).getLangkah_pembuatan();
                        String foto = response.body().getData().get(y).getFoto();
                        resep.add(new Resep(id, namaResep, deskripsi, bahan, langkahPembuatan, foto));
                        n=x+y;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                btBack.setEnabled(true);
                                btNext.setEnabled(true);
                            }
                        }, 1000);


                    }

                }
                if(i == 1) {
//                    Snackbar snackbar = Snackbar.make(modelResep, "Data Kosong", Snackbar.LENGTH_SHORT);
//                    snackbar.show();
                     doLoadList();
//                     btBack.setEnabled(false);
//                    new AlertDialog.Builder(ResepActivity.this)
//                            .setTitle("Data Kosong!")
//                            .setCancelable(false)
//                            .setPositiveButton("BACK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    doLoadNext();
//                                }
//                            }).show();
                }
            }


            @Override
            public void onFailure(Call<Data<List<ResepList>>> call, Throwable t) {
//                Snackbar snackbar = Snackbar.make(mRecipeLayout, "gagal koneksi", Snackbar.LENGTH_SHORT);
//                snackbar.show();
                new AlertDialog.Builder(ResepActivity.this)
                        .setTitle("Loading Data Gagal!")
                        .setMessage("cek koneksi anda")
                        .setCancelable(false)
                        .setPositiveButton("reload", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doLoadList();
                            }
                        }).show();
            }
        });

    }



    public void doLoadList() {

        resep.clear();
        adapter.notifyDataSetChanged();

        doRecipe();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }, 3000);


    }

    public void doLoadNext() {

        resep.clear();
        adapter.notifyDataSetChanged();

        doNextPage();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }, 3000);
    }

    public void handleNextPage(View view) {

        doLoadNext();
        btBack.setEnabled(false);
        btNext.setEnabled(false);
    }


    public void handleBack(View view) {
        resep.clear();
        adapter.notifyDataSetChanged();

        doLoadBack();
        btBack.setEnabled(false);
        btNext.setEnabled(false);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }, 1000);

    }
}
