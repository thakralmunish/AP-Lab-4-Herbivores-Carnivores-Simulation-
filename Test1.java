import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.StringTokenizer;

/** Class for buffered reading int and double values */
class Reader {
    static BufferedReader reader;
    static StringTokenizer tokenizer;

    /** call this method to initialize reader for InputStream */
    static void init(InputStream input) {
        reader = new BufferedReader(
                     new InputStreamReader(input) );
        tokenizer = new StringTokenizer("");
    }

    /** get next word */
    static String next() throws IOException {
        while ( ! tokenizer.hasMoreTokens() ) {
            //TODO add check for eof if necessary
            tokenizer = new StringTokenizer(
                   reader.readLine() );
        }
        return tokenizer.nextToken();
    }

    static int nextInt() throws IOException {
        return Integer.parseInt( next() );
    }
	
    static double nextDouble() throws IOException {
        return Double.parseDouble( next() );
    }
}

class Grassland {
	public String Name;
	public double x;
	public double y;
	public double Radius;
	public int GrassAvailable;
	
	public Grassland (String S, double x, double y, double r, int A) {
		this.Name = S;
		this.x = x;
		this.y = y;
		this.Radius = r;
		this.GrassAvailable = A;	
	}
}

abstract class Animal {
	public String Name;
	public int TimeInstance;
	public double Health;
	public double x;
	public double y;
	public double Distance;
	public int Turns;
	
	public Animal(String S, int T, double H, double x, double y) {
		this.Name = S;
		this.TimeInstance = T;
		this.Health = H;
		this.x = x;
		this.y = y;
		this.Distance = Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 0.5);
	}
	
	public void MoveXInDirection (double x, double y, double Distance) {
		double Perpendicular = y - this.y;
		double Base = x - this.x;
		double Hypotenuse = Math.pow(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2), 0.5);
		double tempx, tempy;
		if (x > this.x) {
			tempx = this.x + Distance * (Base / Hypotenuse);
		}
		else {
			tempx = this.x - Distance * (Base / Hypotenuse);
		}
		if (y > this.y) {
			tempy = this.y + Distance * (Perpendicular / Hypotenuse);
		}
		else {
			tempy = this.y - Distance * (Perpendicular / Hypotenuse);
		}
		this.x = Math.round(tempx);
		this.y = Math.round(tempy);
		this.Distance = Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 0.5);
	}
	
	public void MoveAwayFromX (double x, double y, double Distance) {
		MoveXInDirection(-x, -y, Distance);
	}
	
	public Animal NearestAnimal (Animal[] A, int Size, String S) {
		boolean mincheck = true;
		int k=0;
		double min = 0;
		if (S.equals("Herbivore")) {
			for (int i=0; i<Size; i++) {
				if (A[i].getClass().getName().equals("Herbivore")) {
					if (mincheck) {
						min = this.Distance(A[i].x, A[i].y);
						k = i;
						mincheck = false;
					}
					else if (this.Distance(A[i].x, A[i].y) < min) {
						min = this.Distance(A[i].x, A[i].y);
						k = i;
						mincheck = false;
					}
				}
			}
		}
		else if (S.equals("Carnivore")) {
			for (int i=0; i<Size; i++) {
				if (A[i].getClass().getName().equals("Carnivore")) {
					if (mincheck) {
						min = this.Distance(A[i].x, A[i].y);
						k = i;
						mincheck = false;
					}
					else if (this.Distance(A[i].x, A[i].y) < min) {
						min = this.Distance(A[i].x, A[i].y);
						k = i;
						mincheck = false;
					}
				}
			}
		}		
		return A[k];
		
	}
	
	public Grassland NearestGrassland (Grassland[] G, int Size) {
		double min = this.Distance(G[0].x, G[0].y);
		int k = 0;
		for (int i=1; i<Size; i++) {
			if (this.Distance(G[i].x, G[i].y) < min) {
				min = this.Distance(G[i].x, G[i].y);
				k = i;
			}
		}
		return G[k];
	}
	
	public Grassland NextNearestGrassland (Grassland[] GA, Grassland G) {		
		if (GA[0] == G) {
			return GA[1];
		}
		else {
			return GA[0];
		}
	}
	
	public boolean InsideGrassland (Grassland G) {		
		if (this.Distance(G.x, G.y) < G.Radius) {			
			return true;
		}
		return false;
	}
	
	public void Health (String S, double d) {
		if (S.equals("+")) {
			this.Health += d;
		}
		else if (S.equals("-")) {
			this.Health -= d;
		}
		else if (S.equals("*")) {
			this.Health *= d;
		}
		else {
			this.Health /= d;
		}
	}
	
	public double Distance(double x, double y) {
		return Math.pow(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2), 0.5);
	}
	
	public boolean CheckWhetherAlive() {
		if (this.Health <= 0) {
			System.out.println("It is Dead");
			System.out.println("");
			this.Health = 0;
			return false;
		}
		else {
			System.out.println("It's Health after taking the Turn is " + this.Health);
			System.out.println("");
			return true;
		}
	}
	
	public void UpdateTimeInstance (World W) {		
		Random A = new Random();
		int RandomNumber = A.nextInt(W.TotalTime - this.TimeInstance) + this.TimeInstance + 1;
		this.TimeInstance = RandomNumber;
	}
	
	public abstract void TakeTurn(World W);
	
	public abstract void SevenTurns(World W);
	
}

