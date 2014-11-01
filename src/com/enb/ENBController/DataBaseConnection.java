/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enb.ENBController;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author jc
 */
public class DataBaseConnection {
    public static java.sql.Connection dbConnection() throws ClassNotFoundException, SQLException
    {        
        java.sql.Connection conn=null;
		String url="jdbc:mysql://localhost:3306/enggnotebook";
        Class.forName("com.mysql.jdbc.Driver");
        conn = (java.sql.Connection) DriverManager.getConnection(url,"root","root");
        return conn;
    }
}
