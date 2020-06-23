package c.iglesias.creditsimulator.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import c.iglesias.creditsimulator.db.dao.CreditDao;
import c.iglesias.creditsimulator.db.dao.DetailCreditDao;
import c.iglesias.creditsimulator.db.entity.CreditEntity;
import c.iglesias.creditsimulator.db.entity.DetailCreditEntity;

@Database(entities = {CreditEntity.class, DetailCreditEntity.class}, exportSchema = false, version = 1)
public abstract class CreditDataBase extends RoomDatabase {

    public abstract CreditDao getCreditDao();
    public abstract DetailCreditDao getDetailCreditDao();

    private static volatile CreditDataBase INSTANCE;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public static CreditDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CreditDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, CreditDataBase.class, "CreditDataBase")
                            .build();
                }
            }
        }

        return INSTANCE;

    }
}
