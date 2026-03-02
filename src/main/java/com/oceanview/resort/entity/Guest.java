package com.oceanview.resort.entity;

public class Guest {
    private int guestId;
    private String fullName;
    private String address;
    private String contactNumber;

    public Guest() { }

    public Guest(int guestId, String fullName, String address, String contactNumber) {
        this.guestId = guestId;
        this.fullName = fullName;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
