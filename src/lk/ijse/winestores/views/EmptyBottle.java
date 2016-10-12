/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.SwingUtilities;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import lk.ijse.winestores.controller.ControllerFactory;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.controller.custom.GRNController;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;

/**
 *
 * @author Ranjith Suranga
 */
public class EmptyBottle extends javax.swing.JFrame {

    private SuraButton sbtn;                                                        // Holds SuraButton Instance
    private ArrayList<EmptyBottleDTO> allEmptyBottleTypes;                          // Holds all the empty bottles details
    private ArrayList<GRNController.GRNEmptyBottleDetailModel> grnEmptyBottles;     // Holds all the empty bottles details for specific GRN
    private GRN currentGRN;                                                         // Holds the current GRN reference;

    /**
     * Creates new form EmptyBottel
     */
    public EmptyBottle(GRN grn, ArrayList<GRNController.GRNEmptyBottleDetailModel> grnEmptyBottles,boolean enable) {

        initComponents();
        pnlContainer.setVisible(false);
        setExtendedState(MAXIMIZED_BOTH);
        setBackground(new Color(0, 0, 0, 200));

        sbtn = new SuraButton(this);
        sbtn.convertAllJButtonsToSuraButtons();
        
        lblSaveStatus.setVisible(false);

        loadDataInToCombo();

        this.currentGRN = grn;

        if (grnEmptyBottles == null) {

            this.grnEmptyBottles = new ArrayList<>();

            lblTotalCost.setText("-");

            Calendar c1 = Calendar.getInstance();
            c1.setTime(grn.getInvoiceDate());
            c1.set(Calendar.DAY_OF_MONTH, c1.get(Calendar.DAY_OF_MONTH) + 14);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            lblDueDate.setText(sdf.format(c1.getTime()));

            for (EmptyBottleDTO grnEmptyBottle : allEmptyBottleTypes) {
                GRNController.GRNEmptyBottleDetailModel model = new GRNController.GRNEmptyBottleDetailModel(null, 0, 0, lblDueDate.getText(), null, grnEmptyBottle.getBottleType());
                this.grnEmptyBottles.add(model);
            }

        } else {

            this.grnEmptyBottles = grnEmptyBottles;

            GRNController.GRNEmptyBottleDetailModel model = this.grnEmptyBottles.get(cmbBottleType.getSelectedIndex());
            txtEstimatedBottles.setText(String.valueOf(model.getEstimatedBottles()));
            txtGivenBottles.setText(String.valueOf(model.getGivenBottles()));
            lblDueDate.setText(model.getDueDate());

            calculateCost();

        }
        
        this.enableMe(enable);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                Point p = new Point();
                p.x = (EmptyBottle.this.getWidth() - pnlContainer.getWidth()) / 2;
                p.y = (EmptyBottle.this.getHeight() - pnlContainer.getHeight()) / 2;
                pnlContainer.setLocation(p);
                pnlContainer.setVisible(true);

            }
        });

    }
    
    private void enableMe(boolean enable){
        //cmbBottleType.setEnabled(enable);
        txtEstimatedBottles.setEnabled(enable);
        txtGivenBottles.setEnabled(enable);
        btnSave.setEnabled(enable);
    }

    private void loadDataInToCombo() {

        cmbBottleType.removeAllItems();

        GRNController controller = (GRNController) ControllerFactory.getInstance().getController(SuperController.ControllerType.GRN);

        try {
            allEmptyBottleTypes = controller.getAllEmptyBottleTypes();

            for (EmptyBottleDTO bottle : allEmptyBottleTypes) {
                cmbBottleType.addItem(bottle.getBottleType());
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EmptyBottle.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EmptyBottle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void calculateCost() {

        try {
            int est = Integer.parseInt(txtEstimatedBottles.getText());
            int giv = Integer.parseInt(txtGivenBottles.getText());

            double cost = allEmptyBottleTypes.get(cmbBottleType.getSelectedIndex()).getCost();
            double totalCost = ((est - giv) * cost);
            if (totalCost > 0) {
                lblTotalCost.setText(formatCurrency(totalCost));
            } else {
                lblTotalCost.setText("-");
            }

        } catch (NumberFormatException ex) {
            lblTotalCost.setText("-");
        }

    }

    private String formatCurrency(Object currency) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(currency);
    }

    private boolean isNumericInt(String value) {
        try {
            int i = Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmbBottleType = new javax.swing.JComboBox<>();
        lblTotalCost = new javax.swing.JLabel();
        lblDueDate = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        txtEstimatedBottles = new javax.swing.JTextField();
        txtGivenBottles = new javax.swing.JTextField();
        lblSaveStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(102, 102, 102));
        setUndecorated(true);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(null);

        pnlContainer.setBackground(new java.awt.Color(255, 255, 255));
        pnlContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlContainerMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Bottle Type :");

        jLabel2.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Estimated Bottles :");

        jLabel3.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Given Bottles :");

        jLabel4.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Total Cost :");

        jLabel5.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Due Date :");

        cmbBottleType.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        cmbBottleType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2" }));
        cmbBottleType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbBottleTypeItemStateChanged(evt);
            }
        });
        cmbBottleType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbBottleTypeKeyPressed(evt);
            }
        });

        lblTotalCost.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblTotalCost.setForeground(new java.awt.Color(102, 102, 102));
        lblTotalCost.setText("-");

        lblDueDate.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblDueDate.setForeground(new java.awt.Color(102, 102, 102));
        lblDueDate.setText("jLabel7");

        btnSave.setBackground(new java.awt.Color(72, 158, 231));
        btnSave.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnExit.setBackground(new java.awt.Color(251, 93, 93));
        btnExit.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        txtEstimatedBottles.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtEstimatedBottles.setText("0");
        txtEstimatedBottles.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEstimatedBottlesFocusGained(evt);
            }
        });
        txtEstimatedBottles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtEstimatedBottlesMouseClicked(evt);
            }
        });
        txtEstimatedBottles.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEstimatedBottlesKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEstimatedBottlesKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEstimatedBottlesKeyTyped(evt);
            }
        });

        txtGivenBottles.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtGivenBottles.setText("0");
        txtGivenBottles.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtGivenBottlesCaretUpdate(evt);
            }
        });
        txtGivenBottles.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtGivenBottlesFocusGained(evt);
            }
        });
        txtGivenBottles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtGivenBottlesMouseClicked(evt);
            }
        });
        txtGivenBottles.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGivenBottlesKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGivenBottlesKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGivenBottlesKeyTyped(evt);
            }
        });

        lblSaveStatus.setForeground(new java.awt.Color(51, 204, 0));
        lblSaveStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/okay.png"))); // NOI18N
        lblSaveStatus.setText("Saved Successfully...!");

        javax.swing.GroupLayout pnlContainerLayout = new javax.swing.GroupLayout(pnlContainer);
        pnlContainer.setLayout(pnlContainerLayout);
        pnlContainerLayout.setHorizontalGroup(
            pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContainerLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlContainerLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGivenBottles))
                    .addGroup(pnlContainerLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEstimatedBottles))
                    .addGroup(pnlContainerLayout.createSequentialGroup()
                        .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlContainerLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbBottleType, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlContainerLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTotalCost))
                            .addGroup(pnlContainerLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlContainerLayout.createSequentialGroup()
                                        .addComponent(btnSave)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnExit))
                                    .addComponent(lblDueDate)
                                    .addComponent(lblSaveStatus))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlContainerLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4, jLabel5});

        pnlContainerLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnExit, btnSave});

        pnlContainerLayout.setVerticalGroup(
            pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContainerLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbBottleType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtEstimatedBottles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtGivenBottles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblTotalCost))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblDueDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnExit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSaveStatus)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(pnlContainer);
        pnlContainer.setBounds(10, 10, 330, 230);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        this.dispose();
    }//GEN-LAST:event_formMouseClicked

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnExitActionPerformed

    private void cmbBottleTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbBottleTypeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtEstimatedBottles.requestFocusInWindow();
        }
    }//GEN-LAST:event_cmbBottleTypeKeyPressed

    private void txtEstimatedBottlesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEstimatedBottlesKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtGivenBottles.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtEstimatedBottlesKeyPressed

    private void txtEstimatedBottlesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEstimatedBottlesKeyReleased
        calculateCost();
    }//GEN-LAST:event_txtEstimatedBottlesKeyReleased

    private void txtGivenBottlesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGivenBottlesKeyReleased
        calculateCost();
    }//GEN-LAST:event_txtGivenBottlesKeyReleased

    private void pnlContainerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlContainerMouseClicked
        evt.consume();
    }//GEN-LAST:event_pnlContainerMouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        if (!isNumericInt(txtEstimatedBottles.getText())) {
            txtEstimatedBottles.requestFocusInWindow();
            return;
        } else if (!isNumericInt(txtGivenBottles.getText())) {
            txtGivenBottles.requestFocusInWindow();
            txtGivenBottles.selectAll();
            return;
        } else if (Integer.parseInt(txtEstimatedBottles.getText()) < Integer.parseInt(txtGivenBottles.getText())) {
            txtEstimatedBottles.requestFocusInWindow();
            return;
        }

        GRNController.GRNEmptyBottleDetailModel model = this.grnEmptyBottles.get(cmbBottleType.getSelectedIndex());

        model.setEstimatedBottles(Integer.parseInt(txtEstimatedBottles.getText()));
        model.setGivenBottles(Integer.parseInt(txtGivenBottles.getText()));

        this.currentGRN.setEmptyBottles(grnEmptyBottles);
        cmbBottleType.requestFocusInWindow();

        lblSaveStatus.setVisible(true);
        pnlContainer.setSize(pnlContainer.getSize().width, 260);
        
        new Timer(1500, new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                lblSaveStatus.setVisible(false);
                ((Timer)e.getSource()).stop();
                pnlContainer.setSize(pnlContainer.getSize().width, 230);
            }
        }).start();
        
    }//GEN-LAST:event_btnSaveActionPerformed

    private void cmbBottleTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbBottleTypeItemStateChanged

        if (cmbBottleType.getSelectedIndex() == -1 || this.grnEmptyBottles == null) {
            return;
        }

        GRNController.GRNEmptyBottleDetailModel model = this.grnEmptyBottles.get(cmbBottleType.getSelectedIndex());
        txtEstimatedBottles.setText(String.valueOf(model.getEstimatedBottles()));
        txtGivenBottles.setText(String.valueOf(model.getGivenBottles()));
        lblDueDate.setText(model.getDueDate());

        calculateCost();

    }//GEN-LAST:event_cmbBottleTypeItemStateChanged

    private void txtEstimatedBottlesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEstimatedBottlesFocusGained
        txtEstimatedBottles.setSelectionStart(0);
        txtEstimatedBottles.setSelectionEnd(txtEstimatedBottles.getText().length());
    }//GEN-LAST:event_txtEstimatedBottlesFocusGained

    private void txtEstimatedBottlesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtEstimatedBottlesMouseClicked
        txtEstimatedBottles.setSelectionStart(0);
        txtEstimatedBottles.setSelectionEnd(txtEstimatedBottles.getText().length());
    }//GEN-LAST:event_txtEstimatedBottlesMouseClicked

    private void txtGivenBottlesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGivenBottlesFocusGained
        txtGivenBottles.setSelectionStart(0);
        txtGivenBottles.setSelectionEnd(txtGivenBottles.getText().length());
    }//GEN-LAST:event_txtGivenBottlesFocusGained

    private void txtGivenBottlesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtGivenBottlesMouseClicked
        txtGivenBottles.setSelectionStart(0);
        txtGivenBottles.setSelectionEnd(txtGivenBottles.getText().length());
    }//GEN-LAST:event_txtGivenBottlesMouseClicked

    private void txtGivenBottlesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGivenBottlesKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSave.doClick();
        }
    }//GEN-LAST:event_txtGivenBottlesKeyPressed

    private void txtEstimatedBottlesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEstimatedBottlesKeyTyped
        //|| evt.getKeyChar() != KeyEvent.VK_ENTER || evt.getKeyChar() != KeyEvent.VK_BACK_SPACE || evt.getKeyChar() == KeyEvent.VK_DELETE
        if (!Character.isDigit(evt.getKeyChar()) && (evt.getKeyChar() != KeyEvent.VK_ENTER || evt.getKeyChar() != KeyEvent.VK_BACK_SPACE || evt.getKeyChar() == KeyEvent.VK_DELETE)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtEstimatedBottlesKeyTyped

    private void txtGivenBottlesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGivenBottlesKeyTyped
        if (!Character.isDigit(evt.getKeyChar()) && (evt.getKeyChar() != KeyEvent.VK_ENTER || evt.getKeyChar() != KeyEvent.VK_BACK_SPACE || evt.getKeyChar() == KeyEvent.VK_DELETE)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtGivenBottlesKeyTyped

    private void txtGivenBottlesCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtGivenBottlesCaretUpdate
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGivenBottlesCaretUpdate

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
            java.util.logging.Logger.getLogger(EmptyBottle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmptyBottle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmptyBottle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmptyBottle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new EmptyBottle().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbBottleType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lblDueDate;
    private javax.swing.JLabel lblSaveStatus;
    private javax.swing.JLabel lblTotalCost;
    private javax.swing.JPanel pnlContainer;
    private javax.swing.JTextField txtEstimatedBottles;
    private javax.swing.JTextField txtGivenBottles;
    // End of variables declaration//GEN-END:variables
}
