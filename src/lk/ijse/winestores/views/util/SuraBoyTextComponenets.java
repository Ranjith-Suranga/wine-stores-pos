/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Ranjith Suranga
 */
public class SuraBoyTextComponenets {

    public SuraBoyTextComponenets(Container container) {
        findTextComponenets(container);
    }

    private void findTextComponenets(Container container) {

        for (Component cmp : container.getComponents()) {

            if (cmp instanceof JTextComponent) {
                ((JTextComponent) cmp).putClientProperty("Border", ((JTextComponent) cmp).getBorder());;
                
                cmp.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        if (!cmp.hasFocus()) {
                            JTextComponent txt = (JTextComponent) cmp;
                            txt.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0x489EE7)), new EmptyBorder(2, 2, 2, 2)));
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);
                        if (!cmp.hasFocus()) {
                            JTextComponent txt = (JTextComponent) cmp;
                            txt.setBorder((Border) txt.getClientProperty("Border"));
                        }
                    }

                });

                cmp.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        super.focusGained(e);
                        JTextComponent txt = (JTextComponent) cmp;
                        txt.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(253,163,17)), new EmptyBorder(2, 2, 2, 2)));
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        super.focusLost(e);
                        JTextComponent txt = (JTextComponent) cmp;
                        txt.setBorder((Border) txt.getClientProperty("Border"));
                    }
                });
            }

            if (cmp instanceof JPanel || cmp instanceof JScrollPane) {
                findTextComponenets((Container) cmp);
            }
        }

    }

}
