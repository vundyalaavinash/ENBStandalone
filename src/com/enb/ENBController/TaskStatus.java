package com.enb.ENBController;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TaskStatus {

	private String taskActivity;
	private String taskPlan;
	private String taskActual;
	private int size;
	private int effort;
	private String effortsUnits;
        
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
           // System.out.print(dtm.getValueAt(i,j));
           // System.out.print(" ");
        }
       // System.out.println();
    }  
    return tableData;
    
}
}