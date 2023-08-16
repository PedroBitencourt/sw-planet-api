package com.example.swplanetapi.utils;

import com.example.swplanetapi.model.Planet;

public class PlanetUtils {

    public static final Planet PLANET = Planet.builder().name("Name").climate("climate").terrain("terrain").build();
    public static final Planet INVALID_PLANET = Planet.builder().name("").climate("").terrain("").build();
}
