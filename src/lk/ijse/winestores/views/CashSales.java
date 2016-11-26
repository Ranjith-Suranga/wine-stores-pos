/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import com.sun.glass.events.KeyEvent;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.FocusManager;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
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
import lk.ijse.winestores.controller.custom.QueryController;
import lk.ijse.winestores.controller.custom.SalesController;
import lk.ijse.winestores.dao.custom.CustomDAO;
import lk.ijse.winestores.dao.dto.ChequeDetailsDTO;
import lk.ijse.winestores.dao.dto.CustomItemDetailsDTO;
import lk.ijse.winestores.dao.dto.CustomOrderDTO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.dao.dto.OrderEmptyBottleDetailsDTO;
import lk.ijse.winestores.dao.dto.OrderItemDetailsDTO;
import lk.ijse.winestores.views.util.CashTendered;
import lk.ijse.winestores.views.util.FocusHandler;
import lk.ijse.winestores.views.util.SuraBoyTextComponenets;

/**
 *
 * @author Ranjith Suranga
 */
public class CashSales extends javax.swing.JPanel implements CashTendered, FocusHandler {

    private ArrayList<EmptyBottleDTO> emptyBottleTypes;
    private CashTenderForm cashTenderForm;

    private DefaultTableModel dtmSearchItems;               // Holds the table model of the tblSearchItems
    private DefaultTableModel dtmItems;                     // Holds the table model of the tblItems

    private SuraButton sbtn;                                // Holds the sura button instance
    private SuraTable stblForSearchItems;                   // Holds the sura table instance of "tblSearchItems"
    private SuraTable stblItems;                            // Holds the sura table instance of "tblItems"
    private SuraBoyTextComponenets sbtcmp;

    private KeyEventPostProcessor pstProcessor;

    private BigDecimal emptyBottleTotal = BigDecimal.ZERO;
    private BigDecimal billTotal = BigDecimal.ZERO;
    private BigDecimal finalTotal = BigDecimal.ZERO;

    private CustomOrderDTO customOrder;
    private ChequeDetailsDTO chequeDetails;
    private ArrayList<OrderItemDetailsDTO> orderItemDetails;
    private ArrayList<OrderEmptyBottleDetailsDTO> orderEmptyBottleDetails;

