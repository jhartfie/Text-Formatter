// Justin Hartfield



import java.io.File;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.text.*;
import java.io.PrintWriter;

/**
 * A Homescreen is the home screen of the Text Formatter applet
 * From the homescreen, one can input a file, see the formatted output
 * as well as choose right justification and/or show analysis.
 */
public class Homescreen extends JPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel analysisPanel; // This panel holds the textfields and is added to the Homescreen

    private JButton input;
    private JButton output;

    private JCheckBox rightJustify;
    private JCheckBox showAnalysis;

    private JTextField txtWordsProcessed;
    private JTextField txtLines;
    private JTextField txtLinesRemoved;
    private JTextField txtAverageWordsline;
    private JTextField txtAverageLineLength;

    private JFileChooser fileChooser;

    private int wordsProcessed = 0;
    private int totalLines = 0;
    private int linesRemoved = 0;
    private double avgWordsLine = 0.0;
    private double avgLineLength = 0.0;

    private final int maxLineLength = 80; // Max of 80 characters per line

    private File selectedFile = null;  // Used to hold the location of the file. First will be found when pressing
                                       // input button and then manipulated when pressing output button

    private boolean inputFileChosen = false;
    private boolean outputButtonPressed = false;

    private JFrame outputWindow = null;



    /**
     * Constructor: Arranges all components neatly.
     */
    public Homescreen()
    {
        // Buttons
        input = new JButton ("Input");
        input.addActionListener(new ButtonListener());
        input.setBounds(70,6,117,29);
        output = new JButton ("Output");
        output.addActionListener(new ButtonListener());
        output.setBounds(239,6,117,29);

        // Check Boxes
        rightJustify = new JCheckBox ("Right Justification");
        rightJustify.addItemListener(new CheckBoxListener());
        rightJustify.setBounds(70,47,145,23);
        showAnalysis = new JCheckBox ("Show Analysis");
        showAnalysis.addItemListener(new CheckBoxListener());
        showAnalysis.setBounds(228,47,128,23);

        // Text Fields
        txtWordsProcessed = new JTextField("Words Processed:");
        txtWordsProcessed.setEditable(false);
        txtWordsProcessed.setColumns(15);
        txtLines = new JTextField("Lines:");
        txtLines.setEditable(false);
        txtLines.setColumns(15);
        txtLinesRemoved = new JTextField("Lines Removed:");
        txtLinesRemoved.setEditable(false);
        txtLinesRemoved.setColumns(15);
        txtAverageWordsline = new JTextField("Average words/line:");
        txtAverageWordsline.setEditable(false);
        txtAverageWordsline.setColumns(15);
        txtAverageLineLength = new JTextField("Average line length:");
        txtAverageLineLength.setEditable(false);
        txtAverageLineLength.setColumns(15);

        // File Chooser
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Please choose an input text file");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files","txt");
        fileChooser.setAcceptAllFileFilterUsed(false); // Get rid of All File option
        fileChooser.addChoosableFileFilter(filter);    // Only allow .txt files to be opened

        // Setup Analysis Panel
        analysisPanel = new JPanel();
        analysisPanel.setBounds(101,82,235,155);
        analysisPanel.add(txtWordsProcessed);
        analysisPanel.add(txtLines);
        analysisPanel.add(txtLinesRemoved);
        analysisPanel.add(txtAverageWordsline);
        analysisPanel.add(txtAverageLineLength);

        // Setup Homescreen
        setLayout(null); // Absolute positioning, no layout used
        add(input);  // Adding button
        add(output); // Adding button
        add(rightJustify);  // Adding checkmark box
        add(showAnalysis);  // Adding checkmark box
        add(analysisPanel); // Adding analysis panel with text fields
    }



    /**
     * CheckBoxListener defines an action when a checkbox is
     * selected or unselected
     */
    private class CheckBoxListener implements ItemListener
    {
        /**
         * It sets up show analyses on the homescreen when clicked
         */
        public void itemStateChanged(ItemEvent event)
        {
            avgWordsLine = ((double) wordsProcessed) / totalLines; // Calculate avgWordsLine
            DecimalFormat df = new DecimalFormat("0.00");

            // SHOW ANALYSIS CHECKBOX SELECTED
            if (showAnalysis.isSelected() && outputButtonPressed)                  // Do not display analyses until output button has been pressed
            {                                                                      // at least once.
                txtWordsProcessed.setText("Words Processed: " + wordsProcessed);
                txtLines.setText("Lines: " + totalLines);
                txtLinesRemoved.setText("Lines Removed: " + linesRemoved);
                txtAverageWordsline.setText("Average words/line: " + df.format(avgWordsLine));
                txtAverageLineLength.setText("Average line length: " + df.format(avgLineLength));
            }
            else
            {
                txtWordsProcessed.setText("Words Processed:");
                txtLines.setText("Lines:");
                txtLinesRemoved.setText("Lines Removed:");
                txtAverageWordsline.setText("Average words/line:");
                txtAverageLineLength.setText("Average line length:");
            }
        }
    }



    /**
     * ButtonListener defines an action when a button is
     * selected
     */
    private class ButtonListener implements ActionListener
    {
        /**
         * It either opens a file chooser to choose an input file
         * or it performs the analyses and displays it in an output file.
         */
        public void actionPerformed(ActionEvent event)
        {

            // INPUT BUTTON SELECTED
            if (event.getSource() == input)
            {
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)   // User selected a file (i.e. didn't close the file chooser)
                {
                    selectedFile = fileChooser.getSelectedFile();
                    inputFileChosen = true; // output button will now work
                }
            }


            // OUTPUT BUTTON SELECTED
            if (event.getSource() == output && inputFileChosen) // Output Button will only work after input file has been chosen
            {
                // Reset analysis variables from previous button press
                if (outputButtonPressed)    // Don't reset values first time ouput button is pressed since they are already initialized to 0
                {
                    wordsProcessed = 0;
                    totalLines = 0;
                    linesRemoved = 0;
                    avgWordsLine = 0.0;
                    avgLineLength = 0.0;
                }

                outputButtonPressed = true; // Used for checkbox "Show Analyses". See code.

                try
                {
                    if (outputWindow != null)      // If outputWindow is already open dispose of it before new one is created
                    {                              // Only keep one outputWindow open at a time.
                        outputWindow.dispose();
                    }

                    outputWindow = new JFrame("Output");   // New frame for the output
                    outputWindow.setBounds(450,0,450,300);

                    JTextArea textAreaNoScroll = new JTextArea(); // Text Area will be put into new frame
                    textAreaNoScroll.setEditable(false);

                    if (rightJustify.isSelected()) // Set orientation to right if selected, otherwise default left
                    {
                        textAreaNoScroll.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                    }

                    JScrollPane textAreaScroll = new JScrollPane(textAreaNoScroll); // Change TextArea to have a scroll bar
                    outputWindow.add(textAreaScroll, BorderLayout.CENTER);          // Add textAreaScroll to the frame

                    outputWindow.setVisible(true); // Make frame visible

                    int charactersLine = 0;         // Current number of characters counted in from file, resets at end of each line
                    int totalCharacters = 0;        // Current number of characters counted in from file, does not reset
                    boolean lineCounted = false;    // Used to count each line only once in file, for the totalLines analysis statistic


                    Scanner scanner = new Scanner(selectedFile);    // Count blank lines in file including lines with whitespace in them
                    while (scanner.hasNextLine())
                    {
                        if (scanner.nextLine().trim().isEmpty())
                        {
                            linesRemoved++;
                        }
                    }

                    scanner = new Scanner(selectedFile);           // Reset scanner to top of file
                    PrintWriter writer = new PrintWriter("FormattedOutput.txt","UTF-8");

                    while (scanner.hasNext())
                    {
                            String word = scanner.next();
                            wordsProcessed++;

                            if (charactersLine == 0 && word.length() >= maxLineLength)//First word size is greater than max amount of characters per line
                            {
                                textAreaNoScroll.append(word + "\n");
                                writer.println(word);
                                totalLines++;
                                totalCharacters += word.length();
                            }
                            else
                            {
                                if (word.length() + charactersLine < maxLineLength)
                                {
                                    if (!lineCounted)
                                    {
                                        totalLines++;
                                        lineCounted = true;
                                    }

                                    textAreaNoScroll.append(word + " ");
                                    writer.print(word + " ");
                                    charactersLine += word.length() + 1;
                                    totalCharacters += word.length() + 1;
                                }
                                else if(word.length() + charactersLine == maxLineLength)
                                {
                                    textAreaNoScroll.append(word + "\n");
                                    writer.println(word);
                                    charactersLine = 0; // reset character count

                                    totalCharacters += word.length();
                                    lineCounted = false;  // Reset lineCounted for next line
                                }
                                else // word.length() + charactersLine > maxLineLength
                                {
                                    textAreaNoScroll.append("\n" + word + " ");
                                    writer.println();
                                    writer.print(word + " ");
                                    charactersLine = word.length() + 1; // reset character count to first word plus space

                                    totalCharacters += word.length() + 1;
                                    totalLines++;
                                }
                            }
                    }
                    avgLineLength = ((double) totalCharacters) / totalLines;
                    writer.close();
                }
                catch (Exception e)
                {
                    System.out.println("Exception Thrown: " + e);
                }
            }
        }
    }
}