package com.example.baitapcuoiky.Model;

public class Contact {
    private int id;
    private String name;
    private String phone;
    private String note; // Ghi chú
    public Contact(int id, String name, String phone, String note) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.note = note;
    }

    public Contact(String name, String phone, String note) {
        this.name = name;
        this.phone = phone;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

