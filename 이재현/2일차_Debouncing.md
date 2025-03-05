# [2025-03-05] 2일차 리액트 - Debouncing 적용 방법

## 🎯 학습 목표

- Debounce 개념 및 필요성 이해
- React에서 `lodash.debounce`를 활용한 Debouncing 적용
- Debouncing을 활용한 검색 최적화

---

## 📌 Debouncing 개념 및 필요성

### 🔹 Debounce란?

- 사용자의 입력 또는 이벤트가 발생한 후 **일정 시간이 지나야 실행되는 패턴**
- 연속된 입력을 방지하고, 성능 최적화를 위해 사용됨

### 🔹 Debouncing이 필요한 이유

1. **입력 필드 최적화**
   - 검색창에서 사용자가 입력할 때마다 API 호출 방지
2. **API 요청 최적화**
   - 사용자가 빠르게 여러 번 요청을 보내는 경우 네트워크 부하 방지
3. **UI 렌더링 개선**
   - 불필요한 컴포넌트 리렌더링 방지

---

## 📌 React에서 `debounce` 적용 방법

### 🔹 `lodash.debounce` 활용

```tsx
import { useState, useCallback } from 'react';
import debounce from 'lodash.debounce';

const SearchComponent = () => {
  const [query, setQuery] = useState('');

  const handleSearch = useCallback(
    debounce((value) => {
      console.log('Searching for:', value);
      // API 요청 또는 상태 업데이트 수행
    }, 500), // 500ms 동안 입력이 없을 때 실행
    []
  );

  const onChange = (e) => {
    setQuery(e.target.value);
    handleSearch(e.target.value);
  };

  return (
    <input
      type="text"
      value={query}
      onChange={onChange}
      placeholder="검색어 입력..."
    />
  );
};
```

**💡 주요 개념**

- `debounce`를 적용하여 사용자가 입력할 때마다 API 요청이 즉시 실행되지 않음
- `500ms` 동안 추가 입력이 없을 때만 검색 실행

---

## 📌 Debouncing을 활용한 검색 자동완성 최적화

### 🔹 Debounce를 활용한 API 요청 최적화 (자동완성 검색)

#### ✅ `useCallback` + `debounce` 활용

```tsx
import { useState, useEffect, useCallback } from 'react';
import debounce from 'lodash.debounce';
import axios from 'axios';

const AutoCompleteSearch = () => {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);

  const fetchSearchResults = useCallback(
    debounce(async (searchTerm) => {
      if (!searchTerm) return;
      try {
        const response = await axios.get(
          `https://api.example.com/search?q=${searchTerm}`
        );
        setResults(response.data);
      } catch (error) {
        console.error('검색 실패:', error);
      }
    }, 700), // 700ms 후 API 요청 실행
    []
  );

  useEffect(() => {
    fetchSearchResults(query);
  }, [query, fetchSearchResults]);

  const handleChange = (e) => {
    setQuery(e.target.value);
  };

  return (
    <div>
      <input
        type="text"
        value={query}
        onChange={handleChange}
        placeholder="검색어 입력..."
      />
      <ul>
        {results.map((result, index) => (
          <li key={index}>{result.name}</li>
        ))}
      </ul>
    </div>
  );
};
```

**💡 주요 개념**

- `debounce`를 적용하여 연속 입력 시 API 요청이 지연됨
- `700ms` 동안 추가 입력이 없을 경우 API 호출 (자동완성 검색 최적화)
- `useEffect`를 활용하여 `query` 변경 시 `fetchSearchResults` 실행

---

## 📌 Debounce 적용 이점

1. **불필요한 API 호출 방지 → 서버 부하 감소**
2. **사용자 입력 반응성 개선 → 성능 최적화**
3. **코드의 가독성 및 유지보수성 향상**

---

## 🔗 참고자료

- [Lodash Debounce 공식 문서](https://lodash.com/docs/4.17.15#debounce)
- [React 공식 문서 - Hooks API](https://react.dev/reference/react/useCallback)
- [MDN - Debounce vs Throttle](https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/setTimeout#debouncing_and_throttling)

---
