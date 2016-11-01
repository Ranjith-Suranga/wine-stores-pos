/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import com.sun.glass.events.KeyEvent;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import lk.ijse.winestores.controller.ControllerFactory;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.controller.custom.SupplierController;
import lk.ijse.winestores.dao.dto.SupplierDTO;
import lk.ijse.winestores.views.util.Extension;

/**
 *
 * @author Ranjith Suranga
 */
public class SaveSupplier extends javax.swing.JPanel implements Extension{
    
    private SuraButton sb;          // Holds the SuraButton Instance
    
    private boolean changed;            // Holds whether new changes have been made

    private SupplierDTO supplier;       // Holds the current supplier object
    private Status status;              // Holds the current status, whether to update or add

    @Override
    public boolean exit() {
        if (changed){
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
        return (this.status == Status.ADD) ? "New Supplier" : "Edit Supplier";
    }

    private enum Status {
        ADD, EDIT
    }

    /**
     * Creates new form SaveSupplier
     */
    public SaveSupplier() {
        initComponents();
        init();
        
        this.supplier = new SupplierDTO();
        this.status = Status.ADD;
        
        addDocumentListeners();
    }
    
    public SaveSupplier(SupplierDTO supplier) {
        initComponents();
        init();
        
        this.supplier = supplier;
        txtName.setText(supplier.getName());
        txtAddress.setText(supplier.getAddress());
        txtTelephoneNumber.setText(supplier.getContact());
        txtFaxNumber.setText(supplier.getFax());
        txtCoordinatorName.setText(supplier.getCordintatorName());
        txtCoordinatorPhoneNumber.setText(supplier.getCordinatorContact());
        txtEMail.setText(supplier.getEmail());
        txtAgentName.setText(supplier.getAgentName());
        txtAgentNumber.setText(supplier.getAgenNo());
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date addedDate = null;
        try {
            if (supplier.getSupplierAdded() != null) {
                addedDate = sdf.parse(supplier.getSupplierAdded());
            }
        } catch (ParseException ex) {
            Logger.getLogger(SaveSupplier.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtDate.setDate(addedDate);
        
        this.status = Status.EDIT;
        
        addDocumentListeners();
    }
    
    private void addDocumentListeners(){
        
        for (Component cmp : pnlMainContainer.getComponents()) {
            if (cmp instanceof JTextComponent){
                JTextComponent txt = (JTextComponent) cmp;
                txt.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        changed = true;
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        changed = true;
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        changed = true;
                    }
                });
            }
        } 
        
        txtAddress.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed = true;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed = true;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed = true;
            }
        });
    }
    
    public void init() {
        sb = new SuraButton(this);
        sb.convertAllJButtonsToSuraButtons();
        handleEvents();
        
        txtDate.setDate(new Date());
    }
    
    private void handleEvents() {
        // Selecting all the text when the textfield is focused
        for (Component c : pnlMainContainer.getComponents()) {
            
            if (c instanceof JTextComponent) {
                
                c.addFocusListener(new FocusAdapter() {
                    
                    @Override
                    public void focusGained(FocusEvent e) {
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                ((JTextComponent) c).selectAll();
                            }
                        });
                    }
                    
                });
            }
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtName.requestFocusInWindow();
            }
        });
        
        txtDate.getEditor().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                super.keyPressed(e);
                
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    try {
                        txtDate.commitEdit();
                    } catch (ParseException ex) {
                        //Logger.getLogger(SaveSupplier.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    btnSave.doClick();
                }
            }
        });
        
        
    }
    
    private String formatDate(Date date) {
        
        if (date == null) {
            return null;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
        
    }
    
    private void enableSave() {
        
        if (txtName.getText().trim().isEmpty() || txtAddress.getText().trim().isEmpty()) {
            btnSave.setEnabled(false);
        } else {
            btnSave.setEnabled(true);
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

        pnlMainContainer = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtTelephoneNumber = new javax.swing.JTextField();
        txtEMail = new javax.swing.JTextField();
        txtCoordinatorName = new javax.swing.JTextField();
        txtAgentName = new javax.swing.JTextField();
        txtAgentNumber = new javax.swing.JTextField();
        txtDate = new org.jdesktop.swingx.JXDatePicker();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAddress = new javax.swing.JTextArea();
        btnSave = new javax.swing.JButton();
        btnClearAll = new javax.swing.JButton();
        txtFaxNumber = new javax.swing.JTextField();
        txtCoordinatorPhoneNumber = new javax.swing.JTextField();

        setBackground(new java.awt.Color(233, 236, 242));

        pnlMainContainer.setBackground(new java.awt.Color(255, 255, 255));
        pnlMainContainer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234), 2));

        jPanel2.setBackground(new java.awt.Color(249, 249, 249));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(220, 225, 234)));

        jLabel1.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/edit.png"))); // NOI18N
        jLabel1.setText("Supplier Information");
        jLabel1.setIconTextGap(10);

        jLabel2.setFont(new java.awt.Font("Open Sans", 0, 11)); // NOI18N
        jLabel2.setText("( Fields with * are mandatory)");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setDisplayedMnemonic('N');
        jLabel3.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setLabelFor(txtName);
        jLabel3.setText("Name * :");

        jLabel4.setDisplayedMnemonic('A');
        jLabel4.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setLabelFor(txtAddress);
        jLabel4.setText("Address * :");

        jLabel5.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Telephone Number :");

        jLabel6.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Fax Number :");

        jLabel7.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("E-mail :");

        jLabel8.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Co-ordinator's Name :");

        jLabel9.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Co-ordinator's Phone Number :");

        jLabel10.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Agent Name :");

        jLabel11.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Agent Telephone Number :");

        jLabel12.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Date :");

        txtName.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtName.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtNameCaretUpdate(evt);
            }
        });
        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });
        txtName.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtNamePropertyChange(evt);
            }
        });
        txtName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNameKeyPressed(evt);
            }
        });

        txtTelephoneNumber.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtTelephoneNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelephoneNumberKeyPressed(evt);
            }
        });

        txtEMail.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtEMail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEMailFocusLost(evt);
            }
        });
        txtEMail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEMailKeyPressed(evt);
            }
        });

        txtCoordinatorName.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtCoordinatorName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCoordinatorNameKeyPressed(evt);
            }
        });

        txtAgentName.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtAgentName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAgentNameKeyPressed(evt);
            }
        });

        txtAgentNumber.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtAgentNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAgentNumberKeyPressed(evt);
            }
        });

        txtDate.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtDate.setFormats(new SimpleDateFormat("yyyy-MM-dd"));
        txtDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDateFocusGained(evt);
            }
        });
        txtDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDateActionPerformed(evt);
            }
        });
        txtDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDateKeyPressed(evt);
            }
        });

        txtAddress.setColumns(20);
        txtAddress.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtAddress.setRows(5);
        txtAddress.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtAddressCaretUpdate(evt);
            }
        });
        txtAddress.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAddressFocusGained(evt);
            }
        });
        txtAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAddressKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(txtAddress);

        btnSave.setBackground(new java.awt.Color(72, 158, 231));
        btnSave.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setMnemonic('S');
        btnSave.setText("Save");
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnClearAll.setBackground(new java.awt.Color(72, 158, 231));
        btnClearAll.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnClearAll.setForeground(new java.awt.Color(255, 255, 255));
        btnClearAll.setMnemonic('C');
        btnClearAll.setText("Clear All");
        btnClearAll.setToolTipText("Click to clear all the text fields");
        btnClearAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearAllActionPerformed(evt);
            }
        });

        txtFaxNumber.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtFaxNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFaxNumberKeyTyped(evt);
            }
        });

        txtCoordinatorPhoneNumber.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtCoordinatorPhoneNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCoordinatorPhoneNumberKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout pnlMainContainerLayout = new javax.swing.GroupLayout(pnlMainContainer);
        pnlMainContainer.setLayout(pnlMainContainerLayout);
        pnlMainContainerLayout.setHorizontalGroup(
            pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainContainerLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCoordinatorName))
                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEMail))
                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTelephoneNumber))
                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName))
                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                        .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAgentNumber)
                            .addComponent(txtAgentName)))
                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE))
                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFaxNumber))
                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCoordinatorPhoneNumber))
                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlMainContainerLayout.createSequentialGroup()
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnClearAll, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        pnlMainContainerLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel10, jLabel11, jLabel12, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, jLabel8, jLabel9});

        pnlMainContainerLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnClearAll, btnSave});

        pnlMainContainerLayout.setVerticalGroup(
            pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainContainerLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtTelephoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtFaxNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtEMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtCoordinatorName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtCoordinatorPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtAgentName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtAgentNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnClearAll))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlMainContainerLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnClearAll, btnSave});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlMainContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlMainContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        // Fill the supplier object with details
        supplier.setName(txtName.getText().trim());
        supplier.setAddress(txtAddress.getText().trim());
        supplier.setContact(txtTelephoneNumber.getText().trim());
        supplier.setFax(txtFaxNumber.getText().trim());
        supplier.setCordintatorName(txtCoordinatorName.getText().trim());
        supplier.setCordinatorContact(txtCoordinatorPhoneNumber.getText().trim());
        supplier.setAgentName(txtAgentName.getText().trim());
        supplier.setAgenNo(txtAgentNumber.getText().trim());
        supplier.setSupplierAdded(this.formatDate(txtDate.getDate()));
        supplier.setEmail(txtEMail.getText().trim());
        
        SupplierController ctrl = (SupplierController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SUPPLIER);
        
        try {
            
            int result;
            
            if (status == Status.ADD) {
                
                result = ctrl.saveSupplier(supplier);
                
            } else {
                
                result = ctrl.changeSupplier(supplier);
            }
            
            if (result > 0) {
                Main m = (Main) SwingUtilities.getWindowAncestor(this);
//                m.pnlContainer.removeAll();
//                Suppliers s = new Suppliers();
//                m.pnlContainer.add(s);
//                m.pnlContainer.updateUI();
                m.resetMenu(Main.MenuItems.SUPPLIER_MASTER);
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SaveSupplier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {

            // Error code 1062 means Duplicate entry
            if (ex.getErrorCode() == 1062) {
                
                ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Sorry, this supplier name is already exists. Please try a different name.", "Dupplicate Supplier", JOptionPane.INFORMATION_MESSAGE, icon);
                txtName.requestFocusInWindow();
                
            } else {
                
                Logger.getLogger(SaveSupplier.class.getName()).log(Level.SEVERE, null, ex);
                
            }
        }
        

    }//GEN-LAST:event_btnSaveActionPerformed

    private void txtNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtAddress.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtNameKeyPressed

    private void txtAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            txtTelephoneNumber.requestFocusInWindow();
            
            if (txtAddress.getSelectedText() != null) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtAddressKeyPressed

    private void txtTelephoneNumberKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelephoneNumberKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtFaxNumber.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtTelephoneNumberKeyPressed

    private void txtEMailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEMailKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCoordinatorName.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtEMailKeyPressed

    private void txtCoordinatorNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCoordinatorNameKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCoordinatorPhoneNumber.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtCoordinatorNameKeyPressed

    private void txtAgentNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAgentNameKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtAgentNumber.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtAgentNameKeyPressed

    private void txtAgentNumberKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAgentNumberKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtDate.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtAgentNumberKeyPressed

    private void txtDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDateFocusGained
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtDate.getEditor().selectAll();
            }
        });
    }//GEN-LAST:event_txtDateFocusGained

    private void txtAddressFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAddressFocusGained
        txtAddress.selectAll();
    }//GEN-LAST:event_txtAddressFocusGained

    private void txtDateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDateKeyPressed

    }//GEN-LAST:event_txtDateKeyPressed

    private void txtEMailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEMailFocusLost

    }//GEN-LAST:event_txtEMailFocusLost

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
    }//GEN-LAST:event_txtNameActionPerformed

    private void txtNameCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtNameCaretUpdate
        enableSave();
    }//GEN-LAST:event_txtNameCaretUpdate

    private void txtAddressCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtAddressCaretUpdate
        enableSave();
    }//GEN-LAST:event_txtAddressCaretUpdate

    private void btnClearAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearAllActionPerformed
        txtName.setText("");
        txtAddress.setText("");
        txtTelephoneNumber.setText("");
        txtEMail.setText("");
        txtCoordinatorName.setText("");
        txtAgentName.setText("");
        txtAgentNumber.setText("");
        txtFaxNumber.setText("");
        txtCoordinatorPhoneNumber.setText("");
        
        txtDate.setDate(null);
        
        txtName.requestFocusInWindow();
    }//GEN-LAST:event_btnClearAllActionPerformed

    private void txtFaxNumberKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFaxNumberKeyTyped
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtEMail.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtFaxNumberKeyTyped

    private void txtCoordinatorPhoneNumberKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCoordinatorPhoneNumberKeyTyped
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtAgentName.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtCoordinatorPhoneNumberKeyTyped

    private void txtNamePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtNamePropertyChange

    }//GEN-LAST:event_txtNamePropertyChange

    private void txtDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateActionPerformed
        changed = true;
    }//GEN-LAST:event_txtDateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClearAll;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlMainContainer;
    private javax.swing.JTextArea txtAddress;
    private javax.swing.JTextField txtAgentName;
    private javax.swing.JTextField txtAgentNumber;
    private javax.swing.JTextField txtCoordinatorName;
    private javax.swing.JTextField txtCoordinatorPhoneNumber;
    private org.jdesktop.swingx.JXDatePicker txtDate;
    private javax.swing.JTextField txtEMail;
    private javax.swing.JTextField txtFaxNumber;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtTelephoneNumber;
    // End of variables declaration//GEN-END:variables
}
