/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import lk.ijse.winestores.controller.ControllerFactory;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.controller.custom.EmptyBottleController;
import lk.ijse.winestores.controller.custom.QueryController;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;

/**
 *
 * @author Ranjith Suranga
 */
public class ManageEmptyBottles extends javax.swing.JFrame {

    private SuraButton sbtn;
    private SuraTable stbl;

    private DefaultTableModel dtm;

    private QueryController queryCtrl;

    // Dependencies
    private EmptyBottleController emptyBottleCtrl;

    /**
     * Creates new form ManageEmptyBottles
     */
    public ManageEmptyBottles() {
        initComponents();

        pnlContainer.setVisible(false);

        sbtn = new SuraButton(this);
        sbtn.convertAllJButtonsToSuraButtons();

        stbl = new SuraTable(tblEmptyBottleType);
        stbl.setHeaderAlignment(0, SwingConstants.CENTER);
        stbl.setHeaderAlignment(1, SwingConstants.CENTER);
        stbl.setColumnAlignment(0, SwingConstants.CENTER);
        stbl.setColumnAlignment(1, SwingConstants.CENTER);

        dtm = (DefaultTableModel) tblEmptyBottleType.getModel();
        dtm.addRow(new Object[]{"Bottle Type -1", "100.20"});
        dtm.addRow(new Object[]{"Bottle Type -2", "100.20"});

        tblEmptyBottleType.setFillsViewportHeight(true);

        setExtendedState(MAXIMIZED_BOTH);
        setBackground(new Color(0, 0, 0, 200));

        queryCtrl = (QueryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.QUERY);
        emptyBottleCtrl = (EmptyBottleController) ControllerFactory.getInstance().getController(SuperController.ControllerType.EMPTY_BOTTLE);

        handleEvents();
        loadData();

    }

