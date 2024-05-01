package com.example.Fantazoo_API.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Zookeeper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;

    @ManyToOne
    @JoinColumn(name = "weapon_id")
    @JsonIgnoreProperties("zookeepers")
    Weapon weapon;

    @ManyToOne
    @JoinColumn(name = "cage_id")
    @JsonIgnoreProperties("zookeepers")
    Cage cage;
}
