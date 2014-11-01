/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enb.ENBController;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author rajasekhar
 */
public class Add_to_Log {
     
    public static void main(String user, String desc)throws Exception
    {
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter=  new SimpleDateFormat("yyyy/MMM/dd");
        String date = formatter.format(currentDate.getTime());
        SimpleDateFormat formatter1=  new SimpleDateFormat("hh:mm:ss:ms");
        String time = formatter1.format(currentDate.getTime());
        System.out.println("user\t"+user);
        System.out.println("date\t"+date);
        System.out.println("time\t"+time);
        System.out.println("desc\t"+desc);
        System.out.println("***************************************");
        Connection conn=null;
	String url="jdbc:mysql://localhost:3306/enggnotebook";
        Class.forName("com.mysql.jdbc.Driver");
        conn = (Connection) DriverManager.getConnection(url,"root","root");
	
	String sql;
        
        PreparedStatement ps=(PreparedStatement) conn.prepareStatement("Insert into user_logs values(?,?,?)");
            ps.setString(1, user);//user_name
	    ps.setString(2, date+" "+time);//full_name
	    ps.setString(3, desc);//password
	    ps.execute();
    }
                
             
    
}
