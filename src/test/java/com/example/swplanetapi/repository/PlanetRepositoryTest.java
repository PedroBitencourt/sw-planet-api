package com.example.swplanetapi.repository;

import com.example.swplanetapi.model.Planet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static com.example.swplanetapi.utils.PlanetUtils.EMPTY_PLANET;
import static com.example.swplanetapi.utils.PlanetUtils.INVALID_PLANET;
import static com.example.swplanetapi.utils.PlanetUtils.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataJpaTest
public class PlanetRepositoryTest {
    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void createPlanet_WithValidData_ReturnsPlanet() {
        Planet planet = planetRepository.save(PLANET);

        Planet response = testEntityManager.find(Planet.class, planet.getId());

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(PLANET.getName());
        assertThat(response.getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(response.getTerrain()).isEqualTo(PLANET.getTerrain());
    }

    @Test
    void createPlanet_WithInvalidData_ThrowsException() {
        assertThatThrownBy(() -> planetRepository.save(EMPTY_PLANET)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> planetRepository.save(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }
}
