package c.iglesias.creditsimulator.db.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "credit", indices ={@Index(value = "id", unique = true)})
public class CreditEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public int value;
    public int duration;
    public String nameBank;
    public double tasa;

    public CreditEntity(String name, int value, int duration, String nameBank, double tasa) {
        this.name = name;
        this.value = value;
        this.duration = duration;
        this.nameBank = nameBank;
        this.tasa = tasa;
    }

    public double getTasa() {
        return tasa;
    }

    public void setTasa(double tasa) {
        this.tasa = tasa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getNameBank() {
        return nameBank;
    }

    public void setNameBank(String nameBank) {
        this.nameBank = nameBank;
    }
}
