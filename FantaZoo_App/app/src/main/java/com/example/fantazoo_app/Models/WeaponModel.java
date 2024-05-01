package com.example.fantazoo_app.Models;

import java.util.List;

public class WeaponModel {
    int id;
    String name;
    String damage;

    List<ZookeeperModel> zookeepers;

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

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public List<ZookeeperModel> getZookeepers() {
        return zookeepers;
    }

    public void setZookeepers(List<ZookeeperModel> zookeepers) {
        this.zookeepers = zookeepers;
    }
}
