이름: 공수정

# 프로젝트 개요

기술 스택
: Java 23, Spring Boot 3.4.3, Gradle, JPA, Spring Security, JWT
: H2

# 기능 개요

- 미 로그인 유저: 전체 상품과 옵션에 대해 조회
- 로그인 유저: 자신이 입력했던 상품에 대해서, 상품 및 옵션을 추가, 수정, 삭제 가능

# API 설계

## 인증

### 로그인

이메일과 비밀번호를 이용해 로그인 API

- URL: `POST` `/auth/login`
- 요청
  ```json
  {
    "email": "user@example.com",
    "password": "securepassword"
  }
  ```
- 응답
  ```json
  {
    "accessToken": "jwt_token_here",
    "tokenType": "Bearer",
    "expiresIn": 3600
  }
  ```

### 로그아웃

현재 로그인한 사용자가 로그아웃하는 API

- URL: `POST` `/auth/logout`
- 인증 필요: Authorization: Bearer <JWT 토큰>
- 요청: Body 필요없음
- 응답
  ```json
  {
    "message": "로그아웃 성공"
  }
  ```

## 상품

### 전체 상품 조회

등록된 모든 상품을 조회하는 API

- URL: `GET` `/product?page={페이지번호}&size={페이지당 데이터 수}&keyword={검색어}`
- 응답
  ```json
  {
    "products": [
     {
       "id": 1,
       "name": "상품 이름",
       "description": "상품 설명",
       "price": "상품가격",
       "shippingFee": "배송비",
       "createdBy": "생성한 유저 아이디",
       "createAt" : "생성일",
       "updatedBy": "수정한 유저 아이디",
       "updatedAt" : "수정일"
     }
    ],
    "totalPages": 10,
    "totalItems": 100
  }
  ```

### 특정 상품 조회

특정 상품의 내용을 조회하는 API

- URL: `GET` `/product/{productId}`
- 응답
  ```json
  {
    "id": "추가된 상품의 id",
    "name": "상품 이름",
    "description": "상품 설명",
    "price": "상품 가격",
    "shipping_fee": "배송비",
    "create_date": "등록일",
    "update_date": "수정일"
  }
  ```

### 상품 등록

새로운 상품을 등록하는 API

- URL: `POST` `/product`
- 인증 필요: Authorization: Bearer <JWT 토큰 값>
- 요청
  ```json
  {
    "name": "상품 이름",
    "description": "상품 설명",
    "price": "상품 가격",
    "shipping_fee": "배송비",
    "options" : [
    {
      "name": "옵션이름",
      "optionType": "옵션타입",
      "optionValues": [
        {
          "optionValues" : "옵션 값"
        }   
      ],
      "additionalPrice": "옵션 추가 비용"
    },
    {
      "name": "옵션이름",
      "optionType": "옵션타입",
      "optionValues": [
        {
          "optionValues" : "옵션 값",
          "optionValues" : "옵션 값",
          "optionValues" : "옵션 값"
        }   
      ],
      "additionalPrice": "옵션 추가 비용"
    },
    {
      "name": "옵션이름",
      "optionType": "옵션타입",
      "optionValues": [
        {
          "optionValues" : "옵션 값"
        }   
      ],
      "additionalPrice": "옵션 추가 비용"
    }
    ]    
  }
  ```
- 응답
  ```json
  {
    "id": "상품 id",
    "name": "상품 이름",
    "description": "상품 설명",
    "price": "상품 가격",
    "shipping_fee": "배송비",
    "options" : [
    {
      "id": "옵션 id",
      "name": "옵션이름",
      "optionType": "옵션타입",
      "optionValues": [
        {
          "optionValues" : "옵션 값"
        }   
      ],
      "additionalPrice": "옵션 추가 비용",
      "createdBy": "생성한 유저 아이디",
      "createAt" : "생성일",
      "updatedBy": "수정한 유저 아이디",
      "updatedAt" : "수정일"
    },
    {
      "id": "옵션 id",
      "name": "옵션이름",
      "optionType": "옵션타입",
      "optionValues": [
        {
          "optionValues" : "옵션 값",
          "optionValues" : "옵션 값",
          "optionValues" : "옵션 값"
        }   
      ],
      "additionalPrice": "옵션 추가 비용",
      "createdBy": "생성한 유저 아이디",
      "createAt" : "생성일",
      "updatedBy": "수정한 유저 아이디",
      "updatedAt" : "수정일"
    },
    {
      "id": "옵션 id",
      "name": "옵션이름",
      "optionType": "옵션타입",
      "optionValues": [
        {
          "optionValues" : "옵션 값"
        }   
      ],
      "additionalPrice": "옵션 추가 비용",
      "createdBy": "생성한 유저 아이디",
      "createAt" : "생성일",
      "updatedBy": "수정한 유저 아이디",
      "updatedAt" : "수정일"
    }
    ],
    "createdBy": "생성한 유저 아이디",
    "createAt" : "생성일",
    "updatedBy": "수정한 유저 아이디",
    "updatedAt" : "수정일"
  }
  ```

### 상품 수정

특정 상품의 정보와 옵션을 수정하는 API
옵션 추가, 수정, 삭제 동작

