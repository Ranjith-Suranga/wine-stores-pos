/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import lk.ijse.winestores.controller.ControllerFactory;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.controller.custom.MajorCategoryController;
import lk.ijse.winestores.controller.custom.impl.MajorCategoryControllerImpl;

/**
 *
 * @author Ranjith Suranga
 */
public class MajorCategory extends javax.swing.JPanel {

    private DefaultTableModel dtm;
    private SuraButton sb;
    private SuraTable st;
    private boolean bUpdate = false;    // Holds whether I should update this record or not

    /**
     * Creates new form MajorCatogeries
     */
    public MajorCategory() {

        initComponents();
        sb = new SuraButton(this);
        sb.convertAllJButtonsToSuraButtons();
        btnDelete.setEnabled(false);
//        btnAddNew.setEnabled(false);

        st = new SuraTable(tblMajorCatogeries);
        st.setHeaderAlignment(1, SwingConstants.CENTER);
        st.setColumnAlignment(0, SwingConstants.CENTER);
        st.setColumnAlignment(2, SwingConstants.CENTER);

        dtm = (DefaultTableModel) tblMajorCatogeries.getModel();

        initTable();
        loadDataInToTable();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                txtMajorCategory.requestFocusInWindow();
            }
        });
    }

    private void initTable() {

        dtm.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                int col = e.getColumn();

//                if (col == -1 || col != 0) {
//                    return;
//                }

                for (int i = 0; i < dtm.getRowCount(); i++) {

                    if ((Boolean) dtm.getValueAt(i, 0) == true) {
                        btnDelete.setEnabled(true);
                        return;
                    }

                }

                btnDelete.setEnabled(false);
            }
        });

    }

    private void loadDataInToTable() {
        try {
            dtm.setRowCount(0);

            MajorCategoryController sc = (MajorCategoryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.MAJOR_CATEGORY);
            //ArrayList<MajorCategoryControllerImpl.MajorCategoryModel> allMajorCategory = sc.getAllMajorCategory();
            ArrayList<MajorCategoryControllerImpl.MajorCategoryModel> allMajorCategory = sc.getAllMajorCategory();

//        try {
//            MajorCategoryController sc = (MajorCategoryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.MAJOR_CATEGORY);
//            //ArrayList<MajorCategoryControllerImpl.MajorCategoryModel> allMajorCategory = sc.getAllMajorCategory();
//            ArrayList<MajorCategoryControllerImpl.MajorCategoryModel> allMajorCategory = sc.getAllMajorCategory();
//            
            for (MajorCategoryControllerImpl.MajorCategoryModel majorCategoryModel : allMajorCategory) {
                Object[] rowData = {false, majorCategoryModel.getMainCatId(), majorCategoryModel.getCategoryName(), "<html>"
                    + "<p style='color:blue'>Edit</p></html>"};
                dtm.addRow(rowData);
            }
            
//            
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(MajorCategory.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(MajorCategory.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NullPointerException ex) {
//            //Logger.getLogger(MajorCategory.class.getName()).log(Level.SEVERE, null, ex);
//        }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MajorCategory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MajorCategory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(MajorCategory.class.getName()).log(Level.SEVERE, null, ex);
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

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btnAddNew = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        txtMajorCategory = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        scrPaneOfTable = new javax.swing.JScrollPane();
        tblMajorCatogeries = new javax.swing.JTable();

        setBackground(new java.awt.Color(232, 235, 241));
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Open Sans", 1, 24)); // NOI18N
        jLabel1.setText("Major Category");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234), 2));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(233, 233, 233)));

        jLabel2.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel2.setText("List of Major Category");

        btnAddNew.setBackground(new java.awt.Color(72, 158, 231));
        btnAddNew.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnAddNew.setForeground(new java.awt.Color(255, 255, 255));
        btnAddNew.setText("Save");
        btnAddNew.setEnabled(false);
        btnAddNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNewActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(251, 93, 93));
        btnDelete.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setToolTipText("Check one or more major categorie(s) to delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        txtMajorCategory.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtMajorCategory.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMajorCategoryFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMajorCategoryFocusLost(evt);
            }
        });
        txtMajorCategory.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                txtMajorCategoryCaretPositionChanged(evt);
            }
        });
        txtMajorCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMajorCategoryActionPerformed(evt);
            }
        });
        txtMajorCategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMajorCategoryKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMajorCategoryKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMajorCategoryKeyTyped(evt);
            }
        });

        jLabel3.setDisplayedMnemonic('M');
        jLabel3.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel3.setLabelFor(txtMajorCategory);
        jLabel3.setText("Enter Major Category");
        jLabel3.setToolTipText("");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMajorCategory)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 251, Short.MAX_VALUE)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddNew))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddNew, btnDelete});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtMajorCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnDelete)
                        .addComponent(jLabel2))
                    .addComponent(btnAddNew, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnAddNew, btnDelete});

        scrPaneOfTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tblMajorCatogeries.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        tblMajorCatogeries.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "ID", "Name", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMajorCatogeries.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblMajorCatogeries.setGridColor(new java.awt.Color(233, 236, 242));
        tblMajorCatogeries.setRowHeight(50);
        tblMajorCatogeries.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblMajorCatogeries.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblMajorCatogeries.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tblMajorCatogeries.setShowVerticalLines(false);
        tblMajorCatogeries.getTableHeader().setReorderingAllowed(false);
        tblMajorCatogeries.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblMajorCatogeriesMouseMoved(evt);
            }
        });
        tblMajorCatogeries.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMajorCatogeriesMouseClicked(evt);
            }
        });
        tblMajorCatogeries.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblMajorCatogeriesKeyPressed(evt);
            }
        });
        scrPaneOfTable.setViewportView(tblMajorCatogeries);
        if (tblMajorCatogeries.getColumnModel().getColumnCount() > 0) {
            tblMajorCatogeries.getColumnModel().getColumn(0).setMinWidth(40);
            tblMajorCatogeries.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblMajorCatogeries.getColumnModel().getColumn(0).setMaxWidth(40);
            tblMajorCatogeries.getColumnModel().getColumn(1).setMinWidth(100);
            tblMajorCatogeries.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblMajorCatogeries.getColumnModel().getColumn(1).setMaxWidth(100);
            tblMajorCatogeries.getColumnModel().getColumn(3).setMinWidth(100);
            tblMajorCatogeries.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblMajorCatogeries.getColumnModel().getColumn(3).setMaxWidth(100);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scrPaneOfTable, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(scrPaneOfTable, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblMajorCatogeriesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblMajorCatogeriesKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (dtm.getRowCount() > 0) {
                boolean currentStatus = (boolean) dtm.getValueAt(tblMajorCatogeries.getSelectedRow(), 0);
                dtm.setValueAt(!currentStatus, tblMajorCatogeries.getSelectedRow(), 0);
                evt.consume();
            }
        }
    }//GEN-LAST:event_tblMajorCatogeriesKeyPressed

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        txtMajorCategory.requestFocus();
    }//GEN-LAST:event_formFocusGained

    private void btnAddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNewActionPerformed

        try {
            MajorCategoryController controller = (MajorCategoryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.MAJOR_CATEGORY);

            if (!bUpdate) {
                controller.saveMajorCateogry(txtMajorCategory.getText());
            } else {
                controller.changeMajorCategory((String) dtm.getValueAt(tblMajorCatogeries.getSelectedRow(), 1), txtMajorCategory.getText());
            }
            loadDataInToTable();
            txtMajorCategory.setText("");
            txtMajorCategory.requestFocusInWindow();
            tblMajorCatogeries.scrollRectToVisible(tblMajorCatogeries.getCellRect(dtm.getRowCount() - 1, 0, true));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MajorCategory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MySQLIntegrityConstraintViolationException ex) {
            //Logger.getLogger(MajorCategory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
                txtMajorCategory.setSelectionStart(0);
                txtMajorCategory.setSelectionEnd(txtMajorCategory.getText().length());
                txtMajorCategory.requestFocusInWindow();
            } else {
                Logger.getLogger(MajorCategory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }//GEN-LAST:event_btnAddNewActionPerformed

    private void txtMajorCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMajorCategoryKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnAddNew.doClick();
        }
    }//GEN-LAST:event_txtMajorCategoryKeyPressed

    private void txtMajorCategoryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMajorCategoryKeyTyped

    }//GEN-LAST:event_txtMajorCategoryKeyTyped

    private void txtMajorCategoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMajorCategoryKeyReleased
        if (txtMajorCategory.getText().length() > 0) {
            btnAddNew.setEnabled(true);

        } else {
            bUpdate = false;
            btnAddNew.setEnabled(false);
        }
    }//GEN-LAST:event_txtMajorCategoryKeyReleased

    private void tblMajorCatogeriesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMajorCatogeriesMouseClicked
        int col = tblMajorCatogeries.columnAtPoint(evt.getPoint());
        if (col != 3) {
            txtMajorCategory.setText("");
            //txtMajorCategory.requestFocusInWindow();
            bUpdate = false;
            btnAddNew.setEnabled(false);
            return;
        }
        txtMajorCategory.setText((String) dtm.getValueAt(tblMajorCatogeries.getSelectedRow(), 2));
        txtMajorCategory.setSelectionStart(0);
        txtMajorCategory.setSelectionEnd(txtMajorCategory.getText().length());
        txtMajorCategory.requestFocusInWindow();
        bUpdate = true;
    }//GEN-LAST:event_tblMajorCatogeriesMouseClicked

    private void txtMajorCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMajorCategoryActionPerformed

    }//GEN-LAST:event_txtMajorCategoryActionPerformed

    private void txtMajorCategoryCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtMajorCategoryCaretPositionChanged

    }//GEN-LAST:event_txtMajorCategoryCaretPositionChanged

    private void txtMajorCategoryFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMajorCategoryFocusGained
        if (txtMajorCategory.getText().length() > 0) {
            btnAddNew.setEnabled(true);
        } else {
            bUpdate = false;
            btnAddNew.setEnabled(false);
        }
    }//GEN-LAST:event_txtMajorCategoryFocusGained

    private void txtMajorCategoryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMajorCategoryFocusLost
