
# 🎁 Everybox - 모두의 냉장고 (모냉)

**Everybox**는 대학생 간 나눔을 위한 실시간 채팅 기반 식품 공유 플랫폼입니다.  
학교 인증을 거친 사용자들만 참여할 수 있는 안전한 커뮤니티를 기반으로, 남는 음식이나 물품을 게시하고 채팅을 통해 실시간으로 소통 및 교환할 수 있습니다.

---

## 🎯 프로젝트 목적

- 🌱 **대학생 생활비 절감**: 불필요한 음식 낭비를 줄이고 남는 식자재를 필요한 사람과 나눔
- 🛡 **학교 인증 기반의 안전한 커뮤니티**: @ac.kr 이메일 인증을 통한 실명 기반 접근
- 💬 **1:1 실시간 채팅 기능**: 게시글 당 채팅방이 자동 생성되어 자유로운 소통 가능
- 📍 **카카오맵 연동**: 교내 또는 인근 지역 중심의 물품 위치 확인
- 🪧 **자유로운 게시글 등록과 수정**: 필요한 식품, 수량, 거래장소 등을 자유롭게 입력

---

## 🛠 기술 스택

| 분야        | 기술 |
|-------------|------|
| Language    | Java 17 |
| Framework   | Spring Boot 3.x |
| ORM         | Spring Data JPA |
| 보안       | Spring Security, JWT |
| 문서화      | Springdoc OpenAPI (Swagger UI) |
| DB          | MySQL |
| WebSocket   | STOMP + SockJS |
| Mail        | JavaMailSender |
| OAuth2      | Kakao Login |
| 빌드 도구   | Gradle |
| 기타        | Lombok, Validation, DevTools |

---

## 📁 프로젝트 구조

```
src/main/java/com/everybox/everybox
├── config/           # 보안, Swagger, WebSocket 설정
├── controller/       # 사용자, 인증, 게시글, 채팅 API
├── domain/           # JPA Entity 클래스 (User, Post, Message 등)
├── dto/              # 요청/응답 DTO 모음
├── docs/             # Swagger용 API 문서 작성 클래스
├── exception/        # 글로벌 예외 처리 및 응답 구조
├── jwt/              # JWT 생성/검증/필터 등
├── repository/       # JPA Repository 인터페이스
├── service/          # 비즈니스 로직 (회원가입, 채팅, 게시글 등)
└── EveryboxApplication.java
```

---

## ✅ 구현 기능 현황

- [x] 회원가입/로그인 (JWT + 학교 이메일 인증)
- [x] 카카오 OAuth2 로그인
- [x] 게시글 등록, 조회, 수정
- [x] WebSocket 기반 채팅 기능
- [x] 카카오맵 기반 위치 선택
- [x] Swagger API 문서
- [x] 예외 핸들링 및 표준화된 응답

---

## 🧪 실행 방법

1. `application.yml` 설정

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/template_db
    username: root
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
  mail:
    host: smtp.naver.com
    username: your_email@naver.com
    password: your_app_password
    port: 465
```

2. 실행

```bash
./gradlew bootRun
```

---

## 📄 API 문서 (Swagger)

- http://localhost:8080/swagger-ui/index.html

---

## 🤝 기여 방법

1. Fork → 기능 개발 → Pull Request
2. 커밋 메시지 컨벤션 및 브랜치 전략을 프로젝트 규칙에 따를 것

---

## 📬 팀 정보

성장톤 1팀 - 모두의 냉장고(모냉)
- 👨‍💻 기획: 김재형
- 🎨 디자인: 이다솔
- 💻 프론트엔드: 김다인, 차예린
- 🛠 백엔드: 김태희, 성유진

GitHub 협업 링크: [https://github.com/growthon2025-team1](https://github.com/growthon2025-team1)

---
