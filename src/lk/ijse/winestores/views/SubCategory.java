/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import com.jidesoft.swing.AutoCompletion;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import lk.ijse.winestores.controller.ControllerFactory;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.controller.custom.MajorCategoryController;
import lk.ijse.winestores.controller.custom.SubCategoryController;
import lk.ijse.winestores.controller.custom.impl.MajorCategoryControllerImpl;

/**
 *
 * @author Ranjith Suranga
 */
public class SubCategory extends javax.swing.JPanel {

    private ComboBoxModel<String> cbm;              // Holds the combo box model
    private DefaultTableModel dtm;                  // Holds the default table model
    private ComboBoxModel<String> cbmSub;           // Holds the combo box model of cmbSubCategory
    private JTextField txtSubCategory;              // Holds the sub category combo box editor

    private SuraTable st;                           // Holds the sura table
    private SuraButton sb;                          // Holds the sura button

    private boolean update = false;                  // Whether to add or update when the "Save" button is clickedf

    /**
     * Creates new form SubCategory
     */
    public SubCategory() {
        initComponents();

        sb = new SuraButton(this);
        sb.convertAllJButtonsToSuraButtons();

        st = new SuraTable(tblSubCategory);
        st.setHeaderAlignment(1, SwingConstants.CENTER);
        st.setColumnAlignment(0, SwingConstants.CENTER);
        st.setColumnAlignment(2, SwingConstants.CENTER);
        dtm = (DefaultTableModel) tblSubCategory.getModel();

       // AutoCompleteDecorator.decorate(cmbSubCategory);
        AutoCompletion ac = new AutoCompletion(cmbSubCategory);
        ac.setStrict(false);
        txtSubCategory = (JTextField) cmbSubCategory.getEditor().getEditorComponent();
//        suggestionCombo = new SuraSuggestionCombo(cmbSubCategory);
//        suggestionCombo.makeMeIntelligent();

        cbm = cmbMajorCategory.getModel();
        cbmSub = cmbSubCategory.getModel();

        txtSubCategory.addCaretListener(new CaretListener() {

            @Override
            public void caretUpdate(CaretEvent e) {
                if (txtSubCategory.getText().length() > 0) {
                    if (!isSubCategoryNameExists() && cmbMajorCategory.getSelectedIndex() != -1){
                        btnAddNew.setEnabled(true);
                    }
                } else {
                    btnAddNew.setEnabled(false);
                }
            }
        });

        txtSubCategory.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnAddNew.doClick();
                }
            }

        });

        loadDataInToMajorCombo();
        loadDataInToSubCombo();
        initTable();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                cmbMajorCategory.requestFocusInWindow();
