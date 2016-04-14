package com.newly_dawn.app.wsn.objects;

/**
 * Created by newly_dawn on 2016/4/12.
 */
public class City {
    String provice;
    String city;
    String county;
    int ID;
    public void setProvice(String provice){
        this.provice = provice;
    }
    public void setCity(String city){
        this.city = city;
    }
    public void setCounty(String county){
        this.county = county;
    }
    public void setID(int ID){
        this.ID = ID;
    }
    public String getProvice(){
        return this.provice;
    }
    public String getCity(){
        return this.city;
    }
    public String getCounty(){
        return this.county;
    }
    public int getID(){
        return this.ID;
    }
}
