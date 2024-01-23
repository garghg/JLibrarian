/*
 * References:
 * 1. https://docs.oracle.com/javase/8/docs/api/javax/swing/JTable.html#:~:text=The%20JTable%20is%20used%20to,and%20examples%20of%20using%20JTable%20.
 * 2. https://teams.microsoft.com/l/message/48:notes/1705608936805?context=%7B%22contextType%22%3A%22chat%22%7D
 * 3. https://teams.microsoft.com/l/message/48:notes/1705598903937?context=%7B%22contextType%22%3A%22chat%22%7D
 * 4. https://teams.microsoft.com/l/message/48:notes/1705597296931?context=%7B%22contextType%22%3A%22chat%22%7D
 * 5. https://teams.microsoft.com/l/message/48:notes/1705587174947?context=%7B%22contextType%22%3A%22chat%22%7D
 * 6. https://teams.microsoft.com/l/message/48:notes/1705586994001?context=%7B%22contextType%22%3A%22chat%22%7D
 * 7. Dataset (With additional random prices data added by me): https://www.kaggle.com/datasets/dylanjcastillo/7k-books-with-metadata
 * 8. https://chat.openai.com/auth/login
 * 9. https://stackoverflow.com/questions/1614772/how-to-change-jframe-icon
*/

// Import necessary packages
package library;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

// Main class
public class Main {
    // Main method, the entry point of the program
    public static void main(String[] args) {
        // Call the method to create and show the GUI
        createAndShowGUI();
    }

    // Method to create and show the main menu GUI
    private static void createAndShowGUI() {
        // Create a JFrame (main window) with a title
        JFrame frame = new JFrame("Main Menu");
        // Set the default close operation to exit the program when the window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a JLayeredPane to manage the layering of components
        JLayeredPane layeredPane = new JLayeredPane();
        // Set the layout manager to null for absolute positioning
        layeredPane.setLayout(null);

        // Load and set a background image for the main menu
        ImageIcon backgroundImage = new ImageIcon("C:\\Users\\haardik.garg\\eclipse-workspace\\JLib\\src\\menu.png");
        JLabel background = new JLabel(backgroundImage);
        background.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());

        // Create a JButton for opening the full catalogue
        JButton enterLibraryButton = new JButton("Open Catalogue");
        // Add an ActionListener to handle button clicks
        enterLibraryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Play a sound when the button is clicked
                playButtonClickSound();
                // Call the method to open the library when the button is clicked
                openLibrary(frame);
            }
            
        });
        

        // Set button background and foreground colors
        enterLibraryButton.setBackground(Color.BLACK);
        enterLibraryButton.setForeground(Color.WHITE);

        // Disable focus painting for a cleaner look
        enterLibraryButton.setFocusPainted(false);

        // Set button position and size
        int buttonWidth = 150;
        int buttonHeight = 30;
        int xPosition = 240;
        int yPosition = 300;
        enterLibraryButton.setBounds(xPosition, yPosition, buttonWidth, buttonHeight);
        
		 // Create a JLabel for additional text under the button
		 JLabel text = new JLabel("(Double click any book to view more details!)");
		 // Set the font and foreground color for the text
		 text.setFont(new Font("Arial", Font.ITALIC, 14));
		 text.setForeground(Color.BLACK);
		
		 // Set text position and size
		 int textWidth = 300;
		 int textHeight = 100;
		 int textXPosition = xPosition-60;  // Same X position as the button
		 int textYPosition = yPosition + buttonHeight - 15;  // Adjusted Y position under the button
		 text.setBounds(textXPosition, textYPosition, textWidth, textHeight);

        // Add components to the layered pane with specified layers
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(enterLibraryButton, JLayeredPane.PALETTE_LAYER);
        // Add the text label to the layered pane
        layeredPane.add(text, JLayeredPane.PALETTE_LAYER);

        // Set the content pane of the frame to the layered pane
        frame.setContentPane(layeredPane);

        // Set frame size based on the background image size
        frame.setSize(backgroundImage.getIconWidth(), backgroundImage.getIconHeight() + 50);
        // Disable frame resizing
        frame.setResizable(false);
        // Center the frame on the screen
        frame.setLocationRelativeTo(null);
        //set icon image for frame
        ImageIcon img = new ImageIcon("C:\\Users\\haardik.garg\\eclipse-workspace\\JLib\\src\\icon.png");
        frame.setIconImage(img.getImage());
        // Make the frame visible
        frame.setVisible(true);
    }
    
    // Method to play a sound when the button is clicked
    private static void playButtonClickSound() {
        try {
            // Load the sound file (replace "button_click.wav" with your sound file)
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:\\Users\\haardik.garg\\eclipse-workspace\\JLib\\src\\page.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }

    // Method to open the library and hide the main menu frame
    private static void openLibrary(JFrame mainMenuFrame) {
        // Hide the main menu frame
        mainMenuFrame.setVisible(false);

        // Specify the CSV file path for the library
        String csvFilePath = "C:\\Users\\haardik.garg\\eclipse-workspace\\JLib\\src\\books.csv";
        // Create a new LibraryFrame and make it visible
        LibraryFrame libraryFrame = new LibraryFrame(csvFilePath);
        libraryFrame.setVisible(true);
    }
}
