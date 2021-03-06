/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import lk.ijse.winestores.controller.ControllerFactory;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.controller.custom.DayEndReportsController;
import lk.ijse.winestores.controller.custom.QueryController;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Ranjith Suranga
 */
public class DayEndReports extends javax.swing.JDialog {

    // Dependencies
    private SuraButton btnSura;
    private DayEndReportsController ctrlDayEnd;
    private QueryController ctrlQuery;

    /**
     * Creates new form DayEndReports
     */
    public DayEndReports(java.awt.Frame parent, boolean modal) {

        super(parent, modal);
        initComponents();

        // Dependency Injection
        ctrlDayEnd = (DayEndReportsController) ControllerFactory.getInstance().getController(SuperController.ControllerType.DAYEND_REPORTS);
        ctrlQuery = (QueryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.QUERY);

        btnSura = new SuraButton(this);
        btnSura.convertAllJButtonsToSuraButtons();

        lblTitle.setVisible(false);
        lblClose.setVisible(false);
        pnlContainer.setVisible(false);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setBackground(new Color(0, 0, 0, 200));

        lblSystemDate.setText(formatDate(new Date()));
        try {
            lblLastDayEnd.setText(formatDate(ctrlQuery.getLastDayEnd()));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DayEndReports.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DayEndReports.class.getName()).log(Level.SEVERE, null, ex);
        }

