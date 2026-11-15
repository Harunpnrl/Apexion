package com.hbpnrl.apexion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbpnrl.apexion.dto.request.AuthenticationRequest;
import com.hbpnrl.apexion.dto.request.FitnessProfileRequest;
import com.hbpnrl.apexion.dto.request.RegisterRequest;
import com.hbpnrl.apexion.dto.request.UserUpdateRequest;
import com.hbpnrl.apexion.dto.response.AuthenticationResponse;
import com.hbpnrl.apexion.dto.response.ExerciseResponse;
import com.hbpnrl.apexion.dto.response.FitnessProfileResponse;
import com.hbpnrl.apexion.dto.response.MuscleGroupResponse;
import com.hbpnrl.apexion.dto.response.MuscleInfoResponse;
import com.hbpnrl.apexion.dto.response.UserResponse;
import com.hbpnrl.apexion.dto.response.WorkoutResponse;
import com.hbpnrl.apexion.entity.FitnessProfile;
import com.hbpnrl.apexion.entity.User;
import com.hbpnrl.apexion.exception.GlobalExceptionHandler;
import com.hbpnrl.apexion.service.AuthenticationService;
import com.hbpnrl.apexion.service.ExerciseService;
import com.hbpnrl.apexion.service.FitnessProfileService;
import com.hbpnrl.apexion.service.MuscleGroupService;
import com.hbpnrl.apexion.service.MuscleInfoService;
import com.hbpnrl.apexion.service.UserService;
import com.hbpnrl.apexion.service.WorkoutService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ControllerMockMvcTest {

    @Mock private AuthenticationService authenticationService;
    @Mock private ExerciseService exerciseService;
    @Mock private FitnessProfileService fitnessProfileService;
    @Mock private MuscleGroupService muscleGroupService;
    @Mock private MuscleInfoService muscleInfoService;
    @Mock private UserService userService;
    @Mock private WorkoutService workoutService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private User currentUser;
    private UsernamePasswordAuthenticationToken currentAuthentication;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(
                        new AuthenticationController(authenticationService),
                        new ExerciseController(exerciseService),
                        new FitnessProfileController(fitnessProfileService),
                        new MuscleGroupController(muscleGroupService),
                        new MuscleInfoController(muscleInfoService),
                        new UserController(userService),
                        new WorkoutController(workoutService))
                .setValidator(validator)
                .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(MethodParameter parameter) {
                        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
                    }

                    @Override
                    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                        return currentUser;
                    }
                }, new HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(MethodParameter parameter) {
                        return Authentication.class.isAssignableFrom(parameter.getParameterType());
                    }

                    @Override
                    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                        return currentAuthentication;
                    }
                })
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        currentUser = new User();
        currentUser.setId(7);
        currentUser.setEmail("john@example.com");
        currentUser.setFirstName("John");
        currentUser.setLastName("Doe");
        currentUser.setGender(User.Gender.MALE);
        currentAuthentication = new UsernamePasswordAuthenticationToken(
                currentUser, null, currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(currentAuthentication);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void authenticationEndpointsShouldReturnToken() throws Exception {
        when(authenticationService.register(any(RegisterRequest.class)))
                .thenReturn(AuthenticationResponse.builder().token("register-token").build());
        when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                .thenReturn(AuthenticationResponse.builder().token("login-token").build());

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(RegisterRequest.builder()
                                .firstName("John").lastName("Doe").email("john@example.com")
                                .password("password").build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("register-token"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new AuthenticationRequest("john@example.com", "password"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("login-token"));
    }

    @Test
    void invalidAuthenticationRequestShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"bad\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void exerciseEndpointsShouldReturnServiceResults() throws Exception {
        ExerciseResponse exercise = ExerciseResponse.builder().id(1).name("Bench Press").build();
        when(exerciseService.getAllExercises()).thenReturn(List.of(exercise));
        when(exerciseService.getExerciseById(1)).thenReturn(exercise);
        when(exerciseService.getRandomExercises(3)).thenReturn(List.of(exercise));

        mockMvc.perform(get("/api/v1/exercises"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Bench Press"));
        mockMvc.perform(get("/api/v1/exercises/1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
        mockMvc.perform(get("/api/v1/exercises/random").param("limit", "3"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1));

        verify(exerciseService).getRandomExercises(3);
    }

    @Test
    void muscleEndpointsShouldReturnServiceResults() throws Exception {
        when(muscleGroupService.getAllMuscleGroups()).thenReturn(List.of(
                MuscleGroupResponse.builder().id(1).name("Chest").build()));
        when(muscleGroupService.getPrimaryMuscleGroups()).thenReturn(List.of(
                MuscleGroupResponse.builder().id(1).name("Chest").build()));
        when(muscleInfoService.getMuscleInfoByName("Chest")).thenReturn(
                MuscleInfoResponse.builder().id(1).muscleName("Chest").overview("Overview").build());

        mockMvc.perform(get("/api/v1/muscle-groups"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Chest"));
        mockMvc.perform(get("/api/v1/muscle-groups/primary"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1));
        mockMvc.perform(get("/api/v1/muscle-info/Chest"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.muscleName").value("Chest"));
    }

    @Test
    void userEndpointsShouldHandleProfileAndFavorites() throws Exception {
        UserResponse userResponse = UserResponse.builder().id(7).email("john@example.com").firstName("John").build();
        ExerciseResponse exercise = ExerciseResponse.builder().id(1).name("Bench Press").build();
        when(userService.getUserProfile(currentUser)).thenReturn(userResponse);
        when(userService.updateUserProfile(eq(currentUser), any(UserUpdateRequest.class))).thenReturn(userResponse);
        when(userService.getFavoriteExercises(7)).thenReturn(List.of(exercise));

        mockMvc.perform(get("/api/v1/users/me").with(authenticated()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.email").value("john@example.com"));
        mockMvc.perform(put("/api/v1/users/me").with(authenticated())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserUpdateRequest.builder()
                                .firstName("John").lastName("Doe").build())))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/users/me/favorites").with(authenticated()))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1));
        mockMvc.perform(post("/api/v1/users/me/favorites/1").with(authenticated()))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/v1/users/me/favorites/1").with(authenticated()))
                .andExpect(status().isOk());

        verify(userService).addExerciseToFavorites(7, 1);
        verify(userService).removeExerciseFromFavorites(7, 1);
    }

    @Test
    void fitnessProfileEndpointsShouldHandleCreateReadAndUpdate() throws Exception {
        FitnessProfileResponse response = FitnessProfileResponse.builder()
                .id(2).userId(7).heightCm(BigDecimal.valueOf(180)).weightKg(BigDecimal.valueOf(80))
                .fitnessGoal("GAIN_MUSCLE").activityLevel("MODERATELY_ACTIVE").build();
        when(fitnessProfileService.getProfileByUserId(7)).thenReturn(response);
        when(fitnessProfileService.createOrUpdateProfile(eq(7), any(FitnessProfileRequest.class)))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/profiles/me").with(authenticated()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.userId").value(7));

        String body = objectMapper.writeValueAsString(profileRequest());
        mockMvc.perform(post("/api/v1/profiles/me").with(authenticated())
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/v1/profiles/me").with(authenticated())
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
    }

    @Test
    void workoutEndpointsShouldHandleListCreateAndDelete() throws Exception {
        when(workoutService.getUserWorkouts(7)).thenReturn(List.of(
                WorkoutResponse.builder().id(10).name("Push day").build()));

        mockMvc.perform(get("/api/v1/workouts/my-workouts").with(authenticated()))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(10));

        String body = "{\"name\":\"Push day\",\"exercises\":[{\"exerciseId\":1,\"sets\":3,\"reps\":10,\"weight\":40}]}";
        mockMvc.perform(post("/api/v1/workouts").with(authenticated())
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/v1/workouts/10").with(authenticated()))
                .andExpect(status().isNoContent());

        verify(workoutService).createWorkout(any(), eq(7));
        verify(workoutService).deleteWorkout(10, 7);
    }

    @Test
    void invalidFitnessProfileShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/profiles/me").with(authenticated())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"heightCm\":-1,\"weightKg\":0}"))
                .andExpect(status().isBadRequest());
    }

    private FitnessProfileRequest profileRequest() {
        FitnessProfileRequest request = new FitnessProfileRequest();
        request.setHeightCm(180.0);
        request.setWeightKg(80.0);
        request.setFitnessGoal(FitnessProfile.FitnessGoal.GAIN_MUSCLE);
        request.setActivityLevel(FitnessProfile.ActivityLevel.MODERATELY_ACTIVE);
        return request;
    }

    private RequestPostProcessor authenticated() {
        return request -> {
            SecurityContextHolder.getContext().setAuthentication(currentAuthentication);
            request.setUserPrincipal(currentAuthentication);
            return request;
        };
    }
}
