package Juego;

import Juego.Controles.ControlFantasmas;
import Juego.Controles.Human;
import Juego.Controles.ControlPacMan;
import Juego.Controles.Ejemplos.AtraerRelerFantasmas;
import Juego.Controles.Ejemplos.Legado;
import Juego.Controles.Ejemplos.Legado2elCalculo;
import Juego.Controles.Ejemplos.CercaPillPacMan;
import Juego.Controles.Ejemplos.CercaPillPacManVS;
import Juego.Controles.Ejemplos.RandomFantasmas;
import Juego.Controles.Ejemplos.RandomNonRevPacMan;
import Juego.Controles.Ejemplos.RandomPacMan;
import Juego.Entradas.Fantasmas.*;
import Juego.Entradas.pacman.*;
import Juego.core.G;
import Juego.core.GameView;
import Juego.core.Replay;
import Juego.core._G_;
import Juego.core._RG_;
import ia.game.fanstasmas.MiFanstasmita;

/*
 * This class may be used to execute the game in timed or un-timed modes, with or without
 * visuals. Competitors should implement their controllers in game.entries.ghosts and 
 * game.entries.pacman respectively. The skeleton classes are already provided. The package
 * structure should not be changed (although you may create sub-packages in these packages).
 */
@SuppressWarnings("unused")
public class Ejecutor
{	
	//Several options are listed - simply remove comments to use the option you want
	public static void main(String[] args)
	{
		Ejecutor exec=new Ejecutor();
		
		exec.runGameTimed(new Human(), new MiFanstasmita(), true);
		
		exec.runGameTimed(new MiPacMan(), new Legado2elCalculo(), true);
		exec.runExperiment(new MiPacMan(), new Legado(), 100);
		exec.runExperiment(new MiPacMan(), new Legado2elCalculo(), 100);
		exec.runExperiment(new MiPacMan(), new MiFantasma(), 100);
		exec.runMyExperiment(new Legado2elCalculo(), 100, 80, 95, 1);
		
		//this can be used for numerical testing (non-visual, no delays)
		exec.runExperiment(new MiPacMan(),new RandomFantasmas(),100);
		exec.runOptimizeExperiment(new Legado(), 10, 20, 40, 40, 60, 0, 10, 0, 10, 5);
		//exec.runGameTimed(new MyPacMan(), new RandomGhosts(), true);
		

		//run game without time limits (un-comment if required)
		exec.runGame(new Human(),new MiFanstasmita(),true,G.DELAY);
		
		//run game with time limits (un-comment if required)
		exec.runGameTimed(new Human(),new AtraerRelerFantasmas(true),true);
		//run game with time limits. Here NearestPillPacManVS is chosen to illustrate how to use graphics for debugging/information purposes 
		//exec.runGameTimed(new MyPacMan(),new Legacy(),true);
		
		//this allows you to record a game and replay it later. This could be very useful when
		//running many games in non-visual mode - one can then pick out those that appear irregular
		//and replay them in visual mode to see what is happening.
		//you can play as pacman in this version but ghosts scatter at this point
		exec.runGameTimedAndRecorded(new Human(),new MiFanstasmita(),true,"human-v-Legacy2.txt");
		exec.replayGame("human-v-Legacy2.txt");
		
		
	}
	
    protected int pacDir;
    protected int[] ghostDirs;
    public _G_ game;
    protected PacMan pacMan;
    protected Ghosts ghosts;
    protected boolean pacmanPlayed,ghostsPlayed;
   
    /*
     * For running multiple games without visuals. This is useful to get a good idea of how well a controller plays
     * against a chosen opponent: the random nature of the game means that performance can vary from game to game. 
     * Running many games and looking at the average score (and standard deviation/error) helps to get a better
     * idea of how well the controller is likely to do in the competition.
     */
    public void runExperiment(ControlPacMan pacManController,ControlFantasmas ghostController,int trials)
    {
    	int[] scores = new int[trials];
    	
    	double avgScore=0;
    	
		game=new _G_();
		
		for(int i=0;i<trials;i++)
		{
			game.newGame();
			
			while(!game.gameOver())
			{
				long due=System.currentTimeMillis()+G.DELAY;
		        game.advanceGame(pacManController.getAction(game.copy(),due),ghostController.getActions(game.copy(),due));
			}
			
			avgScore+=game.getScore();
			
			scores[i] = game.getScore();
			System.out.println(game.getScore());
		}
		
		avgScore /= trials;
		
		double stdDeviation = 0;
		
		for (int i = 0; i < trials; i++) {
			stdDeviation += ((scores[i] - avgScore) * (scores[i] - avgScore));
		}
		
		stdDeviation /= trials;
		stdDeviation = Math.sqrt(stdDeviation);
		
		System.out.println(avgScore);
		System.out.println(stdDeviation);
    }
    
