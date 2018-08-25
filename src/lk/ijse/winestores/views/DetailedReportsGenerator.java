/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import com.jidesoft.swing.AutoCompletion;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import lk.ijse.winestores.controller.ControllerFactory;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.controller.custom.GRNController;
import lk.ijse.winestores.dao.custom.GrnDAO;
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
public class DetailedReportsGenerator extends javax.swing.JDialog {

    enum ReportType {
        GRN_DETAIL {
            public String getName() {
                return "GRN";
            }
        },
        SUPPLIER_ORDER_DETAIL {
            public String getName() {
                return "Supplier Order";
            }
        };

        public String getName() {
            return null;
        }
    }

    private SuraButton sbtn;
    private ReportType rptType;
    private Frame frmParent;

    /**
     * Creates new form SalesReportsGenerator
     */
    public DetailedReportsGenerator(java.awt.Frame parent, boolean modal, String title, ReportType rptType) {
        super(parent, modal);
        initComponents();
        sbtn = new SuraButton(this);
        sbtn.convertAllJButtonsToSuraButtons();
        pnlContainer.getRootPane().setDefaultButton(btnGenerateRPT);
        AutoCompletion ac = new AutoCompletion(cmbId);
        ac.setStrict(true);

        this.rptType = rptType;
        this.frmParent = parent;

        lblTitle.setText(title);
        lblDescription.setText("Please enter valid " + rptType.getName() + " ID to generate detailed report");
        lblTitle.setVisible(false);
        lblClose.setVisible(false);
        pnlContainer.setVisible(false);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setBackground(new Color(0, 0, 0, 200));

        handleEvents();
        loadIds();
    }

    private void loadIds() {

        cmbId.removeAllItems();

        switch (rptType) {
            case GRN_DETAIL:

                GRNController ctrlGRN = (GRNController) ControllerFactory.getInstance().getController(SuperController.ControllerType.GRN);
                 {
                    try {
                        ArrayList<GRNController.GRNDetailsModel> grns = ctrlGRN.getGrns(GrnDAO.QueryType.GRN_ID, "");
                        for (GRNController.GRNDetailsModel grn : grns) {
                            if (ctrlGRN.isGrnAuthorized(grn.getGrnId())) {
                                cmbId.addItem(grn.getGrnId());
                            }
                        }
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(DetailedReportsGenerator.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(DetailedReportsGenerator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;
        }
    }

    private void handleEvents() {

        Component editorComponent = cmbId.getEditor().getEditorComponent();
        editorComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnGenerateRPT.doClick();
                }
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                // A little hack for linux
                while(DetailedReportsGenerator.this.getWidth()<=10){}

                Point p = new Point();
                p.x = (DetailedReportsGenerator.this.getWidth() - pnlContainer.getWidth()) / 2;
                p.y = (DetailedReportsGenerator.this.getHeight() - pnlContainer.getHeight()) / 2;
                pnlContainer.setLocation(p);

                Point p2 = (Point) p.clone();
                p2.x = (DetailedReportsGenerator.this.getWidth() - lblTitle.getPreferredSize().width) / 2;
                p2.y -= 50;
                lblTitle.setLocation(p2);

                p.x = p.x + pnlContainer.getWidth() - lblClose.getWidth() - 10;
                p.y -= 20;
                lblClose.setLocation(p);

                lblClose.setVisible(true);
                lblTitle.setVisible(true);
                pnlContainer.setVisible(true);

                cmbId.getEditor().selectAll();
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
        lblDescription = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnGenerateRPT = new javax.swing.JButton();
        cmbId = new javax.swing.JComboBox<>();

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

        lblDescription.setDisplayedMnemonic('P');
        lblDescription.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblDescription.setLabelFor(cmbId);
        lblDescription.setText("Please enter/select a valid GRN ID to generate detailed report");

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

        cmbId.setEditable(true);
        cmbId.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        cmbId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout pnlContainerLayout = new javax.swing.GroupLayout(pnlContainer);
        pnlContainer.setLayout(pnlContainerLayout);
        pnlContainerLayout.setHorizontalGroup(
            pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlContainerLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDescription)
                    .addComponent(cmbId, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        pnlContainerLayout.setVerticalGroup(
            pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDescription)
                .addGap(24, 24, 24)
                .addComponent(cmbId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(pnlContainer);
        pnlContainer.setBounds(10, 100, 630, 160);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCloseMouseClicked
        this.dispose();
    }//GEN-LAST:event_lblCloseMouseClicked

    private void btnGenerateRPTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateRPTActionPerformed

        /*
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
*/
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
                    case GRN_DETAIL:

                        compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/lk/ijse/winestores/reports/GRNDetailedReport.jasper"));
                        parms.put("grnId", cmbId.getSelectedItem().toString() );

                        filledReport = JasperFillManager.fillReport(compiledReport, parms, connection);
                        pnlReport = new JRViewer(filledReport);
                        reportContainer = new ReportContainer("GRN Detailed Report");
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

    }//GEN-LAST:event_btnGenerateRPTActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGenerateRPT;
    private javax.swing.JComboBox<String> cmbId;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblClose;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlContainer;
    // End of variables declaration//GEN-END:variables
}
