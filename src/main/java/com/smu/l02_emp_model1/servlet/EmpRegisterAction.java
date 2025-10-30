package com.smu.l02_emp_model1.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

@WebServlet("/emp/registerAction.do")
public class EmpRegisterAction extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8"); //파라미터 인코딩
        String empnoStr=req.getParameter("empno");
        String ename=req.getParameter("ename");
        String job=req.getParameter("job");
        String mgrStr=req.getParameter("mgr");
        String hiredateStr=req.getParameter("hiredate");
        String salStr=req.getParameter("sal");
        String commStr=req.getParameter("comm");
        String deptnoStr=req.getParameter("deptno");
        try{
            int empno=Integer.parseInt(empnoStr);
            ename=(ename!=null && !ename.isBlank())?ename:null; //""이거나 " "이면 null
            job=(job!=null && !job.isBlank())?job:null;
            Integer mgr=(mgrStr!=null && !mgrStr.isBlank())?Integer.parseInt(mgrStr):null;
            Integer deptno=(deptnoStr!=null && !deptnoStr.isBlank())?Integer.parseInt(deptnoStr):null;
            Double sal=(salStr!=null && !salStr.isBlank())?Double.parseDouble(salStr):null;
            Double comm=(commStr!=null && !commStr.isBlank())?Double.parseDouble(commStr):null;
            Date hiredate=(hiredateStr!=null && !hiredateStr.isBlank())? Date.valueOf(hiredateStr):null;

            Class.forName("oracle.jdbc.OracleDriver");
            String url="jdbc:oracle:thin:@//localhost:1521/XEPDB1";
            String user="scott";
            String pw="tiger";
            String sql="INSERT INTO EMP (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO) VALUES (?,?,?,?,?,?,?,?)";
            try (Connection conn= DriverManager.getConnection(url,user,pw);
                 PreparedStatement ps=conn.prepareStatement(sql);){
                ps.setInt(1, empno);
                ps.setString(2, ename);
                ps.setString(3, job);
                ps.setObject(4, mgr);
                ps.setDate(5, hiredate);
                ps.setObject(6, sal);
                ps.setObject(7, comm);
                ps.setObject(8, deptno);
                int insert=ps.executeUpdate();
                if(insert>0){//등록성공시 상세 or 리스트
                    resp.sendRedirect(req.getContextPath()+"/emp/detail.do?empno="+empno);
                }else { //등록 실패시 다시 등록폼 (대부분 오류지 0이 발생할 가능성은 거의 없음)
                    resp.sendRedirect(req.getContextPath()+"/emp/register.do");
                }
            }
        }catch (IllegalArgumentException e){ //IllegalArgumentException>NumberFormatException 을 포함
            e.printStackTrace();//파라미터를 잘못보냄=>다시 등록 양식으로(400에러도 가능)
            resp.sendRedirect("./register.jsp?error=inValidParam");
            return;
        }catch ( ClassNotFoundException e){
            e.printStackTrace();
            resp.sendRedirect("./register.jsp?error=ClassNotFound");
        }catch (SQLException e){
            e.printStackTrace();
            resp.sendRedirect("./register.jsp?error="+e.getErrorCode());
            //SQLException.getErrorCode() : 쿼리 실행시 발생할 수 있는 에러의 상태 번호
            //1 :"(PK 제약조건)사번이 이미 존재합니다.";
            //1400 : "(NOT NULL 제약조건)필수 입력값이 누락되었습니다.";
            //2291 :"(FK 제약조건)부서번호가 존재하지 않습니다.";
            //2292 : "(FK 제약조건)참조하는 상사부서라 삭제할 수 없습니다.";
            //12899 : "입력값이 컬럼의 최대 길이를 초과했습니다.";
            //(12514,12541,122154) : "데이터베이스를 접속할 수 없습니다. 다시 시도하세요.";
        }
    }
}
