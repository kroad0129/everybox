
# 🎁 Everybox - 모두의 냉장고 (모냉)

**Everybox**는 대학생 간 나눔을 위한 실시간 채팅 기반 식품 공유 플랫폼입니다.  
학교 인증을 거친 사용자들만 참여할 수 있는 안전한 커뮤니티를 기반으로, 남는 음식이나 물품을 게시하고 채팅을 통해 실시간으로 소통 및 교환할 수 있습니다.

---

## 소개

**Everybox**는 대학생 간 나눔을 위한 실시간 채팅 기반 식품 공유 플랫폼입니다.  
학교 이메일(@ac.kr) 인증을 거친 사용자만 가입할 수 있어, 안전하고 신뢰할 수 있는 커뮤니티를 제공합니다.

- 🌱 **생활비 절감 & 음식물 쓰레기 감소**: 남는 식자재/물품을 쉽고 빠르게 필요한 친구와 공유
- 💬 **실시간 채팅**: 게시글 단위 1:1 채팅으로 빠른 소통 및 거래  
- 📍 **카카오맵 연동**: 교내·주변 위치 기반 나눔

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
## 주요 기능

- 🛡 **학교 인증 기반 커뮤니티**: @ac.kr 이메일 인증, 실명 기반
- 💬 **1:1 실시간 채팅**: 게시글 당 채팅방 자동 생성
- 🪧 **게시글 등록/수정/삭제**: 자유롭게 제목, 내용, 거래 위치 등 입력
- 📍 **카카오맵 위치 선택**: 거래 장소를 직관적으로 지정
- 📨 **학교 이메일 인증/메일 발송**: 회원가입 인증
- 🔐 **JWT 기반 인증/인가**
- 🧑‍💻 **카카오 OAuth2 로그인**
- 📝 **Swagger API 문서 자동화**
- 🛠 **글로벌 예외 처리 및 표준화된 응답**

---
## 스크린샷
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
## 배운 점 & 느낀 점
- 실시간 채팅(WebSocket) 구현을 통해 비동기 처리 및 메시지 브로드캐스팅 원리 습득

- JWT 기반 인증과 Spring Security를 직접 설정하며 백엔드 보안에 대한 이해도 향상

- RESTful API와 Swagger 문서화를 경험, 협업과 유지보수의 중요성 체감

- GitHub 협업(PR, 이슈, 코드리뷰)으로 실무형 개발 프로세스를 연습

- 사용자 피드백을 반영해 UX 개선 및 예외처리 표준화 경험
---

## 📬 팀 정보

성장톤 1팀 - 모두의 냉장고(모냉)
- 🛠 백엔드: 김태희, 성유진

GitHub 협업 링크: [https://github.com/growthon2025-team1](https://github.com/growthon2025-team1)

---
