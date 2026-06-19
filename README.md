# Escape Room Manager

## 1. 프로젝트명

**Escape Room Manager - 방탈출 카페 예약 및 힌트 관리 프로그램**

방탈출 카페의 테마 예약, 고객 관리, 플레이 결과 기록, 힌트 사용 관리, 운영 통계 확인을 지원하는 Java Swing 기반 데스크톱 애플리케이션입니다.

---

## 2. 프로젝트 소개

이 프로젝트는 기존 Room Booking System 구조를 바탕으로, **방탈출 카페 운영 상황에 맞게 도메인을 재해석하고 확장한 기말 프로젝트용 프로그램**입니다.

기존 클래스명과 패키지 구조를 무리하게 전체 변경하지 않고 안정성을 우선했습니다. 대신 화면에 표시되는 문구와 README 설명에서는 다음과 같이 방탈출 카페 도메인으로 해석합니다.

| 기존 구조 | 방탈출 카페 도메인에서의 의미 |
| --- | --- |
| `User` | 고객 또는 관리자 계정 |
| `Building` | 방탈출 카페 지점 / 매장 |
| `Room` | 방탈출 테마 |
| `Reservation` | 테마 예약 |
| `PlayResult` | 플레이 결과 기록 |
| `BookingResource` | View와 Service 사이의 Controller |

프로그램 실행 시 아래 예시 테마가 기본 데이터로 등록되어 바로 기능을 확인할 수 있습니다.

| 테마명 | 장르 | 난이도 | 소요 시간 | 권장 인원 |
| --- | --- | --- | --- | --- |
| 저택의 비밀 | 공포 | HARD | 60분 | 2~4명 |
| 연구소 탈출 | SF | NORMAL | 60분 | 2~5명 |
| 사라진 탐정 | 추리 | EASY | 50분 | 2~4명 |

---

## 3. 개발 목적

방탈출 카페는 단순히 예약만 받는 것이 아니라, 운영 과정에서 다음과 같은 정보 관리가 필요합니다.

* 고객이 어떤 테마를 어느 시간에 예약했는지 관리
* 테마별 예약 현황 확인
* 플레이 종료 후 성공 여부와 힌트 사용 횟수 기록
* 남은 시간과 직원 메모를 남겨 고객 응대 및 운영 개선에 활용
* 전체 성공률, 평균 힌트 사용 횟수, 테마별 예약 수와 성공률 등 운영 통계 확인

따라서 본 프로젝트의 목적은 **예약 관리 기능에 플레이 결과 및 통계 기능을 결합하여 방탈출 카페 운영 흐름을 하나의 데스크톱 프로그램으로 관리하는 것**입니다.

---

## 4. 주요 기능

### 4.1 고객 관리

* 고객 추가
* 고객 삭제
* 전체 고객 조회
* 이메일 기반 고객 식별

### 4.2 카페 지점 관리

* 지점 추가
* 지점 삭제
* 전체 지점 조회
* 지점별 테마 연결

### 4.3 방탈출 테마 관리

* 테마 추가
* 테마 삭제
* 전체 테마 조회
* `Room` 모델을 방탈출 테마로 해석하여 사용

### 4.4 예약 관리

* 예약 생성
* 예약 삭제
* 예약 ID로 예약 조회
* 고객 이메일로 예약 조회
* 예약 시간 중복 검증
* 날짜 및 시간 형식 검증

### 4.5 플레이 결과 관리

* 예약 ID(`bookingId`)를 기준으로 플레이 결과 등록
* 성공 여부 저장
* 힌트 사용 횟수 저장
* 남은 시간 저장
* 직원 메모 저장
* 전체 플레이 결과 목록 조회
* 입력값 검증
  * 힌트 사용 횟수는 0 이상
  * 남은 시간은 0 이상
  * 예약 ID는 기존 예약과 연결되어야 함

### 4.6 운영 통계

Java Stream API를 활용하여 다음 통계를 계산합니다.

* 전체 예약 수
* 완료된 플레이 수
* 전체 성공률
* 평균 힌트 사용 횟수
* 테마별 예약 수
* 테마별 성공률

데이터가 없는 경우에도 예외가 발생하지 않도록 0 또는 안내 문구를 출력합니다.

### 4.7 데이터 저장 및 불러오기

File I/O와 객체 직렬화를 사용하여 다음 데이터를 파일로 저장하고 다시 불러올 수 있습니다.

* 고객 데이터
* 지점 데이터
* 테마 데이터
* 예약 데이터
* 플레이 결과 데이터

