/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import java.awt.event.KeyEvent;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import lk.ijse.winestores.controller.ControllerFactory;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.controller.custom.NewItemController;
import lk.ijse.winestores.dao.dto.CustomItemDetailsDTO;
import lk.ijse.winestores.dao.dto.ItemDetailsDTO;
import lk.ijse.winestores.views.util.Extension;

/**
 *
 * @author Ranjith Suranga
 */
public class ChangingPrice extends javax.swing.JPanel implements Extension {

    private NewItemController itemCtrl;

    private SuraButton sbtn;                    // Holds the instance of SuraButton
    private SuraTable stbl;                     // Holds the instance of SuraTable

    private DefaultTableModel dtm;              // Holds the default table model of the table
    private DefaultCellEditor cellEditor;

    /**
     * Creates new form InitialStockTransfer
     */
    public ChangingPrice() {
        initComponents();
        prgLoading.setVisible(false);

        sbtn = new SuraButton(this);
        sbtn.convertAllJButtonsToSuraButtons();

        dtm = (DefaultTableModel) tblItems.getModel();
        dtm.setRowCount(0);
        stbl = new SuraTable(tblItems);

        for (int i = 0; i < tblItems.getColumnCount(); i++) {
            if (i == 1) {
                continue;
            } else if (i == 0) {
                stbl.setColumnAlignment(i, SwingConstants.CENTER);
            } else {
                stbl.setColumnAlignment(i, SwingConstants.RIGHT);
            }
            stbl.setHeaderAlignment(i, SwingConstants.CENTER);
        }

//        tblItems.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                if (pnlProtectedView.isVisible()) {
//                    tblItems.clearSelection();
//                }
//            }
//        });
        itemCtrl = (NewItemController) ControllerFactory.getInstance().getController(SuperController.ControllerType.NEW_ITEM);

        Thread itemLoadingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<CustomItemDetailsDTO> items = itemCtrl.getAllItems();
                    if (items == null) {
                        return;
                    }
                    prgLoading.setVisible(true);
                    prgLoading.setMaximum(items.size());
                    for (CustomItemDetailsDTO item : items) {
                        Object[] rowData = {
                            item.getItemCode(),
                            item.getItemName(),
                            //                            item.getBarCode(),
                            formatPrice(item.getBuyingPrice()),
                            formatPrice(item.getSellingPrice()), //                            String.valueOf(item.getQty())
                        };
                        dtm.addRow(rowData);
                        if (dtm.getRowCount() == 1) {
                            tblItems.getSelectionModel().setSelectionInterval(0, 0);
                            tblItems.requestFocusInWindow();
                        }
                        prgLoading.setValue(prgLoading.getValue() + 1);
                        int precentage = (int) (((double) prgLoading.getValue() / prgLoading.getMaximum()) * 100);
                        prgLoading.setString("Loading " + String.valueOf(precentage) + "%");
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ChangingPrice.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    prgLoading.setVisible(false);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ChangingPrice.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ChangingPrice.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        itemLoadingThread.setPriority(Thread.MIN_PRIORITY);
        itemLoadingThread.start();

        cellEditor = new DefaultCellEditor(txtEditor) {

            @Override
            public boolean stopCellEditing() {
                if (!isValidate()) {
                    txtEditor.selectAll();
                    return false;
                }

                double buyingPrice = (tblItems.getEditingColumn() == 2) ? Double.valueOf((String) cellEditor.getCellEditorValue()) : Double.valueOf((String) tblItems.getValueAt(tblItems.getEditingRow(), 2));
                double sellingPrice = (tblItems.getEditingColumn() == 3) ? Double.valueOf((String) cellEditor.getCellEditorValue()) : Double.valueOf((String) tblItems.getValueAt(tblItems.getEditingRow(), 3));
                //String barCode = (tblItems.getEditingColumn() == 2) ? cellEditor.getCellEditorValue().toString() : (String) tblItems.getValueAt(tblItems.getEditingRow(), 2);

                ItemDetailsDTO dto = new ItemDetailsDTO(
                        dtm.getValueAt(tblItems.getEditingRow(), 0).toString(),
                        -1, // Qty
                        sellingPrice, // Selling Price
                        buyingPrice // Buying Price
                );
                try {
                    dto.setQty(itemCtrl.getQty(tblItems.getValueAt(tblItems.getEditingRow(), 0).toString()));
                    boolean success = itemCtrl.saveItem(dto);
                    if (!success) {
                        ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(ChangingPrice.this),
                                "Sorry, The data you entered can not be saved right now due to unexpected reason",
                                "Saving Failed",
                                JOptionPane.ERROR_MESSAGE,
                                icon);
                    }
//                    success = itemCtrl.saveBarCode(dto.getItemCode(), barCode);

//                    if (!success) {
//                        ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
//                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(ChangingPrice.this),
//                                "Sorry, The barcode you entered can not be saved right now due to unexpected reason",
//                                "Saving Failed",
//                                JOptionPane.ERROR_MESSAGE,
//                                icon);
//                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(InitialStockTransfer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(InitialStockTransfer.class.getName()).log(Level.SEVERE, null, ex);
                }

                return super.stopCellEditing();
            }

        };

        tblItems.getColumnModel().getColumn(2).setCellEditor(cellEditor);
        tblItems.getColumnModel().getColumn(3).setCellEditor(cellEditor);

    }

