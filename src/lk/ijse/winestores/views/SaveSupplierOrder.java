/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.   
 */
package lk.ijse.winestores.views;

import com.jidesoft.swing.AutoCompletion;
import java.awt.Cursor;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import lk.ijse.winestores.controller.ControllerFactory;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.controller.custom.ItemController;
import lk.ijse.winestores.controller.custom.NewItemController;
import lk.ijse.winestores.controller.custom.QueryController;
import lk.ijse.winestores.controller.custom.SubCategoryController;
import lk.ijse.winestores.controller.custom.SupplierController;
import lk.ijse.winestores.controller.custom.SupplierOrderController;
import lk.ijse.winestores.dao.dto.SubCategoryDTO;
import lk.ijse.winestores.dao.dto.SupplierDTO;
import lk.ijse.winestores.dao.dto.SupplierOrderDTO;
import lk.ijse.winestores.dao.dto.SupplierOrderDetailDTO;
import lk.ijse.winestores.views.util.Extension;

/**
 *
 * @author Ranjith Suranga
 */
public class SaveSupplierOrder extends javax.swing.JPanel implements Extension{

    private SuraButton sbtn;                                                    // Holds the SuraButton Instance
    private SuraTable stbl;                                                     // Holds the SuraTable Instance
    private DefaultTableModel dtm;

    private String extensionName;
    
    private boolean changed;
    
    // Dependencies
    private SupplierController supplierCtrl;
    private SupplierOrderController supplierOrderCtrl;
    private SubCategoryController subCategoryCtrl;
    private ItemController itemCtrl;
    private NewItemController newItemCtrl;
    private QueryController queryCtrl;

    private ArrayList<ItemController.ItemModel> currentItems;                   // This has all the current items in the combo
    private int supplierOrderId;

