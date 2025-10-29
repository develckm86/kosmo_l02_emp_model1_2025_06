package com.smu.l02_emp_model1.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/emp/detail.do")
public class EmpDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //서블릿에서 제일 먼저해야하는것
        String empnoStr=req.getParameter("empno");
        //empno 가 없다는 것은 요청을 잘못했다. => 400
        try {
            int empno=Integer.parseInt(empnoStr);
            String sql="SELECT * FROM EMP WHERE empno=?";
            String url="jdbc:oracle:thin:@//localhost:1521/XEPDB1";
            String user="scott";
            String pw="tiger";
            Class.forName("oracle.jdbc.OracleDriver");
            try(Connection conn= DriverManager.getConnection(url,user,pw);
                PreparedStatement ps=conn.prepareStatement(sql))
            {
                ps.setInt(1,empno);
                try(ResultSet rs=ps.executeQuery())
                {
                    resp.setCharacterEncoding("UTF-8");
                    resp.setContentType("text/html;charset=UTF-8");
                    PrintWriter out=resp.getWriter();
                    out.println("<h1>사원 상세 페이지</h1>");
                    out.println("<hr>");
                    if(rs.next()){
                        //상세내역 출력
                        out.println("<p>사번(empno) :"+rs.getInt("empno")+"</p>");
                        out.println("<p>이름 :"+rs.getString("ename")+"</p>");
                        out.println("<p>직책 :"+rs.getString("job")+"</p>");
                        out.println("<p>입사일 :"+rs.getString("hiredate")+"</p>");
                        out.println("<p>상사의 사번 :"+rs.getObject("mgr")+"</p>");
                        out.println("<p>부서번호 :"+rs.getObject("deptno")+"</p>");
                        out.println("<p>급여 :"+rs.getObject("sal")+"</p>");
                        out.println("<p>커미션 :"+rs.getObject("comm")+"</p>");
                        out.println("<p><a href='./modify.do?empno="+rs.getInt("empno")+"'>사원 수정(사원상세와 똑같은데 html만 form)</a></p>");


                    }else{
                        //데이터가 없음  listlink
                        out.println("<p>데이터 없음(이미 삭제된 사원)</p>");
                        out.println("<p><a href='./list.do'>사원 리스트</a></p>");
                    }
                }
            }

        } catch (NumberFormatException e) { //우리가 원하는 파라미터가 아니다.
            e.printStackTrace();
            resp.sendError(400);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500); //서버오류(개발자 잘못, 서버문제)
        }
    }
}
