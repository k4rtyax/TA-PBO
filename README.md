# Smart Clinic - Tugas PBO

### 1. Setup Project
**Cek Dependensi**: 
`java -version` (Cek JRE)
`javac -version` (Cek JDK)
`python3 --version` (Cek Python)

1. **Clone Repository**: Buka terminal/VS Code, lalu jalankan:
   `git clone https://github.com/k4rtyax/TA-PBO.git`
2. **Setup Database (Linux/LAMPP)**:
   - Jalankan service LAMPP melalui terminal: `sudo /opt/lampp/lampp start`
   - Buat database dan import data langsung via terminal (di dalam folder project):
     `/opt/lampp/bin/mysql -u root -e "CREATE DATABASE IF NOT EXISTS smart_clinic;"`
     `/opt/lampp/bin/mysql -u root smart_clinic < smartClinic.sql`

### 2. Git Workflow
**A. sebelum mulai ngoding (Tarik update terbaru dari repo):**
`git pull origin main`

**B. selesai ngoding (Upload):**
1. Masukkan semua perubahan: 
   `git add .`
2. Beri pesan/catatan apa yang baru saja dikerjakan:
   `git commit -m "Deskripsi singkat, contoh: membuat UI form dokter (by:kevin)"`
3. Upload ke GitHub:
   `git push origin main`

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