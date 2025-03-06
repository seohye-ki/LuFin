# [2025-03-06] 2일차\_Tailwind CSS

## 🎯 학습 목표

- Tailwind CSS의 기본 개념 이해
- Tailwind CSS를 사용하여 스타일링 방법 학습

## 📌 배운 내용

### 🔹 Tailwind CSS의 기본 개념

- **Tailwind CSS**는 유틸리티 퍼스트(Utility-first) CSS 프레임워크로, 클래스 기반으로 스타일을 지정하는 방식입니다.
- 기존의 CSS 프레임워크와 달리, 미리 정의된 클래스를 사용하여 스타일을 적용하고, 필요할 때마다 클래스를 조합하여 컴포넌트를 구성합니다.
- **재사용성**이 뛰어나며, HTML 파일 안에서 바로 스타일을 설정할 수 있어 개발 생산성을 높이는 데 유리합니다.

### 🔹 기본 사용법

- Tailwind CSS는 설치 후 **HTML** 파일에 클래스를 추가하여 스타일을 적용합니다. 예를 들어, `text-center`, `bg-blue-500`, `p-4`와 같은 클래스를 사용하여 텍스트 정렬, 배경 색상, 패딩 등을 손쉽게 설정할 수 있습니다.

```html
<div class="bg-blue-500 text-white p-4 text-center">
  <h1>Welcome to Tailwind CSS</h1>
  <p>빠르고 효율적인 스타일링을 제공하는 프레임워크입니다.</p>
</div>
```

### 🔹 Tailwind CSS의 주요 클래스

- **배경 색상**: `bg-{color}-{intensity}`
  - 예: `bg-red-500`, `bg-blue-300`
- **텍스트 색상**: `text-{color}-{intensity}`

  - 예: `text-white`, `text-gray-700`

- **간격**: `p-{size}`, `m-{size}` (Padding, Margin)

  - 예: `p-4`, `m-2`, `px-6`, `my-3`

- **폰트 크기**: `text-{size}`

  - 예: `text-xl`, `text-lg`

- **글꼴**: `font-{family}`
  - 예: `font-sans`, `font-serif`

### 🔹 반응형 디자인

- Tailwind CSS는 반응형 디자인을 지원하여, `sm:`, `md:`, `lg:` 등의 접두사를 사용해 화면 크기에 따라 다른 스타일을 적용할 수 있습니다.

```html
<div class="bg-blue-500 p-4 sm:text-sm md:text-lg lg:text-xl">
  반응형 텍스트 크기
</div>
```

### 🔹 커스터마이징

- Tailwind CSS는 기본 제공되는 클래스를 커스터마이즈하여 사용할 수 있습니다. `tailwind.config.js` 파일에서 색상, 폰트 크기 등 사용자 정의 값을 설정할 수 있습니다.

```js
module.exports = {
  theme: {
    extend: {
      colors: {
        "custom-blue": "#1E40AF",
      },
    },
  },
};
```

## 🔗 참고자료

- [Tailwind CSS 공식 문서](https://tailwindcss.com/docs)
- [Tailwind CSS 설치 가이드](https://tailwindcss.com/docs/installation)
