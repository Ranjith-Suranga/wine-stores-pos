/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import lk.ijse.winestores.controller.ControllerFactory;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.controller.custom.CustomerController;
import lk.ijse.winestores.dao.dto.CustomerDTO;
import lk.ijse.winestores.views.util.Customer;

/**
 *
 * @author Ranjith Suranga
 */
public class SaveCustomer extends javax.swing.JFrame {

    private SuraButton sbtn;

    private boolean addNew;

    private int customerId = -1;
    private int rowIndex;
    private String customerName;
    private String contactNumbers;
    private String address;

    private CustomerController ctrlCustomer;
    
    private Customer frmCustomer;

    /**
     * Creates new form SaveCustomer
     * @param frmCustomer
     */
    public SaveCustomer(Customer frmCustomer) {
        addNew = true;
        this.frmCustomer = frmCustomer;
        initForm();
    }

    public SaveCustomer(Customer frmCustomer, int rowIndex, int customerId, String customerName, String contactNumbers, String address) {
        this.frmCustomer = frmCustomer;
        this.rowIndex = rowIndex;
        this.customerId = customerId;
        this.customerName = customerName;
        this.contactNumbers = contactNumbers;
        this.address = address;
        addNew = false;
        initForm();
    }

    public void initForm() {
        initComponents();

        sbtn = new SuraButton(pnlContainer);
        sbtn.convertAllJButtonsToSuraButtons();

        btnSave.setEnabled(false);

        ctrlCustomer = (CustomerController) ControllerFactory.getInstance().getController(SuperController.ControllerType.CUSTOMER);

        lblTitle.setText((addNew) ? "New Customer" : "Edit Customer");

        txtCustomerName.setText(customerName);
        txtContactNumbers.setText(contactNumbers);
        txtAddress.setText(address);

        DocumentListener dListner = new SuraDocumentListener();
        txtCustomerName.getDocument().addDocumentListener(dListner);
        txtContactNumbers.getDocument().addDocumentListener(dListner);
        txtAddress.getDocument().addDocumentListener(dListner);

        pnlContainer.setVisible(false);
        lblTitle.setVisible(false);
        lblClose.setVisible(false);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setBackground(new Color(0, 0, 0, 200));
        
        for (Component cmp : pnlContainer.getComponents()) {

            if (cmp instanceof JTextComponent || cmp instanceof JScrollPane) {

                JTextComponent txt = (JTextComponent) ((cmp instanceof JTextComponent) ? cmp : ((JScrollPane) cmp).getViewport().getComponent(0));
                txt.addFocusListener(new FocusAdapter() {

                    @Override
                    public void focusGained(FocusEvent e) {
                        super.focusGained(e);
                        txt.setBorder(
                                javax.swing.BorderFactory.createCompoundBorder(
                                        javax.swing.BorderFactory.createLineBorder(new java.awt.Color(72, 158, 231)),
                                        javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))
                        );
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        super.focusLost(e);
                        txt.setBorder(
                                javax.swing.BorderFactory.createCompoundBorder(
                                        javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234)),
                                        javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))
                        );
                    }

                });
            }
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                // A little hack for linux
                while(SaveCustomer.this.getWidth()<=10){}                   

                Point p = new Point();
                p.x = (SaveCustomer.this.getWidth() - pnlContainer.getWidth()) / 2;
                p.y = (SaveCustomer.this.getHeight() - pnlContainer.getHeight()) / 2;
                pnlContainer.setLocation(p);

                Point p2 = (Point) p.clone();
                p2.x = (SaveCustomer.this.getWidth() - lblTitle.getWidth()) / 2;
                p2.y -= 50;
                lblTitle.setLocation(p2);

                p.x = p.x + pnlContainer.getWidth() - lblClose.getWidth() - 10;
                p.y -= 20;
                lblClose.setLocation(p);

                lblClose.setVisible(true);
                pnlContainer.setVisible(true);
                lblTitle.setVisible(true);

            }
        });
    }

    private void enableSaveBtn() {

        btnSave.setEnabled(false);

        if (!txtCustomerName.getText().trim().isEmpty()
                && !txtContactNumbers.getText().trim().isEmpty()
                && !txtAddress.getText().trim().isEmpty()) {
            btnSave.setEnabled(true);
        }

    }

    private class SuraDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            enableSaveBtn();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            enableSaveBtn();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            enableSaveBtn();
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

        lblClose = new javax.swing.JLabel();
        pnlContainer = new javax.swing.JPanel(){
            public void paintComponent(Graphics g) {
                //To change body of generated methods, choose Tools | Templates.
                Graphics2D gd = (Graphics2D)g;
                gd.setColor(pnlContainer.getBackground());
                gd.fillRoundRect(0, 0, pnlContainer.getWidth(), pnlContainer.getHeight(), 10, 10);
            }
        };
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        txtContactNumbers = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAddress = new javax.swing.JTextArea();
        btnSave = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblTitle = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(null);

        lblClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/close.png"))); // NOI18N
        lblClose.setToolTipText("Close");
        lblClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCloseMouseClicked(evt);
            }
        });
        getContentPane().add(lblClose);
        lblClose.setBounds(570, 10, 50, 40);

        pnlContainer.setBackground(new java.awt.Color(255, 255, 255));
        pnlContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlContainerMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Customer Name :");

        jLabel2.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Contact Numbers :");

        jLabel3.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Address :");

        txtCustomerName.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtCustomerName.setBorder(
            javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234)),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );
        txtCustomerName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCustomerNameFocusGained(evt);
            }
        });
        txtCustomerName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCustomerNameKeyReleased(evt);
            }
        });

        txtContactNumbers.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtContactNumbers.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234)),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
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

    jScrollPane1.setBorder(null);

    txtAddress.setColumns(20);
    txtAddress.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
    txtAddress.setRows(5);
    txtAddress.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234)),
        javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
