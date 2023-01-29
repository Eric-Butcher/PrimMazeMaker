import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Maze {
	
	private String[][] grid;
	private int size;
	
	// Only works on UNIX
//	public static final String ANSI_RESET = "\u001B[0m";
//    public static final String ANSI__BACK_YELLOW = "\\u001B[43m";
//    public static final String ANSI_BACK_RED = "\\u001B[41m";
//    public static final String ANSI_BLUE = "\\u001B[34m";
//
//	private static final String path = (ANSI_BLUE + "p" + ANSI_RESET);	
//	private static final String wall = (ANSI_BACK_RED + "w" + ANSI_RESET); 	// Part of the maze that one cannot pass
//	private static final String unvisited = (ANSI__BACK_YELLOW + "u" + ANSI_RESET);		// A block is a coordinate on the grid that has not been visited yet by the algo
	
	
	// For Windows
	private static final String path = ("p");	
	private static final String wall = ("w"); 
	private static final String unvisited = ("u");	

	
	
	public Maze(int s)
	{
		
		// Create instance variables
		this.size = s;
		this.grid = new String[size][size];
		
		// 1. Start with a maze full of walls
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				this.grid[i][j] = unvisited;
			}
		}
		
		// 2. Pick a cell, mark it as part of the maze. Add the walls of the cell to the wall list.
		ArrayList<CoordinatePair> walls = new ArrayList<CoordinatePair>();
		
		int start_x = getStartCoordinate(size);
		int start_y = getStartCoordinate(size);
		
		
		this.grid[start_y][start_x] = path;
		
		// Add the touching spaces of that point to the walls list
		walls.add(new CoordinatePair(start_x-1, start_y));
		walls.add(new CoordinatePair(start_x, start_y-1));
		walls.add(new CoordinatePair(start_x, start_y+1));
		walls.add(new CoordinatePair(start_x+1, start_y));
		
		// Make the maze show these as walls
		this.grid[start_y+1][start_x] = wall;
		this.grid[start_y-1][start_x] = wall;
		this.grid[start_y][start_x+1] = wall;
		this.grid[start_y][start_x-1] = wall;
		
		// 3. While there are walls in the list: 
		int tracker = 0;
		
		while (walls.size() > 0)
		{
			
			
			// 3a. Pick a random wall from the list, making sure that wall is not an edge.. If only one of the cells that the wall divides is visited, then:
			CoordinatePair rand_wall = walls.get(getRandWallIndex(walls.size()));
			
			int ry = rand_wall.get_y();
			int rx = rand_wall.get_x();
			
		
		
	
			// Check if there is a path to the right of the wall
			if ( (rx != 0) && (rx != (this.size-1)) && (this.grid[ry][rx+1] == path) && (this.grid[ry][rx-1] == unvisited) )
			{
				if(validNumSorrounding(rx, ry))
				{
					// Make the path
					this.grid[ry][rx] = path;
					
					// Make every cell around the new path a wall if it is not a path, and add that new wall to walls
					addNewWalls(rx, ry, walls);
					
					// Delete wall
					eliminate(walls, rand_wall);
					continue;
					
				}
			}
			
			// Check if there is a path to the top of the wall
			if ( (ry != (this.size-1)) && (ry != 0) && (this.grid[ry-1][rx] == path) && (this.grid[ry+1][rx] == unvisited))
			{
				if(validNumSorrounding(rx, ry))
				{
					// Make the path
					this.grid[ry][rx] = path;
					
					// Make every cell around the new path a wall if it is not a path, and add that new wall to walls
					addNewWalls(rx, ry, walls);
					
					// Delete wall
					eliminate(walls, rand_wall);
					continue;
				}
			}
			
			// Check if there is a path to the left of the wall
			if ( (rx != 0) && (rx != (this.size-1)) && (this.grid[ry][rx-1] == path) && (this.grid[ry][rx+1] == unvisited) )
			{
				if(validNumSorrounding(rx, ry))
				{
					// Make the path
					this.grid[ry][rx] = path;
					
					// Make every cell around the new path a wall if it is not a path, and add that new wall to walls
					addNewWalls(rx, ry, walls);
					
					// Delete wall
					eliminate(walls, rand_wall);
					continue;
				}
			}
			
			// Check if there is a path to the bottom of the wall
			if (  (ry != 0) && (ry != (this.size-1)) && (this.grid[ry+1][rx] == path) && (this.grid[ry-1][rx] == unvisited) )
			{
				if(validNumSorrounding(rx, ry))
				{
					// Make the path
					this.grid[ry][rx] = path;
					
					// Make every cell around the new path a wall if it is not a path, and add that new wall to walls
					addNewWalls(rx, ry, walls);
					
					// Delete wall
					eliminate(walls, rand_wall);
					continue;
				}
			}
			
			// 3b. Remove the wall from the list.
			eliminate(walls, rand_wall);
		}
		
		// Set entrance and exit
		for (int i = 0; i < this.size; i++)
		{
			if (this.grid[1][i] == path)
			{
				this.grid[0][i] = path;
				break;
			}
		}
		
		for (int i = (this.size -1); i >= 0; i--)
		{
			if (this.grid[this.size-2][i] == path)
			{
				this.grid[this.size-1][i] = path;
				break;
			}
		}
		
		
		// Mark all of the reaming unvisited cells as walls
		for (int i = 0; i < this.size; i++)
		{
			for (int j = 0; j < this.size; j++)
			{
				if (this.grid[i][j] == unvisited)
				{
					this.grid[i][j] = wall;
				}
			}
		}
		
		
		
		
	}
	
	public void printMaze()
	{
		for (int i = 0; i < this.size; i++)
		{
			for (int j = 0; j < this.size; j++)
			{
				System.out.print(this.grid[i][j] + " ");
			}
			System.out.println();
		}
		return;
	}
	

	
	public int getStartCoordinate(int max)
	{
		int min = 1;
	    int retVal = (int) Math.random() * (max - min + 1) + min;
	    return retVal;
	}
	
	public int getRandWallIndex(int len)
	{
		int index = (int)(Math.random() * len);
		return index;
	}
	
	public boolean validNumSorrounding(int rx, int ry)
	{
		int num = 0;
		if (this.grid[ry-1][rx] == path)
		{
			num++;
		}
		if (this.grid[ry+1][rx] == path)
		{
			num++;
		}
		if (this.grid[ry][rx-1] == path)
		{
			num++;
		}
		if (this.grid[ry][rx+1] == path)
		{
			num++;
		}
		
		if (num < 2)
		{
			return true;
		}
		return false;
	}
	
	public void addNewWalls(int rx, int ry, ArrayList<CoordinatePair> walls)
	{
		for (int rxbump = -1; rxbump <=1; rxbump++)
		{
			for (int rybump = -1; rybump <= 1; rybump++)
			{
				if (Math.abs(rxbump) == Math.abs(rybump))
				{
					continue;
				} 
				else 
				{
					try {
						if (this.grid[ry + rybump][rx + rxbump] != path)
						{
							this.grid[ry + rybump][rx + rxbump] = wall;
							CoordinatePair cd = new CoordinatePair(rx + rxbump, ry + rybump);
							// Adds pair to the walls list in not currently in the list
							eliminate(walls, cd);
							walls.add(cd);
							
							
						}
					} catch (ArrayIndexOutOfBoundsException e)
					{
						continue;
					}
				}
			}
		}
		return;
	}
	
	public void eliminate(ArrayList<CoordinatePair> walls, CoordinatePair wall)
	{
		int xval = wall.get_x();
		int yval = wall.get_y();

		
		for (int i = 0; i < walls.size(); i++)
		{
			if ((walls.get(i).get_x() == xval) && (walls.get(i).get_y() == yval))
			{
				walls.remove(i);
				i--;
			}
		}

		return;
	}

	public static void main(String[] args) {
		
		// Get input from the user for size
		Scanner input = new Scanner(System.in);
		boolean validInput = false;
		int num = 0;
		
		do 
		{
			System.out.print("Please type an integer >7 and <100: ");
			String answer = input.nextLine();
			
			try {
				
				num = Integer.parseInt(answer);
				
				if ((num >= 7 ) && (num <= 100))
				{
					validInput = true;
					break;
				}
			} catch (NumberFormatException n) {
				System.out.println("Please type a valid integer. ");
			}
		} while(validInput == false);
			
		
		
		input.close();
		
		System.out.println();
		System.out.println("Maze: ");
		System.out.println();
		
		// Make a maze with size input
		Maze maze = new Maze(num);
		maze.printMaze();

	}

}
