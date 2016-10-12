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
import lk.ijse.winestores.controller.custom.ItemController;
import lk.ijse.winestores.controller.custom.MajorCategoryController;
import lk.ijse.winestores.controller.custom.SubCategoryController;
import lk.ijse.winestores.controller.custom.impl.MajorCategoryControllerImpl;

/**
 *
 * @author Ranjith Suranga
 */
public class ItemCategory extends javax.swing.JPanel {

    private SuraTable st;               // Holds the table
    private SuraButton sb;

    private JTextField txtItem;         // Holds the cmbItem editor;

    private DefaultTableModel dtm;      // Holds the table model;

    private boolean update = false;         // Holds whether to update or add

    /**
     * Creates new form ItemCategory
     */
    public ItemCategory() {
        initComponents();

        sb = new SuraButton(this);
        sb.convertAllJButtonsToSuraButtons();

        st = new SuraTable(tblItemCategory);
        st.setHeaderAlignment(1, SwingConstants.CENTER);
        st.setHeaderAlignment(3, SwingConstants.CENTER);
        st.setColumnAlignment(0, SwingConstants.CENTER);
        st.setColumnAlignment(2, SwingConstants.CENTER);
        st.setColumnAlignment(3, SwingConstants.CENTER);
        dtm = (DefaultTableModel) tblItemCategory.getModel();
        initTable();

        txtItem = (JTextField) cmbItem.getEditor().getEditorComponent();

        loadDataInToMajorCombo();
//        loadDataInToItemsCombo();
        AutoCompletion ac = new AutoCompletion(cmbItem);
        ac.setStrict(false);
        //AutoCompleteDecorator.decorate(cmbItem);

        txtItem.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtBarcode.requestFocusInWindow();
                }

            }
        });

        txtItem.addCaretListener(new CaretListener() {

            @Override
            public void caretUpdate(CaretEvent e) {
                if (txtItem.getText().length() > 0) {
                    if (!isItemNameExists() && cmbMajorCategory.getSelectedIndex() != -1 && cmbSubCategory.getSelectedIndex() != -1) {
                        btnAddNew.setEnabled(true);
                    }
                } else {
                    btnAddNew.setEnabled(false);
                }
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                cmbMajorCategory.requestFocusInWindow();
            }
        });
    }

    private void initTable() {

        dtm.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {

                btnDelete.setEnabled(false);

                for (int i = 0; i < dtm.getRowCount(); i++) {

                    if ((Boolean) dtm.getValueAt(i, 0) == true) {
                        btnDelete.setEnabled(true);
                        return;
                    }

                }

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
        if (cmbMajorCategory.getSelectedIndex() == -1) {
            return;
        }

        try {
            SubCategoryController controller = (SubCategoryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SUB_CATEOGRY);
            ArrayList<SubCategoryController.SubCategoryModel> allSubCategories = controller.getAllSubCategoriesByMajorCategoryName(cmbMajorCategory.getSelectedItem().toString());
            if (allSubCategories == null) {
                return;
            }

            for (SubCategoryController.SubCategoryModel subCategory : allSubCategories) {
                cmbSubCategory.addItem(subCategory.getSubCategoryName());
            }

            txtItem.setText("");
            txtBarcode.setText("");
//            txtSize.setText("");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SubCategory.class.getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(SubCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDataInToTable() {

        dtm.setRowCount(0);

        if (cmbMajorCategory.getSelectedIndex() == -1 || cmbSubCategory.getSelectedIndex() == -1) {
            return;
        }

        ItemController controller = (ItemController) ControllerFactory.getInstance().getController(SuperController.ControllerType.ITEM);

        try {
            ArrayList<ItemController.ItemModel> items = controller.getItems(cmbMajorCategory.getSelectedItem().toString(), cmbSubCategory.getSelectedItem().toString());

            if (items == null) {
                return;
            }

            for (ItemController.ItemModel item : items) {
                Object[] rowData = {false, item.getItemCode(), item.getItemName(), item.getBarcode(),  "<html>"
                    + "<p style='color:blue'>Edit</p></html>"};
                dtm.addRow(rowData);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ItemCategory.class.getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {

            Logger.getLogger(ItemCategory.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

//    private void loadDataInToItemsCombo() {
//
//        cmbItem.removeAllItems();
//
//        ItemController controller = (ItemController) ControllerFactory.getInstance().getController(SuperController.ControllerType.ITEM);
//
//        try {
//            ArrayList<ItemController.ItemModel> allItems = controller.getAllItems();
//
//            if (allItems == null) {
//                return;
//            }
//
//            for (ItemController.ItemModel item : allItems) {
//                cmbItem.addItem(item.getItemName());
//            }
//
//            cmbItem.setSelectedIndex(-1);
//
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(ItemCategory.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(ItemCategory.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    private boolean isItemNameExists() {

//        if (update) {
//            for (int i = 0; i < cmbItem.getItemCount(); i++) {
//                if (txtItem.getText().trim().equalsIgnoreCase(cmbItem.getItemAt(i))) {
//                    return true;
//                }
//            }
//            return false;
//        }
        for (int i = 0; i < dtm.getRowCount(); i++) {
            String itemName = (String) dtm.getValueAt(i, 2);

            if (itemName.equals(txtItem.getText().trim())) {
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
        jLabel5 = new javax.swing.JLabel();
        cmbItem = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtBarcode = new javax.swing.JTextField();
        scrPaneOfTable = new javax.swing.JScrollPane();
        tblItemCategory = new javax.swing.JTable();

        setBackground(new java.awt.Color(232, 235, 241));

        jLabel1.setFont(new java.awt.Font("Open Sans", 1, 24)); // NOI18N
        jLabel1.setText("Item Master");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234), 2));

        jPanel2.setBackground(new java.awt.Color(249, 249, 249));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(233, 233, 233)));

        jLabel2.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel2.setText("List of Items");

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
        btnDelete.setText("Delete");
        btnDelete.setToolTipText("Check one ore more item(s) to delete");
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
        jLabel3.setToolTipText("");

        cmbMajorCategory.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        cmbMajorCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbMajorCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMajorCategoryItemStateChanged(evt);
            }
        });
        cmbMajorCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMajorCategoryActionPerformed(evt);
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
        jLabel4.setText("Select Sub Category");

        cmbSubCategory.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        cmbSubCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSubCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSubCategoryItemStateChanged(evt);
            }
        });
        cmbSubCategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbSubCategoryKeyPressed(evt);
            }
        });

        jLabel5.setDisplayedMnemonic('I');
        jLabel5.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel5.setText("Enter Item Name");

        cmbItem.setEditable(true);
        cmbItem.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        cmbItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbItemActionPerformed(evt);
            }
        });
        cmbItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbItemKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel6.setText("Barcode");

        txtBarcode.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtBarcode.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtBarcodeCaretUpdate(evt);
            }
        });
        txtBarcode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBarcodeFocusGained(evt);
            }
        });
        txtBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBarcodeActionPerformed(evt);
            }
        });
        txtBarcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBarcodeKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbMajorCategory, 0, 561, Short.MAX_VALUE)
                    .addComponent(cmbSubCategory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbItem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddNew))
                    .addComponent(txtBarcode)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddNew, btnDelete});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbMajorCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbSubCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete)
                    .addComponent(btnAddNew, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnAddNew, btnDelete});

        scrPaneOfTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tblItemCategory.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        tblItemCategory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Item Code", "Item Name", "Barcode", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblItemCategory.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblItemCategory.setGridColor(new java.awt.Color(233, 236, 242));
        tblItemCategory.setRowHeight(50);
        tblItemCategory.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblItemCategory.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblItemCategory.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblItemCategory.setShowVerticalLines(false);
        tblItemCategory.getTableHeader().setReorderingAllowed(false);
        tblItemCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblItemCategoryMouseClicked(evt);
            }
        });
        tblItemCategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblItemCategoryKeyPressed(evt);
            }
        });
        scrPaneOfTable.setViewportView(tblItemCategory);
        if (tblItemCategory.getColumnModel().getColumnCount() > 0) {
            tblItemCategory.getColumnModel().getColumn(0).setMinWidth(40);
            tblItemCategory.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblItemCategory.getColumnModel().getColumn(0).setMaxWidth(40);
            tblItemCategory.getColumnModel().getColumn(1).setMinWidth(100);
            tblItemCategory.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblItemCategory.getColumnModel().getColumn(1).setMaxWidth(100);
            tblItemCategory.getColumnModel().getColumn(3).setMinWidth(200);
            tblItemCategory.getColumnModel().getColumn(3).setPreferredWidth(200);
            tblItemCategory.getColumnModel().getColumn(3).setMaxWidth(200);
            tblItemCategory.getColumnModel().getColumn(4).setMinWidth(100);
            tblItemCategory.getColumnModel().getColumn(4).setPreferredWidth(100);
            tblItemCategory.getColumnModel().getColumn(4).setMaxWidth(100);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scrPaneOfTable, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(scrPaneOfTable, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
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

    private void tblItemCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblItemCategoryKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (dtm.getRowCount() > 0) {
                boolean currentStatus = (boolean) dtm.getValueAt(tblItemCategory.getSelectedRow(), 0);
                dtm.setValueAt(!currentStatus, tblItemCategory.getSelectedRow(), 0);

            }
        }
    }//GEN-LAST:event_tblItemCategoryKeyPressed

    private void cmbMajorCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMajorCategoryKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbSubCategory.requestFocusInWindow();
        }
    }//GEN-LAST:event_cmbMajorCategoryKeyPressed

    private void cmbSubCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbSubCategoryKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbItem.requestFocusInWindow();
        }
    }//GEN-LAST:event_cmbSubCategoryKeyPressed

    private void cmbItemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbItemKeyPressed
