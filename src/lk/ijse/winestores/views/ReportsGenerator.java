/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Ranjith Suranga
 */
public class ReportsGenerator extends javax.swing.JDialog {

    enum ReportType {
        CASH_SALES_SUMMARY,
        CREDIT_SALES_SUMMARY,
        SUPPLIER_ORDERS_SUMMARY,
        GRN_SUMMARY
    }

    private SuraButton sbtn;
    private ReportType rptType;
    private Frame frmParent;

    /**
     * Creates new form SalesReportsGenerator
     */
    public ReportsGenerator(java.awt.Frame parent, boolean modal, String title, ReportType rptType) {
        super(parent, modal);
        initComponents();
        sbtn = new SuraButton(this);
        sbtn.convertAllJButtonsToSuraButtons();
        pnlContainer.getRootPane().setDefaultButton(btnGenerateRPT);

        this.rptType = rptType;
        this.frmParent = parent;

        lblTitle.setText(title);
        lblTitle.setVisible(false);
        lblClose.setVisible(false);
        pnlContainer.setVisible(false);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setBackground(new Color(0, 0, 0, 200));

        handleEvents();
    }

    private void handleEvents() {

        txtFrom.getEditor().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtTo.requestFocusInWindow();
                }
            }
        });

        txtFrom.getEditor().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        txtFrom.getEditor().selectAll();
                    }
                });
            }
        });

        txtTo.getEditor().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        txtTo.commitEdit();
                    } catch (ParseException ex) {
                        Logger.getLogger(ReportsGenerator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    btnGenerateRPT.requestFocusInWindow();
                    btnGenerateRPT.doClick();
                }
            }
        });

        txtTo.getEditor().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        txtTo.getEditor().selectAll();
                    }
                });

            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                Point p = new Point();
                p.x = (ReportsGenerator.this.getWidth() - pnlContainer.getWidth()) / 2;
                p.y = (ReportsGenerator.this.getHeight() - pnlContainer.getHeight()) / 2;
                pnlContainer.setLocation(p);

                Point p2 = (Point) p.clone();
                p2.x = (ReportsGenerator.this.getWidth() - lblTitle.getPreferredSize().width) / 2;
                p2.y -= 50;
                lblTitle.setLocation(p2);

                p.x = p.x + pnlContainer.getWidth() - lblClose.getWidth() - 10;
                p.y -= 20;
                lblClose.setLocation(p);

                lblClose.setVisible(true);
                lblTitle.setVisible(true);
                pnlContainer.setVisible(true);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblClose = new javax.swing.JLabel();
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
        txtFrom = new org.jdesktop.swingx.JXDatePicker();
        txtTo = new org.jdesktop.swingx.JXDatePicker();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnGenerateRPT = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(null);

        lblTitle.setFont(new java.awt.Font("Open Sans", 1, 36)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Cash Sales Summary Report");
        getContentPane().add(lblTitle);
        lblTitle.setBounds(20, 30, 510, 50);

        lblClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/close.png"))); // NOI18N
        lblClose.setToolTipText("Close");
        lblClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCloseMouseClicked(evt);
            }
        });
        getContentPane().add(lblClose);
        lblClose.setBounds(560, 30, 50, 40);

        pnlContainer.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel1.setText("Please select a valid date period to generate the report");

        jLabel2.setDisplayedMnemonic('F');
        jLabel2.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel2.setLabelFor(txtFrom);
        jLabel2.setText("From :");
        jLabel2.setToolTipText("");

        txtFrom.setToolTipText("");
        txtFrom.setDate(new Date());
        txtFrom.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtFrom.setFormats(new SimpleDateFormat
            ("yyyy-MM-dd"));
        txtFrom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFromKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFromKeyTyped(evt);
            }
        });

        txtTo.setDate(new Date());
        txtTo.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtTo.setFormats(new SimpleDateFormat
            ("yyyy-MM-dd"));
        txtTo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtToKeyPressed(evt);
            }
        });

        jLabel3.setDisplayedMnemonic('T');
        jLabel3.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel3.setLabelFor(txtTo);
        jLabel3.setText("To :");

        btnGenerateRPT.setBackground(new java.awt.Color(72, 158, 231));
        btnGenerateRPT.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnGenerateRPT.setForeground(new java.awt.Color(255, 255, 255));
        btnGenerateRPT.setMnemonic('G');
        btnGenerateRPT.setText("Generate Report");
        btnGenerateRPT.setToolTipText("Click to generate the report");
        btnGenerateRPT.setPreferredSize(new java.awt.Dimension(147, 42));
        btnGenerateRPT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateRPTActionPerformed(evt);
            }
        });
        jPanel1.add(btnGenerateRPT);

        javax.swing.GroupLayout pnlContainerLayout = new javax.swing.GroupLayout(pnlContainer);
        pnlContainer.setLayout(pnlContainerLayout);
        pnlContainerLayout.setHorizontalGroup(
            pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlContainerLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(pnlContainerLayout.createSequentialGroup()
                        .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTo, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        pnlContainerLayout.setVerticalGroup(
            pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        getContentPane().add(pnlContainer);
        pnlContainer.setBounds(10, 100, 630, 190);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCloseMouseClicked
        this.dispose();
    }//GEN-LAST:event_lblCloseMouseClicked

    private void btnGenerateRPTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateRPTActionPerformed

        if (txtFrom.getDate().compareTo(txtTo.getDate()) > 0) {
            ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
            JOptionPane.showMessageDialog(
                    SwingUtilities.getWindowAncestor(this),
                    "Please select a valid date period to generate the report.",
                    "Invalid Date Period",
                    JOptionPane.ERROR_MESSAGE,
                    icon);
            txtTo.requestFocusInWindow();
        } else {

            Connection connection;

            Cursor currentCursor = this.getCursor();
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            try {

                connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
                Main m = (Main) SwingUtilities.getWindowAncestor(this);
                m.pnlContainer.removeAll();
                ReportContainer reportContainer = null;
                JRViewer pnlReport = null;
                HashMap<String, Object> parms = new HashMap<>();
                JasperReport compiledReport;
                JasperPrint filledReport;

                switch (rptType) {
                    case CASH_SALES_SUMMARY:

                        compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/lk/ijse/winestores/reports/CashSalesSummaryReport.jasper"));
                        parms.put("txtFrom", new java.sql.Date(txtFrom.getDate().getTime()));
                        parms.put("txtTo", new java.sql.Date(txtTo.getDate().getTime()));

                        filledReport = JasperFillManager.fillReport(compiledReport, parms, connection);
                        pnlReport = new JRViewer(filledReport);
                        reportContainer = new ReportContainer("Cash Sales Summary Report");
                        break;
                        
                    case CREDIT_SALES_SUMMARY:
                        
                        compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/lk/ijse/winestores/reports/CreditSalesSummaryReport.jasper"));
                        parms.put("txtFrom", new java.sql.Date(txtFrom.getDate().getTime()));
                        parms.put("txtTo", new java.sql.Date(txtTo.getDate().getTime()));
                        filledReport = JasperFillManager.fillReport(compiledReport, parms, connection);
                        pnlReport = new JRViewer(filledReport);
                        reportContainer = new ReportContainer("Credit Sales Summary Report");
                        break;
                        
                    case GRN_SUMMARY:
                        
                        compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/lk/ijse/winestores/reports/GRNSummaryReport.jasper"));
                        parms.put("txtFrom", new java.sql.Date(txtFrom.getDate().getTime()));
                        parms.put("txtTo", new java.sql.Date(txtTo.getDate().getTime()));
                        filledReport = JasperFillManager.fillReport(compiledReport, parms, connection);
                        pnlReport = new JRViewer(filledReport);
                        reportContainer = new ReportContainer("GRN Summary Report");
                        break;   
                        
                    case SUPPLIER_ORDERS_SUMMARY:
                        
                        compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/lk/ijse/winestores/reports/SupplierOrdersSummaryReport.jasper"));
                        parms.put("txtFrom", new java.sql.Date(txtFrom.getDate().getTime()));
                        parms.put("txtTo", new java.sql.Date(txtTo.getDate().getTime()));
                        filledReport = JasperFillManager.fillReport(compiledReport, parms, connection);
                        pnlReport = new JRViewer(filledReport);
                        reportContainer = new ReportContainer("Supplier Orders Summary Report");
                        break;                           
                        

                }

                this.dispose();                
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

        }

    }//GEN-LAST:event_btnGenerateRPTActionPerformed

    private void txtFromKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFromKeyTyped

    }//GEN-LAST:event_txtFromKeyTyped

    private void txtFromKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFromKeyPressed
    }//GEN-LAST:event_txtFromKeyPressed

    private void txtToKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtToKeyPressed
//        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//            btnGenerateRPT.doClick();
//        }
    }//GEN-LAST:event_txtToKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGenerateRPT;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblClose;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlContainer;
    private org.jdesktop.swingx.JXDatePicker txtFrom;
    private org.jdesktop.swingx.JXDatePicker txtTo;
    // End of variables declaration//GEN-END:variables
}
