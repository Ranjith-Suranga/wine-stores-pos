/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import lk.ijse.winestores.controller.ControllerFactory;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.controller.custom.QueryController;
import lk.ijse.winestores.dao.dto.ChequeDetailsDTO;
import lk.ijse.winestores.dao.dto.CustomOrderDTO;
import lk.ijse.winestores.dao.dto.OrderEmptyBottleDetailsDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.service.custom.impl.SalesServiceImpl;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Ranjith Suranga
 */
public class SearchCashOrderContainer extends javax.swing.JPanel {

    private int orderId;

    private SuraTable stbl;
//    private SuraButton sbtn;

    private DefaultTableModel dtm;

    private QueryController ctrlQuery;

    private CustomOrderDTO order;

    /**
     * Creates new form SearchCashOrderContainer
     */
    public SearchCashOrderContainer(int orderId) {

        initComponents();

        stbl = new SuraTable(tblEmptyBottles);
//        sbtn = new SuraButton(this);
//        sbtn.convertAllJButtonsToSuraButtons();

        for (int i = 0; i < tblEmptyBottles.getColumnCount(); i++) {

            stbl.setHeaderAlignment(i, SwingConstants.CENTER);
            stbl.setColumnAlignment(i, SwingConstants.CENTER);

        }

        dtm = (DefaultTableModel) tblEmptyBottles.getModel();

        ctrlQuery = (QueryController) ControllerFactory.getInstance().getController(SuperController.ControllerType.QUERY);

        this.orderId = orderId;
        loadJasperOrder();
        loadEmptyBottleDetails();
        calculateTotals();
        loadChequeDetails();

    }

