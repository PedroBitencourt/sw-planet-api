package com.example.swplanetapi.repository;

import com.example.swplanetapi.model.Planet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

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

    @AfterEach
    public void afterEach() {
        PLANET.setId(null);
    }

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

    @Test
    void createPlanet_WithExistingName_ThrowsException() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);
        planet.setId(null);

        assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
    }
    @Test
    void getPlanet_ByExistingId_ReturnsPlanet() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        Optional<Planet> planetOptional = planetRepository.findById(planet.getId());

        assertThat(planetOptional).isNotEmpty();
        assertThat(planetOptional.get()).isEqualTo(planet);
    }

    @Test
    void getPlanet_ByUnexistingId_ReturnsNotEmpty() {
        Optional<Planet> planetOptional = planetRepository.findById(1L);

        assertThat(planetOptional).isEmpty();
    }

    @Test
    void getPlanet_ByExistingName_ReturnsPlanet() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        Optional<Planet> planetOptional = planetRepository.findByName(planet.getName());

        assertThat(planetOptional).isNotEmpty();
        assertThat(planetOptional.get()).isEqualTo(planet);
    }

    @Test
    void getPlanet_ByUnexistingName_ReturnsNotEmpty() {
        Optional<Planet> planetOptional = planetRepository.findByName("Unexisting Name");

        assertThat(planetOptional).isEmpty();
    }
}
