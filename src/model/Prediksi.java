package model;

public class Prediksi
        implements Predictable {

    private int idPrediksi;
    private String hasilPrediksi;
    private double probabilitas;

    // RELASI OBJECT
    private Pasien pasien;

    public Prediksi(int idPrediksi,
                    Pasien pasien) {

        this.idPrediksi = idPrediksi;
        this.pasien = pasien;
    }

    @Override
    public void prosesPrediksi() {

        if(pasien.getGulaDarah() > 200){

            hasilPrediksi =
                    "Risiko Diabetes Tinggi";

            probabilitas = 0.92;

        } else {

            hasilPrediksi =
                    "Risiko Normal";

            probabilitas = 0.20;
        }
    }

    public void tampilHasil() {

        System.out.println(
                "Hasil Prediksi : "
                + hasilPrediksi);

        System.out.println(
                "Probabilitas : "
                + probabilitas);
    }
}
