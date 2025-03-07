# [2025-03-07] Nginx

## 🎯 학습 목표

- Nginx의 기본 개념과 역할을 이해하기
- Nginx를 사용하면 오히려 성능이 향상되는 이유를 이해하기
- 리버스 프록시(Reverse Proxy)의 개념과 필요성
- CORS(Cross-Origin Resource Sharing) 에러의 원인과 해결 방법

## 📌 Nginx
리버스 프록시는 클라이언트(브라우저)와 서버(API 서버) 사이에서 요청을 중계하는 서버야. 즉, 클라이언트는 실제 백엔드 서버가 아니라 리버스 프록시(Nginx 같은 서버)에 요청을 보내고, 이 프록시가 백엔드 서버에 전달한 다음, 응답을 받아 클라이언트에게 다시 보내주는 역할을 함.


### 🔹 **Nginx의 역할**

- 웹 서버(Web Server): 정적 파일(HTML, CSS, JS, 이미지 등) 서빙
- 리버스 프록시(Reverse Proxy): 백엔드 서버(API)와 클라이언트 사이에서 요청을 중계
- 로드 밸런서(Load Balancer): 여러 개의 서버에 트래픽을 분산하여 부하를 줄임
- SSL/TLS(HTTPS) 지원: 보안 강화를 위해 HTTPS 설정 가능
- 캐싱(Caching): 자주 요청되는 리소스를 저장하여 성능 최적화

배포할 프로젝트에서, 백엔드 서버를 클라이언트와 직접 연결하는 대신, Nginx를 앞에 두고 트래픽을 관리하면 훨씬 효율적

### 🔹 **Nginx를 두는게 왜 빠를까?**
중간에 Nginx를 하나 더 두면 요청이 느려질 것 같지만, 실제로는 더 빠르게 동작. 왜??
1) 정적 파일 캐싱
- Nginx가 HTML, CSS, JS, 이미지 같은 정적 파일을 캐싱하면, 백엔드까지 요청이 가지 않고 바로 응답 가능!
- 결과: 백엔드 부하 감소 + 빠른 응답
2) 리버스 프록시를 통한 부하 분산
- 백엔드가 직접 모든 요청을 받으면 부담이 크지만, Nginx가 요청을 필터링해서 백엔드에 필요한 요청만 보내줌.
- 결과: 백엔드 서버가 더 가벼워짐.
3) Keep-Alive 연결 유지
- Nginx가 백엔드 서버와 Keep-Alive 연결을 유지하면, 매번 새 연결을 만들지 않아도 됨.
- 결과: 요청 속도 증가
4) 압축(Gzip, Brotli)으로 전송 최적화
- Nginx는 데이터를 압축해서 클라이언트로 보내므로, 네트워크 트래픽이 감소함.
- 결과: 전송 속도가 빨라짐.

## 📌 리버스 프록시(Reverse Proxy)란?

### 🔹 **리버스 프록시의 역할**

직접 백엔드 서버에 요청하면 될 텐데, 굳이 중간에 하나를 더 두는 이유는 뭘까?

1. 보안 강화 (Security)
- 클라이언트가 직접 백엔드 서버(예: http://my-api-server:5000)에 접근하면 보안상 위험할 수 있어, 리버스 프록시를 두면, 실제 백엔드 서버의 IP를 감출 수 있어서 보안이 강화되고, DDoS 공격을 방어하는 WAF(Web Application Firewall) 역할도 할 수 있다.
2. SSL(HTTPS) 적용
- 백엔드 서버는 원래 HTTP로만 동작할 수도 있는데, Nginx 리버스 프록시를 통해 SSL(HTTPS)를 적용하면 보안이 강화되고,
즉, 클라이언트는 https://example.com으로 요청하지만, 내부적으로는 http://backend:5000에 요청이 갈 수도 있게된다.
3. 로드 밸런싱 (Load Balancing)
- 백엔드 서버가 여러 개일 때, 리버스 프록시가 부하를 분산해서 서버가 한쪽으로 몰리는 걸 방지할 수 있다. 예를 들어, 서버가 3개(server1, server2, server3) 있으면, Nginx가 요청을 각각 나눠서 전달할 수 있다.
4. 캐싱 (Caching)
- 정적인 데이터(API 응답, 이미지, HTML 파일 등)를 Nginx가 캐싱하면, 백엔드 서버 부하를 줄이고 성능을 향상시킬 수 있다.
자주 요청되는 데이터를 매번 백엔드에서 가져오지 않고 Nginx가 저장해두고 반환하면 속도가 빨라진다.
5. 도메인 및 경로 매핑
- 예를 들어, 프론트엔드와 백엔드가 다른 포트에서 동작하는데, 클라이언트는 하나의 도메인(example.com)만 알도록 만들고 싶다면?
리버스 프록시를 사용하면 프론트엔드(/)와 백엔드 API(/api/)를 같은 도메인에서 제공할 수 있고, 이렇게 하면 CORS 문제도 줄일 수 있음.


#### 설정예시
```nginx
# https://example.com/api/ 요청이 http://backend-server:8080/ 으로 전달
server {
    listen 80;
    server_name example.com;

    location /api/ {
        proxy_pass http://backend-server:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```


## 📌 CORS(Cross-Origin Resource Sharing)란?

### 🔹 **CORS가 발생하는 이유**
- 브라우저 보안 정책(Same-Origin Policy) 때문에, 다른 출처(Origin)에서 API 요청을 막음
- 출처(Origin) = `프로토콜 + 도메인 + 포트`
- 다른 출처에서 요청하면 CORS 에러 발생

  | 요청 | 같은 출처? | CORS 필요? |
  |---|---|---|
  | `http://example.com` → `http://example.com` | ✅ O | ❌ 필요 없음 |
  | `http://example.com:3000` → `http://example.com:5000` | ❌ X | ✅ 필요 |
  | `https://example.com` → `http://example.com` | ❌ X | ✅ 필요 |
  | `http://localhost:3000` → `http://localhost:5000` | ❌ X | ✅ 필요 |