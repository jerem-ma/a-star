public class Case
{
	Coord coord;

	int cout;
	int estimation;

	public static int heuristique(Coord C, Coord G)
	{
		return Math.abs(C.x - G.x) + Math.abs(C.y - G.y); 
	}

	@Override
	public int hashCode()
	{
		return this.coord.hashCode();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Case))
			return false;

		Case compared = (Case) o;

		return this.coord.equals(compared.coord);
	}
}