---

## 5. 사용 기술

| 기술 | 사용 내용 |
| --- | --- |
| Java | 전체 애플리케이션 구현 언어 |
| Java Swing | GUI 화면 구성 |
| MVC Pattern | Model, View, Controller 계층 분리 |
| Java Stream API | 운영 통계 계산, 평균/그룹별 집계 처리 |
| File I/O | 데이터 저장 및 불러오기 |
| Serialization | Map 기반 데이터 객체 파일 저장 |
| Enum 개념 | 난이도 `EASY`, `NORMAL`, `HARD`처럼 고정된 선택값을 표현하는 설계 개념에 활용 가능하도록 도메인 구성 |
| Exception Handling | 잘못된 입력, 중복 예약, 빈 데이터 조회 등 예외 상황 처리 |
| Collection Framework | `Map`, `HashMap`을 이용한 데이터 관리 |
| JUnit 5 | 기존 테스트 코드 구조 포함 |
| Git | 버전 관리 |

---

## 6. 프로젝트 구조

```text
.
├── README.md
├── LICENSE
├── University-Room-Booking-Application-using-Java-Swing-and-TDD-Approach.iml
└── src/
    ├── RBSystem.java
    ├── controller/
    │   └── BookingResource.java
    ├── model/
    │   ├── Building/
    │   ├── PlayResult/
    │   ├── Reservation/
    │   ├── Room/
    │   └── User/
    ├── service/
    │   ├── BuildingService.java
    │   ├── PlayResultService.java
    │   ├── ReservationService.java
    │   ├── RoomService.java
    │   ├── StatisticsService.java
    │   └── UserService.java
    ├── view/
    │   ├── CLI/
    │   │   └── CommandLine.java
    │   └── GUI/
    │       ├── BUILDINGS/
    │       ├── ROOMS/
    │       ├── Reservation/
    │       ├── USER/
    │       ├── Main_GUI.java
    │       └── StatisticsGUI.java
    └── Tests/
```

### 주요 파일 설명

* `src/RBSystem.java`  
  프로그램의 메인 실행 파일입니다. 서비스, 컨트롤러, GUI/CLI 실행 흐름을 초기화합니다.

* `src/controller/BookingResource.java`  
  View에서 발생한 요청을 Service 계층으로 전달하는 Controller 역할을 합니다.

* `src/model/PlayResult/PlayResult.java`  
  플레이 결과 데이터 모델입니다. 예약 ID, 성공 여부, 힌트 수, 남은 시간, 직원 메모를 저장합니다.

* `src/service/StatisticsService.java`  
  예약 및 플레이 결과 데이터를 기반으로 운영 통계를 계산합니다.

* `src/view/GUI/StatisticsGUI.java`  
  Swing 기반 통계 조회 화면입니다.

---

## 7. 실행 방법

### 7.1 실행 환경

* JDK 17 이상 권장
* IntelliJ IDEA 또는 Java 컴파일/실행이 가능한 터미널 환경

### 7.2 터미널에서 실행

프로젝트 루트 디렉터리에서 아래 명령어를 실행합니다.

```bash
mkdir -p out
find src -name '*.java' ! -path 'src/Tests/*' -print0 | xargs -0 javac -encoding UTF-8 -d out
```

GUI 모드 실행:

```bash
java -cp out RBSystem --gui
```

CLI 모드 실행:

```bash
java -cp out RBSystem --cli
```

그래픽 환경이 없는 경우에는 자동으로 CLI 모드로 실행됩니다.

### 7.3 IntelliJ IDEA에서 실행

1. IntelliJ IDEA에서 프로젝트 폴더를 엽니다.
2. Project SDK를 JDK 17 이상으로 설정합니다.
3. `src` 폴더가 Sources Root로 설정되어 있는지 확인합니다.
4. `src/RBSystem.java` 파일을 엽니다.
5. `main` 메서드 옆 실행 버튼을 누릅니다.
6. 필요한 경우 Program arguments에 아래 값을 입력합니다.
   * `--gui`: Swing GUI 실행
   * `--cli`: 터미널 CLI 실행

---

## 8. 주요 화면 캡처 위치 안내

과제 제출용 보고서 또는 발표 자료에는 아래 화면을 캡처해 포함하면 좋습니다.

