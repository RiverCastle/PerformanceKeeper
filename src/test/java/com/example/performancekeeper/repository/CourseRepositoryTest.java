package com.example.performancekeeper.repository;

import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.course.CourseRepository;
import com.example.performancekeeper.api.users.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CourseRepositoryTest {
    @Autowired
    CourseRepository courseRepository;
    private UserEntity testUser = new UserEntity();

    public CourseRepositoryTest() {
        this.testUser.setId(1L);
        this.testUser.setUsername("test user 1");
        this.testUser.setEncodedPW("test user 1");
        this.testUser.setRole("User");
    }

    @AfterEach
    public void clear() {
        courseRepository.deleteAll();
    }

    @Test
    public void save() {
        // given
        CourseEntity course = new CourseEntity();
        course.setName("test course name 1");
        course.setDescription("test course desc 1");
        course.setJoinCode("test course join code 1");

        // when
        courseRepository.save(course);

        // then
        Assertions.assertThat(courseRepository.findByIdAndDeletedAtIsNull(course.getId()).get()).isEqualTo(course);
    }
}
