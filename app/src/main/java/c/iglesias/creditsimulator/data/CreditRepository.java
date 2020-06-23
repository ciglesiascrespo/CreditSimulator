package c.iglesias.creditsimulator.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import c.iglesias.creditsimulator.db.CreditDataBase;
import c.iglesias.creditsimulator.db.dao.CreditDao;
import c.iglesias.creditsimulator.db.dao.DetailCreditDao;
import c.iglesias.creditsimulator.db.entity.CreditEntity;
import c.iglesias.creditsimulator.db.entity.DetailCreditEntity;

public class CreditRepository {
    private CreditDao dao;
    private DetailCreditDao detailCreditDao;
    private LiveData<List<CreditEntity>> allCredit;



    public CreditRepository(Application application) {
        CreditDataBase db = CreditDataBase.getDatabase(application);
        dao = db.getCreditDao();
        detailCreditDao = db.getDetailCreditDao();
        allCredit = dao.getAll();
    }

    public LiveData<List<CreditEntity>> getAllCredits() {
        return allCredit;
    }

    public void insertCredit(CreditEntity creditEntity, List<DetailCreditEntity> listDetail) {

        CreditDataBase.databaseWriteExecutor.execute(() -> {
            long idCredit = dao.insert(creditEntity);
            List<DetailCreditEntity> list = new ArrayList<>();

            for (DetailCreditEntity detail : listDetail) {
                detail.setIdCredit(idCredit);
                list.add(detail);
            }

            insertAllDetails(list);

        });

    }

    public void deleteCredit(int idCredit){
        CreditDataBase.databaseWriteExecutor.execute(()->{
            detailCreditDao.deleteByIdCredit(idCredit);
            dao.deleteById(idCredit);
        });
    }

    public void insertAllDetails(List<DetailCreditEntity> list) {
        CreditDataBase.databaseWriteExecutor.execute(() -> detailCreditDao.insertAll(list));
    }


    public void updateCredit(CreditEntity creditEntity) {
        CreditDataBase.databaseWriteExecutor.execute(() -> dao.update(creditEntity));
    }

    public LiveData<CreditEntity> getCredit(int idCredit){
       return dao.getCredit(idCredit);
    }

    public LiveData<List<DetailCreditEntity>> getDetailCredit(int idCredit) {
        return detailCreditDao.getDetailByIdCredit(idCredit);
    }

}
