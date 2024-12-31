# 쿠폰 관리 서비스

## 프로젝트 개요

사용자에게 쿠폰을 생성, 관리, 사용 및 조회하는 기능을 제공합니다.</br>
**시연 영상**과 **주요 기능**은 토글을 열어서 확인 가능합니다

---

## 사용 기술 스택

- **Backend**: Java 21, Spring Boot 3.4.0, JPA (Hibernate)
- **Database**: MariaDB, mongoDB
- **Cache**: Redis
- **Deployment**: Docker
- **Authentication**: JWT
- **Build :** Gradle

---

## 요구 사항

- 1개의 주제당 n개의 쿠폰 코드 발행이 가능합니다.
- 쿠폰 코드는 사용자별 1회 사용이 가능합니다.
- 쿠폰 코드는 숫자와 알파벳을 혼용하여 16자리로 구성됩니다.
- 단일 쿠폰 코드에 대해서는 동시 요청시에도 중복 사용이 되지 않도록 처리합니다.
- 필수 구현 사항
    1. 쿠폰 발행
    2. 쿠폰 사용
    3. 일괄 쿠폰 정지 (주제별)
- 선택 구현 사항
    1. Unit Test 작성
    2. API 문서 작성
    3. 누적되는 쿠폰 데이터 조회 성능 조회

---

## ERD

