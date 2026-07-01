package model;

public class Poli {

    private int idPoli;
    private String namaPoli;
    private String deskripsi;
    private String lokasi;

    public Poli() {
    }

    public Poli(int idPoli,
                String namaPoli,
                String deskripsi,
                String lokasi) {

        this.idPoli = idPoli;
        this.namaPoli = namaPoli;
        this.deskripsi = deskripsi;
        this.lokasi = lokasi;
    }

    public int getIdPoli() {
        return idPoli;
    }

    public void setIdPoli(int idPoli) {
        this.idPoli = idPoli;
    }

    public String getNamaPoli() {
        return namaPoli;
    }

    public void setNamaPoli(String namaPoli) {
        this.namaPoli = namaPoli;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    @Override
    public String toString() {
        return namaPoli;
    }
}
