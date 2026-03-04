package model;

import java.time.LocalDateTime;

public class ForumTopic {
    private int topicId;
    private String titlu;
    private String categorie;
    private int creatDe;
    private LocalDateTime dataCrearii;

    // For display
    private String numeCreator;
    private int numarPostari;

    public ForumTopic() {
        this.dataCrearii = LocalDateTime.now();
    }

    public ForumTopic(String titlu, String categorie, int creatDe) {
        this();
        this.titlu = titlu;
        this.categorie = categorie;
        this.creatDe = creatDe;
    }

    // Getters and Setters
    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getCreatDe() {
        return creatDe;
    }

    public void setCreatDe(int creatDe) {
        this.creatDe = creatDe;
    }

    public LocalDateTime getDataCrearii() {
        return dataCrearii;
    }

    public void setDataCrearii(LocalDateTime dataCrearii) {
        this.dataCrearii = dataCrearii;
    }

    public String getNumeCreator() {
        return numeCreator;
    }

    public void setNumeCreator(String numeCreator) {
        this.numeCreator = numeCreator;
    }

    public int getNumarPostari() {
        return numarPostari;
    }

    public void setNumarPostari(int numarPostari) {
        this.numarPostari = numarPostari;
    }

    @Override
    public String toString() {
        return "ForumTopic{" +
                "topicId=" + topicId +
                ", titlu='" + titlu + '\'' +
                ", categorie='" + categorie + '\'' +
                '}';
    }
}
