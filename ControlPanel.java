import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class ControlPanel extends JPanel
{
	private MainPanel mainPanel;

	public ControlPanel(MainPanel mainPanel)
	{
		super();
		this.mainPanel = mainPanel;

		this.setBackground(Color.green);

		JRadioButton obstacles = new JRadioButton("Obstacles");
		obstacles.setSelected(true);
		obstacles.addActionListener((ActionEvent ae) -> {this.mainPanel.setMode(Mode.OBSTACLE);});

		JRadioButton A = new JRadioButton("A*");
		A.addActionListener((ActionEvent ae) -> {this.mainPanel.setMode(Mode.A);});

		ButtonGroup group = new ButtonGroup();
		group.add(obstacles);
		group.add(A);

		this.add(obstacles);
		this.add(A);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}
}
