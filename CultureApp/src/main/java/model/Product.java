package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {
    private int productId;
    private int artistId;
    private String titlu;
    private String descriere;
    private BigDecimal pret;
    private String categorie;
    private String imagine;
    private int stoc;
    private LocalDateTime dataPostarii;

    // For display purposes
    private String numeArtist;

    public Product() {
        this.dataPostarii = LocalDateTime.now();
        this.stoc = 0;
    }

    public Product(int artistId, String titlu, String descriere, BigDecimal pret, String categorie) {
        this();
        this.artistId = artistId;
        this.titlu = titlu;
        this.descriere = descriere;
        this.pret = pret;
        this.categorie = categorie;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
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

    public BigDecimal getPret() {
        return pret;
    }

    public void setPret(BigDecimal pret) {
        this.pret = pret;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getImagine() {
        return imagine;
    }

    public void setImagine(String imagine) {
        this.imagine = imagine;
    }

    public int getStoc() {
        return stoc;
    }

    public void setStoc(int stoc) {
        this.stoc = stoc;
    }

    public LocalDateTime getDataPostarii() {
        return dataPostarii;
    }

    public void setDataPostarii(LocalDateTime dataPostarii) {
        this.dataPostarii = dataPostarii;
    }

    public String getNumeArtist() {
        return numeArtist;
    }

    public void setNumeArtist(String numeArtist) {
        this.numeArtist = numeArtist;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", titlu='" + titlu + '\'' +
                ", pret=" + pret +
                ", categorie='" + categorie + '\'' +
                '}';
    }
}
