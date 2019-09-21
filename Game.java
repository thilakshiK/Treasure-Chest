import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



class Binocular {
   
    public int searchLotus(int xPoint , int yPoint, Grid lakeNozama) {
        
	if (xPoint != 0 && lakeNozama.grid[xPoint - 1][yPoint][0] instanceof Lotus  && ((Lotus)lakeNozama.grid[xPoint - 1][yPoint][0]).hasPetals()) {
            return 0;
        } else if (xPoint != 10  && lakeNozama.grid[xPoint+1][yPoint][0] instanceof Lotus && ((Lotus)lakeNozama.grid[xPoint+1][yPoint][0]).hasPetals()) {
            return 1;
        } else if (yPoint != 0 && lakeNozama.grid[xPoint][yPoint-1][0] instanceof Lotus && ((Lotus)lakeNozama.grid[xPoint][yPoint-1][0]).hasPetals()){
            return 2;
        } else if (yPoint != 10 && lakeNozama.grid[xPoint][yPoint +1][0] instanceof Lotus && ((Lotus)lakeNozama.grid[xPoint][yPoint+1][0]).hasPetals()) {
            return 3;
        } else {
            return 4;
        }
    }
}


class Fin {

}

class Node {
    
    private Object object;
    private int xPoint;
    private int yPoint;
    private ArrayList<String> warriorRoute = new ArrayList<String>();
    
    public Node(int x, int y ,Object object){
        xPoint = x;
        yPoint = y;
        this.object = object;
    }
    
    public void setPointX(int x){
        xPoint = x;
        String route = "(" + Integer.toString(this.xPoint) + "," + Integer.toString(this.yPoint) + ")";
        warriorRoute.add(route);
    }
    
    public void setPointY(int y){
        yPoint = y;
        String route = "(" + Integer.toString(this.xPoint) + "," + Integer.toString(this.yPoint) + ")";
        warriorRoute.add(route);
    }
    
    public String getTheRoute() {
        String wRoute = "";
        for (int j = 0; j < warriorRoute.size(); j++) {
            wRoute= wRoute + (warriorRoute.get(j) + "  ");
        }
        return wRoute;
    }
    
    public int getPointX(){
        return xPoint;
    }
    
    public int getPointY(){
        return yPoint;
    }
}

class Lotus {
    
    private boolean hasPetal;
    private static int petals = 100;
    Node node;
    
    private ArrayList<Petal> petalList = new ArrayList<Petal>(100);;
    
    public Lotus() {
        for(int i =0 ; i< 100 ; i++){
            Petal petal = new Petal();
            petalList.add(petal);
        }
        hasPetal = true;
    }
    
    public void setCoordinates(int xCoordinate, int yCoordinate) {
	node = new Node ( xCoordinate, yCoordinate, this);
    }
    
    public synchronized void loosePetal(Warrior w){
        if (petals!=0){
            petalList.remove(0);
            w.setImmortal(true);
            petals --;
            System.out.println(w.getInhabitantName() + "warrior is immortal ");
        }
        else{
            hasPetal = false;
            System.out.println("There are no petals in this lotus");
            
        }
    }
       
    public boolean hasPetals(){
        return hasPetal;
    }
}

class Petal {
    
}

abstract class Inhabitant extends Thread {
    
    private String name;
    public Node node;
    
	
    public abstract void setCoordinates(int xCoordinate, int yCoordinate);
	
	
    public void setInhabitantName(String name) {
	this.name = name;
    }
	
    public String getInhabitantName() {
	return name;
    }
}

class Fish extends Inhabitant {
    
    Grid lakeNozama;
                     
    public Fish(String name, Grid lakeNozama) {
        setInhabitantName(name);
        this.lakeNozama = lakeNozama;
    }

    public void setCoordinates(int xPoint, int yPoint) {
        node = new Node(xPoint, yPoint, this);
    }

}

class RubberEater extends Fish{
    
    ArrayList<Fin> fins; 
	
    public RubberEater(String name, Grid lake) {                                
        super(name, lake);
		fins = new ArrayList<Fin>();

    }
    
    public void update(Warrior warrior){
        if ( warrior.node.getPointX()== this.node.getPointX() && warrior.node.getPointY() == this.node.getPointY()) {
            eatFins(warrior); 
        }
    }
    
    public synchronized void eatFins(Warrior warrior) {                                                          
        if (warrior.node.getPointX() == this.node.getPointX() && warrior.node.getPointY() == this.node.getPointY()) {
            Game.warriorCount--;   //warriors who can swim is decreased by one
            warrior.noFins();
            System.out.println( getInhabitantName()+ " ate the fins of " + warrior.getInhabitantName());
            
            
        }
    }
}

