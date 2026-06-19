# Escape Room Manager

## 1. 프로젝트명

**Escape Room Manager - 방탈출 카페 예약 및 힌트 관리 프로그램**

Java Swing을 사용해 방탈출 카페의 고객, 지점, 테마, 예약, 플레이 결과, 운영 통계를 관리하는 데스크톱 애플리케이션입니다.

---

## 2. 프로젝트 소개

이 프로젝트는 기존 Java Swing 기반 Room Booking System을 바탕으로, **방탈출 카페 운영에 맞게 기능과 화면 문구를 확장한 고급자바 프로그래밍 기말 프로젝트**입니다.

기존 구조를 크게 바꾸지 않고 안정성을 유지하기 위해 내부 클래스명 일부는 원래 구조를 유지했습니다. 대신 프로그램 화면과 README에서는 아래와 같이 방탈출 카페 도메인으로 해석합니다.

| 기존 구조 | 본 프로젝트에서의 의미 |
| --- | --- |
| `User` | 고객 또는 관리자 |
| `Building` | 방탈출 카페 지점 |
| `Room` | 방탈출 테마 |
| `Reservation` | 테마 예약 |
| `ReservationStatus` | 예약 상태 Enum |
| `PlayResult` | 플레이 결과 기록 |
| `BookingResource` | View와 Service를 연결하는 Controller |

프로그램 실행 시 발표 시연을 쉽게 할 수 있도록 아래 기본 테마가 자동 등록됩니다.

| 테마명 | 장르 | 난이도 | 소요 시간 | 권장 인원 |
| --- | --- | --- | --- | --- |
| 저택의 비밀 | 공포 | HARD | 60분 | 2~4명 |
| 연구소 탈출 | SF | NORMAL | 60분 | 2~5명 |
| 사라진 탐정 | 추리 | EASY | 50분 | 2~4명 |

---

## 3. 개발 목적

방탈출 카페 운영에는 단순 예약 등록뿐 아니라 다음과 같은 관리 기능이 필요합니다.

* 고객이 어떤 테마를 언제 예약했는지 관리
* 테마별 예약 현황 확인
* 플레이 종료 후 성공 여부와 힌트 사용 횟수 기록
* 남은 시간과 직원 메모를 저장해 운영 품질 개선에 활용
* 전체 성공률, 평균 힌트 사용 횟수, 테마별 성공률 등 운영 통계 확인

따라서 본 프로젝트의 목적은 **방탈출 카페 예약과 힌트/플레이 결과 관리를 하나의 Java Swing 프로그램으로 구현하고, MVC 구조와 Stream API, File I/O, Enum, 예외 처리 등 고급 Java 기법을 적용하는 것**입니다.

---

## 4. 주요 기능

### 4.1 고객 관리

* 고객 추가
* 고객 삭제
* 전체 고객 조회
* 이메일 기반 고객 식별

### 4.2 카페 지점 관리

* 카페 지점 추가
* 카페 지점 삭제
* 전체 지점 조회

### 4.3 방탈출 테마 관리

* 테마 추가
* 테마 삭제
* 전체 테마 조회
* 기존 `Room` 모델을 방탈출 테마로 해석하여 사용

### 4.4 예약 관리

* 테마 예약 생성
* 예약 삭제
* 예약 ID로 예약 조회
* 고객 이메일로 예약 조회
* 예약 날짜 및 시간 형식 검증
* 예약 시간 중복 검증
* `ReservationStatus` Enum을 통해 예약 상태 표현

### 4.5 플레이 결과 및 힌트 관리

* 예약 ID(`bookingId`) 기준으로 플레이 결과 등록
* 성공 여부 저장
* 힌트 사용 횟수 저장
* 남은 시간 저장
* 직원 메모 저장
* 전체 플레이 결과 목록 조회
* 입력값 검증
  * 힌트 수는 0 이상
  * 남은 시간은 0 이상
  * 예약 ID는 기존 예약과 연결되어야 함

### 4.6 운영 통계

`StatisticsService`에서 Java Stream API를 사용해 다음 통계를 계산합니다.

* 전체 예약 수
* 완료된 플레이 수
* 전체 성공률
* 평균 힌트 사용 횟수
* 테마별 예약 수
* 테마별 성공률

예약 또는 플레이 결과 데이터가 없어도 예외가 발생하지 않도록 0 또는 안내 문구를 출력합니다.

