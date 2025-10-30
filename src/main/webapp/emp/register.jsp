<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>사원 등록 양식</title>
</head>
<body>
    <h1>사원 등록 양식</h1>
    <hr>
    <form name="registerForm" action="registerAction.do" method="post">
        <p><label>사번 : <input type="text" name="empno" value="9999"></label></p>
        <p><label>이름 : <input type="text" name="ename" value="테스터"></label></p>
        <p><label>직책 : <input type="text" name="job" value="back"></label></p>
        <p><label>급여 : <input type="text" name="sal" value="9999.99"></label></p>
        <p><label>상여급 : <input type="text" name="comm" value="99.99"></label></p>
        <p><label>부서번호 : <input type="text" name="deptno" value="10"></label></p>
        <p><label>상사사번 : <input type="text" name="mgr" value="7788"></label></p>
        <p><label>입사일 : <input type="date" name="hiredate" value="2025-01-01"></label></p>
        <p>
            <button type="reset">초기화</button>
            <button>제출</button>
        </p>
    </form>

</body>
</html>
