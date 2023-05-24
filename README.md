# grade-study
최범균님 코딩 연습 따라하기

## 스팩
* Spring Boot 3.1.0
* mysql 8.0.32

## 내용
학생 승급 회원 승급

## 목적
* TDD 코딩을 배워보자
* 설계하는 방법에 대해서

## 정리 (요약)
Status: 프로그램(GradeAdvanceService) 에 상태를 표시.  
GradeAdvanceService: 학생 회원 승급을 담당.  
AdvanceApplier: 승급이 끝난 학생들을 실제로 적용.  
TargetGen: 타켓(학생)을 데이터베이스 불러옴. 
TargetExporter: 타켓을 저장하는 기능 (문제가 있을때 돌아가기 위해서).  
TargetImporter: 타텟을 파일에서 불러온다.  

## 메모
* 예외(Unchecked)를 사용하는 이유 
  * 스프링에 롤백을 활용하기 위해서 
  * 결과 코드를 추가하는 방식 고려 
* TDD 로 개발을 진행하는 방법
* DB 에 데이터와 검점을 할 때 사용할 수 있는 방법 (TargetsApplierTest.apply())
* `@Value` 사용 방법 (의존성 주입) (States.java)
* `GivenAndAssertHelper` 마틴 파울러 [ObjectMother](https://martinfowler.com/bliki/ObjectMother.html) 
* `com.spencerwi:Either` 라이브러리(함수형 프로그래밍에 자료구조?) 를 사용하여 리팩토링

## 동영상 링크
https://www.youtube.com/watch?v=7P1dJ-VoQb4&list=PLwouWTPuIjUg5gQBL9ajinVkcX4D8BAkI&index=3
