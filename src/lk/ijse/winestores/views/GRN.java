/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.    @Override
 * To change body of generated methods, choose Tools | Templates.
 */
package lk.ijse.winestores.views;

import com.jidesoft.swing.AutoCompletion;
import java.awt.Cursor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
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
import lk.ijse.winestores.controller.custom.GRNController;
import lk.ijse.winestores.controller.custom.ItemController;
import lk.ijse.winestores.controller.custom.MajorCategoryController;
import lk.ijse.winestores.controller.custom.SubCategoryController;
import lk.ijse.winestores.controller.custom.SupplierController;
import lk.ijse.winestores.controller.custom.impl.MajorCategoryControllerImpl;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Ranjith Suranga
 */
public class GRN extends javax.swing.JPanel {

    private SuraButton sbtn;                                                    // Holds the SuraButton Instance
    private SuraTable stbl;                                                     // Holds the SuraTable Instance

    private DefaultTableModel dtm;                                              // Holds the Default Table Model
    private ArrayList<SupplierController.SupplierModel> allSuppliers;           // Holds all the suppliers
    private ArrayList<ItemController.ItemModel> subItems;                       // Holds all the items in the sub category
    private ArrayList<GRNController.GRNItemModel> items;                        // Holds all the items in GRN
    private ArrayList<GRNController.GRNEmptyBottleDetailModel> emptyBottles;    // Holds all the empty bottle details for this GRN

    private boolean authorizedStatus = false;                                   // Holds whether the GRN is authorized or not
    private boolean addNewGrn = true;                                           // Holds the key whether to add new GRN or view existing GRN
    private boolean updateStatus = false;                                       // Holds another special status, whether to update or add new

