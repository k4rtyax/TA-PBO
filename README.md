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

### 2. Git Workflow
Agar tidak conflict, gunakan perintah berikut:

**mulai ngoding (Tarik update terbaru):**
```bash
git pull origin main
```

**selesai ngoding (Simpan & Upload):**
```bash
git add .
git commit -m "Update UI Form Dokter (by: kevin)" <-- contoh
git push origin main
```

## Checklist Pengembangan Kelompok

### Modul Data Dokter (CRUD)
- [ ] Buat tabel dokter di database (id, nama, spesialis, no_hp, alamat)
- [ ] Buat class Model Dokter.java
- [ ] Buat class DokterDAO.java
- [ ] Buat class DokterService.java
- [ ] Buat UI form_dokter.fxml dan dokter.fxml
- [ ] Buat Controller untuk dokter dan sambungkan ke Dashboard

### Modul Prediksi Risiko Diabetes (Machine Learning)
- [ ] Buat script Python (train.py) untuk melatih model dari dataset
- [ ] Simpan hasil model machine learning (.pkl)
- [ ] Buat script Python (predict.py) untuk menjalankan prediksi
- [ ] Buat UI prediksi.fxml untuk memasukkan data medis pasien
- [ ] Buat Controller Prediksi dan integrasikan dengan Python menggunakan ProcessBuilder

### Modifikasi Tambahan(todo)
- [ ] Tambahkan field No HP dan Alamat pada data Pasien
- [ ] Tambahkan field Gula Darah dan Tekanan Darah
- [ ] Berikan validasi input kosong pada form
- [ ] Tampilkan jumlah total pasien di Dashboard

---