package c.iglesias.creditsimulator.common;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import c.iglesias.creditsimulator.db.entity.DetailCreditEntity;

public class Utils {

    public static String formatNumber(String originalString, boolean simbol) {

        Long longval;
        if (originalString.contains(",")) {
            originalString = originalString.replaceAll(",", "");
        }
        longval = Long.parseLong(originalString);

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formattedString = formatter.format(longval);

        return (simbol ? "$ " : "") + formattedString;
    }

    public static List<DetailCreditEntity> getDetailFromPlazo(int valor, double tasa, int plazo) {
        List<DetailCreditEntity> list = new ArrayList<>();
        double tasaPorc = tasa / 100;
        double cuota = getCuota(valor, tasaPorc, plazo);

        int valorCuota = (int) Math.round(cuota);
        DetailCreditEntity detailCreditEntity = new DetailCreditEntity();
        for (int numCuota = 1; numCuota <= plazo; numCuota++) {

            int valorInteres = 0;
            int valorCapital = 0;
            int valorSaldo = 0;


            if (numCuota == 1) {
                valorInteres = (int) Math.round(valor * tasaPorc);
                valorCapital = valorCuota - valorInteres;
                valorSaldo = valor - valorCapital;
            } else {
                valorInteres = (int) Math.round(detailCreditEntity.getValorSaldo() * tasaPorc);
                valorCapital = valorCuota - valorInteres;
                valorSaldo = detailCreditEntity.getValorSaldo() - valorCapital;
            }

            detailCreditEntity = new DetailCreditEntity(-1, valorCuota, valorInteres, valorCapital, valorSaldo, numCuota);
            list.add(detailCreditEntity);

            Log.e("Test", detailCreditEntity.toString());
        }
        return list;
    }

    public static double getPlazo(int valor, double tasaPorc, int valorCuota) {
        return logbn((1 + tasaPorc), (valorCuota / (valorCuota - valor * tasaPorc)));
    }

    public static double getCuota(int valor, double tasaPorc, int plazo) {
        return (valor * (tasaPorc * (Math.pow((1 + tasaPorc), plazo)))) / (Math.pow((1 + tasaPorc), plazo) - 1);
    }

    public static List<DetailCreditEntity> getDetailFromCuota(int valor, double tasa, int valorCuota) {
        List<DetailCreditEntity> list = new ArrayList<>();
        double tasaPorc = tasa / 100;
        double plazoD = getPlazo(valor, tasaPorc, valorCuota);
        boolean flagEx = false;
        double ex = plazoD - (int) plazoD;
        int plazo = 0;
        flagEx = ex > 0;
        if (flagEx) {
            plazo = (int) plazoD + 1;
        }

        DetailCreditEntity detailCreditEntity = new DetailCreditEntity();
        for (int numCuota = 1; numCuota <= plazo; numCuota++) {

            int valorInteres = 0;
            int valorCapital = 0;
            int valorSaldo = 0;


            if (numCuota == 1) {
                valorInteres = (int) Math.round(valor * tasaPorc);
                valorCapital = valorCuota - valorInteres;
                valorSaldo = valor - valorCapital;
            } else {
                if (numCuota == plazo) {
                    valorInteres = (int) Math.round(detailCreditEntity.getValorSaldo() * tasaPorc);
                    valorCapital = (int) (((double) valorCuota) * ex);
                    valorSaldo = detailCreditEntity.getValorSaldo() - valorCapital;
                } else {
                    valorInteres = (int) Math.round(detailCreditEntity.getValorSaldo() * tasaPorc);
                    valorCapital = valorCuota - valorInteres;
                    valorSaldo = detailCreditEntity.getValorSaldo() - valorCapital;
                }

            }

            detailCreditEntity = new DetailCreditEntity(-1, valorCuota, valorInteres, valorCapital, valorSaldo, numCuota);
            list.add(detailCreditEntity);

            Log.e("Test", detailCreditEntity.toString());
        }
        return list;
    }

    public static double logbn(double base, double n) {
        return (Math.log(n) / Math.log(base));
    }
}
