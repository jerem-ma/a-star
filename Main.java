import java.awt.*;

import javax.swing.*;

public class Main
{
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();

		frame.setSize(500, 700);
		frame.setLocation(0, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;

		c.weighty = 0.95;
		c.gridy = 0;
		JPanel mainPanel = new MainPanel();
		frame.add(mainPanel, c);

		JPanel controlPanel = new ControlPanel((MainPanel) mainPanel);
		c.weighty = 0.05;
		c.gridy = 1;
		frame.add(controlPanel, c);

		frame.setVisible(true);
	}
}