    public void runMyExperiment(ControlFantasmas ghostController,int trials, int min, int max, int step)
    {
    	ControlPacMan pacManController = new MiPacMan();
    	
    	int bestClose = -1;
    	int bestBlue = -1;
    	double bestScore = Double.MIN_VALUE;
    	
    	for (int close = min; close <= max; close += step) {
    		System.out.println("Tick: " + close);
    		for (int blue = min; blue <= max; blue += step) {
    			System.out.println("\ttick: " + blue);
    			MiPacMan.CLOSE_DIST = close;
    			MiPacMan.CLOSE_BLUE_DIST = blue;
		    	
		    	double avgScore=0;
		    	
				game=new _G_();
				
				for(int i=0;i<trials;i++)
				{
					game.newGame();
					
					while(!game.gameOver())
					{
						long due=System.currentTimeMillis()+G.DELAY;
				        game.advanceGame(pacManController.getAction(game.copy(),due),ghostController.getActions(game.copy(),due));
					}
					
					avgScore+=game.getScore();
				}
				
				avgScore /= trials;
				
				if (avgScore > bestScore) {
					bestScore = avgScore;
					bestClose = close;
					bestBlue = blue;
				}
    		}
    	}
    	
    	System.out.println("Best Close: " + bestClose + " blue: " + bestBlue);
    	System.out.println(bestScore);
    }
    
    public void runOptimizeExperiment(
    		ControlFantasmas ghostController, int trials, 
    		int close_min, int close_max,
    		int blue_min, int blue_max,
    		int power_min, int power_max,
    		int junc_min, int junc_max,
    		int step)
    {
    	ControlPacMan pacManController = new MiPacMan();
    	
    	int bestClose = -1;
    	int bestBlue = -1;
    	int bestPower = -1;
    	int bestJunc = -1;
    	double bestScore = Double.MIN_VALUE;
    	
    	for (int close = close_min; close <= close_max; close += step) {
    		System.out.println("Tick: " + close);
    		for (int blue = blue_min; blue <= blue_max; blue += step) {
    			System.out.println("\tTick: " + blue);
    			for (int power = power_min; power <= power_max; power += step) {
    				System.out.println("\t\tTick: " + power);
    				for (int junc = junc_min; junc <= junc_max; junc += step) {
    					System.out.println("\t\t\tTick: " + junc);
		    			MiPacMan.CLOSE_DIST = close;
		    			MiPacMan.CLOSE_BLUE_DIST = blue;
		    			MiPacMan.POWER_DIST = power;
		    			MiPacMan.JUNC_DIST = junc;
				    	
				    	double avgScore=0;
				    	
						game=new _G_();
						
						for(int i=0;i<trials;i++)
						{
							game.newGame();
							
							while(!game.gameOver())
							{
								long due=System.currentTimeMillis()+G.DELAY;
						        game.advanceGame(pacManController.getAction(game.copy(),due),ghostController.getActions(game.copy(),due));
							}
							
							avgScore+=game.getScore();
						}
						
						avgScore /= trials;
						
						if (avgScore > bestScore) {
							bestScore = avgScore;
							bestClose = close;
							bestBlue = blue;
							bestPower = power;
							bestJunc = junc;
						}
    				}
				}
    		}
    	}
    	
    	System.out.println("Best Close: " + bestClose);
    	System.out.println("Best blue: " + bestBlue);
    	System.out.println("Best power: " + bestPower);
    	System.out.println("Best junc: " + bestJunc);
    	System.out.println(bestScore);
    }
    
    /*
     * Run game without time limit. Very good for testing as game progresses as soon as the controllers
     * return their action(s). Can be played with and without visual display of game states. The delay
     * is purely for visual purposes (as otherwise the game could be too fast if controllers compute quickly. 
     * For testing, this can be set to 0 for fasted game play.
     */
	public void runGame(ControlPacMan pacManController,ControlFantasmas ghostController,boolean visual,int delay)
	{
		game=new _G_();
		game.newGame();

		GameView gv=null;
		
		if(visual)
			gv=new GameView(game).showGame();
		
		while(!game.gameOver())
		{
			long due=System.currentTimeMillis()+G.DELAY;
	        game.advanceGame(pacManController.getAction(game.copy(),due),ghostController.getActions(game.copy(),due));
	        
	        try{Thread.sleep(delay);}catch(Exception e){}
	        
	        if(visual)
	        	gv.repaint();
		}
	}
	
    /*
     * Run game with time limit. This is how it will be done in the competition. 
     * Can be played with and without visual display of game states.
     */
	public void runGameTimed(ControlPacMan pacManController,ControlFantasmas ghostController,boolean visual)
	{
		game=new _G_();
		game.newGame();
		pacMan=new PacMan(pacManController);
		ghosts=new Ghosts(ghostController);
		
		GameView gv=null;
		
		if(visual)
		{
			gv=new GameView(game).showGame();
			
			if(pacManController instanceof Human)
				gv.getFrame().addKeyListener((Human)pacManController);
		}		
		
		while(!game.gameOver())
		{
			pacMan.alert();
			ghosts.alert();

			try
			{
				Thread.sleep(G.DELAY);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}

	        game.advanceGame(pacDir,ghostDirs);	        
	        
	        if(visual)
	        	gv.repaint();
		}
		
		pacMan.kill();
		ghosts.kill();
	}
	
