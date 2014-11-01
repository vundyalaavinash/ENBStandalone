package com.enb.ENBController;


import com.enb.Userinterface.Home;
import com.mysql.jdbc.PreparedStatement;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class ENBCOntroller {

	private ENBRepository theENBs;
	private ENB currentENB;
        private static final String SQL_DESERIALIZE_OBJECT = "SELECT notes FROM enb_notes WHERE enb_name = ?";
         
	public void ENBController() {
		// TODO - implement ENBCOntroller.ENBController
		throw new UnsupportedOperationException();
	}

	public void addENB() {
		// TODO - implement ENBCOntroller.addENB
		throw new UnsupportedOperationException();
	}

public static String[] selectENB(String project,String user) throws ClassNotFoundException, SQLException {
        Connection conn=DataBaseConnection.dbConnection();
	Statement stmt = null;
        stmt = (Statement) conn.createStatement();
	String sql;
        ArrayList d=new ArrayList();
                System.out.println(project);

        sql = "SELECT * FROM enbdetails WHERE projectname='"+project+"' and username='"+user+"'";
        ResultSet rs = null;
        rs = (ResultSet) stmt.executeQuery(sql);
        
        
        while(rs.next()){
            d.add(rs.getString("enb_name"));
                      
        }
         String[] files;
        if(d.size()==0)
        {
            files= new String[]{""};
        }
        else{
        files= new String[d.size()];
       // System.out.println("arry size"+d.size());
        for(int i=0;i<d.size();i++)
        {
            files[i]=d.get(i).toString();
           // System.out.println(d.get(i));
             
        }
        }
        return files;
        
}



public static String[] getProjects(String user) throws ClassNotFoundException, SQLException {
        Connection conn=DataBaseConnection.dbConnection();
	Statement stmt = null;
        stmt = (Statement) conn.createStatement();
	String sql;
        ArrayList d=new ArrayList();
                System.out.println(user);

        sql = "SELECT distinct projectname FROM enbdetails WHERE username='"+user+"'";
        ResultSet rs = null;
        rs = (ResultSet) stmt.executeQuery(sql);
        
        
        while(rs.next()){
            d.add(rs.getString("projectname"));
                      
        }
         String[] files;
        if(d.size()==0)
        {
            files= new String[]{""};
        }
        else{
        files= new String[d.size()];
       // System.out.println("arry size"+d.size());
        for(int i=0;i<d.size();i++)
        {
            files[i]=d.get(i).toString();
           // System.out.println(d.get(i));
             
        }
        }
        return files;
        
}


	public void displayENB() {
		// TODO - implement ENBCOntroller.displayENB
		throw new UnsupportedOperationException();
	}

	public void addENBPage() {
		// TODO - implement ENBCOntroller.addENBPage
		throw new UnsupportedOperationException();
	}

	public void selectENBPage() {
		// TODO - implement ENBCOntroller.selectENBPage
		throw new UnsupportedOperationException();
	}

	public void displayENBPage() {
		// TODO - implement ENBCOntroller.displayENBPage
		throw new UnsupportedOperationException();
	}

	public void addNote(String enbname,StyledDocument doc) throws SQLException, ClassNotFoundException, BadLocationException {
		System.out.println("addnote clicked");
                String sTimeStamp = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                doc.insertString(doc.getEndPosition().getOffset(), "\n--Last Saved--" + sTimeStamp, null);
               Connection conn=DataBaseConnection.dbConnection();
	Statement stmt = null; 
        stmt = (Statement) conn.createStatement();
	String sql;
                sql = "DELETE FROM enb_notes WHERE enb_name='"+enbname+"'";
        stmt.executeUpdate(sql);
        PreparedStatement ps1=(PreparedStatement) conn.prepareStatement("Insert into enb_notes values(?,?)");
        ps1.setString(1, enbname);//enb_name
        ps1.setObject(2, doc);
       // ps1.setString(2, notes);//notes
        ps1.execute();
        System.out.println("In Save Notes java");
        JOptionPane.showMessageDialog(null,"saved successfully");
		//throw new UnsupportedOperationException();
	}
public static Object deSerializeJavaObjectFromDB(String str5) throws SQLException, IOException,
      ClassNotFoundException {
            Connection conn=DataBaseConnection.dbConnection();
         byte[] buf=null;
       
         PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL_DESERIALIZE_OBJECT);
        // System.out.println("asdfasdfasd");
        //System.out.println(str5+"haha");
         if(str5!=null){
         pstmt.setString(1, str5);

        ResultSet rs = (ResultSet) pstmt.executeQuery();
        rs.next();
        buf = rs.getBytes(1);rs.close();}
    ObjectInputStream objectIn = null;
    if (buf != null)
      objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
    
    pstmt.close();
    Object deSerializedObject = objectIn.readObject();
   
   /* System.out.println("Java object de-serialized from database. Object: "
        + deSerializedObject + " Classname: "
        + deSerializedObject.getClass().getName());
    System.out.println(deSerializedObject);*/
    return deSerializedObject;
    }
 public static int createEnbName(String ENBname, String Project, String Duration,String Description,String DateNow) throws ClassNotFoundException, SQLException
        {
        Connection conn=DataBaseConnection.dbConnection();
	Statement stmt = null;
        stmt = (Statement) conn.createStatement();
        String sql;
        String user= Home.username;
        sql = "SELECT * FROM enbdetails WHERE username='"+user+"'";
        ResultSet rs = null;
        rs = (ResultSet) stmt.executeQuery(sql);
        ArrayList existing=new ArrayList();
        while(rs.next()){
            existing.add(rs.getString("enb_name"));
                      
        }
        int flag=0;
        for (int i=0; i< existing.size();i++)
        {
            if(existing.get(i).equals(ENBname))
            {
                //enb with same name already exists
                JOptionPane.showMessageDialog(null,"ENB with same name exists. Try new one");
                flag=1;
                return 1;
            }
                       
        }
        if(flag==0){
               PreparedStatement ps=(PreparedStatement) conn.prepareStatement("Insert into enbdetails values(?,?,?,?,?)");
                ps.setString(1, ENBname);//enb_name
                ps.setString(2, user);//username
                ps.setString(3, Duration);//duration
                ps.setString(4, Description);//description
                ps.setString(5, Project);
                ps.execute();
                
                
                
            }
         return 0;
    }

        
           
            
        
	public void selectNote() {
		// TODO - implement ENBCOntroller.selectNote
                System.out.println("selectnote/view note clicked");
		throw new UnsupportedOperationException();
	}

	public void editNote() {
		  System.out.println("editnote clicked");
		throw new UnsupportedOperationException();
	}

	public void addDeliverableStatus(String ENB_Name,JTable table) throws ClassNotFoundException, SQLException {
		// TODO - implement ENBCOntroller.addDeliverableStatus
            System.out.println("---hello-----");
             int j=table.getRowCount();
        
        
                com.mysql.jdbc.Connection conn=null;
                conn=(com.mysql.jdbc.Connection) DataBaseConnection.dbConnection();
                
                com.mysql.jdbc.Statement stmt = null;
                stmt = (com.mysql.jdbc.Statement) conn.createStatement();
                String sql;
                sql="DELETE from deliverable WHERE enb_name='"+ENB_Name+"'";
                stmt.executeUpdate(sql);
                System.out.println("rows-->"+j);
        for(int i=0;i<j;i++)
          {
               if(table.getModel().getValueAt(i,0)==null ){
                   //pw.println(" -- -- -- -- ");
                  // JOptionPane.showMessageDialog(null,"Empty Deliverables");
                   //System.out.println(table.getModel().getValueAt(i,3));
                   System.out.println("I am breaking");
                   break;
               }
               else{
                   
               String enb_name=ENB_Name;
               String deliverable=(String) table.getModel().getValueAt(i,0);
               String plan_to_accomplish=(String) table.getModel().getValueAt(i,1);
               String actually_accomplished=(String) table.getModel().getValueAt(i,2);
               String size=(String) table.getModel().getValueAt(i,3);
               String effort=(String) table.getModel().getValueAt(i,4);
               System.out.println("enb name:"+ enb_name);
               System.out.println("enb deliverable:"+ deliverable);
               System.out.println("plan_to_accomplish:"+ plan_to_accomplish);
               System.out.println("actually_accomplished "+actually_accomplished);
               System.out.println(size);
               System.out.println(effort);
               System.out.println("---------------------");
               
                    PreparedStatement ps=(PreparedStatement) conn.prepareStatement("Insert into deliverable values(?,?,?,?,?,?)");
                    ps.setString(1, enb_name);//enb_name
                    ps.setString(2, deliverable);//deliverable
                    ps.setString(3, plan_to_accomplish);//plan
                    ps.setString(4, actually_accomplished);//actual
                    ps.setString(5, size);//size
                    ps.setString(6, effort);//effort
                    ps.execute();
                    
                
                   
                }
               
               //Add_to_Log.main(MainStarts.login_name, "Deliverable Status Updated in ENB: "+ENB_Name);
          }
       
		//throw new UnsupportedOperationException();
	}

	public void selectDeliverableStatus() {
		// TODO - implement ENBCOntroller.selectDeliverableStatus
		throw new UnsupportedOperationException();
	}

	public void editDeliverableStatus() {
		// TODO - implement ENBCOntroller.editDeliverableStatus
		throw new UnsupportedOperationException();
	}

	public void addPlan() {
		// TODO - implement ENBCOntroller.addPlan
		throw new UnsupportedOperationException();
	}

	/*public void selectPlan() {
		// TODO - implement ENBCOntroller.selectPlan
		throw new UnsupportedOperationException();
	}*/

	public void editPlan() {
		// TODO - implement ENBCOntroller.editPlan
		throw new UnsupportedOperationException();
	}

	public void addLessonLearned() {
		// TODO - implement ENBCOntroller.addLessonLearned
		throw new UnsupportedOperationException();
	}

	public void editLessonLearned() {
		// TODO - implement ENBCOntroller.editLessonLearned
		throw new UnsupportedOperationException();
	}
	public  static JTable selectPlan(String ENB_name,JTable table)throws Exception  {
        // TODO - implement ENBCOntroller.selectPlan
         DefaultTableModel model;
       // jt.setModel(model);
        Connection conn=null;
    
        conn = DataBaseConnection.dbConnection();
    Statement stmt = null;
        stmt = (Statement) conn.createStatement();
    String sql;
        sql = "SELECT * FROM plan WHERE enb_name='"+ENB_name+"'";
        ResultSet rs = null;
        rs = (ResultSet) stmt.executeQuery(sql);
        int i=0;
       int row=0;
       
       System.out.println("****"+table.getColumnName(0));
       while(rs.next()){
           
                       System.out.println(rs.getString(2));
                      
                          
                          try{
                         //table.getModel().setValueAt(row+1, row, 0);     
                         table.getModel().setValueAt(rs.getString(2), row, 0);
                         table.getModel().setValueAt(rs.getString(3), row, 1); 
                         row++;                          }
                          catch(Exception e)
                          {
                              
                          }
       }
        
        return table;
        
    }
	public static void StoreReflections(String ENB_Name,JTable table) throws SQLException, ClassNotFoundException
{
    int j=table.getRowCount();
        
        
                com.mysql.jdbc.Connection conn=(com.mysql.jdbc.Connection) DataBaseConnection.dbConnection();
               
               
                com.mysql.jdbc.Statement stmt = null;
                stmt = (com.mysql.jdbc.Statement) conn.createStatement();
                String sql;
                sql="DELETE from reflection WHERE enb_name='"+ENB_Name+"'";
                stmt.executeUpdate(sql);
                
        for(int i=0;i<j;i++)
          {
               if(table.getModel().getValueAt(i,0)==null && table.getModel().getValueAt(i,1)==null){
                   //pw.println(" -- -- -- -- ");
                   //JOptionPane.showMessageDialog(null,"Empty Deliverables");
               }
               else{
                   
               String enb_name=ENB_Name;
               String context=(String) table.getModel().getValueAt(i,0);
               String lesson=(String) table.getModel().getValueAt(i,1);
               System.out.println("context: "+context);
               System.out.println("lesson: "+lesson);
                    PreparedStatement ps=(PreparedStatement) conn.prepareStatement("Insert into reflection values(?,?,?)");
                    ps.setString(1, enb_name);//enb_name
                    ps.setString(2, context);//deliverable
                    ps.setString(3, lesson);//plan
                    
                    ps.execute();

                
                   
                }
          }
        //Add_to_Log.main(MainStarts.login_name, "Added Reflection to ENB: "+ENB_Name);
}
public static JTable getDeliverable(String ENB_name,JTable table)throws Exception 
    {
        
        
        
   Object[] columnNames = {"Deliverable", "Plan_to_accomplish","actually_accomplished","size","effort"};
   Object[][] rowData={null,null,null,null,null};
    DefaultTableModel model;

    model = new DefaultTableModel(rowData, columnNames);
       // jt.setModel(model);
     int i=0;
       int row=0;
        com.mysql.jdbc.Connection conn=(com.mysql.jdbc.Connection) DataBaseConnection.dbConnection();
	
	com.mysql.jdbc.Statement stmt = null;
        stmt = (com.mysql.jdbc.Statement) conn.createStatement();
	String sql;
        sql = "SELECT * FROM deliverable WHERE enb_name='"+ENB_name+"'";
        ResultSet rs = null;
        rs = (ResultSet) stmt.executeQuery(sql);
       
       
       System.out.println("****"+table.getColumnName(0));
       while(rs.next()){
           
                       System.out.println(rs.getString(2));
                      
                          
                          try{
                        // table.getModel().setValueAt(row+1, row, 0);     
                         table.getModel().setValueAt(rs.getString(2), row, 0);
                         table.getModel().setValueAt(rs.getString(3), row, 1);
                         table.getModel().setValueAt(rs.getString(4), row, 2);
                         table.getModel().setValueAt(rs.getString(5), row, 3);
                         table.getModel().setValueAt(rs.getString(6), row, 4); 
                         row++;                          }
                          catch(Exception e)
                          {
                              
                          }
       }
        
        return table;
        
    }