//        if (evt.getKeyCode() == KeyEvent.VK_ENTER){
//            txtBarcode.requestFocusInWindow();
//        }
    }//GEN-LAST:event_cmbItemKeyPressed

    private void txtBarcodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarcodeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//            txtSize.requestFocusInWindow();
            btnAddNew.doClick();
        }
    }//GEN-LAST:event_txtBarcodeKeyPressed

    private void cmbMajorCategoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMajorCategoryItemStateChanged
        loadDataInToSubCombo();
    }//GEN-LAST:event_cmbMajorCategoryItemStateChanged

    private void btnAddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNewActionPerformed

        if (cmbMajorCategory.getSelectedIndex() == -1 || cmbSubCategory.getSelectedIndex() == -1) {
            return;
        }

        ItemController controller = (ItemController) ControllerFactory.getInstance().getController(SuperController.ControllerType.ITEM);

        try {

            if (!update) {

                if (!isItemNameExists()) {
                    controller.saveItem(cmbMajorCategory.getSelectedItem().toString(), cmbSubCategory.getSelectedItem().toString(), txtItem.getText(), "", txtBarcode.getText());
                }else{
                    JOptionPane.showMessageDialog(this,"Sorry, item name is already exsits");
                }

            } else {
                // Update code goes here

                int result = controller.changeItem((String) dtm.getValueAt(tblItemCategory.getSelectedRow(), 2), (String) dtm.getValueAt(tblItemCategory.getSelectedRow(), 1), txtItem.getText(), "", txtBarcode.getText());
                System.out.println(result);
                if (result == -100) {
                    //JOptionPane.showMessageDialog(this, "Sorry, item name can't be changed since it has been used by other categories");
                }else if (result == -200){
                    //JOptionPane.showMessageDialog(this, "Sorry, the item name you have entered is already there");
                }

            }

            loadDataInToTable();
//            loadDataInToItemsCombo();

            // Scroll down to last record
            if (!update) {
                tblItemCategory.scrollRectToVisible(tblItemCategory.getCellRect(dtm.getRowCount() - 1, 0, true));

            }

            txtItem.setText("");
            txtBarcode.setText("");
//            txtSize.setText("");
            txtItem.requestFocusInWindow();
            btnAddNew.setEnabled(false);
            update = false;

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ItemCategory.class.getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(ItemCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAddNewActionPerformed

    private void cmbMajorCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMajorCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMajorCategoryActionPerformed

    private void cmbSubCategoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSubCategoryItemStateChanged
        loadDataInToTable();
        txtItem.setText("");
        txtBarcode.setText("");
//        txtSize.setText("");
    }//GEN-LAST:event_cmbSubCategoryItemStateChanged

    private void tblItemCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemCategoryMouseClicked
        int col = tblItemCategory.columnAtPoint(evt.getPoint());
        if (col != 4) {
            txtItem.setText("");
            txtBarcode.setText("");
//            txtSize.setText("");
            update = false;
            btnAddNew.setEnabled(false);
            return;
        }
        txtItem.setText((String) dtm.getValueAt(tblItemCategory.getSelectedRow(), 2));
        txtItem.setSelectionStart(0);
        txtItem.setSelectionEnd(txtItem.getText().length());
        txtItem.requestFocusInWindow();

        txtBarcode.setText((String) dtm.getValueAt(tblItemCategory.getSelectedRow(), 3));
//        txtSize.setText((String) dtm.getValueAt(tblItemCategory.getSelectedRow(), 4));
        update = true;
    }//GEN-LAST:event_tblItemCategoryMouseClicked

    private void txtBarcodeCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtBarcodeCaretUpdate
