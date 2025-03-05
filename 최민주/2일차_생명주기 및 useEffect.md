# [2025-03-05] 2일차\_생명주기 및 useEffect

## 🎯 학습 목표

- 해당 컴포넌트의 생명주기(과정) 이해
- `useEffect` 훅의 활용법 학습

## 📌 배운 내용

### 💺 생명주기와 컴포넌트 종류

- 생명주기(Lifecycle)는 컴포넌트가 생성, 사용 중, 반응, 소멸되는 과정을 의미합니다.
- 컴포넌트는 방식에 따라 두 가지로 나뉘는데, 함수형 컴포넌트와 클래스형 컴포넌트가 있습니다.

### 🔹 `useEffect` 함수 활용

- `useEffect` 훅은 컴포넌트의 생명주기와 관련된 반응을 처리할 때 사용됩니다.
- 클래스형 컴포넌트에서 사용하던 `componentDidMount`, `componentDidUpdate`, `componentWillUnmount`를 대체할 수 있습니다.

```jsx
import React, { useState, useEffect } from "react";

function Timer() {
  const [count, setCount] = useState(0);

  useEffect(() => {
    console.log("컴포넌트 마운트 설정");
    const interval = setInterval(() => {
      setCount((prevCount) => prevCount + 1);
    }, 1000);

    return () => {
      console.log("컴포넌트 언마운트");
      clearInterval(interval);
    };
  }, []); // 불필요한 재실행을 막기 위한 빈 배열 적용

  return <p>경과 시간: {count}초</p>;
}

export default Timer;
```

### 🔹 `useEffect` 리뷰

- **처음 한 번만 실행**: `useEffect(() => {...}, [])`
- **해당 값이 변경될 때 실행**: `useEffect(() => {...}, [state])`
- **컴포넌트가 렌더링될 때마다 실행**: `useEffect(() => {...})`

## 🔗 참고자료

- [React 공식 문서](https://reactjs.org/docs/getting-started.html)
- [useEffect 함수](https://reactjs.org/docs/hooks-effect.html)
