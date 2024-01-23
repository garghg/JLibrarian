package library;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LibraryFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static LibraryFrame libraryFrameInstance;

    // Constructor for LibraryFrame
    public LibraryFrame(String csvFilePath) {
        super("Catalogue");
        libraryFrameInstance = this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create table from CSV file
        JTable table = createTableFromCSV(csvFilePath);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create search panel and add it to the frame
        JPanel searchPanel = createSearchPanel(table);
        add(scrollPane, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);

        setSize(1200, 600);
        //set icon image for frame
        ImageIcon img = new ImageIcon("C:\\Users\\haardik.garg\\eclipse-workspace\\JLib\\src\\icon.png");
        setIconImage(img.getImage());
        setResizable(false);
        setLocationRelativeTo(null);
    }

    // Method to create JTable from CSV file
    private static JTable createTableFromCSV(String csvFilePath) {
        JTable table;
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
        model.addColumn("Number of Pages");
        model.addColumn("Published Year");
        model.addColumn("Average Rating");
        model.addColumn("Ratings Count");
        model.addColumn("ISBN-13");
        model.addColumn("ISBN-10");
        model.addColumn("Thumbnail Link");

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                // Parse CSV line and add data to the model
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].replaceAll("^\"|\"$", "");
                }

                String[] rearrangedData = {
                        data[5],  // Genre
                        data[2],  // Title
                        data[3],  // Subtitle
                        data[4],  // Authors
                        data[12], // Prices
                        data[7],  // Description
                        data[10], // Num Pages
                        data[8],  // Published Year
                        data[9],  // Average Rating
                        data[11], // Ratings Count
                        data[0],  // ISBN-13
                        data[1],  // ISBN-10
                        data[6]   // Thumbnail links
                };

                model.addRow(rearrangedData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create JTable with model and set properties
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getTableHeader().setReorderingAllowed(false);

        // Add double-click event to open book details frame
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    if (row != -1) {
                        Object[] rowData = new Object[model.getColumnCount()];
                        for (int i = 0; i < model.getColumnCount(); i++) {
                            rowData[i] = model.getValueAt(row, i);
                        }
                        
                        playButtonClickSound();

                        BookDetailsFrame detailsFrame = new BookDetailsFrame(rowData);
                        detailsFrame.setVisible(true);

                        libraryFrameInstance.dispose();
                    }
                }
            }
        });

        // Set preferred column widths
        int[] columnWidths = {150, 250, 250, 150, 100, 300, 150, 100, 100, 150, 100, 80, 150};
        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        // Add sorting functionality to the table
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(model);
        table.setRowSorter(rowSorter);

        return table;
    }

    // Method to create search panel
    private static JPanel createSearchPanel(JTable table) {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Search components
        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton myCartButton = new JButton("My Cart");

        JLabel itemCountLabel = new JLabel("Items: " + table.getRowCount());
        searchPanel.add(itemCountLabel);

        // Configure search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search(table, searchField, itemCountLabel);
            }
        });

        // Configure my cart button
        myCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCart();
            }
        });

        // Set button styles
        myCartButton.setBackground(Color.BLACK);
        myCartButton.setForeground(Color.WHITE);
        myCartButton.setFocusPainted(false);

        searchButton.setBackground(Color.BLACK);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);

        // Add components to search panel
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(myCartButton);

        return searchPanel;
    }

    // Method to open the cart
    public static void openCart() {
        SwingUtilities.invokeLater(() -> {
            if (CartFrame.getInstance() != null) {
                CartFrame.getInstance().setVisible(true);
            } else {
                CartFrame cartFrame = new CartFrame();
                cartFrame.setVisible(true);
            }
            libraryFrameInstance.dispose();
        });
    }

    // Method to show error message
    private static void showErrorMessage(String message, JTextField textField) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        textField.setText("");
    }

    // Method to perform search
    private static void search(JTable table, JTextField searchField, JLabel itemCountLabel) {
        String searchText = searchField.getText().trim();

        if (searchText.isEmpty()) {
            showErrorMessage("Search is empty. Please enter a valid search term.", searchField);
            itemCountLabel.setText("Items: " + table.getRowCount());
            return;
        }

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        ArrayList<String[]> searchResults = new ArrayList<>();

        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                String cellValue = model.getValueAt(row, col).toString();
                if (cellValue.toLowerCase().contains(searchText.toLowerCase())) {
                    String[] rowData = new String[model.getColumnCount()];
                    for (int i = 0; i < model.getColumnCount(); i++) {
                        rowData[i] = model.getValueAt(row, i).toString();
                    }
                    searchResults.add(rowData);
                    break;
                }
            }
        }

        if (!searchResults.isEmpty()) {
            SearchResultsFrame searchResultsFrame = new SearchResultsFrame(searchText, searchResults);

            JFrame libraryFrame = (JFrame) SwingUtilities.getWindowAncestor(table);
            libraryFrame.dispose();

            searchResultsFrame.setVisible(true);
            searchResultsFrame.setResizable(false);
        } else {
            showErrorMessage("No results found for: " + searchText, searchField);
        }

        itemCountLabel.setText("Items: " + table.getRowCount());
        searchField.setText("");
    }
    
    // Method to play a sound when the button is clicked
    private static void playButtonClickSound() {
        try {
            // Load the sound file (replace "button_click.wav" with your sound file)
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:\\Users\\haardik.garg\\eclipse-workspace\\JLib\\src\\shelf.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }

    // Main method
    public static void main(String[] args) {
        String csvFilePath = "C:\\Users\\haardik.garg\\eclipse-workspace\\JLib\\src\\books.csv";
        LibraryFrame libraryFrame = new LibraryFrame(csvFilePath);
        libraryFrame.setVisible(true);
        libraryFrame.setResizable(false);
    }
}
