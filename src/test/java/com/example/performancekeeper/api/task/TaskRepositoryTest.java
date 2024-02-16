package com.example.performancekeeper.api.task;

import com.example.performancekeeper.api.entity.CourseEntity;
import com.example.performancekeeper.api.entity.task.TaskEntity;
import com.example.performancekeeper.api.repository.CourseRepository;
import com.example.performancekeeper.api.entity.MemberEntity;


import com.example.performancekeeper.api.repository.MemberRepository;
import com.example.performancekeeper.api.repository.TaskRepository;
import com.example.performancekeeper.api.entity.UserEntity;

import com.example.performancekeeper.api.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TaskRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TaskRepository taskRepository;
    private UserEntity testUser;
    private List<CourseEntity> testCourses = new ArrayList<>();
    private List<MemberEntity> testMembers = new ArrayList<>();

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setRole("User");
        testUser.setUsername("test user");
        testUser.setEncodedPW("test user pw");
        userRepository.save(testUser);

        for (int i = 0; i < 3; i++) {
            CourseEntity testCourse = new CourseEntity();
            testCourse.setName("test Course " + i);
            testCourse.setJoinCode("test course pw " + i);
            testCourse.setDescription("desc " + i);
            testCourses.add(testCourse);

            MemberEntity testMember = new MemberEntity(testUser, testCourses.get(i), "Manager");
            testMembers.add(testMember);
        }
        courseRepository.saveAll(testCourses);
        memberRepository.saveAll(testMembers);
    }

    @Test
    @DisplayName("강의실 별 실습과제 조회")
    void findAllByCourseAndDeletedAtIsNull() {
        // given
        Random randomGenerator = new Random();
        int[] randomInts = new int[3];

        for (int i = 0; i < 3; i++) {
            CourseEntity testCourse = testCourses.get(i);
            String courseName = testCourse.getName();
            System.out.println(i + 1 + "번 강의실에 과제 생성을 시작합니다.");
            int randomInt = randomGenerator.nextInt(1, 777);
            randomInts[i] = randomInt;
            System.out.println("생성된 임의의 수 " + randomInt + "개의 task를 생성합니다.");
            MemberEntity testManager = testMembers.get(i);
            List<TaskEntity> testTasks = new ArrayList<>();
            for (int j = 0; j < randomInt; j++) {
                TaskEntity testTask = new TaskEntity();
                testTask.setName(courseName + " test task " + j);
                testTask.setCourse(testCourse);
                testTasks.add(testTask);
            }
            taskRepository.saveAll(testTasks);
        }

        // when
        List<TaskEntity> result1 = taskRepository.findAllByCourseAndDeletedAtIsNull(testCourses.get(0));
        List<TaskEntity> result2 = taskRepository.findAllByCourseAndDeletedAtIsNull(testCourses.get(1));
        List<TaskEntity> result3 = taskRepository.findAllByCourseAndDeletedAtIsNull(testCourses.get(2));

        // then
        Assertions.assertThat(result1.size()).isEqualTo(randomInts[0]);
        Assertions.assertThat(result2.size()).isEqualTo(randomInts[1]);
        Assertions.assertThat(result3.size()).isEqualTo(randomInts[2]);
    }

    @Test
    @DisplayName("강의실 별 과제 시작일을 조건으로 조회")
    void findAllByCourseAndStartAtAndDeletedAtIsNull() {
        // given
        Random randomGenerator = new Random();
        int targetDay = randomGenerator.nextInt(1, 9);
        LocalDate targetDate = LocalDate.of(2024,1,targetDay);
        System.out.println("테스트 할 타겟 날짜는 " + targetDate + " 입니다.");
        List<Integer> expected = new ArrayList<>();
        CourseEntity testCourse = testCourses.get(0);
        String courseName = testCourse.getName();
        List<Integer> testDays = new ArrayList<>();
        int answer = 0;
        for (int i = 0; i < 20; i++) testDays.add(randomGenerator.nextInt(1, 9));

        for (Integer i : testDays) {
            LocalDate tempStartAt = LocalDate.of(2024, 1, i);
            int randomTaskInt = randomGenerator.nextInt(1, 30);
            System.out.println(tempStartAt.toString() + "에 시작하는 과제를 생성합니다. 총 " + randomTaskInt + "개를 생성합니다.");
            List<TaskEntity> testTasks = new ArrayList<>();
            for (int j = 0; j < randomTaskInt; j++) {
                TaskEntity testTask = new TaskEntity();
                testTask.setStartAt(tempStartAt);
                testTask.setCourse(testCourse);
                testTask.setName(courseName + "test task name " + j);
                testTasks.add(testTask);
            }
            taskRepository.saveAll(testTasks);
            if (targetDay == i) {
                expected.add(randomTaskInt);
                answer += randomTaskInt;
            }
        }

        // when
        List<TaskEntity> result = taskRepository.findAllByCourseAndStartAtAndDeletedAtIsNull(testCourse, targetDate);
        System.out.println("시작일이 " + targetDate + "인 실습과제는 총 " + expected.size() + "번에 걸쳐서 다음과 같이 부여되었습니다." + expected.toString());
        System.out.println("시작일인 " + targetDate + "를 기준으로 조회된 과제의 개수는 " + result.size() + "개입니다.");

        // then
        Assertions.assertThat(result.size()).isEqualTo(answer);
    }

    @Test
    void findAllByCourseAndNameContainingAndDeletedAtIsNull() {
    }

    @Test
    void findByIdAndDeletedAtIsNull() {
    }
}