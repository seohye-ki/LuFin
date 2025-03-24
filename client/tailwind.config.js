/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Pretendard', '-apple-system', 'BlinkMacSystemFont', 'system-ui', 'Roboto', 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'sans-serif'],
      },
      fontSize: {
        // Heading styles
        'h1': ['2xl', { // 32px
          fontWeight: '400',
        }],
        'h1-semibold': ['2xl', {
          fontWeight: '600',
        }],
        'h1-bold': ['2xl', {
          fontWeight: '700',
        }],
        'h2': ['xl', { // 24px
          fontWeight: '400',
        }],
        'h2-semibold': ['xl', {
          fontWeight: '600',
        }],
        'h2-bold': ['xl', {
          fontWeight: '700',
        }],
        
        // Paragraph styles
        'p1': ['lg', { // 18px
          fontWeight: '400',
        }],
        'p1-medium': ['lg', {
          fontWeight: '500',
        }],
        'p1-semibold': ['lg', {
          fontWeight: '600',
        }],
        'p2': ['base', { // 16px
          fontWeight: '400',
        }],
        'p2-medium': ['base', {
          fontWeight: '500',
        }],
        'p2-semibold': ['base', {
          fontWeight: '600',
        }],
        'p3': ['sm', { // 14px
          fontWeight: '400',
        }],
        'p3-medium': ['sm', {
          fontWeight: '500',
        }],
        'p3-semibold': ['sm', {
          fontWeight: '600',
        }],
        'p3-bold': ['sm', {
          fontWeight: '700',
        }],
        
        // Caption styles
        'c1': ['xs', { // 12px
          fontWeight: '400',
        }],
        'c1-medium': ['xs', {
          fontWeight: '500',
        }],
        'c1-semibold': ['xs', {
          fontWeight: '600',
        }],
        'c1-bold': ['xs', {
          fontWeight: '700',
        }],
        'c2': ['10px', { // 10px
          lineHeight: '0.75rem', // 12px
          fontWeight: '300',
        }],
        'c2-regular': ['10px', {
          lineHeight: '0.75rem',
          fontWeight: '400',
        }],
        'c2-medium': ['10px', {
          lineHeight: '0.75rem',
          fontWeight: '500',
        }],
        'c2-semibold': ['10px', {
          lineHeight: '0.75rem',
          fontWeight: '600',
        }],
      },
      colors: {
        // Regular colors
        white: '#FFFFFF',
        'broken-white': '#F7F8FA',
        'new-grey': '#EEEEEE',
        'grey-25': '#A7A9AA',
        'grey-30': '#D9D9D94d',
        'light-grey': '#A7A9AA',
        grey: '#8A8D8E',
        'dark-grey': '#8A8D8E',
        black: '#242424',
        
        // Main colors
        'light-cyan': {
          DEFAULT: '#C3EBFA',
          30: '#C3EBFAAd',
        },
        purple: {
          DEFAULT: '#CFCEFF',
          30: '#CFCEFFAd',
        },
        yellow: {
          DEFAULT: '#FAE27C',
          30: '#FAE27C4d',
        },
        
        // Semantic colors
        success: '#00997E',
        danger: '#FF414B',
        info: '#4A8DE8',
        warning: '#FFAE41',
        
        // Secondary colors
        'dark-purple': '#8785FF',
        'dark-pink': '#FF88E5',
        pink: {
          DEFAULT: '#FFDBF7',
          30: '#FFEAFAAd',
        },
        
        // Function colors
        placeholder: '#D9D9D9',
      },
    },
  },
  plugins: [],
} 