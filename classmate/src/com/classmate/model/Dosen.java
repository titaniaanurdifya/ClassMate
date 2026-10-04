package com.classmate.model;

public class Dosen {
    private int idDosen;
    private String namaDosen;
    private String email;
    private String noHp;
    private String mataKuliah;

    public Dosen() {}

    public Dosen(int idDosen, String namaDosen, String email, String noHp, String mataKuliah) {
        this.idDosen = idDosen;
        this.namaDosen = namaDosen;
        this.email = email;
        this.noHp = noHp;
        this.mataKuliah = mataKuliah;
    }

    public int getIdDosen() { return idDosen; }
    public void setIdDosen(int idDosen) { this.idDosen = idDosen; }

    public String getNamaDosen() { return namaDosen; }
    public void setNamaDosen(String namaDosen) { this.namaDosen = namaDosen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNoHp() { return noHp; }
    public void setNoHp(String noHp) { this.noHp = noHp; }

    public String getMataKuliah() { return mataKuliah; }
    public void setMataKuliah(String mataKuliah) { this.mataKuliah = mataKuliah; }
}
