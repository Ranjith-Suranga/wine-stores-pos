/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Ranjith Suranga
 */
public class SuraTable {

    private JTable tbl;
    private DefaultTableModel dtm;

    private SuraTableCellRender cellRenderForBoolean;
    private ArrayList<SuraTableCellRender> cellRenderForObjects;

    public SuraTable(JTable tbl) {
        this.tbl = tbl;
        dtm = (DefaultTableModel) tbl.getModel();
        initBasics();
        initTableHeader();
        initTable();
    }

    private void initBasics() {

        // Removing border around the table
        Container c = tbl.getParent().getParent();
        if (c instanceof JScrollPane) {
            JScrollPane jsp = (JScrollPane) c;
            jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
        }

    }

    public void initTableHeader() {

        // Adjusting Table Header Height
        JTableHeader header = tbl.getTableHeader();
        Dimension d = header.getPreferredSize();
        d.setSize(d.getWidth(), tbl.getRowHeight());
        header.setPreferredSize(d);

        // Adding Style
        header.setFont(tbl.getFont().deriveFont(Font.BOLD));
        header.setBorder(new MatteBorder(0, 0, 1, 0, new Color(233, 233, 233)));

        // Rendering Customized Header
        for (int i = 0; i < tbl.getColumnCount(); i++) {

            // Adding check box to header ?
            if (dtm.getColumnClass(i) == Boolean.class && dtm.getColumnName(i).length() == 0) {

                DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {

                        JCheckBox jcb = new JCheckBox();
                        jcb.setBackground(new Color(72, 158, 231));
                        jcb.setOpaque(true);
                        jcb.setBorderPainted(true);
                        jcb.setBorder(new MatteBorder(0, 0, 0, 1, new Color(233, 233, 233)));
                        jcb.setHorizontalAlignment(SwingConstants.CENTER);
                        return jcb;

                    }

                };

                header.getColumnModel().getColumn(i).setHeaderRenderer(dtcr);

            } else {

                DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {

                        JLabel lbl = new JLabel();
                        lbl.setBackground(new Color(72, 158, 231));
                        lbl.setOpaque(true);
                        CompoundBorder cb = new CompoundBorder(new MatteBorder(0, 0, 0, 1, new Color(233, 233, 233)), new EmptyBorder(0, 4, 0, 0));
                        lbl.setBorder(cb);
                        lbl.setFont(jtable.getFont().deriveFont(Font.BOLD));
                        lbl.setText(o.toString());
                        lbl.setForeground(Color.white);
                        return lbl;

                    }

                };

                header.getColumnModel().getColumn(i).setHeaderRenderer(dtcr);

            }

        }

        // Adding Mouse Listeners To Header
        header.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                int columnIndex = header.columnAtPoint(e.getPoint());
                if (columnIndex == -1) {
                    return;
                }
                
                // Checkbox ?
                if (dtm.getColumnClass(columnIndex) == Boolean.class && dtm.getColumnName(columnIndex).length() == 0) {

                    JCheckBox chk = (JCheckBox) header.getColumnModel().getColumn(columnIndex).getHeaderRenderer().getTableCellRendererComponent(tbl, " ", true, true, 0, columnIndex);
                    
                    // Either we check all the combox box here or not at all
                    for (int i = 0; i < dtm.getRowCount(); i++) {
                        dtm.setValueAt(!chk.isSelected(), i, columnIndex);

                    }

                    DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer() {

                        @Override
                        public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {

                            JCheckBox jcb = new JCheckBox();
                            jcb.setBackground(new Color(72, 158, 231));
                            jcb.setOpaque(true);
                            jcb.setSelected(!chk.isSelected());
                            jcb.setBorderPainted(true);
                            jcb.setBorder(new MatteBorder(0, 0, 0, 1, new Color(233, 233, 233)));
                            jcb.setHorizontalAlignment(SwingConstants.CENTER);

                            return jcb;

                        }

                    };

                    header.getColumnModel().getColumn(columnIndex).setHeaderRenderer(dtcr);
                    header.repaint();

                }
            }

        });

    }

    public void setHeaderAlignment(int columnIndex, int alignment) {

        // Checking whether we have to align a column with check box, if that is the case, we don't support it yet.
        if (dtm.getColumnClass(columnIndex) == Boolean.class && dtm.getColumnName(columnIndex).length() == 0) {
            return;
        }

        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {

                Border border;

                JLabel lbl = new JLabel();
                lbl.setBackground(new Color(72, 158, 231));
                lbl.setOpaque(true);
                lbl.setHorizontalAlignment(alignment);

                if (alignment != SwingConstants.CENTER) {
                    border = new CompoundBorder(new MatteBorder(0, 0, 0, 1, new Color(233, 233, 233)), new EmptyBorder(0, 4, 0, 4));
                } else {
                    border = new MatteBorder(0, 0, 0, 1, new Color(233, 233, 233));
                }
                lbl.setBorder(border);
                lbl.setFont(jtable.getFont().deriveFont(Font.BOLD));
                lbl.setText(o.toString());
                lbl.setForeground(Color.white);
                return lbl;

            }

        };

        tbl.getColumnModel().getColumn(columnIndex).setHeaderRenderer(dtcr);
    }

    public void initTable() {

        cellRenderForBoolean = new SuraTableCellRender(SuraTableCellRender.ColumnTypes.BOOLEAN, Color.WHITE, Color.BLACK, SwingConstants.CENTER);
        tbl.setDefaultRenderer(Boolean.class, cellRenderForBoolean);
        cellRenderForObjects = new ArrayList<>();
        for (int i = 0; i < tbl.getColumnCount(); i++) {
            if (tbl.getColumnClass(i) != Boolean.class) {
                SuraTableCellRender cellRender = new SuraTableCellRender(SuraTableCellRender.ColumnTypes.OBJECT, Color.WHITE, Color.BLACK, SwingConstants.LEFT);
                cellRenderForObjects.add(cellRender);
                tbl.getColumnModel().getColumn(i).setCellRenderer(cellRender);
            }
        }

        tbl.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {

                int row = tbl.rowAtPoint(e.getPoint());

                if (row == -1) {
                    return;
                }
                setMouseHoverForCellRenderObjects(true);
                setMouseHoverRowForCellRenderObjects(row, new Color(230, 230, 230), Color.black);
                //cellRenderForObject.setMouseHoverRowSettings(row, new Color(230, 230, 230), Color.darkGray);
                cellRenderForBoolean.setMouseHoverRow(true);
                cellRenderForBoolean.setMouseHoverRowSettings(row, new Color(230, 230, 230), Color.black);
            }

        });

        tbl.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseExited(MouseEvent e) {
                setMouseHoverForCellRenderObjects(false);
                cellRenderForBoolean.setMouseHoverRow(false);
            }

        });
        
        tbl.addPropertyChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("model")){
                    dtm = (DefaultTableModel) tbl.getModel();
                    tbl.repaint();
                }
            }
            
        });

    }

    public void setColumnAlignment(int colIndex, int alignment) {
        cellRenderForObjects.get(colIndex).setAlignment(alignment);
        tbl.repaint();
    }

    private void setMouseHoverForCellRenderObjects(boolean enable) {
        for (SuraTableCellRender cellRenderForObject : cellRenderForObjects) {
            cellRenderForObject.setMouseHoverRow(enable);
        }
    }

    private void setMouseHoverRowForCellRenderObjects(int rowIndex, Color hoverBackgroundColor, Color hoverForegroundColor) {
        for (SuraTableCellRender cellRenderForObject : cellRenderForObjects) {
            cellRenderForObject.setMouseHoverRowSettings(rowIndex, hoverBackgroundColor, hoverForegroundColor);
        }
    }

    private static class SuraTableCellRender extends DefaultTableCellRenderer {

        public enum ColumnTypes {
            BOOLEAN, OBJECT
        }

        private ColumnTypes colType;
        private Color backgroundColor;
        private Color foregroundColor;
        private int alignment;
        private boolean mouseHoverRow;
        private Color hoverBackgroundColor;
        private Color hoverForegroundColor;
        private int rowIndex;

        /**
         * @return the alignment
         */
        public int getAlignment() {
            return alignment;
        }

        /**
         * @param alignment the alignment to set
         */
        public void setAlignment(int alignment) {
            this.alignment = alignment;
        }

        public SuraTableCellRender(ColumnTypes columnType, Color backgroundColor, Color foregroundColor, int alignment) {
            colType = columnType;
            this.backgroundColor = backgroundColor;
            this.foregroundColor = foregroundColor;
            this.alignment = alignment;
        }

        public void setMouseHoverRow(boolean enable) {
            mouseHoverRow = enable;
        }

        public void setMouseHoverRowSettings(int rowIndex, Color backgroundColor, Color foregroundColor) {
            this.rowIndex = rowIndex;
            hoverBackgroundColor = backgroundColor;
            hoverForegroundColor = foregroundColor;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        
            if (value instanceof Boolean) {

                JCheckBox chk = new JCheckBox();

                chk.setFont(table.getFont());
                
                if (!isSelected) {
                    chk.setBackground((mouseHoverRow == true && rowIndex == row) ? hoverBackgroundColor : backgroundColor);
                    chk.setForeground((mouseHoverRow == true && rowIndex == row) ? hoverForegroundColor : foregroundColor);
                } else {
                    chk.setBackground(table.getSelectionBackground());
                    chk.setForeground(table.getSelectionForeground());
                }
                
                chk.setOpaque(true);
                chk.setSelected((boolean) value);
                chk.setBorderPainted(false);
                chk.setHorizontalAlignment(SwingConstants.CENTER);

                return chk;
            }

            JLabel lbl = new JLabel((String) value);

            lbl.setFont(table.getFont());

            if (!isSelected) {
                lbl.setBackground((mouseHoverRow == true && rowIndex == row) ? hoverBackgroundColor : backgroundColor);
                lbl.setForeground((mouseHoverRow == true && rowIndex == row) ? hoverForegroundColor : foregroundColor);
            } else {
                lbl.setBackground(table.getSelectionBackground());
                lbl.setForeground(table.getSelectionForeground());
            }
            lbl.setOpaque(true);
            if (getAlignment() == SwingConstants.CENTER) {
                lbl.setHorizontalAlignment(getAlignment());
            } else {
                lbl.setBorder(new EmptyBorder(0,4,0,4));
                lbl.setHorizontalAlignment(getAlignment());
            }
            table.setIntercellSpacing(new Dimension(0, 1));
            return lbl;

        }
    }

}
