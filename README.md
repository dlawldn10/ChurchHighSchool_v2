# Church-High-School v2. QR코드 출석체크 변경
### 문화교회 고등부 애플리케이션 프로토타입 v2
#### • 문교회고등부 학생 및 교사를 위한 안드로이드 앱 제작
#### • 학생들의 QR코드 생성과 선생님들의 스캐너 -> 출석체크 기능
#### • Firebase를 활용하여 출석, 기도 목록 및 뉴스레터를 포함한 데이터 관리
#### • SQLite를 사용하여 사용자의 설교 노트 저장
#### • 문화교회 고등부 유튜브 채널 바로가기 기능을 통해 온라인 설교 듣기




## 홈 화면
#### 문화교회 고등부에 대한 간단한 소개와 유튜브 바로가기 탭
### <img src = "https://user-images.githubusercontent.com/69448918/115144041-65909200-a085-11eb-85be-029208f41164.jpg" width="300px"> <img src = "https://user-images.githubusercontent.com/69448918/115144029-60334780-a085-11eb-8ac6-53c5e4d57ca7.jpg" width="300px"> 
---
## 문화교회 고등부 선생님들을 위한 출석체크 화면
#### '이번주 출석체크 확인'버튼을 통해 QR코드 스캐너 활성화
#### 스캐너로 학생들 QR코드를 인식하여 해당 주차의 출석 인원 확인
#### ZXing라이브러리 이용한 QR코드스캔 기능
### <img src = "https://user-images.githubusercontent.com/69448918/116881916-ccee3a80-ac5e-11eb-96af-dce72830ff91.jpg" width="300px"> <img src =  https://user-images.githubusercontent.com/69448918/116883198-49354d80-ac60-11eb-9bbe-2bd0812f3071.jpg width="300px"> <img src = "https://user-images.githubusercontent.com/69448918/116881908-c9f34a00-ac5e-11eb-9ca7-ef67d66cfa59.jpg" width="300px">
---
## 문화교회 고등부 학생들을 위한 출석체크 화면
#### 학생 정보를 담은 QR코드를 생성하여 선생님께 스캔받기
#### ZXing라이브러리 이용한 QR코드생성 기능
### <img src = https://user-images.githubusercontent.com/69448918/116881915-cc55a400-ac5e-11eb-914b-f392c85c4573.jpg width="300px">



#### 출석 데이터 베이스
### <img src = https://user-images.githubusercontent.com/69448918/125427865-96007d73-9f8f-4710-87d8-3184a063b59e.png width="800px"> <img src = https://user-images.githubusercontent.com/69448918/125427875-cebe0c3b-5cbc-4ece-aa6c-03909b30f76e.png width="800px">

---
## 이번주 주보를 손쉽게 확인
#### 선생님들 화면에서는 주보 업데이트 가능/학생은 변경 불가
#### 파이어베이스 스토리지 사용
### <img src = "https://user-images.githubusercontent.com/69448918/115144035-61fd0b00-a085-11eb-829f-8d68eb83549f.jpg" width="300px">
---
## 이번주 들은 설교 내용 메모, 휴대폰에 저장
#### SQLite를 이용한 메모 저장
### <img src = "https://user-images.githubusercontent.com/69448918/117546280-9c464080-b064-11eb-8b3e-77f9f71baa81.jpg" width="300px"> <img src = "https://user-images.githubusercontent.com/69448918/117546283-9d776d80-b064-11eb-918b-fde3c985d77b.jpg" width="300px">
---
## 각자의 기도제목을 쓰고 함께 기도하기
#### 익명 게시판으로 설정하여 부담없이 기도제목 게시 가능
#### 리사이클러뷰의 무한 스크롤
#### 파이어베이스 실시간 데이터베이스 사용
### <img src = "https://user-images.githubusercontent.com/69448918/115144024-5e698400-a085-11eb-8535-461c2896de39.jpg" width="300px"> <img src = "https://user-images.githubusercontent.com/69448918/115144045-69241900-a085-11eb-8f5b-9688c9522b19.jpg" width="300px">
---
## 마이페이지에서 프로필 사진과 인적사항 입력/관리
### <img src = "https://user-images.githubusercontent.com/69448918/115144026-60334780-a085-11eb-9c45-8e1944dfd1bc.jpg" width="300px">



### 사용자 정보 데이터베이스
### <img src = https://user-images.githubusercontent.com/69448918/125428283-c7344fc6-9993-4c16-8848-0702473e3a76.png width="800px"> 




