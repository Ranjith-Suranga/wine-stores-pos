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
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import lk.ijse.winestores.views.util.Extension;
import lk.ijse.winestores.views.util.FocusHandler;

/**
 *
 * @author Ranjith Suranga
 */
public class Main extends javax.swing.JFrame {

    // Menus
    private DashBoard pnlDashBoard;
    private CashSales pnlCashSales;
    private CreditSales pnlCreditIssue;
    private Items pnlItemSearch;
    private ItemMaster pnlItemMaster;
    private Suppliers pnlSupplierMaster;
    private SupplierOrders pnlSupplierOrders;
    private GRNMaster pnlGRNMaster;
    private Views pnlViews;
    private SystemSettings pnlSystemSettings;
    private Customers pnlCustomers;

    private Extension extension;

    private MenuItems SelectedMenuItem;

    //private static final Color SELECTED_MENU_FORGROUNDECOLOR = Color.WHITE;
    private static final Color SELECTED_MENU_FORGROUNDECOLOR = new Color(72, 158, 231);
    private static final Color SELECTED_MENU_BACKGROUNDCOLOR = new Color(32, 33, 38);
    //private static final Color MENU_FOREGROUNDCOLOR = new Color(150, 154, 167);
    private static final Color MENU_FOREGROUNDCOLOR = Color.WHITE;
    private static final Color THEME_COLOR1 = new Color(72, 158, 231);

