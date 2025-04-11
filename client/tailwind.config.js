/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        // Regular Colors
        white: 'var(--color-white)',
        'broken-white': 'var(--color-broken-white)',
        'new-grey': 'var(--color-new-grey)',
        'grey-25': 'var(--color-grey-25)',
        'grey-30': 'var(--color-grey-30)',
        'light-grey': 'var(--color-light-grey)',
        grey: 'var(--color-grey)',
        'dark-grey': 'var(--color-dark-grey)',
        black: 'var(--color-black)',

        // Main Colors
        'light-cyan': 'var(--color-light-cyan)',
        'light-cyan-30': 'var(--color-light-cyan-30)',
        purple: 'var(--color-purple)',
        'purple-30': 'var(--color-purple-30)',
        yellow: 'var(--color-yellow)',
        'yellow-30': 'var(--color-yellow-30)',

        // Semantic Colors
        success: 'var(--color-success)',
        'success-60': 'var(--color-success-60)',
        danger: 'var(--color-danger)',
        'danger-60': 'var(--color-danger-60)',
        info: 'var(--color-info)',
        warning: 'var(--color-warning)',

        // Secondary Colors
        'dark-purple': 'var(--color-dark-purple)',
        'dark-pink': 'var(--color-dark-pink)',
        pink: 'var(--color-pink)',
        'pink-30': 'var(--color-pink-30)',

        // Function Colors
        placeholder: 'var(--color-placeholder)',
      },
      fontSize: {
        h1: ['var(--text-h1)', { lineHeight: 'var(--leading-h1)' }],
        h2: ['var(--text-h2)', { lineHeight: 'var(--leading-h2)' }],
        h3: ['var(--text-h3)', { lineHeight: 'var(--leading-h3)' }],
        p1: ['var(--text-p1)', { lineHeight: 'var(--leading-p1)' }],
        p2: ['var(--text-p2)', { lineHeight: 'var(--leading-p2)' }],
        c1: ['var(--text-c1)', { lineHeight: 'var(--leading-c1)' }],
        c2: ['var(--text-c2)', { lineHeight: 'var(--leading-c2)' }],
      },
      fontWeight: {
        light: 'var(--font-weight-light)',
        regular: 'var(--font-weight-regular)',
        medium: 'var(--font-weight-medium)',
        semibold: 'var(--font-weight-semibold)',
        bold: 'var(--font-weight-bold)',
      },
      fontFamily: {
        pretendard: ['var(--font-pretendard)', 'system-ui', 'sans-serif'],
      },
    },
  },
  plugins: [require('tailwind-scrollbar-hide')],
};