class KillerFish extends Fish{
    
    public KillerFish(String name, Grid lake) {                                
        super(name, lake);

    }
     
    public void update(Warrior warrior) {
        if (this.node.getPointX() == warrior.node.getPointX() && this.node.getPointY() == warrior.node.getPointY()) {
            killWarrior(warrior);
        }
    }
    
    public synchronized void killWarrior(Warrior warrior) {                                                       
        if (warrior.getImmortal()==false) {
            warrior.setCanSwim(false);
            System.out.println(this.getInhabitantName() + " killer fish, killed warrior " + warrior.getInhabitantName());
            warrior = null;
            Game.warriorCount--;
        } else {
            System.out.println( warrior.getInhabitantName() + " immortal warrior cannot be killed");
        }
    }
}

interface WarriorInterface {
    
    public  void eat();
    
    public  void sleep();
}

class Warrior extends Inhabitant implements WarriorInterface {
    
    private boolean canSwim;
    private boolean isImmortal ;
    private static int numberOfWarriors = 0;
    ArrayList<Fin> fins = new ArrayList<Fin>(2);
    ArrayList<Inhabitant> warriorObservers;
    Random random;
    Grid lakeNozama;
	
    public Warrior(String name, Grid lakeNozama,ArrayList<Inhabitant> warriorObservers) {
	
		for (int i=0; i<2 ; i++){
            Fin fin = new Fin();
            fins.add(fin);
        }
        this.warriorObservers = warriorObservers;
        setInhabitantName(name);
        random = new Random();
		isImmortal = false;
		canSwim = true;
        this.lakeNozama = lakeNozama;
        numberOfWarriors += 1;
        
    }
	
    public void setCoordinates(int xCoordinate, int yCoordinate) {
	node = new Node( xCoordinate, yCoordinate, this);
		
    }
    
    public void swim(int gridDirection ,Grid lake ) { 
        
        if (getCanSwim()==false) {
            System.out.println(getInhabitantName() + " can't swim in the lake!");
            this.stop();
            
            
        }else{
            
            
            if (gridDirection == 0 && node.getPointX() != 0) {
                lake.changeCoordinate(node.getPointX(), node.getPointY(), node.getPointX()-1, node.getPointY(), 4, this );
                node.setPointX(node.getPointX() - 1);
                
            } else if (gridDirection == 1 && node.getPointX() != 10) {
                lake.changeCoordinate( node.getPointX(), node.getPointY(), node.getPointX()+1, node.getPointY(),4, this);
                node.setPointX(node.getPointX() + 1);

            } else if (gridDirection == 2 && node.getPointY() != 0) {

                lake.changeCoordinate( node.getPointX(), node.getPointY(), node.getPointX(), node.getPointY()-1,4, this);
                node.setPointY(node.getPointY() - 1);

            } else if (gridDirection == 3 && node.getPointY() != 10) {
                lake.changeCoordinate(node.getPointX(), node.getPointY(), node.getPointX(), node.getPointY()+1, 4, this);
                node.setPointY(node.getPointY() + 1);

            }
           
            if (lake.grid[node.getPointX()][node.getPointY()][0] != null) {
                
                if ( !getImmortal() && lake.grid[node.getPointX()][node.getPointY()][0] instanceof Lotus ) {
                    Lotus lotus = (Lotus) lake.grid[node.getPointX()][node.getPointY()][0];
                    pickLotus(lotus);
                }
            }
            
            
            notifyFish();
        }
    }
    
    @Override
    public void run() {
        while (!TreasureChest.hasWon()) {
            
            if (Game.warriorCount == 0) {
                System.out.println("Game over !");
                break;
            }
            swim(random.nextInt(4),lakeNozama);
            

        }
        this.stop();
    }
    
    public void notifyFish() {
        for (int j = 0; j < 6; j++) {
            Inhabitant in = warriorObservers.get(j);
            if (in instanceof RubberEater) {
                RubberEater re = (RubberEater) in;
                re.update(this);
            } else if (in instanceof KillerFish) {
               KillerFish kf = (KillerFish) in;
               kf.update(this);
            }
        }
    } 
    
    public void noFins() {                               
        setCanSwim(false);
        
    }
    
    public void eat(){
        //
    }
    
    public void sleep(){
        //
    }
    
    public void setImmortal(boolean immortal) {
        isImmortal = immortal;
    }