    private static final String PATH_NAME = "/lk/ijse/winestores/icons/";

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        initMe();
    }

    private void initMe() {

        lblfslash1.setVisible(false);
        lblMenuItem.setVisible(false);
        lblfslash2.setVisible(false);
        lblExtension.setVisible(false);

        setExtendedState(MAXIMIZED_BOTH);
//        initMenuItems();  Because it is also called inside setSelectedMenuItem() method
        initDateTime();
        setSeletctedMenuItem(MenuItems.DASHBOARD);
        setFunctionKeyListener();
    }

    private void setFunctionKeyListener() {
        KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();

        kfm.addKeyEventPostProcessor(new KeyEventPostProcessor() {

            @Override
            public boolean postProcessKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_F1:
                            setSeletctedMenuItem(MenuItems.DASHBOARD);
                            break;
                        case KeyEvent.VK_F2:
                            setSeletctedMenuItem(MenuItems.CASH_SALES);
                            break;
                        case KeyEvent.VK_F3:
                            setSeletctedMenuItem(MenuItems.CREDIT_SALE);
                            break;
                        case KeyEvent.VK_F4:
                            setSeletctedMenuItem(MenuItems.ITEMS);
                            break;
                        case KeyEvent.VK_F5:
                            setSeletctedMenuItem(MenuItems.ITEM_MASTER);
                            break;
                        case KeyEvent.VK_F6:
                            setSeletctedMenuItem(MenuItems.SUPPLIER_MASTER);
                            break;
                        case KeyEvent.VK_F7:
                            setSeletctedMenuItem(MenuItems.SUPPLIER_ORDER);
                            break;
                        case KeyEvent.VK_F8:
                            setSeletctedMenuItem(MenuItems.GRN);
                            break;
                        case KeyEvent.VK_F9:
                            setSeletctedMenuItem(MenuItems.VIEWS);
                            break;
                        case KeyEvent.VK_F10:
                            setSeletctedMenuItem(MenuItems.CUSTOMERS);
                            break;
                        case KeyEvent.VK_F11:
                            setSeletctedMenuItem(MenuItems.SYSTEM_SETTINGS);
                            break;
                    }
                }
                return false;
            }
        });
    }

    private void initDateTime() {

        ActionListener al = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss    dd-MMM-yyyy");
                lblTime.setText(sdf.format(new Date()));
            }
        };

        new Timer(100, al).start();

    }

    private void initMenuItems() {

        int i = 0, k = 0;  // For looping

        for (Component c : pnlMenu.getComponents()) {

            if (c instanceof JLabel) {

                final int j = i;  // A little hack, because anonymouse inner class can't call local variables if there aren't final or effectively final

                JLabel lbl = (JLabel) c;    // For tempoary usage

                lbl.setBorder(new EmptyBorder(0, 15, 0, 0));
                lbl.setOpaque(false);
                lbl.setForeground(MENU_FOREGROUNDCOLOR);
                lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN));
                lbl.setCursor(java.awt.Cursor.getDefaultCursor());

                try {
                    ImageIcon icon;
                    if (lbl.getText().equals("Minimize") || lbl.getText().equals("Log out") || lbl.getText().equals("Exit System")) {
                        icon = new ImageIcon(getClass().getResource(PATH_NAME + lbl.getText() + ".png"));
                    } else {
                        icon = new ImageIcon(getClass().getResource(PATH_NAME + lbl.getText().split("- ")[1] + ".png"));
                    }
                    lbl.setIcon(icon);
                } catch (ArrayIndexOutOfBoundsException ex) {
                } catch (NullPointerException exception) {
                    exception.printStackTrace();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                k++;

                if (lbl.getMouseListeners().length >= 2) {  // To avoid adding listeners every time this method invoke
                    continue;
                }

                lbl.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);

                        if (e.getComponent() == lblDashboard) {
                            setSeletctedMenuItem(MenuItems.DASHBOARD);
                        } else if (e.getComponent() == lblCashSale) {
                            setSeletctedMenuItem(MenuItems.CASH_SALES);
                        } else if (e.getComponent() == lblCreditSale) {
                            setSeletctedMenuItem(MenuItems.CREDIT_SALE);
                        } else if (e.getComponent() == lblItems) {
                            setSeletctedMenuItem(MenuItems.ITEMS);
                        } else if (e.getComponent() == lblItemMaster) {
                            setSeletctedMenuItem(MenuItems.ITEM_MASTER);
                        } else if (e.getComponent() == lblSuppliers) {
                            setSeletctedMenuItem(MenuItems.SUPPLIER_MASTER);
                        } else if (e.getComponent() == lblSupplierOrders) {
                            setSeletctedMenuItem(MenuItems.SUPPLIER_ORDER);
                        } else if (e.getComponent() == lblGRN) {
                            setSeletctedMenuItem(MenuItems.GRN);
                        } else if (e.getComponent() == lblViews) {
                            setSeletctedMenuItem(MenuItems.VIEWS);
                        } else if (e.getComponent() == lblSystemSettings) {
                            setSeletctedMenuItem(MenuItems.SYSTEM_SETTINGS);
                        } else if (e.getComponent() == lblMinimize) {
                            setSeletctedMenuItem(MenuItems.MINIMIZE);
                        } else if (e.getComponent() == lblLogOut) {
                            setSeletctedMenuItem(MenuItems.LOGOUT);
                        } else if (e.getComponent() == lblCustomers) {
                            setSeletctedMenuItem(MenuItems.CUSTOMERS);
                        } else {
                            setSeletctedMenuItem(MenuItems.EXIT_SYSTEM);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);

                        CompoundBorder cb = BorderFactory.createCompoundBorder(new MatteBorder(0, 4, 0, 0, THEME_COLOR1), new EmptyBorder(0, 15, 0, 0));
                        lbl.setBorder(cb);

                        lbl.setForeground(SELECTED_MENU_FORGROUNDECOLOR);
                        lbl.setBackground(SELECTED_MENU_BACKGROUNDCOLOR);
                        lbl.setOpaque(true);
                        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
                        lbl.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));

                        try {
                            ImageIcon icon;
                            if (lbl.getText().equals("Minimize") || lbl.getText().equals("Log out") || lbl.getText().equals("Exit System")) {
                                icon = new ImageIcon(getClass().getResource(PATH_NAME + lbl.getText() + "_hover.png"));
                            } else {
                                icon = new ImageIcon(getClass().getResource(PATH_NAME + lbl.getText().split("- ")[1] + "_hover.png"));
                            }
                            lbl.setIcon(icon);
                        } catch (NullPointerException | ArrayIndexOutOfBoundsException exception) {
                            exception.printStackTrace();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);

                        if (SelectedMenuItem.getLabelIndex() == (j)) {
                            return;
                        }

                        lbl.setBorder(new EmptyBorder(0, 15, 0, 0));
                        lbl.setOpaque(false);
                        lbl.setForeground(MENU_FOREGROUNDCOLOR);
                        lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN));
                        lbl.setCursor(java.awt.Cursor.getDefaultCursor());

                        try {
                            ImageIcon icon;
                            if (lbl.getText().equals("Minimize") || lbl.getText().equals("Log out") || lbl.getText().equals("Exit System")) {
                                icon = new ImageIcon(getClass().getResource(PATH_NAME + lbl.getText() + ".png"));
                            } else {
                                icon = new ImageIcon(getClass().getResource(PATH_NAME + lbl.getText().split("- ")[1] + ".png"));
                            }
                            lbl.setIcon(icon);
                        } catch (NullPointerException | ArrayIndexOutOfBoundsException exception) {
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }

                });

                i++;
            }
        }
    }

    private void setSeletctedMenuItem(MenuItems menuItem) {

        //System.out.println(menuItem + " " + SelectedMenuItem);
        if (menuItem == SelectedMenuItem) {
            return;
        }

        if (this.extension != null) {
            if (!this.extension.exit()) {
                return;
            }
        }

        initMenuItems();

        JLabel lbl = (JLabel) pnlMenu.getComponent(menuItem.getLabelIndex());
        CompoundBorder cb = BorderFactory.createCompoundBorder(new MatteBorder(0, 4, 0, 0, THEME_COLOR1), new EmptyBorder(0, 15, 0, 0));
        lbl.setBorder(cb);

        lbl.setForeground(SELECTED_MENU_FORGROUNDECOLOR);
        lbl.setBackground(SELECTED_MENU_BACKGROUNDCOLOR);
        lbl.setOpaque(true);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        lbl.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));

        try {
            ImageIcon icon;
            if (lbl.getText().equals("Minimize") || lbl.getText().equals("Log out") || lbl.getText().equals("Exit System")) {
                icon = new ImageIcon(getClass().getResource(PATH_NAME + lbl.getText() + "_hover.png"));
            } else {
                icon = new ImageIcon(getClass().getResource(PATH_NAME + lbl.getText().split("- ")[1] + "_hover.png"));
            }
            lbl.setIcon(icon);
        } catch (NullPointerException | ArrayIndexOutOfBoundsException exception) {
            //

        } catch (Exception exception) {
            exception.printStackTrace();

        } finally {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            MenuItems oldMenuItem = SelectedMenuItem;
            SelectedMenuItem = menuItem;

            lbl.setCursor(this.getCursor());

            switch (menuItem) {
                case DASHBOARD:
                case CASH_SALES:
                case CREDIT_SALE:
                case ITEMS:
                case ITEM_MASTER:
                case SUPPLIER_MASTER:
                case SUPPLIER_ORDER:
                case GRN:
                case VIEWS:
                case SYSTEM_SETTINGS:
                case CUSTOMERS:
                    handleNavigation(menuItem, null);
                    break;
                case MINIMIZE:
                    this.setState(JFrame.ICONIFIED);
                    SelectedMenuItem = oldMenuItem;
                    JLabel lblMenu = (JLabel) pnlMenu.getComponent(oldMenuItem.getLabelIndex());
                    lblMenu.setBorder(cb);

                    lblMenu.setForeground(SELECTED_MENU_FORGROUNDECOLOR);
                    lblMenu.setBackground(SELECTED_MENU_BACKGROUNDCOLOR);
                    lblMenu.setOpaque(true);
                    lblMenu.setFont(lbl.getFont().deriveFont(Font.BOLD));

                    ImageIcon icon;
                    if (lblMenu.getText().equals("Minimize") || lblMenu.getText().equals("Log out") || lblMenu.getText().equals("Exit System")) {
                        icon = new ImageIcon(getClass().getResource(PATH_NAME + lblMenu.getText() + "_hover.png"));
                    } else {
                        icon = new ImageIcon(getClass().getResource(PATH_NAME + lblMenu.getText().split("- ")[1] + "_hover.png"));
                    }
                    lblMenu.setIcon(icon);

                    break;
                case EXIT_SYSTEM:
                    System.exit(0);
                    break;

            }

            pnlContainer.updateUI();
            this.setCursor(Cursor.getDefaultCursor());
            lbl.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        }
    }

    public enum MenuItems {

        /*
         * I have messed it up totally here, Now, rather than going back, I used a tricky case.
         * Run below code everytime if you add new item to the menu panel
         * Then set the label index according to the output
         */
//        int i = 0;
//        for (Component com : pnlMenu.getComponents()) {
//            if (com instanceof JLabel){
//                JLabel lbl = (JLabel) com;
//                System.out.println(i + " " + lbl.getText());
//                i++;
//            }
//        }   
//        
//0 [F1] - Dashboard
//1 [F2] - Cash Sales
//2 [F4] - Item Search
//3 [F5] - Item Master
//4 [F6] - Supplier Master
//5 [F7] - Supplier Orders
//6 [F8] - GRN
//7 [F9] - Views
//8 [F10] - Customers
//9 Log out
//10 [F3] - Credit Issue
//11 Minimize
//12 Exit System
//13 [F11] - System Settings   
        NOTHING(-1), DASHBOARD(0), CASH_SALES(1), CREDIT_SALE(10), ITEMS(2), ITEM_MASTER(3), SUPPLIER_MASTER(4),
        SUPPLIER_ORDER(5), GRN(6), VIEWS(7), SYSTEM_SETTINGS(13), MINIMIZE(11), LOGOUT(9), EXIT_SYSTEM(12), CUSTOMERS(8);

        private final int index;

        private MenuItems(int index) {
            this.index = index;
        }

        public int getLabelIndex() {
            return index;
        }

    }

    private void handleNavigation(MenuItems menuItem, Extension extension) {

//        if (this.extension != null) {
//            if (!this.extension.exit()) {
//                return;
//            }
//        }
        this.extension = extension;

        if (menuItem != MenuItems.MINIMIZE) {
            pnlContainer.removeAll();
        }

        if (extension == null) {

            lblExtension.setVisible(false);
            lblfslash2.setVisible(false);

            if (menuItem != MenuItems.DASHBOARD) {
                lblfslash1.setVisible(true);
                lblMenuItem.setVisible(true);
                lblMenuItem.setFont(lblMenuItem.getFont().deriveFont(Font.BOLD));
                lblHome.setFont(lblHome.getFont().deriveFont(Font.PLAIN));
            } else {
                lblfslash1.setVisible(false);
                lblMenuItem.setVisible(false);
                lblHome.setFont(lblHome.getFont().deriveFont(Font.BOLD));
            }

            switch (menuItem) {
                case DASHBOARD:
                    if (pnlDashBoard == null) {
                        pnlDashBoard = new DashBoard();
                    }
                    pnlContainer.add(pnlDashBoard);
                    break;
                case CASH_SALES:
                    if (pnlCashSales == null) {
                        pnlCashSales = new CashSales();
                    }
                    lblMenuItem.setText("Cash Sales");
                    pnlContainer.add(pnlCashSales);
                    break;
                case CREDIT_SALE:
                    if (pnlCreditIssue == null) {
                        pnlCreditIssue = new CreditSales();
                    }
                    lblMenuItem.setText("Credit Issue");
                    pnlContainer.add(pnlCreditIssue);
                    break;
                case ITEMS:
                    if (pnlItemSearch == null) {
                        pnlItemSearch = new Items();
                    }
                    lblMenuItem.setText("Item Search");
                    pnlContainer.add(pnlItemSearch);
                    break;
                case ITEM_MASTER:
                    if (pnlItemMaster == null) {
                        pnlItemMaster = new ItemMaster();
                    }
                    lblMenuItem.setText("Item Master");
                    pnlContainer.add(pnlItemMaster);
                    break;
                case SUPPLIER_MASTER:
                    if (pnlSupplierMaster == null) {
                        pnlSupplierMaster = new Suppliers();
                    }
                    lblMenuItem.setText("Supplier Master");
                    pnlContainer.add(pnlSupplierMaster);
                    break;
                case SUPPLIER_ORDER:
                    if (pnlSupplierOrders == null) {
                        pnlSupplierOrders = new SupplierOrders();
                    }
                    lblMenuItem.setText("Supplier Orders");
                    pnlContainer.add(pnlSupplierOrders);
                    break;
                case GRN:
                    if (pnlGRNMaster == null) {
                        pnlGRNMaster = new GRNMaster();
                    }
                    lblMenuItem.setText("GRN");
                    pnlContainer.add(pnlGRNMaster);
                    break;
                case VIEWS:
                    if (pnlViews == null) {
                        pnlViews = new Views();
                    }
                    lblMenuItem.setText("Views");
                    pnlContainer.add(pnlViews);
                    break;
                case SYSTEM_SETTINGS:
                    if (pnlSystemSettings == null) {
                        pnlSystemSettings = new SystemSettings();
                    }
                    lblMenuItem.setText("System Settings");
                    pnlContainer.add(pnlSystemSettings);
                    break;
                case CUSTOMERS:
                    if (pnlCustomers == null) {
                        pnlCustomers = new Customers();
                    }
                    lblMenuItem.setText("Customers");
                    pnlContainer.add(pnlCustomers);
                    break;
            }

            if (pnlContainer.getComponent(0) instanceof FocusHandler) {
                FocusHandler fh = (FocusHandler) pnlContainer.getComponent(0);
                fh.initFoucs();
            }

        } else {

            lblfslash2.setVisible(true);
            lblExtension.setText(extension.getExtensionName());
            lblExtension.setVisible(true);

            lblMenuItem.setFont(lblMenuItem.getFont().deriveFont(Font.PLAIN));
            lblExtension.setFont(lblExtension.getFont().deriveFont(Font.BOLD));

        }

    }

    public void setExtenstion(Extension extension) {
        handleNavigation(SelectedMenuItem, extension);
    }

    public void resetMenu(MenuItems menuItem) {
        switch (menuItem) {
            case DASHBOARD:
                pnlDashBoard = new DashBoard();
                break;
            case CASH_SALES:
                pnlCashSales = new CashSales();
                break;
            case CREDIT_SALE:
                pnlCreditIssue = new CreditSales();
                break;
            case ITEMS:
                pnlItemSearch = new Items();
                break;
            case ITEM_MASTER:
                pnlItemMaster = new ItemMaster();
                break;
            case SUPPLIER_MASTER:
                pnlSupplierMaster = new Suppliers();
                break;
            case SUPPLIER_ORDER:
                pnlSupplierOrders = new SupplierOrders();
                break;
            case GRN:
                pnlGRNMaster = new GRNMaster();
                break;
            case VIEWS:
                pnlViews = new Views();
                break;
            case SYSTEM_SETTINGS:
                pnlSystemSettings = new SystemSettings();
                break;
        }
        this.extension = null;
        // A little hack
        SelectedMenuItem = MenuItems.NOTHING;
        setSeletctedMenuItem(menuItem);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        pnlHeader = new javax.swing.JPanel();
        lblUser = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        lblHome = new javax.swing.JLabel();
        lblfslash1 = new javax.swing.JLabel();
        lblMenuItem = new javax.swing.JLabel();
        lblfslash2 = new javax.swing.JLabel();
        lblExtension = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pnlMenu = new javax.swing.JPanel();
        lblDashboard = new javax.swing.JLabel();
        lblCashSale = new javax.swing.JLabel();
        lblCreditSale = new javax.swing.JLabel();
        lblItems = new javax.swing.JLabel();
        lblItemMaster = new javax.swing.JLabel();
        lblSuppliers = new javax.swing.JLabel();
        lblSupplierOrders = new javax.swing.JLabel();
        lblGRN = new javax.swing.JLabel();
        lblViews = new javax.swing.JLabel();
        lblCustomers = new javax.swing.JLabel();
        lblMinimize = new javax.swing.JLabel();
        lblLogOut = new javax.swing.JLabel();
        lblExitSystem = new javax.swing.JLabel();
        lblSystemSettings = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnlContainer = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Wine Stores");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setUndecorated(true);
        setResizable(false);

        pnlHeader.setBackground(new java.awt.Color(72, 158, 231));

        lblUser.setFont(new java.awt.Font("Open Sans", 1, 24)); // NOI18N
        lblUser.setForeground(new java.awt.Color(255, 255, 255));
        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUser.setText("Admin");
        lblUser.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lblUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblUserMouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Open Sans", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("HORAMPELLA WINE STORES");

        lblTime.setFont(new java.awt.Font("Open Sans", 1, 24)); // NOI18N
        lblTime.setForeground(new java.awt.Color(255, 255, 255));
        lblTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTime.setText("jLabel1");

        lblHome.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        lblHome.setForeground(new java.awt.Color(255, 255, 255));
        lblHome.setText("Dashboard");
        lblHome.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHomeMouseClicked(evt);
            }
        });

        lblfslash1.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblfslash1.setForeground(new java.awt.Color(255, 255, 255));
        lblfslash1.setText("/");

        lblMenuItem.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblMenuItem.setForeground(new java.awt.Color(255, 255, 255));
        lblMenuItem.setText("Supplier Orders");
        lblMenuItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenuItemMouseClicked(evt);
            }
        });

        lblfslash2.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblfslash2.setForeground(new java.awt.Color(255, 255, 255));
        lblfslash2.setText("/");

        lblExtension.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        lblExtension.setForeground(new java.awt.Color(255, 255, 255));
        lblExtension.setText("New Supplier Order");
        lblExtension.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel1.setFont(new java.awt.Font("Open Sans", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 204, 204));
        jLabel1.setText("[");

        jLabel3.setFont(new java.awt.Font("Open Sans", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 204, 204));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("]");

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblfslash1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMenuItem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblfslash2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblExtension)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(lblTime)
                .addGap(22, 22, 22)
                .addComponent(lblUser)
                .addGap(21, 21, 21))
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUser)
                    .addComponent(lblTime)
                    .addComponent(jLabel2)
                    .addComponent(lblHome)
                    .addComponent(lblfslash1)
                    .addComponent(lblMenuItem)
                    .addComponent(lblfslash2)
                    .addComponent(lblExtension)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlMenu.setBackground(new java.awt.Color(55, 57, 66));
        pnlMenu.setPreferredSize(new java.awt.Dimension(175, 74));

        lblDashboard.setBackground(new java.awt.Color(255, 0, 255));
        lblDashboard.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblDashboard.setForeground(new java.awt.Color(150, 154, 167));
        lblDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/Dashboard.png"))); // NOI18N
        lblDashboard.setText("[F1] - Dashboard");
        lblDashboard.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
        lblDashboard.setIconTextGap(15);
        lblDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDashboardMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblDashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblDashboardMouseExited(evt);
            }
        });

        lblCashSale.setBackground(new java.awt.Color(255, 0, 255));
        lblCashSale.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblCashSale.setForeground(new java.awt.Color(150, 154, 167));
        lblCashSale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/Cash Sales.png"))); // NOI18N
        lblCashSale.setText("[F2] - Cash Sales");
        lblCashSale.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
        lblCashSale.setIconTextGap(15);
        lblCashSale.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblCashSaleMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblCashSaleMouseExited(evt);
            }
        });

        lblCreditSale.setBackground(new java.awt.Color(255, 0, 255));
        lblCreditSale.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblCreditSale.setForeground(new java.awt.Color(150, 154, 167));
        lblCreditSale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/Credit Issue.png"))); // NOI18N
        lblCreditSale.setText("[F3] - Credit Issue");
        lblCreditSale.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
        lblCreditSale.setIconTextGap(15);
        lblCreditSale.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCreditSaleMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblCreditSaleMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblCreditSaleMouseExited(evt);
            }
        });

        lblItems.setBackground(new java.awt.Color(255, 0, 255));
        lblItems.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblItems.setForeground(new java.awt.Color(150, 154, 167));
        lblItems.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/Item Search.png"))); // NOI18N
        lblItems.setText("[F4] - Item Search");
        lblItems.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
        lblItems.setIconTextGap(15);
        lblItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblItemsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblItemsMouseExited(evt);
            }
        });

        lblItemMaster.setBackground(new java.awt.Color(255, 0, 255));
        lblItemMaster.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblItemMaster.setForeground(new java.awt.Color(150, 154, 167));
        lblItemMaster.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/Item Master.png"))); // NOI18N
        lblItemMaster.setText("[F5] - Item Master");
        lblItemMaster.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
        lblItemMaster.setIconTextGap(15);
        lblItemMaster.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblItemMasterMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblItemMasterMouseExited(evt);
            }
        });

        lblSuppliers.setBackground(new java.awt.Color(255, 0, 255));
        lblSuppliers.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblSuppliers.setForeground(new java.awt.Color(150, 154, 167));
        lblSuppliers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/Supplier Master.png"))); // NOI18N
        lblSuppliers.setText("[F6] - Supplier Master");
        lblSuppliers.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
        lblSuppliers.setIconTextGap(15);
        lblSuppliers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSuppliersMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblSuppliersMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblSuppliersMouseExited(evt);
            }
        });

        lblSupplierOrders.setBackground(new java.awt.Color(255, 0, 255));
        lblSupplierOrders.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblSupplierOrders.setForeground(new java.awt.Color(150, 154, 167));
        lblSupplierOrders.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/Supplier Orders.png"))); // NOI18N
        lblSupplierOrders.setText("[F7] - Supplier Orders");
        lblSupplierOrders.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
        lblSupplierOrders.setIconTextGap(15);
        lblSupplierOrders.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSupplierOrdersMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblSupplierOrdersMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblSupplierOrdersMouseExited(evt);
            }
        });

        lblGRN.setBackground(new java.awt.Color(255, 0, 255));
        lblGRN.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblGRN.setForeground(new java.awt.Color(150, 154, 167));
        lblGRN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/GRN.png"))); // NOI18N
        lblGRN.setText("[F8] - GRN");
        lblGRN.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
        lblGRN.setIconTextGap(15);
        lblGRN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblGRNMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblGRNMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblGRNMouseExited(evt);
            }
        });

        lblViews.setBackground(new java.awt.Color(255, 0, 255));
        lblViews.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblViews.setForeground(new java.awt.Color(150, 154, 167));
        lblViews.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/Views.png"))); // NOI18N
        lblViews.setText("[F9] - Views");
        lblViews.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
        lblViews.setIconTextGap(15);
        lblViews.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblViewsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblViewsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblViewsMouseExited(evt);
            }
        });

        lblCustomers.setBackground(new java.awt.Color(255, 0, 255));
        lblCustomers.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblCustomers.setForeground(new java.awt.Color(150, 154, 167));
        lblCustomers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/Customers.png"))); // NOI18N
        lblCustomers.setText("[F10] - Customers");
        lblCustomers.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
        lblCustomers.setIconTextGap(15);
        lblCustomers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCustomersMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblCustomersMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblCustomersMouseExited(evt);
            }
        });

        lblMinimize.setBackground(new java.awt.Color(255, 0, 255));
        lblMinimize.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblMinimize.setForeground(new java.awt.Color(150, 154, 167));
        lblMinimize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/Minimize.png"))); // NOI18N
        lblMinimize.setText("Minimize");
        lblMinimize.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
        lblMinimize.setIconTextGap(15);
        lblMinimize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMinimizeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblMinimizeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblMinimizeMouseExited(evt);
            }
        });

        lblLogOut.setBackground(new java.awt.Color(255, 0, 255));
        lblLogOut.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblLogOut.setForeground(new java.awt.Color(150, 154, 167));
        lblLogOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/Log out.png"))); // NOI18N
        lblLogOut.setText("Log out");
        lblLogOut.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
        lblLogOut.setIconTextGap(15);
        lblLogOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblLogOutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblLogOutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblLogOutMouseExited(evt);
            }
        });

        lblExitSystem.setBackground(new java.awt.Color(255, 0, 255));
        lblExitSystem.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblExitSystem.setForeground(new java.awt.Color(150, 154, 167));
        lblExitSystem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/Exit System.png"))); // NOI18N
        lblExitSystem.setText("Exit System");
        lblExitSystem.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
        lblExitSystem.setIconTextGap(15);
        lblExitSystem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblExitSystemMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblExitSystemMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblExitSystemMouseExited(evt);
            }
        });

        lblSystemSettings.setBackground(new java.awt.Color(255, 0, 255));
        lblSystemSettings.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblSystemSettings.setForeground(new java.awt.Color(150, 154, 167));
        lblSystemSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/ijse/winestores/icons/System Settings.png"))); // NOI18N
        lblSystemSettings.setText("[F11] - System Settings");
        lblSystemSettings.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
        lblSystemSettings.setIconTextGap(15);
        lblSystemSettings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSystemSettingsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblSystemSettingsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblSystemSettingsMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnlMenuLayout = new javax.swing.GroupLayout(pnlMenu);
        pnlMenu.setLayout(pnlMenuLayout);
        pnlMenuLayout.setHorizontalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblDashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblCashSale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblItems, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblItemMaster, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblSuppliers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblSupplierOrders, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblGRN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblViews, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblCustomers, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
            .addComponent(lblLogOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblCreditSale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblMinimize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblExitSystem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblSystemSettings, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
        );
        pnlMenuLayout.setVerticalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lblCashSale, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lblCreditSale, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lblItems, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lblItemMaster, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lblSuppliers, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lblSupplierOrders, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lblGRN, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lblViews, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lblCustomers, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lblSystemSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(lblMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lblLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lblExitSystem, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane1.setBackground(new java.awt.Color(233, 236, 242));
        jScrollPane1.setBorder(null);

        pnlContainer.setBackground(new java.awt.Color(233, 236, 242));
        pnlContainer.setLayout(new java.awt.CardLayout());
        jScrollPane1.setViewportView(pnlContainer);

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addComponent(pnlMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1))
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(pnlMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblDashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDashboardMouseEntered

    }//GEN-LAST:event_lblDashboardMouseEntered

    private void lblDashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDashboardMouseExited

    }//GEN-LAST:event_lblDashboardMouseExited

    private void lblCashSaleMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCashSaleMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCashSaleMouseEntered

    private void lblCashSaleMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCashSaleMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCashSaleMouseExited

    private void lblItemsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblItemsMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblItemsMouseEntered

    private void lblItemsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblItemsMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblItemsMouseExited

    private void lblItemMasterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblItemMasterMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblItemMasterMouseEntered

    private void lblItemMasterMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblItemMasterMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblItemMasterMouseExited

    private void lblSuppliersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSuppliersMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblSuppliersMouseClicked

    private void lblSuppliersMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSuppliersMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblSuppliersMouseEntered

    private void lblSuppliersMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSuppliersMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblSuppliersMouseExited

    private void lblSupplierOrdersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSupplierOrdersMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblSupplierOrdersMouseClicked

    private void lblSupplierOrdersMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSupplierOrdersMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblSupplierOrdersMouseEntered

    private void lblSupplierOrdersMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSupplierOrdersMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblSupplierOrdersMouseExited

    private void lblGRNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblGRNMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblGRNMouseClicked

    private void lblGRNMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblGRNMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblGRNMouseEntered

    private void lblGRNMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblGRNMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblGRNMouseExited

    private void lblViewsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblViewsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblViewsMouseClicked

    private void lblViewsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblViewsMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblViewsMouseEntered

    private void lblViewsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblViewsMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblViewsMouseExited

    private void lblCustomersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCustomersMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCustomersMouseClicked

    private void lblCustomersMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCustomersMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCustomersMouseEntered

    private void lblCustomersMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCustomersMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCustomersMouseExited

    private void lblLogOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLogOutMouseClicked
    }//GEN-LAST:event_lblLogOutMouseClicked

    private void lblLogOutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLogOutMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblLogOutMouseEntered

    private void lblLogOutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLogOutMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblLogOutMouseExited

    private void lblDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDashboardMouseClicked

    }//GEN-LAST:event_lblDashboardMouseClicked

    private void lblUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblUserMouseClicked

    }//GEN-LAST:event_lblUserMouseClicked

    private void lblCreditSaleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCreditSaleMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCreditSaleMouseClicked

    private void lblCreditSaleMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCreditSaleMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCreditSaleMouseEntered

    private void lblCreditSaleMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCreditSaleMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCreditSaleMouseExited

    private void lblMinimizeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMinimizeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblMinimizeMouseClicked

    private void lblMinimizeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMinimizeMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblMinimizeMouseEntered

    private void lblMinimizeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMinimizeMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblMinimizeMouseExited

    private void lblExitSystemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblExitSystemMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblExitSystemMouseClicked

    private void lblExitSystemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblExitSystemMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblExitSystemMouseEntered

    private void lblExitSystemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblExitSystemMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblExitSystemMouseExited

    private void lblSystemSettingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSystemSettingsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblSystemSettingsMouseClicked

    private void lblSystemSettingsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSystemSettingsMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblSystemSettingsMouseEntered

    private void lblSystemSettingsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSystemSettingsMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblSystemSettingsMouseExited
    private void lblMenuItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenuItemMouseClicked
        // A little hack
        MenuItems currentSelectedMenuItem = SelectedMenuItem;
        SelectedMenuItem = MenuItems.NOTHING;
        setSeletctedMenuItem(currentSelectedMenuItem);
    }//GEN-LAST:event_lblMenuItemMouseClicked

    private void lblHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseClicked
        setSeletctedMenuItem(MenuItems.DASHBOARD);
    }//GEN-LAST:event_lblHomeMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    UIManager.put("ToolTip.background", new Color(0, 0, 0, 200));
                    UIManager.put("ToolTip.border", new EmptyBorder(5, 5, 5, 5));
//                    UIManager.put("ToolTip.size", new Di);
                    UIManager.put("ToolTip.foreground", new Color(255, 255, 255));
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCashSale;
    private javax.swing.JLabel lblCreditSale;
    private javax.swing.JLabel lblCustomers;
    private javax.swing.JLabel lblDashboard;
    private javax.swing.JLabel lblExitSystem;
    private javax.swing.JLabel lblExtension;
    private javax.swing.JLabel lblGRN;
    private javax.swing.JLabel lblHome;
    private javax.swing.JLabel lblItemMaster;
    private javax.swing.JLabel lblItems;
    private javax.swing.JLabel lblLogOut;
    private javax.swing.JLabel lblMenuItem;
    private javax.swing.JLabel lblMinimize;
    private javax.swing.JLabel lblSupplierOrders;
    private javax.swing.JLabel lblSuppliers;
    private javax.swing.JLabel lblSystemSettings;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblViews;
    private javax.swing.JLabel lblfslash1;
    private javax.swing.JLabel lblfslash2;
    javax.swing.JPanel pnlContainer;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlMenu;
    // End of variables declaration//GEN-END:variables
}
