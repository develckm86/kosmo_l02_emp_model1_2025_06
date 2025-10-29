package com.smu.l02_emp_model1.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/emp/list.do")
public class EmpListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        req.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();
        out.println("<h1>유저 리스트 model1</h1>");
        out.println("<hr>");
        out.println("<table border='1'>");
//       empno, ename, hiredate, sal, jop
        out.println("<tr><th>사번</th><th>이름</th><th>직책</th><th>입사일</th><th>급여</th><th>상세</th></tr>");
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            resp.sendError(500);
        }
        String sql="SELECT * FROM EMP ORDER BY empno DESC";
        try(Connection conn= DriverManager.getConnection(
                "jdbc:oracle:thin:@//localhost:1521/XEPDB1","scott","tiger"))
        {
            try(Statement st=conn.createStatement();
                ResultSet rs=st.executeQuery(sql);){
                while (rs.next()){
                      out.println("<tr>");
                      out.println("<td>"+rs.getInt("empno")+"</td>");
                      out.println("<td>"+rs.getString("ename")+"</td>");
                      out.println("<td>"+rs.getString("job")+"</td>");
                      out.println("<td>"+rs.getString("hiredate")+"</td>");
                      out.println("<td>"+rs.getDouble("sal")+"</td>");
                      //  /emp/list.do => /emp/detail.do?empno=...
                      out.println("<td><a href='./detail.do?empno="+rs.getInt("empno")+"'>상세</a></td>");
                      out.println("</tr>");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            //클라이언트(브라우저)=>유저리스트 조회 (오류); 500
            resp.sendError(500); //서버오류!
        }


        out.println("</table>");

    }
}
