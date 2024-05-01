package com.example.Fantazoo_API.Repositories;

import com.example.Fantazoo_API.Models.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimRepository extends JpaRepository<Animal, Integer> {
}
