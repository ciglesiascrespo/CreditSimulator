package c.iglesias.creditsimulator.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import c.iglesias.creditsimulator.db.entity.CreditEntity;

@Dao
public interface CreditDao {
    @Insert
    long insert(CreditEntity creditEntity);

    @Update
    void update(CreditEntity creditEntity);

    @Query("Delete from credit")
    void deleteAll();

    @Query("Delete from credit where id = :idCredit")
    void deleteById(int idCredit);

    @Query("Select * from credit order by name")
    LiveData<List<CreditEntity>> getAll();

    @Query("Select * from credit where id = :idCredit")
    LiveData<CreditEntity> getCredit(int idCredit);


}
