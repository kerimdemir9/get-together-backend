package com.get.together.backend.controller;

import com.get.together.backend.TestBase;
import com.get.together.backend.controller.model.PagedData;
import com.get.together.backend.controller.model.User;
import com.get.together.backend.data.model.UserModel;
import com.get.together.backend.util.SortDirection;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class UserControllerIntegrationTests extends TestBase {
    private static UserModel newUser;
    private static UserModel newUser2;

    public void insertNewUser() {
        newUser = userService.save(UserModel.builder()
                .userName("user1")
                .mail("user@gmail.com")
                .firstName("F1")
                .lastName("L1")
                .password("P1")
                .biography("")
                .phoneNumber("12345")
                .build());
    }

    public void insertNewUser2() {
        newUser2 = userService.save(UserModel.builder()
                .userName("user2")
                .mail("user2@gmail.com")
                .firstName("F2")
                .lastName("L2")
                .password("P2")
                .biography("")
                .phoneNumber("123456")
                .build());
    }

    public void testPagedDataResponse(PagedData<User> model) {
        assertNotNull(model);
        assertFalse(model.getContent().isEmpty());
        assertEquals(Optional.of(2L), Optional.of(model.getTotalElements()));
        assertEquals(Optional.of(1), Optional.of(model.getTotalPages()));
        assertEquals(Optional.of(2), Optional.of(model.getNumberOfElements()));

        assertTrue(model.getContent()
                .stream()
                .anyMatch(f -> String.valueOf(f.getId()).equals(newUser.getId().toString())));

        assertTrue(model.getContent()
                .stream()
                .anyMatch(f -> String.valueOf(f.getId()).equals(newUser2.getId().toString())));
    }

    @Before
    public void setup() {
        userService.hardDeleteAll();
    }

    @Test
    public void find_by_id_test() {
        insertNewUser();

        val url = TestBase.LOCALHOST
                .concat(String.valueOf(port))
                .concat("/v1/user/")
                .concat(newUser.getId().toString());
        val response = restTemplate.getForEntity(url, User.class);

        assertTrue(StringUtils.isNotBlank(response.toString()));
        assertNotNull(response.getBody());
        assertEquals(newUser.getId(), response.getBody().getId());
    }

    @Test
    public void find_by_id_with_exception_test() {
            val url = TestBase.LOCALHOST
                    .concat(String.valueOf(port))
                    .concat("/v1/user/1");
        try {
            restTemplate.getForEntity(url, User.class);
        } catch (final HttpClientErrorException e) {
            assertThat(e.getMessage(), containsString("404"));
        }
    }

    @Test
    public void find_all_like_user_name_test() {
        insertNewUser();
        insertNewUser2();

        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                        .concat("/v1/user/find_all_like_user_name/user");

        testPagedDataResponse(restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<PagedData<User>>() {
                }).getBody());
    }

    @Test
    public void find_all_like_user_name_with_exception_test() {
        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                .concat("/v1/user/find_all_like_user_name/user");
        try {
            restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<PagedData<User>>() {
                });
        } catch (final HttpClientErrorException e) {
            assertThat(e.getMessage(), containsString("404"));
        }
    }

    @Test
    public void insert_new_user_test() {
        val userToPost = User.builder()
                .userName("kerimdemir")
                .firstName("kerim")
                .lastName("demir")
                .biography("")
                .phoneNumber("12345")
                .mail("kerim@gmail.com")
                .password("pass")
                .build();

        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                .concat("/v1/user/save");

        val response = restTemplate.postForEntity
                (url, new HttpEntity<>(userToPost), User.class);

        assertTrue(StringUtils.isNotBlank(response.toString()));
        assertNotNull(response.getBody());

        val found = userService.findById(response.getBody().getId());
        assertNotNull(found);
        assertEquals(String.valueOf(response.getBody().getId()), found.getId().toString());
    }

    @Test
    public void insert_new_user_with_exception_test() {
        val userToPost = User.builder()
                .userName("kerimdemir")
                .firstName("")
                .lastName("demir")
                .biography("")
                .phoneNumber("12345")
                .mail("kerim@gmail.com")
                .build();
        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                .concat("/v1/user/save");
        try {
            restTemplate.postForEntity(url,
                    new HttpEntity<>(userToPost), User.class);
        } catch (final HttpClientErrorException ex) {
            assertThat(ex.getMessage(), containsString("400"));
            assertThat(ex.getMessage(), containsString("must not be empty"));
        }
    }

    @Test
    public void delete_user_test() {
        insertNewUser();

        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                .concat("/v1/user/delete/").concat(newUser.getId().toString());

        val response = restTemplate.exchange(url, HttpMethod.DELETE, null, User.class);

        assertTrue(StringUtils.isNotBlank(response.toString()));
        assertNotNull(response.getBody());
        assertEquals(newUser.getId(), response.getBody().getId());
    }

    @Test
    public void delete_user_with_exception_test() {
        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                .concat("/v1/user/delete/1");
        try {
            restTemplate.exchange(url, HttpMethod.DELETE, null, User.class);
        } catch(final HttpClientErrorException e) {
            assertThat(e.getMessage(), containsString("404"));
        }
    }

    @Test
    public void update_user_test() {
        insertNewUser();

        val userToUpdate = User.builder()
                .userName("kerimdemir")
                .firstName("kerim")
                .lastName("demir")
                .biography("")
                .phoneNumber("12345")
                .password("pass")
                .mail("kerim@gmail.com")
                .build();
        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                .concat("/v1/user/update");
        val response = restTemplate.postForEntity
                (url, new HttpEntity<>(userToUpdate), User.class);

        assertTrue(StringUtils.isNotBlank(response.toString()));
        assertNotNull(response.getBody());
        assertEquals("kerim", userToUpdate.getFirstName());
    }

    @Test
    public void update_user_with_exception_test() {
        insertNewUser();

        val userToUpdate = User.builder()
                .userName("kerimdemir")
                .firstName("")
                .lastName("demir")
                .biography("")
                .phoneNumber("12345")
                .created(new Date(Instant.now().toEpochMilli()))
                .mail("kerim@gmail.com")
                .build();
        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                .concat("/v1/user/save");
        try {
            restTemplate.exchange(url, HttpMethod.POST,
                    new HttpEntity<>(userToUpdate), User.class);
        } catch (final HttpClientErrorException e) {
            assertThat(e.getMessage(), containsString("400"));
        }
    }

    @Test
    public void find_all_like_first_name_test() {
        insertNewUser();
        insertNewUser2();

        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                .concat("/v1/user/find_all_like_first_name/")
                .concat("F");
        testPagedDataResponse(restTemplate.exchange(url, HttpMethod.GET,
                null, new ParameterizedTypeReference<PagedData<User>>() {
        }).getBody());
    }

    @Test
    public void find_all_like_first_name_with_exception_test() {
        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                .concat("/v1/user/find_all_like_first_name/")
                .concat("F");
        try {
        restTemplate.exchange(url, HttpMethod.GET,
                null, new ParameterizedTypeReference<PagedData<User>>() {
                });
        } catch (final HttpClientErrorException e) {
            assertThat(e.getMessage(), containsString("404"));
        }
    }

    @Test
    public void find_all_like_last_name_test() {
        insertNewUser();
        insertNewUser2();

        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                .concat("/v1/user/find_all_like_last_name/")
                .concat("L");
        testPagedDataResponse(restTemplate.exchange(url, HttpMethod.GET,
                null, new ParameterizedTypeReference<PagedData<User>>() {
                }).getBody());
    }

    @Test
    public void find_all_like_last_name_with_exception_test() {
        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                .concat("/v1/user/find_all_like_last_name/")
                .concat("L");
        try {
            restTemplate.exchange(url, HttpMethod.GET,
                null, new ParameterizedTypeReference<PagedData<User>>() {
                });
        } catch (final HttpClientErrorException e) {
            assertThat(e.getMessage(), containsString("404"));
        }
    }

    @Test
    public void find_all_like_first_and_last_name_test() {
        insertNewUser();
        insertNewUser2();

        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                .concat("/v1/user/find_all_like_first_name_and_last_name/F/L");
        
        testPagedDataResponse(restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<PagedData<User>>() {
                }).getBody());
    }

    @Test
    public void find_all_like_first_and_last_name_with_exception_test() {
        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                .concat("/v1/user/find_all_like_first_and_last_name/F/L");
        try {
            restTemplate.exchange(url, HttpMethod.GET,
                    null, new ParameterizedTypeReference<PagedData<User>>() {
                    });
        } catch (final HttpClientErrorException e) {
            assertThat(e.getMessage(), containsString("404"));
        }
    }

    @Test
    public void find_all_like_mail_test() {
        insertNewUser();
        insertNewUser2();

        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                .concat("/v1/user/find_all_like_mail/@gmail.com");

        testPagedDataResponse(restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<PagedData<User>>() {
                }).getBody());
    }

    @Test
    public void find_all_like_mail_with_exception_test() {
        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                .concat("/v1/user/find_all_like_mail/@gmail.com");
        try {
            restTemplate.exchange(url, HttpMethod.GET,
                    null, new ParameterizedTypeReference<PagedData<User>>() {
                    });
        } catch (final HttpClientErrorException e) {
            assertThat(e.getMessage(), containsString("404"));
        }
    }

    @Test
    public void find_all_like_phone_number_test() {
        insertNewUser();
        insertNewUser2();

        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                .concat("/v1/user/find_all_like_phone_number/123");

        testPagedDataResponse(restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<PagedData<User>>() {
                }).getBody());
    }

    @Test
    public void find_all_like_phone_number_with_exception_test() {
        val url = TestBase.LOCALHOST.concat(String.valueOf(port))
                .concat("/v1/user/find_all_like_phone_number/123");
        try {
            restTemplate.exchange(url, HttpMethod.GET,
                    null, new ParameterizedTypeReference<PagedData<User>>() {
                    });
        } catch (final HttpClientErrorException e) {
            assertThat(e.getMessage(), containsString("404"));
        }
    }
}
