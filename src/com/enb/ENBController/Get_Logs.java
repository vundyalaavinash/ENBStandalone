/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enb.ENBController;

import com.enb.Userinterface.viewlogs;
import javax.swing.table.TableModel;

/**
 *
 * @author
 */

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.Blob;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
public class Get_Logs {
    
    
    public static TableModel get_logs(String username,String selectDate) throws Exception
    {
        Connection conn=null;
	String url="jdbc:mysql://localhost:3306/enggnotebook";
        Class.forName("com.mysql.jdbc.Driver");
        conn = (Connection) DriverManager.getConnection(url,"root","root");
	Statement stmt = null;
        stmt = (Statement) conn.createStatement();
	String sql="";
        username=username.toLowerCase();
        System.out.println(username);
         final JPanel panel = new JPanel();
        
        Blob str=null;
        
        
        Vector v= new Vector();
        v.add("Date and Time");
        v.add("Description");
        System.out.println(selectDate);
        if(selectDate == null)
                {
                    sql = "SELECT count(*) FROM user_logs WHERE user_name='"+username.toLowerCase()+"'";
                    }
        else
        {
            sql = "SELECT count(*) FROM user_logs WHERE user_name='"+username.toLowerCase()+"' AND date_time LIKE'"+selectDate+"%'" ;
        }
        
        ResultSet rs1= null;
        rs1 = (ResultSet) stmt.executeQuery(sql);
        int count=0;
        while(rs1.next())
        {
            count=rs1.getInt(1);
           
        }
        System.out.println(count);
        
        JTable jt=new JTable();
        TableModel tm=new DefaultTableModel(v,count);
        if(count==0)
        {
            JOptionPane.showMessageDialog(null, " No Logs for the selcted date");
            //tm=Get_Logs.get_logs(username, null);
        }
        else
        {
        int row=0;int col=0;
        if(selectDate==null)
        {
            sql = "SELECT * FROM user_logs WHERE user_name='"+username.toLowerCase()+"'";
        }
        else
        {
           sql= "SELECT * FROM user_logs WHERE user_name='"+username.toLowerCase()+"' AND date_time LIKE'"+selectDate+"%'" ;
        
        }
        
        ResultSet rs = null;
        rs = (ResultSet) stmt.executeQuery(sql);
        while(rs.next())
        {
            String date=rs.getString("date_time");
            tm.setValueAt(date, row, col);
            System.out.println(row+"  "+ col +"  "+ date);

            col++;
                    
            String desc=rs.getString("description");
            tm.setValueAt(desc, row, col);
            System.out.println(row+"  "+ col +"  "+ desc);
            col=0;
            
            row++;
            
        }
        
        jt.setModel(tm);
        }
        
        return tm;
    }
}
