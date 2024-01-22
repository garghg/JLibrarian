package library;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

// BookDetailsFrame class extends JFrame
public class BookDetailsFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static BookDetailsFrame frame;

    // Constructor to initialize the BookDetailsFrame
    public BookDetailsFrame(Object[] bookData) {
        super("Book Details");
        frame = this;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create and add the details panel
        JPanel detailsPanel = createDetailsPanel(bookData);
        add(detailsPanel, BorderLayout.CENTER);

        // Create and add the buttons panel
        JPanel buttonsPanel = createButtonsPanel(bookData);
        add(buttonsPanel, BorderLayout.SOUTH);

        setSize(1200, 600);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    // Method to create the details panel
    private static JPanel createDetailsPanel(Object[] bookData) {
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 100, 5, 5);

        // Array of labels for different book details
        String[] labels = {
                "Genre:", "Title:", "Subtitle:", "Author(s):", "Prices (CAD):", "Description:",
                "Number of Pages:", "Published Year:", "Average Rating:", "Ratings Count:",
                "ISBN-13:", "ISBN-10:", "Thumbnail Link:"
        };

        // Loop to create and add labels and corresponding values to the details panel
        for (int i = 0; i < labels.length; i++) {
            if (i >= bookData.length) {
                break;
            }

            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Arial", Font.BOLD, 14));

            Component value;
            if (labels[i].equals("Description:")) {
                // If the label is "Description:", create a JTextArea for the description
                String descriptionText = String.valueOf(bookData[i]);
                JTextArea textArea;
                if (descriptionText.isEmpty()) {
                    textArea = new JTextArea("Sorry...Description unavailable at the moment! :(");
                } else {
                    textArea = new JTextArea(descriptionText);
                }
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setEditable(false);
                textArea.setBackground(detailsPanel.getBackground());
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 100));
                scrollPane.setBorder(BorderFactory.createEmptyBorder());
                value = scrollPane;
            } else if (labels[i].equals("Thumbnail Link:")) {
                // If the label is "Thumbnail Link:", create an image JLabel with a clickable link
                String thumbnailLink = String.valueOf(bookData[i]);
                JLabel imageLabel = new JLabel("<html><img src='" + thumbnailLink + "' width='150' height='225'></html>");

                imageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

                imageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        openWebpage(thumbnailLink);
                    }
                });

                gbc.gridx = -1;
                gbc.gridy = i - 10;
                gbc.gridheight = 5;
                detailsPanel.add(imageLabel, gbc);
                continue;
            } else {
                // For other labels, create a simple JLabel with the corresponding value
                JLabel textLabel = new JLabel(String.valueOf(bookData[i]));
                textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                value = textLabel;
            }

            gbc.gridx = 1;
            gbc.gridy = i;
            gbc.gridheight = 1;
            detailsPanel.add(label, gbc);

            gbc.gridx = 2;
            gbc.gridy = i;
            detailsPanel.add(value, gbc);
        }

        return detailsPanel;
    }

    // Method to create the buttons panel
    private static JPanel createButtonsPanel(Object[] bookData) {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 50, 30));

        // Create "My Cart" button
        JButton myCartButton = new JButton("My Cart");
        myCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCart();
            }

            // Static method to open the cart frame
            public static void openCart() {
                SwingUtilities.invokeLater(() -> {
                    if (CartFrame.getInstance() != null) {
                        CartFrame.getInstance().setVisible(true);
                    } else {
                        CartFrame cartFrame = new CartFrame();
                        cartFrame.setVisible(true);
                    }
                    frame.dispose();
                });
            }
        });

        // Create "Add to Cart" button
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart(bookData);
            }
        });

        // Create "Buy Now" button
        JButton buyNowButton = new JButton("Buy Now");
        buyNowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processPurchase(bookData);
            }
        });

        // Create "Continue Shopping" button
        JButton viewFullCatalogueButton = new JButton("Continue Shopping");
        viewFullCatalogueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewFullCatalogue();
            }
        });

        // Set button styles
        setButtonStyles(myCartButton);
        setButtonStyles(addToCartButton);
        setButtonStyles(buyNowButton);
        setButtonStyles(viewFullCatalogueButton);

        // Add buttons to the panel
        buttonsPanel.add(addToCartButton);
        buttonsPanel.add(buyNowButton);
        buttonsPanel.add(viewFullCatalogueButton);
        buttonsPanel.add(myCartButton);

        return buttonsPanel;
    }

    // Method to set styles for buttons
    private static void setButtonStyles(JButton button) {
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    // Method to handle the purchase process
    private static void processPurchase(Object[] bookData) {
        int quantity = getQuantityInput(1, 10);
        if (quantity > 0) {
            Object priceObj = bookData[4];

            if (priceObj != null) {
                String priceString = priceObj.toString();
                try {
                    double amount = Double.parseDouble(priceString) * quantity;
                    int confirmation = JOptionPane.showConfirmDialog(
                            null, "Confirm your purchase of $" + amount + " for " + quantity + " book(s)", "Confirmation", JOptionPane.YES_NO_OPTION);

                    if (confirmation == JOptionPane.YES_OPTION) {
                        String message = "We appreciate your support for our business. Come again!";
                        JOptionPane.showMessageDialog(null, message, "Purchase Successful", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error parsing the book price.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(
                        null, "Price information not available for the selected book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to add items to the cart
    private static void addToCart(Object[] bookData) {
        int quantity = getQuantityInput(1, 10);
        if (quantity > 0) {
            CartFrame.getInstance().addToCart(bookData, quantity);

            JOptionPane.showMessageDialog(
                    null, quantity + " item(s) added to cart.", "Added to Cart", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Method to get quantity input from the user
    public static int getQuantityInput(int start, int max) {
        JPanel quantityPanel = new JPanel(new BorderLayout(5, 5));
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField(String.valueOf(start), 2);
        JButton increaseButton = new JButton("+");
        JButton decreaseButton = new JButton("-");

        // Set button styles
        setButtonStyles(increaseButton);
        setButtonStyles(decreaseButton);

        quantityField.setEditable(false);
        quantityField.setBackground(quantityPanel.getBackground());
        quantityField.setBorder(BorderFactory.createEmptyBorder());

        // Increase button action listener
        increaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentQuantity = Integer.parseInt(quantityField.getText());
                if (currentQuantity < max) {
                    quantityField.setText(String.valueOf(currentQuantity + 1));
                }
            }
        });

        // Decrease button action listener
        decreaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentQuantity = Integer.parseInt(quantityField.getText());
                if (currentQuantity > 1) {
                    quantityField.setText(String.valueOf(currentQuantity - 1));
                }
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        buttonPanel.add(decreaseButton);
        buttonPanel.add(increaseButton);

        quantityPanel.add(quantityLabel, BorderLayout.WEST);
        quantityPanel.add(quantityField, BorderLayout.CENTER);
        quantityPanel.add(buttonPanel, BorderLayout.EAST);

        int option = JOptionPane.showConfirmDialog(
                null, quantityPanel, "Enter Quantity", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                return Integer.parseInt(quantityField.getText());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        null, "Invalid quantity. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return start;
    }

    // Method to view the full catalog
    private static void viewFullCatalogue() {
        LibraryFrame libraryFrame = new LibraryFrame("C:\\Users\\gargh\\eclipse-workspace\\JLib\\src\\books.csv");
        libraryFrame.setVisible(true);
        frame.dispose();
    }

    // Method to open a webpage
    private static void openWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Main method to test the BookDetailsFrame
    public static void main(String[] args) {
        Object[] sampleBookData = {
                "Fiction", "The Greatest Novel of All Time", "The Novel", "Haardik Garg",
                "100", "This is the greatest book of all time. READ IT!", "100", "2024", "5", "99999999999999",
                "1234567890123", "0123456789", "https://v4m9y9w9.rocketcdn.me/wp-content/uploads/2019/08/Image-via-Meme-e1565882942796-300x286.png"
        };
        BookDetailsFrame detailsFrame = new BookDetailsFrame(sampleBookData);
        detailsFrame.setVisible(true);
    }
}