    /**
     * Creates new form NewGRN
     */
    public GRN() {

        initComponents();

        items = new ArrayList<>();

        btnAuthorize.setVisible(false);

        addNewGrn = true;

        if (addNewGrn) {

            pnlProtectedView.setVisible(false);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
            lblGrnDate.setText("GRN Date : " + sdf.format(new Date()));

            loadSuppliersInToCombo();
            loadMajorCategoriesInToCombo();

            txtInvoiceDate.setDate(new Date());

            GRNController controller = (GRNController) ControllerFactory.getInstance().getController(SuperController.ControllerType.GRN);
            try {
                lblGrnId.setText(controller.getNewGrnId());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            pnlProtectedView.setVisible(true);

        }

        sbtn = new SuraButton(this);
        sbtn.convertAllJButtonsToSuraButtons();

        dtm = (DefaultTableModel) tblItems.getModel();
        stbl = new SuraTable(tblItems);

        for (int i = 0; i < dtm.getColumnCount(); i++) {

            if (i == 2) {
                continue;
            }
            stbl.setHeaderAlignment(i, SwingConstants.CENTER);
        }

        for (int i = 0; i < dtm.getColumnCount() - 1; i++) {
            if (i == 0 || i == 2) {
                stbl.setColumnAlignment(i, SwingConstants.CENTER);
            } else if (i == 1) {
                stbl.setColumnAlignment(i, SwingConstants.LEFT);
            } else {
                stbl.setColumnAlignment(i, SwingConstants.RIGHT);
            }

        }

        AutoCompletion acSupplier = new AutoCompletion(cmbSupplier);
        acSupplier.setStrict(false);
        
        AutoCompletion acItem = new AutoCompletion(cmbItem);
        acItem.setStrict(false);
        //AutoCompleteDecorator.decorate(cmbSupplier);
        //AutoCompleteDecorator.decorate(cmbItem);

        handleEvents();

    }

    public GRN(GRNController.GRNDetailsModel currentGrn, boolean isGrnAuthorized) {

        initComponents();

        addNewGrn = false;

        this.authorizedStatus = isGrnAuthorized;

        items = new ArrayList<>();

        loadSuppliersInToCombo();
        loadMajorCategoriesInToCombo();

        sbtn = new SuraButton(this);
        sbtn.convertAllJButtonsToSuraButtons();

        dtm = (DefaultTableModel) tblItems.getModel();
        stbl = new SuraTable(tblItems);

        for (int i = 0; i < dtm.getColumnCount(); i++) {

            if (i == 2) {
                continue;
            }
            stbl.setHeaderAlignment(i, SwingConstants.CENTER);
        }

        for (int i = 0; i < dtm.getColumnCount() - 1; i++) {
            if (i == 0 || i == 2) {
                stbl.setColumnAlignment(i, SwingConstants.CENTER);
            } else if (i == 1) {
                stbl.setColumnAlignment(i, SwingConstants.LEFT);
            } else {
                stbl.setColumnAlignment(i, SwingConstants.RIGHT);
            }

        }

        AutoCompleteDecorator.decorate(cmbSupplier);
        AutoCompleteDecorator.decorate(cmbItem);

        // Filling data
        fillingData(currentGrn, isGrnAuthorized);

        handleEvents();

    }

    private void fillingData(GRNController.GRNDetailsModel currentGrn, boolean isGrnAuthorized) {

        // Filling Basic GRN Data
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date grnDate = sdf.parse(currentGrn.getGrnDate());
            sdf.applyPattern("yyyy-MMM-dd");
            lblGrnDate.setText("GRN Date : " + sdf.format(grnDate));
        } catch (ParseException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        }

        lblGrnId.setText(currentGrn.getGrnId());
        txtInvoiceId.setText(currentGrn.getInvoiceId());
        sdf.applyPattern("yyyy-MM-dd");
        try {
            Date invoiceDate = sdf.parse(currentGrn.getInvoiceDate());
            txtInvoiceDate.setDate(invoiceDate);
        } catch (ParseException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Filling GRN item Data
        GRNController grnCtrl = (GRNController) ControllerFactory.getInstance().getController(SuperController.ControllerType.GRN);
        try {
            ArrayList<GRNController.GRNItemModel> grnItems = grnCtrl.getItemDetails(currentGrn.getGrnId(), isGrnAuthorized);

            if (grnItems == null) {
                return;
            }

            for (GRNController.GRNItemModel grnItem : grnItems) {

                if (grnItem.getExpireDate() == null) {
                    grnItem.setExpireDate("");
                }
                this.items.add(grnItem);
                JCheckBox jcb = (JCheckBox) tblItems.getTableHeader().getColumnModel().getColumn(0).getHeaderRenderer().getTableCellRendererComponent(tblItems, null, true, false, 0, 0);
                String totalCost = formatCurrency(grnItem.getCostPrice().multiply(BigDecimal.valueOf(grnItem.getQty())));
                String totalSelling = formatCurrency(grnItem.getSellingPrice().multiply(BigDecimal.valueOf(grnItem.getQty())));

                Object[] rowData = {jcb.isSelected(), grnItem.getItemCode(), grnItem.getItemName(), String.valueOf(grnItem.getQty()), formatCurrency(grnItem.getCostPrice()), totalCost, formatCurrency(grnItem.getSellingPrice()), totalSelling};
                dtm.addRow(rowData);
            }

            //tblItems.getSelectionModel().setSelectionInterval(0, 0);
            //tblItemsMouseClicked(null);
            // Filling Empty Bottle Details, if any
            ArrayList<GRNController.GRNEmptyBottleDetailModel> emptyBottleDetails = grnCtrl.getEmptyBottleDetails(currentGrn.getGrnId());

            if (emptyBottleDetails != null) {
                this.setEmptyBottles(emptyBottleDetails);
            }

            this.enableEditing(false);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void enableEditing(boolean enable) {

        if (!enable) {
            cmbSupplier.setEnabled(false);
            txtInvoiceId.setEnabled(false);
            txtInvoiceDate.setEnabled(false);
            cmbMajorCateogry.setEnabled(false);
            cmbSubCategory.setEnabled(false);
            cmbItem.setEnabled(false);
            txtQty.setEnabled(false);
            txtCostPrice.setEnabled(false);
            txtSellingPrice.setEnabled(false);
            txtExpireDate.setEnabled(false);
            btnAddItem.setEnabled(false);
            btnDelete.setEnabled(false);
            btnSave.setEnabled(false);
            btnAuthorize.setVisible(true);
            if (!this.authorizedStatus){
                btnAuthorize.setEnabled(true);
            }
        } else {
            cmbSupplier.setEnabled(true);
            txtInvoiceId.setEnabled(true);
            txtInvoiceDate.setEnabled(true);
            cmbMajorCateogry.setEnabled(true);
            cmbSubCategory.setEnabled(true);
            cmbItem.setEnabled(true);
            txtQty.setEnabled(true);
            txtCostPrice.setEnabled(true);
            txtSellingPrice.setEnabled(true);
            txtExpireDate.setEnabled(true);
            btnAddItem.setEnabled(true);
            //btnDelete.setEnabled(true);
            //btnSave.setEnabled(true);            
        }

    }

    private void handleEvents() {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                cmbSupplier.requestFocusInWindow();
            }
        });

        cmbSupplier.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtInvoiceId.requestFocusInWindow();
                }
            }

        });

        txtInvoiceDate.getEditor().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    cmbMajorCateogry.requestFocusInWindow();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                try {
                    txtInvoiceDate.commitEdit();
                } catch (ParseException ex) {
                    Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (!txtInvoiceId.getText().trim().isEmpty() && txtInvoiceDate.getDate() != null && cmbSupplier.getSelectedIndex() != -1) {
                    btnSave.setEnabled(dtm.getRowCount() != 0);
                } else {
                    btnSave.setEnabled(false);
                }
            }

        });

        cmbItem.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtQty.requestFocusInWindow();
                }
                
                if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_SPACE)){
                    cmbItem.showPopup();
                }
            }

        });

        dtm.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {

                if (!txtInvoiceId.getText().trim().isEmpty() && txtInvoiceDate.getDate() != null && cmbSupplier.getSelectedIndex() != -1) {
                    btnSave.setEnabled(dtm.getRowCount() != 0);
                }

                btnDelete.setEnabled(false);

                if (pnlProtectedView.isVisible()) {
                    return;
                }

                for (int i = 0; i < dtm.getRowCount(); i++) {

                    if ((boolean) dtm.getValueAt(i, 0) == true) {
                        btnDelete.setEnabled(true);
                        break;
                    }

                }
            }
        });

        tblItems.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tblItemsMouseClicked(null);
            }
        });

    }

    private void loadSuppliersInToCombo() {

        cmbSupplier.removeAllItems();

        SupplierController controller = (SupplierController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SUPPLIER);

        try {
            allSuppliers = controller.getAllSuppliers();

            if (allSuppliers == null) {
                return;
            }

            for (SupplierController.SupplierModel supplier : allSuppliers) {
                cmbSupplier.addItem(supplier.getName());
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void loadMajorCategoriesInToCombo() {

        cmbMajorCateogry.removeAllItems();

        MajorCategoryController controller = (MajorCategoryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.MAJOR_CATEGORY);

        try {
            ArrayList<MajorCategoryControllerImpl.MajorCategoryModel> allMajorCategories = controller.getAllMajorCategory();

            if (allMajorCategories == null) {
                return;
            }

            for (MajorCategoryControllerImpl.MajorCategoryModel majorCateogry : allMajorCategories) {
                cmbMajorCateogry.addItem(majorCateogry.getCategoryName());
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadSubCategoriesInToCombo() {

        cmbSubCategory.removeAllItems();

        if (cmbMajorCateogry.getSelectedIndex() == -1) {
            return;
        }

        SubCategoryController controller = (SubCategoryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SUB_CATEOGRY);

        try {
            ArrayList<SubCategoryController.SubCategoryModel> allSubCategories = controller.getAllSubCategoriesByMajorCategoryName(cmbMajorCateogry.getSelectedItem().toString());

            if (allSubCategories == null) {
                return;
            }

            for (SubCategoryController.SubCategoryModel subCategory : allSubCategories) {
                cmbSubCategory.addItem(subCategory.getSubCategoryName());
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadItemsInToCombo() {

        cmbItem.removeAllItems();

        if (cmbSubCategory.getSelectedIndex() == -1 || cmbMajorCateogry.getSelectedIndex() == -1) {
            return;
        }

        ItemController controller = (ItemController) ControllerFactory.getInstance().getController(SuperController.ControllerType.ITEM);

        try {
            subItems = controller.getItems(cmbMajorCateogry.getSelectedItem().toString(), cmbSubCategory.getSelectedItem().toString());

            if (subItems == null) {
                return;
            }

            for (ItemController.ItemModel item : subItems) {
                cmbItem.addItem(item.getItemName());
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        }

        cmbItem.setSelectedIndex(-1);

    }

    private void enableAddItemButton() {

        if (cmbItem.getSelectedIndex() != -1 && !pnlProtectedView.isVisible()) {

            if (txtQty.getText().isEmpty() || txtCostPrice.getText().isEmpty() || txtSellingPrice.getText().isEmpty()) {
                return;
            }

            if (isNumeric(txtQty.getText()) > 0 && isNumeric(txtCostPrice.getText()) > 0 && isNumeric(txtSellingPrice.getText()) > 0) {
                btnAddItem.setEnabled(true);
                return;
            }

            return;
        }

        btnAddItem.setEnabled(false);
    }

    private boolean isItemAlreadyInTheList() {

        for (int i = 0; i < dtm.getRowCount(); i++) {

            String itemName = dtm.getValueAt(i, 2).toString();
            if (cmbItem.getSelectedItem().toString().trim().equalsIgnoreCase(itemName)) {
                return true;
            }

        }

        return false;
    }

    private double isNumeric(String txt) {

        txt = txt.replaceAll(",", "");

        return Double.parseDouble(txt);

    }

    private void reset() {

        txtQty.setValue(null);
        txtSellingPrice.setValue(null);
        txtCostPrice.setValue(null);
        txtExpireDate.getEditor().setValue(null);
        btnAddItem.setEnabled(false);
        cmbItem.setSelectedIndex(-1);
        cmbItem.requestFocusInWindow();

    }

    public ItemController.ItemModel getCurrentItem() {

        if (subItems == null) {
            return null;
        }

        for (ItemController.ItemModel subItem : subItems) {

            if (subItem.getItemName().equalsIgnoreCase(cmbItem.getSelectedItem().toString().trim())) {
                return subItem;
            }

        }

        return null;
    }

    private String formatCurrency(Object currency) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(currency);
    }

    private String formatDate(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d);
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
        lblGrnId = new javax.swing.JLabel();
        lblGrnDate = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbSupplier = new javax.swing.JComboBox<>();
        txtInvoiceDate = new org.jdesktop.swingx.JXDatePicker();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtInvoiceId = new javax.swing.JTextField();
        btnEmptyBottles = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        cmbMajorCateogry = new javax.swing.JComboBox<>();
        cmbSubCategory = new javax.swing.JComboBox<>();
        cmbItem = new javax.swing.JComboBox<>();
        txtQty = new javax.swing.JFormattedTextField();
        txtCostPrice = new javax.swing.JFormattedTextField();
        txtSellingPrice = new javax.swing.JFormattedTextField();
        txtExpireDate = new org.jdesktop.swingx.JXDatePicker();
        btnAddItem = new javax.swing.JButton();
        pnlTableContainer = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblItems = new javax.swing.JTable();
        btnSave = new javax.swing.JButton();
        btnAuthorize = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

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
        jLabel3.setText("Good Received Notes Information");
        jLabel3.setIconTextGap(10);

        jLabel4.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("GRN No :");

        lblGrnId.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N

        lblGrnDate.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblGrnDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGrnDate.setText("jLabel17");

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
                .addComponent(lblGrnId, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblGrnDate)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(lblGrnId)
                    .addComponent(lblGrnDate))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Supplier :");

        cmbSupplier.setEditable(true);
        cmbSupplier.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
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

        txtInvoiceDate.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtInvoiceDate.setFormats(new SimpleDateFormat("yyyy-MM-dd"));
        txtInvoiceDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInvoiceDateFocusLost(evt);
            }
        });
        txtInvoiceDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInvoiceDateActionPerformed(evt);
            }
        });
        txtInvoiceDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInvoiceDateKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Invoice ID :");

        jLabel7.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Invoice Date :");

        txtInvoiceId.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtInvoiceId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInvoiceIdKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtInvoiceIdKeyReleased(evt);
            }
        });

        btnEmptyBottles.setBackground(new java.awt.Color(72, 158, 231));
        btnEmptyBottles.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnEmptyBottles.setForeground(new java.awt.Color(255, 255, 255));
        btnEmptyBottles.setMnemonic('E');
        btnEmptyBottles.setText("Empty Bottles");
        btnEmptyBottles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmptyBottlesActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Major Category :");

        jLabel10.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Sub Category :");

        jLabel11.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Item :");

        jLabel12.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Qty. :");

        jLabel13.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Cost Price :");

        jLabel14.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Selling Price :");

        jLabel15.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Expire Date :");

        cmbMajorCateogry.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        cmbMajorCateogry.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMajorCateogryItemStateChanged(evt);
            }
        });
        cmbMajorCateogry.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbMajorCateogryKeyPressed(evt);
            }
        });

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
        cmbItem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbItemItemStateChanged(evt);
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

        txtQty.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQty.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtQty.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtQtyCaretUpdate(evt);
            }
        });
        txtQty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQtyFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQtyFocusLost(evt);
            }
        });
        txtQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQtyActionPerformed(evt);
            }
        });
        txtQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtQtyKeyPressed(evt);
            }
        });

        txtCostPrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.##"))));
        txtCostPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCostPrice.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtCostPrice.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtCostPriceCaretUpdate(evt);
            }
        });
        txtCostPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCostPriceFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCostPriceFocusLost(evt);
            }
        });
        txtCostPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCostPriceKeyPressed(evt);
            }
        });

        txtSellingPrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.##"))));
        txtSellingPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSellingPrice.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtSellingPrice.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtSellingPriceCaretUpdate(evt);
            }
        });
        txtSellingPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSellingPriceFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSellingPriceFocusLost(evt);
            }
        });
        txtSellingPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSellingPriceActionPerformed(evt);
            }
        });
        txtSellingPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSellingPriceKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSellingPriceKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSellingPriceKeyTyped(evt);
            }
        });

        txtExpireDate.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtExpireDate.setFormats(new SimpleDateFormat("yyyy-MM-dd"));

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
                "", "Item Code", "Item", "Qty.", "Cost Price", "Total Cost Price", "Sell Price", "Total Sell Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
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
            tblItems.getColumnModel().getColumn(6).setPreferredWidth(125);
            tblItems.getColumnModel().getColumn(7).setPreferredWidth(125);
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        btnSave.setBackground(new java.awt.Color(72, 158, 231));
        btnSave.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Save");
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnAuthorize.setBackground(new java.awt.Color(92, 184, 92));
        btnAuthorize.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnAuthorize.setForeground(new java.awt.Color(255, 255, 255));
        btnAuthorize.setText("Authorize");
        btnAuthorize.setEnabled(false);
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(251, 93, 93));
        btnDelete.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setMnemonic('e');
        btnDelete.setText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

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
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbSupplier, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbSubCategory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbItem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAuthorize)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbMajorCateogry, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(txtInvoiceId, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(txtInvoiceDate, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(btnEmptyBottles))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(txtExpireDate, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCostPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSellingPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAddItem, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 90, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddItem, btnDelete});

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
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel8))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(txtInvoiceId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel7))
                    .addComponent(txtInvoiceDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEmptyBottles))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbMajorCateogry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel10))
                    .addComponent(cmbSubCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel11))
                    .addComponent(cmbItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel12))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel15))
                    .addComponent(txtExpireDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCostPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAddItem)
                        .addComponent(txtSellingPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14)
                        .addComponent(btnDelete)))
                .addGap(18, 18, 18)
                .addComponent(pnlTableContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAuthorize, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSupplierActionPerformed

    private void btnEmptyBottlesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmptyBottlesActionPerformed
        if (getInvoiceDate() != null) {
            EmptyBottle eb = new EmptyBottle(this, getEmptyBottles(), !pnlProtectedView.isVisible());
            eb.setVisible(true);
        } else {

        }
    }//GEN-LAST:event_btnEmptyBottlesActionPerformed

    private void cmbMajorCateogryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMajorCateogryItemStateChanged
        loadSubCategoriesInToCombo();
    }//GEN-LAST:event_cmbMajorCateogryItemStateChanged

    private void cmbSubCategoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSubCategoryItemStateChanged
        loadItemsInToCombo();
    }//GEN-LAST:event_cmbSubCategoryItemStateChanged

    private void txtQtyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusLost
        if (txtQty.getText().trim().isEmpty()) {
            txtQty.setValue(null);
        } else {
            try {
                txtQty.commitEdit();
            } catch (ParseException ex) {
                txtQty.setValue(null);
                //Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txtQtyFocusLost

    private void txtCostPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCostPriceFocusLost
        if (txtCostPrice.getText().trim().isEmpty()) {
            txtCostPrice.setValue(null);
        } else {
            try {
                txtCostPrice.commitEdit();
            } catch (ParseException ex) {
                txtCostPrice.setValue(null);
                //Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txtCostPriceFocusLost

    private void txtSellingPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSellingPriceFocusLost
        if (txtSellingPrice.getText().trim().isEmpty()) {
            txtSellingPrice.setValue(null);
        } else {
            try {
                txtSellingPrice.commitEdit();
            } catch (ParseException ex) {
                txtSellingPrice.setValue(null);
                //Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txtSellingPriceFocusLost

    private void txtQtyCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtQtyCaretUpdate
        enableAddItemButton();
    }//GEN-LAST:event_txtQtyCaretUpdate

    private void txtCostPriceCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtCostPriceCaretUpdate
        enableAddItemButton();
    }//GEN-LAST:event_txtCostPriceCaretUpdate

    private void txtSellingPriceCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtSellingPriceCaretUpdate
        enableAddItemButton();
    }//GEN-LAST:event_txtSellingPriceCaretUpdate

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed

        ItemController.ItemModel currentItem = getCurrentItem();
        
        if (currentItem == null) {
            return;
        }

        try {
            txtQty.commitEdit();
            txtSellingPrice.commitEdit();
            txtCostPrice.commitEdit();
            txtExpireDate.commitEdit();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        // To avoid NullPointerException, it would better check before proceeding
        if (txtQty.getValue() == null) {
            txtQty.requestFocusInWindow();
            btnAddItem.setEnabled(false);
            return;
        } else if (txtCostPrice.getValue() == null) {
            txtCostPrice.requestFocusInWindow();
            btnAddItem.setEnabled(false);
            return;
        } else if (txtSellingPrice.getValue() == null) {
            txtSellingPrice.requestFocusInWindow();
            btnAddItem.setEnabled(false);
            return;
        }

        BigDecimal costPrice = new BigDecimal(((Number) txtCostPrice.getValue()).doubleValue());
        BigDecimal sellingPrice = new BigDecimal(((Number) txtSellingPrice.getValue()).doubleValue());

        GRNController.GRNItemModel item = new GRNController.GRNItemModel(cmbMajorCateogry.getSelectedItem().toString(),
                cmbSubCategory.getSelectedItem().toString(),
                currentItem.getItemCode(),
                currentItem.getItemName(),
                Integer.parseInt(txtQty.getValue().toString()),
                costPrice,
                sellingPrice,
                txtExpireDate.getEditor().getText());

        JCheckBox jcb = (JCheckBox) tblItems.getTableHeader().getColumnModel().getColumn(0).getHeaderRenderer().getTableCellRendererComponent(tblItems, null, true, false, 0, 0);
        String totalCost = formatCurrency(item.getCostPrice().multiply(BigDecimal.valueOf(item.getQty())));
        String totalSelling = formatCurrency(item.getSellingPrice().multiply(BigDecimal.valueOf(item.getQty())));

        if (!updateStatus) {

            if (!isItemAlreadyInTheList()) {
                items.add(item);
                Object[] rowData = {jcb.isSelected(), currentItem.getItemCode(), currentItem.getItemName(), txtQty.getValue().toString(), formatCurrency(costPrice), totalCost, formatCurrency(sellingPrice), totalSelling};
                dtm.addRow(rowData);
            } else {
                ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
                System.out.println(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png").toString());
                JOptionPane.showMessageDialog(this, "Item is already in the list. Instead of trying to add it again, please edit the item by clicking on the item", "Duplicates are not allowed", JOptionPane.INFORMATION_MESSAGE, icon);
                reset();
                cmbItem.requestFocusInWindow();
            }

        } else {
            items.set(tblItems.getSelectedRow(), item);
            dtm.setValueAt(item.getItemCode(), tblItems.getSelectedRow(), 1);
            dtm.setValueAt(item.getItemName(), tblItems.getSelectedRow(), 2);
            dtm.setValueAt(String.valueOf(item.getQty()), tblItems.getSelectedRow(), 3);
            dtm.setValueAt(formatCurrency(costPrice), tblItems.getSelectedRow(), 4);
            dtm.setValueAt(totalCost, tblItems.getSelectedRow(), 5);
            dtm.setValueAt(formatCurrency(sellingPrice), tblItems.getSelectedRow(), 6);
            dtm.setValueAt(totalSelling, tblItems.getSelectedRow(), 7);
        }
        updateStatus = false;
        btnAddItem.setText("Add Item");
        reset();
    }//GEN-LAST:event_btnAddItemActionPerformed

    private void btnAddItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddItemMouseClicked
        // if (evt.getButton() == MouseEvent.BUTTON_LEFT){
        // btnAddItem.doClick();
        //}
    }//GEN-LAST:event_btnAddItemMouseClicked

    private void cmbItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbItemActionPerformed

    private void cmbSubCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSubCategoryActionPerformed

    }//GEN-LAST:event_cmbSubCategoryActionPerformed

    private void cmbItemItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbItemItemStateChanged
        enableAddItemButton();
    }//GEN-LAST:event_cmbItemItemStateChanged

    private void txtInvoiceIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceIdKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtInvoiceDate.requestFocusInWindow();
        }

    }//GEN-LAST:event_txtInvoiceIdKeyPressed

    private void txtInvoiceDateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceDateKeyPressed


    }//GEN-LAST:event_txtInvoiceDateKeyPressed

    private void cmbMajorCateogryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMajorCateogryKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbSubCategory.requestFocusInWindow();
        }
    }//GEN-LAST:event_cmbMajorCateogryKeyPressed

    private void cmbSubCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbSubCategoryKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbItem.requestFocusInWindow();
        }
    }//GEN-LAST:event_cmbSubCategoryKeyPressed

    private void cmbItemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbItemKeyPressed
    }//GEN-LAST:event_cmbItemKeyPressed

    private void txtQtyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCostPrice.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtQtyKeyPressed

    private void txtCostPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostPriceKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtSellingPrice.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtCostPriceKeyPressed

    private void txtSellingPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSellingPriceKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            btnAddItem.doClick();

        }
    }//GEN-LAST:event_txtSellingPriceKeyPressed

    private void txtInvoiceIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceIdKeyReleased
        if (!txtInvoiceId.getText().trim().isEmpty() && txtInvoiceDate.getDate() != null && cmbSupplier.getSelectedIndex() != -1) {
            btnSave.setEnabled(dtm.getRowCount() != 0);
        } else {
            btnSave.setEnabled(false);
        }
    }//GEN-LAST:event_txtInvoiceIdKeyReleased

    private void txtInvoiceDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInvoiceDateActionPerformed
        if (!txtInvoiceId.getText().trim().isEmpty() && txtInvoiceDate.getDate() != null) {
            btnSave.setEnabled(dtm.getRowCount() != 0);
        } else {
            btnSave.setEnabled(false);
        }
    }//GEN-LAST:event_txtInvoiceDateActionPerformed

    private void txtInvoiceDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInvoiceDateFocusLost
        try {
            txtInvoiceDate.commitEdit();
        } catch (ParseException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtInvoiceDateFocusLost

    private void tblItemsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblItemsKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (dtm.getRowCount() > 0) {
                boolean currentStatus = (boolean) dtm.getValueAt(tblItems.getSelectedRow(), 0);
                dtm.setValueAt(!currentStatus, tblItems.getSelectedRow(), 0);

            }
        }
    }//GEN-LAST:event_tblItemsKeyPressed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed

        for (int i = 0; i < tblItems.getRowCount(); i++) {

            boolean deleteStatus = (boolean) dtm.getValueAt(i, 0);
            if (deleteStatus) {
                dtm.removeRow(i);
                items.remove(i);
                i--;
            }

        }

        updateStatus = false;
        btnAddItem.setText("Add Item");
        reset();

    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemsMouseClicked

        if (tblItems.getSelectedRow() == -1) {
            return;
        }

        int row = tblItems.getSelectedRow();
        GRNController.GRNItemModel item = items.get(row);

        cmbMajorCateogry.setSelectedItem(item.getMajorCategory().toString());
        cmbSubCategory.setSelectedItem(item.getSubCategory().toString());
        cmbItem.setSelectedItem(item.getItemName().toString());

        txtQty.setValue((Integer) item.getQty());

        txtSellingPrice.setValue(item.getSellingPrice());
        txtCostPrice.setValue(item.getCostPrice());

        if (!item.getExpireDate().trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");;
            try {
                txtExpireDate.setDate(sdf.parse(item.getExpireDate()));
            } catch (ParseException ex) {
                txtExpireDate.setDate(null);
                Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            txtExpireDate.setDate(null);
        }

        updateStatus = true;
        btnAddItem.setText("Update");

    }//GEN-LAST:event_tblItemsMouseClicked

    private void txtSellingPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSellingPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSellingPriceActionPerformed

    private void txtSellingPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSellingPriceKeyTyped

    }//GEN-LAST:event_txtSellingPriceKeyTyped

    private void txtSellingPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSellingPriceKeyReleased

    }//GEN-LAST:event_txtSellingPriceKeyReleased

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        //JOptionPane.showMessageDialog(this, getEmptyBottles());
        
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        GRNController controller = (GRNController) ControllerFactory.getInstance().getController(SuperController.ControllerType.GRN);
        int result;
        try {
            result = controller.authorizeGrn(lblGrnId.getText());
            if (result > 0) {
                pnlProtectedView.setVisible(true);
                this.enableEditing(false);
                btnAuthorize.setEnabled(false);
                authorizedStatus = true;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.setCursor(Cursor.getDefaultCursor());

    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        String currentTime;

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentTime = sdf.format(new Date());

        GRNController controller = (GRNController) ControllerFactory.getInstance().getController(SuperController.ControllerType.GRN);

        // Check whether I should add new Grn or just update existing GRN details
        if (this.addNewGrn) {

            GRNController.GRNDetailsModel grnModel = new GRNController.GRNDetailsModel(lblGrnId.getText(),
                    allSuppliers.get(cmbSupplier.getSelectedIndex()).getSupplierId(),
                    formatDate(txtInvoiceDate.getDate()),
                    formatDate(new Date()),
                    "Admin",
                    currentTime,
                    txtInvoiceId.getText()
            );

            try {
                int result = controller.saveGrn(grnModel, this.items, this.emptyBottles);
//                System.out.println(result);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            GRNController.GRNDetailsModel grnModel = new GRNController.GRNDetailsModel(lblGrnId.getText(),
                    allSuppliers.get(cmbSupplier.getSelectedIndex()).getSupplierId(),
                    formatDate(txtInvoiceDate.getDate()),
                    formatDate(new Date()),
                    "Admin",
                    currentTime,
                    txtInvoiceId.getText()
            );

            try {
                int result = controller.changeGrn(grnModel, this.items, this.emptyBottles, authorizedStatus);
//                System.out.println(result);
                authorizedStatus = false;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        addNewGrn = false;
        pnlProtectedView.setVisible(true);
        this.enableEditing(false);

        this.setCursor(Cursor.getDefaultCursor());

    }//GEN-LAST:event_btnSaveActionPerformed

    private void txtQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQtyActionPerformed

    private void txtQtyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusGained
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtQty.selectAll();
            }
        });
    }//GEN-LAST:event_txtQtyFocusGained

    private void txtCostPriceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCostPriceFocusGained
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtCostPrice.selectAll();
            }
        });
    }//GEN-LAST:event_txtCostPriceFocusGained

    private void txtSellingPriceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSellingPriceFocusGained
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtSellingPrice.selectAll();
            }
        });
    }//GEN-LAST:event_txtSellingPriceFocusGained

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        pnlProtectedView.setVisible(false);
        this.enableEditing(true);
        btnSave.setEnabled(true);
        btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void cmbSupplierFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSupplierFocusGained
        cmbSupplier.getEditor().selectAll();
    }//GEN-LAST:event_cmbSupplierFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnAuthorize;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnEmptyBottles;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbItem;
    private javax.swing.JComboBox<String> cmbMajorCateogry;
    private javax.swing.JComboBox<String> cmbSubCategory;
    private javax.swing.JComboBox<String> cmbSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblGrnDate;
    private javax.swing.JLabel lblGrnId;
    private javax.swing.JPanel pnlProtectedView;
    private javax.swing.JPanel pnlTableContainer;
    private javax.swing.JTable tblItems;
    private javax.swing.JFormattedTextField txtCostPrice;
    private org.jdesktop.swingx.JXDatePicker txtExpireDate;
    private org.jdesktop.swingx.JXDatePicker txtInvoiceDate;
    private javax.swing.JTextField txtInvoiceId;
    private javax.swing.JFormattedTextField txtQty;
    private javax.swing.JFormattedTextField txtSellingPrice;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the emptyBottles
     */
    public ArrayList<GRNController.GRNEmptyBottleDetailModel> getEmptyBottles() {
        return emptyBottles;
    }

    /**
     * @param emptyBottles the emptyBottles to set
     */
    public void setEmptyBottles(ArrayList<GRNController.GRNEmptyBottleDetailModel> emptyBottles) {
        this.emptyBottles = emptyBottles;
    }

    public Date getInvoiceDate() {
        return txtInvoiceDate.getDate();
    }

}
