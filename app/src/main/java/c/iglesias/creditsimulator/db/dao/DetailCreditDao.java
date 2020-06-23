package c.iglesias.creditsimulator.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import c.iglesias.creditsimulator.db.entity.DetailCreditEntity;

@Dao
public interface DetailCreditDao {

    @Insert
    Long insert(DetailCreditEntity detailCreditEntity);

    @Insert
    void insertAll(List<DetailCreditEntity> list);

    @Transaction
    @Query("select * from credit_detail where idCredit = :idCredit order by numCuota")
    LiveData<List<DetailCreditEntity>> getDetailByIdCredit(int idCredit);

    @Transaction
    @Query("select * from credit_detail  order by idCredit,numCuota")
    LiveData<List<DetailCreditEntity>> getAllDetail();

    @Query("Delete from credit_detail where idCredit =:idCredit")
    void deleteByIdCredit(int idCredit);


}
