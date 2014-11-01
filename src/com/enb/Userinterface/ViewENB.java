package com.enb.Userinterface;

import com.enb.ENBController.DataBaseConnection;
import com.enb.ENBController.ENBCOntroller;
import com.enb.ENBController.Print_ENB;
import com.enb.ENBController.SendFileEmail;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Toolkit;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mBk'
 */
public class ViewENB extends javax.swing.JFrame {

    
private static String username;
    private static String enbname;
    private String[] args;
    int inch = Toolkit.getDefaultToolkit().getScreenResolution();

  float pixelToPoint = (float) 72 / (float) inch;
    /**
     * Creates new form ViewENB
     */
    public ViewENB(String user,String enb) throws Exception {
        this.username=user;
        this.enbname=enb;
        initComponents();
        jButton7.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        paintToPDF(jTextPane1);
      }
    });
        jTextPane1.setEditable(false);
        try {
            
            jLabel2.setText("Welcome "+username );
            jLabel7.setText("ENB name: "+enbname );
            jTextPane1.setStyledDocument((StyledDocument) ENBCOntroller.deSerializeJavaObjectFromDB(enbname));
             this.jTable1=ENBCOntroller.getDeliverable(enbname,jTable1);
             this.jTable3=ENBCOntroller.getRefelction(enbname, jTable3);
            this.jTable2=ENBCOntroller.selectPlan(enbname, jTable2);
        } catch (SQLException ex) {
           // Logger.getLogger(ViewEnb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ViewENB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ViewENB.class.getName()).log(Level.SEVERE, null, ex);
        }
        Object data = getdata(enbname);
        if(data!=null)
        jTextPane1.setStyledDocument((StyledDocument)data );
        jTextPane1.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(final HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
                    EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            // TIP: Show hand cursor
                            SwingUtilities.getWindowAncestor(jTextPane1).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            // TIP: Show URL as the tooltip
                            //jEditorPane1.setToolTipText(e.getURL().toExternalForm());
                        }
                    });
                } else if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
                    EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            // Show default cursor
                            SwingUtilities.getWindowAncestor(jTextPane1).setCursor(Cursor.getDefaultCursor());

                            // Reset tooltip
                            //jEditorPane1.setToolTipText(null);
                        }
                    });
                } else if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    // TIP: Starting with JDK6 you can show the URL in desktop browser
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        } catch (Exception ex) {
                            Logger.getLogger(ViewENB.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        }
                    }
                    //System.out.println("Go to URL: " + e.getURL());
                }
            }
        });
        
        
    }

    
    public void paintToPDF(JTextPane ta) {
    try {
      ta.setBounds(0, 0, (int) convertToPixels(612 - 58), (int) convertToPixels(792 - 60));

      Document document = new Document();
      FileOutputStream fos = new FileOutputStream(enbname+".pdf");
      PdfWriter writer = PdfWriter.getInstance(document, fos);

      document.setPageSize(new com.lowagie.text.Rectangle(612, 792));
      document.open();
      
     
      PdfContentByte cb = writer.getDirectContent();

      cb.saveState();
      cb.concatCTM(1, 0, 0, 1, 0, 0);

      DefaultFontMapper mapper = new DefaultFontMapper();
      mapper.insertDirectory("c:/windows/fonts");

      Graphics2D g2 = cb.createGraphics(612, 792, mapper, true, .95f);

      AffineTransform at = new AffineTransform();
      at.translate(convertToPixels(20), convertToPixels(20));
      at.scale(pixelToPoint, pixelToPoint);

      g2.transform(at);

      g2.setColor(Color.WHITE);
      g2.fill(ta.getBounds());

      Rectangle alloc = getVisibleEditorRect(ta);
      ta.getUI().getRootView(ta).paint(g2, alloc);

      g2.setColor(Color.BLACK);
      g2.draw(ta.getBounds());

      g2.dispose();
      cb.restoreState();
      document.newPage();
      document.add(new Paragraph("Deliverables"));
      document.add(createDeliverables());
      document.newPage();
      document.add(new Paragraph("Lessons Learned"));
      document.add(createReflection());
      document.newPage();
      document.add(new Paragraph("Plan for next week"));
      document.add(createPlan());
      document.close();
      fos.flush();
      fos.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public float convertToPoints(int pixels) {
    return (float) (pixels * pixelToPoint);
  }

  public float convertToPixels(int points) {
    return (float) (points / pixelToPoint);
  }

  protected Rectangle getVisibleEditorRect(JTextPane ta) {
    Rectangle alloc = ta.getBounds();
    if ((alloc.width > 0) && (alloc.height > 0)) {
      alloc.x = alloc.y = 0;
      Insets insets = ta.getInsets();
      alloc.x += insets.left;
      alloc.y += insets.top;
      alloc.width -= insets.left + insets.right;
      alloc.height -= insets.top + insets.bottom;
      return alloc;
    }
    return null;
  }

    private PdfPTable createDeliverables() {
        try {
            // a table with three columns
             PdfPTable table = new PdfPTable(5);
             // the cell object
             PdfPCell cell;
             // we add a cell with colspan 3
             cell = new PdfPCell(new Phrase("Delivearables"));
             cell.setColspan(5);
             table.addCell(cell);
             // now we add a cell with rowspan 2
            // cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
            // cell.setRowspan(1);
            // table.addCell(cell);
             // we add the four remaining cells with addCell()
             com.mysql.jdbc.Connection conn=(com.mysql.jdbc.Connection) DataBaseConnection.dbConnection();
             
             com.mysql.jdbc.Statement stmt = null;
             stmt = (com.mysql.jdbc.Statement) conn.createStatement();
             String sql;
             ResultSet rs = null;
             sql = "SELECT * FROM deliverable WHERE enb_name='"+enbname+"'";
             rs = (ResultSet) stmt.executeQuery(sql);
              while(rs.next()){
             table.addCell(rs.getString(2));
             table.addCell(rs.getString(3));
             table.addCell(rs.getString(4));
             table.addCell(rs.getString(5));
             table.addCell(rs.getString(6));
              }
             return table;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ViewENB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ViewENB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
    


    private PdfPTable createReflection() {
        try {
            // a table with three columns
             PdfPTable table = new PdfPTable(2);
             // the cell object
             PdfPCell cell;
             // we add a cell with colspan 3
             cell = new PdfPCell(new Phrase("Reflections"));
             cell.setColspan(2);
             table.addCell(cell);
             // now we add a cell with rowspan 2
            // cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
            // cell.setRowspan(1);
            // table.addCell(cell);
             // we add the four remaining cells with addCell()
             com.mysql.jdbc.Connection conn=(com.mysql.jdbc.Connection) DataBaseConnection.dbConnection();
             
             com.mysql.jdbc.Statement stmt = null;
             stmt = (com.mysql.jdbc.Statement) conn.createStatement();
             String sql;
             ResultSet rs = null;
             sql = "SELECT * FROM reflection WHERE enb_name='"+enbname+"'";
             rs = (ResultSet) stmt.executeQuery(sql);
              while(rs.next()){
             table.addCell(rs.getString(2));
             table.addCell(rs.getString(3));
             
              }
             return table;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ViewENB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ViewENB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
    
    
    private PdfPTable createPlan() {
        try {
            // a table with three columns
             PdfPTable table = new PdfPTable(2);
             // the cell object
             PdfPCell cell;
             // we add a cell with colspan 3
             cell = new PdfPCell(new Phrase("Plans"));
             cell.setColspan(2);
             table.addCell(cell);
             // now we add a cell with rowspan 2
            // cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
            // cell.setRowspan(1);
            // table.addCell(cell);
             // we add the four remaining cells with addCell()
             com.mysql.jdbc.Connection conn=(com.mysql.jdbc.Connection) DataBaseConnection.dbConnection();
             
             com.mysql.jdbc.Statement stmt = null;
             stmt = (com.mysql.jdbc.Statement) conn.createStatement();
             String sql;
             ResultSet rs = null;
             sql = "SELECT * FROM plan WHERE enb_name='"+enbname+"'";
             rs = (ResultSet) stmt.executeQuery(sql);
              while(rs.next()){
             table.addCell(rs.getString(2));
             table.addCell(rs.getString(3));
            
              }
             return table;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ViewENB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ViewENB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }

class BoldAction extends StyledEditorKit.StyledTextAction {
  private static final long serialVersionUID = 9174670038684056758L;

  public BoldAction() {
    super("font-bold");
  }

  public String toString() {
    return "Bold";
  }

  public void actionPerformed(ActionEvent e) {
    JEditorPane editor = getEditor(e);
    if (editor != null) {
      StyledEditorKit kit = getStyledEditorKit(editor);
      MutableAttributeSet attr = kit.getInputAttributes();
      boolean bold = (StyleConstants.isBold(attr)) ? false : true;
      SimpleAttributeSet sas = new SimpleAttributeSet();
      StyleConstants.setBold(sas, bold);
      setCharacterAttributes(editor, sas, false);

    }
  }
}

class ItalicAction extends StyledEditorKit.StyledTextAction {

  private static final long serialVersionUID = -1428340091100055456L;

  public ItalicAction() {
    super("font-italic");
  }

  public String toString() {
    return "Italic";
  }

  public void actionPerformed(ActionEvent e) {
    JEditorPane editor = getEditor(e);
    if (editor != null) {
      StyledEditorKit kit = getStyledEditorKit(editor);
      MutableAttributeSet attr = kit.getInputAttributes();
      boolean italic = (StyleConstants.isItalic(attr)) ? false : true;
      SimpleAttributeSet sas = new SimpleAttributeSet();
      StyleConstants.setItalic(sas, italic);
      setCharacterAttributes(editor, sas, false);
    }
  }
}
    public static Object getdata(String name) throws ClassNotFoundException, SQLException, IOException{
        Object deSerializedObject = null;
        Connection conn=(Connection) DataBaseConnection.dbConnection();
        String SQL1 = "SELECT notes FROM enb_notes WHERE enb_name = '" +name + "'";
                PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL1);
                //pstmt.setString(1, (String) Data.get(1));
                System.out.println(SQL1);
                ResultSet rs = (ResultSet) pstmt.executeQuery();
                if(rs.next()){
                byte[] buf = rs.getBytes(1);
                ObjectInputStream objectIn = null;
                if (buf != null) {
                    objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
                }
                deSerializedObject = objectIn.readObject();
                rs.close();
                pstmt.close();

                System.out.println("Java object de-serialized from database. Object: "
                        + deSerializedObject + " Classname: "
                        + deSerializedObject.getClass().getName());
                return deSerializedObject;}
                return null;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jLabel3 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("NOTES: (Record key insights from readings and discussions.)");

        jLabel1.setText("Deliverable Status");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Deliverables", "What did you plan to accomplish", "What did you actually accomplished", "Size", "Effort"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(1).setMinWidth(200);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTable1.getColumnModel().getColumn(1).setMaxWidth(200);
            jTable1.getColumnModel().getColumn(2).setMinWidth(200);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(200);
            jTable1.getColumnModel().getColumn(2).setMaxWidth(200);
        }

        jLabel5.setText("Plan for the Next Week: (These items should appear in the deliverable status for the next week.)");

        jTable3.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Context (e.g. The gap between plan and actual)", "Lesson"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.setEnabled(false);
        jTable3.setRowHeight(18);
        jTable3.setSelectionBackground(new java.awt.Color(153, 153, 255));
        jTable3.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(jTable3);

        jLabel6.setText("Lessons Learned Reflection");

        jTable2.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Deliverable", "What do you intend to accomplish and why"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setRowHeight(18);
        jTable2.setSelectionBackground(new java.awt.Color(153, 153, 255));
        jTable2.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(jTable2);

        jScrollPane6.setViewportView(jTextPane1);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1000, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(175, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel5);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ent.png"))); // NOI18N

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logout.png"))); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 153, 255));
        jLabel7.setText("jLabel7");

        jButton7.setText("Download");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(51, 153, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Welcome");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/home.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/create.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/manage.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/view.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logs.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/account.png"))); // NOI18N
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1035, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(20, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton7)
                                .addGap(63, 63, 63)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(78, 78, 78))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(36, 36, 36)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7))
                        .addGap(5, 5, 5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 657, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        dispose();
        new Login().setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        paintToPDF(jTextPane1);
        JOptionPane.showMessageDialog(null, "PDF Created");
        String email = JOptionPane.showInputDialog("Enter the Email Address of your mentor: ");
        new SendFileEmail().sendMail(email, enbname+".pdf",username);
        JOptionPane.showMessageDialog(null, "Email Sent Sucessfully!!!");
        
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        dispose();
        new Home().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        dispose();
        CreateEnb.main(username);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        dispose();
        new viewlogs().main(username);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        dispose();
        ManageSelect.main(username);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        dispose();
        SelectView.main(username);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        dispose();
        Account.main(username);
    }//GEN-LAST:event_jButton12ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String username1,String enbname1) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ViewENB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewENB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewENB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewENB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        username=username1;
        enbname=enbname1;
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                  // new ViewENB().setVisible(true);
                    
                } catch (Exception ex) {
                    Logger.getLogger(ViewENB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
