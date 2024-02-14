# Performance Keeper
Performance Keeper는 실시간 온라인 교육 현장에서 학생, 강사, 그리고 매니저들을 위해 실습과제를 용이하게 관리할 수 있도록 돕는 서비스입니다.
프로젝트 소개자료 pdf 다운로드 [최강성_Performance_Keeper_소개자료.pdf](https://github.com/RiverCastle/PerformanceKeeper/files/13768537/_Performance_Keeper_.pdf)

## 🚀Enhancements (발전내용)
### 1. JPA N + 1 문제 해결📈 [관련 블로그 포스팅](https://programming-with-j.tistory.com/36)
DB로 데이터를 요청하기 위해 1개의 쿼리를 보냈는데, 다른 부수적인 쿼리들이 추가로 보내지는 상황을 포착하였습니다. 원인은 크게 2가지로, 지연 로딩을 처리하지 않은 경우와 연관관계에 있는 엔티티들을 보낸 쿼리로 조회할 수 없었기 때문이었습니다. 첫 번째 문제는 Fetch 전략을 Lazy로 설정하여 불필요한 쿼리 N 개가 발생하지 않도록 막았고, 두 번째 문제는 연관관계에 있는 필요한 엔티티에 대해 Fetch join을 사용해서 불필요한 쿼리 N 개가 발생하지 않도록 조치하여 문제를 해결하였습니다.

### 2. 로그인(JWT 발급) 성능 개선📈 [관련 블로그 포스팅](https://programming-with-j.tistory.com/42)
클라이언트로부터 ID와 PW를 입력받은 후, 입력받은 데이터를 바로 DB에 데이터를 조회하여 비교하는 로직을 사용하고 있었습니다. 로그인에 성공하는 경우에 대해서는 정당한 쿼리라고 생각하였지만, 로그인에 실패하는 경우에 대해서는 굳이 DB에 쿼리를 보낼 필요가 없을 것이라고 생각하였습니다. 그래서 상용 서비스의 아이디와 비밀번호 생성 조건(길이)에 대해서 떠올렸고, 생성 조건에 위배되는 요청에 대해서는 DB에 쿼리를 보내기 전에 에러 메시지를 발생시켜 불필요한 쿼리를 방지하였으며, 평균 20ms에서 3ms 이내로 로그인 실패 응답을 보낼 수 있는 이점을 챙길 수 있었습니다. 

### 3. 순환참조 문제 해결을 위한 Facade Pattern 적용🛠️ [관련 블로그 포스팅](https://programming-with-j.tistory.com/35)
(매니저)사용자가 강의실을 생성하는 요청에 대하여 Course와 Member 도메인에 대하여 각각 Entity를 생성하는 처리를 하다보니 CourseService에서 새 강의실을 생성하는 로직을 수행한 뒤에 MemberService의 관리자 멤버를 생성하는 메서드를 호출하도록 코드를 작성하였습니다. 추후에 Member 도메인에 대한 요청을 처리하면서 Course 유효성 검증을 위해 CourseService를 MemberService에 호출하게 되면서 순환참조 문제를 겪었습니다. 이를 계기로 여러 도메인에 걸친 요청을 처리하면서 각 도메인에 대해서 분리할 수 있는 방법을 찾은 결과 Facade Pattern을 이용해 문제를 해결할 수 있었습니다. 


## 주요 기능 및 목적 소개

### 학생모드
- 현재 참여 중인 강의실 목록과 부여된 과제의 완수율을 한눈에 확인할 수 있습니다.
- 과제는 부여된 날짜에 따라 미완료와 완료로 나뉘어 표시되어, 실습 내용과 채팅이 혼재되는 문제를 해결하였습니다.


### 매니저모드
- 강의실에 입실한 학생들의 평균 과제 완수률을 조회하고, 날짜 또는 검색어를 활용해 각 학생들의 과제 진행 현황을 상세하게 파악할 수 있습니다.
- 과제명에 커서를 위치시키면 해당 과제의 진행 현황을 상태별로 확인할 수 있어, 강사와 매니저는 학생들의 성과를 더욱 효과적으로 관리할 수 있습니다.


## ERD
![Performance Keeper ERD](https://github.com/RiverCastle/PerformanceKeeper/assets/131141755/1fd056ae-48b1-4e1c-9ee5-4e7aa0de4f84)
---
## 화면 구성 및 기능 소개
### 홈화면
![Performance Keeper Home Page](https://github.com/RiverCastle/PerformanceKeeper/assets/131141755/a16105df-420f-470a-8d8a-0e2b298271c4)
---
### 학생모드
![Student Course Leave](https://github.com/RiverCastle/PerformanceKeeper/assets/131141755/5e0d02e4-2e78-4067-aedc-515b97cf5f85)
---
![Student Course Page](https://github.com/RiverCastle/PerformanceKeeper/assets/131141755/ed7b8fe5-dae0-4e15-9543-2600c882b37b)
---
![Student Course Search](https://github.com/RiverCastle/PerformanceKeeper/assets/131141755/de3de5cb-923b-40be-8ae5-8600e8478d16)
---
![Student Main](https://github.com/RiverCastle/PerformanceKeeper/assets/131141755/25799768-2c36-4c13-912b-8aeed2d1aa43)
---
![Student Task Status](https://github.com/RiverCastle/PerformanceKeeper/assets/131141755/5576afa3-4836-4f33-8470-d88a7854c4ed)
---

### 매니저모드
![Manager Main](https://github.com/RiverCastle/PerformanceKeeper/assets/131141755/355f9fa9-a875-46f8-9650-67f9d5fe14cd)
---
![Manager Course Create](https://github.com/RiverCastle/PerformanceKeeper/assets/131141755/17fd5d37-135b-47cc-ac76-3fe88ba7ae79)
---
![Manager Course Page](https://github.com/RiverCastle/PerformanceKeeper/assets/131141755/871cda62-e310-435a-919b-7afeb8707794)
---
![Manager Course Page](https://github.com/RiverCastle/PerformanceKeeper/assets/131141755/2a3cb157-2db1-4593-9d3d-376441a720a6)
---
![Manager Course Page](https://github.com/RiverCastle/PerformanceKeeper/assets/131141755/7d36d08b-2759-4a86-99ff-e1aa4c45bee2)
---
![Manager Task Create](https://github.com/RiverCastle/PerformanceKeeper/assets/131141755/2cabfcc5-48a7-4bd7-a5bf-2fad9ec69e62)
---
![Manager Task Comment Reply](https://github.com/RiverCastle/PerformanceKeeper/assets/131141755/69ed01c5-a2bb-4045-9a9d-8d7e95cfa06f)
---

## 기술 스택
![Skill Stacks](https://github.com/RiverCastle/PerformanceKeeper/assets/131141755/de6a9ced-e557-4ba8-90d8-bcbeec62aef8)



