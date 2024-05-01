package com.example.Fantazoo_API.Models;

import com.example.Fantazoo_API.Extra.Gender;
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
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    int age;
    Gender gender;

    @ManyToOne
    @JoinColumn(name = "cage_id")
    @JsonIgnoreProperties({"animals", "zookeepers"})
    Cage cage;
}
