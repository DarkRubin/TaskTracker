package org.roadmap.tasktrackerbackend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@SpringBootTest
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRegistrationWithValidData() throws Exception {
        mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@email.org\",\"password\":\"82390ufjfiojDud\"}"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }

    @Test
    void testGetUserWithValidData() throws Exception {

    }


    @Test
    void testRegistrationWithInvalidData() throws Exception {

    }


}

