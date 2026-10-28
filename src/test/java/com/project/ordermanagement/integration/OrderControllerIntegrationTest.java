package com.project.ordermanagement.integration;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateValidOrder() throws Exception {
        String orderJson = "{\"customerName\": \"Ion Popescu\", \"amount\": 150.50, \"status\": \"PENDING\"}";

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.customerName", is("Ion Popescu")))
                .andExpect(jsonPath("$.amount", is(150.50)))
                .andExpect(jsonPath("$.status", is("PENDING")));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingInvalidOrder() throws Exception {
        String invalidOrderJson = "{\"customerName\": \"\", \"amount\": -50.0, \"status\": \"PENDING\"}";

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidOrderJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldUpdateOrderStatusSuccessfully() throws Exception {
        String updateJson = "{\"status\": \"COMPLETED\"}";

        mockMvc.perform(patch("/orders/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("COMPLETED")));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingOrderInFinalStatus() throws Exception {
        String completeJson = "{\"status\": \"COMPLETED\"}";
        mockMvc.perform(patch("/orders/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(completeJson))
                .andExpect(status().isOk());

        String updateAgainJson = "{\"status\": \"PENDING\"}";

        mockMvc.perform(patch("/orders/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateAgainJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturnNotFoundWhenGettingNonExistentOrder() throws Exception {
        mockMvc.perform(get("/orders/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldNotChangeUpdatedAtWhenUpdatingWithSameStatus() throws Exception {
        MvcResult firstResult = mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andReturn();

        String initialResponseBody = firstResult.getResponse().getContentAsString();
        String initialUpdatedAt = JsonPath.read(initialResponseBody, "$.updatedAt");

        String updateJson = "{\"status\": \"PENDING\"}";

        mockMvc.perform(patch("/orders/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.updatedAt", is(initialUpdatedAt)));
    }
}