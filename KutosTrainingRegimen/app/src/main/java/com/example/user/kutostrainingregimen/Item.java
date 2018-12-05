package com.example.user.kutostrainingregimen;

public class Item {
    private String name;
    private String skill;
    private String imageUrl;

    public Item(String name, String skill, String imageUrl) {
        this.name = name;
        this.skill = skill;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
