# To-Do List Presentasi Tugas Akhir PBO

Berdasarkan dokumen `presentasi_TugasAkhir_PBO.pdf` dan hasil pengecekan struktur kode `TA-PBO` saat ini, berikut adalah daftar fitur dan komponen yang **masih belum selesai atau belum kita lakukan**:

## 1. Modul Login & Otorisasi Aktor

- [x] Membuat file `login.fxml` untuk UI form login (Hanya butuh input Username dan Password).
- [x] Membuat `LoginController.java` untuk menangani logika otentikasi.
- [x] Mengubah `AppMain.java` agar pada saat aplikasi dijalankan, pertama kali yang muncul adalah form Login, bukan langsung ke `dashboard.fxml`.
- [x] Mengimplementasikan logika pembatasan hak akses (Otorisasi) di `DashboardController.java` berdasarkan 3 aktor:
  - **Admin:** Memiliki kontrol penuh, bisa membuka fitur Kelola User/Petugas, Master Data, hingga Laporan.
  - **Petugas:** Hanya bisa membuka menu Master Pasien, Pendaftaran, dan Laporan.
  - **Dokter:** Hanya akan melihat menu yang relevan dengan medis, seperti Master Obat, Pemeriksaan (Input Diagnosa/Resep), Riwayat Rekam Medis, dan Prediksi ML.

## 2. CRUD Master Poli

- [ ] Menambahkan tabel `poli` di dalam file `smartClinic.sql`.
- [ ] Membuat model entitas `Poli.java`.
- [ ] Membuat akses database `PoliDAO.java`.
- [ ] Membuat `PoliService.java`.
- [ ] Membuat UI form `poli.fxml` dan (opsional) pop-up `form_poli.fxml`.
- [ ] Membuat controller terkait (`PoliController.java`).
- [ ] Menambahkan tombol navigasi untuk "Poli" di `dashboard.fxml`.

## 3. CRUD Master Jadwal

- [ ] Menambahkan tabel `jadwal` di dalam file `smartClinic.sql`.
- [ ] Membuat model entitas `Jadwal.java`.
- [ ] Membuat akses database `JadwalDAO.java`.
- [ ] Membuat `JadwalService.java`.
- [ ] Membuat UI form `jadwal.fxml` dan (opsional) pop-up `form_jadwal.fxml`.
- [ ] Membuat controller terkait (`JadwalController.java`).
- [ ] Menambahkan tombol navigasi untuk "Jadwal" di `dashboard.fxml`.

## 4. Modul Laporan

- [ ] Membuat desain form / view untuk melihat laporan (`laporan.fxml`).
- [ ] Membuat `LaporanController.java` untuk memproses data laporan (misal: laporan pendaftaran per hari atau kunjungan pasien).
- [ ] Menambahkan `onAction` pada tombol "📈 Laporan Klinik" di `dashboard.fxml` agar terhubung ke form Laporan.

## 5. Persiapan Teori Presentasi (Mahasiswa 1)

- [ ] Mempersiapkan slide / dokumen analisis sistem (Latar Belakang, Identifikasi Aktor, Use Case Diagram, ERD).
- [ ] Menyiapkan penjelasan Struktur Database MySQL dan Arsitektur MVC beserta Package program kita.

> **Catatan:** Fitur *Pendaftaran*, *Pemeriksaan (Diagnosa & Resep)*, *Riwayat Pemeriksaan (Rekam Medis)*, dan *Master (Dokter, Pasien, Obat)* **sudah ada** dan bisa didemonstrasikan.
