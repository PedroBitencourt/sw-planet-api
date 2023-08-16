package com.example.swplanetapi.service;

import com.example.swplanetapi.model.Planet;
import com.example.swplanetapi.repository.PlanetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.swplanetapi.utils.PlanetUtils.INVALID_PLANET;
import static com.example.swplanetapi.utils.PlanetUtils.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
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
    void getPlanet_WithInvalidId_ThrowsException() {
        Optional<Planet> response = planetService.get(1L);

        assertThat(response).isEmpty();
    }
}
