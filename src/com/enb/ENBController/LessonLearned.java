package com.enb.ENBController;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.ResultSet;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class LessonLearned {

	private String theLesson;
public static Object[][] getTableData (JTable jTable1)
{
    DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
    int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
    Object[][] tableData = new Object[nRow][nCol];
    for (int i = 0 ; i < nRow ; i++)
    { 
        for (int j = 0 ; j < nCol ; j++)
        {
            tableData[i][j] = dtm.getValueAt(i,j);        // TODO add your handling code here:
            //System.out.print(dtm.getValueAt(i,j));
          //  System.out.print(" ");
        }
      //  System.out.println();
    }  
    return tableData;
    
}
public static JTable getRefelction(String ENB_name,JTable table)throws Exception 
    {
        
        
        DefaultTableModel model;
       // jt.setModel(model);
        Connection conn=(Connection) DataBaseConnection.dbConnection();
	
	Statement stmt = null;
        stmt = (Statement) conn.createStatement();
	String sql;
        sql = "SELECT * FROM reflection WHERE enb_name='"+ENB_name+"'";
        ResultSet rs = null;
        rs = (ResultSet) stmt.executeQuery(sql);
        int i=0;
       int row=0;
       
       System.out.println("****"+table.getColumnName(0));
       while(rs.next()){
           
                       System.out.println(rs.getString(2));
                      
                          
                          try{
                              
                         table.getModel().setValueAt(rs.getString(2), row, 1);
                         table.getModel().setValueAt(rs.getString(3), row, 2); 
                         row++;                          }
                          catch(Exception e)
                          {
                              
                          }
       }
        
        return table;
        
    }
}