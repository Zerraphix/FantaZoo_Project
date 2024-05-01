package com.example.fantazoo_app.Models;

public class ZookeeperModel {
    int id;
    String name;

    WeaponModel weapon;

    CageModel cage;

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

    public WeaponModel getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponModel weapon) {
        this.weapon = weapon;
    }

    public CageModel getCage() {
        return cage;
    }

    public void setCage(CageModel cage) {
        this.cage = cage;
    }
}