    private void loadData() {

        dtm.setRowCount(0);

        try {
            ArrayList<EmptyBottleDTO> allEmptyBottles = queryCtrl.getAllEmptyBottles();

            if (allEmptyBottles != null) {

                for (EmptyBottleDTO emptyBottle : allEmptyBottles) {
                    Object[] rowData = {emptyBottle.getBottleType(), this.formatPrice(emptyBottle.getCost())};
                    dtm.addRow(rowData);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ManageEmptyBottles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ManageEmptyBottles.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void handleEvents() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                Point p = new Point();
                p.x = (ManageEmptyBottles.this.getWidth() - pnlContainer.getWidth()) / 2;
                p.y = (ManageEmptyBottles.this.getHeight() - pnlContainer.getHeight()) / 2;
                pnlContainer.setLocation(p);
                pnlContainer.setVisible(true);

            }
        });

        txtBottleType.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                enableSave();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                enableSave();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                enableSave();
            }
        });

        txtCost.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                enableSave();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                enableSave();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                enableSave();
            }
        });

        tblEmptyBottleType.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                btnDelete.setEnabled(false);

                if (tblEmptyBottleType.getSelectedRow() != -1) {
                    btnDelete.setEnabled(true);
                    txtCost.setEnabled(true);
                    txtBottleType.setEnabled(true);
                    txtBottleType.setText((String) dtm.getValueAt(tblEmptyBottleType.getSelectedRow(), 0));
                    txtCost.setText((String) dtm.getValueAt(tblEmptyBottleType.getSelectedRow(), 1));
                } else {
                    txtCost.setText("");
                    txtBottleType.setText("");
                    txtCost.setEnabled(false);
                    txtBottleType.setEnabled(false);
                }
            }
        });
    }

    private String formatPrice(Object price) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(false);
        return nf.format(price);
    }

    private void enableSave() {

        btnSave.setEnabled(false);

        if (!txtBottleType.getText().trim().isEmpty()) {

            String txt = txtCost.getText().trim();
            if (!txt.isEmpty() && txt.matches("\\d*(\\.\\d{2})?")) {
                btnSave.setEnabled(true);
            }

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
        jButton1 = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtBottleType = new javax.swing.JTextField();
        txtCost = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        pnlTableContainer = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmptyBottleType = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setUndecorated(true);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(null);

        pnlContainer.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Open Sans", 1, 16)); // NOI18N
        jLabel1.setText("Manage Empty Bottles");

        jButton1.setBackground(new java.awt.Color(251, 93, 93));
        jButton1.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/exit.png"))); // NOI18N
        jButton1.setText("Close");
        jButton1.setToolTipText("Click to close the dialog box");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnNew.setBackground(new java.awt.Color(76, 175, 80));
        btnNew.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/New.png"))); // NOI18N
        btnNew.setMnemonic('N');
        btnNew.setText("New");
        btnNew.setToolTipText("Click to add new empty bottle type");
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(224, 219, 221));

        jLabel2.setDisplayedMnemonic('B');
        jLabel2.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel2.setLabelFor(txtBottleType);
        jLabel2.setText("Bottle Type :");
        jLabel2.setToolTipText("");

        jLabel3.setDisplayedMnemonic('C');
        jLabel3.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel3.setLabelFor(txtCost);
        jLabel3.setText("Cost :");

        txtBottleType.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtBottleType.setEnabled(false);
        txtBottleType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBottleTypeFocusGained(evt);
            }
        });
        txtBottleType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBottleTypeKeyTyped(evt);
            }
        });

        txtCost.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtCost.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCost.setEnabled(false);
        txtCost.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCostFocusGained(evt);
            }
        });
        txtCost.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCostKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostKeyTyped(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(72, 158, 231));
        btnSave.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setMnemonic('S');
        btnSave.setText("Save");
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(251, 93, 93));
        btnDelete.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setToolTipText("Click to delete selected bottle type");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        pnlTableContainer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234)));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);

        tblEmptyBottleType.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        tblEmptyBottleType.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Bottle Type", "Cost"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmptyBottleType.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblEmptyBottleType.setGridColor(new java.awt.Color(233, 236, 242));
        tblEmptyBottleType.setRowHeight(35);
        tblEmptyBottleType.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblEmptyBottleType.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblEmptyBottleType.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblEmptyBottleType.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblEmptyBottleType);

        javax.swing.GroupLayout pnlTableContainerLayout = new javax.swing.GroupLayout(pnlTableContainer);
        pnlTableContainer.setLayout(pnlTableContainerLayout);
        pnlTableContainerLayout.setHorizontalGroup(
            pnlTableContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableContainerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        pnlTableContainerLayout.setVerticalGroup(
            pnlTableContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableContainerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout pnlContainerLayout = new javax.swing.GroupLayout(pnlContainer);
        pnlContainer.setLayout(pnlContainerLayout);
        pnlContainerLayout.setHorizontalGroup(
            pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(pnlContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlContainerLayout.createSequentialGroup()
                        .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBottleType)
                            .addGroup(pnlContainerLayout.createSequentialGroup()
                                .addComponent(txtCost)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnSave)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDelete))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlContainerLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlTableContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnlContainerLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnNew, jButton1});

        pnlContainerLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnDelete, btnSave});

        pnlContainerLayout.setVerticalGroup(
            pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1)
                    .addComponent(btnNew))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtBottleType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave)
                    .addComponent(btnDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlTableContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(pnlContainer);
        pnlContainer.setBounds(30, 20, 420, 320);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        this.dispose();
    }//GEN-LAST:event_formMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtBottleTypeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBottleTypeFocusGained
        txtBottleType.selectAll();
    }//GEN-LAST:event_txtBottleTypeFocusGained

    private void txtCostFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCostFocusGained
        txtCost.selectAll();
    }//GEN-LAST:event_txtCostFocusGained

    private void txtBottleTypeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBottleTypeKeyTyped
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtCost.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtBottleTypeKeyTyped

    private void txtCostKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostKeyTyped

        if (!(Character.isDigit(evt.getKeyChar())
                || evt.getKeyChar() == KeyEvent.VK_DELETE
                || evt.getKeyChar() == KeyEvent.VK_BACK_SPACE
                || evt.getKeyChar() == KeyEvent.VK_PERIOD)) {
            evt.consume();
        }

        if (evt.getKeyChar() == KeyEvent.VK_PERIOD) {
            if (txtCost.getText().contains(".")) {
                evt.consume();
                this.getToolkit().beep();
            }
        }

    }//GEN-LAST:event_txtCostKeyTyped

    private void txtCostKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSave.doClick();
        }
    }//GEN-LAST:event_txtCostKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        txtBottleType.setEnabled(true);
        txtCost.setEnabled(true);
        tblEmptyBottleType.clearSelection();
        txtBottleType.setText("");
        txtCost.setText("");
        txtBottleType.requestFocusInWindow();
        btnSave.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        
        try {
            boolean success = emptyBottleCtrl.deleteEmptyBottle(dtm.getValueAt(tblEmptyBottleType.getSelectedRow(), 0).toString());
            
            if (success){
                dtm.removeRow(tblEmptyBottleType.getSelectedRow());
                tblEmptyBottleType.clearSelection();
            }else{
                ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
                JOptionPane.showMessageDialog(this,
                        "Deletion has failed due to unexepected reason, please try again later",
                        "Deletion has Failed",
                        JOptionPane.ERROR_MESSAGE,
                        icon);   
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ManageEmptyBottles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ManageEmptyBottles.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        // Check whether the item is already in the list
        for (int i = 0; i < dtm.getRowCount(); i++) {

            if (i == tblEmptyBottleType.getSelectedRow()) {
                continue;
            }

            if (dtm.getValueAt(i, 0).toString().equalsIgnoreCase(txtBottleType.getText().trim())) {
                ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
                JOptionPane.showMessageDialog(this,
                        "The empty bottle type that you are trying to enter is already in the list, please try a different name",
                        "Duplicate Found",
                        JOptionPane.ERROR_MESSAGE,
                        icon);
                txtBottleType.requestFocusInWindow();
                return;
            }

        }

        try {
            int row = tblEmptyBottleType.getSelectedRow();
            boolean success = emptyBottleCtrl.saveEmptyBottle(new EmptyBottleDTO(
                    txtBottleType.getText().trim(),
                    Double.valueOf(txtCost.getText())
            ),
                    (row == -1) ? null : dtm.getValueAt(row, 0).toString());

            if (success) {
                
                if (tblEmptyBottleType.getSelectedRow() == -1) {
                    Object[] rowData = {txtBottleType.getText().trim(), formatPrice(Double.valueOf(txtCost.getText().trim()))};
                    dtm.addRow(rowData);
                    tblEmptyBottleType.getSelectionModel().setSelectionInterval(tblEmptyBottleType.getRowCount()-1, tblEmptyBottleType.getRowCount()-1);
                    tblEmptyBottleType.scrollRectToVisible(tblEmptyBottleType.getCellRect(tblEmptyBottleType.getRowCount()-1, 0, true));
                } else {
                    dtm.setValueAt(txtBottleType.getText().trim(), tblEmptyBottleType.getSelectedRow(), 0);
                    dtm.setValueAt(formatPrice(Double.valueOf(txtCost.getText().trim())), tblEmptyBottleType.getSelectedRow(), 1);
                }
                tblEmptyBottleType.clearSelection();
                btnNew.requestFocusInWindow();
            }else{
                ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
                JOptionPane.showMessageDialog(this,
                        "We can't save right now due to unexepected reason, please try again",
                        "Saving Failed",
                        JOptionPane.ERROR_MESSAGE,
                        icon);   
                txtBottleType.requestFocusInWindow();
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ManageEmptyBottles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ManageEmptyBottles.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnSaveActionPerformed

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
            java.util.logging.Logger.getLogger(ManageEmptyBottles.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageEmptyBottles.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageEmptyBottles.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageEmptyBottles.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageEmptyBottles().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel pnlContainer;
    private javax.swing.JPanel pnlTableContainer;
    private javax.swing.JTable tblEmptyBottleType;
    private javax.swing.JTextField txtBottleType;
    private javax.swing.JTextField txtCost;
    // End of variables declaration//GEN-END:variables
}