    public boolean getImmortal() {
        return isImmortal;
    }

    public void setCanSwim(boolean canSwim) {
        this.canSwim = canSwim;
    }
    
    public boolean getCanSwim() {
	return canSwim;
    }
	
    public static int getWarriorCount() {
	return numberOfWarriors;
    }
	    
    public void update(){
        this.stop();
    }
	
	
    public void pickLotus(Lotus lotus) {
        
        if(lotus.hasPetals()!=false){
            lotus.loosePetal(this);
	
        }
    }
}

class SuperWarrior extends Warrior {
    
    
    Binocular binocular;

    public SuperWarrior(String name, Grid lake, ArrayList<Inhabitant> warriorObservers) {                                 
        super(name, lake, warriorObservers);
        binocular = new Binocular();

    }

    public void swimSuper(Grid lake) {                                  
        int direction = 4;
        if(this.getImmortal()!= false){
            binocular.searchLotus(node.getPointX(), node.getPointY(), lake);      
        }
        if(direction ==4){
            swim(random.nextInt(4), lake);
        }else if (direction >=0  && direction <4) {
            swim(direction, lake);
        
        }

    }
	
	@Override
    public void run() {
        while (!TreasureChest.hasWon()) {
            
            if (Game.warriorCount == 0) {
                System.out.println("Game over !");
                
                break;
            }
            swimSuper(lakeNozama);
        }
        this.stop();
    }

    public void eat(){
        //
    }
    
    public void sleep(){
        //
    }
}


class TreasureChest extends Thread {
    
    
    private static boolean hasWon = false;          
    ArrayList<Inhabitant> swimmers;

    public TreasureChest(ArrayList<Inhabitant> warriors) {
        swimmers = warriors;
    }
    
    public static synchronized void setHasWon(boolean won) {
        hasWon = won;
    }

    public synchronized void notifyWarriors() {
        
        for (int j = 0; j < Game.warriorCount; j++) {
            Warrior war = (Warrior) swimmers.get(j);
            war.update();
        }
    }

    
    public static synchronized boolean hasWon() {
        return hasWon;

    }

}



class Grid {
    
    private boolean nodeIsAvailable;
    TreasureChest treasure;
    Object[][][] grid;
    Random random;
	
    public Grid(int gridWidth, int gridLength, TreasureChest treasure) {
        nodeIsAvailable = true;
        random = new Random();
		this.treasure = treasure;
        grid = new Object[gridWidth][gridLength][2];
    }
    
    public synchronized int checkCoordinate(int xPoint, int yPoint, int indexNo) {                         
       
        if (grid[xPoint][yPoint][indexNo] instanceof Lotus) {
            return 1;
        } else if (grid[xPoint][yPoint][indexNo] instanceof RubberEater) {
            return 2;
        } else if (grid[xPoint][yPoint][indexNo] instanceof KillerFish) {
            return 3;
        } else if (grid[xPoint][yPoint][indexNo] instanceof Warrior ) {
            return 4;
        } else if (grid[xPoint][yPoint][indexNo] instanceof Fish){
            return 5;
        }else if (grid[xPoint][yPoint][indexNo] == null){
            return 6;
        } else {
            return 0 ; 
        }
    }
    
    public synchronized void removeObject(int xPoint, int yPoint) {                                         
        grid[xPoint][yPoint][0] = null;
    }
    
    public synchronized void setObject( int xPoint, int yPoint,Object object , int indexNo) {                              
        grid[xPoint][yPoint][indexNo] = object;
    }

    public synchronized boolean checkWinner(int xPoint, int yPoint) {                              
        return (xPoint == 5 & yPoint == 5);

    }