    public SaveSupplierOrder() {
        init();
        pnlProtectedView.setVisible(false);
        
        extensionName = "New Supplier Order";

        try {

            // Setting new supplier order ID
            supplierOrderId = supplierOrderCtrl.getLastSupplierOrderId();

            if (supplierOrderId == -1) {
                supplierOrderId = 1;
            } else {
                supplierOrderId++;
            }
            lblSupplierOrderId.setText(String.valueOf(supplierOrderId));

            // Setting the order date
            lblSupplierOrderDate.setText(this.formatDate(new Date()));

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SaveSupplierOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SaveSupplierOrder.class.getName()).log(Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                cmbSupplier.requestFocusInWindow();
            }
        });
        
    }

    public SaveSupplierOrder(int supplierOrderId) {
        init();
        
        extensionName = "Edit Supplier Order";

        // Setting supplier order id
        this.supplierOrderId = supplierOrderId;
        lblSupplierOrderId.setText(String.valueOf(this.supplierOrderId));

        try {

            SupplierOrderDTO supplierOrder = queryCtrl.getSupplierOrder(supplierOrderId);

            // Setting supplier order date
            lblSupplierOrderDate.setText(formatDate(supplierOrder.getOrderDate()));

            // Setting supplier
            cmbSupplier.setSelectedItem(supplierCtrl.getSupplierById(supplierOrder.getSupplierId()).getName());

            // Loading order details 
            for (SupplierOrderDetailDTO dto : supplierOrder.getOrderDetails()) {

                BigDecimal bdTotal = null;
                if (dto.getBuyingPrice() != null) {
                    BigDecimal bdQty = new BigDecimal(dto.getQty());
                    if (!dto.getBuyingPrice().toString().equals("0.00")){
                        bdTotal = bdQty.multiply(dto.getBuyingPrice());
                    }
                }

                String itemName = queryCtrl.getItemNameByItemCode(dto.getItemCode());

                Object[] rowData = {
                    false,
                    dto.getItemCode(),
                    itemName,
                    String.valueOf(dto.getQty()),
                    (!dto.getBuyingPrice().toString().equals("0.00")) ?  dto.getBuyingPrice().toString() : "",
                    (bdTotal != null) ? formatPrice(bdTotal) : ""
                };

                dtm.addRow(rowData);

            }

            calculateTotalPrice();

            // Select the first item in the table
            tblItems.getSelectionModel().setSelectionInterval(0, 0);

            enableEditing(false);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SaveSupplierOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SaveSupplierOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    * All the initialization process is here
     */
    public void init() {

        initComponents();

        sbtn = new SuraButton(this);
        sbtn.convertAllJButtonsToSuraButtons();

        stbl = new SuraTable(tblItems);
        dtm = (DefaultTableModel) tblItems.getModel();

        for (int i = 0; i < tblItems.getColumnCount(); i++) {

            if (i == 2) {
                continue;
            }
            stbl.setHeaderAlignment(i, SwingConstants.CENTER);
        }

        for (int i = 0; i < tblItems.getColumnCount() - 1; i++) {
            if ((i == 0) || (i == 2)) {
                stbl.setColumnAlignment(i, SwingConstants.CENTER);
            } else if (i == 1) {
                stbl.setColumnAlignment(i, SwingConstants.LEFT);
            } else {
                stbl.setColumnAlignment(i, SwingConstants.RIGHT);
            }

        }

        // Initializing controllers
        supplierCtrl = (SupplierController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SUPPLIER);
        supplierOrderCtrl = (SupplierOrderController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SUPPLIER_ORDER);
        subCategoryCtrl = (SubCategoryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SUB_CATEOGRY);
        itemCtrl = (ItemController) ControllerFactory.getInstance().getController(SuperController.ControllerType.ITEM);
        newItemCtrl = (NewItemController) ControllerFactory.getInstance().getController(SuperController.ControllerType.NEW_ITEM);
        queryCtrl = (QueryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.QUERY);

        // Loading all the suppliers        
        loadAllSuppliers(null);

        AutoCompletion suggestMaster = new AutoCompletion(cmbItem);
        suggestMaster.setStrict(true);

        cmbItem.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent evt) {

                super.keyPressed(evt);

                if ((evt.isControlDown()) && (evt.getKeyCode() == KeyEvent.VK_SPACE)) {
                    cmbItem.showPopup();
                }

                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtQty.requestFocusInWindow();
                }

            }

        });

        cmbItem.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);

                cmbItem.getEditor().selectAll();
            }

        });

        dtm.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {

                btnDelete.setEnabled(false);

                if (!pnlProtectedView.isVisible()) {
                    btnSave.setEnabled(dtm.getRowCount() > 0);

                    for (int i = 0; i < dtm.getRowCount(); i++) {
                        boolean isChecked = (boolean) dtm.getValueAt(i, 0);

                        if (isChecked) {
                            btnDelete.setEnabled(true);
                            break;
                        }

                    }

                }

            }
        });

        tblItems.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (tblItems.getSelectedRow() == -1) {
                    return;
                }

                try {

                    SubCategoryDTO subCategory = queryCtrl.getSubCategoryByItemCode(tblItems.getValueAt(tblItems.getSelectedRow(), 1).toString());

                    if (subCategory != null) {

                        btnAddItem.setText("Update");
                        cmbSubCategory.setSelectedItem(subCategory.getSubCategoryName());
                        cmbItem.setSelectedItem(dtm.getValueAt(tblItems.getSelectedRow(), 2).toString());
                        txtQty.setText(dtm.getValueAt(tblItems.getSelectedRow(), 3).toString());
                        txtBuyingPrice.setText(dtm.getValueAt(tblItems.getSelectedRow(), 4).toString());

                        cmbSubCategory.requestFocusInWindow();
                        enableAddItem();
                    }

                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SaveSupplierOrder.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(SaveSupplierOrder.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

    }

    /*
    * Parameters:
    * selectedSupplierName - supplier name to be selected in the combo box, if null first supplier will be selected
     */
    private void loadAllSuppliers(String selectedSupplierName) {

        try {

            ArrayList<SupplierController.SupplierModel> allSuppliers = supplierCtrl.getAllSuppliers();

            if (allSuppliers != null) {

                for (SupplierController.SupplierModel supplier : allSuppliers) {
                    cmbSupplier.addItem(supplier.getName());
                }

                if (selectedSupplierName != null) {
                    cmbSupplier.setSelectedItem(selectedSupplierName);
                } else {
                    cmbSupplier.setSelectedIndex(-1);
                }
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SaveSupplierOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SaveSupplierOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private String getCurrentItemCode() {

        for (ItemController.ItemModel item : currentItems) {

            if (item.getItemName().equalsIgnoreCase(cmbItem.getSelectedItem().toString())) {
                return item.getItemCode();
            }

        }

        return null;

    }

    private String formatPrice(Object price) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setGroupingUsed(false);
        return nf.format(price);
    }

    private void enableAddItem() {
        btnAddItem.setEnabled(false);

        if (pnlProtectedView.isVisible()) {
            return;
        }

        if (!txtQty.getText().isEmpty()) {
            if (cmbSubCategory.getSelectedIndex() != -1
                    && cmbItem.getSelectedIndex() != -1
                    && Integer.parseInt(txtQty.getText()) > 0) {
                if (txtBuyingPrice.getText().trim().isEmpty()) {
                    btnAddItem.setEnabled(true);
                } else if (txtBuyingPrice.getText().matches("\\d+(\\.\\d{2})?")) {
                    btnAddItem.setEnabled(true);
                }

            }
        }
    }

    private void calculateTotalPrice() {

        BigDecimal total = null;

        for (int i = 0; i < dtm.getRowCount(); i++) {

            String totalBuyingPrice = dtm.getValueAt(i, 5).toString();

            if (!totalBuyingPrice.trim().isEmpty()) {

                if (total == null) {
                    total = new BigDecimal(totalBuyingPrice);
                } else {
                    total = total.add(new BigDecimal(totalBuyingPrice));
                }

            }

        }

        if (total != null) {
            lblOrderTotal.setText("Order Total : " + formatPrice(total));
        } else {
            lblOrderTotal.setText("Order Total : -");
        }

    }

    private void enableEditing(boolean enable) {

        cmbSupplier.setEnabled(enable);
        txtMajorCategory.setEnabled(enable);
        cmbSubCategory.setEnabled(enable);
        cmbItem.setEnabled(enable);
        txtQty.setEnabled(enable);
        txtBuyingPrice.setEnabled(enable);

        if (!enable) {
            btnAddItem.setEnabled(enable);
            btnDelete.setEnabled(enable);
            btnSave.setEnabled(enable);
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

        jPanel2 = new javax.swing.JPanel();
        pnlProtectedView = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblSupplierOrderId = new javax.swing.JLabel();
        lblSupplierOrderDate = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbSupplier = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cmbSubCategory = new javax.swing.JComboBox<>();
        cmbItem = new javax.swing.JComboBox<>();
        btnAddItem = new javax.swing.JButton();
        pnlTableContainer = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblItems = new javax.swing.JTable();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        txtBuyingPrice = new javax.swing.JTextField();
        lblOrderTotal = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtQty = new javax.swing.JTextField();
        txtStockQty = new javax.swing.JTextField();
        txtMajorCategory = new javax.swing.JTextField();

        setBackground(new java.awt.Color(233, 236, 242));
        setForeground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234), 2));
        jPanel2.setAutoscrolls(true);

        pnlProtectedView.setBackground(new java.awt.Color(252, 247, 182));

        jLabel1.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/warning.png"))); // NOI18N
        jLabel1.setText("PROTECTED VIEW");

        jLabel2.setFont(new java.awt.Font("Open Sans", 0, 11)); // NOI18N
        jLabel2.setText("Be careful before you edit");

        btnEdit.setBackground(new java.awt.Color(72, 158, 231));
        btnEdit.setFont(new java.awt.Font("Open Sans", 0, 11)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Enable Editing");
        btnEdit.setToolTipText("Click to enable editing");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlProtectedViewLayout = new javax.swing.GroupLayout(pnlProtectedView);
        pnlProtectedView.setLayout(pnlProtectedViewLayout);
        pnlProtectedViewLayout.setHorizontalGroup(
            pnlProtectedViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProtectedViewLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEdit)
                .addContainerGap())
        );
        pnlProtectedViewLayout.setVerticalGroup(
            pnlProtectedViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlProtectedViewLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlProtectedViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlProtectedViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel1))
                    .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(249, 249, 249));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(220, 225, 234)));

        jLabel3.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/edit.png"))); // NOI18N
        jLabel3.setText("Supplier Order Information");
        jLabel3.setIconTextGap(10);

        jLabel4.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Supplier Order No :");

        lblSupplierOrderId.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N

        lblSupplierOrderDate.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblSupplierOrderDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSupplierOrderDate.setText("jLabel17");

        jLabel5.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Order Date : ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSupplierOrderId, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSupplierOrderDate)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(lblSupplierOrderId)
                    .addComponent(lblSupplierOrderDate)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setDisplayedMnemonic('S');
        jLabel6.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setLabelFor(cmbSupplier);
        jLabel6.setText("Supplier :");
        jLabel6.setToolTipText("");

        cmbSupplier.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        cmbSupplier.setToolTipText("Click to select the supplier");
        cmbSupplier.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSupplierItemStateChanged(evt);
            }
        });
        cmbSupplier.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSupplierFocusGained(evt);
            }
        });
        cmbSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSupplierActionPerformed(evt);
            }
        });
        cmbSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbSupplierKeyPressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Major Category :");

        jLabel10.setDisplayedMnemonic('C');
        jLabel10.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setLabelFor(cmbSubCategory);
        jLabel10.setText("Sub Category :");

        jLabel11.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Item :");

        jLabel12.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Stock Qty. :");

        jLabel13.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Buying Price :");

        cmbSubCategory.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        cmbSubCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSubCategoryItemStateChanged(evt);
            }
        });
        cmbSubCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSubCategoryActionPerformed(evt);
            }
        });
        cmbSubCategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbSubCategoryKeyPressed(evt);
            }
        });

        cmbItem.setEditable(true);
        cmbItem.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        cmbItem.setToolTipText("Press (Ctrl+Space) to open up the item list");
        cmbItem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbItemItemStateChanged(evt);
            }
        });
        cmbItem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbItemFocusGained(evt);
            }
        });
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

        btnAddItem.setBackground(new java.awt.Color(72, 158, 231));
        btnAddItem.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnAddItem.setForeground(new java.awt.Color(255, 255, 255));
        btnAddItem.setText("Add Item");
        btnAddItem.setEnabled(false);
        btnAddItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddItemMouseClicked(evt);
            }
        });
        btnAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddItemActionPerformed(evt);
            }
        });

        pnlTableContainer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234), 2));

        tblItems.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        tblItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Item Code", "Item", "Qty.", "Buying Price", "Total Buying Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblItems.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblItems.setGridColor(new java.awt.Color(233, 236, 242));
        tblItems.setRowHeight(50);
        tblItems.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblItems.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblItems.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblItems.getTableHeader().setReorderingAllowed(false);
        tblItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblItemsMouseClicked(evt);
            }
        });
        tblItems.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblItemsKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblItems);
        if (tblItems.getColumnModel().getColumnCount() > 0) {
            tblItems.getColumnModel().getColumn(0).setMinWidth(40);
            tblItems.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblItems.getColumnModel().getColumn(0).setMaxWidth(40);
            tblItems.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblItems.getColumnModel().getColumn(2).setPreferredWidth(400);
            tblItems.getColumnModel().getColumn(3).setPreferredWidth(60);
            tblItems.getColumnModel().getColumn(4).setPreferredWidth(125);
            tblItems.getColumnModel().getColumn(5).setPreferredWidth(125);
        }

        javax.swing.GroupLayout pnlTableContainerLayout = new javax.swing.GroupLayout(pnlTableContainer);
        pnlTableContainer.setLayout(pnlTableContainerLayout);
        pnlTableContainerLayout.setHorizontalGroup(
            pnlTableContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableContainerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1)
                .addGap(0, 0, 0))
        );
        pnlTableContainerLayout.setVerticalGroup(
            pnlTableContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableContainerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        btnSave.setBackground(new java.awt.Color(72, 158, 231));
        btnSave.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setMnemonic('a');
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
        btnDelete.setToolTipText("Check one more items in the table to delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        txtBuyingPrice.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtBuyingPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBuyingPrice.setToolTipText("Entering buying price is optional");
        txtBuyingPrice.setEnabled(false);
        txtBuyingPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuyingPriceFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBuyingPriceFocusLost(evt);
            }
        });
        txtBuyingPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuyingPriceKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuyingPriceKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuyingPriceKeyTyped(evt);
            }
        });

        lblOrderTotal.setFont(new java.awt.Font("Open Sans", 1, 18)); // NOI18N
        lblOrderTotal.setText("Order Total : -");

        jLabel7.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel7.setText("Qty. :");

        txtQty.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQty.setToolTipText("Qty. should be more than zero");
        txtQty.setEnabled(false);
        txtQty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQtyFocusGained(evt);
            }
        });
        txtQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtQtyKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQtyKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtQtyKeyTyped(evt);
            }
        });

        txtStockQty.setEditable(false);
        txtStockQty.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtStockQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtMajorCategory.setEditable(false);
        txtMajorCategory.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlProtectedView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlTableContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbSupplier, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblOrderTotal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMajorCategory))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbSubCategory, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbItem, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(txtStockQty, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBuyingPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnAddItem, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 24, Short.MAX_VALUE)))))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddItem, btnDelete});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtBuyingPrice, txtQty});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(pnlProtectedView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel6))
                    .addComponent(cmbSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtMajorCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbSubCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuyingPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12)
                    .addComponent(jLabel7)
                    .addComponent(txtQty)
                    .addComponent(txtStockQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlTableContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOrderTotal))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnAddItem, btnDelete});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmbSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSupplierActionPerformed

    }//GEN-LAST:event_cmbSupplierActionPerformed

    private void cmbSubCategoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSubCategoryItemStateChanged

        changed = true;
        enableAddItem();
        cmbItem.removeAllItems();

        if (cmbSubCategory.getSelectedIndex() == -1) {
            return;
        }

        try {

            currentItems = itemCtrl.getItems(txtMajorCategory.getText().trim(), cmbSubCategory.getSelectedItem().toString().trim());

            if (currentItems != null) {

                for (ItemController.ItemModel item : currentItems) {
                    cmbItem.addItem(item.getItemName());
                }

                cmbItem.setSelectedIndex(-1);

            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SaveSupplierOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SaveSupplierOrder.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_cmbSubCategoryItemStateChanged

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed

        changed = true;
        String currentItemCode = getCurrentItemCode();

        BigDecimal bdTotal = null;
        if (!txtBuyingPrice.getText().trim().isEmpty()) {
            txtBuyingPrice.setText(this.formatPrice(Double.valueOf(txtBuyingPrice.getText())));
            BigDecimal bdQty = new BigDecimal(txtQty.getText());
            BigDecimal bdBuyingPrice = new BigDecimal(txtBuyingPrice.getText());
            bdTotal = bdQty.multiply(bdBuyingPrice);
        }

        JCheckBox chk = (JCheckBox) tblItems.getColumnModel().getColumn(0).getHeaderRenderer().getTableCellRendererComponent(tblItems, null, false, false, 0, 0);

        if (btnAddItem.getText().equals("Add Item")) {

            // Check whether the item is already in the table
            for (int i = 0; i < dtm.getRowCount(); i++) {

                if (dtm.getValueAt(i, 1).toString().equals(currentItemCode)) {

                    ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                            "Item is already in the list, please click on the item to edit",
                            "Dupplicated Items",
                            JOptionPane.ERROR_MESSAGE,
                            icon);
                    tblItems.getSelectionModel().setSelectionInterval(i, i);
                    return;
                }

            }

            Object[] rowData = {
                chk.isSelected(),
                currentItemCode,
                cmbItem.getSelectedItem().toString(),
                txtQty.getText(),
                txtBuyingPrice.getText(),
                (bdTotal != null) ? formatPrice(bdTotal) : ""
            };

            dtm.addRow(rowData);

        } else {

            int row = tblItems.getSelectedRow();

            dtm.setValueAt(chk.isSelected(), row, 0);                                   // Check box
            dtm.setValueAt(currentItemCode, row, 1);                                    // Item Code
            dtm.setValueAt(cmbItem.getSelectedItem().toString(), row, 2);               // Item Name
            dtm.setValueAt(txtQty.getText(), row, 3);                                   // Qty
            dtm.setValueAt(txtBuyingPrice.getText(), row, 4);                           // Buying Price
            dtm.setValueAt((bdTotal != null) ? formatPrice(bdTotal) : "", row, 5);    // Total Buying Price

        }

        calculateTotalPrice();

        cmbItem.setSelectedIndex(-1);
        cmbSubCategory.requestFocusInWindow();

        btnAddItem.setText("Add Item");
        tblItems.clearSelection();

    }//GEN-LAST:event_btnAddItemActionPerformed

    private void btnAddItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddItemMouseClicked

    }//GEN-LAST:event_btnAddItemMouseClicked

    private void cmbItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbItemActionPerformed
    }//GEN-LAST:event_cmbItemActionPerformed

    private void cmbSubCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSubCategoryActionPerformed

    }//GEN-LAST:event_cmbSubCategoryActionPerformed

    private void cmbItemItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbItemItemStateChanged

        changed = true;
        enableAddItem();
        if (cmbItem.getSelectedIndex() == -1) {
            txtStockQty.setText("");
            txtQty.setText("");
            txtBuyingPrice.setText("");
            txtQty.setEnabled(false);
            txtBuyingPrice.setEnabled(false);
            return;
        }

        try {

            int qty = newItemCtrl.getQty(getCurrentItemCode());
            txtStockQty.setText(String.valueOf(qty));

            if (!pnlProtectedView.isVisible()) {
                txtQty.setEnabled(true);
                txtBuyingPrice.setEnabled(true);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SaveSupplierOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SaveSupplierOrder.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_cmbItemItemStateChanged

    private void cmbSubCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbSubCategoryKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbItem.requestFocusInWindow();
        }
    }//GEN-LAST:event_cmbSubCategoryKeyPressed

    private void cmbItemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbItemKeyPressed

    }//GEN-LAST:event_cmbItemKeyPressed

    private void tblItemsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblItemsKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {

            if (tblItems.getRowCount() > 0) {
                boolean currentStatus = (boolean) tblItems.getValueAt(tblItems.getSelectedRow(), 0);
                tblItems.setValueAt(!currentStatus, tblItems.getSelectedRow(), 0);

            }
        }
    }//GEN-LAST:event_tblItemsKeyPressed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed

        changed = true;
        // A little hack
        tblItems.editingCanceled(null);

        btnAddItem.setText("Add Item");

        for (int i = 0; i < dtm.getRowCount(); i++) {
            boolean delete = (boolean) dtm.getValueAt(i, 0);

            if (delete) {
                dtm.removeRow(i);
                i--;
            }
        }

        tblItems.clearSelection();

        cmbItem.setSelectedIndex(-1);
        cmbSubCategory.requestFocusInWindow();

        btnDelete.setEnabled(false);

    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemsMouseClicked

    }//GEN-LAST:event_tblItemsMouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        Cursor cursor = this.getCursor();
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {

            ArrayList<SupplierDTO> suppliers = supplierCtrl.getSuppliers(cmbSupplier.getSelectedItem().toString());

            if (suppliers.size() == 1) {

                String orderTotal = lblOrderTotal.getText().split(":")[1];

                SupplierOrderDTO supplierOrderDTO = new SupplierOrderDTO(
                        supplierOrderId,
                        suppliers.get(0).getSupplierId(),
                        new Date(),
                        "Admin",
                        orderTotal
                );

                ArrayList<SupplierOrderDetailDTO> supplierOrderDetails = new ArrayList<>();

                for (int i = 0; i < dtm.getRowCount(); i++) {

                    SupplierOrderDetailDTO dto = new SupplierOrderDetailDTO(null,
                            supplierOrderId,
                            dtm.getValueAt(i, 1).toString(),
                            Integer.parseInt(dtm.getValueAt(i, 3).toString()),
                            dtm.getValueAt(i, 4).toString().trim().isEmpty() ? null : new BigDecimal(dtm.getValueAt(i, 4).toString())
                    );

                    supplierOrderDetails.add(dto);

                }

                boolean success = supplierOrderCtrl.saveSupplierOrder(supplierOrderDTO, supplierOrderDetails);

                if (success) {

                    ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/ok.png"));
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                            "Supplier order has been successfully saved !",
                            "Saved Success",
                            JOptionPane.INFORMATION_MESSAGE,
                            icon);

                    Main m = (Main) SwingUtilities.getWindowAncestor(this);
                    m.resetMenu(Main.MenuItems.SUPPLIER_ORDER);
//                    m.pnlContainer.removeAll();
//                    SupplierOrders supplierOrders = new SupplierOrders();
//                    m.pnlContainer.add(supplierOrders);
//                    m.pnlContainer.updateUI();

                } else {

                    ImageIcon icon = new ImageIcon(this.getClass().getResource("../error_icon.png"));
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                            "Sorry, we coudn't able to save this order right now, please try again",
                            "Save Failed",
                            JOptionPane.ERROR_MESSAGE,
                            icon);

                }
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SaveSupplierOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SaveSupplierOrder.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.setCursor(cursor);
        }

    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        enableEditing(true);
        cmbSupplier.requestFocusInWindow();
        pnlProtectedView.setVisible(false);
        enableAddItem();
        
        for (int i = 0; i < dtm.getRowCount(); i++) {
            boolean delete = (boolean) dtm.getValueAt(i, 0);
            if (delete){
                btnDelete.setEnabled(true);
                break;
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void cmbSupplierFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSupplierFocusGained
        cmbSupplier.getEditor().selectAll();
    }//GEN-LAST:event_cmbSupplierFocusGained

    private void cmbSupplierItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSupplierItemStateChanged

        changed = true;
        
        if (cmbSupplier.getSelectedIndex() == -1) {
            txtMajorCategory.setText("");
            cmbSubCategory.removeAllItems();
            return;
        }

        // Setting Major Category Name
        String supplierName = cmbSupplier.getSelectedItem().toString();
        String majorCategory = supplierName.substring(supplierName.lastIndexOf("(") + 1, supplierName.lastIndexOf(")"));
        txtMajorCategory.setText(majorCategory);

        try {
            // Loading all the subcategories for this major category
            ArrayList<SubCategoryController.SubCategoryModel> subCategories = subCategoryCtrl.getAllSubCategoriesByMajorCategoryName(majorCategory);

            if (subCategories != null) {

                cmbSubCategory.removeAllItems();

                for (SubCategoryController.SubCategoryModel subCategory : subCategories) {
                    cmbSubCategory.addItem(subCategory.getSubCategoryName());
                }

                cmbSubCategory.setSelectedIndex(0);

                // Resetting
                dtm.setRowCount(0);
                btnAddItem.setText("Add Item");
                enableAddItem();
                btnDelete.setEnabled(false);

            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SaveSupplierOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SaveSupplierOrder.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_cmbSupplierItemStateChanged

    private void cmbSupplierKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbSupplierKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbSubCategory.requestFocusInWindow();
        }
    }//GEN-LAST:event_cmbSupplierKeyPressed

    private void cmbItemFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbItemFocusGained
        cmbSubCategory.getEditor().selectAll();
    }//GEN-LAST:event_cmbItemFocusGained

    private void txtQtyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusGained
        txtQty.selectAll();
    }//GEN-LAST:event_txtQtyFocusGained

    private void txtQtyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyTyped
        if (!(Character.isDigit(evt.getKeyChar())
                || evt.getKeyChar() == KeyEvent.VK_DELETE
                || evt.getKeyChar() == KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtQtyKeyTyped

    private void txtQtyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyPressed
        if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_V) {
            evt.consume();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtBuyingPrice.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtQtyKeyPressed

    private void txtBuyingPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuyingPriceKeyTyped

        if (!(Character.isDigit(evt.getKeyChar())
                || evt.getKeyChar() == KeyEvent.VK_DELETE
                || evt.getKeyChar() == KeyEvent.VK_BACK_SPACE
                || evt.getKeyChar() == KeyEvent.VK_PERIOD)) {

            evt.consume();
        } else {

            String currentPrice = txtBuyingPrice.getText();

            if (currentPrice.contains(".") && evt.getKeyChar() == KeyEvent.VK_PERIOD) {
                if (txtBuyingPrice.getSelectedText() == null) {
                    evt.consume();
                }
            }

        }
    }//GEN-LAST:event_txtBuyingPriceKeyTyped

    private void txtBuyingPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuyingPriceFocusLost
        if (!txtBuyingPrice.getText().isEmpty()) {
            if (txtBuyingPrice.getText().trim().equals(".")) {
                txtBuyingPrice.setText("0");
            }
            txtBuyingPrice.setText(this.formatPrice(Double.valueOf(txtBuyingPrice.getText())));
        }
        if (btnAddItem.isEnabled() == false) {
            enableAddItem();
        }
    }//GEN-LAST:event_txtBuyingPriceFocusLost

    private void txtQtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyReleased
        changed = true;
        enableAddItem();
    }//GEN-LAST:event_txtQtyKeyReleased

    private void txtBuyingPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuyingPriceKeyReleased
        changed = true;
        enableAddItem();
    }//GEN-LAST:event_txtBuyingPriceKeyReleased

    private void txtBuyingPriceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuyingPriceFocusGained
        txtBuyingPrice.selectAll();
    }//GEN-LAST:event_txtBuyingPriceFocusGained

    private void txtBuyingPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuyingPriceKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnAddItem.doClick();
        }
    }//GEN-LAST:event_txtBuyingPriceKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbItem;
    private javax.swing.JComboBox<String> cmbSubCategory;
    private javax.swing.JComboBox<String> cmbSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblOrderTotal;
    private javax.swing.JLabel lblSupplierOrderDate;
    private javax.swing.JLabel lblSupplierOrderId;
    private javax.swing.JPanel pnlProtectedView;
    private javax.swing.JPanel pnlTableContainer;
    private javax.swing.JTable tblItems;
    private javax.swing.JTextField txtBuyingPrice;
    private javax.swing.JTextField txtMajorCategory;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtStockQty;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean exit() {
        if (changed && !pnlProtectedView.isVisible()){
            ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/question.png"));
            int result = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(this),
                    "Some changes have been made. Do you want to exit without saving them ?",
                    "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE,icon);
            return result == JOptionPane.YES_OPTION;
        }else{
            return true;
        }
    }

    @Override
    public String getExtensionName() {
        return extensionName;
    }

}
