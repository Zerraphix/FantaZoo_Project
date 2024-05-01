package com.example.Fantazoo_API.Repositories;

import com.example.Fantazoo_API.Models.Zookeeper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZKRepository extends JpaRepository<Zookeeper, Integer> {
}
