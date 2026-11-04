package by.may.web.controllers;

import by.may.application.dto.ProductCreateRequest;
import by.may.domain.Role;
import by.may.infrastructure.entity.CategoryJpaEntity;
import by.may.infrastructure.entity.UserJpaEntity;
import by.may.infrastructure.repository.CategoryRepository;
import by.may.infrastructure.repository.UserRepository;
import by.may.infrastructure.security.CurrentUser;
import by.may.infrastructure.security.JwtService;
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
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    private String adminToken;
    private String userToken;
    private Long categoryId;

    @BeforeEach
    void setUp() {
        cleanDatabase();

        UserJpaEntity admin = userRepository.save(new UserJpaEntity(
                "admin", "admin@test.com",
                passwordEncoder.encode("admin12345"), Role.ADMIN));
        UserJpaEntity user = userRepository.save(new UserJpaEntity(
                "customer", "customer@test.com",
                passwordEncoder.encode("customer12345"), Role.CUSTOMER));

        adminToken = jwtService.createJwt(new CurrentUser(admin.getId(), "admin", Role.ADMIN));
        userToken = jwtService.createJwt(new CurrentUser(user.getId(), "customer", Role.CUSTOMER));

        categoryId = categoryRepository.save(
                new CategoryJpaEntity("Телефоны", "Смартфоны")).getId();
    }

    @Test
    void createProduct_asAdmin_returns201() throws Exception {
        mockMvc.perform(post("/api/products")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(validRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Телефон Apple iPhone 13"));
    }

    @Test
    void createProduct_withoutToken_returns401() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(validRequest())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createProduct_asUser_returns403() throws Exception {
        mockMvc.perform(post("/api/products")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(validRequest())))
                .andExpect(status().isForbidden());
    }

    @Test
    void createProduct_blankName_returns400() throws Exception {
        ProductCreateRequest dto = new ProductCreateRequest(
                " ", null, new BigDecimal("10"), categoryId);

        mockMvc.perform(post("/api/products")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProduct_negativePrice_returns400() throws Exception {
        ProductCreateRequest dto = new ProductCreateRequest(
                "Laptop", null, new BigDecimal("-1"), categoryId);

        mockMvc.perform(post("/api/products")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProduct_unknownId_returns404() throws Exception {
        mockMvc.perform(get("/api/products/999999"))
                .andExpect(status().isNotFound());
    }

    private ProductCreateRequest validRequest() {
        return new ProductCreateRequest(
                "Телефон Apple iPhone 13",
                "Цвет: розовый, ОЗУ: 4Гб, память: 128Гб",
                new BigDecimal("1499.00"),
                categoryId);
    }

    private String json(Object o) throws Exception {
        return mapper.writeValueAsString(o);
    }

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM reviews");
        jdbcTemplate.execute("DELETE FROM price_history");
        jdbcTemplate.execute("DELETE FROM products");
        jdbcTemplate.execute("DELETE FROM categories");
        jdbcTemplate.execute("DELETE FROM users");
    }
}