### 4.7 데이터 저장 및 불러오기

File I/O와 객체 직렬화를 사용해 다음 데이터를 파일로 저장하고 다시 불러옵니다.

* 고객 데이터
* 카페 지점 데이터
* 테마 데이터
* 예약 데이터
* 플레이 결과 데이터

---

## 5. 사용 기술

| 기술 | 적용 내용 |
| --- | --- |
| Java | 전체 프로그램 구현 언어 |
| Swing | GUI 화면 구현 |
| MVC 구조 | Model, View, Controller, Service 계층 분리 |
| 객체지향 설계 | 도메인별 Model과 Service 클래스로 책임 분리 |
| Collection | `Map`, `HashMap`을 사용해 고객, 지점, 테마, 예약, 결과 데이터 관리 |
| Enum | `ReservationStatus`로 예약 상태 관리 |
| Stream API | 성공률, 평균 힌트 수, 테마별 예약 수와 성공률 계산 |
| File I/O | 파일 저장 및 불러오기 기능 구현 |
| Serialization | 객체 Map 데이터를 파일로 저장 |
| Exception Handling | 빈 입력, 잘못된 날짜/시간, 중복 예약, 음수 힌트 수 등 방지 |
| JUnit 5 | 기존 테스트 코드 구조 포함 |
| Git | 버전 관리 |

---

## 6. 프로젝트 구조

```text
.
├── README.md
├── LICENSE
├── docs/
│   └── CLEANUP_CANDIDATES.md
├── src/
│   ├── RBSystem.java
│   ├── controller/
│   │   └── BookingResource.java
│   ├── model/
│   │   ├── Building/
│   │   ├── PlayResult/
│   │   ├── Reservation/
│   │   │   ├── Reservation.java
│   │   │   └── ReservationStatus.java
│   │   ├── Room/
│   │   └── User/
│   ├── service/
│   │   ├── BuildingService.java
│   │   ├── PlayResultService.java
│   │   ├── ReservationService.java
│   │   ├── RoomService.java
│   │   ├── StatisticsService.java
│   │   └── UserService.java
│   ├── view/
│   │   ├── CLI/
│   │   │   └── CommandLine.java
│   │   └── GUI/
│   │       ├── BUILDINGS/
│   │       ├── ROOMS/
│   │       ├── Reservation/
│   │       ├── USER/
│   │       ├── Main_GUI.java
│   │       └── StatisticsGUI.java
│   └── Tests/
└── University-Room-Booking-Application-using-Java-Swing-and-TDD-Approach.iml
```

### 핵심 파일

* `src/RBSystem.java`  
  프로그램 시작점입니다. Service 객체를 만들고 Controller를 연결한 뒤 GUI 또는 CLI를 실행합니다.

* `src/controller/BookingResource.java`  
  GUI/CLI에서 들어온 요청을 Service 계층으로 전달하는 Controller입니다.

* `src/model/Reservation/Reservation.java`  
  예약 정보를 저장하는 Model입니다.

* `src/model/Reservation/ReservationStatus.java`  
  예약 상태를 나타내는 Enum입니다.

* `src/model/PlayResult/PlayResult.java`  
  플레이 결과 정보를 저장하는 Model입니다.

* `src/service/StatisticsService.java`  
  Stream API로 운영 통계를 계산하는 Service입니다.

* `src/view/GUI/StatisticsGUI.java`  
  통계 결과를 보여주는 Swing 화면입니다.

---

## 7. 실행 방법

### 7.1 준비 사항

초보자도 실행할 수 있도록 터미널 기준으로 설명합니다.

1. JDK 17 이상을 설치합니다.
2. 터미널 또는 IntelliJ IDEA를 준비합니다.
3. 프로젝트 루트 폴더로 이동합니다.

현재 위치가 프로젝트 루트인지 확인하려면 아래 파일들이 보여야 합니다.

```text
README.md
src/
LICENSE
```

### 7.2 터미널에서 컴파일

프로젝트 루트에서 아래 명령어를 실행합니다.

```bash
mkdir -p out
find src -name '*.java' ! -path 'src/Tests/*' -print0 | xargs -0 javac -encoding UTF-8 -d out
```

설명:

* `out` 폴더에 컴파일 결과를 저장합니다.
* `src/Tests`는 JUnit 의존성이 필요하므로 기본 실행용 컴파일에서는 제외합니다.
* 한글 테마명이 포함되어 있으므로 `-encoding UTF-8` 옵션을 사용합니다.

