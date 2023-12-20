package com.example.performancekeeper.repository;

import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.course.CourseRepository;
import com.example.performancekeeper.api.users.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
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

    @Test
    @DisplayName("Soft Deletion 확인")
    void findByIdAndDeletedAtIsNull() {
        // given
        CourseEntity course1 = new CourseEntity();
        CourseEntity course2 = new CourseEntity();
        CourseEntity course3 = new CourseEntity();
        course1.setName("test course name 1");
        course1.setDescription("test course desc 1");
        course1.setJoinCode("test course join code 1");
        course2.setName("test course name 2");
        course2.setDescription("test course desc 2");
        course2.setJoinCode("test course join code 2");
        course3.setId(4L);
        course3.setDeletedAt(LocalDateTime.now());
        course3.setName("test course name 3");
        course3.setDescription("test course desc 3");
        course3.setJoinCode("test course join code 3");

        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);

        // when
        CourseEntity courseEntity1 = courseRepository.findByIdAndDeletedAtIsNull(course1.getId()).get();
        CourseEntity courseEntity2 = courseRepository.findByIdAndDeletedAtIsNull(course2.getId()).get();
        Optional<CourseEntity> optionalCourseEntity = courseRepository.findByIdAndDeletedAtIsNull(course3.getId());

        // then
        Assertions.assertThat(course1).isEqualTo(courseEntity1);
        Assertions.assertThat(course1).isNotEqualTo(courseEntity2);
        Assertions.assertThat(optionalCourseEntity).isEmpty();
    }

    @Test
    @DisplayName("특정 키워드를 가지고 있는 course 찾기")
    void findAllByNameContainingAndDeletedAtIsNull() {
        // given
        CourseEntity course1 = new CourseEntity();
        CourseEntity course2 = new CourseEntity();
        CourseEntity course3 = new CourseEntity();
        course1.setName("test course name 1");
        course1.setDescription("test course desc 1");
        course1.setJoinCode("test course join code 1");
        course2.setName("test  name 2");    // name에 course가 없음.
        course2.setDescription("test course desc 2");
        course2.setJoinCode("test course join code 2");
        course3.setId(4L);
        course3.setDeletedAt(LocalDateTime.now());
        course3.setName("test course name 3");
        course3.setDescription("test course desc 3");
        course3.setJoinCode("test course join code 3");

        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);

        // when
        List<CourseEntity> result = courseRepository.findAllByNameContainingAndDeletedAtIsNull("course");

        // then
        Assertions.assertThat(result).contains(course1); // course 1은 조회, course 2는 X, course 3은 삭제됨.
    }
}