    public synchronized void checkAvailable(Warrior warrior) {  // guarded block
        while (nodeIsAvailable==false) {
            try {
                warrior.wait();
            } catch (InterruptedException ex) {
            }
        }
    }
    public synchronized void changeCoordinate( int firstX, int firstY, int secondX, int secondY, int inhabitantType,Warrior warrior) {  

        if (checkCoordinate(secondX, secondY, 1) == 6 && TreasureChest.hasWon()== false) {
            grid[secondX][secondY][1] = grid[firstX][firstY][1];
            grid[firstX][firstY][1] = null;
            System.out.println(warrior.getInhabitantName() + " swam to (" + secondX + "," + secondY + ")");
        }
        if (checkWinner(secondX, secondY)) {
            long endTime = System.currentTimeMillis();
            TreasureChest.setHasWon(true);
            try {
                File file = new  File("winner.txt");
                FileWriter fileWriter = new FileWriter(file);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                
                printWriter.println("Winner : " + warrior.getInhabitantName());
                System.out.println("Winner : " + warrior.getInhabitantName());
                
                printWriter.append("Winner route : " + warrior.node.getTheRoute()+"\n");
                System.out.println("Winner route : " + warrior.node.getTheRoute());
                
                printWriter.append("Time : " + ((endTime - Game.getStartTime()) ) + " miliseconds");
                System.out.println("Time : " + ((endTime - Game.getStartTime()) ) + " miliseconds");
                
                fileWriter.close();
                treasure.notifyWarriors();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Game {
    
    private static long timeBegan;
    public static int warriorCount = 0;
    
    public static void main(String[] args) {
        
        Random random = new Random();
        ArrayList<Lotus> lotus = new ArrayList<Lotus>();  
        ArrayList<Inhabitant> fish = new ArrayList<Inhabitant>();  
        ArrayList<Inhabitant> warriors = new ArrayList<Inhabitant>();  
        
		TreasureChest treasure = new TreasureChest(warriors); 
        Grid lake = new Grid( 11, 11, treasure);  
        
        lake.setObject(5, 5, treasure, 0); 
        System.out.println("Treasure is at (5,5)");
        
        lotus.add(new Lotus());                                                  
        lotus.add(new Lotus());
        lotus.add(new Lotus());
        lotus.add(new Lotus());
        lotus.add(new Lotus());
        
        fish.add(new Fish("CatWoman", lake));                        
        fish.add(new Fish("RobbinHood",  lake));
        fish.add(new RubberEater("Oswarld",  lake));
        fish.add(new RubberEater("FishMooney", lake));
        fish.add(new KillerFish("Voldermort",  lake));
        fish.add(new KillerFish("Barbara", lake));
        
        warriors.add(new Warrior("WonderWoman", lake, fish));                               
        warriors.add(new Warrior("Flash", lake, fish));
        warriors.add(new Warrior("Batman",  lake, fish));
        warriors.add(new SuperWarrior("SuperGirl", lake,fish));
        
        
        
        int[] gridCorners = {0, 10};                                        
        while (warriorCount <= 3) {
            
            int xPoint, yPoint = 0;
            int gridSide = random.nextInt(2);
            if (gridSide != 0) {
                xPoint = gridCorners[random.nextInt(2)];
                yPoint = random.nextInt(11);
            } else {
                xPoint = random.nextInt(11);
                yPoint = gridCorners[random.nextInt(2)];
            }
            if (lake.checkCoordinate(xPoint, yPoint, 0) == 6) {
                lake.setObject( xPoint, yPoint,warriors.get(warriorCount), 1);
                warriors.get(warriorCount).setCoordinates(xPoint, yPoint);       
                System.out.println(warriors.get(warriorCount).getClass().getSimpleName()
                        + " " + warriors.get(warriorCount).getInhabitantName() 
                        + " at  (" + xPoint + "," + yPoint + ")");
                warriorCount++;
            }
        }

        
        int numberOfLotus = 0;                                                     
        while (numberOfLotus < 5) {
            int xPoint = random.nextInt(11);
            int yPoint = random.nextInt(11);
            if (lake.checkCoordinate(xPoint, yPoint, 0) == 6) {
                lake.setObject( xPoint, yPoint,lotus.get(numberOfLotus), 0);
                lotus.get(numberOfLotus).setCoordinates(xPoint, yPoint);
                System.out.println("A Tree at (" + xPoint + "," + yPoint + ")");
                numberOfLotus++;
            }
        }
        

        int numberOfFish = 0;                                                  
        while (numberOfFish < 6) {
            int xPoint = random.nextInt(11);
            int yPoint = random.nextInt(11);
            if (lake.checkCoordinate(xPoint, yPoint, 0) == 6) {
                lake.setObject(xPoint, yPoint, fish.get(numberOfFish),0);           
                fish.get(numberOfFish).setCoordinates(xPoint, yPoint);        
                System.out.println( fish.get(numberOfFish).getClass().getSimpleName()
                        + " " + fish.get(numberOfFish).getInhabitantName()     
                        + " at  (" + xPoint + "," + yPoint + ")");
                numberOfFish++;
            }
        }
        
        System.out.println("Number Of Warriors = " + warriorCount);
        
        timeBegan =  System.currentTimeMillis();
        for (int j = 0; j < warriorCount; j++) {
            warriors.get(j).start();
        }

    }

    public static long getStartTime() {
        return timeBegan;
    }
}

	



