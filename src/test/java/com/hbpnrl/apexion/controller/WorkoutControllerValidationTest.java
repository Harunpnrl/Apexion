package com.hbpnrl.apexion.controller;

import com.hbpnrl.apexion.dto.request.WorkoutRequest;
import com.hbpnrl.apexion.entity.User;
import com.hbpnrl.apexion.service.WorkoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class WorkoutControllerValidationTest {

    @Mock
    private WorkoutService workoutService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        WorkoutController controller = new WorkoutController(workoutService);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setValidator(validator)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void invalidWorkoutRequestShouldReturnBadRequestBeforeServiceCall() throws Exception {
        String payload = "{\"name\":\"\",\"exercises\":[]}";

        mockMvc.perform(post("/api/v1/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());

        verify(workoutService, org.mockito.Mockito.never())
                .createWorkout(any(WorkoutRequest.class), any(Integer.class));
    }

    @Test
    void validWorkoutRequestShouldReachService() throws Exception {
        User user = new User();
        user.setId(7);
        user.setEmail("john@example.com");
        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        String payload = "{\"name\":\"Push day\",\"exercises\":[{\"exerciseId\":1,\"sets\":3,\"reps\":10,\"weight\":40}]}";

        mockMvc.perform(post("/api/v1/workouts")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        verify(workoutService).createWorkout(any(WorkoutRequest.class), eq(7));
    }
}