//        if (txtMajorCategory.getText().length() > 0) {
//            btnAddNew.setEnabled(true);
//        } else {
//            bUpdate = false;
//            btnAddNew.setEnabled(false);
//        }
    }//GEN-LAST:event_txtMajorCategoryFocusLost

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        MajorCategoryController ctrl = (MajorCategoryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.MAJOR_CATEGORY);
        tblMajorCatogeries.editingCanceled(null);
        for (int i = 0; i < dtm.getRowCount(); i++) {
            
            dtm = (DefaultTableModel) tblMajorCatogeries.getModel();
            boolean delete = (boolean) dtm.getValueAt(i, 0);
            
            //<editor-fold defaultstate="collapsed" desc="if delete">
            if (delete){
                
                String categoryId = (String) dtm.getValueAt(i, 1);
                ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/stop.png"));
                try {
                    
                    boolean success = ctrl.removeMajorCategory(categoryId);
                    
                    if (!success){
                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                                "Major Category No: '" + categoryId + "' can not be deleted right now due to some unexpected reason. Please try again later.",
                                "Delete Failed",
                                JOptionPane.ERROR_MESSAGE,
                                icon);
                        break;
                    }
                    
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MajorCategory.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    
                    // 1451 : Cannot delete or update a parent row: a foreign key constraint fails
                    if (ex.getErrorCode() == 1451){
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                                "Major Category No: '" + categoryId + "' can not be deleted because it has some related sub categories. Please delete all the related sub categories first.",
                                "Delete Failed",
                                JOptionPane.ERROR_MESSAGE,
                                icon);
                        break;                        
                    }
                }
            }
            //</editor-fold>
        }
        
        loadDataInToTable();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblMajorCatogeriesMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMajorCatogeriesMouseMoved

    }//GEN-LAST:event_tblMajorCatogeriesMouseMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddNew;
    private javax.swing.JButton btnDelete;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane scrPaneOfTable;
    private javax.swing.JTable tblMajorCatogeries;
    javax.swing.JTextField txtMajorCategory;
    // End of variables declaration//GEN-END:variables
}
