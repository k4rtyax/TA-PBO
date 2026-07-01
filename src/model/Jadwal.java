package model;

public class Jadwal {

    private int idJadwal;
    private Dokter dokter;
    private Poli poli;
    private String hari;
    private String jamMulai;
    private String jamSelesai;

    public Jadwal() {
    }

    public Jadwal(int idJadwal,
                  Dokter dokter,
                  Poli poli,
                  String hari,
                  String jamMulai,
                  String jamSelesai) {

        this.idJadwal = idJadwal;
        this.dokter = dokter;
        this.poli = poli;
        this.hari = hari;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
    }

    public int getIdJadwal() {
        return idJadwal;
    }

    public void setIdJadwal(int idJadwal) {
        this.idJadwal = idJadwal;
    }

    public Dokter getDokter() {
        return dokter;
    }

    public void setDokter(Dokter dokter) {
        this.dokter = dokter;
    }

    public Poli getPoli() {
        return poli;
    }

    public void setPoli(Poli poli) {
        this.poli = poli;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getJamMulai() {
        return jamMulai;
    }

    public void setJamMulai(String jamMulai) {
        this.jamMulai = jamMulai;
    }

    public String getJamSelesai() {
        return jamSelesai;
    }

    public void setJamSelesai(String jamSelesai) {
        this.jamSelesai = jamSelesai;
    }

    // untuk tableview
    public String getNamaDokter() {
        return dokter != null ? dokter.getNama() : "";
    }

    public String getNamaPoli() {
        return poli != null ? poli.getNamaPoli() : "";
    }

    @Override
    public String toString() {
        return hari + " " + jamMulai + "-" + jamSelesai;
    }
}