아래는 쿠폰 관리 서비스의 데이터베이스 설계입니다:
![coupon_erd](https://github.com/user-attachments/assets/ac08035c-f6e6-4ea1-8d76-72cea9d26bed)



### ERD 설명

- **Coupon**: 쿠폰 정보를 저장하는 테이블.
- **User**: 사용자 정보를 저장하는 테이블.
- **CouponRedemption**: 쿠폰과 사용자 간의 매핑 테이블 (중간 테이블).
- **CouponTopic**: 쿠폰의 그룹 정보를 관리하는 테이블.

---

## 인프라 설계

서비스의 인프라는 다음과 같이 구성되어 있습니다:</br>

<img src="https://github.com/user-attachments/assets/0d151989-e0cd-4f82-9318-28e9791e1043" alt="codegenerator" style="border: 1px solid #ddd; border-radius: 5px; width: 500px;">





### 인프라 설명

- **Application Layer**: Spring Boot 기반의 애플리케이션. MVC 패턴으로 작업
- **Database**: MariaDB를 사용하여 관계형 데이터 저장. mongoDB를 사용하여 오래된 데이터 파티셔닝
- **Cache**: Redis를 사용하여 쿠폰 사용시 동시성 문제 예방
- **Monitoring :** Prometheus를 이용하여 Circuit Breaker 구현

---

## 시연 영상

<details>
<summary> <strong> 영상 내용 (클릭) </strong> </summary>
</br>
1. 회원 가입 - 로그인 (jwt 토큰 발급)</br>
    

https://github.com/user-attachments/assets/8dc56337-1292-436f-8982-95ceeadb9860


2. 쿠폰 토픽 생성 - 쿠폰 생성 - 쿠폰 토픽으로 일괄 비활성화 처리</br>


https://github.com/user-attachments/assets/f6968ad3-ab64-4f48-9603-050dfcaf9049



3. 쿠폰 일괄 조회 및 검색 기능 확인</br>


https://github.com/user-attachments/assets/bec806ad-7770-4bfa-8423-75cb00391e4d


4. 쿠폰 코드로 쿠폰 사용 처리하기</br>

- 쿠폰 사용 오류 내고 prometheus로 fallback 확인하기</br>

https://github.com/user-attachments/assets/1786f95c-54d9-4101-9a6b-d68d21271449

5. 배치를 통해 데이터 마이그레이션 (시연 영상에서는 즉시 마이그레이션 됩니다.)</br>

</br>

https://github.com/user-attachments/assets/6968287d-88b7-4cce-bf2a-1a02501720b2

6. build 후 rest docs (api 명세서) 확인하기

</br>


https://github.com/user-attachments/assets/757a08ce-08f6-4443-879e-cd3954c34aeb


</details>

시연은 POST MAN과 intelliJ IDE로 진행하였으며, 순서는 다음과 같습니다.

1. 회원 가입 - 로그인 (jwt 토큰 발급)
2. 쿠폰 토픽 생성 - 쿠폰 생성 - 쿠폰 토픽으로 일괄 비활성화 처리
3. 쿠폰 일괄 조회 및 검색 기능 확인
4. 쿠폰 코드로 쿠폰 사용 처리 하기
    1. 쿠폰 사용 오류 내서 fallback 함수 발동하는지 확인
    2. prometheus로 fallback 확인
5. 배치를 통해 쿠폰 데이터 mongoDB로 마이그레이션 후 일괄 삭제
6. build 후 rest docs (api 명세서) 확인하기

---

## 주요 기능 (코드 내용 포함)

### 1. 쿠폰 topic 관리

- 쿠폰 주제 생성
- 쿠폰 주제 조회
- 주제별 쿠폰 삭제 (soft delete)
<details>
<summary>주제별 쿠폰 활성화 / 비활성화</summary>
</br>
- controller</br>
    <img src="https://github.com/user-attachments/assets/6ee92252-9cdc-41e4-b389-9999053e56aa" alt="activationcontroller" style="border: 1px solid #ddd; border-radius: 5px; width="400px"></br>
- service</br>
<img src="https://github.com/user-attachments/assets/61088bee-f4f2-4417-8143-11c590e3148f" alt="activationcontroller" style="border: 1px solid #ddd; border-radius: 5px; width="400px"></br>
</details>


### 2. 쿠폰 관리


<details>
<summary>일괄 쿠폰 정보 조회 및 검색</summary>
</br>
- query dsl을 사용하여, 검색을 위해 필요한 동적 쿼리 생성
</br>
    <img src="https://github.com/user-attachments/assets/e5755d8b-6854-4d4b-9f96-71c9bf429622" alt="querydsl" style="border: 1px solid #ddd; border-radius: 5px; width="400px"></br>
</details>

<details>
<summary>쿠폰 생성</summary>
</br>
- code generator util을 만들어 코드가 겹치지 않게 생성
</br>
    <img src="https://github.com/user-attachments/assets/85c3c65f-4564-42b2-9708-cf7b9f89a5b1" alt="codegenerator" style="border: 1px solid #ddd; border-radius: 5px; width="400"></br>
- custom exception을 터뜨려 예외 처리, 쿠폰 생성 서비스</br>
    <img src="https://github.com/user-attachments/assets/363062fa-69d8-4779-90ef-f82f6044b6e6" alt="codegenerator" style="border: 1px solid #ddd; border-radius: 5px; width="400"></br>


</details>

<details>
<summary>쿠폰 코드를 입력받아 사용처리</summary>
</br>
- code를 입력받으면 우선 검증을 합니다. 사용 되었는지(isRedeemed), 활성화 되었는지(isActive)</br>
- 검증이 완료되었으면 redis에서 lock key를 생성하여 쿠폰 사용 메서드에 동시에 접근하는 것을 막습니다.</br>
- 접근을 하게되면, 쿠폰은 사용처리가 되고 쿠폰과 유저의 중간 테이블이 생성됩니다.</br>
- redis의 문제로 빈번한 실패 시에는, resilience4j가 fallback method를 실행시킵니다.</br>
- 서버의 9090포트로 들어가서 prometheus 로그를 확인하여 circuitbreaker가 어떻게 작동하는지 확인할 수 있습니다.</br>
- fallback method에서는 ConcurrentHashMap으로 동시성을 제어하였습니다. (단일 서버 한정)</br>
</br>
- 
    <img src="https://github.com/user-attachments/assets/85c3c65f-4564-42b2-9708-cf7b9f89a5b1" alt="codegenerator" style="border: 1px solid #ddd; border-radius: 5px; width="400"></br>
- custom exception을 터뜨려 예외 처리, 쿠폰 생성 서비스</br>
    <img src="https://github.com/user-attachments/assets/363062fa-69d8-4779-90ef-f82f6044b6e6" alt="codegenerator" style="border: 1px solid #ddd; border-radius: 5px; width="400"></br>


</details>
<details>
<summary>오래된 쿠폰 데이터 관리</summary>
</br>
- 스프링 배치를 활용하여 지워진 지 6개월 이상 된 쿠폰 데이터를 mongoDB에 마이그래이션 하였습니다.<br/>
- 배치에서는 job과 step으로 나뉘며, step에서는 reader,writer,proccesor 을 파라미터로 주입받아 100개의 데이터씩 처리합니다.</br>
- 스케줄링 기능으로 3개월에 한 번씩 동작합니다.</br>
- 추가로 자주 검색하는 Coupon 테이블의 code 컬럼과 CouponRedemption의 coupon_fk는 DB에 인덱스를 추가하여 쿼리 성능을 높였습니다.</br>
</br>
- index (sql) </br>
    <img src="https://github.com/user-attachments/assets/281e2395-7a76-4344-894a-b571868d0183" alt="index" style="border: 1px solid #ddd; border-radius: 5px;"></br>
    </br>
- ItemReader </br>
    <img src = "https://github.com/user-attachments/assets/590cb489-16a5-4f5b-8951-9cbcb224b4cf" alt="ItemReader" style="border: 1px solid #ddd; border-radius: 5px;"></br>
    </br>
- ItemWriter</br>
    <img src = "https://github.com/user-attachments/assets/508a503b-a8d3-4496-97be-a0d5df0c75b0" alt = "ItemWriter1" style="border: 1px solid #ddd; border-radius: 5px;"></br>
    <img src = "https://github.com/user-attachments/assets/b98d0eaf-81e9-40be-835d-4fa951670037" alt = "ItemWriter2" style="border: 1px solid #ddd; border-radius: 5px;"></br>
    </br>    
- ItemProcessor </br>
    <img src = "https://github.com/user-attachments/assets/e4bc4d30-bf06-4459-baf5-4e30d2265637" alt="ItemProcessor" style="border: 1px solid #ddd; border-radius: 5px;"></br>
    </br>
- BatchConfig </br>
    <img src = "https://github.com/user-attachments/assets/26778a7c-40cf-4218-aef2-6b2bacce81c2" alt="batchconfig" style="border: 1px solid #ddd; border-radius: 5px;"></br>
    </br>
- BatchService </br>
    <img src="https://github.com/user-attachments/assets/26283775-4288-41d4-9b41-01ed3cfb74cc" alt="batchservice" style="border: 1px solid #ddd; border-radius: 5px;">
    </br>
</details>


### 3. 사용자 관리

- 사용자 회원가입
- jwt를 이용하여 인증 및 인가 관리

### 4. 테스트 코드

<details>
<summary>쿠폰 사용 처리 테스트 코드</summary>
</br>
- controller 테스트에서는 @WebMvcTest 어노테이션을 사용하여 컨트롤러 계층만 빈에 로드하였습니다.</br>
- controller 공용 response를 분리하여 static으로 생성하였습니다.</br>
- service 테스트에서는 레포지토리를 모킹하여 단위 테스트를 작성하였습니다.</br>
- 컨트롤러 테스트가 통과하면 rest docs에 반영이 됩니다.</br>
</br>
- controller test
<img src="https://github.com/user-attachments/assets/e639e849-89ac-491d-9cdc-53b2e6b94130" alt="controllertest" style="border: 1px solid #ddd; border-radius: 5px; width="400"></br>

- service test (에러 검증 및 정상 사용 테스트)
<img src="https://github.com/user-attachments/assets/80e4aa7b-9163-4e23-918e-9eb7ca7eeb82" alt="servicevalid1" style="border: 1px solid #ddd; border-radius: 5px;">


</br>
<img src="https://github.com/user-attachments/assets/eeb3b54a-496b-4772-93aa-8bf03e935d42" alt="servicevalid2" style="border: 1px solid #ddd; border-radius: 5px; width="400">

</br>
<img src="https://github.com/user-attachments/assets/0022f04f-0ee2-40de-8968-ebbc61777d9e" alt="servicevalid3" style="border: 1px solid #ddd; border-radius: 5px; width="400">

</br>
<img src="https://github.com/user-attachments/assets/738055ee-fc6f-40e5-b680-4381ac42e51d" alt="servicecouponuse" style="border: 1px solid #ddd; border-radius: 5px; width="400">

</details>


---

## 트러블 슈팅

### 1. 쿠폰 사용시 발생하는 동시성 문제

- **문제**: 쿠폰 사용 시 여러 사용자가 동시에 요청을 보내면 중복 사용 발생.</br>
- **해결**: Redis를 활용하여 동시 요청 처리, Redis에 문제 발생 시 resilience4j를 통해 fallback 메소드 실행, fallback 메소드는 ConcurrentHashMap으로 구현</br>
메서드에 직접 syncronized 키워드를 붙이거나 DB에 락을 거는 방법도 있었으나, 전체적으로 락을 걸기 때문에 성능 저하가 우려되어 bucket에만 락을거는 ConcurrentHashmap을 선택했습니다.
또한, 다중 클러스터 환경을 고려하여 Redis를 사용하였습니다.

### 2. 오래된 데이터 누적 시 발생하는 쿼리 성능 감소 문제

- **문제**: 쿠폰 데이터 누적시 쿼리 성능이 감소
- **해결**: DB index 설정, mongoDB에 주기적으로 파티셔닝 하여 쿼리 성능 문제 해결</br>
자주 입력하는 code에 인덱스를 걸었으며, coupon_fk에 인덱스 처리를 하여 couponRedemption 테이블 조회를 신속하게 할 수 있습니다.

---

## 개선점
- 요구 사항에 따라 ReentrantLock이나 메시지 큐를 활용하면 동시성 문제를 효과적으로 해결할 수 있을 거라 생각합니다.</br>
- JPA Repository를 직접 상속받는 대신, 별도의 Repository 인터페이스를 정의하고 이를 구현하는 구조로 변경하면 더 명확하고 유연한 설계가 가능합니다.</br>
- Exception 처리 로직이 중복되는 부분은 상속 구조를 도입하여 재사용성을 높일 수 있습니다.</br>
- QueryDSL의 Q엔티티를 static으로 관리하면 객체를 반복적으로 생성하는 비용을 줄일 수 있습니다.</br>
- BooleanExpression의 구성 요소를 미리 정의해 두면 재사용성을 높이는 데 도움이 될 것입니다.</br>
- 디렉토리 구조를 수정해도 좋을 거 같습니다. 가령, core / base 으로 나누어 controller, service, repository 등의 로직과 직결된 파일은 core, 나머지 엔티티나 dto, config는 base에 두어도 괜찮을 거 같습니다.</br>
예시 )</br>
src/main/java/com/we_assignment</br>
├── core</br>
│   ├── config                      # 공통 설정 클래스</br>
│   ├── domain                      # 핵심 도메인 엔티티</br>
│   │   ├── Coupon.java</br>
│   │   ├── CouponRedemption.java</br>
│   │   └── User.java</br>
│   ├── exception                   # 공통 예외 처리</br>
│   ├── repository                  # 핵심 레포지토리 인터페이스</br>
│   └── service                     # 핵심 비즈니스 로직</br>
│</br>
├── base</br>
│   ├── entity                      # 공통 엔티티 클래스</br>
│   │   └── BaseTimeEntity.java</br>
│   ├── exception                   # 공통 예외 정의</br>
│   │   └── CustomException.java</br>
│   ├── util                        # 공통 유틸리티 클래스</br>
│   └── constants                   # 상수 관리</br>
│   └── ErrorCode.java</br>
└── ...</br>
---

