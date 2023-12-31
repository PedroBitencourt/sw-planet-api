package com.example.swplanetapi.service;

import com.example.swplanetapi.model.Planet;
import com.example.swplanetapi.repository.PlanetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PlanetService {

    private final PlanetRepository planetRepository;

    public Planet create(Planet planet){
        return planetRepository.save(planet);
    }


    public Optional<Planet> get(Long id) {
        return planetRepository.findById(id);
    }

    public Optional<Planet> getByName(String name) {
        return planetRepository.findByName(name);
    }

    public List<Planet> list(String climate, String terrain) {
        return planetRepository.findAll(climate, terrain);
    }

    public void remove(Long id) {
        planetRepository.deleteById(id);
    }
}