    private String formatPrice(Object price) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(false);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        return nf.format(price);
    }

    private boolean isValidate() {

        if (tblItems.getEditingColumn() == 2 || tblItems.getEditingColumn() == 3) {
            String txt = txtEditor.getText();
            if (txt.lastIndexOf(".") != txt.indexOf(".")) {
                return false;
            }
            txt = formatPrice(Double.valueOf(txt));
            if (txt.matches("\\d+[.]\\d{2}")) {
                txtEditor.setText(txt);
                return true;
            }
        }
//        else if (tblItems.getEditingColumn() == 5) {
//            String txt = txtEditor.getText();
//            if (txt.matches("\\d+")) {
//                return true;
//            }
//        } else if (tblItems.getEditingColumn() == 2) {
//            return true;
//        }
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

        txtEditor = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblGrnId = new javax.swing.JLabel();
        prgLoading = new javax.swing.JProgressBar();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblItems = new javax.swing.JTable();

        txtEditor.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtEditor.setText("jTextField1");
        txtEditor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEditorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEditorFocusLost(evt);
            }
        });
        txtEditor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEditorKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEditorKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEditorKeyTyped(evt);
            }
        });

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(249, 249, 249));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(220, 225, 234)));

        jLabel3.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/change_price.png"))); // NOI18N
        jLabel3.setText("Changing Prices of All the Current Items");
        jLabel3.setIconTextGap(10);

        lblGrnId.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N

        prgLoading.setString("Loading 0%");
        prgLoading.setStringPainted(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(77, 77, 77)
                .addComponent(lblGrnId, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(prgLoading, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblGrnId)
                    .addComponent(prgLoading, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234)));

        tblItems.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        tblItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Code", "Item Name", "Cost Price", "Selling Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblItems.setGridColor(new java.awt.Color(204, 204, 204));
        tblItems.setRowHeight(50);
        tblItems.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblItems.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblItems.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblItems.getTableHeader().setReorderingAllowed(false);
        tblItems.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblItemsKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tblItemsKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(tblItems);
        if (tblItems.getColumnModel().getColumnCount() > 0) {
            tblItems.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblItems.getColumnModel().getColumn(1).setPreferredWidth(600);
            tblItems.getColumnModel().getColumn(2).setPreferredWidth(150);
            tblItems.getColumnModel().getColumn(3).setPreferredWidth(150);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1)
                .addGap(0, 0, 0))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtEditorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEditorKeyPressed
        if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_V) {
            evt.consume();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            switch (tblItems.getEditingColumn()) {
//                case 2:
//                    tblItems.editCellAt(tblItems.getSelectedRow(), 3);
//                    txtEditor.requestFocusInWindow();
//                    evt.consume();
//                    break;
                case 2:
                    if (isValidate()) {
                        tblItems.editCellAt(tblItems.getSelectedRow(), 3);
                        txtEditor.requestFocusInWindow();
                    } else {
                        txtEditor.selectAll();
                    }
                    evt.consume();
                    break;
                case 3:
                    if (isValidate()) {
                        if ((tblItems.getSelectedRow() + 1) < tblItems.getRowCount()) {
                            tblItems.getSelectionModel().setSelectionInterval(tblItems.getSelectedRow() + 1, tblItems.getSelectedRow() + 1);
//                        txtEditor.requestFocusInWindow();
                        }
                    } else {
                        txtEditor.selectAll();
                    }
                    break;
//                default:
//                    if ((tblItems.getSelectedRow() + 1) < tblItems.getRowCount()) {
//                        tblItems.getSelectionModel().setSelectionInterval(tblItems.getSelectedRow() + 1, tblItems.getSelectedRow() + 1);
////                        txtEditor.requestFocusInWindow();
//                    }
//                    break;
            }
        }
    }//GEN-LAST:event_txtEditorKeyPressed

    private void tblItemsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblItemsKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tblItems.editCellAt(tblItems.getSelectedRow(), 2);
            txtEditor.requestFocusInWindow();
            evt.consume();
        }
    }//GEN-LAST:event_tblItemsKeyPressed

    private void tblItemsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblItemsKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_tblItemsKeyTyped

    private void txtEditorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEditorFocusGained
        txtEditor.selectAll();
    }//GEN-LAST:event_txtEditorFocusGained

    private void txtEditorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEditorKeyReleased

    }//GEN-LAST:event_txtEditorKeyReleased

    private void txtEditorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEditorKeyTyped

//        if (tblItems.getEditingColumn() != 2) {
        if (!Character.isDigit(evt.getKeyChar())) {
            if (!(evt.getKeyChar() == KeyEvent.VK_DELETE || evt.getKeyChar() == KeyEvent.VK_BACK_SPACE)) {
                if (!(tblItems.getEditingColumn() != 5 && (evt.getKeyChar() == KeyEvent.VK_PERIOD))) {
                    evt.consume();
                }
            }
        }
//        }
    }//GEN-LAST:event_txtEditorKeyTyped

    private void txtEditorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEditorFocusLost

    }//GEN-LAST:event_txtEditorFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblGrnId;
    private javax.swing.JProgressBar prgLoading;
    private javax.swing.JTable tblItems;
    private javax.swing.JTextField txtEditor;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean exit() {
        return true;
    }

    @Override
    public String getExtensionName() {
        return "Changing Prices";
    }
}
