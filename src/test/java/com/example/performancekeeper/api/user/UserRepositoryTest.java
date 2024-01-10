package com.example.performancekeeper.api.user;

import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
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

    @Test
    @DisplayName("Username으로 유저 찾기 테스트")
    public void findByUsernameAndDeletedAtIsNull() {
        // given
        UserEntity user1 = new UserEntity();
        user1.setUsername("test user 1");
        user1.setEncodedPW("test user 1");
        user1.setRole("Student");
        UserEntity user2 = new UserEntity();
        user2.setUsername("test user 2");
        user2.setEncodedPW("test user 2");
        user2.setRole("Student");
        userRepository.save(user1);
        userRepository.save(user2);

        // when
        UserEntity userEntity1 = userRepository.findByUsernameAndDeletedAtIsNull("test user 1").get();
        UserEntity userEntity2 = userRepository.findByUsernameAndDeletedAtIsNull("test user 2").get();
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsernameAndDeletedAtIsNull("test user 3");

        // then
        Assertions.assertThat(user1).isEqualTo(userEntity1);
        Assertions.assertThat(user2).isEqualTo(userEntity2);
        Assertions.assertThat(userEntity1).isNotEqualTo(userEntity2);
        Assertions.assertThat(optionalUserEntity).isEmpty();
    }
}
