package com.example.swplanetapi.controller;

import com.example.swplanetapi.model.Planet;
import com.example.swplanetapi.service.PlanetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.swplanetapi.utils.PlanetUtils.EMPTY_PLANET;
import static com.example.swplanetapi.utils.PlanetUtils.INVALID_PLANET;
import static com.example.swplanetapi.utils.PlanetUtils.PLANET;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlanetService planetService;

    @Test
    void createPlanet_WithValidData_ReturnsCreated() throws Exception {
        when(planetService.create(PLANET)).thenReturn(PLANET);

        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(PLANET))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    void createPlanet_WithInvalidData_ReturnsBadRequest() throws Exception {
        mockMvc
                .perform(post("/planets").content(objectMapper.writeValueAsString(EMPTY_PLANET))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(INVALID_PLANET))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createPlanet_WithExistingName_ReturnsConflict() throws Exception {
        when(planetService.create(any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(PLANET))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void getPlanet_ByExistingId_ReturnsPlanet() throws Exception {
        when(planetService.get(1L)).thenReturn(Optional.ofNullable(PLANET));

        mockMvc.perform(get("/planets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    void getPlanet_ByUnexistingId_ReturnsNotFound() throws Exception {
        when(planetService.get(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/planets/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPlanet_ByExistingName_ReturnsPlanet() throws Exception {
        when(planetService.getByName("Name")).thenReturn(Optional.of(PLANET));

        mockMvc.perform(get("/planets/name/Name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    void getPlanet_ByUnexistigName_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/planets/name/invalidName"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listPlanets_ReturnsFilteredPlanets() throws Exception {
        List<Planet> planets = Collections.singletonList(PLANET);
        when(planetService.list("climate", "terrain"))
                .thenReturn(planets);

        mockMvc.perform(get("/planets")
                        .param("climate", "climate")
                        .param("terrain", "terrain"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").value(PLANET));
    }

    @Test
    void listPlanets_ReturnsNoPlanets() throws Exception {
        when(planetService.list(null, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/planets")
                        .param("climate", "climate")
                        .param("terrain", "terrain"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void removePlanet_WithExistingId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/planets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void removePlanet_WithUnexistingId_ReturnsNotFound() throws Exception {
        doThrow(new EmptyResultDataAccessException(1)).when(planetService).remove(1L);

        mockMvc.perform(delete("/planets/1"))
                .andExpect(status().isNotFound());
    }
}
