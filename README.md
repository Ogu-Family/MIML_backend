# MIML_backend

> Movie Is My Life, 롯데시네마 영화 예매 클론 프로젝트입니다!

----

## 프로젝트 목표

- Restful API 설계 경험
- Agile 방식의 협업 경험
- 상용 서비스의 도메인 분석 및 구현 경험
- 동시성 이슈 해결 및 성능 최적화

-----

## 프로젝트 문서

- [노션]()
- [API 명세서]()

----

## Member

|  Name   |             [최정은](https://github.com/JeongeunChoi)              |             [조인수](https://github.com/ZZAMBAs)              |             [김남규](https://github.com/GiHoo)              |
|:-------:|:---------------------------------------------------------------:|:----------------------------------------------------------:|:--------------------------------------------------------:|
| Profile | <img width="100px" src="https://github.com/JeongeunChoi.png" /> | <img width="100px" src="https://github.com/ZZAMBAs.png" /> | <img width="100px" src="https://github.com/GiHoo.png" /> |
|  Role   |                          Scrum Master                           |                       Product Owner                        |                        Developer                         |

----

## 🛠 기술 스택

<p>
<img src="https://img.shields.io/badge/java 17-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white"> 
</p>

<p>
<img src="https://img.shields.io/badge/spring 6.1.1-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/springboot 3.2.0-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/spring data jpa-F7DF1E?style=for-the-badge"> 
</p>

<p>
<img src="https://img.shields.io/badge/mysql 8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
</p>

## ✏️ 문서/협업

<p>
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">
<img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white">
<img src="https://img.shields.io/badge/intellijidea-000000?style=for-the-badge&logo=intellijidea&logoColor=white">
<img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">
<img src="https://img.shields.io/badge/spring rest docs-6DB33F?style=for-the-badge">
</p>

----

## ERD

![스크린샷 2024-01-18 11.53.34.png](..%2F..%2F..%2F..%2F..%2F..%2Fvar%2Ffolders%2Fxb%2Fnmk53nx90jd9bn_lsgqv_qsm0000gn%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_zmF8vi%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-01-18%2011.53.34.png)

[ERD 링크](https://www.erdcloud.com/d/TYGrwdn9Kj5wnb4RQ)

----

## Convention

### Branch 전략

![branches.svg](..%2F..%2F..%2F..%2FDownloads%2Fbranches.svg)

- Main : 최종 버전 브랜치
- Develop : Merge 전용 브랜치
- Feature : 하나의 이슈마다 하나의 브랜치 생성(feature/[이슈번호])

### Commit Convention

| Tag Name | Description                                                    |
|----------|----------------------------------------------------------------|
| feat     | 새로운 기능을 추가                                                     |
| refactor | 프로덕션 코드 리팩토링                                                   |
| docs     | 문서 수정                                                          |
| test     | 테스트 코드, 리펙토링 테스트 코드 추가, Production Code(실제로 사용하는 코드) 변경 없음     |
| chore    | 빌드 업무 수정, 패키지 매니저 수정, 패키지 관리자 구성 등 업데이트, Production Code 변경 없음 |
| style    | 코드 포맷 변경, 세미 콜론 누락, 코드 수정이 없는 경우                               |
