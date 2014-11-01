/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enb.ENBController;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 *
 * @author jc
 */
public class ForgotPasswordConroller {
    public static String forgot(String username, String email)throws Exception
    {
        Connection conn=null;
        conn=(Connection) DataBaseConnection.dbConnection();
	
	Statement stmt = null;
        stmt = (Statement) conn.createStatement();
	String sql;
        sql = "SELECT * FROM registrations WHERE user_name='"+username+"' AND emai_id='"+email+"'";
        ResultSet rs = null;
        rs = (ResultSet) stmt.executeQuery(sql);
        String passw=null;
        while(rs.next())
        {
            passw=rs.getString("password");
        }
        System.out.println("Test pass="+passw);
        return passw;
        
    }
    
}
