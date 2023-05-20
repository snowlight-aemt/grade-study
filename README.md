# grade-study
최범균님 코딩 연습 따라하기

## 내용
학생 승급 회원 승급

## 목적
* TDD 코딩을 배워보자
* 설계하는 방법에 대해서

## 정리 (요약)
Status: 프로그램(GradeAdvanceService) 에 상태를 표시.  
GradeAdvanceService: 학생 회원 승급을 담당.  
AdvanceApplier: 승급이 끝난 학생들을 실제로 적용.  
TargetGen: 타켓(학생)을 불러옴.  
TargetExporter: 타켓을 저장하는 기능 (문제가 있을때 돌아가기 위해서). 

## 메모
* 예외(Unchecked)를 사용하는 이유 
  * 스프링에 롤백을 활용하기 위해서 
  * 결과 코드를 추가하는 방식 고려 

## 동영상 링크
https://www.youtube.com/watch?v=7P1dJ-VoQb4&list=PLwouWTPuIjUg5gQBL9ajinVkcX4D8BAkI&index=3