    private void loadJasperOrder() {

        try {

            JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/lk/ijse/winestores/reports/CashSaleBill.jasper"));

            HashMap<String, Object> parms = new HashMap<>();
            parms.put("orderId", this.orderId);

            try {

                Connection connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();

                JasperPrint filledReport = JasperFillManager.fillReport(compiledReport, parms, connection);

                JRViewer pnlReport = new JRViewer(filledReport);

                pnlOrder.add(pnlReport);
                pnlOrder.updateUI();

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SearchCashOrderContainer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(SearchCashOrderContainer.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (JRException ex) {
            Logger.getLogger(SalesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadEmptyBottleDetails() {

        dtm.setRowCount(0);

        try {

            ArrayList<OrderEmptyBottleDetailsDTO> emptyBottleDetails = ctrlQuery.getEmptyBottleDetails(this.orderId);

            if (emptyBottleDetails != null) {

                for (OrderEmptyBottleDetailsDTO emptyBottleDetail : emptyBottleDetails) {

                    Object[] rowData = {emptyBottleDetail.getBottleType(),
                        formatPrice(
                        ctrlQuery.getEmptyBottle(emptyBottleDetail.getBottleType()).getCost()
                        ),
                        String.valueOf(emptyBottleDetail.getQty()),
                        formatPrice(emptyBottleDetail.getTotal())
                    };
                    dtm.addRow(rowData);

                }

            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SearchCashOrderContainer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SearchCashOrderContainer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String formatPrice(Double price) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(price);
    }

    private void calculateTotals() {

        BigDecimal emptyBottleCost = BigDecimal.ZERO;

        // (Empty Bottle Cost)
        if (dtm.getRowCount() > 0) {

            for (int i = 0; i < dtm.getRowCount(); i++) {
                emptyBottleCost = emptyBottleCost.add(new BigDecimal(dtm.getValueAt(i, 3).toString()));
            }
            lblBottleCost.setText(emptyBottleCost.setScale(2).toPlainString());

        } else {
            lblBottleCost.setText("-");
        }

        try {

            order = ctrlQuery.getCashOrder(this.orderId);

            BigDecimal billTotal = BigDecimal.valueOf(order.getTotal());
            lblBillTotal.setText(billTotal.setScale(2).toPlainString());

            BigDecimal total = billTotal.add(emptyBottleCost);
            lblFinalTotal.setText(total.setScale(2).toPlainString());

            if (order.getTenderedCash().compareTo(BigDecimal.ZERO) != 0) {

                lblTenderedAmount.setText(order.getTenderedCash().setScale(2).toPlainString());

                BigDecimal balance = order.getTenderedCash().subtract(total).setScale(2, RoundingMode.HALF_UP);
                lblBalance.setText(balance.toPlainString());
                
            } else {
                lblTenderedAmount.setText("-");
                lblBalance.setText("-");
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SearchCashOrderContainer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SearchCashOrderContainer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadChequeDetails() {

        if (order.getPaymentId().equals("02")) {        // Cheque

            try {

                ChequeDetailsDTO chequeDetails = ctrlQuery.getChequeDetails(orderId);
                lblChequeNumber.setText(chequeDetails.getChequeNumber());
                lblBank.setText(chequeDetails.getBank());
                lblBranch.setText(chequeDetails.getBranch());

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SearchCashOrderContainer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(SearchCashOrderContainer.class.getName()).log(Level.SEVERE, null, ex);
            }

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

        jPanel1 = new javax.swing.JPanel();
        pnlOrder = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane(){
            {
                this.getViewport().setBackground(Color.WHITE);
            }
        };
        tblEmptyBottles = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblChequeNumber = new javax.swing.JLabel();
        lblBank = new javax.swing.JLabel();
        lblBranch = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblTenderedAmount = new javax.swing.JLabel();
        lblFinalTotal = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblBillTotal = new javax.swing.JLabel();
        lblBalance = new javax.swing.JLabel();
        lblBottleCost = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        pnlOrder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        pnlOrder.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(pnlOrder, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(pnlOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel1.setText("Empty Bottle Details");

        jSeparator1.setForeground(new java.awt.Color(224, 219, 221));

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234)));

        jScrollPane1.setBorder(null);

        tblEmptyBottles.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        tblEmptyBottles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Bottle Type", "Bottle Price", "Qty", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmptyBottles.setGridColor(new java.awt.Color(204, 204, 204));
        tblEmptyBottles.setRowHeight(50);
        tblEmptyBottles.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblEmptyBottles.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblEmptyBottles.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblEmptyBottles.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblEmptyBottles);
        if (tblEmptyBottles.getColumnModel().getColumnCount() > 0) {
            tblEmptyBottles.getColumnModel().getColumn(2).setMinWidth(70);
            tblEmptyBottles.getColumnModel().getColumn(2).setPreferredWidth(70);
            tblEmptyBottles.getColumnModel().getColumn(2).setMaxWidth(70);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jLabel5.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel5.setText(" Cheque Details");

        jSeparator2.setForeground(new java.awt.Color(224, 219, 221));

        jLabel6.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Cheque Number :");

        jLabel7.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Bank :");

        jLabel8.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Branch :");

        lblChequeNumber.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblChequeNumber.setText("-");

        lblBank.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblBank.setText("-");

        lblBranch.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblBranch.setText("-");

        jButton2.setBackground(new java.awt.Color(0, 204, 0));
        jButton2.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jButton2.setMnemonic('d');
        jButton2.setText("Edit");
        jButton2.setToolTipText("Click to edit the order");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Open Sans", 1, 18)); // NOI18N
        jLabel10.setText("Balance :");

        jLabel3.setFont(new java.awt.Font("Open Sans", 1, 18)); // NOI18N
        jLabel3.setText("Bill Total :");

        lblTenderedAmount.setFont(new java.awt.Font("Open Sans", 1, 18)); // NOI18N
        lblTenderedAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTenderedAmount.setText("-");

        lblFinalTotal.setFont(new java.awt.Font("Open Sans", 1, 24)); // NOI18N
        lblFinalTotal.setForeground(new java.awt.Color(0, 153, 0));
        lblFinalTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFinalTotal.setText("-");
        lblFinalTotal.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 153, 0)),
                javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 153, 0)))
        );

        jLabel4.setFont(new java.awt.Font("Open Sans", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 153, 0));
        jLabel4.setText("Final Total :");

        lblBillTotal.setFont(new java.awt.Font("Open Sans", 1, 18)); // NOI18N
        lblBillTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBillTotal.setText("-");

        lblBalance.setFont(new java.awt.Font("Open Sans", 1, 18)); // NOI18N
        lblBalance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBalance.setText("-");

        lblBottleCost.setFont(new java.awt.Font("Open Sans", 1, 18)); // NOI18N
        lblBottleCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBottleCost.setText("-");

        jLabel9.setFont(new java.awt.Font("Open Sans", 1, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Tendered Amount :");

        jLabel2.setFont(new java.awt.Font("Open Sans", 1, 18)); // NOI18N
        jLabel2.setText("Bottle Cost :");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBottleCost, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblBillTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblFinalTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                    .addComponent(lblTenderedAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblBalance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblBottleCost))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblBillTotal))
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblFinalTotal))
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(lblTenderedAmount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lblBalance))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblBank))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblChequeNumber))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblBranch))))
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel6, jLabel7, jLabel8});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblChequeNumber))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblBank))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lblBranch))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblBalance;
    private javax.swing.JLabel lblBank;
    private javax.swing.JLabel lblBillTotal;
    private javax.swing.JLabel lblBottleCost;
    private javax.swing.JLabel lblBranch;
    private javax.swing.JLabel lblChequeNumber;
    private javax.swing.JLabel lblFinalTotal;
    private javax.swing.JLabel lblTenderedAmount;
    private javax.swing.JPanel pnlOrder;
    private javax.swing.JTable tblEmptyBottles;
    // End of variables declaration//GEN-END:variables
}