class Carnivore extends Animal {

	public Carnivore(String S, int T, int H, double x, double y) {
		super(S, T, H, x, y);
		// TODO Auto-generated constructor stub
	}
	
	public Animal NearHerbivore (Animal[] A, int Size, int Distance) {
		for (int i=0; i<Size; i++) {
			if (A[i].getClass().getName().equals("Herbivore")) {
				if (this.Distance(A[i].x, A[i].y) < Distance ){
					return A[i];
				}
			}
		}		
		return null;
	}
	
	public void KillHerbivore (Animal A) {
		this.Health("+", 2 * A.Health / 3);
		A.Health = 0;
	}

	@Override
	public void TakeTurn(World W) {
		// TODO Auto-generated method stub
		Grassland G = this.NearestGrassland(W.Grasslands, W.GrasslandsSize);
		if (W.NoOfHerbivores == 0) {
			// STAY AT POSITION			
		}
		else {
			Animal Hunt = NearHerbivore(W.Animals, W.AnimalsSize, 1);
			if ( Hunt != null ) {
				// KILL THE HERBIVORE
				this.KillHerbivore(Hunt);																				// NEAREST HARBIVORE STILL TO BE CHECKED
			}
			else {
				if (InsideGrassland(G)) {
					if (W.GenerateChance(92)) {
						// CARNIVORE WILL MOVE 4 UNITS IN DIRECTION TOWARDS POSITION OF NEAREST HERBIVORE
						Animal H = this.NearestAnimal(W.Animals, W.AnimalsSize, "Herbivore");
						this.MoveXInDirection(H.x, H.y, 4);
					}
					else {
						// STAY
						this.Health("-", 30);
					}
				}
				else {
					if (W.GenerateChance(25)) {
						// STAY INSIDE GRASSLAND
						this.Health("-", 60);
					}
					else {
						// CARNIVORE WILL MOVE 2 UNITS IN DIRECTION TOWARDS POSITION OF NEAREST HERBIVORE
						Animal H = this.NearestAnimal(W.Animals, W.AnimalsSize, "Herbivore");
						this.MoveXInDirection(H.x, H.y, 2);
					}
				}
			}
		}
		SevenTurns(W);
	}
	
	@Override
	public void SevenTurns(World W) {
		// TODO Auto-generated method stub
		if (this.NearHerbivore(W.Animals, W.AnimalsSize, 5) != null) {
			this.Turns = 0;
		}
		else {
			this.Turns += 1;			
			if (this.Turns > 7) {
				this.Health -= 5;
			}
		}
	}
	
}

class Herbivore extends Animal {
	public int GrassCapacity;
	
	public Herbivore(String S, int T, int H, double x, double y, int C) {
		super(S, T, H, x, y);
		this.GrassCapacity = C;
		// TODO Auto-generated constructor stub
	}
	
