package c.iglesias.creditsimulator.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import c.iglesias.creditsimulator.R;
import c.iglesias.creditsimulator.data.CreditViewModel;
import c.iglesias.creditsimulator.db.entity.CreditEntity;

import static android.view.View.GONE;
import static c.iglesias.creditsimulator.common.Utils.getCuota;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CreditViewModel viewModel;
    CreditListAdapter adapter;
    FloatingActionButton fabNewCredit;
    FrameLayout frameLayout;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initAd();
        initViewModel();
    }

    private void initAd() {
        MobileAds.initialize(this, getString(R.string.str_id_app_admob));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    private void initView() {

        recyclerView = findViewById(R.id.idRecyclerListCredit);
        fabNewCredit = findViewById(R.id.fab);
        frameLayout = findViewById(R.id.idFrameLayout);

        List<CreditEntity> list = new ArrayList<>();
        adapter = new CreditListAdapter(list, this);
        recyclerView.setAdapter(adapter);

        fabNewCredit.setOnClickListener(V -> showDialogNewCredit());


    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this)
                .get(CreditViewModel.class);

        viewModel.setSnackbar(Snackbar.make(recyclerView, "", Snackbar.LENGTH_SHORT));
        viewModel.getAllCredits().observe(this,
                creditEntities -> {
                    adapter.setListItems(creditEntities);
                    if (creditEntities.size() > 0) {
                        frameLayout.setVisibility(GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        frameLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(GONE);
                    }
                });
    }

    private void showDialogNewCredit() {
        FragmentManager fm = getSupportFragmentManager();
        NewCreditDialogFragment dialog = new NewCreditDialogFragment();
        dialog.show(fm, "TAG");
    }


}
