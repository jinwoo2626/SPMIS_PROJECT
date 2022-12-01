# SPMIS

### STUDENT PROJECT MANAGEMENT INFORMATION SYSTEM ( 학생 프로젝트 관리 정보 시스템 )
- 프로젝트의 관리를 편하게하기 위한 프로젝트 관리 정보 시스템
- TO DO 리스트 칸반형식의 Task들과 외부 회의방을 사용하여 프로젝트를 관리하고 대시보드를 통해 참여중인 프로젝트들의 정보를 확인 할 수있다.

# 팀원 

### 정재령
https://github.com/dev-Rluan
#### 기능 개발 
- 프로젝트 배포 및 파이프라인 
- Spring Security와 Oauth2를 사용한 로그인
- 디스코드 웹훅(알림)
- 대시보드
- 회의방 링크 및 웹훅링크 관리
- 공개 프로젝트 
- 그룹원 관리
- 파일 공유 (개발 진행 중)
- 알림기능

### 백진우
#### 기능 개발
- chart  
- 이메일 전송 및 인증
- 칸반 관련 및 태스크
- 프로젝트 리스트
- 문서 변환 모듈
- 간트 차트 및 캘린더 제작(개발 진행 중)

### 공통작업
인원수가 적어 백-프론트로 나눈다기보다는 기능 위주로 나누어서 개발하였습니다.
특히 프론트의 경우 먼저 기본 틀을 만들어 놓고 필요한 기능이 있다면 파트를 맡은 사람이 수정하여 사용하였습니다.

## 기능 
### 사용자 관리(Login, join)
- Security와 Oauth2 기술을 사용하여 Form 로그인 구글계정을 이용한 로그인
- 이메일 인증(인증코드)을 통한 회원가입
- 프로젝트별 사용자 권한관리 (개선중)

### Mypage
- 프로필 관리
- 패스워드 변경
- 프로젝트 참가요청 확인 및 수락, 거절
- 닉네임 변경
- 유저의 활동내역 출력(PDF)

## 프로젝트 관리
### Project List
- 참여중인 프로젝트들의 리스트를 확인하고 새로운 프로젝트 생성

### Project
- TO DO 리스트의 칸반 형식으로 작업 진척도 관리
- Task(작업 내용) 생성, 수정, 삭제
- 칸반 생성, 수정, 삭제
- 프로젝트별 룰을 생성, 삭제

### DashBoard
- 본인이 맡은 작업내용, 프로젝트, 프로젝트별 회의방 링크들을 확인

### Project Settings
- 프로젝트 삭제, 종료여부 설정
- 공개범위 수정
- 참여자 별 작업 수 차트로 확인
- 그룹원 추가, 삭제 및 권한 수정
- 회의방 링크 추가 ( 외부 회의방 ex) discord, kakao 오픈 채팅방, 줌 등의 링크)
- 디스코드 웹훅 기능 ( 프로젝트 공지사항을 discord 회의방에 동시에 올려주기 위한 웹훅)

### Public Project List
- 공개된 프로젝트 리스트 확인
- 연결된 프로젝트의 정보를 확인한다.
  (현재는 project 페이지로 바로 이동하지만 새로운 페이지를 하나두어서 참여자와는 다른 화면을 보일 수 있게끔 개선)
- 리스트에 참여버튼으로 프로젝트에 참여요청이 가능 

## 기술 스택
- 개발언어 : Java, JavaScript, HTML, CSS 
- IDE : Intellij
- Framework : Spring Framework
- view : Thymeleaf
- DB : MySql(workbentch를 사용하여 관리), mybaties
- 배포 : Heroku
- CI/CD : GitLab
- 라이브러리 : chart.js
- 협업 도구 : GitLab, GitHub


## 개선할 사항 및 현재 미구현 기능
- Project Setting쪽에 몰려있는 기능들을 페이지 분리하여 적용
- 파일 공유 기능 개발
- 간트 차트 및 캘린더 제작
- 개인 프로필 페이지 제작
- 공개 프로젝트에 접근할때 공개 범위를 지정할때 상세하게 설정하여 보여줄 수 있도록 개선
- 전체적인 디자인 
- Heroku 무료 플랜 종료로인한 배포 및 DB 서비스 이전

## 배포 링크(heroku 무료 플랜 중지로 인한 배포 중지 - 다른 배포방식을 찾는중입니다.)
~https://spmis.herokuapp.com/~