## 프로젝트 실행 방법

### 1. Clone Repository

git clone https://github.com/ksngh/coupon_service.git

cd coupon_service

### 2. pull docker image & container 띄우기

- mongoDB </br>
docker run -d --name mongodb -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=we_assignment -e MONGO_INITDB_ROOT_PASSWORD=we_password mongo:6.0</br>
- prometheus {/path/to/your/prometheus.yml}은 해당 파일이 있는 절대 경로입니다.</br>
docker run -d --name prometheus -p 9090:9090 -v "{/path/to/your/prometheus.yml}:etc/prometheus/" prom/prometheus</br>
- mariaDB </br>
docker run -d --name mariadb-container -e MYSQL_ROOT_PASSWORD=root_password -e MYSQL_DATABASE=we_database -e MYSQL_USER=we_user -e MYSQL_PASSWORD=my_password -p 3306:3306 mariadb:latest</br>
- redis</br>
docker run -d --name redis-container -p 6379:6379 redis:latest

### 3. Build and Run

./gradlew build

cd build/libs

java -jar coupon_service-0.0.1-SNAPSHOT.jar

* resources/static/sqls 디렉토리 내 batch의 스키마와 index의 sql문이 자동화가 되어있지 않습니다.</br>
따로 DB에서 실행 하여야 합니다.

### 4. API 문서 확인

빌드 후 rest Docs를 통해 API 명세를 확인할 수 있습니다</br>
디렉토리는 /build/docs/asciidoc/index.html 입니다.

---

## 작업 방식

1. 이슈를 생성하여 문제를 기록합니다.
2. 기능 별로 브랜치를 생성하여 해당 브랜치의 기능에서 작업을 합니다.
3. Pull Request를 통해 코드를 머지하였습니다.
