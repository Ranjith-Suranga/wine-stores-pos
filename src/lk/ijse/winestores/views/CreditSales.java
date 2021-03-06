/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import com.jidesoft.swing.AutoCompletion;
import com.sun.glass.events.KeyEvent;
import java.awt.Cursor;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import lk.ijse.winestores.controller.ControllerFactory;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.controller.custom.ItemController;
import lk.ijse.winestores.controller.custom.QueryController;
import lk.ijse.winestores.controller.custom.SalesController;
import lk.ijse.winestores.dao.custom.CustomDAO;
import lk.ijse.winestores.dao.dto.CreditOrderDTO;
import lk.ijse.winestores.dao.dto.CreditOrderItemDetailsDTO;
import lk.ijse.winestores.dao.dto.CustomItemDetailsDTO;
import lk.ijse.winestores.dao.dto.CustomerDTO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.views.util.CashTendered;
import lk.ijse.winestores.views.util.Customer;
import lk.ijse.winestores.views.util.FocusHandler;
import lk.ijse.winestores.views.util.SuraBoyTextComponenets;

/**
 *
 * @author Ranjith Suranga
 */
public class CreditSales extends javax.swing.JPanel implements FocusHandler, Customer {
    
    private ArrayList<EmptyBottleDTO> emptyBottleTypes;
    
    private DefaultTableModel dtmSearchItems;               // Holds the table model of the tblSearchItems
    private DefaultTableModel dtmItems;                     // Holds the table model of the tblItems

    private SuraButton sbtn;                                // Holds the sura button instance
    private SuraTable stblForSearchItems;                   // Holds the sura table instance of "tblSearchItems"
    private SuraTable stblItems;                            // Holds the sura table instance of "tblItems"
    private SuraBoyTextComponenets sbtcmp;
    
    private KeyEventPostProcessor pstProcessor;
    
    private ArrayList<CustomerDTO> alCustomers;
    
    private QueryController ctrlQuery;
    
    private DefaultComboBoxModel dcbm;
    
//    private CashTenderForm cashTenderForm;

    //private BigDecimal emptyBottleTotal = BigDecimal.ZERO;
//    private BigDecimal billTotal = BigDecimal.ZERO;
    private BigDecimal finalTotal = BigDecimal.ZERO;
    
    private CreditOrderDTO creditOrder;
    private CustomerDTO customerDTO;
    private ArrayList<CreditOrderItemDetailsDTO> creditOrderItemDetails;
    //private ArrayList<CreditOrderEmptyBottleDetailsDTO> creditOrderEmptyBottleDetails;

    /**
     * Creates new form CashSales
     */
    public CreditSales() {
        initComponents();
        
        sbtn = new SuraButton(this);
        sbtn.convertAllJButtonsToSuraButtons();
        sbtcmp = new SuraBoyTextComponenets(this);

        // Init Controllers
        ctrlQuery = (QueryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.QUERY);
        
        stblForSearchItems = new SuraTable(tblSearchItems);
        // Item Code
        stblForSearchItems.setHeaderAlignment(0, SwingConstants.CENTER);
        stblForSearchItems.setColumnAlignment(0, SwingConstants.CENTER);
        // Qty
        stblForSearchItems.setHeaderAlignment(2, SwingConstants.RIGHT);
        stblForSearchItems.setColumnAlignment(2, SwingConstants.RIGHT);
        // Selling Price
        stblForSearchItems.setHeaderAlignment(3, SwingConstants.RIGHT);
        stblForSearchItems.setColumnAlignment(3, SwingConstants.RIGHT);
        
        stblItems = new SuraTable(tblItems);
        // Item Code
        stblItems.setHeaderAlignment(1, SwingConstants.CENTER);
        stblItems.setColumnAlignment(0, SwingConstants.CENTER);
        // Qty
        stblItems.setHeaderAlignment(3, SwingConstants.RIGHT);
        stblItems.setColumnAlignment(2, SwingConstants.RIGHT);
        // Selling Price
        stblItems.setHeaderAlignment(4, SwingConstants.RIGHT);
        stblItems.setColumnAlignment(3, SwingConstants.RIGHT);
        // Total
        stblItems.setHeaderAlignment(5, SwingConstants.RIGHT);
        stblItems.setColumnAlignment(4, SwingConstants.RIGHT);
        
        dtmSearchItems = (DefaultTableModel) tblSearchItems.getModel();
        dtmItems = (DefaultTableModel) tblItems.getModel();
        
        AutoCompletion ac = new AutoCompletion(cmbCustomerName);
        ac.setStrict(false);
        loadCustomers();

        // A little hack
        JTextField txt = (JTextField) cmbCustomerName.getEditor().getEditorComponent();
        
        txt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyChar() == KeyEvent.VK_ENTER){
                    btnPay.doClick();
                }
            }
        });
        
        txt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                listenToChanges();
                
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                listenToChanges();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                listenToChanges();
            }
            
            private void listenToChanges() {
                enablePay();
                if (cmbCustomerName.getSelectedIndex() != -1) {
                    if (!txt.getText().equals(cmbCustomerName.getSelectedItem().toString())) {
                        resetCustomer(false);
                    } else {
                        cmbCustomerNameItemStateChanged(null);
                    }
                }
            }
        });

