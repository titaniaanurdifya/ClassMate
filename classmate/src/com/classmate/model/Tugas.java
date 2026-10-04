package com.classmate.model;

public class Tugas {
    private int idTugas;
    private int idMahasiswa;
    private int idJadwal;
    private String namaTugas;
    private String deskripsi;
    private String deadline;
    private String status;

    public Tugas() {}

    public Tugas(int idTugas, int idMahasiswa, int idJadwal, String namaTugas,
                 String deskripsi, String deadline, String status) {
        this.idTugas = idTugas;
        this.idMahasiswa = idMahasiswa;
        this.idJadwal = idJadwal;
        this.namaTugas = namaTugas;
        this.deskripsi = deskripsi;
        this.deadline = deadline;
        this.status = status;
    }

    public int getIdTugas() { return idTugas; }
    public void setIdTugas(int idTugas) { this.idTugas = idTugas; }

    public int getIdMahasiswa() { return idMahasiswa; }
    public void setIdMahasiswa(int idMahasiswa) { this.idMahasiswa = idMahasiswa; }

    public int getIdJadwal() { return idJadwal; }
    public void setIdJadwal(int idJadwal) { this.idJadwal = idJadwal; }

    public String getNamaTugas() { return namaTugas; }
    public void setNamaTugas(String namaTugas) { this.namaTugas = namaTugas; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
