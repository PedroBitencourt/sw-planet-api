package com.example.swplanetapi.repository;

import com.example.swplanetapi.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlanetRepository extends JpaRepository<Planet, Long> {
    Optional<Planet> findByName(String name);

    @Query("SELECT p FROM Planet p WHERE (:climate is null or p.climate = :climate) " +
            "AND (:terrain is null or p.terrain = :terrain)")
    List<Planet> findAll(String climate, String terrain);
}
