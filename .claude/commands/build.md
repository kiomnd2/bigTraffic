# 빌드/실행/Docker 명령어

## Gradle 빌드
```bash
./gradlew build              # 전체 빌드
./gradlew build -x test      # 테스트 제외 빌드
./gradlew clean build         # 클린 빌드
```

## 테스트
```bash
./gradlew test                                              # 전체 테스트
./gradlew test --tests "kr.kiomn2.bigtraffic.{테스트클래스}"  # 특정 클래스
./gradlew test --info                                        # 상세 출력
```

## 애플리케이션 실행
```bash
./gradlew bootRun                                           # 기본 실행
./gradlew bootRun --args='--spring.profiles.active=dev'     # dev 프로필
```

## Docker (MySQL 5.7)
```bash
docker-compose -f docker/docker-compose.yml up -d    # DB 시작
docker-compose -f docker/docker-compose.yml down      # DB 중지
docker-compose -f docker/docker-compose.yml logs -f   # 로그 확인
```

## DB 접속 정보
- Host: localhost:3306
- Database: kiomnd2-db
- User: kiomnd2
- Charset: utf8mb4 / utf8mb4_unicode_ci
