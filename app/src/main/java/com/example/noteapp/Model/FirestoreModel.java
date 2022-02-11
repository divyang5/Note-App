package com.example.noteapp.Model;

public class FirestoreModel {
    private String tittle;
    private String desc;

    public FirestoreModel() {

    }

    public FirestoreModel(String tittle, String desc) {
        this.tittle = tittle;
        this.desc = desc;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
