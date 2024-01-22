package library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CartFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private DefaultTableModel cartTableModel;
    private static CartFrame cartFrameInstance;
    private JLabel itemsCountLabel;
    private JButton modifyQuantityButton;

    // Constructor for CartFrame
    public CartFrame() {
        super("My Cart");
        cartFrameInstance = this; // Initialize the instance variable
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for displaying cart content
        JPanel cartContentPanel = new JPanel(new BorderLayout());

        // Create a table model for the cart
        cartTableModel = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add columns to the table model
        cartTableModel.addColumn("Category");
        cartTableModel.addColumn("Title");
        cartTableModel.addColumn("Author");
        cartTableModel.addColumn("Price");
        cartTableModel.addColumn("Quantity");

        // Create a table with the specified table model
        JTable cartTable = new JTable(cartTableModel);
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartTable.getTableHeader().setReorderingAllowed(false);

        cartContentPanel.add(cartScrollPane, BorderLayout.CENTER);

        add(cartContentPanel, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonsPanel = new JPanel();

        // Button to continue shopping
        JButton viewCatalogueButton = new JButton("Continue Shopping");
        viewCatalogueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCatalogue();
            }
        });

        // Customize button appearance
        viewCatalogueButton.setBackground(Color.BLACK);
        viewCatalogueButton.setForeground(Color.WHITE);
        viewCatalogueButton.setFocusPainted(false);

        buttonsPanel.add(viewCatalogueButton);

        // Button to proceed to checkout
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowCount = cartTableModel.getRowCount();

                if (rowCount > 0) {
                    // Logic for handling checkout
                    String message = "Items in Cart:\n";
                    double totalPrice = 0.0;

                    for (int row = 0; row < rowCount; row++) {
                        String title = (String) cartTableModel.getValueAt(row, 1);
                        int quantity = (int) cartTableModel.getValueAt(row, 4);
                        double price = Double.parseDouble(cartTableModel.getValueAt(row, 3).toString());

                        totalPrice += quantity * price;

                        message += title + " - Quantity: " + quantity + " - Price: $" + price + "\n";
                    }

                    int confirmation = JOptionPane.showConfirmDialog(null, message + "\nTotal Price: $" + totalPrice + "\nConfirm your purchase?", "Checkout Confirmation", JOptionPane.YES_NO_OPTION);

                    if (confirmation == JOptionPane.YES_OPTION) {
                        clearCart();

                        JOptionPane.showMessageDialog(null, "Thank you for your purchase! Please come again.", "Checkout Successful", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Your cart is empty. Add items before checking out.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Customize button appearance
        checkoutButton.setBackground(Color.BLACK);
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFocusPainted(false);

        buttonsPanel.add(checkoutButton);

        // Button to modify quantity
        modifyQuantityButton = new JButton("Modify Quantity");
        modifyQuantityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cartTable.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a row to modify quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    modifyQuantity(cartTable);
                }
            }
        });

        // Button to remove selected item
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cartTable.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(null, "Please select an item in cart to remove.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    removeSelectedRow(cartTable);
                }
            }
        });

        // Customize button appearance
        modifyQuantityButton.setBackground(Color.BLACK);
        modifyQuantityButton.setForeground(Color.WHITE);
        modifyQuantityButton.setFocusPainted(false);
        
        removeButton.setBackground(Color.BLACK);
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);

        buttonsPanel.add(modifyQuantityButton);
        buttonsPanel.add(removeButton);

        // Label to display the total items in the cart
        itemsCountLabel = new JLabel("Items in Cart: 0");
        itemsCountLabel.setForeground(Color.BLACK);
        buttonsPanel.add(itemsCountLabel);

        add(buttonsPanel, BorderLayout.SOUTH);

        // Set frame properties
        setSize(1200, 600);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    // Method to get a single instance of CartFrame
    public static CartFrame getInstance() {
        if (cartFrameInstance == null) {
            cartFrameInstance = new CartFrame();
        }
        return cartFrameInstance;
    }

    // Method to add an item to the cart
    public void addToCart(Object[] bookData, int quantity) {
        try {
            if (cartFrameInstance == null) {
                cartFrameInstance = new CartFrame();
            }

            String title = (String) bookData[1];
            int existingRow = findItemRow(title);

            if (existingRow != -1) {
                int currentQuantity = (int) cartTableModel.getValueAt(existingRow, 4);
                cartTableModel.setValueAt(currentQuantity + quantity, existingRow, 4);
            } else {
                cartTableModel.addRow(new Object[]{
                        bookData[0], // Category
                        title,        // Title
                        bookData[3], // Author
                        bookData[4], // Price
                        quantity      // Quantity
                });
            }

            updateItemsCountLabel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to find the row of a specific item in the cart
    private int findItemRow(String title) {
        for (int row = 0; row < cartTableModel.getRowCount(); row++) {
            String rowTitle = (String) cartTableModel.getValueAt(row, 1);
            if (title.equals(rowTitle)) {
                return row;
            }
        }
        return -1;
    }

    // Method to update the items count label
    private void updateItemsCountLabel() {
        int totalQuantity = 0;
        for (int row = 0; row < cartTableModel.getRowCount(); row++) {
            totalQuantity += (int) cartTableModel.getValueAt(row, 4);
        }

        itemsCountLabel.setText("Items in Cart: " + totalQuantity);
    }

    // Method to modify the quantity of an item in the cart
    private void modifyQuantity(JTable cartTable) {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow != -1) {
            int currentQuantity = (int) cartTableModel.getValueAt(selectedRow, 4);

            int newQuantity = BookDetailsFrame.getQuantityInput(currentQuantity, 10);

            cartTableModel.setValueAt(newQuantity, selectedRow, 4);
            updateItemsCountLabel();
        }
    }

    // Method to remove the selected item from the cart
    private void removeSelectedRow(JTable cartTable) {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow != -1) {
            cartTableModel.removeRow(selectedRow);
            updateItemsCountLabel();
        }
    }

    // Method to clear the entire cart
    private void clearCart() {
        cartTableModel.setRowCount(0);
        updateItemsCountLabel();
    }

    // Method to open the catalog (used when continuing shopping)
    private static void openCatalogue() {
        SwingUtilities.invokeLater(() -> {
            if (cartFrameInstance != null) {
                cartFrameInstance.setVisible(false);
            }

            String csvFilePath = "C:\\Users\\gargh\\eclipse-workspace\\JLib\\src\\books.csv";
            LibraryFrame libraryFrame = new LibraryFrame(csvFilePath);
            libraryFrame.setVisible(true);
        });
    }

    // Main method to run the CartFrame
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CartFrame cartFrame = new CartFrame();
            cartFrame.setVisible(true);
        });
    }
}
