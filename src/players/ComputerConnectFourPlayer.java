package players;
import java.util.ArrayList;

import helper.Node;

/**
 * This class will implement adversarial search using a cutoff test and an evaluation function.
 * @author baolson
 *
 */
public class ComputerConnectFourPlayer implements ConnectFourPlayer {
	int depth;
	byte side;
	/** 
	 * Constructor for the computer player.
	 * @param depth the number of plies to look ahead
	 * @param side -1 or 1, depending on which player this is
	 */
	public ComputerConnectFourPlayer(int depth, byte side) {
		this.depth = depth;
		this.side = side;
	}

	/** 
	 * This computer will return the minmax best play.
	 * @param rack the current rack
	 * @return the column to play
	 */
	public int getNextPlay(byte[][] rack) {
		//Generating start node
		Node startNode = new Node(rack,side);
		//Generating possible actions
		Node[] actions = startNode.generateStrictChildren();
		//determining best action if side 1
		int posBest = -1;
		if(side == 1) {
			int bestVal = -1000000000;
			for(int i=0;i<7;i++) {
				if(actions[i] != null) {
					if(bestVal < minValue(actions[i])) {
						bestVal = minValue(actions[i]);
						posBest = i;
					}
				}
			}
		}
		//determining best action if side -1
		if(side == -1) {
			int bestVal = 1000000000;
			for(int i=0;i<7;i++) {
				if(actions[i] != null) {
					if(bestVal > maxValue(actions[i])) {
						bestVal = maxValue(actions[i]);
						posBest = i;
					}
				}
			}
		}
		return posBest;
	}
	// Helpers!
	/**
	 * Returns the maximum of the results returned by min value or the current node's evaluated value.
	 * @return
	 */
	private int maxValue(Node state) {
		//Ending if terminal
		if(cutoffTest(state)) return evaluate(state);
		//Instantiating action set and initial best value
		ArrayList<Node> actions = state.generateChildren();
		Integer v = Integer.MIN_VALUE;
		//Updating best value for each action.
		for(Node action : actions) {
			v = Math.max(v, minValue(action));
		}
		
		return v;
	}
	
	/**
	 * Returns the minimum of the results returned by max value or the current node's evaluated value.
	 * @return
	 */
	private int minValue(Node state) {
		//Ending if terminal
		if(cutoffTest(state)) return evaluate(state);
		//Instantiating action set and initial best value
		ArrayList<Node> actions = state.generateChildren();
		Integer v = Integer.MAX_VALUE;
		//Updating best value for each action.
		for(Node action : actions) {
			v = Math.min(v, maxValue(action));
		}
		return v;
	}

	//tested
	/**
	 * Returns the estimated value of the current game state for the current player.
	 * @return
	 */
	private int evaluate(Node currState) {
		byte[][] rack = currState.getRack();
		//Summing value of all row spans
		int rowSum = 0;
		for(int row=0;row<6;row++) {
			for(int start=0;start<4;start++) {
				int count1 = 0;
				int countNeg1 = 0;
				for(int i=start;i<start+4;i++) {
					if(rack[row][i] == (byte) 1) {
						count1++;
					}
					if(rack[row][i] == (byte) -1) {
						countNeg1++;
					}
				}
				rowSum += getValue(count1,countNeg1);
			}
		}
		//Summing value of all column spans
		int colSum = 0;
		for(int col=0;col<7;col++) {
			for(int start=0;start<3;start++) {
				int count1 = 0;
				int countNeg1 = 0;
				for(int i=start;i<start+4;i++) {
					if(rack[i][col] == (byte) 1) {
						count1++;
					}
					if(rack[i][col] == (byte) -1) {
						countNeg1++;
					}
				}
				colSum += getValue(count1,countNeg1);
			}
		}
		//Summing value of all diagonal spans
		int diagSum = 0;
		//Summing upwards diagonals
		for(int startRow =3;startRow <= 5;startRow++) {
			for(int startCol = 0; startCol <=3; startCol++) {
				int count1 = 0;
				int countNeg1 = 0;
				for(int i=0;i<4;i++) {
					if(rack[startRow - i][startCol + i] == (byte) 1) {
						count1++;
					}
					if(rack[startRow - i][startCol +i] == (byte) -1) {
						countNeg1++;
					}
				}
				diagSum += getValue(count1,countNeg1);
			}
		}
		//Summing downwards diagonals
		for(int startRow = 0;startRow <= 2; startRow++) {
			for(int startCol = 0; startCol <= 3; startCol++) {
				int count1 = 0;
				int countNeg1 = 0;
				for(int i=0;i<4;i++) {
					if(rack[startRow + i][startCol + i] == (byte) 1) {
						count1++;
					}
					if(rack[startRow + i][startCol + i] == (byte) -1) {
						countNeg1++;
					}
				}
				diagSum += getValue(count1,countNeg1);
			}
		}
		
		
		return rowSum + colSum + diagSum;
	}
	
	//Tested
	/**
	 * Returns whether or not the passed in node is at the cutoff depth.
	 * @return
	 */
	private boolean cutoffTest(Node currState) {
		if(currState.getDepth()>= depth) return true;
		if(this.evaluate(currState) >= 500000) return true;
		if(this.evaluate(currState) <= -500000) return true;
		return false;
	}
	
	//Tested
	private int getValue(int count1, int countNeg1) {
		if(count1 > 0 && countNeg1 > 0) return 0;
		if(count1 == 0 && countNeg1 == 0) return 0;
		if(count1 > 0) {
			switch(count1) {
			case 1:
				return 1;
			case 2:
				return 10;
			case 3:
				return 100;
			case 4:
				return 100000000;
			default:
			}
		}
		if(countNeg1 > 0) {
			switch(countNeg1) {
			case 1:
				return -1;
			case 2:
				return -10;
			case 3:
				return -100;
			case 4:
				return -10000000;
			default:
			}
		}
		return 0;
	}
	
	public static void main(String[] args) {
		byte[][] rack = new byte[6][7];
		ComputerConnectFourPlayer testPlayer = new ComputerConnectFourPlayer(1,(byte) 1);
		System.out.println(testPlayer.getNextPlay(rack));
	}
}
