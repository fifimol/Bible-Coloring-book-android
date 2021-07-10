package com.biblestory.color.category;


public class DataModel {


    String name;
    String cat_folder;
    int id_;
    int image;

    public DataModel(String name, String version, int id_, int image) {
        this.name = name;
        this.cat_folder = version;
        this.id_ = id_;
        this.image=image;
    }


    public String getName() {
        return name;
    }


    public String getCat_folder() {
        return cat_folder;
    }

    public int getImage() {
        return image;
    }

    public int getId() {
        return id_;
    }
}