	public void EatGrass (Grassland G) {
		if (G.GrassAvailable > 0) {
			if (this.GrassCapacity > G.GrassAvailable ) {
				G.GrassAvailable = 0;
				this.Health = 1.2 * this.Health;
			}
			else {
				G.GrassAvailable -= this.GrassCapacity;
				this.Health = 1.5 * this.Health;
			}
		}
		else {
			// NO CHANGE IN HEALTH
		}
	}
	
	@Override
	public void TakeTurn(World W) {
		// TODO Auto-generated method stub
		Grassland G = this.NearestGrassland(W.Grasslands, W.GrasslandsSize); 
		if (W.NoOfCarnivores == 0) {
			if (InsideGrassland(G)) {
				if (W.GenerateChance(50)) {
					// GO TO NEXT NEAREST GRASSLAND BY 5 UNITS
					Grassland NNG = this.NextNearestGrassland(W.Grasslands, G);
					this.MoveXInDirection(NNG.x, NNG.y, 5);
					this.Health("-", 25);
				}
				else {
					// SSTAY AND EAT GRASS
					this.EatGrass(G);
				}
			}
			else {
				if (W.GenerateChance(50)) {
					// GO TO NEAREST GRASSLAND BY 5 UNITS
					this.MoveXInDirection(G.x, G.y, 5);
				}
				else {
					// STAY BUT NO GRASS TO EAT, SINCE IT IS OUTSIDE GRASSLAND
				}
			}
		}
		else {
			if (! InsideGrassland(G)) {
				if (W.GenerateChance(5)) {
					// STAY BUT NO GRASS TO EAT, SINCE IT IS OUTSIDE GRASSLAND
				}
				else {
					if (W.GenerateChance(65)) {
						// MOVE 5 UNITS IN DIRECTION OF CENTRE OF NEAREST GRASSLAND
						this.MoveXInDirection(G.x, G.y, 5);
					}
					else {
						// MOVE 4 UNITS AWAY FROM DIRECTION OF POSITION OF NEAREST CARNIVORE
						Animal C = this.NearestAnimal(W.Animals, W.AnimalsSize, "Carnivore");
						this.MoveAwayFromX(C.x, C.y, 4);
					}
				}
			}
			else {
				if (G.GrassAvailable >= this.GrassCapacity) {
					if (W.GenerateChance(90)) {
						// HERBIVORE STAYS AND EATS GRASS UP TO MAX CAPACITY OF ITSELF
						this.EatGrass(G);
					}
					else {
						if (W.GenerateChance(50)) {
							// HERBIVORE WILL MOVE 2 UNITS AWAY FROM POSITION OF NEAREST CARNIVORE
							Animal C = this.NearestAnimal(W.Animals, W.AnimalsSize, "Carnivore");
							this.MoveAwayFromX(C.x, C.y, 2);
							this.Health("-", 25);
						}
						else {
							// HERBIVORE WILL MOVE 3 UNITS IN THE DIRECTION OF NEXT NEAREST GRASSLAND
							Grassland NNG = this.NextNearestGrassland(W.Grasslands, G);
							this.MoveXInDirection(NNG.x, NNG.y, 5);
							this.Health("-", 25);
						}
					}
				}
				else {
					if (W.GenerateChance(20)) {
						// HERBIVORE WILL STAY, AND EAT THE GRASS AND FINISH THE WHOLE GRASSLAND
						this.EatGrass(G);
					}
					else {
						if (W.GenerateChance(70)) {
							// HERBIVORE WILL MOVE 4 UNITS AWAY FROM POSITION OF NEAREST CARNIVORE
							Animal C = this.NearestAnimal(W.Animals, W.AnimalsSize, "Carnivore");
							this.MoveAwayFromX(C.x, C.y, 4);
							this.Health("-", 25);
						}
						else {
							// HERBIVORE WILL MOVE 2 UNITS IN THE DIRECTION OF NEXT NEAREST GRASSLAND
							Grassland NNG = this.NextNearestGrassland(W.Grasslands, G);
							this.MoveXInDirection(NNG.x, NNG.y, 5);
							this.Health("-", 25);
						}
					}
				}
			}
		}
		SevenTurns(W);
	}

