# 💱 실시간 환율 조회 시스템 (IoT 프로젝트)

MQTT 통신과 공공 API를 활용하여 미국 달러(USD) 환율 정보를 실시간으로 수집하고,  
MongoDB에 저장된 데이터를 웹 페이지에서 실시간으로 확인할 수 있는 시스템입니다.

---

## 🧩 시스템 구성도

[MQTT Publisher (Java)]
↓
[MQTT Broker (Mosquitto)]
↓
[MQTT Subscriber (Node.js)] → [MongoDB]
↓
[Socket.IO → HTML 클라이언트]


---

## ⚙ 사용 기술

| 분류       | 기술/도구                       |
|------------|---------------------------------|
| MQTT       | Mosquitto, Eclipse Paho (Java) |
| Backend    | Node.js, Socket.IO             |
| Database   | MongoDB                        |
| Frontend   | HTML, CSS (템플릿 기반)        |
| API        | 공공데이터포털 환율 정보 API   |

---

## 🗂 주요 기능

### 1. MQTT 통신
- **Publisher**: Java로 구현, 공공 API에서 환율 정보 수집 후 MQTT 메시지 발행
- **Subscriber**: Node.js 기반, 수신된 메시지를 MongoDB에 저장

### 2. MongoDB 저장 구조
- `Country`: 국가명 저장
- `Currency`: 화폐명 저장
- `Rate`: 환율 값 저장

### 3. 실시간 웹 페이지
- Socket.IO를 통해 최신 환율 정보를 주기적으로 전송
- HTML 페이지에서 실시간 표시 (Country, Currency, Rate)

---

## 🧪 실행 방법

### 1. Mosquitto 실행
![Image](https://github.com/user-attachments/assets/29a90ded-df6d-4aa4-89f0-641565e482ff)

### 2. node.js 서버 실행
![Image](https://github.com/user-attachments/assets/5b12cc6a-3b85-44dd-9884-a618b46bd549)

### 3. MQTT Publisher 실행
![Image](https://github.com/user-attachments/assets/84af45ff-9b59-4804-b42c-562a83aec2d6)

### 4. 웹페이지 확인
![Image](https://github.com/user-attachments/assets/d55bcd31-138a-474e-8d5d-926889ad88f1)


## DB 저장 화면
![Image](https://github.com/user-attachments/assets/a97cdd98-c0a2-40c2-ba04-98d1d8206874)

![Image](https://github.com/user-attachments/assets/d87b7beb-4b05-4888-bd14-b96cd9345720)

![Image](https://github.com/user-attachments/assets/3a22e156-d4a3-4603-b81d-2f8971b06f09)

![Image](https://github.com/user-attachments/assets/c6bbd82b-04b9-4acc-9884-17271cff79db)
