public class Coord
{
	int x;
	int y;

	public Coord(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode()
	{
		return 1000*x + y;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Coord))
			return false;

		Coord compared = (Coord) o;

		return this.x == compared.x && this.y == compared.y;
	}
}
