<h1 align="center">AI Interview Service - Backend</h1>

<p align="center">
  <b>Dashboard-Oriented Backend Architecture</b><br/>
  대시보드 중심 서비스 운영을 위한 집계·통계·권한 설계
</p>

---

## 📌 Project Overview

AI Interview Service의 백엔드 서버입니다.

본 백엔드는 **대시보드에서 의미 있는 지표를 제공하는 것**을
가장 중요한 목표로 설계되었습니다.

> 프론트엔드에서 설계한  
> **사용자 성장 지표 / 운영·매출 지표**를  
> 안정적으로 제공하기 위한 데이터 집계와 비즈니스 로직에 집중했습니다.

---

## 📊 Dashboard-Centered Backend Design (⭐ Core Focus)

### Why Dashboard First?

이 서비스의 핵심은  
단순히 데이터를 보여주는 것이 아니라,  
**운영과 학습 상태를 판단할 수 있게 만드는 것**이라고 판단했습니다.

따라서 백엔드에서는 단순 조회 API 대신  
**대시보드 전용 집계 API**를 중심으로 설계했습니다.

---

### Dashboard Domains

대시보드 성격에 따라 통계 도메인을 명확히 분리했습니다.

- **사용자 대시보드**  
  면접 점수 추이, 피드백 기반 성장 판단

- **관리자 대시보드**  
  전체 운영 현황 요약

- **통계 대시보드**  
  매출 · 비용 · 사용량 비교 분석

---

## 📈 Data Aggregation Strategy

### Aggregation Approach

- 기간 단위 집계: 일 / 7일 / 월
- 도메인별 통계 분리: 사용자 / 면접 / 매출
- 통계 전용 SQL 및 Mapper 구성 (MyBatis)

단순 Entity 조회가 아닌,  
**통계 목적에 맞는 쿼리와 응답 구조**를 설계했습니다.

---

### Revenue & Cost Modeling

가상의 서비스 수익 모델을 설정하여  
매출과 비용을 함께 고려한 통계 로직을 구현했습니다.

#### Revenue Model
- 면접 1회당: **800원**
- 이력서 분석 1회당: **300원**
- 챗봇: 부가 서비스

#### Cost Model
- GPT 사용 비용: **1000 토큰당 1원**

➡️ 매출 / 비용 / 순수익을 함께 계산하여  
대시보드 통계 API로 제공했습니다.

---

## 🧩 Service Layer & Response Design

대시보드 요구사항에 맞춰  
**Service Layer 중심의 데이터 가공 구조**를 설계했습니다.

```text
Controller
  ↓
Service (집계 · 계산 · 비즈니스 로직)
  ↓
Mapper (통계 SQL)
  ↓
Response VO (대시보드 전용)
```
## 🧩 Service Layer & Authentication Design  
### (Dashboard-Oriented Architecture)

본 백엔드는 대시보드 중심 서비스 특성에 맞춰  
**데이터 집계·계산 로직과 인증 정보 활용을 Service Layer에서 유기적으로 처리**하도록 설계했습니다.

---

### 🧩 Service Layer Responsibility

- **집계 및 계산 로직은 Service Layer에서 처리**
- Controller는 요청/응답 역할만 담당
- 화면에서 바로 사용할 수 있는 **Dashboard 전용 Response 구조 제공**

이를 통해 비즈니스 로직, 통계 처리, 인증 정보 활용을  
Controller로부터 분리하고 유지보수성을 높였습니다.

---

## 🔐 Authentication & Authorization  
### (Dashboard-Oriented Extension)

본 프로젝트의 JWT 인증 구조는 기존 인증 설계를 기반으로 하되,  
**대시보드 및 통계 기능에서 인증 정보를 실질적으로 활용할 수 있도록 확장**했습니다.

JWT 자체를 강조하기보다는,  
대시보드 · 통계 · 로그 처리에 필요한  
**인증 정보 흐름을 안정적으로 제공하는 것**에 초점을 맞췄습니다.

---

### 🎯 Design Goal

- 대시보드 API에서 **인증된 사용자 식별자를 즉시 활용**
- 사용자 / 관리자 구분을 **DB 설계와 일관되게 처리**
- 프론트엔드 인증 상태 복원에 필요한 API 제공

---

### 🧩 Custom User Principal Design

JWT 인증 이후 Security Context에 저장되는 사용자 정보를  
서비스 요구사항에 맞게 확장했습니다.

Spring Security의 `UserDetails`를 확장한  
`CustomUserDetails`를 구현하여 다음 정보를 함께 관리했습니다.

- 로그인 ID (`m_id`)
- **회원 PK (`m_idx`)**  
  → 토큰 사용량 로그, 면접 기록, 통계 집계에 활용
- 권한 정보 (`ROLE_USER`, `ROLE_ADMIN`)

이를 통해 Controller 및 Service Layer에서  
추가적인 DB 조회 없이도  
**사용자 PK 기준 로직 처리**가 가능해졌습니다.

---

### 🏷 Role Mapping Strategy (DB 중심 설계)

권한(Role)은 별도의 Role 테이블이나 Enum이 아닌,  
회원 테이블의 `m_admin` 컬럼을 기준으로 처리했습니다.

- `m_admin = 0` → 일반 사용자 (`ROLE_USER`)
- `m_admin = 1` → 관리자 (`ROLE_ADMIN`)

DB 설계와 Security Role을 일관되게 매핑하여  
권한 판단 로직을 단순화하고 유지보수성을 높였습니다.

---

### 🔍 JWT Filter Extension

JWT 필터(`OncePerRequestFilter`)에서는 다음 흐름을 처리합니다.

1. Authorization 헤더에서 JWT 추출  
2. 토큰 만료 여부 검증  
3. 토큰에서 사용자 식별자(`m_id`) 추출  
4. 사용자 활성 상태(`m_active`) 검증  
5. 인증 성공 시 Security Context에 인증 정보 등록  

이를 통해 URL 직접 접근 시에도  
**서버 단에서 인증 및 권한 검증이 수행**되도록 구성했습니다.

---

### 👤 Authenticated User API (`/me`)

프론트엔드에서 인증 상태 복원 및  
대시보드 접근 제어를 위해 `/me` API를 구현했습니다.

- JWT 인증 정보를 기반으로 현재 로그인 사용자 조회
- Security Context의 인증 정보를 활용하여  
  추가적인 토큰 파싱 없이 사용자 정보 제공
- 인증되지 않은 경우 명확한 실패 응답 반환

이 API를 통해 프론트엔드에서는  
페이지 새로고침 이후에도  
**인증 상태를 안정적으로 복구**할 수 있습니다.

---

### 📊 Why This Matters for Dashboard

이 설계를 통해 다음이 가능해졌습니다.

- 모든 대시보드 API에서  
  **인증된 사용자 PK(`m_idx`) 기준 데이터 집계**
- 토큰 사용량, 면접 기록, 매출 통계 로그를  
  사용자 단위로 안정적으로 저장 및 조회
- 사용자 / 관리자 권한에 따른  
  대시보드 접근 제어 일관성 유지

JWT 인증은 본 프로젝트에서  
**대시보드를 안전하게 제공하기 위한 기본 인프라 역할**로 활용되었습니다.



