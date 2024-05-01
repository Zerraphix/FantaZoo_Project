package com.example.fantazoo_app.Models;

import java.util.List;

public class CageModel {
    int id;
    String name;

    List<ZookeeperModel> zookeepers;

    List<AnimalModel> animals;

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

    public List<ZookeeperModel> getZookeepers() {
        return zookeepers;
    }

    public void setZookeepers(List<ZookeeperModel> zookeepers) {
        this.zookeepers = zookeepers;
    }

    public List<AnimalModel> getAnimals() {
        return animals;
    }

    public void setAnimals(List<AnimalModel> animals) {
        this.animals = animals;
    }
}