//        if (update) {
//            if (txtItem.getText().length() > 0) {
//                if (cmbMajorCategory.getSelectedIndex() != -1 && cmbSubCategory.getSelectedIndex() != -1) {
//                    if (!isItemNameExists()) {
//                        btnAddNew.setEnabled(true);
//                    } else if (txtItem.getText().trim().equalsIgnoreCase((String) dtm.getValueAt(tblItemCategory.getSelectedRow(), 2))) {
//                        btnAddNew.setEnabled(true);
//                    } else {
//                        btnAddNew.setEnabled(false);
//                    }
//                }
//            } else {
//                btnAddNew.setEnabled(false);
//            }
//        }

        if (update) {
            if (txtItem.getText().length() > 0) {
                if (cmbMajorCategory.getSelectedIndex() != -1 && cmbSubCategory.getSelectedIndex() != -1) {
                    btnAddNew.setEnabled(true);
                }
            } else {
                btnAddNew.setEnabled(false);
            }
        }

    }//GEN-LAST:event_txtBarcodeCaretUpdate

    private void cmbItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbItemActionPerformed

    private void txtBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarcodeActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        ItemController ctrl = (ItemController) ControllerFactory.getInstance().getController(SuperController.ControllerType.ITEM);
        tblItemCategory.editingCanceled(null);
        for (int i = 0; i < dtm.getRowCount(); i++) {
            
            boolean delete = (boolean) dtm.getValueAt(i, 0);
            
            //<editor-fold defaultstate="collapsed" desc="if delete">
            if (delete){
                
                String itemCode = (String) dtm.getValueAt(i, 1);
                ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/stop.png"));
                try {
                    
                    boolean success = ctrl.removeItem(itemCode);
                    if (!success){
                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                                "Item Code: '" + itemCode + "' can not be deleted right now due to some unexpected reason. Please try again later.",
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
                                "Item Code: '" + itemCode + "' can not be deleted because it is related with some GRNs or Sales Orders. Please contact the Admin.",
                                "Delete Failed",
                                JOptionPane.ERROR_MESSAGE,
                                icon);
                        break;                        
                    }else{
                        ex.printStackTrace();
                    }
                }
            }
            //</editor-fold>
        }
        
//        loadDataInToItemsCombo();    
        loadDataInToTable();        
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void txtBarcodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBarcodeFocusGained
        txtBarcode.selectAll();
    }//GEN-LAST:event_txtBarcodeFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddNew;
    private javax.swing.JButton btnDelete;
    private javax.swing.JComboBox<String> cmbItem;
    private javax.swing.JComboBox<String> cmbMajorCategory;
    private javax.swing.JComboBox<String> cmbSubCategory;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane scrPaneOfTable;
    private javax.swing.JTable tblItemCategory;
    private javax.swing.JTextField txtBarcode;
    // End of variables declaration//GEN-END:variables

}
