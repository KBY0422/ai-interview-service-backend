<h1 align="center">AI Interview Service - Backend</h1>

<p align="center">
  <b>Dashboard-Oriented Backend Architecture</b><br/>
  대시보드 중심 서비스 운영을 위한 집계 · 통계 · 권한 설계
</p>

---

## 📌 Project Overview

AI Interview Service의 백엔드 서버입니다.

본 백엔드는 단순 CRUD 제공이 아닌,  
**대시보드에서 ‘의미 있는 판단이 가능한 지표’를 제공하는 것**을 핵심 목표로 설계되었습니다.

프론트엔드에서 설계한  
**사용자 성장 지표 / 운영 · 매출 지표**를  
안정적으로 제공하기 위한 **집계 중심 비즈니스 로직**에 집중했습니다.

---

## 📊 Dashboard-Oriented Backend Design (Core Focus)

### Why Dashboard First?

이 서비스의 핵심은  
데이터를 나열하는 것이 아니라,  
**운영 상태와 학습 흐름을 판단할 수 있게 만드는 것**이라고 판단했습니다.

따라서 백엔드에서는  
일반적인 Entity 조회 API가 아닌,  
**대시보드 전용 집계 API 중심 구조**로 설계했습니다.

---

## 📂 Dashboard Domains

대시보드 성격에 따라 통계 도메인을 명확히 분리했습니다.

- **User Dashboard**
    - 면접 점수 추이
    - 사용자 성장 흐름 판단

- **Admin Dashboard**
    - 전체 서비스 운영 현황 요약

- **Statistics Dashboard**
    - 면접 · 매출 · 사용자 통계 분석

---

## 📈 Data Aggregation Strategy

### Aggregation Approach

- 기간 단위 집계: 일 / 7일 / 월
- 도메인별 통계 분리: 사용자 / 면접 / 매출
- MyBatis 기반 **통계 전용 SQL 및 Mapper 구성**

단순 CRUD 조회가 아닌,  
**통계 목적에 맞는 쿼리와 응답 구조**를 설계했습니다.

---

## 💰 Revenue & Cost Modeling

가상의 서비스 수익 모델을 적용하여  
매출과 비용을 함께 고려한 통계 로직을 구현했습니다.

### Revenue Model
- 면접 1회당: 800원
- 이력서 분석 1회당: 300원
- 챗봇: 부가 서비스

### Cost Model
- GPT 사용 비용: 1000 토큰당 1원

➡️ 매출 / 비용 / 순수익을 함께 계산해  
대시보드 통계 API로 제공했습니다.

---

## 🧩 Service Layer & Response Design

대시보드 요구사항에 맞춰  
**Service Layer 중심 데이터 가공 구조**를 설계했습니다.

```text
Controller
  ↓
Service (집계 · 계산 · 비즈니스 로직)
  ↓
Mapper (통계 SQL)
  ↓
Response VO (대시보드 전용)
```


- Controller: 요청 / 응답만 담당
- Service: 집계 · 계산 · 인증 정보 활용
- Response VO: 화면에서 바로 사용 가능한 구조

---

## 🔐 Authentication & Authorization

### Authentication
- JWT 기반 인증
- `/me` API를 통한 인증 상태 복원
- 페이지 새로고침 시 인증 유지

### Authorization
- Role: USER / ADMIN
- 회원 테이블의 `m_admin` 컬럼 기반 권한 처리
- URL 직접 접근 시 서버 단 권한 검증

---

## 🧩 Custom User Principal Design

JWT 인증 이후  
Security Context에 저장되는 사용자 정보를  
서비스 요구사항에 맞게 확장했습니다.

- 로그인 ID (`m_id`)
- 회원 PK (`m_idx`)
    - 면접 기록
    - 토큰 사용량
    - 통계 집계 기준
- 권한 정보 (`ROLE_USER`, `ROLE_ADMIN`)

이를 통해  
추가 DB 조회 없이  
**사용자 PK 기준 집계 로직 처리**가 가능해졌습니다.

---

## 🔍 JWT Filter Flow

JWT 필터(`OncePerRequestFilter`) 처리 흐름

1. Authorization 헤더에서 JWT 추출
2. 토큰 만료 여부 검증
3. 사용자 식별자(`m_id`) 추출
4. 사용자 활성 상태(`m_active`) 검증
5. 인증 성공 시 Security Context 등록

➡️ URL 직접 접근 시에도  
서버 단에서 인증 · 권한 검증 수행

---

## 🧠 Backend Design Philosophy

- CRUD가 아닌 **집계 중심 API 설계**
- 대시보드에서 바로 판단 가능한 응답 구조
- 인증 정보(`m_idx`)를 활용한 통계 일관성 유지
- 프론트엔드 시각화를 고려한 데이터 구조 제공

---

## 🛠 Tech Stack (Backend)

| Category | Stack |
|--------|------|
| Language | Java 17 |
| Framework | Spring Boot |
| Security | Spring Security, JWT |
| ORM / Mapper | MyBatis |
| Database | MySQL |
| Infra | AWS EC2 (Ubuntu), Docker, Nginx |
| CI/CD | GitHub Actions |



