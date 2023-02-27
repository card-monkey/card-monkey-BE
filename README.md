<div align="center">
  <img src=https://user-images.githubusercontent.com/50852143/221121802-cbb326fa-df41-4bb5-bc5f-91401e5e71bb.jpg />
  <h3>
    <b>< KDT3 Back-End : Mini Project Team2 ></b>
  </h3>
</div>


***

# 1. 프로젝트 소개 💁
### 프로젝트 설명
- 패스트캠퍼스 미니 핀테크 프로젝트
- 카드상품을 다루며 추천해주는 서비스
- JWT을 이용한 로그인/로그아웃, JPA을 이용한 카드추천, 검색, 신청 등의 기능구현

### 개발 기간
- 1차: 2023.2.13 ~ 2023.2.24(총 12일)

### 링크 모음
<a href="https://card-monkey.netlify.app/">
  <img src="https://img.shields.io/badge/배포사이트-FF0000?style=for-the-badge&color=yellow" />
</a>&nbsp;&nbsp;
<a href="https://github.com/card-monkey/card-monkey-BE">
  <img src="https://img.shields.io/badge/팀레포-181717?style=for-the-badge&logo=github&logoColor=white" />
</a>&nbsp;&nbsp;
<a href="https://docs.google.com/spreadsheets/d/1IlOppfpjftCuGY9JWhR-0yXaApH4Vn1fDCZCYBKlSrs/edit#gid=1048947792
">
  <img src="https://img.shields.io/badge/WBS-34A853?style=for-the-badge&logo=Google Sheets&color=green" />
</a>&nbsp;&nbsp;
<a href="https://documenter.getpostman.com/view/25864684/2s93CEvwPg">
  <img src="https://img.shields.io/badge/Postman API-FF6C37?style=for-the-badge&logo=Postman&logoColor=white" />
</a>&nbsp;&nbsp;

<br><br>

# 2. 팀원 소개 & 역할 분담 👥

|김윤기👑|김우석|임재억|주찬혁|
|:---:|:---:|:---:|:---:|
|<a href="https://github.com/yunki-kim"><img src="https://avatars.githubusercontent.com/u/63786040?v=4" width=160/></a>|<a href="https://github.com/flimberkim"><img src="https://avatars.githubusercontent.com/u/113500922?v=4" width=160/></a>|<a href="https://github.com/lim950808"><img src="https://avatars.githubusercontent.com/u/90830299?v=4" width=160/></a>|<a href="https://github.com/crossbell8368"><img src="https://avatars.githubusercontent.com/u/50852143?v=4" width=160/></a>|
|<b>팀장</b><br>초기 Entity 구성<br>GitHub 관리<br>로그인 기능구현<br>'혜택'기능구현<br>리팩토리|초기 Entity 구성<br>'검색' 기능구현<br>리팩토링|배포서버(AWS EC2) 인프라 구성<br>DB(AWS RDS)서버 구성<br>회원관리 기능구현<br>'찜하기'기능구현<br>리팩토링|ERD 테이블구성<br>'신청하기'기능구현<br>리팩토링 & README|

<br><br>

# 4. 기술 스택 ⚙️
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">  <img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white">
<br><br>

# 5. 협업 방식 🤝
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/notion-fc9847?style=for-the-badge&logo=notion&logoColor=white">  <img src="https://img.shields.io/badge/slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">
- **Github**
  * Front & Back 합동 Organization 구축
  * 개별 Team Repository 생성 & 개발용 develop 브랜치 생성
  * Team Repository, 개발자별 fork 진행
  * issue에 개발기능 기재 & 개발완료 후 pull request 작성
  * 변경내용 확인 후 Team Repository merge 진행
- **Notion**
  * 팀원별 진행 과정을 공유 시 사용
- **Slack**
  * Front & Back 팀원간 간단한 질의/응답/요청 시 사용
  * pull request 알림

<br><br>

# 6. 구현 내용 🛠︎

<b>서버 인프라</b>
- AWS EC2 Server(Linux ver) : Ubuntu 20.04
- AWS RDS : MariaDB
- AWS Route53
<br>
  
<b>회원관리(JWT token)</b>
- Token 발급
  - 회원가입 후 JWT Token 발급
- Token 기능
  - Header에 토큰이 없는경우 서비스 접근불가
- Token 관리
  - 로그아웃하는 경우, Token Blacklist 에 등록
  - 동일 토큰으로 접근시 Access 불가능
<br>

<b>JPA활용 CRUD</b>
- Spring Data JPA 활용
- '회원가입', '카드신청', '찜하기', '혜택선택' 등 기능구현 시 JPA활용
- '혜택선택'의 경우, Entity 구조에 따른 JPA 활용제한으로 커스텀쿼리 적용
- 지연로딩, batch fetch size 적용 등으로 쿼리 최적화 적용
<br>

<b>CORS & HTTPS</b>
- CORS
  - Front와 협력간 Cross-Origin 문제발생
  - WebMVC 설정 & SecurityFilterChain 기능추가 & corsConfigure 설정으로 해결
  
- HTTPS
  - Front와의 통합배포 시, https로만 접근가능 이슈 확인
  - Domain 구매 & certbot을 활용한 SSL 인증서 발급, nginx를 통한 적용으로 http -> https 리다이렉트 적용
