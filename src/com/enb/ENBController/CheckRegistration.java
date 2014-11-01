/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enb.ENBController;

import com.mysql.jdbc.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author rajasekhar
 */
public class CheckRegistration {
     public static int Check_username(ArrayList details) throws Exception
    {
        Connection conn=null;
	conn = DataBaseConnection.dbConnection();
	Statement stmt = null;
        stmt = (Statement) conn.createStatement();
	String sql;
        sql = "SELECT user_name FROM registrations";
        ResultSet rs = null;
        rs = (ResultSet) stmt.executeQuery(sql);
        ArrayList al=new ArrayList();
        while(rs.next()){
            al.add(rs.getString("user_name"));
        }
        
        if (al.contains(details.get(0).toString()) && al.size()>0){
            System.out.println("username already exists");
            JOptionPane.showMessageDialog(null,"UserName already Exists...Try with new one");
            return 0;
        }
        else
        {
            PreparedStatement ps=(PreparedStatement) conn.prepareStatement("Insert into registrations values(?,?,?,?,?,?,?)");
            ps.setString(1, details.get(0).toString());//user_name
	    ps.setString(2, details.get(1).toString());//full_name
	    ps.setString(3, details.get(2).toString());//password
	    ps.setString(4, details.get(3).toString());//email_id
	    ps.setString(5, details.get(4).toString());//gender
            ps.setString(6, details.get(5).toString());//security question
            ps.setString(7, details.get(6).toString());//security answer
            ps.execute();
            return 1;

        }
     
    }
}
