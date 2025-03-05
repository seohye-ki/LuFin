# [2025-03-04] 1일차 리액트 - TypeScript 이해하기

## 🎯 학습 목표

- TypeScript의 기본 타입을 이해하고 활용할 수 있다.

## 📌 TypeScript의 기본 타입

- 원시타입과 리터럴타입
  - number, string, boolean 등의 기본 타입
- 배열과 튜플
  - 배열: 타입[] 또는 Array<타입> 사용
  - 튜플: 고정된 개수와 타입을 가지는 배열
- 객체
  - 객체 타입 정의
  - 선택적 속성 (? 사용)
- 타입 별칭과 인덱스 시그니처
  - type 키워드를 사용하여 타입을 별칭으로 저장
  - 인덱스 시그니처 ([key: string]: 타입) 사용하여 동적 속성 지정 가능

### 🔹 코드 예제

```
let age: number = 25;
let userName: string = "John";
let isActive: boolean = true;

// 리터럴 타입
let role: "admin" | "user" | "guest";
role = "admin"; // ✅ 가능
role = "superuser"; // ❌ 오류 발생
```

```
let numbers: number[] = [1, 2, 3, 4, 5];
let names: Array<string> = ["Alice", "Bob", "Charlie"];

// 튜플 (고정된 타입과 개수)
let person: [string, number];
person = ["Alice", 25]; // ✅ 가능
person = [25, "Alice"]; // ❌ 오류 발생
```

```
type User = {
  id: number;
  name: string;
  isAdmin?: boolean; // 선택적 속성
};

let user1: User = {
  id: 1,
  name: "Alice",
};

let user2: User = {
  id: 2,
  name: "Bob",
  isAdmin: true, // 선택적 속성 포함
};
```

```
// 타입 별칭
type Product = {
  id: number;
  name: string;
  price: number;
};

let item: Product = {
  id: 101,
  name: "Laptop",
  price: 1500,
};

// 인덱스 시그니처
type Dictionary = {
  [key: string]: string;
};

let translations: Dictionary = {
  hello: "안녕하세요",
  goodbye: "안녕히 가세요",
};
```

## 🔗 참고자료
