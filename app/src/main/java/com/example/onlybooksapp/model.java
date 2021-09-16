package com.example.onlybooksapp;

public class model {
    String name,price,purl;
    model()
    {

    }
    public model(String name, String book_name, String price, String purl) {
        this.name = name;
        this.price = price;
        this.purl = purl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }
}