//                txtMajorCategory.requestFocusInWindow();
            }
        });
    }

    private void initTable() {

        dtm.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
//                int col = e.getColumn();

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

    private void loadDataInToMajorCombo() {

        try {
            cmbMajorCategory.removeAllItems();

            MajorCategoryController controller = (MajorCategoryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.MAJOR_CATEGORY);
            ArrayList<MajorCategoryControllerImpl.MajorCategoryModel> allMajorCategory = controller.getAllMajorCategory();

            for (MajorCategoryControllerImpl.MajorCategoryModel majorCategoryModel : allMajorCategory) {
                cmbMajorCategory.addItem(majorCategoryModel.getCategoryName());
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SubCategory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SubCategory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(SubCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDataInToSubCombo() {

        cmbSubCategory.removeAllItems();

        try {
            SubCategoryController controller = (SubCategoryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SUB_CATEOGRY);
            ArrayList<SubCategoryController.SubCategoryModel> allSubCategories = controller.getAllSubCategories();
            if (allSubCategories == null) {
                return;
            }

            for (SubCategoryController.SubCategoryModel subCategory : allSubCategories) {
                cmbSubCategory.addItem(subCategory.getSubCategoryName());
            }

            cmbSubCategory.setSelectedIndex(-1);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SubCategory.class.getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(SubCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDataInToTable() {

        if (cmbMajorCategory.getSelectedIndex() == -1) {
            return;
        }

        dtm.setRowCount(0);

        SubCategoryController controller = (SubCategoryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SUB_CATEOGRY);

        try {
            ArrayList<SubCategoryController.SubCategoryModel> allSubCategories = controller.getAllSubCategoriesByMajorCategoryName(cmbMajorCategory.getSelectedItem().toString());

            if (allSubCategories != null) {

                for (SubCategoryController.SubCategoryModel subCategory : allSubCategories) {
                    Object[] rowData = {false, subCategory.getSubCategoryId(), subCategory.getSubCategoryName(), "<html>"
                        + "<p style='color:blue'>Edit</p></html>"};
                    dtm.addRow(rowData);
                }

            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SubCategory.class.getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(SubCategory.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private boolean isSubCategoryNameExists() {

        for (int i = 0; i < dtm.getRowCount(); i++) {
            String subCategoryName = (String) dtm.getValueAt(i, 2);

            if (subCategoryName.equalsIgnoreCase(txtSubCategory.getText().trim())) {
                return true;
            }
        }

        return false;

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
        jLabel3 = new javax.swing.JLabel();
        cmbMajorCategory = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cmbSubCategory = new javax.swing.JComboBox<>();
        scrPaneOfTable = new javax.swing.JScrollPane();
        tblSubCategory = new javax.swing.JTable();

        setBackground(new java.awt.Color(232, 235, 241));

        jLabel1.setFont(new java.awt.Font("Open Sans", 1, 24)); // NOI18N
        jLabel1.setText("Sub Category");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234), 2));

        jPanel2.setBackground(new java.awt.Color(249, 249, 249));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(233, 233, 233)));

        jLabel2.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel2.setText("List of Sub Category");

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
        btnDelete.setToolTipText("Check one or more sub categorie(s) to delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jLabel3.setDisplayedMnemonic('M');
        jLabel3.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel3.setLabelFor(cmbMajorCategory);
        jLabel3.setText("Select Major Category");

        cmbMajorCategory.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        cmbMajorCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbMajorCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMajorCategoryItemStateChanged(evt);
            }
        });
        cmbMajorCategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbMajorCategoryKeyPressed(evt);
            }
        });

        jLabel4.setDisplayedMnemonic('S');
        jLabel4.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel4.setLabelFor(cmbSubCategory);
        jLabel4.setText("Enter Sub Category");

        cmbSubCategory.setEditable(true);
        cmbSubCategory.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        cmbSubCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSubCategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbSubCategoryKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddNew))
                    .addComponent(cmbMajorCategory, 0, 530, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cmbSubCategory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddNew, btnDelete});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbMajorCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbSubCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete)
                    .addComponent(btnAddNew, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnAddNew, btnDelete});

        scrPaneOfTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tblSubCategory.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        tblSubCategory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "ID", "Description", ""
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
        tblSubCategory.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblSubCategory.setGridColor(new java.awt.Color(233, 236, 242));
        tblSubCategory.setRowHeight(50);
        tblSubCategory.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblSubCategory.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblSubCategory.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblSubCategory.setShowVerticalLines(false);
        tblSubCategory.getTableHeader().setReorderingAllowed(false);
        tblSubCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSubCategoryMouseClicked(evt);
            }
        });
        tblSubCategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblSubCategoryKeyPressed(evt);
            }
        });
        scrPaneOfTable.setViewportView(tblSubCategory);
        if (tblSubCategory.getColumnModel().getColumnCount() > 0) {
            tblSubCategory.getColumnModel().getColumn(0).setMinWidth(40);
            tblSubCategory.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblSubCategory.getColumnModel().getColumn(0).setMaxWidth(40);
            tblSubCategory.getColumnModel().getColumn(1).setMinWidth(100);
            tblSubCategory.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblSubCategory.getColumnModel().getColumn(1).setMaxWidth(100);
            tblSubCategory.getColumnModel().getColumn(3).setMinWidth(100);
            tblSubCategory.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblSubCategory.getColumnModel().getColumn(3).setMaxWidth(100);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scrPaneOfTable, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(scrPaneOfTable, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
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

    private void tblSubCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblSubCategoryKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (dtm.getRowCount() > 0) {
                boolean currentStatus = (boolean) dtm.getValueAt(tblSubCategory.getSelectedRow(), 0);
                dtm.setValueAt(!currentStatus, tblSubCategory.getSelectedRow(), 0);
                evt.consume();
            }
        }
    }//GEN-LAST:event_tblSubCategoryKeyPressed

    private void cmbMajorCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMajorCategoryKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtSubCategory.requestFocusInWindow();
        }
    }//GEN-LAST:event_cmbMajorCategoryKeyPressed

    private void cmbMajorCategoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMajorCategoryItemStateChanged
        loadDataInToTable();
        txtSubCategory.setText("");
        update = false;
    }//GEN-LAST:event_cmbMajorCategoryItemStateChanged

    private void btnAddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNewActionPerformed

        if (cmbMajorCategory.getSelectedIndex() == -1) {
            return;
        }

        SubCategoryController controller = (SubCategoryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SUB_CATEOGRY);

        try {

            if (!isSubCategoryNameExists()) {

                if (!update) {
                    controller.saveSubCategory(cmbMajorCategory.getSelectedItem().toString(), txtSubCategory.getText());
                } else {
                    int result = controller.changeSubCategory((String) dtm.getValueAt(tblSubCategory.getSelectedRow(), 1), txtSubCategory.getText());
                    if (result == -100) {
                        // Error Message Here
                        ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                                "This name is already in use, Please try a different name.",
                                "Save Failed",
                                JOptionPane.INFORMATION_MESSAGE,
                                icon);
                        txtSubCategory.selectAll();
                        txtSubCategory.requestFocusInWindow();
                        return;
                    }
                }

            } else {
                // Error message for duplicate entries
                txtSubCategory.setSelectionStart(0);
                txtSubCategory.setSelectionEnd(txtSubCategory.getText().length());
                txtSubCategory.requestFocusInWindow();
                return;
            }

            loadDataInToTable();
            loadDataInToSubCombo();
            // Scroll down to last record
            if (!update) {
                tblSubCategory.scrollRectToVisible(tblSubCategory.getCellRect(dtm.getRowCount() - 1, 0, true));
            }

            txtSubCategory.setText("");
            txtSubCategory.requestFocusInWindow();

            update = false;

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SubCategory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SubCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAddNewActionPerformed

    private void tblSubCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSubCategoryMouseClicked
        int col = tblSubCategory.columnAtPoint(evt.getPoint());
        if (col != 3) {
            txtSubCategory.setText("");
            update = false;
            btnAddNew.setEnabled(false);
            return;
        }
        txtSubCategory.setText((String) dtm.getValueAt(tblSubCategory.getSelectedRow(), 2));
        txtSubCategory.setSelectionStart(0);
        txtSubCategory.setSelectionEnd(txtSubCategory.getText().length());
        txtSubCategory.requestFocusInWindow();
        update = true;
    }//GEN-LAST:event_tblSubCategoryMouseClicked

    private void cmbSubCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbSubCategoryKeyPressed

    }//GEN-LAST:event_cmbSubCategoryKeyPressed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        SubCategoryController ctrl = (SubCategoryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SUB_CATEOGRY);
        tblSubCategory.editingCanceled(null);
        for (int i = 0; i < dtm.getRowCount(); i++) {
            
            boolean delete = (boolean) dtm.getValueAt(i, 0);
            
            //<editor-fold defaultstate="collapsed" desc="if delete">
            if (delete){
                
                String categoryId = (String) dtm.getValueAt(i, 1);
                ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/stop.png"));
                try {
                    
                    boolean success = ctrl.removeSubCategory(categoryId);
                    
                    if (!success){
                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                                "Sub Category No: '" + categoryId + "' can not be deleted right now due to some unexpected reason. Please try again later.",
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
                                "Sub Category No: '" + categoryId + "' can not be deleted because it has some related items. Please delete all the related items first.",
                                "Delete Failed",
                                JOptionPane.ERROR_MESSAGE,
                                icon);
                        break;                        
                    }
                }
            }
            //</editor-fold>
        }
        
        loadDataInToSubCombo();    
        loadDataInToTable();
    }//GEN-LAST:event_btnDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddNew;
    private javax.swing.JButton btnDelete;
    private javax.swing.JComboBox<String> cmbMajorCategory;
    private javax.swing.JComboBox<String> cmbSubCategory;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane scrPaneOfTable;
    private javax.swing.JTable tblSubCategory;
    // End of variables declaration//GEN-END:variables
}
