// Import necessary packages
package library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

// Define the SearchResultsFrame class that extends JFrame
public class SearchResultsFrame extends JFrame {
    // Serial version UID for serialization
    private static final long serialVersionUID = 1L;

    // Constructor for SearchResultsFrame
    public SearchResultsFrame(String searchText, ArrayList<String[]> searchResults) {
        super("Search Results"); // Set the title of the JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation
        setLayout(new BorderLayout()); // Set layout manager for the frame

        // Create and configure search label
        JLabel searchLabel = new JLabel("Search Results for: " + searchText);
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(searchLabel, BorderLayout.NORTH);

        add(headerPanel, BorderLayout.NORTH); // Add search label to the frame

        // Check if search results are not empty
        if (!searchResults.isEmpty()) {
            JTable searchTable = createSearchTable(searchResults); // Create search table

            // Add a mouse listener to handle double-click events on the table
            searchTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = searchTable.rowAtPoint(e.getPoint());
                        if (row != -1) {
                            // Get the data of the selected row
                            Object[] rowData = new Object[searchTable.getColumnCount()];
                            for (int i = 0; i < searchTable.getColumnCount(); i++) {
                                rowData[i] = searchTable.getValueAt(row, i);
                            }

                            // Create and display a new BookDetailsFrame with the selected data
                            BookDetailsFrame detailsFrame = new BookDetailsFrame(rowData);
                            detailsFrame.setVisible(true);

                            // Close the current SearchResultsFrame
                            dispose();
                        }
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(searchTable);
            add(scrollPane, BorderLayout.CENTER); // Add scrollable table to the center of the frame
        }

        // Create and add button panel to the frame
        JPanel buttonPanel = createButtonPanel(searchResults.size());
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(1200, 600); // Set the size of the frame
        setResizable(false); // Disable frame resizing
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    // Create and configure the search results table
    private static JTable createSearchTable(ArrayList<String[]> searchResults) {
        JTable table;
        // Create a default table model with non-editable cells
        DefaultTableModel model = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add columns to the table model
        model.addColumn("Genre");
        model.addColumn("Title");
        model.addColumn("Subtitle");
        model.addColumn("Author(s)");
        model.addColumn("Prices (CAD)");
        model.addColumn("Description");
        model.addColumn("Availability");
        model.addColumn("Number of Pages");
        model.addColumn("Published Year");
        model.addColumn("Average Rating");
        model.addColumn("Ratings Count");
        model.addColumn("ISBN-13");
        model.addColumn("ISBN-10");
        model.addColumn("Thumbnail Link");

        // Add rows to the table model based on search results
        for (String[] data : searchResults) {
            model.addRow(data);
        }

        // Create a JTable with the configured model
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set preferred column widths for better display
        int[] columnWidths = {150, 200, 200, 200, 150, 300, 150, 100, 100, 100, 150, 100, 80, 300};
        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        return table;
    }

    // Create and configure the button panel
    private JPanel createButtonPanel(int numberOfResults) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton viewFullCatalogueButton = new JButton("View Full Catalogue");

        // Add action listener to the button to handle the "View Full Catalogue" action
        viewFullCatalogueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewFullCatalogue(); // Call method to view the full catalogue

                // Close the current SearchResultsFrame
                dispose();
            }
        });

        // Configure the appearance of the button
        viewFullCatalogueButton.setBackground(Color.BLACK);
        viewFullCatalogueButton.setForeground(Color.WHITE);
        viewFullCatalogueButton.setFocusPainted(false);

        // Create and configure a label to display the number of search results
        JLabel resultsLabel = new JLabel("Showing " + numberOfResults + " results");
        resultsLabel.setForeground(Color.BLUE);

        // Add components to the button panel
        buttonPanel.add(viewFullCatalogueButton);
        buttonPanel.add(resultsLabel);

        return buttonPanel;
    }

    // Method to view the full catalogue
    private void viewFullCatalogue() {
        String csvFilePath = "C:\\Users\\gargh\\eclipse-workspace\\JLib\\src\\books.csv";
        // Create a new LibraryFrame with the specified CSV file path and make it visible
        LibraryFrame libraryFrame = new LibraryFrame(csvFilePath);
        libraryFrame.setVisible(true);
    }

    // Main method for testing the SearchResultsFrame
    public static void main(String[] args) {
        // Create sample search results for testing
        ArrayList<String[]> sampleSearchResults = new ArrayList<>();
        sampleSearchResults.add(new String[]{"1234567890123", "0123456789", "Book Title", "Subtitle", "Author", "Fiction", "https://example.com/thumbnail", "Description", "2022", "4.5", "300", "1000"});
        
        // Create and display a SearchResultsFrame with the sample search results
        SearchResultsFrame resultsFrame = new SearchResultsFrame("Sample Search", sampleSearchResults);
        resultsFrame.setVisible(true);
    }
}
