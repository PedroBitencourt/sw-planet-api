package com.example.swplanetapi.service;

import com.example.swplanetapi.model.Planet;
import com.example.swplanetapi.repository.PlanetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PlanetService {

    private final PlanetRepository planetRepository;

    public Planet create(Planet planet){
        return planetRepository.save(planet);
    }


}
