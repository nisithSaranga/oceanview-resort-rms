package com.oceanview.resort.entity;

public class Guest {

    private int guestId;
    private String fullName;
    private String address;
    private String contactNumber;

    public Guest() {
        // no-arg constructor
    }

    public int getGuestId() {
        return guestId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    public void setAddress(String addr) {
        this.address = addr;
    }

    public void setContactNumber(String no) {
        this.contactNumber = no;
    }
}

