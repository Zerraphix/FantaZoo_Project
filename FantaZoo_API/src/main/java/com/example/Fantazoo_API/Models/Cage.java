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
public class Cage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;

    @OneToMany(mappedBy = "cage", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"cage", "weapon"})
    List<Zookeeper> zookeepers;

    @OneToMany(mappedBy = "cage", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("cage")
    List<Animal> animals;
}

