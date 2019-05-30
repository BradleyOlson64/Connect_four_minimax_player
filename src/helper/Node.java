package helper;
import java.util.ArrayList;

/**
 * This class instantiates a Node containing a copy of the current board state with functionality for
 * generating all child states.
 * @author baolson
 *
 */
public class Node {
	// Globals
	private byte[][] rack;
	private byte player;
	private int depth;
	
	/**
	 * Instantiates a new node from a rack and player, containing a rack, player, and depth.
	 */
	public Node(byte[][] rack, byte player) {
		this.rack = rack;
		this.player = (byte) player;
		this.depth = 0;
	}
	
	/**
	 * Instantiates a new node from a parent node with a rack, player, and depth.
	 * @param rack
	 */
	public Node(Node parent, byte[][] changedRack) {
		this.rack = changedRack;
		this.player = (byte) (parent.player * -1);
		this.depth = parent.depth + 1;
	}
	/**
	 * Generates child nodes including invalid ones of a given rack. This is needed to identify the
	 * final argument to be passed back from ComputerConnectFourPlayer.
	 * @return
	 */
	public Node[] generateStrictChildren(){
		//Generating return list
		Node[] returnList = new Node[7];
		//Generating and adding nodes to return
		for(int j=0;j<7;j++) {
			//Making new rack
			byte[][] newRack = new byte[6][7];
			for(int i=0;i<6;i++) {
				for(int k=0;k<7;k++) {
					newRack[i][k] = rack[i][k];
				}
			}
			// Making needed change
			if(newRack[0][j] == 0) {
				for(int i= 5;i>=0;i--) {
					if(newRack[i][j] == 0) {
						newRack[i][j] = player;
						break;
					}
				}
				// Making and returning new node
				Node newNode = new Node(this,newRack);
				returnList[j] = newNode;
			}
		}
		return returnList;
	}
	
	/**
	 * Generates all the child nodes of a given rack and returns them in an array list
	 * @param rack
	 * @return
	 */
	public ArrayList<Node> generateChildren() {
		//Generating return list
		ArrayList<Node> returnList = new ArrayList<>();
		//Generating and adding nodes to return
		for(int j=0;j<7;j++) {
			//Making new rack
			byte[][] newRack = new byte[6][7];
			for(int i=0;i<6;i++) {
				for(int k=0;k<7;k++) {
					newRack[i][k] = rack[i][k];
				}
			}
			// Making needed change
			if(newRack[0][j] == 0) {
				for(int i= 5;i>=0;i--) {
					if(newRack[i][j] == 0) {
						newRack[i][j] = player;
						break;
					}
				}
				// Making and returning new node
				Node newNode = new Node(this,newRack);
				returnList.add(newNode);
			}
		}
		return returnList;
	}
	
	/**
	 * Returns the rack of this node.
	 * @return
	 */
	public byte[][] getRack(){
		return rack;
	}
	/**
	 * Returns the depth of this node.
	 * @return
	 */
	public int getDepth() {
		return depth;
	}
	
	@Override
	/**
	 * Allows for printouts of the game state.
	 */
	public String toString() {
		String returnString = "";
		for(int i=0;i<6;i++) {
			if(i!=0) returnString = returnString + "\n";
			for(int j=0;j<7;j++) {
				returnString = returnString + rack[i][j] + " ";
			}
		}
		returnString += "\n";
		return returnString;
	}
	
	public static void main(String[] Args) {
		byte[][] rack = new byte[6][7];
		rack[5][0] = 1;
		rack[4][0] = -1;
		rack[3][0] = 1;
		rack[2][0] = -1;
		rack[1][0] = 1;
		rack[0][0] = -1;
		Node test = new Node(rack, (byte) 1);
		ArrayList<Node> children = test.generateChildren();
		System.out.println(test.toString());
		for(int i=0;i<6;i++) {
			System.out.println(children.get(i).toString());
		}
		Node[] children2 = test.generateStrictChildren();
		System.out.println(children2[0]);
	}
}
