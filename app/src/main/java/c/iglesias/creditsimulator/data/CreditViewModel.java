package c.iglesias.creditsimulator.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import c.iglesias.creditsimulator.db.entity.CreditEntity;
import c.iglesias.creditsimulator.db.entity.DetailCreditEntity;

public class CreditViewModel extends AndroidViewModel {
    private LiveData<List<CreditEntity>> allCredits;
    private CreditRepository repository;
    private Snackbar snackbar;

    public CreditViewModel(@NonNull Application application) {
        super(application);

        repository = new CreditRepository(application);
        allCredits = repository.getAllCredits();
    }

    public LiveData<List<CreditEntity>> getAllCredits() {
        return allCredits;
    }

    public LiveData<CreditEntity> getCredit(int idCredit) {
        return repository.getCredit(idCredit);
    }

    public void insertCredit(CreditEntity creditEntity, List<DetailCreditEntity> list) {
        repository.insertCredit(creditEntity, list);
    }

    public void updateCredit(CreditEntity creditEntity) {
        repository.updateCredit(creditEntity);
    }

    public LiveData<List<DetailCreditEntity>> getDetailCredit(int idCredit) {
        return repository.getDetailCredit(idCredit);
    }

    public void setSnackbar(Snackbar snackbar) {
        this.snackbar = snackbar;
    }

    public void showSnackBar(String text) {
        snackbar.setText(text);
        snackbar.show();
    }

    public void showEditDialog(){

    }

    public void deleteCredit(int idCredit){
        repository.deleteCredit(idCredit);
    }


}
