# [2025-03-05] 2일차 리액트 - TypeScript 기본 타입

## 🎯 학습 목표

- TypeScript의 기본 타입을 이해하고 활용할 수 있다.
- 객체 타입의 호환성을 학습한다.
- 대수 타입과 타입 추론의 개념을 익힌다.

## 📌 TypeScript의 기본 타입

- 타입 계층도와 함께 기본타입 살펴보기
  - TypeScript의 기본 타입 (number, string, boolean, null, undefined, symbol, bigint)
  - any, unknown, never 등의 특수 타입
- 객체 타입의 호환성
  - 구조적 타입 시스템과 객체 타입 간의 호환성 개념
- 대수 타입
  - 유니온 타입 (|): 여러 개의 타입 중 하나를 가질 수 있음
  - 인터섹션 타입 (&): 여러 개의 타입을 모두 만족해야 함
- 타입 추론론
  - 변수에 초기값을 할당하면 자동으로 타입을 추론
  - const와 let의 차이점

### 🔹 코드 예제

```
let age: number = 30;
let username: string = "Alice";
let isActive: boolean = true;

let anything: any = "Hello"; // any는 타입 체크를 하지 않음
let unknownValue: unknown = 42; // unknown은 보다 안전한 any
```

```
type Person = {
  name: string;
  age: number;
};

let user: Person = { name: "Alice", age: 25 };

let student: { name: string; age: number; grade: string } = {
  name: "Bob",
  age: 20,
  grade: "A",
};

// student 타입을 Person 타입으로 할당 가능 (추가 속성은 허용)
let newUser: Person = student; // ✅ 가능

// 반대로는 불가능 (grade 속성이 없기 때문)
// let newStudent: typeof student = user; // ❌ 오류 발생
```

```
// 유니온 타입
let id: number | string;
id = 10; // ✅ 가능
id = "user123"; // ✅ 가능

// 인터섹션 타입
type Developer = { name: string; skills: string[] };
type Manager = { name: string; department: string };

type TechLead = Developer & Manager;

let techLead: TechLead = {
  name: "Charlie",
  skills: ["TypeScript", "React"],
  department: "Engineering",
};
```

```
let message = "Hello, TypeScript"; // 자동으로 string 타입 추론
// message = 42; // ❌ 오류 발생

const pi = 3.14; // 자동으로 상수 리터럴 타입 추론
// pi = 3.1415; // ❌ 오류 발생

function add(x: number, y: number) {
  return x + y; // 반환 타입 자동 추론 → number
}
```

## 🔗 참고자료
