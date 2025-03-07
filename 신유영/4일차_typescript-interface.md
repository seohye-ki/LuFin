# [2025-03-07] 4일차 Typescript - 인터페이스

## 🎯 학습 목표

- TypeScript에서 interface를 활용하는 방법을 익힌다.
- 인터페이스를 확장하고, 여러 개의 인터페이스를 합치는 방법을 학습한다.

## 📌 TypeScript의 기본 타입

- 인터페이스
  - 객체의 구조를 정의하는 용도로 사용됨
  - type과 유사하지만 확장이 용이함
- 인터페이스 확장하기
  - extends 키워드를 사용하여 인터페이스를 확장 가능
- 인터페이스 합치기
  - 동일한 이름의 인터페이스를 여러 번 선언하면 자동으로 병합됨

### 🔹 코드 예제

```
interface User {
  id: number;
  name: string;
  isAdmin: boolean;
}

const user: User = {
  id: 1,
  name: "Alice",
  isAdmin: true,
};
```

```
interface Person {
  name: string;
  age: number;
}

interface Employee extends Person {
  employeeId: number;
}

const employee: Employee = {
  name: "Bob",
  age: 30,
  employeeId: 1001,
};
```

```
interface Car {
  brand: string;
}

interface Car {
  model: string;
}

const myCar: Car = {
  brand: "Tesla",
  model: "Model 3",
};
```

## 🔗 참고자료
