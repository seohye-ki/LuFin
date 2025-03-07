# [2025-03-07] 4일차\_Zustand

## 🎯 학습 목표

- ZUSTAND의 기본 개념 이해
- ZUSTAND를 사용하여 상태 관리 방법 학습

## 📌 배운 내용

### 🔹 ZUSTAND의 기본 개념

- **ZUSTAND**는 React 애플리케이션에서 상태 관리를 쉽게 할 수 있게 도와주는 경량화된 상태 관리 라이브러리입니다.
- React의 `useState`나 `useReducer`를 대체할 수 있으며, 전역 상태 관리와 로컬 상태 관리를 모두 간단하게 처리할 수 있습니다.
- ZUSTAND는 "구성 요소"별로 상태를 관리할 수 있어 복잡한 애플리케이션에서도 유연하게 사용할 수 있습니다.

### 🔹 기본 사용법

- ZUSTAND는 `create` 함수를 사용해 상태를 정의하고, 해당 상태를 전역적으로 관리할 수 있습니다. 예를 들어, 상태를 정의하고, 해당 상태를 읽고 업데이트할 수 있는 훅을 제공합니다.

```javascript
import create from "zustand";

const useStore = create((set) => ({
  count: 0,
  increment: () => set((state) => ({ count: state.count + 1 })),
  decrement: () => set((state) => ({ count: state.count - 1 })),
}));

// 사용 예시
function Counter() {
  const { count, increment, decrement } = useStore();

  return (
    <div>
      <h1>{count}</h1>
      <button onClick={increment}>Increment</button>
      <button onClick={decrement}>Decrement</button>
    </div>
  );
}
```

### 🔹 ZUSTAND의 주요 특징

- **간편한 상태 관리**: ZUSTAND는 간단한 API로 상태를 정의하고 사용할 수 있어 설정이 매우 간단합니다.
- **상태 변경 함수**: 상태를 변경하는 함수는 `set`을 통해 상태를 변경하고, 이를 기반으로 리렌더링을 발생시킵니다.

- **전역 상태 관리**: 애플리케이션의 다양한 컴포넌트에서 동일한 상태를 공유하며, 변경사항을 자동으로 반영할 수 있습니다.

### 🔹 상태 저장소 (Store) 설정

- ZUSTAND는 하나 이상의 상태 저장소를 만들 수 있으며, 각 상태는 독립적으로 관리됩니다. `create` 함수를 사용하여 여러 상태를 손쉽게 정의할 수 있습니다.

```javascript
const useStore = create((set) => ({
  user: null,
  setUser: (user) => set({ user }),
}));
```

- 여기서 `setUser`는 `user` 상태를 업데이트하는 함수로, 새로운 사용자를 설정할 때 사용됩니다.

### 🔹 상태 선택 (Selectors)

- ZUSTAND에서는 상태를 선택하여 컴포넌트에서 필요한 값만을 구독할 수 있습니다. 이로 인해 불필요한 리렌더링을 방지할 수 있습니다.

```javascript
const useStore = create((set) => ({
  user: { name: "John", age: 30 },
  setUserName: (name) => set((state) => ({ user: { ...state.user, name } })),
}));

// 상태 선택
const userName = useStore((state) => state.user.name);
```

### 🔹 미들웨어

- ZUSTAND는 상태 변경을 추적하거나 상태를 로컬 저장소에 저장하는 등의 작업을 할 수 있는 미들웨어를 제공합니다. 예를 들어, `persist` 미들웨어를 사용하여 상태를 로컬 스토리지에 저장할 수 있습니다.

```javascript
import create from "zustand";
import { persist } from "zustand/middleware";

const useStore = create(
  persist(
    (set) => ({
      count: 0,
      increment: () => set((state) => ({ count: state.count + 1 })),
    }),
    {
      name: "zustand-store", // 로컬 스토리지에 저장할 키
    }
  )
);
```

## 🔗 참고자료

- [ZUSTAND 공식 문서](https://github.com/pmndrs/zustand)
- [ZUSTAND 설치 가이드](https://github.com/pmndrs/zustand#installation)