    /**
     * Creates new form CashSales
     */
    public CashSales() {
        initComponents();

        sbtn = new SuraButton(this);
        sbtn.convertAllJButtonsToSuraButtons();
        sbtcmp = new SuraBoyTextComponenets(this);

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
        cmbEmptyBottle.removeAllItems();

        loadEmptyBottles();
        txtEmptyBottleQty.setEnabled((emptyBottleTypes != null) && (emptyBottleTypes.size() != 0));

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
                    Logger.getLogger(CashSales.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(CashSales.class.getName()).log(Level.SEVERE, null, ex);
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

    private void loadEmptyBottles() {
        GRNController ctrl = (GRNController) ControllerFactory.getInstance().getController(SuperController.ControllerType.GRN);
        try {
            emptyBottleTypes = ctrl.getAllEmptyBottleTypes();
            if (emptyBottleTypes != null) {
                orderEmptyBottleDetails = new ArrayList<>();
                cmbEmptyBottle.removeAllItems();
                for (EmptyBottleDTO emptyBottleType : emptyBottleTypes) {
                    cmbEmptyBottle.addItem(emptyBottleType.getBottleType());
                    OrderEmptyBottleDetailsDTO dto = new OrderEmptyBottleDetailsDTO(null,
                            null,
                            emptyBottleType.getBottleType(),
                            0,
                            0);
                    orderEmptyBottleDetails.add(dto);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CashSales.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CashSales.class.getName()).log(Level.SEVERE, null, ex);
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
                if (item.getQty() > 0) {
                    Object[] rowData = {item.getItemCode(), item.getItemName(), String.valueOf(item.getQty()), this.formatPrice(item.getSellingPrice())};
                    dtmSearchItems.addRow(rowData);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CashSales.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CashSales.class.getName()).log(Level.SEVERE, null, ex);
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
            if (chkChequeDetails.isSelected()) {
                if (!txtChequeNumber.getText().trim().isEmpty()
                        && !txtBank.getText().trim().isEmpty()
                        && !txtBranch.getText().trim().isEmpty()) {
                    btnPay.setEnabled(true);
                }
            } else {
                btnPay.setEnabled(true);
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

    private void calculateEmptyBottleCost() {
        BigDecimal total = BigDecimal.valueOf(0);
        lblEmptyBottleCost.setText("-");

        for (OrderEmptyBottleDetailsDTO dto : orderEmptyBottleDetails) {
            total = total.add(BigDecimal.valueOf(dto.getTotal()));
        }

        if (total.compareTo(BigDecimal.ZERO) == 0) {
            lblEmptyBottleCost.setText("-");
        } else {
            String formattedTotal = this.formatPrice(total, true);
            lblEmptyBottleCost.setText(formattedTotal);
        }
        emptyBottleTotal = total;

        calculateTotals();
    }

    private void calculateTotals() {
        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < dtmItems.getRowCount(); i++) {
            String t = (String) dtmItems.getValueAt(i, 5);
            total = total.add(new BigDecimal(t));
        }

        billTotal = total;
        finalTotal = billTotal.add(emptyBottleTotal);

        if (billTotal.compareTo(BigDecimal.ZERO) == 0) {
            lblBillTotal.setText("-");
        } else {
            String formattedTotal = this.formatPrice(billTotal, true);
            lblBillTotal.setText(formattedTotal);
        }

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
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbEmptyBottle = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        txtEmptyBottleQty = new javax.swing.JTextField();
        lblQty = new javax.swing.JLabel();
        txtQty = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        btnSave = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel16 = new javax.swing.JLabel();
        btnPay = new javax.swing.JButton();
        pnlCheque = new javax.swing.JPanel();
        txtBranch = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtBank = new javax.swing.JTextField();
        lbl = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtChequeNumber = new javax.swing.JTextField();
        chkChequeDetails = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        lblBillTotal = new javax.swing.JLabel();
        lblFinalTotal = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        lblEmptyBottleCost = new javax.swing.JLabel();

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
        txtBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBarcodeActionPerformed(evt);
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
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(btnDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(224, 219, 221)));

        jLabel8.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel8.setText("Empty Bottles Details");

        jLabel9.setDisplayedMnemonic('o');
        jLabel9.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel9.setLabelFor(cmbEmptyBottle);
        jLabel9.setText("Bottle Type");

        cmbEmptyBottle.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        cmbEmptyBottle.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbEmptyBottle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbEmptyBottleItemStateChanged(evt);
            }
        });
        cmbEmptyBottle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmbEmptyBottleKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cmbEmptyBottleKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel11.setText("Qty.");

        txtEmptyBottleQty.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtEmptyBottleQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtEmptyBottleQty.setText("0");
        txtEmptyBottleQty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEmptyBottleQtyFocusGained(evt);
            }
        });
        txtEmptyBottleQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmptyBottleQtyKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEmptyBottleQtyKeyTyped(evt);
            }
        });

        lblQty.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblQty.setText("Enter Qty.");

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

        jSeparator2.setForeground(new java.awt.Color(224, 219, 221));

        jSeparator3.setForeground(new java.awt.Color(224, 219, 221));

        jLabel16.setFont(new java.awt.Font("Open Sans", 1, 22)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Total :");

        btnPay.setBackground(new java.awt.Color(76, 175, 80));
        btnPay.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnPay.setForeground(new java.awt.Color(255, 255, 255));
        btnPay.setText("Pay [End]");
        btnPay.setEnabled(false);
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });

        pnlCheque.setBackground(new java.awt.Color(255, 255, 255));

        txtBranch.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtBranch.setEnabled(false);
        txtBranch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBranchFocusGained(evt);
            }
        });
        txtBranch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBranchKeyReleased(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel14.setText("Bank");
        jLabel14.setEnabled(false);

        txtBank.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtBank.setEnabled(false);
        txtBank.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBankFocusGained(evt);
            }
        });
        txtBank.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBankKeyReleased(evt);
            }
        });

        lbl.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lbl.setText("Cheque Number");
        lbl.setEnabled(false);

        jLabel15.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel15.setText("Branch");
        jLabel15.setEnabled(false);

        txtChequeNumber.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtChequeNumber.setEnabled(false);
        txtChequeNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtChequeNumberFocusGained(evt);
            }
        });
        txtChequeNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtChequeNumberKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnlChequeLayout = new javax.swing.GroupLayout(pnlCheque);
        pnlCheque.setLayout(pnlChequeLayout);
        pnlChequeLayout.setHorizontalGroup(
            pnlChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChequeLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(pnlChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtChequeNumber, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBank, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBranch, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlChequeLayout.createSequentialGroup()
                        .addGroup(pnlChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );
        pnlChequeLayout.setVerticalGroup(
            pnlChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChequeLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtChequeNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBank, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBranch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        chkChequeDetails.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        chkChequeDetails.setMnemonic('C');
        chkChequeDetails.setText(" Cheque Details");
        chkChequeDetails.setToolTipText("Click to enable cheque details section");
        chkChequeDetails.setOpaque(false);
        chkChequeDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkChequeDetailsActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Bill Total :");

        lblBillTotal.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        lblBillTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBillTotal.setText("-");

        lblFinalTotal.setFont(new java.awt.Font("Open Sans", 1, 22)); // NOI18N
        lblFinalTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFinalTotal.setText("-");

        jSeparator4.setForeground(new java.awt.Color(224, 219, 221));

        jLabel10.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Bottle Cost :");

        lblEmptyBottleCost.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        lblEmptyBottleCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEmptyBottleCost.setText("-");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtQty)
                    .addComponent(jSeparator1)
                    .addComponent(cmbEmptyBottle, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtEmptyBottleQty)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator3)
                    .addComponent(pnlCheque, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblQty)
                            .addComponent(jLabel11)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(chkChequeDetails))
                        .addGap(0, 77, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblBillTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblFinalTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblEmptyBottleCost, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblQty)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbEmptyBottle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmptyBottleQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkChequeDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlCheque, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(lblBillTotal))
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lblEmptyBottleCost))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(lblFinalTotal))
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

    private void txtQtyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusGained
        ((JTextField) evt.getSource()).selectAll();
    }//GEN-LAST:event_txtQtyFocusGained

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
            if (qty < 10) {
                btnSave.setEnabled(true);
            } else if (evt.getKeyCode() != KeyEvent.VK_ENTER) {
                getToolkit().beep();
                ImageIcon icon = new ImageIcon(CashSales.class.getResource("/lk/ijse/winestores/icons/error_icon.png"));
                JOptionPane.showMessageDialog(FocusManager.getCurrentManager().getActiveWindow(),
                        "Exceed the legal limit for the Qty. Please place a new order for the items which exceed the legal limit.",
                        "Exceed the legal limit for the Qty.",
                        JOptionPane.INFORMATION_MESSAGE,
                        icon);
            }
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

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed

        QueryController ctrlQuery = (QueryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.QUERY);
        try {
            if (ctrlQuery.hasDayEndDone(new Date())){
                ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
                JOptionPane.showMessageDialog(
                        SwingUtilities.getWindowAncestor(this),
                        "Sorry, further sales can not be done today since the day end has been already done.",
                        "No more sales for today",
                        JOptionPane.INFORMATION_MESSAGE,
                        icon);
                return;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CashSales.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CashSales.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (!chkChequeDetails.isSelected()) {
            if (cashTenderForm == null) {
                cashTenderForm = new CashTenderForm(this, finalTotal);
            }
            if (!cashTenderForm.isVisible()) {
                cashTenderForm.setTotal(finalTotal);
                cashTenderForm.initFoucs();
                cashTenderForm.setVisible(true);
            }
        } else {
            processChequeOrder();
        }

    }//GEN-LAST:event_btnPayActionPerformed

    private void tblItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemsMouseClicked
        initUpdate();
    }//GEN-LAST:event_tblItemsMouseClicked

    private void txtQtyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusLost
    }//GEN-LAST:event_txtQtyFocusLost

    private void chkChequeDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkChequeDetailsActionPerformed
        enablePay();
        for (Component cmp : pnlCheque.getComponents()) {
            cmp.setEnabled(chkChequeDetails.isSelected());
        }
        txtChequeNumber.requestFocusInWindow();
    }//GEN-LAST:event_chkChequeDetailsActionPerformed

    private void txtChequeNumberFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChequeNumberFocusGained
        ((JTextField) evt.getSource()).selectAll();
    }//GEN-LAST:event_txtChequeNumberFocusGained

    private void txtBankFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBankFocusGained
        ((JTextField) evt.getSource()).selectAll();
    }//GEN-LAST:event_txtBankFocusGained

    private void txtBranchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBranchFocusGained
        ((JTextField) evt.getSource()).selectAll();
    }//GEN-LAST:event_txtBranchFocusGained

    private void txtChequeNumberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtChequeNumberKeyReleased
        enablePay();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtBank.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtChequeNumberKeyReleased

    private void txtBankKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBankKeyReleased
        enablePay();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtBranch.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtBankKeyReleased

    private void txtBranchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBranchKeyReleased
        enablePay();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnPay.doClick();
        }
    }//GEN-LAST:event_txtBranchKeyReleased

    private void txtEmptyBottleQtyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmptyBottleQtyFocusGained
        ((JTextField) evt.getSource()).selectAll();
    }//GEN-LAST:event_txtEmptyBottleQtyFocusGained

    private void cmbEmptyBottleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbEmptyBottleKeyReleased

    }//GEN-LAST:event_cmbEmptyBottleKeyReleased

    private void txtEmptyBottleQtyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmptyBottleQtyKeyTyped
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            cmbEmptyBottle.requestFocusInWindow();
        }
        if (!Character.isDigit(evt.getKeyChar())) {
            if (!(evt.getKeyChar() == KeyEvent.VK_DELETE | evt.getKeyChar() == KeyEvent.VK_BACKSPACE)) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtEmptyBottleQtyKeyTyped

    private void cmbEmptyBottleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbEmptyBottleKeyTyped
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtEmptyBottleQty.requestFocusInWindow();
        }
    }//GEN-LAST:event_cmbEmptyBottleKeyTyped

    private void txtEmptyBottleQtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmptyBottleQtyKeyReleased
        if (orderEmptyBottleDetails == null) {
            return;
        }

        OrderEmptyBottleDetailsDTO dto = orderEmptyBottleDetails.get(cmbEmptyBottle.getSelectedIndex());
        // If user does some silly, copy and paste thing :(
        try {
            dto.setQty(Integer.parseInt(txtEmptyBottleQty.getText().trim()));
            BigDecimal total = BigDecimal.valueOf(emptyBottleTypes.get(cmbEmptyBottle.getSelectedIndex()).getCost());
            total = total.multiply(new BigDecimal(dto.getQty()));
            dto.setTotal(Double.valueOf(this.formatPrice(total)));
        } catch (NumberFormatException exception) {
            dto.setQty(0);
            dto.setTotal(0);
            txtEmptyBottleQty.setText("0");
            txtEmptyBottleQty.selectAll();
            txtEmptyBottleQty.requestFocusInWindow();
        }
        calculateEmptyBottleCost();
    }//GEN-LAST:event_txtEmptyBottleQtyKeyReleased

    private void cmbEmptyBottleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbEmptyBottleItemStateChanged
        if (orderEmptyBottleDetails == null || orderEmptyBottleDetails.size() == 0) {
            return;
        }

        txtEmptyBottleQty.setText(String.valueOf(orderEmptyBottleDetails.get(cmbEmptyBottle.getSelectedIndex()).getQty()));
    }//GEN-LAST:event_cmbEmptyBottleItemStateChanged

    private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
    }//GEN-LAST:event_formComponentHidden

    private void formAncestorRemoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_formAncestorRemoved
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventPostProcessor(pstProcessor);
    }//GEN-LAST:event_formAncestorRemoved

    private void formAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_formAncestorAdded
        this.pstProcessor = new KeyEventPostProcessor() {
            @Override
            public boolean postProcessKeyEvent(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_END && e.getID() == java.awt.event.KeyEvent.KEY_PRESSED) {
                    btnPay.doClick();
                }
                return false;
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(pstProcessor);
    }//GEN-LAST:event_formAncestorAdded

    private void txtBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarcodeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnPay;
    private javax.swing.JButton btnSave;
    private javax.swing.JCheckBox chkChequeDetails;
    private javax.swing.JComboBox<String> cmbEmptyBottle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lbl;
    private javax.swing.JLabel lblBillTotal;
    private javax.swing.JLabel lblEmptyBottleCost;
    private javax.swing.JLabel lblFinalTotal;
    private javax.swing.JLabel lblQty;
    private javax.swing.JPanel pnlCheque;
    private javax.swing.JTable tblItems;
    private javax.swing.JTable tblSearchItems;
    private javax.swing.JTextField txtBank;
    private javax.swing.JTextField txtBarcode;
    private javax.swing.JTextField txtBranch;
    private javax.swing.JTextField txtChequeNumber;
    private javax.swing.JTextField txtEmptyBottleQty;
    private javax.swing.JTextField txtItemCode;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtSellingPrice;
    // End of variables declaration//GEN-END:variables

    @Override
    public void processOrder(BigDecimal cashTendered) {

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        // Filling the cutomer order
        customOrder = new CustomOrderDTO(null,
                formatDate(new Date()),
                "Admin",
                billTotal.doubleValue(),
                cashTendered,
                (chkChequeDetails.isSelected()) ? "02" : "01");

        // Filling the order item details
        orderItemDetails = new ArrayList<>();
        for (int i = 0; i < dtmItems.getRowCount(); i++) {
            OrderItemDetailsDTO dto = new OrderItemDetailsDTO(
                    null,
                    null,
                    dtmItems.getValueAt(i, 1).toString(),
                    Integer.parseInt(dtmItems.getValueAt(i, 3).toString()),
                    Double.valueOf(dtmItems.getValueAt(i, 4).toString()));
            orderItemDetails.add(dto);
        }
        // Filling cheque details
        if (chkChequeDetails.isSelected()) {
            chequeDetails = new ChequeDetailsDTO(null,
                    null,
                    txtChequeNumber.getText().trim(),
                    txtBank.getText().trim(),
                    txtBranch.getText().trim());
        }

        // Sending data to the controller
        SalesController controller = (SalesController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SALES);
        try {
            boolean success = controller.saveCashSale(customOrder, orderItemDetails, orderEmptyBottleDetails, chequeDetails);
            if (success) {
                // Resetting
//                resetTextFields(null);
//                dtmSearchItems.setRowCount(0);
//                dtmItems.setRowCount(0);
//                if (cmbEmptyBottle.getItemCount() > 0) {
//                    cmbEmptyBottle.setSelectedIndex(0);
//                }
//                txtEmptyBottleQty.setText("0");
//                chkChequeDetails.setSelected(false);
//                txtChequeNumber.setText("");
//                txtBank.setText("");
//                txtBranch.setText("");
//                txtBarcode.requestFocusInWindow();
//                enableQty();
//                enablePay();

                Main m = (Main) SwingUtilities.getWindowAncestor(this);
                m.resetMenu(Main.MenuItems.CASH_SALES);

//                ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/ok.png"));
//                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor((this)),
//                        "Order has been successfully saved.",
//                        "Order Success",
//                        JOptionPane.INFORMATION_MESSAGE,
//                        icon);
            } else {
                ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                        "Sorry, this order can not be saved right now due to some unexpected reason. Please try again.",
                        "Order Failed",
                        JOptionPane.INFORMATION_MESSAGE,
                        icon);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CashSales.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CashSales.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.setCursor(Cursor.getDefaultCursor());
        }

    }

    @Override
    public void initFoucs() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtBarcode.requestFocusInWindow();
                loadEmptyBottles();
            }
        });
    }

    private void processChequeOrder() {

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        BigDecimal cashTendered = BigDecimal.ZERO;

        // Filling the cutomer order
        customOrder = new CustomOrderDTO(null,
                formatDate(new Date()),
                "Admin",
                billTotal.doubleValue(),
                cashTendered,
                "02");

        // Filling the order item details
        orderItemDetails = new ArrayList<>();
        for (int i = 0; i < dtmItems.getRowCount(); i++) {
            OrderItemDetailsDTO dto = new OrderItemDetailsDTO(
                    null,
                    null,
                    dtmItems.getValueAt(i, 1).toString(),
                    Integer.parseInt(dtmItems.getValueAt(i, 3).toString()),
                    Double.valueOf(dtmItems.getValueAt(i, 4).toString()));
            orderItemDetails.add(dto);
        }
        // Filling cheque details
        chequeDetails = new ChequeDetailsDTO(null,
                null,
                txtChequeNumber.getText().trim(),
                txtBank.getText().trim(),
                txtBranch.getText().trim());

        // Sending data to the controller
        SalesController controller = (SalesController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SALES);
        try {
            boolean success = controller.saveCashSale(customOrder, orderItemDetails, orderEmptyBottleDetails, chequeDetails);
            if (success) {
                // Resetting
//                resetTextFields(null);
//                dtmSearchItems.setRowCount(0);
//                dtmItems.setRowCount(0);
//                if (cmbEmptyBottle.getItemCount() > 0) {
//                    cmbEmptyBottle.setSelectedIndex(0);
//                }
//                txtEmptyBottleQty.setText("0");
//                chkChequeDetails.setSelected(false);
//                txtChequeNumber.setText("");
//                txtBank.setText("");
//                txtBranch.setText("");
//                txtBarcode.requestFocusInWindow();
//                enableQty();
//                enablePay();

                Main m = (Main) SwingUtilities.getWindowAncestor(this);
                m.resetMenu(Main.MenuItems.CASH_SALES);

//                ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/ok.png"));
//                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor((this)),
//                        "Order has been successfully saved.",
//                        "Order Success",
//                        JOptionPane.INFORMATION_MESSAGE,
//                        icon);
            } else {
                ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                        "Sorry, this order can not be saved right now due to some unexpected reason. Please try again.",
                        "Order Failed",
                        JOptionPane.INFORMATION_MESSAGE,
                        icon);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CashSales.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CashSales.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.setCursor(Cursor.getDefaultCursor());
        }

    }
}
