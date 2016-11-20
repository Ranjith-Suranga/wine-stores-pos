/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import com.sun.java.swing.plaf.windows.WindowsButtonUI;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 *
 * @author Ranjith Suranga
 */
public class SuraButton {

    private Container container;

    public SuraButton(Container container) {
        this.container = container;
    }

    public void convertAllJButtonsToSuraButtons() {
        convertAllJButtonsToSuraButtons(container);
    }

    private void convertAllJButtonsToSuraButtons(Container container) {

        for (Component com : container.getComponents()) {

            if (com instanceof JButton) {

                // Sura's Button's Default States
                JButton btn = (JButton) com;
                btn.setBorder(null);
                btn.setBorderPainted(false);
                btn.setContentAreaFilled(false);
                btn.setOpaque(true);
                btn.repaint();

                if (!btn.isEnabled()) {
                    btn.putClientProperty("Color", btn.getBackground());
                    btn.setBackground(Color.gray);
                }

                btn.addPropertyChangeListener(new PropertyChangeListener() {

                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {

//                        System.out.println(evt.getPropertyName());
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        if (evt.getPropertyName().equalsIgnoreCase("enabled")) {

                            JButton btn = (JButton) evt.getSource();
                            boolean enable = (boolean) evt.getNewValue();

                            if (enable) {
                                btn.setBackground((Color) btn.getClientProperty("Color"));
                                btn.putClientProperty("Color", null);
                            } else {
                                btn.putClientProperty("Color", btn.getBackground());
                                btn.setBackground(Color.gray);
                            }

                        }
                    }

                });

                // Change button's color when the mouse over and exit
                btn.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        int r, g, b;

                        super.mouseEntered(e);

                        if (!btn.isEnabled()) {
                            return;
                        }

                        Color currentColor = btn.getBackground();
                        if (btn.getClientProperty("data-background") == null) {
                            btn.putClientProperty("data-background", currentColor);
                        } else {
                            currentColor = (Color) btn.getClientProperty("data-background");
                        }

                        r = currentColor.getRed() - 10;
                        g = currentColor.getGreen() - 10;
                        b = currentColor.getBlue() - 10;

                        r = (r > 0) ? r : r + 10;
                        g = (g > 0) ? g : g + 10;
                        b = (b > 0) ? b : b + 10;

                        btn.setBackground(new Color(r, g, b));

                        btn.putClientProperty("data-entered", true);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        int r, g, b;

                        super.mouseExited(e);

                        if (!btn.isEnabled()) {
                            return;
                        }

                        if (btn.getClientProperty("data-background") == null) {
                            Color currentColor = btn.getBackground();
                            r = currentColor.getRed() + 10;
                            g = currentColor.getGreen() + 10;
                            b = currentColor.getBlue() + 10;

                            r = (r < 255) ? r : r - 10;
                            g = (g < 255) ? g : g - 10;
                            b = (b < 255) ? b : b - 10;

                            btn.setBackground(new Color(r, g, b));
                        } else {
                            Color currentColor = (Color) btn.getClientProperty("data-background");
                            btn.setBackground(currentColor);
                        }

                        btn.putClientProperty("data-entered", false);

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        super.mouseReleased(e);
                    }

                });

                // Change the look & feel of the button
                btn.setUI(new WindowsButtonUI() {

                    @Override
                    public void paint(Graphics g, JComponent c) {
                        super.paint(g, c);

                        if (!c.isEnabled()) {
//                            btn.putClientProperty("Color", btn.getBackground());
                            btn.setBackground(Color.gray);
                        }

                        if (c.getClientProperty("data-entered") != null) {
                            if (c.getClientProperty("data-entered") == Boolean.TRUE) {

                                Rectangle rect = c.getBounds().getBounds();
                                rect.setLocation(c.getLocationOnScreen());

                                if (!rect.contains(MouseInfo.getPointerInfo().getLocation())) {
                                    Color currentColor = (Color) btn.getClientProperty("data-background");
                                    btn.setBackground(currentColor);
                                    btn.putClientProperty("data-entered", false);
                                }
                            }
                        }
//                        c.setBackground(?);
                        //Object clientProperty = c.getClientProperty("Color");
//                        if (!btn.isEnabled()) {
////                        if (clientProperty == null) {
////                            c.putClientProperty("Color", c.getBackground());
////                        }                            
//                            btn.setBackground(Color.GRAY);
//                        } else {
//                            if (clientProperty != null){
//                                
//                            }
//                            //btn.setBackground((Color) c.getClientProperty("Color"));
//                        }
                    }

                    @Override
                    protected void paintButtonPressed(Graphics g, AbstractButton b) {
                        super.paintButtonPressed(g, b);

                        Graphics2D gd = (Graphics2D) g;
                        gd.setPaint(new GradientPaint(0, 0, btn.getBackground().darker(), 0, btn.getHeight(), btn.getBackground().brighter()));
                        gd.fill(gd.getClip());
                    }

                    @Override
                    protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {

                        //super.paintFocus(g, b, viewRect, textRect, iconRect);
                        Graphics2D gd = (Graphics2D) g;
                        gd.setColor(new Color(77, 144, 254));
                        gd.setStroke(new BasicStroke(1));
                        gd.drawRect(0, 0, gd.getClipBounds().width - 1, gd.getClipBounds().height - 1);
                    }

                });
            }

            if (com instanceof Container) {  // To get buttons inside another continaer in this container
                convertAllJButtonsToSuraButtons((Container) com);
            }

        }
    }

}
