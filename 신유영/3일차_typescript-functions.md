# [2025-03-06] 3일차 Typescript - 함수와 타입

## 🎯 학습 목표

- TypeScript에서 함수의 타입을 정의하는 방법을 이해한다.
- 함수 타입 표현식과 호출 시그니처를 학습한다.
- 함수의 타입 호환성과 오버로딩 개념을 익힌다.
- 사용자 정의 타입 가드를 활용하여 보다 안전한 코드 작성법을 익힌다.

## 📌 TypeScript의 기본 타입

- 함수 타입
  - 함수의 매개변수와 반환 타입을 명확하게 지정
- 함수 타입 표현식과 호출 시그니처
  - 함수 타입을 별도의 타입으로 정의할 수 있음
  - type 또는 interface를 활용 가능
- 함수 타입의 호환성
  - 매개변수 개수와 반환 타입이 일치하면 함수 타입 간 할당이 가능
  - 추가 매개변수가 있는 함수는 더 적은 매개변수를 요구하는 함수에 할당 가능 (초과 매개변수 허용)
- 함수 오버로딩
  - 같은 함수 이름을 여러 개 정의하고 매개변수의 개수나 타입에 따라 다르게 동작하도록 설정
- 사용자 정의 타입 가드
  - typeof 또는 instanceof를 활용하여 특정 타입을 판별
  - is 키워드를 사용하여 타입을 좁히는 함수 정의 가능

### 🔹 코드 예제

```
function add(a: number, b: number): number {
  return a + b;
}

const multiply = (x: number, y: number): number => x * y;
```

```
// 함수 타입 표현식
type MathOperation = (a: number, b: number) => number;

const subtract: MathOperation = (a, b) => a - b;

// 호출 시그니처 (Call Signature)
interface Transformer {
  (input: string): string;
}

const toUpperCase: Transformer = (text) => text.toUpperCase();
```

```
type Print = (message: string) => void;

const printMessage: Print = (msg) => console.log(msg);

// 매개변수가 더 많은 함수는 호환 가능
const printWithTimestamp: Print = (msg, timestamp = Date.now()) => {
  console.log(`[${timestamp}] ${msg}`);
};

printMessage("Hello, TypeScript!"); // ✅ 가능
printWithTimestamp("Logging message"); // ✅ 가능
```

```
function greet(name: string): string;
function greet(name: string, age: number): string;
function greet(name: string, age?: number): string {
  if (age !== undefined) {
    return `Hello, my name is ${name} and I am ${age} years old.`;
  }
  return `Hello, my name is ${name}.`;
}

console.log(greet("Alice")); // Hello, my name is Alice.
console.log(greet("Bob", 30)); // Hello, my name is Bob and I am 30 years old.
```

```
type Dog = { bark: () => void };
type Cat = { meow: () => void };

function isDog(animal: Dog | Cat): animal is Dog {
  return (animal as Dog).bark !== undefined;
}

function makeSound(animal: Dog | Cat) {
  if (isDog(animal)) {
    animal.bark();
  } else {
    animal.meow();
  }
}

const myPet: Dog = { bark: () => console.log("Woof!") };
makeSound(myPet); // Woof!
```

## 🔗 참고자료