	@Override
	public void SevenTurns(World W) {
		// TODO Auto-generated method stub
		if (this.InsideGrassland(NearestGrassland(W.Grasslands, W.GrasslandsSize))) {
			this.Turns = 0;
		}
		else {
			this.Turns += 1;
			if (this.Turns > 7) {
				this.Health -= 5;
			}
		}
		
	}

}

class World {
	public int TotalTime;
	public Animal[] Animals;
	public Grassland[] Grasslands;
	public int AnimalsSize;
	public int GrasslandsSize;
	public int NoOfCarnivores;
	public int NoOfHerbivores;
	
	public World (int A) {
		this.TotalTime = A;
		this.AnimalsSize = 0;
		this.GrasslandsSize = 0;
		this.Grasslands = new Grassland[2];
		this.Animals = new Animal[4];
	}
	
	/*
	public void PrintGrasslandArray () {
		System.out.print("**XX**XX**XX**XX** GRASSLAND ARRAY --> ");
		for (int i=0; i<this.GrasslandsSize; i++) {
			System.out.print(this.Grasslands[i].Name + " : ");
		}
		System.out.println("");
	}
	
	public void PrintAnimalArray () {
		System.out.print("**XX**XX**XX**XX** ANIMAL ARRAY --> ");
		for (int i=0; i<this.AnimalsSize; i++) {
			System.out.print(this.Animals[i].Name + " : ");
		}
		System.out.println("");
	}
	*/
	
	public void AddGrassland (Grassland G) {
		this.Grasslands[this.GrasslandsSize] = G;
		this.GrasslandsSize += 1;
	}
	
	public void AddAnimal (Animal A) {
		int i = 0;
		boolean Added = false;
		if (A.getClass().getName().equals("Herbivore")) {
			this.NoOfHerbivores += 1;
		}
		else {
			this.NoOfCarnivores += 1;
		}
		if (this.AnimalsSize == 0) {
			AddAnimalAti(A, 0);
			Added = true;
		}
		else {
			for (i=0; i<this.AnimalsSize; i++) {
				if (Added) {
					break;
				}
				if (A.TimeInstance < this.Animals[i].TimeInstance) {
					AddAnimalAti(A,i);
					Added = true;
				}
				else if (A.TimeInstance == this.Animals[i].TimeInstance) {
					if (A.Health > Animals[i].Health) {
						AddAnimalAti(A,i);
						Added = true;
					}
					else if (A.Health < this.Animals[i].Health) {
						AddAnimalAti(A,i+1);
						Added = true;
					}
					else {
						if (A.getClass().getName().equals("Herbivore")) {
							if (this.Animals[i].getClass().getName().equals("Herbivore")) {
								if (A.Distance < this.Animals[i].Distance) {
									AddAnimalAti(A, i);
									Added = true;
								}
								else {									
									AddAnimalAti(A, i+1);
									Added = true;
								}
							}
							else {								
								AddAnimalAti(A, i);
								Added = true;
							}
						}
						else {
							if (this.Animals[i].getClass().getName().equals("Herbivore")) {
								AddAnimalAti(A, i+1);
								Added = true;
							}
							else {
								if (A.Distance < this.Animals[i].Distance) {
									AddAnimalAti(A, i);
									Added = true;
								}
								else {
									AddAnimalAti(A, i+1);
									Added = true;
								}
							}
						}
					}
				}
				else {
					continue;
				}
			}
			if (! Added) {
				AddAnimalAti(A, i);
			}
		}
	}
	
	public void AddAnimalAti (Animal A, int i) {
		if (this.AnimalsSize == 0) {
			this.Animals[0] = A;
			this.AnimalsSize += 1;
		}
		else {
			for (int j=this.AnimalsSize; j > i; j--) {
				this.Animals[j] = this.Animals[j-1];
			}
			this.Animals[i] = A;
			this.AnimalsSize += 1;
		}
	}
	
	public boolean GenerateChance(double L) {
		double c = L / 100.00;
		if (Math.random() < c ) {
			return true;
		}
		return false;
	}
	
