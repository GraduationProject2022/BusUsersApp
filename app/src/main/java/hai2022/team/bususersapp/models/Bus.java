package hai2022.team.bususersapp.models;

import java.io.Serializable;

public class Bus implements Serializable {
    private String ID;
    private String name;
    private String DriverName;
    private String email;
    private String imgpath;
    private int img;
    private int Passengers;
    private double rate;
    private double NumOfRev;
    private String Location;
    private String Phone;
    private String Password;

    public Bus() {
    }

    public Bus(String ID, int img, String location) {
        this.ID = ID;
        this.img = img;
        Location = location;
    }

    public Bus(String ID, String name, String driverName, String email, String imgpath, int img, int passengers, double rate, double numOfRev, String location) {
        this.ID = ID;
        this.name = name;
        DriverName = driverName;
        this.email = email;
        this.imgpath = imgpath;
        this.img = img;
        Passengers = passengers;
        this.rate = rate;
        NumOfRev = numOfRev;
        Location = location;
    }

    public Bus(String name, String driverName, String email, String imgpath, int passengers, String location, String password) {
        this.name = name;
        DriverName = driverName;
        this.email = email;
        this.imgpath = imgpath;
        Passengers = passengers;
        Location = location;
        Password = password;
    }

    public Bus(String ID, String name, String driverName, String email, String imgpath, int passengers, String location, String password) {
        this.ID = ID;
        this.name = name;
        DriverName = driverName;
        this.email = email;
        this.imgpath = imgpath;
        Passengers = passengers;
        Location = location;
        Password = password;
    }


    public Bus(String ID, String name, String driverName, String email, String imgpath, int passengers, String location, String phone, String password) {
        this.ID = ID;
        this.name = name;
        DriverName = driverName;
        this.email = email;
        this.imgpath = imgpath;
        Passengers = passengers;
        Location = location;
        Phone = phone;
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getPassengers() {
        return Passengers;
    }

    public void setPassengers(int passengers) {
        Passengers = passengers;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getNumOfRev() {
        return NumOfRev;
    }

    public void setNumOfRev(double numOfRev) {
        NumOfRev = numOfRev;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
