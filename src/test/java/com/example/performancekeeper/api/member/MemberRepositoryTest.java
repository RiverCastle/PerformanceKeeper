package com.example.performancekeeper.api.member;

import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.course.CourseRepository;
import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class MemberRepositoryTest {
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private UserEntity testUser;
    private UserEntity testUser2;
    private CourseEntity testCourse;
    private CourseEntity testCourse2;
    private CourseEntity testCourse3;
    @Autowired
    public MemberRepositoryTest(MemberRepository memberRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.memberRepository = memberRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @BeforeEach
    public void createTestUserAndTestCourse() {
        System.out.println("Test Users | Test Courses 생성");

        this.testUser = new UserEntity("test user 1", "1234", "User");
        userRepository.save(testUser);

        this.testUser2 = new UserEntity("test user 2", "1234", "User");
        userRepository.save(testUser2);

        this.testCourse = new CourseEntity();
        this.testCourse.setName("test course");
        this.testCourse.setDescription("test desc");
        this.testCourse.setJoinCode("1234");
        courseRepository.save(testCourse);

        this.testCourse2 = new CourseEntity();
        this.testCourse2.setName("test course2");
        this.testCourse2.setDescription("test desc2");
        this.testCourse2.setJoinCode("1234");
        courseRepository.save(testCourse2);

        this.testCourse3 = new CourseEntity();
        this.testCourse3.setName("test course3");
        this.testCourse3.setDescription("test desc3");
        this.testCourse3.setJoinCode("1234");
        courseRepository.save(testCourse3);
    }

    @AfterEach
    public void clear() {
        memberRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }
    @Test
    @DisplayName("유저로 멤버 조회 테스트")
    void findAllByUserAndDeletedAtIsNull() {
        // given
        MemberEntity member = new MemberEntity(testUser, testCourse, "Manager");
        memberRepository.save(member);
        MemberEntity member2 = new MemberEntity(testUser, testCourse2, "Manager");
        memberRepository.save(member2);
        MemberEntity member3 = new MemberEntity(testUser, testCourse3, "Student");
        memberRepository.save(member3);
        MemberEntity member4 = new MemberEntity(testUser2, testCourse3, "Manager");
        memberRepository.save(member4);

        // when
        List<MemberEntity> myMembers = memberRepository.findAllByUserAndDeletedAtIsNull(testUser);

        // then
        Assertions.assertThat(myMembers.size()).isEqualTo(3);
        Assertions.assertThat(myMembers).doesNotContain(member4);
    }

    @Test
    @DisplayName("수강생 멤버 조회 테스트")
    void findAllByCourseAndRoleAndDeletedAtIsNull() {
        // given
        MemberEntity member = new MemberEntity(testUser, testCourse, "Manager");
        memberRepository.save(member);
        MemberEntity member2 = new MemberEntity(testUser, testCourse2, "Manager");
        memberRepository.save(member2);
        MemberEntity member3 = new MemberEntity(testUser, testCourse3, "Student");
        memberRepository.save(member3);
        MemberEntity member4 = new MemberEntity(testUser2, testCourse3, "Manager");
        memberRepository.save(member4);
        MemberEntity member5 = new MemberEntity(testUser2, testCourse, "Student");
        memberRepository.save(member5);

        // when
        List<MemberEntity> studentsOfTestCourse = memberRepository.findAllByCourseAndRoleAndDeletedAtIsNull(testCourse, "Student");
        List<MemberEntity> studentsOfTestCourse2 = memberRepository.findAllByCourseAndRoleAndDeletedAtIsNull(testCourse2, "Student");
        List<MemberEntity> studentsOfTestCourse3 = memberRepository.findAllByCourseAndRoleAndDeletedAtIsNull(testCourse3, "Student");

        // then
        Assertions.assertThat(studentsOfTestCourse).contains(member5);
        Assertions.assertThat(studentsOfTestCourse).doesNotContain(member2);
        Assertions.assertThat(studentsOfTestCourse.size()).isEqualTo(1);

        Assertions.assertThat(studentsOfTestCourse2).doesNotContain(member);
        Assertions.assertThat(studentsOfTestCourse2.size()).isEqualTo(0);

        Assertions.assertThat(studentsOfTestCourse3).contains(member3);
        Assertions.assertThat(studentsOfTestCourse3.size()).isEqualTo(1);
        Assertions.assertThat(studentsOfTestCourse).doesNotContain(member4);
    }

    @Test
    @DisplayName("학생 or 강사 확인 테스트")
    void findByUserAndCourseAndRoleAndDeletedAtIsNull() {
        // given
        MemberEntity member = new MemberEntity(testUser, testCourse, "Manager");
        memberRepository.save(member);
        MemberEntity member2 = new MemberEntity(testUser, testCourse2, "Manager");
        memberRepository.save(member2);
        MemberEntity member3 = new MemberEntity(testUser, testCourse3, "Student");
        memberRepository.save(member3);
        MemberEntity member4 = new MemberEntity(testUser2, testCourse3, "Manager");
        memberRepository.save(member4);
        MemberEntity member5 = new MemberEntity(testUser2, testCourse, "Student");
        memberRepository.save(member5);

        // when
        Optional<MemberEntity> result1 =
                memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(testUser, testCourse, "Manager");
        Optional<MemberEntity> result2 =
                memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(testUser, testCourse2, "Manager");
        Optional<MemberEntity> result3 =
                memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(testUser, testCourse3, "Manager");
        Optional<MemberEntity> result4 =
                memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(testUser2, testCourse2, "Manager");
        Optional<MemberEntity> result5 =
                memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(testUser2, testCourse3, "Manager");
        Optional<MemberEntity> result6 =
                memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(testUser, testCourse, "Student");
        Optional<MemberEntity> result7 =
                memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(testUser, testCourse2, "Student");
        Optional<MemberEntity> result8 =
                memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(testUser, testCourse3, "Student");
        Optional<MemberEntity> result9 =
                memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(testUser2, testCourse2, "Student");
        Optional<MemberEntity> result10 =
                memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(testUser2, testCourse3, "Student");
        Optional<MemberEntity> result12 =
                memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(testUser2, testCourse, "Manager");
        Optional<MemberEntity> result11 =
                memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(testUser2, testCourse, "Student");
        // then
        Assertions.assertThat(result1).isNotEmpty();
        Assertions.assertThat(result2).isNotEmpty();
        Assertions.assertThat(result3).isEmpty();
        Assertions.assertThat(result4).isEmpty();
        Assertions.assertThat(result5).isNotEmpty();
        Assertions.assertThat(result6).isEmpty();
        Assertions.assertThat(result7).isEmpty();
        Assertions.assertThat(result8).isNotEmpty();
        Assertions.assertThat(result9).isEmpty();
        Assertions.assertThat(result10).isEmpty();
        Assertions.assertThat(result11).isNotEmpty();
        Assertions.assertThat(result12).isEmpty();
    }

    @Test
    @DisplayName("강의실 입실 여부 테스트")
    void findByUserAndCourseAndDeletedAtIsNull() {
        // given
        MemberEntity member = new MemberEntity(testUser, testCourse, "Manager");
        memberRepository.save(member);
        MemberEntity member2 = new MemberEntity(testUser, testCourse2, "Manager");
        memberRepository.save(member2);
        MemberEntity member3 = new MemberEntity(testUser, testCourse3, "Student");
        memberRepository.save(member3);
        MemberEntity member4 = new MemberEntity(testUser2, testCourse3, "Manager");
        memberRepository.save(member4);
        MemberEntity member5 = new MemberEntity(testUser2, testCourse, "Student");
        memberRepository.save(member5);

        // when
        Optional<MemberEntity> result1 = memberRepository.findByUserAndCourseAndDeletedAtIsNull(testUser, testCourse);
        Optional<MemberEntity> result2 = memberRepository.findByUserAndCourseAndDeletedAtIsNull(testUser, testCourse2);
        Optional<MemberEntity> result3 = memberRepository.findByUserAndCourseAndDeletedAtIsNull(testUser, testCourse3);
        Optional<MemberEntity> result4 = memberRepository.findByUserAndCourseAndDeletedAtIsNull(testUser2, testCourse);
        Optional<MemberEntity> result5 = memberRepository.findByUserAndCourseAndDeletedAtIsNull(testUser2, testCourse2);
        Optional<MemberEntity> result6 = memberRepository.findByUserAndCourseAndDeletedAtIsNull(testUser2, testCourse3);

        // then
        Assertions.assertThat(result1).isNotEmpty();
        Assertions.assertThat(result2).isNotEmpty();
        Assertions.assertThat(result3).isNotEmpty();
        Assertions.assertThat(result4).isNotEmpty();
        Assertions.assertThat(result5).isEmpty();
        Assertions.assertThat(result6).isNotEmpty();
    }
}