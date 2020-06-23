package c.iglesias.creditsimulator.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import c.iglesias.creditsimulator.R;
import c.iglesias.creditsimulator.common.Constants;
import c.iglesias.creditsimulator.common.Utils;
import c.iglesias.creditsimulator.data.CreditViewModel;
import c.iglesias.creditsimulator.db.entity.DetailCreditEntity;

public class DetailActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private TextView txtPlazo, txtTasa, txtEntidad, txtValor, txtCuota, txtTotal;
    private DetailCreditListAdapter adapter;
    private CreditViewModel viewModel;
    private int idCredit, plazo;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    private Bundle bundle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        initAdmob();
        initViewModel();
        initValues();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_menu_delete:
                showConfirmDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initAdmob() {
        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId(getString(R.string.str_id_intersitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mAdView = findViewById(R.id.adViewDetail);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    private void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView = findViewById(R.id.idRecyclerDetail);
        txtEntidad = findViewById(R.id.idTxtEntidadDetail);

        txtPlazo = findViewById(R.id.idTxtPlazoDetail);
        txtTasa = findViewById(R.id.idTxtTasaDetail);
        txtValor = findViewById(R.id.idTxtValorDetail);
        txtCuota = findViewById(R.id.idTxtValorCuota);
        txtTotal = findViewById(R.id.idTxtTotal);


        bundle = getIntent().getBundleExtra(Constants.ARGS);

        idCredit = bundle.getInt(Constants.ARG_ID_CREDIT);

        List<DetailCreditEntity> list = new ArrayList<>();
        adapter = new DetailCreditListAdapter(list, this);
        recyclerView.setAdapter(adapter);


    }

    private void initValues() {
        viewModel.getCredit(idCredit).observe(this,entity -> {
            txtValor.setText(Utils.formatNumber(String.valueOf(entity.getValue()), false));
            txtTasa.setText(String.valueOf(entity.getTasa()));
            plazo= entity.getDuration();
            txtPlazo.setText(String.valueOf(entity.getDuration()));
            txtEntidad.setText(entity.getNameBank());
            getSupportActionBar().setTitle(entity.getName());
        });



        viewModel.getDetailCredit(idCredit).observe(this, detailCreditEntities -> {
            adapter.setListItems(detailCreditEntities);
            if (detailCreditEntities != null && detailCreditEntities.size() > 0) {
                int cuota = detailCreditEntities.get(0).getValorCuota();
                int total = cuota * plazo;
                bundle.putInt(Constants.ARG_CUOTA_CREDIT, cuota);
                txtCuota.setText(Utils.formatNumber(String.valueOf(cuota), false));
                txtTotal.setText(Utils.formatNumber(String.valueOf(total), false));
            }
        });

    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this)
                .get(CreditViewModel.class);

    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.str_title_delete_credit);
        builder.setPositiveButton(R.string.str_btn_aceptar, (dialog, id) -> {
            viewModel.deleteCredit(idCredit);
            finish();
        });
        builder.setNegativeButton(R.string.str_btn_cancelar, (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


}
