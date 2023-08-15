package com.example.swplanetapi.repository;

import com.example.swplanetapi.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanetRepository extends JpaRepository<Planet, Long> {
}
