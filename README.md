# Smart Clinic - Tugas PBO

### 1. Setup Project

**A. Cek Dependensi:** 
```bash
java -version
javac -version
python3 --version
```

**B. Clone & Setup Database (Linux/LAMPP):**
```bash
git clone https://github.com/k4rtyax/TA-PBO.git
cd TA-PBO
sudo /opt/lampp/lampp start
/opt/lampp/bin/mysql -u root -e "CREATE DATABASE IF NOT EXISTS smart_clinic;"
/opt/lampp/bin/mysql -u root smart_clinic < smartClinic.sql
```

### 2. Cara Mengompilasi & Menjalankan Aplikasi

Tersedia skrip pembantu untuk mempermudah proses kompilasi JavaFX dan penyalinan aset/resources (seperti berkas `.fxml` dan `.css`) ke direktori output `bin`.

**A. Kompilasi:**
```bash
./compile.sh
```
*(Catatan: Jika letak library JavaFX SDK Anda berbeda dari default skrip, Anda dapat mengekspor path terlebih dahulu: `export PATH_TO_FX=/path/to/javafx/lib` sebelum menjalankan skrip).*

**B. Menjalankan Aplikasi:**
```bash
./run.sh
```

### 3. Git Workflow
Agar tidak conflict, gunakan perintah berikut:

**mulai ngoding (Tarik update terbaru):**
```bash
git pull origin main
```

**selesai ngoding (Simpan & Upload):**
```bash
git add src/ compile.sh run.sh smartClinic.sql README.md
git commit -m "feat: implementasi lengkap seluruh modul, statistik dashboard, dan penyempurnaan UI"
git push origin main
```

## Checklist Pengembangan Kelompok

### Modul Data Dokter (CRUD)
- [x] Buat tabel dokter di database (id, nama, spesialis, no_hp, alamat)
- [x] Buat class Model Dokter.java
- [x] Buat class DokterDAO.java
- [x] Buat class DokterService.java
- [x] Buat UI form_dokter.fxml dan dokter.fxml
- [x] Buat Controller untuk dokter dan sambungkan ke Dashboard

### Modul Prediksi Risiko Diabetes (Machine Learning)
- [x] Buat script Python (train.py) untuk melatih model dari dataset
- [x] Simpan hasil model machine learning (.pkl)
- [x] Buat script Python (predict.py) untuk menjalankan prediksi
- [x] Buat UI prediksi.fxml untuk memasukkan data medis pasien
- [x] Buat Controller Prediksi dan integrasikan dengan Python menggunakan ProcessBuilder

### Modul Tambahan & Integrasi Dashboard
- [x] Modul Pendaftaran Periksa (UI & CRUD lengkap)
- [x] Modul Pemeriksaan Dokter (UI & CRUD lengkap, sinkronisasi data vital ke database pasien)
- [x] Modul Master Data Obat (UI & CRUD lengkap)
- [x] Modul Manajemen Petugas/Users (UI & CRUD lengkap)
- [x] Modul Laporan Rekam Medis (UI & CRUD lengkap)
- [x] Dashboard dinamis dengan statistik real-time dari database

### Modifikasi Tambahan
- [x] Tambahkan field No HP dan Alamat pada data Pasien
- [x] Tambahkan field Gula Darah dan Tekanan Darah
- [x] Berikan validasi input kosong pada form
- [x] Tampilkan jumlah total pasien di Dashboard
- [x] Perbaikan kompatibilitas tampilan warna teks label pada sistem dengan Dark Mode aktif