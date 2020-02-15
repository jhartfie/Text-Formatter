// Justin Hartfield

import javax.swing.*;

/**
 * TextFormatter creates a frame which holds the Homescreen
 * The Homescreen has "Input" and "Output" buttons
 * It also contains "Right Justification" and
 * "Show Analysis" check boxes and text fields
 * to display the analyses.
 */
public class TextFormatter
{
        public static void main(String[] args)
        {
            JFrame frame = new JFrame();
            Homescreen home = new Homescreen();

            frame.setSize(450,300);
            frame.add(home);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
}
