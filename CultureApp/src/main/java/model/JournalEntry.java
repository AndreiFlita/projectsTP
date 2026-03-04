package model;

import java.time.LocalDate;

public class JournalEntry {
    private int entryId;
    private int userId;
    private String tipActivitate; // carte, film, expoziție
    private String titlu;
    private String descriere;
    private LocalDate dataExperientei;
    private int rating; // 1-5

    public JournalEntry() {
        this.dataExperientei = LocalDate.now();
        this.rating = 5;
    }

    public JournalEntry(int userId, String tipActivitate, String titlu, String descriere, int rating) {
        this();
        this.userId = userId;
        this.tipActivitate = tipActivitate;
        this.titlu = titlu;
        this.descriere = descriere;
        this.rating = rating;
    }

    // Getters and Setters
    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTipActivitate() {
        return tipActivitate;
    }

    public void setTipActivitate(String tipActivitate) {
        this.tipActivitate = tipActivitate;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public LocalDate getDataExperientei() {
        return dataExperientei;
    }

    public void setDataExperientei(LocalDate dataExperientei) {
        this.dataExperientei = dataExperientei;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
        }
    }

    @Override
    public String toString() {
        return "JournalEntry{" +
                "entryId=" + entryId +
                ", tipActivitate='" + tipActivitate + '\'' +
                ", titlu='" + titlu + '\'' +
                ", rating=" + rating +
                '}';
    }
}
