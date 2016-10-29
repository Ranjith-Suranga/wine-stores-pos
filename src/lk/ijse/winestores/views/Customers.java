/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import com.sun.glass.events.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import lk.ijse.winestores.controller.ControllerFactory;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.controller.custom.CustomerController;
import lk.ijse.winestores.controller.custom.QueryController;
import lk.ijse.winestores.dao.dto.CustomerDTO;
import lk.ijse.winestores.views.util.Customer;
import lk.ijse.winestores.views.util.FocusHandler;

/**
 *
 * @author Ranjith Suranga
 */
public class Customers extends javax.swing.JPanel implements FocusHandler, Customer{

    private SuraButton sbtn;
    private SuraTable stbl;

    private DefaultTableModel dtm;
    private TableRowSorter rowSorter;

    private QueryController ctrlQuery;
    private CustomerController ctrlCustomer;

    public Customers() {

        initForm();

//        Object[] rowData = {"01", "D M R Suranga","0772048857","Earth"};
//        DefaultTableModel model = (DefaultTableModel) tblCustomers.getModel();
//        model.addRow(rowData);
//        model.addRow(rowData);
//        model.addRow(rowData);
    }

    private void initForm() {

        initComponents();

        ctrlQuery = (QueryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.QUERY);
        ctrlCustomer = (CustomerController) ControllerFactory.getInstance().getController(SuperController.ControllerType.CUSTOMER);
        
        sbtn = new SuraButton(pnlHeader);
        sbtn.convertAllJButtonsToSuraButtons();

        stbl = new SuraTable(tblCustomers);

        for (int i = 0; i < tblCustomers.getColumnCount(); i++) {
            if (i == 1 || i == 3) {
                continue;
            }
            stbl.setHeaderAlignment(i, SwingConstants.CENTER);
        }

        // Centering CustomerID & Contact Number
        stbl.setColumnAlignment(0, SwingConstants.CENTER);
        stbl.setColumnAlignment(2, SwingConstants.CENTER);

        dtm = (DefaultTableModel) tblCustomers.getModel();
        rowSorter = new TableRowSorter(dtm){
            @Override
            public boolean isSortable(int column) {
                return false; 
            }
            
        };
        tblCustomers.setRowSorter(rowSorter);
        //rowSorter.
        
        loadAllCustomers();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtCustomerName.requestFocusInWindow();
            }
        });
        
        tblCustomers.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                btnEdit.setEnabled(false);
                btnDelete.setEnabled(false);
                
                if (tblCustomers.getSelectedRow() != -1){
                    btnEdit.setEnabled(true);
                    btnDelete.setEnabled(true);
                }
            }
        });
        
        txtCustomerName.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                showResult();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                showResult();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                showResult();
            }
            
            private void showResult(){
                rowSorter.setRowFilter( RowFilter.regexFilter(txtCustomerName.getText().trim(), 1));
            }
        });
        
        
    }

    public void loadAllCustomers() {

        dtm.setRowCount(0);

        try {

            ArrayList<CustomerDTO> allCustomers = ctrlQuery.getAllCustomers();

            if (allCustomers != null) {
                for (CustomerDTO customer : allCustomers) {
                    Object[] rowData = {String.valueOf(customer.getCustomerId()),
                        customer.getCustomerName(),
                        customer.getTelephoneNumber(),
                        customer.getAddress()};
                    dtm.addRow(rowData);
                }
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Customers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Customers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void changeCustomer(int rowIndex, CustomerDTO customer) {
        dtm.removeRow(rowIndex);
        Object[] rowData = {String.valueOf(customer.getCustomerId()),
            customer.getCustomerName(),
            customer.getTelephoneNumber(),
            customer.getAddress()
        };
        dtm.insertRow(rowIndex, rowData);
    }
   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane(){
            {
                //getViewport().setBackground(Color.WHITE);
            }
        };
        tblCustomers = new javax.swing.JTable();

        pnlHeader.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setDisplayedMnemonic('C');
        jLabel1.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel1.setLabelFor(txtCustomerName);
        jLabel1.setText("Enter Customer Name to find customer(s)");
        jLabel1.setToolTipText("");

        txtCustomerName.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtCustomerName.setBorder(
            javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234)),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );
        txtCustomerName.setMargin(new java.awt.Insets(3, 5, 3, 3));
        txtCustomerName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCustomerNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerNameFocusLost(evt);
            }
        });

        btnAdd.setBackground(new java.awt.Color(72, 158, 231));
        btnAdd.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setToolTipText("Click to add new customer");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(240, 173, 78));
        btnEdit.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setMnemonic('E');
        btnEdit.setText("Edit");
        btnEdit.setToolTipText("Click to edit selected customer");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(251, 93, 93));
        btnDelete.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setMnemonic('e');
        btnDelete.setText("Delete");
        btnDelete.setToolTipText("Click to delete selected customer");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jLabel2.setDisplayedMnemonic('L');
        jLabel2.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel2.setLabelFor(tblCustomers);
        jLabel2.setText("List of Customers");

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlHeaderLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtCustomerName)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHeaderLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pnlHeaderLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAdd, btnDelete, btnEdit});

        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(153, 0, 51));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234)));
        jPanel3.setOpaque(false);

        jScrollPane1.setBackground(new java.awt.Color(255, 102, 0));
        jScrollPane1.setBorder(null);
        jScrollPane1.setOpaque(false);

        tblCustomers.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        tblCustomers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Customer ID", "Customer Name", "Contact Number(s)", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCustomers.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblCustomers.setGridColor(new java.awt.Color(233, 236, 242));
        tblCustomers.setOpaque(false);
        tblCustomers.setRowHeight(50);
        tblCustomers.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblCustomers.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblCustomers.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblCustomers.getTableHeader().setReorderingAllowed(false);
        tblCustomers.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tblCustomersFocusGained(evt);
            }
        });
        tblCustomers.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tblCustomersKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(tblCustomers);
        if (tblCustomers.getColumnModel().getColumnCount() > 0) {
            tblCustomers.getColumnModel().getColumn(0).setMinWidth(130);
            tblCustomers.getColumnModel().getColumn(0).setPreferredWidth(130);
            tblCustomers.getColumnModel().getColumn(0).setMaxWidth(130);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        new SaveCustomer(this).setVisible(true);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        new SaveCustomer(this,
        tblCustomers.convertRowIndexToModel(tblCustomers.getSelectedRow()),
        Integer.parseInt(tblCustomers.getValueAt(tblCustomers.getSelectedRow(), 0).toString()),
        tblCustomers.getValueAt(tblCustomers.getSelectedRow(), 1).toString(),
                tblCustomers.getValueAt(tblCustomers.getSelectedRow(), 2).toString(),
                tblCustomers.getValueAt(tblCustomers.getSelectedRow(), 3).toString()).setVisible(true);
        
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        try {
            boolean result = ctrlCustomer.deleteCustomer(Integer.parseInt((String) tblCustomers.getValueAt(tblCustomers.getSelectedRow(), 0)));
            if (result){
                dtm.removeRow(tblCustomers.getSelectedRow());
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Customers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Customers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void txtCustomerNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerNameFocusGained

    }//GEN-LAST:event_txtCustomerNameFocusGained

    private void txtCustomerNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerNameFocusLost

    }//GEN-LAST:event_txtCustomerNameFocusLost

    private void tblCustomersFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblCustomersFocusGained
        if (dtm.getRowCount() > 0){
            if (tblCustomers.getSelectedRow() == -1){
                tblCustomers.getSelectionModel().setSelectionInterval(0, 0);
            }
        }
    }//GEN-LAST:event_tblCustomersFocusGained

    private void tblCustomersKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCustomersKeyTyped
        if (evt.getKeyChar() == KeyEvent.VK_ENTER && tblCustomers.getSelectedRow() != -1){
            btnEdit.doClick();
        }
    }//GEN-LAST:event_tblCustomersKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JTable tblCustomers;
    private javax.swing.JTextField txtCustomerName;
    // End of variables declaration//GEN-END:variables

    @Override
    public void initFoucs() {
        txtCustomerName.requestFocusInWindow();
    }

    @Override
    public void addCustomer(CustomerDTO customer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
