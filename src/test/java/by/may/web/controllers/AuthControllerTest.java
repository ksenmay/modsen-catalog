package by.may.web.controllers;

import by.may.application.dto.LoginCreateRequest;
import by.may.application.dto.RefreshCreateRequest;
import by.may.domain.Role;
import by.may.infrastructure.entity.UserJpaEntity;
import by.may.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin12345";

    private final MockMvc mockMvc;
    private final JsonMapper mapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    AuthControllerTest(MockMvc mockMvc,
                       JsonMapper mapper,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JdbcTemplate jdbcTemplate) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM reviews");
        jdbcTemplate.execute("DELETE FROM price_history");
        jdbcTemplate.execute("DELETE FROM products");
        jdbcTemplate.execute("DELETE FROM categories");
        jdbcTemplate.execute("DELETE FROM users");

        userRepository.save(new UserJpaEntity(
                USERNAME,
                "admin@test.com",
                passwordEncoder.encode(PASSWORD),
                Role.ADMIN));
    }

    @Test
    void login_validCredentials_returnsTokenPair() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new LoginCreateRequest(USERNAME, PASSWORD))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    void login_wrongPassword_returns403() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new LoginCreateRequest(USERNAME, "wrong"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void login_unknownUser_returns404() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new LoginCreateRequest("nobody", "x"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void refresh_validToken_returnsNewPair() throws Exception {
        String oldRefresh = loginNode().get("refreshToken").asString();

        MvcResult result = mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new RefreshCreateRequest(oldRefresh))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andReturn();

        String newRefresh = readNode(result).get("refreshToken").asString();
        assertNotEquals(oldRefresh, newRefresh);
    }

    @Test
    void refresh_oldTokenAfterRotation_returns403() throws Exception {
        String oldRefresh = loginNode().get("refreshToken").asString();

        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(new RefreshCreateRequest(oldRefresh))));

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new RefreshCreateRequest(oldRefresh))))
                .andExpect(status().isForbidden());
    }

    @Test
    void logout_withAccessToken_returns204() throws Exception {
        String access = loginNode().get("accessToken").asString();

        mockMvc.perform(post("/api/auth/logout")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + access))
                .andExpect(status().isNoContent());
    }

    @Test
    void logout_withoutToken_returns403() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isForbidden());
    }

    private JsonNode loginNode() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new LoginCreateRequest(USERNAME, PASSWORD))))
                .andReturn();
        return readNode(result);
    }

    private JsonNode readNode(MvcResult result) {
        return mapper.readTree(result.getResponse().getContentAsByteArray());
    }

    private String json(Object o) {
        return mapper.writeValueAsString(o);
    }
}