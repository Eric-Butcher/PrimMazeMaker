
public class CoordinatePair {
	
	private int x;
	private int y;
	
	public CoordinatePair(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int get_y()
	{
		return this.y;
	}
	
	public int get_x()
	{
		return this.x;
	}
	
	public void change_x(int n)
	{
		this.x = n;
	}
	
	public void change_y(int n)
	{
		this.y = n;
	}
	
	@Override
    public String toString()
    {
         return ("<CDP, X: " + this.x + " Y: " + this.y + " >");
    }
	
	

}
