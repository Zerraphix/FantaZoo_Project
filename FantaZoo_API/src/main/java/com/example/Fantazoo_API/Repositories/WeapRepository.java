package com.example.Fantazoo_API.Repositories;

import com.example.Fantazoo_API.Models.Weapon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeapRepository extends JpaRepository<Weapon, Integer> {
}