//        cmbEmptyBottle.removeAllItems();
//        GRNController ctrl = (GRNController) ControllerFactory.getInstance().getController(SuperController.ControllerType.GRN);
//        try {
//            emptyBottleTypes = ctrl.getAllEmptyBottleTypes();
//            if (emptyBottleTypes != null) {
//                creditOrderEmptyBottleDetails = new ArrayList<>();
//                for (EmptyBottleDTO emptyBottleType : emptyBottleTypes) {
//                    cmbEmptyBottle.addItem(emptyBottleType.getBottleType());
//                    CreditOrderEmptyBottleDetailsDTO dto = new CreditOrderEmptyBottleDetailsDTO(null,
//                            null,
//                            emptyBottleType.getBottleType(),
//                            0,
//                            0);
//                    creditOrderEmptyBottleDetails.add(dto);
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        txtEmptyBottleQty.setEnabled((emptyBottleTypes != null) && (emptyBottleTypes.size() != 0));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtBarcode.requestFocusInWindow();
            }
        });
        
        tblSearchItems.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                
                if (tblSearchItems.getSelectedRow() == -1) {
                    enableQty();
                    return;
                }
                
                txtItemCode.setText((String) dtmSearchItems.getValueAt(tblSearchItems.getSelectedRow(), 0));
                txtItemName.setText((String) dtmSearchItems.getValueAt(tblSearchItems.getSelectedRow(), 1));
                txtSellingPrice.setText((String) dtmSearchItems.getValueAt(tblSearchItems.getSelectedRow(), 3));
                
                try {
                    ItemController ctrl = (ItemController) ControllerFactory.getInstance().getController(SuperController.ControllerType.ITEM);
                    String barCode = ctrl.getBarCodeByItemCode(txtItemCode.getText().trim());
                    if (barCode != null) {
                        txtBarcode.setText(barCode);
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        dtmItems.addTableModelListener(new TableModelListener() {
            
            @Override
            public void tableChanged(TableModelEvent e) {
                
                calculateTotals();
                enablePay();

                // Delete Section
                btnDelete.setEnabled(false);
                
                for (int i = 0; i < dtmItems.getRowCount(); i++) {
                    
                    if ((boolean) dtmItems.getValueAt(i, 0) == true) {
                        btnDelete.setEnabled(true);
                        break;
                    }
                    
                }
            }
        });
        
    }
    
    private void loadCustomers(){
        cmbCustomerName.removeAllItems();
        
        dcbm = (DefaultComboBoxModel) cmbCustomerName.getModel();
        
        try {
            alCustomers = ctrlQuery.getAllCustomers();
            
            if (alCustomers != null) {
                for (CustomerDTO customer : alCustomers) {
                    dcbm.addElement(customer);
                }
            }
            
            cmbCustomerName.setSelectedIndex(-1);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    private void searchItems(CustomDAO.ItemQueryType queryType, String queryWord) {
        
        ItemController ctrl = (ItemController) ControllerFactory.getInstance().getController(SuperController.ControllerType.ITEM);
        dtmSearchItems.setRowCount(0);
        
        if (queryWord.trim().isEmpty()) {
            return;
        }
        
        try {
            ArrayList<CustomItemDetailsDTO> items = ctrl.getItems(queryType, queryWord);
            
            if (items == null) {
                return;
            }
            
            for (CustomItemDetailsDTO item : items) {
                Object[] rowData = {item.getItemCode(), item.getItemName(), String.valueOf(item.getQty()), this.formatPrice(item.getSellingPrice())};
                dtmSearchItems.addRow(rowData);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void resetTextFields(JTextField avoidTxt) {
        if (avoidTxt != txtBarcode) {
            txtBarcode.setText("");
        }
        if (avoidTxt != txtItemCode) {
            txtItemCode.setText("");
        }
        if (avoidTxt != txtItemName) {
            txtItemName.setText("");
        }
        if (avoidTxt != txtSellingPrice) {
            txtSellingPrice.setText("");
        }
        txtQty.setText("");
    }
    
    private String formatPrice(Object price) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(false);
        return nf.format(price);
    }
    
    private String formatPrice(Object price, boolean enableGrouping) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(enableGrouping);
        return nf.format(price);
    }
    
    private void enableQty() {
        int qty;
        
        txtQty.setText("");
        txtQty.setEnabled(false);
        btnSave.setText("Add Item");
        
        if (tblSearchItems.getSelectedRow() == -1) {
            return;
        }

        // Get qty for the selected item
        qty = Integer.parseInt((String) dtmSearchItems.getValueAt(tblSearchItems.getSelectedRow(), 2));
        if (qty > 0) {
            txtQty.setEnabled(true);
            txtQty.requestFocusInWindow();
        }
        
    }
    
    private void enablePay() {
        
        btnPay.setEnabled(false);
        if (dtmItems.getRowCount() > 0) {
            if (cmbCustomerName.getSelectedIndex() != -1) {
                if (cmbCustomerName.getSelectedItem().toString().equals(cmbCustomerName.getEditor().getItem().toString())) {
                    btnPay.setEnabled(true);
                }
            }
        }
        
    }
    
    private int getItemRowIndex(String itemCode) {
        for (int i = 0; i < dtmItems.getRowCount(); i++) {
            String code = (String) dtmItems.getValueAt(i, 1);
            if (code.equals(itemCode)) {
                return i;
            }
        }
        return -1;
    }
    
    private void initUpdate() {
        int selectedRow = tblItems.getSelectedRow();
        
        txtItemCode.setText((String) dtmItems.getValueAt(selectedRow, 1));
        resetTextFields(txtItemCode);
        searchItems(CustomDAO.ItemQueryType.ITEM_CODE, txtItemCode.getText());
        
        tblSearchItems.getSelectionModel().setSelectionInterval(0, 0);
        enableQty();
        txtQty.setText((String) dtmItems.getValueAt(selectedRow, 3));
        
        btnSave.setText("Update");
        btnSave.setEnabled(true);
    }

//    private void calculateEmptyBottleCost() {
//        BigDecimal total = BigDecimal.valueOf(0);
//        lblEmptyBottleCost.setText("-");
//
//        for (CreditOrderEmptyBottleDetailsDTO dto : creditOrderEmptyBottleDetails) {
//            total = total.add(BigDecimal.valueOf(dto.getTotal()));
//        }
//
//        if (total.compareTo(BigDecimal.ZERO) == 0) {
//            lblEmptyBottleCost.setText("-");
//        } else {
//            String formattedTotal = this.formatPrice(total, true);
//            lblEmptyBottleCost.setText(formattedTotal);
//        }
//        emptyBottleTotal = total;
//
//        calculateTotals();
//    }
    private void calculateTotals() {
        BigDecimal total = BigDecimal.ZERO;
        
        for (int i = 0; i < dtmItems.getRowCount(); i++) {
            String t = (String) dtmItems.getValueAt(i, 5);
            total = total.add(new BigDecimal(t));
        }
        
        finalTotal = total;
        //finalTotal = billTotal.add(emptyBottleTotal);

//        if (billTotal.compareTo(BigDecimal.ZERO) == 0) {
//            //lblBillTotal.setText("-");
//        } else {
//            String formattedTotal = this.formatPrice(billTotal, true);
//           // lblBillTotal.setText(formattedTotal);
//        }
        if (finalTotal.compareTo(BigDecimal.ZERO) == 0) {
            lblFinalTotal.setText("-");
        } else {
            String formattedTotal = this.formatPrice(finalTotal, true);
            lblFinalTotal.setText(formattedTotal);
        }
        
    }
    
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(date);
    }
    
    private void resetCustomer(boolean all) {
        txtCustomerId.setText(null);
        txtContactNumbers.setText(null);
        txtAddress.setText(null);
        if (all) {
            cmbCustomerName.setSelectedIndex(-1);
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

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBarcode = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtItemCode = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtItemName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtSellingPrice = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSearchItems = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblItems = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtQty = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        btnSave = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        btnPay = new javax.swing.JButton();
        pnlCheque = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        lbl = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtContactNumbers = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCustomerId = new javax.swing.JTextField();
        cmbCustomerName = new javax.swing.JComboBox<>();
        btnAddNewCustomer = new javax.swing.JButton();
        lblFinalTotal = new javax.swing.JLabel();

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        setBackground(new java.awt.Color(255, 255, 255));
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                formAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                formAncestorRemoved(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                formComponentHidden(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setDisplayedMnemonic('B');
        jLabel1.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel1.setLabelFor(txtBarcode);
        jLabel1.setText("Barcode");

        txtBarcode.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtBarcode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBarcodeFocusGained(evt);
            }
        });
        txtBarcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBarcodeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBarcodeKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel2.setText("Item Code");

        txtItemCode.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtItemCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtItemCodeFocusGained(evt);
            }
        });
        txtItemCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtItemCodeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtItemCodeKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel3.setText("Item Name");

        txtItemName.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtItemName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtItemNameFocusGained(evt);
            }
        });
        txtItemName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtItemNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtItemNameKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel4.setText("Selling Price");

        txtSellingPrice.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtSellingPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSellingPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSellingPriceFocusGained(evt);
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

        jLabel5.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel5.setText("Enter Barcode/ Item Code/ Item Name or Selling Price to Search the Item");

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234)));

        jScrollPane1.setBorder(null);

        tblSearchItems.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        tblSearchItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Code", "Item Name", "Qty.", "Selling Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSearchItems.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblSearchItems.setGridColor(new java.awt.Color(204, 204, 204));
        tblSearchItems.setRowHeight(30);
        tblSearchItems.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblSearchItems.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblSearchItems.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblSearchItems.getTableHeader().setReorderingAllowed(false);
        tblSearchItems.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tblSearchItemsFocusGained(evt);
            }
        });
        tblSearchItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSearchItemsMouseClicked(evt);
            }
        });
        tblSearchItems.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblSearchItemsKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblSearchItemsKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tblSearchItemsKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(tblSearchItems);
        if (tblSearchItems.getColumnModel().getColumnCount() > 0) {
            tblSearchItems.getColumnModel().getColumn(0).setMinWidth(0);
            tblSearchItems.getColumnModel().getColumn(0).setPreferredWidth(100);
            tblSearchItems.getColumnModel().getColumn(0).setMaxWidth(100);
            tblSearchItems.getColumnModel().getColumn(2).setMinWidth(50);
            tblSearchItems.getColumnModel().getColumn(2).setPreferredWidth(50);
            tblSearchItems.getColumnModel().getColumn(2).setMaxWidth(50);
            tblSearchItems.getColumnModel().getColumn(3).setMinWidth(150);
            tblSearchItems.getColumnModel().getColumn(3).setPreferredWidth(150);
            tblSearchItems.getColumnModel().getColumn(3).setMaxWidth(150);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1)
                .addGap(0, 0, 0))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txtBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(115, 115, 115)
                                .addComponent(jLabel3))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtItemName, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtSellingPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSellingPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel6.setText("List of Items");

        btnDelete.setBackground(new java.awt.Color(251, 93, 93));
        btnDelete.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234)));

        jScrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234), 2));

        tblItems.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        tblItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Item Code", "Item Name", "Qty.", "Selling Price", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
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
        tblItems.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        tblItems.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblItems.setGridColor(new java.awt.Color(204, 204, 204));
        tblItems.setRowHeight(30);
        tblItems.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblItems.setSelectionForeground(new java.awt.Color(0, 0, 0));
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
        jScrollPane3.setViewportView(tblItems);
        if (tblItems.getColumnModel().getColumnCount() > 0) {
            tblItems.getColumnModel().getColumn(0).setMinWidth(50);
            tblItems.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblItems.getColumnModel().getColumn(0).setMaxWidth(50);
            tblItems.getColumnModel().getColumn(1).setMinWidth(100);
            tblItems.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblItems.getColumnModel().getColumn(1).setMaxWidth(100);
            tblItems.getColumnModel().getColumn(3).setMinWidth(50);
            tblItems.getColumnModel().getColumn(3).setPreferredWidth(50);
            tblItems.getColumnModel().getColumn(3).setMaxWidth(50);
            tblItems.getColumnModel().getColumn(4).setMinWidth(150);
            tblItems.getColumnModel().getColumn(4).setPreferredWidth(150);
            tblItems.getColumnModel().getColumn(4).setMaxWidth(150);
            tblItems.getColumnModel().getColumn(5).setMinWidth(150);
            tblItems.getColumnModel().getColumn(5).setPreferredWidth(150);
            tblItems.getColumnModel().getColumn(5).setMaxWidth(150);
        }

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3)
                .addGap(0, 0, 0))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDelete))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(btnDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(224, 219, 221)));

        jLabel7.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel7.setText("Enter Qty.");

        txtQty.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQty.setEnabled(false);
        txtQty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQtyFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQtyFocusLost(evt);
            }
        });
        txtQty.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtQtyPropertyChange(evt);
            }
        });
        txtQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQtyKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtQtyKeyTyped(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(224, 219, 221));

        btnSave.setBackground(new java.awt.Color(72, 158, 231));
        btnSave.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Add Item");
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Open Sans", 1, 22)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Total :");

        btnPay.setBackground(new java.awt.Color(76, 175, 80));
        btnPay.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnPay.setForeground(new java.awt.Color(255, 255, 255));
        btnPay.setText("Issue [End]");
        btnPay.setEnabled(false);
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });

        pnlCheque.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel13.setText("Customer Details");

        lbl.setDisplayedMnemonic('C');
        lbl.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lbl.setLabelFor(cmbCustomerName);
        lbl.setText("Customer Name");

        jLabel14.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel14.setText("Contact Numbers");

        txtContactNumbers.setEditable(false);
        txtContactNumbers.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtContactNumbers.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtContactNumbersFocusGained(evt);
            }
        });
        txtContactNumbers.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtContactNumbersKeyReleased(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel15.setText("Address");

        txtAddress.setEditable(false);
        txtAddress.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtAddress.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAddressFocusGained(evt);
            }
        });
        txtAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAddressKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel8.setText("Customer ID");

        txtCustomerId.setEditable(false);
        txtCustomerId.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtCustomerId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCustomerIdFocusGained(evt);
            }
        });

        cmbCustomerName.setEditable(true);
        cmbCustomerName.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        cmbCustomerName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCustomerNameItemStateChanged(evt);
            }
        });
        cmbCustomerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCustomerNameActionPerformed(evt);
            }
        });

        btnAddNewCustomer.setBackground(new java.awt.Color(72, 158, 231));
        btnAddNewCustomer.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnAddNewCustomer.setMnemonic('A');
        btnAddNewCustomer.setText("+ Add New Customer");
        btnAddNewCustomer.setToolTipText("Click to add new Customer");
        btnAddNewCustomer.setPreferredSize(new java.awt.Dimension(89, 42));
        btnAddNewCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNewCustomerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlChequeLayout = new javax.swing.GroupLayout(pnlCheque);
        pnlCheque.setLayout(pnlChequeLayout);
        pnlChequeLayout.setHorizontalGroup(
            pnlChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChequeLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(pnlChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAddress)
                    .addComponent(txtContactNumbers)
                    .addComponent(txtCustomerId)
                    .addComponent(cmbCustomerName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlChequeLayout.createSequentialGroup()
                        .addGroup(pnlChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel15)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13)
                            .addComponent(lbl))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnAddNewCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        pnlChequeLayout.setVerticalGroup(
            pnlChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChequeLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addComponent(lbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCustomerId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtContactNumbers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAddNewCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblFinalTotal.setFont(new java.awt.Font("Open Sans", 1, 22)); // NOI18N
        lblFinalTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFinalTotal.setText("-");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                    .addComponent(txtQty)
                    .addComponent(jSeparator1)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlCheque, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFinalTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCheque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFinalTotal)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPay, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(8, 8, 8)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtBarcodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarcodeKeyReleased
        resetTextFields(txtBarcode);
        searchItems(CustomDAO.ItemQueryType.BARCODE, txtBarcode.getText());
    }//GEN-LAST:event_txtBarcodeKeyReleased

    private void txtItemCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemCodeKeyReleased
        resetTextFields(txtItemCode);
        searchItems(CustomDAO.ItemQueryType.ITEM_CODE, txtItemCode.getText());
    }//GEN-LAST:event_txtItemCodeKeyReleased

    private void txtItemNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemNameKeyReleased
        resetTextFields(txtItemName);
        searchItems(CustomDAO.ItemQueryType.ITEM_NAME, txtItemName.getText());
    }//GEN-LAST:event_txtItemNameKeyReleased

    private void txtSellingPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSellingPriceKeyReleased
        resetTextFields(txtSellingPrice);
        searchItems(CustomDAO.ItemQueryType.SELLING_PRICE, txtSellingPrice.getText());
    }//GEN-LAST:event_txtSellingPriceKeyReleased

    private void txtSellingPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSellingPriceKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && dtmSearchItems.getRowCount() > 0) {
            tblSearchItems.requestFocusInWindow();
            tblSearchItems.getSelectionModel().setSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_txtSellingPriceKeyPressed

    private void txtSellingPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSellingPriceKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            if (!(evt.getKeyChar() == KeyEvent.VK_DELETE | evt.getKeyChar() == KeyEvent.VK_BACKSPACE | evt.getKeyChar() == KeyEvent.VK_PERIOD)) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtSellingPriceKeyTyped

    private void txtBarcodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBarcodeFocusGained
        ((JTextField) evt.getSource()).selectAll();
    }//GEN-LAST:event_txtBarcodeFocusGained

    private void txtItemCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtItemCodeFocusGained
        ((JTextField) evt.getSource()).selectAll();
    }//GEN-LAST:event_txtItemCodeFocusGained

    private void txtItemNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtItemNameFocusGained
        ((JTextField) evt.getSource()).selectAll();
    }//GEN-LAST:event_txtItemNameFocusGained

    private void txtSellingPriceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSellingPriceFocusGained
        ((JTextField) evt.getSource()).selectAll();
    }//GEN-LAST:event_txtSellingPriceFocusGained

    private void txtBarcodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarcodeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && dtmSearchItems.getRowCount() > 0) {
            tblSearchItems.requestFocusInWindow();
            tblSearchItems.getSelectionModel().setSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_txtBarcodeKeyPressed

    private void txtItemCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemCodeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && dtmSearchItems.getRowCount() > 0) {
            tblSearchItems.requestFocusInWindow();
            tblSearchItems.getSelectionModel().setSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_txtItemCodeKeyPressed

    private void txtItemNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemNameKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && dtmSearchItems.getRowCount() > 0) {
            tblSearchItems.requestFocusInWindow();
            tblSearchItems.getSelectionModel().setSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_txtItemNameKeyPressed

    private void tblSearchItemsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblSearchItemsFocusGained

    }//GEN-LAST:event_tblSearchItemsFocusGained

    private void tblSearchItemsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblSearchItemsKeyTyped

    }//GEN-LAST:event_tblSearchItemsKeyTyped

    private void tblSearchItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSearchItemsMouseClicked
        enableQty();
    }//GEN-LAST:event_tblSearchItemsMouseClicked

    private void tblSearchItemsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblSearchItemsKeyReleased

    }//GEN-LAST:event_tblSearchItemsKeyReleased

    private void tblSearchItemsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblSearchItemsKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            enableQty();
            evt.consume();
        }
    }//GEN-LAST:event_tblSearchItemsKeyPressed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        for (int i = 0; i < dtmItems.getRowCount(); i++) {
            
            boolean delete = (boolean) dtmItems.getValueAt(i, 0);
            if (delete) {
                dtmItems.removeRow(i);
                i--;
            }
        }
        
        resetTextFields(null);
        searchItems(CustomDAO.ItemQueryType.ITEM_CODE, txtItemCode.getText());
        enableQty();
        txtBarcode.requestFocusInWindow();
        tblItems.getSelectionModel().clearSelection();

    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblItemsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblItemsKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE) {
            
            if (dtmItems.getRowCount() > 0) {
                boolean currentStatus = (boolean) dtmItems.getValueAt(tblItems.getSelectedRow(), 0);
                dtmItems.setValueAt(!currentStatus, tblItems.getSelectedRow(), 0);
                
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            initUpdate();
        }
    }//GEN-LAST:event_tblItemsKeyPressed

    private void tblItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemsMouseClicked
        initUpdate();
    }//GEN-LAST:event_tblItemsMouseClicked

    private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
    }//GEN-LAST:event_formComponentHidden

    private void formAncestorRemoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_formAncestorRemoved
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventPostProcessor(pstProcessor);
    }//GEN-LAST:event_formAncestorRemoved

    private void formAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_formAncestorAdded
        this.pstProcessor = new KeyEventPostProcessor() {
            @Override
            public boolean postProcessKeyEvent(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_END) {
                    btnPay.doClick();
                }
                return false;
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(pstProcessor);
    }//GEN-LAST:event_formAncestorAdded

    private void txtAddressKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyReleased

    }//GEN-LAST:event_txtAddressKeyReleased

    private void txtAddressFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAddressFocusGained
        ((JTextField) evt.getSource()).selectAll();
    }//GEN-LAST:event_txtAddressFocusGained

    private void txtContactNumbersKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContactNumbersKeyReleased

    }//GEN-LAST:event_txtContactNumbersKeyReleased

    private void txtContactNumbersFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContactNumbersFocusGained
        ((JTextField) evt.getSource()).selectAll();
    }//GEN-LAST:event_txtContactNumbersFocusGained

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed
        
