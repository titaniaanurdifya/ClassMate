package com.classmate.model;

public class Reminder {
    private int idReminder;
    private int idTugas;
    private String pesan;
    private String waktuReminder;
    private String statusReminder;

    public Reminder() {}

    public Reminder(int idReminder, int idTugas, String pesan, String waktuReminder, String statusReminder) {
        this.idReminder = idReminder;
        this.idTugas = idTugas;
        this.pesan = pesan;
        this.waktuReminder = waktuReminder;
        this.statusReminder = statusReminder;
    }

    public int getIdReminder() { return idReminder; }
    public void setIdReminder(int idReminder) { this.idReminder = idReminder; }

    public int getIdTugas() { return idTugas; }
    public void setIdTugas(int idTugas) { this.idTugas = idTugas; }

    public String getPesan() { return pesan; }
    public void setPesan(String pesan) { this.pesan = pesan; }

    public String getWaktuReminder() { return waktuReminder; }
    public void setWaktuReminder(String waktuReminder) { this.waktuReminder = waktuReminder; }

    public String getStatusReminder() { return statusReminder; }
    public void setStatusReminder(String statusReminder) { this.statusReminder = statusReminder; }
}
