# 삭제 또는 정리 후보 파일 목록

이 문서는 발표 준비 중 확인한 **정리 후보**를 기록하기 위한 문서입니다. 실행에 필요한 파일을 임의로 삭제하지 않기 위해 실제 삭제 대신 후보만 정리했습니다.

## 정리 후보

| 경로 | 정리 후보로 본 이유 | 현재 조치 |
| --- | --- | --- |
| `src/Tests/` | JUnit 5 테스트 코드가 포함되어 있지만, 터미널 기본 실행 명령에서는 JUnit 의존성이 포함되어 있지 않아 컴파일 대상에서 제외됩니다. | 삭제하지 않음. IDE 또는 CI에서 JUnit을 추가하면 테스트 참고 자료로 사용할 수 있습니다. |
| `*.form` 파일들 | IntelliJ GUI Designer용 폼 파일입니다. 일부 화면은 Java 코드에서 직접 레이아웃을 구성하므로 폼 파일과 표시 문구가 완전히 일치하지 않을 수 있습니다. | 삭제하지 않음. IntelliJ 프로젝트 호환성을 위해 유지합니다. |
| `learn.md` | 프로젝트 실행에는 직접 필요하지 않은 학습/메모성 문서로 보입니다. | 삭제하지 않음. 원본 참고 자료일 수 있어 유지합니다. |
| `University-Room-Booking-Application-using-Java-Swing-and-TDD-Approach.iml` | IntelliJ 모듈 설정 파일입니다. 터미널 실행에는 필수는 아니지만 IntelliJ 실행에는 도움이 됩니다. | 삭제하지 않음. 제출자가 IntelliJ로 열 수 있도록 유지합니다. |

## 유지해야 하는 주요 실행 파일

아래 파일들은 프로그램 실행과 발표 시연에 필요하므로 삭제하면 안 됩니다.

* `src/RBSystem.java`
* `src/controller/BookingResource.java`
* `src/model/**`
* `src/service/**`
* `src/view/GUI/**`
* `src/view/CLI/CommandLine.java`
* `README.md`
