package com.get.together.backend;

import com.get.together.backend.data.service.UserService;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;


@SuppressWarnings("rawtypes")
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GetTogetherBackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
@Ignore
public abstract class TestBase {
    public static final String LOCALHOST = "http://localhost:";
    @Container
    private static final MySQLContainer container;
    private static final String IMAGE_VERSION = "mysql:8.0";

    @LocalServerPort
    public int port;

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    public UserService userService;


    static {
        container = new MySQLContainer<>(IMAGE_VERSION)
                .withUsername("test_user")
                .withPassword("test_password")
                //.withInitScript("ddl.sql")
                .withDatabaseName("get_together");
        container.start();
    }

    @DynamicPropertySource
    public static void overrideContainerProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", container::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", container::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", container::getPassword);
    }
}
