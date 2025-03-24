import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'


// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
  ],
  server: {
    host: '127.0.0.1',
    port: 5713,
    open: true, // 브라우저를 자동으로 열기
    strictPort: false, // 포트가 이미 사용 중인 경우 다른 포트 사용
    cors: true, // CORS 활성화
  },
})