	/*
	 * Runs a game and records all directions taken by all controllers - the data may then be used to replay any game saved using
	 * replayGame(-).
	 */
	public void runGameTimedAndRecorded(ControlPacMan pacManController,ControlFantasmas ghostController,boolean visual,String fileName)
	{
		StringBuilder history=new StringBuilder();
		int lastLevel=0;
		boolean firstWrite=false;	//this makes sure the content of any existing files is overwritten
		
		game=new _G_();
		game.newGame();
		pacMan=new PacMan(pacManController);
		ghosts=new Ghosts(ghostController);
		
		GameView gv=null;
		
		if(visual)
		{
			gv=new GameView(game).showGame();
			
			if(pacManController instanceof Human)
				gv.getFrame().addKeyListener((Human)pacManController);
		}		
		
		while(!game.gameOver())
		{
			pacMan.alert();
			ghosts.alert();

			try
			{
				Thread.sleep(G.DELAY);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}

	        int[] actionsTaken=game.advanceGame(pacDir,ghostDirs);	        
	        
	        if(visual)
	        	gv.repaint();
	        
	        history=addActionsToString(history,actionsTaken,game.getCurLevel()==lastLevel);
        	
	        //saves actions after every level
        	if(game.getCurLevel()!=lastLevel)
        	{
        		Replay.saveActions(history.toString(),fileName,firstWrite);
        		lastLevel=game.getCurLevel();
        		firstWrite=true;
        		history=new StringBuilder();
        	}	   
		}
		
		//save the final actions
		Replay.saveActions(history.toString(),fileName,firstWrite);
		
		pacMan.kill();
		ghosts.kill();
	}
	
	/*
	 * This is used to replay a recorded game. The controllers are given by the class Replay which may
	 * also be used to load the actions from file.
	 */
	public void replayGame(String fileName)
	{
		_RG_ game=new _RG_();
		game.newGame();

		Replay replay=new Replay(fileName);
		ControlPacMan pacManController=replay.getPacMan();
		ControlFantasmas ghostController=replay.getGhosts();
		
		GameView gv=new GameView(game).showGame();
		
		while(!game.gameOver())
		{
	        game.advanceGame(pacManController.getAction(game.copy(),0),ghostController.getActions(game.copy(),0));
	        
	        gv.repaint();
	        
	        try{Thread.sleep(G.DELAY);}catch(Exception e){}
		}
	}
	
    private StringBuilder addActionsToString(StringBuilder history,int[] actionsTaken,boolean newLine)
    {
    	history.append((game.getTotalTime()-1)+"\t"+actionsTaken[0]+"\t");

        for (int i=0;i<G.NUM_GHOSTS;i++)
        	history.append(actionsTaken[i+1]+"\t");

        if(newLine)
        	history.append("\n");
        
        return history;
    }
    	
	//sets the latest direction to take for each game step (if controller replies in time)
	public void setGhostDirs(int[] ghostDirs)
	{
		this.ghostDirs=ghostDirs;
		this.ghostsPlayed=true;
	}
	
	//sets the latest direction to take for each game step (if controller replies in time)
	public void setPacDir(int pacDir)
	{
		this.pacDir=pacDir;
		this.pacmanPlayed=true;
	}
	
	/*
	 * Wraps the controller in a thread for the timed execution. This class then updates the
	 * directions for Exec to parse to the game.
	 */
	public class PacMan extends Thread 
	{
	    private ControlPacMan pacMan;
	    private boolean alive;

	    public PacMan(ControlPacMan pacMan) 
	    {
	        this.pacMan=pacMan;
	        alive=true;
	        start();
	    }

	    public synchronized void kill() 
	    {
	        alive=false;
	        notify();
	    }
	    
	    public synchronized void alert()
	    {
	        notify();
	    }

	    public synchronized void run() 
	    {
	        while(alive) 
	        {
	        	try 
	        	{
	        		synchronized(this)
	        		{
	        			wait();
	                }
	                
	        		setPacDir(pacMan.getAction(game.copy(),System.currentTimeMillis()+G.DELAY));
	            } 
	        	catch(InterruptedException e) 
	        	{
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	/*
	 * Wraps the controller in a thread for the timed execution. This class then updates the
	 * directions for Exec to parse to the game.
	 */
	public class Ghosts extends Thread 
	{
		private ControlFantasmas ghosts;
	    private boolean alive;

	    public Ghosts(ControlFantasmas ghosts) 
	    {	    	
	    	this.ghosts=ghosts;
	        alive=true;
	        start();
	    }

	    public synchronized void kill() 
	    {
	        alive=false;
	        notify();
	    }

	    public synchronized void alert() 
	    {
	        notify();
	    }
	    
	    public synchronized void run() 
	    {
	        while(alive) 
	        {
	        	try 
	        	{
	        		synchronized(this)
	        		{
	        			wait();
	                }

	        		setGhostDirs(ghosts.getActions(game.copy(),System.currentTimeMillis()+G.DELAY));
	            } 
	        	catch(InterruptedException e) 
	        	{
	                e.printStackTrace();
	            }
	        }
	    }
	}
}