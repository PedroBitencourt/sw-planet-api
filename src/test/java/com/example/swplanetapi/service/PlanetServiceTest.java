package com.example.swplanetapi.service;

import com.example.swplanetapi.model.Planet;
import com.example.swplanetapi.repository.PlanetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.swplanetapi.utils.PlanetUtils.INVALID_PLANET;
import static com.example.swplanetapi.utils.PlanetUtils.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {
    @InjectMocks
    private PlanetService planetService;

    @Mock
    private PlanetRepository planetRepository;

    @Test
    void createPlanet_WithValidData_ReturnsPlanet() {
        when(planetRepository.save(PLANET)).thenReturn(PLANET);

        Planet response = planetService.create(PLANET);

        assertThat(response).isEqualTo(PLANET);
    }

    @Test
    void createPlanet_WithInvalidData_ThrowsException() {
        when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void getPlanet_WithValidId_ReturnsPlanet() {
        when(planetRepository.findById(anyLong())).thenReturn(Optional.ofNullable(PLANET));

        Optional<Planet> response = planetService.get(1L);

        assertThat(response).isNotEmpty();
        assertThat(response.get()).isEqualTo(PLANET);
    }

    @Test
    void getPlanet_WithInvalidId_ReturnsEmpty() {
        Optional<Planet> response = planetService.get(1L);

        assertThat(response).isEmpty();
    }

    @Test
    void getPlanet_WithValidName_ReturnsPlanet() {
        when(planetRepository.findByName(anyString())).thenReturn(Optional.ofNullable(PLANET));

        Optional<Planet> response = planetService.getByName(PLANET.getName());

        assertThat(response).isNotEmpty();
        assertThat(response.get()).isEqualTo(PLANET);
    }

    @Test
    void getPlanet_WithInvalidName_ReturnsEmpty() {
        final String name = "Unexisting name";
        Optional<Planet> response = planetService.getByName(name);

        assertThat(response).isEmpty();
    }

    @Test
    void listPlanets_ReturnsAllPlanets() {
        when(planetRepository.findAll(PLANET.getClimate(), PLANET.getTerrain())).thenReturn(Collections.singletonList(PLANET));

        List<Planet> response = planetService.list(PLANET.getClimate(), PLANET.getTerrain());

        assertThat(response).isNotEmpty();
        assertThat(response.size()).isEqualTo(1);
    }

    @Test
    void listPlanets_ReturnsNoPlanets() {
        List<Planet> response = planetService.list(PLANET.getClimate(), PLANET.getTerrain());

        assertThat(response).isEmpty();
    }

    @Test
    void removePlanet_WithExistingId_DoesNotThrowAnyException() {
        assertThatCode(() -> planetService.remove(1L)).doesNotThrowAnyException();
    }

    @Test
    void removePlanet_WithUnexistingId_ThrowsException() {
        final Long invalidId = 4948448L;

        doThrow(new RuntimeException()).when(planetRepository).deleteById(invalidId);

        assertThatThrownBy(() -> planetService.remove(invalidId)).isInstanceOf(RuntimeException.class);
    }
}
