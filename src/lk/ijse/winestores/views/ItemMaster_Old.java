/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 *
 * @author Ranjith Suranga
 */
public class ItemMaster_Old extends javax.swing.JPanel {

    private JPanel selectedPanel;

    /**
     * Creates new form ItemMaster
     */
    public ItemMaster_Old() {
        initComponents();
        setSelectedPanel(pnlMajorCatogeries);
    }

    private void setSelectedPanel(JPanel pnl) {

        if (pnl == selectedPanel) {
            return;
        }

        // Let's make sure all the panels are in thier default state
        initMainPanels();

        // Now, let's style the selected panel
        pnl.setBackground(new Color(226, 236, 245));
        Border b = new MatteBorder(0, 0, 3, 0, new Color(72, 158, 231));
        pnl.setBorder(b);
        JLabel lbl = (JLabel) pnl.getComponent(0);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));

        selectedPanel = pnl;

        pnlContainer.removeAll();

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        pnl.setCursor(this.getCursor());
        if (selectedPanel == pnlMajorCatogeries) {
            MajorCategory mc = new MajorCategory();
            pnlContainer.add(mc);
        } else if (selectedPanel == pnlSubCatogeries) {
            SubCategory sc = new SubCategory();
            pnlContainer.add(sc);
        } else {
            ItemCategory ic = new ItemCategory();
            pnlContainer.add(ic);
        }

        pnlContainer.repaint();
        pnl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setCursor(Cursor.getDefaultCursor());

    }

    private void initMainPanels() {

        for (Component component : pnlHeader.getComponents()) {

            if (!(component instanceof JPanel)) {
                continue;
            }

            final JPanel pnl = (JPanel) component;
            pnl.setBackground(Color.white);
            pnl.getComponent(0).setForeground(Color.BLACK);
            pnl.setBorder(null);
            JLabel lbl = (JLabel) pnl.getComponent(0);
            lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN));

            if (pnl.getMouseListeners().length >= 2) {
                continue; // To avoid adding listeners every time this method invoke
            }

            pnl.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    setMouseEnter((JPanel) e.getComponent());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    setMouseLeave((JPanel) e.getComponent());
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    setSelectedPanel(pnl);
                }

            });

        }
    }

    private void setMouseEnter(JPanel pnl) {
        pnl.setBackground(new Color(226, 236, 245));
        Border b = new MatteBorder(0, 0, 3, 0, new Color(72, 158, 231));
        pnl.setBorder(b);
        JLabel lbl = (JLabel) pnl.getComponent(0);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
    }

    private void setMouseLeave(JPanel pnl) {

        if (pnl == selectedPanel) {
            return;   // Because we don't want to remove the style of selected panel
        }
        pnl.setBackground(Color.white);
        pnl.getComponent(0).setForeground(Color.BLACK);
        pnl.setBorder(null);
        JLabel lbl = (JLabel) pnl.getComponent(0);
        lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN));
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
        pnlMajorCatogeries = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        pnlSubCatogeries = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        pnlItems = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        pnlContainer = new javax.swing.JPanel();

        setBackground(new java.awt.Color(233, 236, 242));

        pnlHeader.setBackground(new java.awt.Color(55, 57, 66));
        pnlHeader.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(220, 225, 234)));

        pnlMajorCatogeries.setBackground(new java.awt.Color(255, 255, 255));
        pnlMajorCatogeries.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMajorCatogeries.setPreferredSize(new java.awt.Dimension(232, 90));

        jLabel9.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel9.setText("Major Category");

        jLabel10.setBackground(new java.awt.Color(251, 93, 93));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/major_catogeries.png"))); // NOI18N
        jLabel10.setOpaque(true);

        javax.swing.GroupLayout pnlMajorCatogeriesLayout = new javax.swing.GroupLayout(pnlMajorCatogeries);
        pnlMajorCatogeries.setLayout(pnlMajorCatogeriesLayout);
        pnlMajorCatogeriesLayout.setHorizontalGroup(
            pnlMajorCatogeriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMajorCatogeriesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlMajorCatogeriesLayout.setVerticalGroup(
            pnlMajorCatogeriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMajorCatogeriesLayout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(35, 35, 35))
            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlHeader.add(pnlMajorCatogeries);

        pnlSubCatogeries.setBackground(new java.awt.Color(255, 255, 255));
        pnlSubCatogeries.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlSubCatogeries.setPreferredSize(new java.awt.Dimension(232, 90));

        jLabel3.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel3.setText("Sub Category");

        jLabel4.setBackground(new java.awt.Color(111, 214, 75));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/sub_catogeries.png"))); // NOI18N
        jLabel4.setOpaque(true);

        javax.swing.GroupLayout pnlSubCatogeriesLayout = new javax.swing.GroupLayout(pnlSubCatogeries);
        pnlSubCatogeries.setLayout(pnlSubCatogeriesLayout);
        pnlSubCatogeriesLayout.setHorizontalGroup(
            pnlSubCatogeriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSubCatogeriesLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlSubCatogeriesLayout.setVerticalGroup(
            pnlSubCatogeriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSubCatogeriesLayout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(35, 35, 35))
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlHeader.add(pnlSubCatogeries);

        pnlItems.setBackground(new java.awt.Color(255, 255, 255));
        pnlItems.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlItems.setPreferredSize(new java.awt.Dimension(232, 90));

        jLabel5.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel5.setText("Items");

        jLabel6.setBackground(new java.awt.Color(247, 148, 29));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/items_catogeries.png"))); // NOI18N
        jLabel6.setOpaque(true);

        javax.swing.GroupLayout pnlItemsLayout = new javax.swing.GroupLayout(pnlItems);
        pnlItems.setLayout(pnlItemsLayout);
        pnlItemsLayout.setHorizontalGroup(
            pnlItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlItemsLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlItemsLayout.setVerticalGroup(
            pnlItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlItemsLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel5)
                .addContainerGap(35, Short.MAX_VALUE))
            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlHeader.add(pnlItems);

        pnlContainer.setBackground(new java.awt.Color(232, 235, 241));
        pnlContainer.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, 879, Short.MAX_VALUE)
            .addComponent(pnlContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(pnlContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel pnlContainer;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlItems;
    private javax.swing.JPanel pnlMajorCatogeries;
    private javax.swing.JPanel pnlSubCatogeries;
    // End of variables declaration//GEN-END:variables
}
