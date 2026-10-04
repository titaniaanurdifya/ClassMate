package com.classmate.model;

public class Jadwal {
    private int idJadwal;
    private int idMahasiswa;
    private String mataKuliah;
    private String hari;
    private String jamMulai;
    private String jamSelesai;
    private String ruangan;
    private String dosenPengajar;

    public Jadwal() {}

    public Jadwal(int idJadwal, int idMahasiswa, String mataKuliah, String hari,
                  String jamMulai, String jamSelesai, String ruangan, String dosenPengajar) {
        this.idJadwal = idJadwal;
        this.idMahasiswa = idMahasiswa;
        this.mataKuliah = mataKuliah;
        this.hari = hari;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.ruangan = ruangan;
        this.dosenPengajar = dosenPengajar;
    }

    public int getIdJadwal() { return idJadwal; }
    public void setIdJadwal(int idJadwal) { this.idJadwal = idJadwal; }

    public int getIdMahasiswa() { return idMahasiswa; }
    public void setIdMahasiswa(int idMahasiswa) { this.idMahasiswa = idMahasiswa; }

    public String getMataKuliah() { return mataKuliah; }
    public void setMataKuliah(String mataKuliah) { this.mataKuliah = mataKuliah; }

    public String getHari() { return hari; }
    public void setHari(String hari) { this.hari = hari; }

    public String getJamMulai() { return jamMulai; }
    public void setJamMulai(String jamMulai) { this.jamMulai = jamMulai; }

    public String getJamSelesai() { return jamSelesai; }
    public void setJamSelesai(String jamSelesai) { this.jamSelesai = jamSelesai; }

    public String getRuangan() { return ruangan; }
    public void setRuangan(String ruangan) { this.ruangan = ruangan; }

    public String getDosenPengajar() { return dosenPengajar; }
    public void setDosenPengajar(String dosenPengajar) { this.dosenPengajar = dosenPengajar; }
}