//        if (cashTenderForm == null) {
//            cashTenderForm = new CashTenderForm(this, finalTotal);
//        }
//        if (!cashTenderForm.isVisible()) {
//            cashTenderForm.setTotal(finalTotal);
//            cashTenderForm.setVisible(true);
//        }

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        // Filling the customer details
        customerDTO = alCustomers.get(cmbCustomerName.getSelectedIndex());

        // Filling the cutomer order
        creditOrder = new CreditOrderDTO(null,
                formatDate(new Date()),
                "Admin",
                finalTotal.doubleValue(),
                String.valueOf(customerDTO.getCustomerId())
                );

        // Filling the order item details
        creditOrderItemDetails = new ArrayList<>();
        for (int i = 0; i < dtmItems.getRowCount(); i++) {
            CreditOrderItemDetailsDTO dto = new CreditOrderItemDetailsDTO(
                    null,
                    null,
                    dtmItems.getValueAt(i, 1).toString(),
                    Integer.parseInt(dtmItems.getValueAt(i, 3).toString()),
                    Double.valueOf(dtmItems.getValueAt(i, 4).toString()));
            creditOrderItemDetails.add(dto);
        }

        // Sending data to the controller
        SalesController controller = (SalesController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SALES);
        try {
            boolean success = controller.saveCreditSale(creditOrder, creditOrderItemDetails);
            if (success) {
                // Resetting
                resetTextFields(null);
                dtmSearchItems.setRowCount(0);
                dtmItems.setRowCount(0);
                cmbCustomerName.setSelectedIndex(-1);
//                if (cmbEmptyBottle.getItemCount() > 0) {
//                    cmbEmptyBottle.setSelectedIndex(0);
//                }
//                txtEmptyBottleQty.setText("0");
//                txtCustomerName.setText("");
//                txtContactNumbers.setText("");
//                txtAddress.setText("");
                txtBarcode.requestFocusInWindow();
                enableQty();
                enablePay();
                ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/ok.png"));
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor((this)),
                        "Order has been successfully saved.",
                        "Order Success",
                        JOptionPane.INFORMATION_MESSAGE,
                        icon);
            } else {
                ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                        "Sorry, this order can not be saved right now due to some unexpected reason. Please try again.",
                        "Order Failed",
                        JOptionPane.INFORMATION_MESSAGE,
                        icon);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.setCursor(Cursor.getDefaultCursor());
        }


    }//GEN-LAST:event_btnPayActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        
        if (btnSave.getText().equals("Add Item")) {

            // Check whether the item is already in the table
            if (this.getItemRowIndex(txtItemCode.getText().trim()) != -1) {
                ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
                JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(this),
                        "You have already added this item to the list, click on the item to change the qty.",
                        "Item has been already added",
                        JOptionPane.INFORMATION_MESSAGE,
                        icon);
            } else {
                JCheckBox chk = (JCheckBox) tblItems.getColumnModel().getColumn(0).getHeaderRenderer().getTableCellRendererComponent(tblItems, null, true, true, 0, 0);
                
                BigDecimal bd = new BigDecimal(txtSellingPrice.getText().trim());
                bd = bd.multiply(new BigDecimal(txtQty.getText().trim()));
                
                Object[] rowData = {false,
                    txtItemCode.getText().trim(),
                    txtItemName.getText().trim(),
                    txtQty.getText().trim(),
                    txtSellingPrice.getText().trim(),
                    this.formatPrice(bd)
                };
                dtmItems.addRow(rowData);
            }
            
        } else {
            
            int selectedRow = tblItems.getSelectedRow();
            dtmItems.setValueAt(txtQty.getText().trim(), selectedRow, 3);
            
            BigDecimal bd = new BigDecimal(txtSellingPrice.getText().trim());
            bd = bd.multiply(new BigDecimal(txtQty.getText().trim()));
            dtmItems.setValueAt(this.formatPrice(bd), selectedRow, 5);
            
            btnSave.setText("Add Item");
            
        }
        
        resetTextFields(null);
        txtBarcode.requestFocusInWindow();
        txtBarcodeKeyReleased(null);
        tblItems.getSelectionModel().clearSelection();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void txtQtyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            if (!(evt.getKeyChar() == KeyEvent.VK_DELETE | evt.getKeyChar() == KeyEvent.VK_BACKSPACE)) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtQtyKeyTyped

    private void txtQtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyReleased
        int qty = 0, stock;
        
        btnSave.setEnabled(false);

        // If user does some silly copy paste of some string
        try {
            qty = Integer.parseInt(txtQty.getText());
        } catch (NumberFormatException exception) {
            qty = 0;
            txtQty.selectAll();
        }
        
        stock = Integer.parseInt((String) dtmSearchItems.getValueAt(tblSearchItems.getSelectedRow(), 2));
        
        if (qty <= stock && qty > 0) {
            btnSave.setEnabled(true);
        }
        
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSave.doClick();
        }
    }//GEN-LAST:event_txtQtyKeyReleased

    private void txtQtyPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtQtyPropertyChange
        if (!txtQty.isEnabled()) {
            btnSave.setEnabled(false);
        }
    }//GEN-LAST:event_txtQtyPropertyChange

    private void txtQtyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusLost

    }//GEN-LAST:event_txtQtyFocusLost

    private void txtQtyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusGained
        ((JTextField) evt.getSource()).selectAll();
    }//GEN-LAST:event_txtQtyFocusGained

    private void cmbCustomerNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCustomerNameItemStateChanged

        //System.out.println(cmbCustomerName.getSelectedIndex());
        resetCustomer(false);
        
        if (cmbCustomerName.getSelectedIndex() != -1) {
            CustomerDTO customer = alCustomers.get(cmbCustomerName.getSelectedIndex());
            txtCustomerId.setText(customer.getCustomerId() + "");
            txtContactNumbers.setText(customer.getTelephoneNumber());
            txtAddress.setText(customer.getAddress());
        }
    }//GEN-LAST:event_cmbCustomerNameItemStateChanged

    private void btnAddNewCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNewCustomerActionPerformed
        new SaveCustomer(this).setVisible(true);
    }//GEN-LAST:event_btnAddNewCustomerActionPerformed

    private void txtCustomerIdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerIdFocusGained
        txtCustomerId.selectAll();
    }//GEN-LAST:event_txtCustomerIdFocusGained

    private void cmbCustomerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCustomerNameActionPerformed
//        btnPay.doClick();
    }//GEN-LAST:event_cmbCustomerNameActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddNewCustomer;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnPay;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbCustomerName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lbl;
    private javax.swing.JLabel lblFinalTotal;
    private javax.swing.JPanel pnlCheque;
    private javax.swing.JTable tblItems;
    private javax.swing.JTable tblSearchItems;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtBarcode;
    private javax.swing.JTextField txtContactNumbers;
    private javax.swing.JTextField txtCustomerId;
    private javax.swing.JTextField txtItemCode;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtSellingPrice;
    // End of variables declaration//GEN-END:variables

    @Override
    public void initFoucs() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadCustomers();
                txtBarcode.requestFocusInWindow();
            }
        });
    }
    
    @Override
    public void addCustomer(CustomerDTO customer) {
        if (alCustomers == null) {
            alCustomers = new ArrayList<>();
        }
        alCustomers.add(customer);
        cmbCustomerName.addItem(customer.getCustomerName());
        cmbCustomerName.setSelectedIndex(cmbCustomerName.getItemCount() - 1);
    }
  
}
