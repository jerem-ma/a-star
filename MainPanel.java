import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

public class MainPanel extends JPanel implements MouseInputListener
{
	private int[][] cells = new int[50][50];
	private Coord caseDepart = null;
	private ArrayList<Coord> path = null;

	private Mode mode = Mode.OBSTACLE;

	public MainPanel()
	{
		super();
		this.setBackground(Color.BLUE);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		drawCells(g);
		drawPath(g);
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (this.mode != Mode.OBSTACLE)
			return;

		Coord pos = getPosFromRealPos(e.getX(), e.getY());

		int x = pos.x;
		int y = pos.y;

		if (x >= this.cells.length || x < 0 || y >= this.cells[0].length || y < 0)
			return;

		if (SwingUtilities.isLeftMouseButton(e))
			cells[x][y] = 1;
		else if (SwingUtilities.isRightMouseButton(e))
			cells[x][y] = 0;

		this.repaint();
	}

	private Coord getPosFromRealPos(int xReal, int yReal)
	{
		double widthCell = this.getWidth()/this.cells.length;
		double heightCell = this.getHeight()/this.cells[0].length;

		int x = (int) (xReal/widthCell);
		int y = (int) (yReal/heightCell);

		return new Coord(x, y);
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		if (this.caseDepart == null || this.mode != Mode.A)
			return;

		Coord pos = getPosFromRealPos(e.getX(), e.getY());

		this.path = getPath(caseDepart, pos);

		this.repaint();
	}

	private ArrayList<Coord> getPath(Coord A, Coord B)
	{
		int width = this.cells.length;
		int height = this.cells[0].length;

		Case[][] cases = getCasesArray();

		PriorityQueue<Case> openList = new PriorityQueue<Case>(11, (a, b) -> {return a.estimation - b.estimation;});

		boolean[][] visited = new boolean[width][height];
		HashMap<Case, Case> mapPath = new HashMap<Case, Case>();

		/* Début de l'algo A* */
		Case ACase = new Case();
		ACase.coord = A;
		ACase.cout = 0;
		ACase.estimation = Case.heuristique(A, B);
		openList.add(ACase);

		boolean found = false;

		while (!openList.isEmpty())
		{
			Case current = openList.poll();
			if (current.coord.equals(B))
			{
				found = true;
				break;
			}

			visited[current.coord.x][current.coord.y] = true;

			Coord[] adjCases = {new Coord(current.coord.x+1, current.coord.y),
													new Coord(current.coord.x-1, current.coord.y),
													new Coord(current.coord.x, current.coord.y+1),
													new Coord(current.coord.x, current.coord.y-1)};

			for (Coord adjCase : adjCases)
			{
				if (adjCase.x >= visited.length || adjCase.x < 0 ||
						adjCase.y >= visited[adjCase.x].length || adjCase.y < 0)
					continue;

				if (visited[adjCase.x][adjCase.y] || cells[adjCase.x][adjCase.y] == 1) // Case visitée ou obstacle
					continue;

				Case tmpCase = new Case();
				tmpCase.coord = adjCase;

				if (openList.contains(tmpCase))
				{
					Case realCase = null;

					for (Case testCase : openList)
					{
						if (tmpCase.equals(testCase))
							realCase = tmpCase;
					}

					if (current.cout+1 < realCase.cout)
					{
						realCase.cout = current.cout + 1;
						realCase.estimation = realCase.cout + Case.heuristique(tmpCase.coord, B);
						mapPath.put(realCase, current);
					}
				}

				else
				{
					tmpCase.cout = current.cout+1;
					tmpCase.estimation = tmpCase.cout + Case.heuristique(tmpCase.coord, B);
					mapPath.put(tmpCase, current);
					openList.add(tmpCase);
				}
			}
		}

		if (!found)
			return null;

		else
		{
			ArrayList<Coord> path = new ArrayList<Coord>();

			Case tmpCase = new Case();
			tmpCase.coord = B;

			while (!tmpCase.coord.equals(A))
			{
				path.add(tmpCase.coord);
				tmpCase = mapPath.get(tmpCase);
			}

			path.add(A);

			return path;
		}
	}

	private Case[][] getCasesArray()
	{
		int width = this.cells.length;
		int height = this.cells[0].length;
		Case[][] cases = new Case[width][height];

		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				Case casse = new Case();
				casse.coord = new Coord(x, y);

				casse.cout = Integer.MAX_VALUE;
				casse.estimation = Integer.MAX_VALUE;

				cases[x][y] = casse;
			}
		}

		return cases;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (this.mode != Mode.A)
			return;

		if (SwingUtilities.isRightMouseButton(e))
		{
			this.caseDepart = null;
			return;
		}

		if (!SwingUtilities.isLeftMouseButton(e))
			return;

		Coord pos = getPosFromRealPos(e.getX(), e.getY());
		
		int x = pos.x;
		int y = pos.y;

		if (x >= cells.length || y >= cells[0].length || x < 0 || y < 0)
			return;

		this.caseDepart = new Coord(x, y);
	}

	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}

	public void setMode(Mode mode)
	{
		this.mode = mode;
	}

	private void drawCells(Graphics g)
	{
		g.setColor(Color.BLACK);

		int widthCell = this.getWidth()/cells.length;
		int heightCell = this.getHeight()/cells[0].length;

		for (int x = 0; x < cells.length; x++)
		{
			for (int y = 0; y < cells[x].length; y++)
			{
				if (cells[x][y] == 1)
				{
					int xReal = x*widthCell;
					int yReal = y*heightCell;
					g.fillRect(xReal, yReal, widthCell, heightCell);
				}
			}
		}
	}

	private void drawPath(Graphics g)
	{
		if (this.path == null)
			return;

		int widthRect = this.getWidth()/cells.length;
		int heightRect = this.getHeight()/cells[0].length;

		g.setColor(Color.ORANGE);

		for (Coord coord : path)
		{
			int x = coord.x;
			int y = coord.y;

			int xRect = x * widthRect;
			int yRect = y * heightRect;

			g.fillRect(xRect, yRect, widthRect, heightRect);
		}	
	}
}
