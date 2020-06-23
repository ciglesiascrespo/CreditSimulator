package c.iglesias.creditsimulator.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "credit_detail", foreignKeys = @ForeignKey(entity = CreditEntity.class,
        parentColumns = "id",
        childColumns = "idCredit",
        onDelete = ForeignKey.CASCADE))
public class DetailCreditEntity {

    public long idCredit;
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int valorCuota;
    public int valorInteres;
    public int valorCapital;
    public int valorSaldo;
    public int numCuota;

    public DetailCreditEntity() {
        this.idCredit = 0;
        this.valorCuota = 0;
        this.valorInteres = 0;
        this.valorCapital = 0;
        this.valorSaldo = 0;
        this.numCuota = 0;
    }

    public DetailCreditEntity(long idCredit, int valorCuota, int valorInteres, int valorCapital, int valorSaldo, int numCuota) {
        this.idCredit = idCredit;
        this.valorCuota = valorCuota;
        this.valorInteres = valorInteres;
        this.valorCapital = valorCapital;
        this.valorSaldo = valorSaldo;
        this.numCuota = numCuota;
    }

    public int getNumCuota() {
        return numCuota;
    }

    public void setNumCuota(int numCuota) {
        this.numCuota = numCuota;
    }

    public long getIdCredit() {
        return idCredit;
    }

    public void setIdCredit(long idCredit) {
        this.idCredit = idCredit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValorCuota() {
        return valorCuota;
    }

    public void setValorCuota(int valorCuota) {
        this.valorCuota = valorCuota;
    }

    public int getValorInteres() {
        return valorInteres;
    }

    public void setValorInteres(int valorInteres) {
        this.valorInteres = valorInteres;
    }

    public int getValorCapital() {
        return valorCapital;
    }

    public void setValorCapital(int valorCapital) {
        this.valorCapital = valorCapital;
    }

    public int getValorSaldo() {
        return valorSaldo;
    }

    public void setValorSaldo(int valorSaldo) {
        this.valorSaldo = valorSaldo;
    }

    @NonNull
    @Override
    public String toString() {
        String str = "Cuota: " + getNumCuota() + ", Capital: " + getValorCapital() + ", Interes: " + getValorInteres() + ", Saldo: " + getValorSaldo();
        return str;
    }
}
