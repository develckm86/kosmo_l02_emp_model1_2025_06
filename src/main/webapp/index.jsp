<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
    <h1>사원 관리 웹 앱 (MODEL1)</h1>
    <p>요청처리(Controller)와 db접속(Model)과 view 렌더링(View)이 한페이지에 존재함 => 재사용 불가</p>
    <hr>
    <h2>사원 리스트</h2>
    <p><a href="./emp/list.do">사원 리스트 조회</a></p>
    <hr>
    <h2>부서 리스트(과제)</h2>
    <p><a href="./dept/list.do">부서 리스트(부서번호 오름차순 정렬) 조회</a></p>
</body>
</html>