	public Animal Dequeue() {
		Animal temp1 = this.Animals[0];
		for (int i = 0; i < this.AnimalsSize - 1; i++) {
			this.Animals[i] = this.Animals[i+1];
		}
		this.AnimalsSize -= 1;
		if (temp1.getClass().getName().equals("Herbivore")) {
			this.NoOfHerbivores -= 1;
		}
		else {
			this.NoOfCarnivores -= 1;
		}
		return temp1;
	}

	
}


public class Test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Reader.init(System.in);
		try {
			System.out.println("ENTER TOTAL FINAL TIME FOR SIMULATION - ");
			int TFT = Reader.nextInt();
			World World = new World(TFT);
			
			System.out.println("ENTER (X,Y) CENTRE, RADIUS AND GRASS AVAILABLE FOR FIRST GRASSLAND - ");
			double temp1 = Reader.nextDouble();
			double temp2 = Reader.nextDouble();
			double temp3 = Reader.nextDouble();
			int temp4 = Reader.nextInt();
			Grassland FirstGrassland = new Grassland ("FIRST GRASSLAND",temp1, temp2, temp3, temp4);
			
			System.out.println("ENTER (X,Y) CENTRE, RADIUS AND GRASS AVAILABLE FOR SECOND GRASSLAND - ");
			temp1 = Reader.nextDouble();
			temp2 = Reader.nextDouble();
			temp3 = Reader.nextDouble();
			temp4 = Reader.nextInt();
			Grassland SecondGrassland = new Grassland ("SECOND GRASSLAND", temp1, temp2, temp3, temp4);
			
			System.out.println("ENTER HEALTH AND GRASS CAPACITY FOR HERBIVORES - ");
			int temp8 = Reader.nextInt();
			int temp5 = Reader.nextInt();
			
			System.out.println("ENTER (X,Y) POSITION AND TIMESTAMP FOR FIRST HERBIVORE - ");
			temp1 = Reader.nextDouble();
			temp2 = Reader.nextDouble();
			int temp6 = Reader.nextInt();
			Herbivore FirstHerbivore = new Herbivore ("First Herbivore", temp6, temp8, temp1, temp2, temp5);
			
			System.out.println("ENTER (X,Y) POSITION AND TIMESTAMP FOR SECOND HERBIVORE - ");
			temp1 = Reader.nextDouble();
			temp2 = Reader.nextDouble();
			temp6 = Reader.nextInt();
			Herbivore SecondHerbivore = new Herbivore ("Second Herbivore",temp6, temp8, temp1, temp2, temp5);
			
			System.out.println("ENTER HEALTH FOR CARNIVORES - ");
			temp8 = Reader.nextInt();
			
			System.out.println("ENTER (X,Y) POSITION AND TIMESTAMP FOR FIRST CARNIVORE - ");
			temp1 = Reader.nextDouble();
			temp2 = Reader.nextDouble();
			temp6 = Reader.nextInt();
			Carnivore FirstCarnivore = new Carnivore ("First Carnivore",temp6, temp8, temp1, temp2);
			
			System.out.println("ENTER (X,Y) POSITION AND TIMESTAMP FOR SECOND CARNIVORE - ");
			temp1 = Reader.nextDouble();
			temp2 = Reader.nextDouble();
			temp6 = Reader.nextInt();
			Carnivore SecondCarnivore = new Carnivore ("Second Carnivore",temp6, temp8, temp1, temp2);
			
			World.AddAnimal(FirstHerbivore);
			World.AddAnimal(SecondHerbivore);
			World.AddAnimal(FirstCarnivore);
			World.AddAnimal(SecondCarnivore);
			World.AddGrassland(FirstGrassland);
			World.AddGrassland(SecondGrassland);
			
			System.out.println("The Simulation Begins - ");
			
			int time = 0;
			while (time < World.TotalTime && World.AnimalsSize != 0 ) {
				Animal A = World.Dequeue();
				time = A.TimeInstance;
				if (time >= World.TotalTime) {
					break;
				}
				System.out.println("It is " + A.Name);
				A.TakeTurn(World);
				A.UpdateTimeInstance(World);
				if (A.CheckWhetherAlive()) {
					World.AddAnimal(A);
				}
				else {
					continue;
				}
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}