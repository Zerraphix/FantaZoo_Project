package com.example.Fantazoo_API.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weapon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String damage;

    @OneToMany(mappedBy = "weapon", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("weapon")
    List<Zookeeper> zookeepers;
}