| 캡처 항목 | 실행/접근 위치 |
| --- | --- |
| 메인 화면 | 프로그램 실행 후 첫 Swing 화면 |
| 고객 관리 화면 | Main → `CUSTOMERS` |
| 카페 지점 관리 화면 | Main → `CAFES / BRANCHES` |
| 테마 관리 화면 | Main → `ESCAPE THEMES` |
| 예약 생성 화면 | Main → `RESERVATIONS` → `BOOK A THEME` |
| 플레이 결과 입력 화면 | Main → `RESERVATIONS` → `ADD PLAY RESULT` |
| 플레이 결과 목록 화면 | Main → `RESERVATIONS` → `VIEW PLAY RESULTS` |
| 운영 통계 화면 | Main → `OPERATION STATISTICS` |
| CLI 실행 화면 | `java -cp out RBSystem --cli` 실행 결과 |

권장 캡처 저장 위치:

```text
docs/screenshots/
```

예시 파일명:

```text
docs/screenshots/01_main.png
docs/screenshots/02_reservation.png
docs/screenshots/03_play_result.png
docs/screenshots/04_statistics.png
```

현재 저장소에는 스크린샷 파일이 기본 포함되어 있지 않으므로, 제출 전 로컬 환경에서 실행 후 직접 캡처해 위 경로에 추가하면 됩니다.

---

## 9. 발표 영상에서 설명할 핵심 포인트

발표 영상에서는 아래 순서로 설명하면 프로젝트의 목적과 구현 내용을 자연스럽게 전달할 수 있습니다.

1. **프로젝트 주제 소개**  
   방탈출 카페 예약 및 힌트/결과 관리 프로그램이라는 주제를 소개합니다.

2. **기존 구조를 유지한 이유**  
   기존 Room Booking 구조를 크게 리팩터링하지 않고 `Room = 테마`, `Building = 지점`, `User = 고객`으로 재해석해 안정성을 유지한 점을 설명합니다.

3. **MVC 구조 설명**  
   Model, Service, Controller, View가 어떻게 분리되어 있는지 설명합니다.

4. **예약 기능 시연**  
   고객, 지점, 테마를 확인하고 예약을 생성하는 흐름을 보여줍니다.

5. **플레이 결과 관리 시연**  
   예약 ID를 선택/입력해 성공 여부, 힌트 수, 남은 시간, 직원 메모를 저장하는 기능을 보여줍니다.

6. **운영 통계 시연**  
   전체 예약 수, 완료된 플레이 수, 성공률, 평균 힌트 사용 횟수, 테마별 통계를 보여줍니다.

7. **예외 처리 설명**  
   빈 입력, 잘못된 날짜/시간, 음수 힌트 수, 음수 남은 시간, 중복 예약 등을 어떻게 처리했는지 설명합니다.

8. **File I/O 저장/불러오기 설명**  
   프로그램 데이터를 파일로 저장하고 다시 불러오는 흐름을 설명합니다.

9. **확장 가능성**  
   향후 관리자 로그인, 실제 DB 연동, 테마 난이도 Enum 정식 적용, 매출 통계, 고객별 재방문 분석 등을 추가할 수 있음을 설명합니다.

---

## 10. 참고한 오픈소스 출처와 변경 사항

### 원본 프로젝트 / 참고 출처

* 원본 성격: Java Swing 기반 Room Booking System 프로젝트
* 저장소 내 라이선스: `LICENSE` 파일 참고
* 주요 기반 구조: MVC 형태의 `model`, `service`, `controller`, `view` 계층 구조

### 본 프로젝트에서 변경한 사항

기존 Room Booking System을 그대로 제출하지 않고, 방탈출 카페 운영 프로그램으로 확장하기 위해 다음과 같은 변경을 수행했습니다.

* 프로젝트 주제를 **방탈출 카페 예약 및 힌트 관리 프로그램**으로 변경
* 화면 문구를 방탈출 카페 도메인에 맞게 수정
  * `Room` → Escape Theme
  * `Building` → Cafe Branch
  * `User` → Customer
* 기본 방탈출 테마 3개 추가
* 실행 모드 개선
  * `--gui` GUI 실행
  * `--cli` CLI 실행
  * Headless 환경에서 CLI 자동 실행
* 플레이 결과 관리 기능 추가
  * 성공 여부
  * 힌트 사용 횟수
  * 남은 시간
  * 직원 메모
* 운영 통계 기능 추가
  * 전체 예약 수
  * 완료된 플레이 수
  * 전체 성공률
  * 평균 힌트 사용 횟수
  * 테마별 예약 수
  * 테마별 성공률
* README를 기말 프로젝트 제출 형식으로 재작성

---

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
