package com.example.performancekeeper.repository;

import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @AfterEach
    public void clear() {
        userRepository.deleteAll();
    }
    @Test
    public void save() {
        // given
        UserEntity user = new UserEntity();
        user.setUsername("test user 1");
        user.setEncodedPW("test user 1");
        user.setRole("Student");
        UserEntity user2 = new UserEntity();
        user2.setUsername("test user 2");
        user2.setEncodedPW("test user 2");
        user2.setRole("Student");

        // when
        userRepository.save(user);
        userRepository.save(user2);

        // then
        Assertions.assertThat(user).isEqualTo(user);
        Assertions.assertThat(user2).isEqualTo(user2);
        Assertions.assertThat(user).isNotEqualTo(user2);
    }

}
