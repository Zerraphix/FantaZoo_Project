package com.example.Fantazoo_API.Repositories;

import com.example.Fantazoo_API.Models.Cage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CageRepository extends JpaRepository<Cage, Integer> {
}