### 7.3 GUI 실행

```bash
java -cp out RBSystem --gui
```

GUI 환경이 있는 PC에서는 Swing 화면이 실행됩니다.

### 7.4 CLI 실행

```bash
java -cp out RBSystem --cli
```

터미널 기반 메뉴가 실행됩니다.

### 7.5 IntelliJ IDEA에서 실행

1. IntelliJ IDEA에서 프로젝트 폴더를 엽니다.
2. Project SDK를 JDK 17 이상으로 설정합니다.
3. `src` 폴더를 Sources Root로 설정합니다.
4. `src/RBSystem.java` 파일을 엽니다.
5. `main` 메서드 옆 실행 버튼을 클릭합니다.
6. Program arguments에 필요에 따라 아래 값을 입력합니다.
   * `--gui`: Swing GUI 실행
   * `--cli`: CLI 실행

그래픽 환경이 없는 경우에는 자동으로 CLI 모드로 실행됩니다.

---

## 8. 주요 화면 캡처

제출 자료 또는 발표 자료에는 아래 화면을 캡처하면 좋습니다.

| 캡처 화면 | 접근 방법 |
| --- | --- |
| 메인 화면 | 프로그램 실행 후 첫 화면 |
| 고객 관리 | Main → `1. Manage Customers` |
| 카페 지점 관리 | Main → `2. Manage Cafe Branches` |
| 테마 관리 | Main → `3. Manage Escape Themes` |
| 예약/플레이 결과 메뉴 | Main → `4. Reservations & Play Results` |
| 예약 생성 화면 | Reservations → `1. Book Escape Theme` |
| 플레이 결과 입력 화면 | Reservations → `3. Add Play Result` |
| 플레이 결과 목록 화면 | Reservations → `4. View Play Results` |
| 운영 통계 화면 | Main → `10. Operation Statistics` |
| CLI 화면 | `java -cp out RBSystem --cli` |

권장 저장 위치:

```text
docs/screenshots/
```

예시 파일명:

```text
docs/screenshots/01_main.png
docs/screenshots/02_theme_reservation.png
docs/screenshots/03_play_result.png
docs/screenshots/04_statistics.png
```

현재 저장소에는 스크린샷 이미지가 포함되어 있지 않습니다. 제출 전 로컬 PC에서 실행 후 직접 캡처하면 됩니다.

---

## 9. 주요 코드 설명

### 9.1 실행 흐름 - `RBSystem`

`RBSystem`은 프로그램 시작점입니다.

1. `UserService`, `RoomService`, `ReservationService`, `BuildingService` 생성
2. `BookingResource` Controller 생성
3. 기본 방탈출 테마 등록
4. 실행 인자에 따라 GUI 또는 CLI 실행
5. Headless 환경이면 CLI로 자동 전환

### 9.2 Controller - `BookingResource`

`BookingResource`는 View와 Service를 연결합니다.

예시 흐름:

```text
Swing 화면 입력
→ BookingResource 메서드 호출
→ Service에서 검증 및 저장
→ Model 데이터 변경
```

예약, 플레이 결과, 통계 기능 모두 이 Controller를 통해 접근합니다.

### 9.3 예약 Model - `Reservation`

예약 ID, 고객 이메일, 지점명, 테마명, 예약 날짜와 시간을 저장합니다.  
`ReservationStatus` Enum을 사용해 예약 상태도 함께 표현합니다.

### 9.4 플레이 결과 Model - `PlayResult`

플레이 결과는 예약 ID와 연결됩니다.

저장 정보:

* 성공 여부
* 힌트 사용 횟수
* 남은 시간
* 직원 메모

### 9.5 통계 Service - `StatisticsService`

`StatisticsService`는 예약과 플레이 결과 데이터를 받아 Stream API로 통계를 계산합니다.

계산 항목:

* 전체 예약 수
* 완료된 플레이 수
* 전체 성공률
* 평균 힌트 사용 횟수
* 테마별 예약 수
* 테마별 성공률

---

## 10. 고급 Java 기법 적용 내용

### 10.1 MVC 구조로 화면과 로직 분리

프로젝트는 MVC 구조를 기반으로 합니다.

