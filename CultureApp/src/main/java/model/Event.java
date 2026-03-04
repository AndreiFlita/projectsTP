package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Event {
    private int eventId;
    private int organizerId;
    private String titlu;
    private String descriere;
    private String locatie;
    private LocalDateTime dataStart;
    private LocalDateTime dataEnd;
    private int numarLocuri;
    private BigDecimal pretBilet;

    // For display
    private String numeOrganizator;
    private int locuriRamase;

    public Event() {
        this.numarLocuri = 0;
        this.pretBilet = BigDecimal.ZERO;
    }

    public Event(int organizerId, String titlu, String descriere, String locatie,
                 LocalDateTime dataStart, LocalDateTime dataEnd) {
        this();
        this.organizerId = organizerId;
        this.titlu = titlu;
        this.descriere = descriere;
        this.locatie = locatie;
        this.dataStart = dataStart;
        this.dataEnd = dataEnd;
    }

    // Getters and Setters
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
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

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public LocalDateTime getDataStart() {
        return dataStart;
    }

    public void setDataStart(LocalDateTime dataStart) {
        this.dataStart = dataStart;
    }

    public LocalDateTime getDataEnd() {
        return dataEnd;
    }

    public void setDataEnd(LocalDateTime dataEnd) {
        this.dataEnd = dataEnd;
    }

    public int getNumarLocuri() {
        return numarLocuri;
    }

    public void setNumarLocuri(int numarLocuri) {
        this.numarLocuri = numarLocuri;
    }

    public BigDecimal getPretBilet() {
        return pretBilet;
    }

    public void setPretBilet(BigDecimal pretBilet) {
        this.pretBilet = pretBilet;
    }

    public String getNumeOrganizator() {
        return numeOrganizator;
    }

    public void setNumeOrganizator(String numeOrganizator) {
        this.numeOrganizator = numeOrganizator;
    }

    public int getLocuriRamase() {
        return locuriRamase;
    }

    public void setLocuriRamase(int locuriRamase) {
        this.locuriRamase = locuriRamase;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", titlu='" + titlu + '\'' +
                ", locatie='" + locatie + '\'' +
                ", dataStart=" + dataStart +
                '}';
    }
}