- URL: `PUT` `/product/{productId}`
- 인증 필요: Authorization: Bearer <JWT 토큰 값>
- 요청
  ```json
  {
    "name": "상품 이름",
    "description": "상품 설명",
    "price": "상품 가격",
    "shipping_fee": "배송비",
    "options" : [
    {
      "id": "옵션 id",
      "name": "옵션이름",
      "optionType": "옵션타입",
      "optionValues": [
        {
          "optionValues" : "옵션 값"
        }   
      ],
      "additionalPrice": "옵션 추가 비용"
    },
    {
      "id": "옵션 id",
      "name": "옵션이름",
      "optionType": "옵션타입",
      "optionValues": [
        {
          "optionValues" : "옵션 값",
          "optionValues" : "옵션 값",
          "optionValues" : "옵션 값"
        }   
      ],
      "additionalPrice": "옵션 추가 비용"
    },
    {
      "id": "옵션 id",
      "name": "옵션이름",
      "optionType": "옵션타입",
      "optionValues": [
        {
          "optionValues" : "옵션 값"
        }   
      ],
      "additionalPrice": "옵션 추가 비용"
    }
    ],
    "deleteOptionIds": ["삭제할 옵션 아이디", "삭제할 옵션 아이디"]
  }
  ```
- 응답
  ```json
  {
    "id": "상품 id",
    "name": "상품 이름",
    "description": "상품 설명",
    "price": "상품 가격",
    "shipping_fee": "배송비",
    "options" : [
    {
      "id": "옵션 id",
      "name": "옵션이름",
      "optionType": "옵션타입",
      "optionValues": [
        {
          "optionValues" : "옵션 값"
        }   
      ],
      "additionalPrice": "옵션 추가 비용",
      "createdBy": "생성한 유저 아이디",
      "createAt" : "생성일",
      "updatedBy": "수정한 유저 아이디",
      "updatedAt" : "수정일"
    },
    {
      "id": "옵션 id",
      "name": "옵션이름",
      "optionType": "옵션타입",
      "optionValues": [
        {
          "optionValues" : "옵션 값",
          "optionValues" : "옵션 값",
          "optionValues" : "옵션 값"
        }   
      ],
      "additionalPrice": "옵션 추가 비용",
      "createdBy": "생성한 유저 아이디",
      "createAt" : "생성일",
      "updatedBy": "수정한 유저 아이디",
      "updatedAt" : "수정일"
    },
    {
      "id": "옵션 id",
      "name": "옵션이름",
      "optionType": "옵션타입",
      "optionValues": [
        {
          "optionValues" : "옵션 값"
        }   
      ],
      "additionalPrice": "옵션 추가 비용",
      "createdBy": "생성한 유저 아이디",
      "createAt" : "생성일",
      "updatedBy": "수정한 유저 아이디",
      "updatedAt" : "수정일"
    }
    ],
    "createdBy": "생성한 유저 아이디",
    "createAt" : "생성일",
    "updatedBy": "수정한 유저 아이디",
    "updatedAt" : "수정일"
  }
  ```

### 상품 삭제

특정 상품의 정보를 삭제하는 API

- URL: `DELETE` `/product/{productId}`
- 인증 필요: Authorization: Bearer <JWT 토큰 값>
- 응답
  ```json
  {
    "id": "삭제된 상품의 id"
  }
  ```

# DB 설계

## 유저(user)

| 필드명      | 타입                   | 설명       |
|----------|----------------------|----------|
| id       | BIGINT               | PK, 자동증가 |
| email    | VARCHAR(255)         | 이메일(유니크) |
| password | VARCHAR(255)         | 해시된 비밀번호 |
| role     | ENUM('USER','ADMIN') | 사용자 권한   |

## 상품(product)

| 필드명          | 타입           | 설명          |
|--------------|--------------|-------------|
| id           | BIGINT       | PK, 자동증가    |
| name         | VARCHAR(255) | 상품 이름       |
| description  | VARCHAR(255) | 상품 설명       |
| price        | INT          | 가격          |
| shipping_fee | INT          | 배송료         |
| created_by   | BIGINT       | FK, 생성유저 ID |
| created_at   | datetime     | 생성일         |
| update_by    | BIGINT       | FK, 수정유저 ID |
| update_at    | datetime     | 수정일         |

## 옵션(product_option)

| 필드명              | 타입                      | 설명                  |
|------------------|-------------------------|---------------------|
| id               | BIGINT                  | PK, 자동증가            |
| product_id       | BIGINT                  | FK, 상품 ID           |
| name             | VARCHAR(255)            | 옵션 이름               |
| option_type      | ENUM('INPUT', 'SELECT') | 옵션 타입 (입력 or 선택 옵션) |
| additional_price | INT                     | 가격                  |
| created_by       | BIGINT                  | FK, 생성유저            |
| created_at       | datetime                | 생성일                 |
| update_by        | BIGINT                  | FK, 수정유저            |
| update_at        | datetime                | 수정일                 |

## 옵션 값(product_option)

| 필드명                  | 타입                      | 설명                            |
|----------------------|-------------------------|-------------------------------|
| id                   | BIGINT                  | PK, 자동증가                      |
| product_option_id    | BIGINT                  | FK, 상품 옵션 ID                  |
| product_option_value | VARCHAR(255)            | 옵션 값                          |

# 인증 및 보안

JWT 토큰 구조

- Header: { "alg": "HS256", "typ": "JWT" }
- Payload: { "sub": "user@example.com", "exp": 1712345678 }
- Signature: HMACSHA256

Spring Security 적용 방식

- POST /auth/login → 인증 불필요
- GET /product → 인증 불필요
- * /auth → JWT 인증 필요
- * /product/** → JWT 인증 필요