## 주요 기술 코드
### 이메일 전송 및 인증
- 의존성 추가하기  
![image](https://user-images.githubusercontent.com/74890691/204205024-1aa90583-5212-49af-af4e-68f26f96d839.png)  

- 설정정보 입력  
![image](https://user-images.githubusercontent.com/74890691/204224850-ae2502da-022b-4c99-93ea-71a07781ba54.png)  
이메일 발송을 위한 계정 정보를 설정과 host, port등의 정보들을 application.yml에 입력합니다.  
password는 구글계정의 앱 비밀번호키를 사용합니다.  

- Config 생성  
![image](https://user-images.githubusercontent.com/74890691/204205899-2c558891-d9c3-4dce-83f5-8a7be9fc7e86.png)  
application.yml파일 작성 후 EmailConfig.java파일을 생성합니다.  
EmailConfig.java파일을 생성하였으면 yml파일에서 작성한 정보들을 불러오고 값을 세팅하는 코드를 작성합니다.  

- Service 생성  
![image](https://user-images.githubusercontent.com/74890691/204206238-0c261b89-d15c-4b39-b5d1-75620727e0c1.png)  
EmailService에서는 수신자, 메일제목, 수신내용들을 설정하고, javaMailSender로 값을 전송합니다.  
JavaMailSender에서 실제 메일이 발송됩니다.  

### 차트
- 차트 스크립트 작성  
![image](https://user-images.githubusercontent.com/74890691/204207419-b5fa6e66-781e-4daa-9dd3-bb99f0f63374.png)  

- 차트를 그릴 영역 설정  
![image](https://user-images.githubusercontent.com/74890691/204207461-c822061a-ebdc-4d56-a979-594eee2c3d2d.png)  

- 차트 설정  
![image](https://user-images.githubusercontent.com/74890691/204207516-c0e3eadb-f417-49f5-8e6a-98aa8fb1cf91.png)  
먼저 앞에서 설정한 Canvas를 불러옵니다.  
js에서 차트의 유형(bar, bubble, line)을 선택할 수 있습니다.  
data에서는 차트에서 나타낼 데이터값을 저장합니다.  
Controller에서 model로 넘긴 값을 불러와서 차트 값(datasets)으로 설정합니다.  
options에서는 차트모양을 꾸밀 수 있습니다.  

### 문서변환모듈
- 의존성 추가하기  
![image](https://user-images.githubusercontent.com/74890691/204219328-8054492e-916c-4490-8939-aa4fa5e6e984.png)  
pdf 라이브러리 itext와 pdf에서 사용할 차트라이브러리 jfreechart를 추가합니다.

- Controller 작성  
![image](https://user-images.githubusercontent.com/74890691/204220764-a9edff91-acbb-4508-9ec2-4a028b3b68ec.png)  

![image](https://user-images.githubusercontent.com/74890691/204220561-361cb1a4-1b78-469e-8052-4216620bd64d.png)  
마이페이지 활동내역출력하기에서 선택한 프로젝트, 활동값들을 @RequestParam으로 가져옵니다.  
선택한값들과 세션값을 불러와서 ModelAndView에 값을 추가하고 CustomPdfViewService로 ModelAndView를 리턴합니다.  

- Service 작성  
![image](https://user-images.githubusercontent.com/74890691/204222346-2121ba04-0d76-4f53-b3c1-ffd03bf0ff87.png)  
![image](https://user-images.githubusercontent.com/74890691/204222614-67c7a18a-e941-4ed0-8b4f-1e0ea82aa2bf.png)  

documentinfo메서드에서 출력할 값들을 불러옵니다.  
builder를 사용하여 저장한 값들을 반환합니다.  
출력할 때 사용할 폰트를 지정합니다.  
  
![image](https://user-images.githubusercontent.com/74890691/204223223-b873a328-7c45-4a71-8880-d2a41b343338.png)  
Chunk와 Paragraph를 이용하여 텍스트를 문서에 출력할 수 있습니다.  
  
![image](https://user-images.githubusercontent.com/74890691/204223511-74df4e29-46fb-4a0a-858a-813e714a5a95.png)  

차트를 출력하기 위한 jfreechart 코드입니다.  
먼저 데이터셋을 생성하고 데이터셋에 차트로 출력할 값들을 지정합니다. (value, rowKey, columnKey)형식으로 들어갑니다.  

차트 객체를 선언하고 제목, 차트 형식, 라벨, 데이터셋들을 지정합니다.  

![image](https://user-images.githubusercontent.com/74890691/204228348-d5d06413-0d84-4918-901e-fb0787a9548d.png)  
표를 출력하기 위한 코드입니다.
PdfTable로 객체를 생성하고 PdfPCell로 셀을 생성하고 추가하는 형식으로 작성합니다.


