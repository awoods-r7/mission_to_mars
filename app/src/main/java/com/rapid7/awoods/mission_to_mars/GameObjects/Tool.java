package com.rapid7.awoods.mission_to_mars.GameObjects;


public class Tool {

    String name;
    String discription;

    public Tool(String name, String discription) {
        this.name = name;
        this.discription = discription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
}
