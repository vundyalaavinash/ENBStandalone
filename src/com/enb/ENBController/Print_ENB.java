package com.enb.ENBController;
import com.enb.Userinterface.Login;
//import com.enb.Userinterface.ViewEnb;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.StyledDocument;


public class Print_ENB  {

    public static void main(String ENB_name)throws Exception
    {
        
	try{
		String uname=Login.login_name.toLowerCase();
            //    String note =ExtractNotes.main(ENB_name);
                
		
                try {
                    DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        java.util.Date date = new java.util.Date();
        System.out.println(dateFormat.format(date));
com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);

String path = "./"+uname+"_"+ENB_name+".pdf";
System.out.println(path);
JFrame parentFrame = new JFrame();
 

JFileChooser fileChooser = new JFileChooser();
fileChooser.setDialogTitle("Specify a file to save");   
 
int userSelection = fileChooser.showSaveDialog(parentFrame);
 
if (userSelection == JFileChooser.APPROVE_OPTION)
{
    File fileToSave = fileChooser.getSelectedFile();
    PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileChooser.getSelectedFile()+".pdf"));
    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
}




Connection conn=null;
	String url="jdbc:mysql://localhost:3306/enggnotebook";
        Class.forName("com.mysql.jdbc.Driver");
        conn = (Connection) DriverManager.getConnection(url,"root","root");
	Statement stmt = null;
        stmt = (Statement) conn.createStatement();
//String path = "C:\\pdf\\"+uname+"_"+name+"_"+dateFormat.format(date)+".pdf";
PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(path));
document.open();
document.addAuthor(uname);
document.addSubject(ENB_name);
document.addCreator(uname);
document.addCreationDate();
document.addTitle(ENB_name);
String Title= "AUTHOR:"+uname+"\n"+"ENB-NAME:"+ENB_name+"\n"+  "DATE:"+dateFormat.format(date);
Paragraph title1 = new Paragraph(Title, 

   FontFactory.getFont(FontFactory.TIMES_BOLD, 
   
   16, Font.BOLDITALIC, new CMYKColor(0, 255, 255,17)));
   title1.setAlignment(Element.ALIGN_CENTER);
//Chapter chapter1 = new Chapter(title1, 1);
      
//chapter1.setNumberDepth(0);
   document.add(title1);
   
   
   
Paragraph title11 = new Paragraph("Notes", 

       FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, 
   
       new CMYKColor(0, 0, 0,100)));
 document.add(title11);
 StyledDocument o=(StyledDocument)ENBCOntroller.deSerializeJavaObjectFromDB(ENB_name);
//System.out.print(o.getText(0,o.getLength()));
Paragraph title111 = new Paragraph(o.getText(0,o.getLength()));

  document.add(title111);              
                
Paragraph title12 = new Paragraph("Deliverables", 

       FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, 
   
       new CMYKColor(0, 0, 0,100)));
                Chapter chapter2 = new Chapter(title12,2);
      
       chapter2.setNumberDepth(0);
   
       Section section2 = chapter2.addSection(title12);
PdfPTable t = new PdfPTable(5);

       t.setSpacingBefore(25);
      
       t.setSpacingAfter(25);
      
       PdfPCell c1 = new PdfPCell(new Phrase("Deliverables"));  
      
       t.addCell(c1);
      
       PdfPCell c2 = new PdfPCell(new Phrase("Plan to accomplish"));
      
       t.addCell(c2);
      
       PdfPCell c3 = new PdfPCell(new Phrase("actually_accomplished"));
      
       t.addCell(c3);
       PdfPCell c4 = new PdfPCell(new Phrase("Size"));
      
       t.addCell(c4);
       PdfPCell c5 = new PdfPCell(new Phrase("Effort"));
      
       t.addCell(c5);
       System.out.println("table");
       
	String sql= "SELECT * FROM deliverable WHERE enb_name='"+ENB_name+"'";
        ResultSet rs1= null;
        rs1 = (ResultSet) stmt.executeQuery(sql);
        
                while(rs1.next())
                    {
                        t.addCell(rs1.getString("deliverable"));
                        
                       t.addCell(rs1.getString("plan_to_accomplish"));
                        
                      t.addCell(rs1.getString("actually_accomplished"));
                        
                      t.addCell(rs1.getString("size"));
                        
                    t.addCell(rs1.getString("effort"));
                    }
                System.out.println("Section");
                section2.add(t);
                document.add(chapter2);

                
                
 //plan
 Paragraph title13 = new Paragraph("Plan", 

       FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, 
   
       new CMYKColor(0, 0, 0,100)));
                Chapter chapter3 = new Chapter(title13, 3);
      
       chapter2.setNumberDepth(0);
   
       Section section3 = chapter3.addSection(title13);
PdfPTable t_plan = new PdfPTable(2);

       t_plan.setSpacingBefore(25);
      
       t_plan.setSpacingAfter(25);
      
       PdfPCell p_c1 = new PdfPCell(new Phrase("Deliverable"));  
      
       t.addCell(p_c1);
      
      
       PdfPCell p_c2 = new PdfPCell(new Phrase("Plan_why"));
      
       t.addCell(p_c2);
      
        System.out.println("t.add cell is done");
       System.out.println("plan");
       sql= "SELECT * FROM plan WHERE enb_name='"+ENB_name+"'";
        ResultSet rs2= null;
        rs2 = (ResultSet) stmt.executeQuery(sql);
       
                while(rs2.next())
                    {
                        t_plan.addCell(rs2.getString("deliverable"));
                        
                       t_plan.addCell(rs2.getString("plan_why"));
                        
                      
                    }
                System.out.println("Section");
                section3.add(t_plan);
                document.add(chapter3);
                
                
 //reflection
   Paragraph title14 = new Paragraph("Reflection", 

       FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, 
   
       new CMYKColor(0, 0, 0,100)));
                Chapter chapter4 = new Chapter(title14, 4);
      
       chapter2.setNumberDepth(0);
   
       Section section4 = chapter4.addSection(title14);
PdfPTable t_re = new PdfPTable(2);

       t_re.setSpacingBefore(25);
      
       t_re.setSpacingAfter(25);
      
       PdfPCell re_c1 = new PdfPCell(new Phrase("Context"));  
      
       t.addCell(re_c1);
      
       PdfPCell re_c2 = new PdfPCell(new Phrase("Lesson Learned"));
      
       t.addCell(re_c2);
      
       
       System.out.println("reflection");
       sql= "SELECT * FROM reflection WHERE enb_name='"+ENB_name+"'";
        ResultSet rs3= null;
        rs3 = (ResultSet) stmt.executeQuery(sql);

                while(rs3.next())
                    {
                        t_re.addCell(rs3.getString("context"));
                        
                       t_re.addCell(rs3.getString("lesson"));
                        
                      
                    }
                System.out.println("Section");
                section4.add(t_re);
                document.add(chapter4);
                
//SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
//SAXmyHtmlHandler shh = new SAXmyHtmlHandler(document);
//document.setRole(PdfName.op);
document.close();
JOptionPane.showMessageDialog(null,"Your DELIVERABLES,PLAN AND REFLECTION ARE STORED IN"+path);
        
System.out.println("end");
} catch(Exception e) {
e.printStackTrace();
}
         
}
        catch(Exception e) {
e.printStackTrace();
}
}
}