public void addPlan(String ENB_name,JTable table) throws ClassNotFoundException, SQLException {
        // TODO - implement ENBCOntroller.addPlan
        System.out.println("addplan clicked");
        int j=table.getRowCount();
        
        
                Connection conn=DataBaseConnection.dbConnection();
                
                Statement stmt = null;
                stmt = (Statement) conn.createStatement();
                String sql;
                sql="DELETE from plan WHERE enb_name='"+ENB_name+"'";
                stmt.executeUpdate(sql);
                
        for(int i=0;i<j;i++)
          {
               if(table.getModel().getValueAt(i,0)==null && table.getModel().getValueAt(i,1)==null){
                   //pw.println(" -- -- -- -- ");
                 //  JOptionPane.showMessageDialog(null,"Empty Deliverables");
               }
               else{
                   try{
               String enb_name=ENB_name;
               String deliverable=(String) table.getModel().getValueAt(i,0);
               String plan_why=(String) table.getModel().getValueAt(i,1);
                    PreparedStatement ps=(PreparedStatement) conn.prepareStatement("Insert into plan values(?,?,?)");
                    ps.setString(1, enb_name);//enb_name
                    ps.setString(2, deliverable);//deliverable
                    ps.setString(3, plan_why);//plan
                    ps.execute();

                   }
                   catch(Exception e)
                   {
                       
                   }
                   
                }
          }
       // Add_to_Log.main(MainStarts.login_name, "Plan Updated for ENB: "+ENB_Name);
            
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
                          // table.getModel().setValueAt(row+1, row, 0);   
                         table.getModel().setValueAt(rs.getString(2), row, 0);
                         table.getModel().setValueAt(rs.getString(3), row, 1); 
                         row++;                          }
                          catch(Exception e)
                          {
                              
                          }
       }
        
        return table;
        
    }

}