txtAddress.addFocusListener(new java.awt.event.FocusAdapter() {
    public void focusGained(java.awt.event.FocusEvent evt) {
        txtAddressFocusGained(evt);
    }
    });
    txtAddress.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            txtAddressKeyPressed(evt);
        }
        public void keyReleased(java.awt.event.KeyEvent evt) {
            txtAddressKeyReleased(evt);
        }
        public void keyTyped(java.awt.event.KeyEvent evt) {
            txtAddressKeyTyped(evt);
        }
    });
    jScrollPane1.setViewportView(txtAddress);

    btnSave.setBackground(new java.awt.Color(72, 158, 231));
    btnSave.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
    btnSave.setForeground(new java.awt.Color(255, 255, 255));
    btnSave.setText("Save");
    btnSave.setToolTipText("Click to save the changes you have made");
    btnSave.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnSaveActionPerformed(evt);
        }
    });

    jLabel4.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
    jLabel4.setText("Customer's Details");

    jSeparator1.setForeground(new java.awt.Color(224, 219, 221));

    javax.swing.GroupLayout pnlContainerLayout = new javax.swing.GroupLayout(pnlContainer);
    pnlContainer.setLayout(pnlContainerLayout);
    pnlContainerLayout.setHorizontalGroup(
        pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(pnlContainerLayout.createSequentialGroup()
            .addGap(20, 20, 20)
            .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlContainerLayout.createSequentialGroup()
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(pnlContainerLayout.createSequentialGroup()
                    .addComponent(jLabel4)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(pnlContainerLayout.createSequentialGroup()
                    .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlContainerLayout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtCustomerName))
                        .addGroup(pnlContainerLayout.createSequentialGroup()
                            .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel3))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtContactNumbers)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                                .addGroup(pnlContainerLayout.createSequentialGroup()
                                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE)))))
                    .addGap(20, 20, 20))))
    );

    pnlContainerLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3});

    pnlContainerLayout.setVerticalGroup(
        pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(pnlContainerLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel4)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2)
                .addComponent(txtContactNumbers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlContainerLayout.createSequentialGroup()
                    .addGap(3, 3, 3)
                    .addComponent(jLabel3)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(45, Short.MAX_VALUE))
    );

    getContentPane().add(pnlContainer);
    pnlContainer.setBounds(10, 60, 570, 340);

    lblTitle.setFont(new java.awt.Font("Open Sans", 1, 36)); // NOI18N
    lblTitle.setForeground(new java.awt.Color(255, 255, 255));
    lblTitle.setText("New Customer");
    getContentPane().add(lblTitle);
    lblTitle.setBounds(210, 10, 290, 50);

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCloseMouseClicked
        this.dispose();
    }//GEN-LAST:event_lblCloseMouseClicked

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        this.dispose();
    }//GEN-LAST:event_formMouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        CustomerDTO customer = new CustomerDTO(customerId,
                txtCustomerName.getText().trim(),
                txtContactNumbers.getText().trim(),
                txtAddress.getText().trim());

        try {

            boolean result;

            if (addNew) {
                customerId = ctrlCustomer.saveCustomer(customer);
                customer.setCustomerId(customerId);
                result = true;
                if (result){
                    if (frmCustomer instanceof Customers){
                        ((Customers) frmCustomer).loadAllCustomers();
                    }else{
                        frmCustomer.addCustomer(customer);
                    }
                }
            } else {
                result = ctrlCustomer.changeCustomer(customerId, customer);
                if (result){
                    ((Customers) frmCustomer).changeCustomer(rowIndex, customer);
                }
            }

            if (!result){
                ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
                JOptionPane.showMessageDialog(this,
                        "Can't save the customer information due to unexpected reason. Please try again later.", "Save Failed",
                        JOptionPane.ERROR_MESSAGE,
                        icon);
                txtCustomerName.requestFocusInWindow();
            }else{
                this.dispose();
            }

        } catch (SQLException exception) {
            ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
            JOptionPane.showMessageDialog(this,
                    "Can't save the customer information due to unexpected reason. Please try again later.", "Save Failed",
                    JOptionPane.ERROR_MESSAGE,
                    icon);
            txtCustomerName.requestFocusInWindow();
            Logger.getLogger(SaveCustomer.class.getName()).log(Level.SEVERE, null, exception);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SaveCustomer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnSaveActionPerformed

    private void txtCustomerNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustomerNameKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtContactNumbers.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtCustomerNameKeyReleased

    private void txtContactNumbersKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContactNumbersKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtAddress.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtContactNumbersKeyReleased

    private void txtAddressKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyReleased

    }//GEN-LAST:event_txtAddressKeyReleased

    private void txtAddressKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyTyped

    }//GEN-LAST:event_txtAddressKeyTyped

    private void txtAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            btnSave.requestFocusInWindow();
            btnSave.doClick();
        }
    }//GEN-LAST:event_txtAddressKeyPressed

    private void txtCustomerNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerNameFocusGained
        txtCustomerName.selectAll();
    }//GEN-LAST:event_txtCustomerNameFocusGained

    private void txtContactNumbersFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContactNumbersFocusGained
        txtContactNumbers.selectAll();
    }//GEN-LAST:event_txtContactNumbersFocusGained

    private void txtAddressFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAddressFocusGained
        txtAddress.selectAll();
    }//GEN-LAST:event_txtAddressFocusGained

    private void pnlContainerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlContainerMouseClicked

    }//GEN-LAST:event_pnlContainerMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblClose;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlContainer;
    private javax.swing.JTextArea txtAddress;
    private javax.swing.JTextField txtContactNumbers;
    private javax.swing.JTextField txtCustomerName;
    // End of variables declaration//GEN-END:variables
}