| 계층 | 역할 | 예시 |
| --- | --- | --- |
| Model | 데이터 표현 | `Reservation`, `PlayResult`, `User`, `Room` |
| View | GUI/CLI 화면 | `Main_GUI`, `AddReservation`, `CommandLine` |
| Controller | View와 Service 연결 | `BookingResource` |
| Service | 검증, 저장, 통계 계산 | `ReservationService`, `PlayResultService`, `StatisticsService` |

화면 코드는 직접 데이터를 저장하지 않고 Controller를 통해 Service에 요청합니다. 이를 통해 화면과 비즈니스 로직의 책임을 분리했습니다.

### 10.2 Enum으로 예약 상태 관리

`ReservationStatus` Enum을 추가하여 예약 상태를 표현했습니다.

```java
public enum ReservationStatus {
    RESERVED,
    CANCELLED
}
```

현재 예약 생성 시 `isBooked` 값에 따라 `RESERVED` 또는 `CANCELLED` 상태가 설정됩니다. 향후 `COMPLETED`, `NO_SHOW` 등의 상태를 추가해 확장할 수 있습니다.

### 10.3 Stream API로 성공률과 평균 힌트 수 계산

`StatisticsService`에서 Stream API를 사용합니다.

활용 예시:

* `mapToInt(PlayResult::getHintCount).average()`로 평균 힌트 수 계산
* `Collectors.groupingBy(Reservation::getRoom, Collectors.counting())`으로 테마별 예약 수 계산
* 성공 여부를 `1` 또는 `0`으로 변환한 뒤 평균을 내 성공률 계산

데이터가 없는 경우 `orElse(0.0)`을 사용해 예외 없이 0으로 표시합니다.

### 10.4 파일 입출력으로 데이터 저장/불러오기

각 Service 클래스는 File I/O와 Object Serialization을 사용합니다.

저장되는 파일 예시:

* `userData.txt`
* `buildingData.txt`
* `roomData.txt`
* `resData.txt`
* `playResultData.txt`

프로그램에서 `Save Data`를 누르면 현재 데이터를 파일로 저장하고, `Load Data`를 누르면 다시 불러옵니다.

### 10.5 예외 처리로 잘못된 입력 방지

다음 상황에서 예외 처리를 사용합니다.

* 빈 입력값
* 중복 예약 ID
* 존재하지 않는 고객/지점/테마
* 잘못된 날짜 형식
* 잘못된 시간 형식
* 종료 시간이 시작 시간보다 빠른 경우
* 힌트 사용 횟수가 음수인 경우
* 남은 시간이 음수인 경우

GUI에서는 `JOptionPane`으로 오류 메시지를 표시합니다.

---

## 11. 참고한 오픈소스 출처

### 원본 프로젝트

* 원본 성격: Java Swing 기반 Room Booking System 프로젝트
* 저장소 내 라이선스: `LICENSE` 파일 참고
* 원본 구조: MVC 형태의 `model`, `service`, `controller`, `view` 계층 구조

### 본 프로젝트에서 변경한 사항

기존 Room Booking System을 방탈출 카페 프로그램으로 확장하기 위해 다음 변경을 수행했습니다.

* 프로젝트 주제를 방탈출 카페 예약 및 힌트 관리로 변경
* 화면 문구 변경
  * `User` → Customer
  * `Building` → Cafe Branch
  * `Room` → Escape Theme
* 기본 방탈출 테마 3개 추가
* `ReservationStatus` Enum 추가
* `PlayResult` 모델 및 서비스 추가
* 플레이 결과 입력/조회 Swing 화면 추가
* 운영 통계 서비스 및 Swing 화면 추가
* Java Stream API 기반 통계 계산 추가
* File I/O 저장/불러오기 대상에 플레이 결과 추가
* README를 고급자바 프로그래밍 기말 프로젝트 제출 형식으로 재작성

---

## 12. 향후 개선 사항

추후 개선할 수 있는 부분은 다음과 같습니다.

* 실제 데이터베이스 연동
* 관리자 로그인과 권한 분리
* 예약 상태 Enum 확장
  * `COMPLETED`
  * `NO_SHOW`
  * `CANCELLED_BY_CUSTOMER`
* 테마 장르와 난이도를 별도 Enum으로 분리
* 날짜/시간 입력을 텍스트 필드가 아닌 선택 UI로 개선
* 고객별 방문 이력 분석
* 매출 관리 기능 추가
* 스크린샷과 발표 자료 자동 생성용 `docs/screenshots` 폴더 구성
* JUnit 테스트 실행 환경을 README에 더 자세히 추가

---

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