        txtDate.getEditor().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        txtDate.getEditor().selectAll();
                    }
                });

            }

        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                // A little hack for linux
                while(DayEndReports.this.getWidth()<=10){}

                Point p = new Point();
                p.x = (DayEndReports.this.getWidth() - pnlContainer.getWidth()) / 2;
                p.y = (DayEndReports.this.getHeight() - pnlContainer.getHeight()) / 2;
                pnlContainer.setLocation(p);

                Point p2 = (Point) p.clone();
                p2.x = (DayEndReports.this.getWidth() - lblTitle.getWidth()) / 2;
                p2.y -= 50;
                lblTitle.setLocation(p2);

                p.x = p.x + pnlContainer.getWidth() - lblClose.getWidth() - 10;
                p.y -= 20;
                lblClose.setLocation(p);

                lblClose.setVisible(true);
                lblTitle.setVisible(true);
                pnlContainer.setVisible(true);

                txtDateActionPerformed(null);

            }
        });
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private Date parseDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(DayEndReports.class.getName()).log(Level.SEVERE, null, ex);
        }
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

        lblClose = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        pnlContainer = new javax.swing.JPanel(){
            public void paintComponent(Graphics g) {
                //To change body of generated methods, choose Tools | Templates.
                Graphics2D gd = (Graphics2D)g;
                gd.setColor(pnlContainer.getBackground());
                gd.fillRoundRect(0, 0, pnlContainer.getWidth(), pnlContainer.getHeight(), 10, 10);
            }
        };
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblSystemDate = new javax.swing.JLabel();
        lblLastDayEnd = new javax.swing.JLabel();
        txtDate = new org.jdesktop.swingx.JXDatePicker();
        btnDayEnd = new javax.swing.JButton();
        btnGenerateReport = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(null);

        lblClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/close.png"))); // NOI18N
        lblClose.setToolTipText("Close");
        lblClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCloseMouseClicked(evt);
            }
        });
        getContentPane().add(lblClose);
        lblClose.setBounds(470, 40, 50, 40);

        lblTitle.setFont(new java.awt.Font("Open Sans", 1, 36)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Day End ");
        getContentPane().add(lblTitle);
        lblTitle.setBounds(210, 10, 160, 50);

        pnlContainer.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("System Date :");

        jLabel2.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 51, 204));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Last \" Day End \" Report Generated On :");

        jLabel3.setDisplayedMnemonic('D');
        jLabel3.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setLabelFor(txtDate);
        jLabel3.setText("Select a Date to Generate a Report : ");
        jLabel3.setToolTipText("");

        lblSystemDate.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        lblSystemDate.setForeground(new java.awt.Color(255, 0, 0));
        lblSystemDate.setText("jLabel4");

        lblLastDayEnd.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        lblLastDayEnd.setForeground(new java.awt.Color(0, 51, 204));
        lblLastDayEnd.setText("jLabel5");

        txtDate.setDate(new Date());
        txtDate.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtDate.setFormats(new SimpleDateFormat
            ("yyyy-MM-dd"));
        txtDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDateFocusGained(evt);
            }
        });
        txtDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDateActionPerformed(evt);
            }
        });

        btnDayEnd.setBackground(new java.awt.Color(76, 175, 80));
        btnDayEnd.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnDayEnd.setForeground(new java.awt.Color(255, 255, 255));
        btnDayEnd.setMnemonic('E');
        btnDayEnd.setText("End the day");
        btnDayEnd.setToolTipText("Click to end the day. This will be required if you want to generate the report for that specific date.");
        btnDayEnd.setEnabled(false);
        btnDayEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDayEndActionPerformed(evt);
            }
        });

        btnGenerateReport.setBackground(new java.awt.Color(72, 158, 231));
        btnGenerateReport.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnGenerateReport.setForeground(new java.awt.Color(255, 255, 255));
        btnGenerateReport.setMnemonic('G');
        btnGenerateReport.setText("Generate the report");
        btnGenerateReport.setEnabled(false);
        btnGenerateReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlContainerLayout = new javax.swing.GroupLayout(pnlContainer);
        pnlContainer.setLayout(pnlContainerLayout);
        pnlContainerLayout.setHorizontalGroup(
            pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContainerLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlContainerLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSystemDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlContainerLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblLastDayEnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlContainerLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnGenerateReport, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                            .addComponent(btnDayEnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(21, 21, 21))
        );

        pnlContainerLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3});

        pnlContainerLayout.setVerticalGroup(
            pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContainerLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblSystemDate))
                .addGap(18, 18, 18)
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblLastDayEnd))
                .addGap(18, 18, 18)
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnDayEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGenerateReport, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        getContentPane().add(pnlContainer);
        pnlContainer.setBounds(10, 67, 560, 250);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCloseMouseClicked
        this.dispose();
    }//GEN-LAST:event_lblCloseMouseClicked

    private void txtDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDateFocusGained

    }//GEN-LAST:event_txtDateFocusGained

    private void txtDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateActionPerformed
        btnDayEnd.setEnabled(false);
        btnGenerateReport.setEnabled(false);

        try {
            if (ctrlQuery.hasDayEndDone(txtDate.getDate())) {
                btnGenerateReport.setEnabled(true);
            } else {
                Date systemDate = new Date();
                Date lastDayEnd = parseDate(lblLastDayEnd.getText());

                if (systemDate.compareTo(lastDayEnd) > 0) {
                    if (txtDate.getDate().compareTo(systemDate) <= 0 && txtDate.getDate().compareTo(lastDayEnd) > 0) {
                        btnDayEnd.setEnabled(true);
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DayEndReports.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DayEndReports.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtDateActionPerformed

    private void btnDayEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDayEndActionPerformed
        
        boolean success = false;

        try {
            success = ctrlDayEnd.finalizeDayEnd(txtDate.getDate());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DayEndReports.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DayEndReports.class.getName()).log(Level.SEVERE, null, ex);
        }
        btnDayEnd.setEnabled(!success);

        btnGenerateReport.setEnabled(success);
    }//GEN-LAST:event_btnDayEndActionPerformed

    private void btnGenerateReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateReportActionPerformed
        
        Connection connection;

        Cursor currentCursor = this.getCursor();
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {

            DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
            JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.xpath.executer.factory",
                    "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");

            connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
            //JasperReport compiledReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/lk/ijse/winestores/reports/ItemMasterFullReport.jrxml"));
            JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/lk/ijse/winestores/reports/DayEndReport.jasper"));
            
            HashMap<String, Object> parms = new HashMap<>();
            parms.put("date", formatDate(txtDate.getDate()));
            
            JasperPrint filledReport = JasperFillManager.fillReport(compiledReport, parms, connection);
            JRViewer pnlReport = new JRViewer(filledReport);

            this.dispose();
            Main m = (Main) SwingUtilities.getWindowAncestor(this);
            m.pnlContainer.removeAll();
            ReportContainer reportContainer = new ReportContainer("Day End Report");
            m.setExtenstion(reportContainer);
            reportContainer.add(pnlReport);
            reportContainer.updateUI();
            m.pnlContainer.add(reportContainer);
            m.pnlContainer.updateUI();

            // Closing the connection
            connection.close();
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            Logger.getLogger(Views.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Views.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Views.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.setCursor(currentCursor);
        }        
        
        
    }//GEN-LAST:event_btnGenerateReportActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(DayEndReports.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DayEndReports.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DayEndReports.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DayEndReports.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DayEndReports dialog = new DayEndReports(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDayEnd;
    private javax.swing.JButton btnGenerateReport;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblClose;
    private javax.swing.JLabel lblLastDayEnd;
    private javax.swing.JLabel lblSystemDate;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlContainer;
    private org.jdesktop.swingx.JXDatePicker txtDate;
    // End of variables declaration//GEN-END:variables
}
