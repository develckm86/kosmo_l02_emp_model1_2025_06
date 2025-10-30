package com.smu.l02_emp_model1.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

@WebServlet("/emp/modify.do")
public class EmpModifyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //파라미터처리!!!
        // "" , "  " => null
        String empnoStr=req.getParameter("empno");
        empnoStr=(empnoStr.isBlank())?null:empnoStr;
        String ename=req.getParameter("ename");
        ename=(ename.isBlank())?null:ename;
        String job=req.getParameter("job");
        job=(job.isBlank())?null:job;
        String deptnoStr=req.getParameter("deptno");
        deptnoStr=(deptnoStr.isBlank())?null:deptnoStr;
        String salStr=req.getParameter("sal");
        salStr=((salStr.isBlank())?null:salStr);
        String commStr=req.getParameter("comm");
        commStr=(commStr.isBlank())?null:commStr;
        String hiredateStr=req.getParameter("hiredate");
        hiredateStr=(hiredateStr.isBlank())?null:hiredateStr;
        String mgrStr=req.getParameter("mgr");
        mgrStr=((mgrStr.isBlank())?null:mgrStr);

        Integer empno=null;
        Integer deptno=null;
        Double sal=null;
        Double comm=null;
        java.sql.Date hiredate=null;
        Integer mgr=null;
        try {

            if(empnoStr==null || ename ==null) throw new IllegalArgumentException("사번이나 이름은 꼭 와야합니다.");
            empno=Integer.parseInt(empnoStr);
            if(deptnoStr!=null) deptno=Integer.parseInt(deptnoStr);
            if(salStr!=null) sal=Double.parseDouble(salStr);
            if(commStr!=null) comm=Double.parseDouble(commStr);
            if(hiredateStr!=null) hiredate=java.sql.Date.valueOf(hiredateStr);
            if(mgrStr!=null) mgr=Integer.parseInt(mgrStr);

        }catch ( Exception e){ //와야할 파라미터가 안옴
            e.printStackTrace();
            resp.sendError(400);
            return;
        }
        String sql="UPDATE EMP SET ENAME=?, SAL=?, COMM=?, JOB=?, DEPTNO=?, HIREDATE=?, MGR=? WHERE EMPNO=?";
        String url="jdbc:oracle:thin:@//localhost:1521/XEPDB1";
        String user="scott";
        String password="tiger";
        try(Connection conn = DriverManager.getConnection(url,user,password);
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1,ename);
            pstmt.setObject(2,sal);
            pstmt.setObject(3,comm);
            pstmt.setString(4,job);
            pstmt.setObject(5,deptno);
            pstmt.setDate(6, hiredate);
            pstmt.setObject(7,mgr);
            pstmt.setInt(8,empno);
            int update=pstmt.executeUpdate();
            if(update==1){ //성공이면 상세
//                resp.getWriter().println("SUCCESS");
                resp.sendRedirect("./detail.do?empno="+empno);
            }else {//실패(삭제) => 사원 리스트로 이동
//                resp.getWriter().println("FAIL");
                resp.sendRedirect("./list.do");
            }
        }catch (Exception e){
            e.printStackTrace();
            resp.sendError(500);
            //수정중 서버오류
            //1. 500
            //2. 수정페이지로 이동 서버에 문제가 생겨서 다시 시도하세요!
            return;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8"); //쿼리스트링 인코딩(양식데이터 한글깨짐)
        //empno 없다. => 페이지동작 불능
        String empnoStr=req.getParameter("empno");
        Integer empno=null;
        try {
            empno=Integer.parseInt(empnoStr);
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (NumberFormatException e) { //empnoStr 이 null 이거나  수가아닌 데이터
            e.printStackTrace(); //로거 log4j
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);//400의 상수
            return; //doGet()함수 종료
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500서버오류
            return;
        }
        String sql="SELECT * FROM EMP WHERE EMPNO=?";
        String url="jdbc:oracle:thin:@//localhost:1521/XEPDB1";
        String user="scott";
        String password="tiger";
        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out=resp.getWriter();
        out.println("<h1>사원 수정 양식</h1>");
        out.println("<hr>");
        out.println("<form method='post' action='./modify.do'>");
        try (Connection conn= DriverManager.getConnection(url,user,password);
             PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setInt(1,empno);
            try (ResultSet rs=ps.executeQuery()){
                if(rs.next()){
                    String ename=rs.getString("ename");
                    String job=rs.getString("job");
                    Object deptno=rs.getObject("deptno");
                    Object mgr=rs.getObject("mgr");
                    Object sal=rs.getObject("sal");
                    Object comm=rs.getObject("comm");
                    java.sql.Date hiredate=rs.getDate("hiredate");
                    String hiredateStr=(hiredate!=null)? hiredate.toLocalDate().toString() :"";

                    out.println("<p><label> 사번 :  <input name='empno' value='"+empno+"' readonly ></label></p>");
                    out.println("<p><label> 이름 :  <input name='ename' value='"+ename+"' ></label></p>");
                    out.println("<p><label> 부서번호 :  <input name='deptno' value='"+(deptno!=null?deptno:"") +"' ></label></p>");
                    out.println("<p><label> 직책 :  <input name='job' value='"+(job!=null?job:"") +"' ></label></p>");
                    out.println("<p><label> 상사 :  <input name='mgr' value='"+(mgr!=null?mgr:"")+"' ></label></p>");
                    out.println("<p><label> 급여 :  <input name='sal' value='"+(sal!=null?sal:"") +"' ></label></p>");
                    out.println("<p><label> 커미션 :  <input name='comm' value='"+(comm!=null?comm:"") +"' ></label></p>");
                    out.println("<p><label> 입사일 :  <input name='hiredate' value='"+hiredateStr+"' type='date'></label></p>");
                    //value 년-월-일  2025-1-1(x) 2025-01-01(O)
                    out.println("<p>");
                    out.println("<button type='button' onclick='history.back()'>뒤로가기</button>");
                    //out.println("<button type='button' onclick='location.href=`./remove.do?empno="+empno+"`'>삭제</button>");
                    // a 태그 사용 권장? SEO : 검색엔진 봇에게 a 태그는 웹앱의 네비 역할 (버튼으로 a 태그를 대신하면 사이트 구조 파악이 힘들어짐)
                    out.println("<a href='./remove.do?empno="+empno+"'>삭제 </a>");
                    out.println("<button type='reset'>초기화</button>"); //form.reset()
                    out.println("<button type='submit'>제출(수정)</button>");

                    out.println("</p>");
                }else {
                    out.println("<h2>조회한 사원이 없습니다.</h2>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        out.println("</form>");

    }
}
