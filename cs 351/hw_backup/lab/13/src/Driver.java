import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

import edu.uwm.cs351.GraphInterface;
import edu.uwm.cs351.util.GraphNode;


public class Driver {

	public static void printUsage() { 
		System.out.println("usage: Driver");
		System.out.println("	<filename:input> <string:from> <string:to>");
		System.exit(1);
	}
	
	public static void main(String[] args) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		if(args.length != 3) {
			printUsage();
		}
		System.out.println("     JJwwtt                                              ttkk         ");
		System.out.println("          JJkkRRRRJJ                                      MM          ");
		System.out.println("                  wwMM                                ttMM            ");
		System.out.println("                    ttMMMMZZ                        ZZMM              ");
		System.out.println("                      ;;MMwwww            RRkk    ZZRR                ");
		System.out.println("                        wwZZZZ            ZZ;;  MMMM    ;;            ");
		System.out.println("                        MMwwZZ          JJkk  RRRR  JJMMMMMMMM;;      ");
		System.out.println("              kkwwtt  JJMMJJJJ  tt;;    RRJJRRRRwwwwMMtt    ;;MMMMZZ  ");
		System.out.println("          kkMMMMkkMMkkkkZZJJttZZRRRRMMZZwwRRRRZZRRMMww          ;;    ");
		System.out.println("          MMtt    RRMMRRZZkkMMMMZZMMMMMMwwRRRRRRRRRRMMkk              ");
		System.out.println("          MM        MMMMRRRRMMMMRRMMRRMMRRRRMMRRMMRRMMMM;;            ");
		System.out.println("        wwZZ          MMRRRRRRRRRRRRMMMMRRRRMMRRRRMMMMMMkk            ");
		System.out.println("        ZZtt            RRMMMMRRRRRRRRMMMMMMMMRRRRMMMMMMZZ            ");
		System.out.println("        RR      JJMMMMMMMMRRMMRRRRRRMMMMMMMMRRMMMMMMMMMMRRkkJJ        ");
		System.out.println("      ZZww    MMRRJJwwZZZZJJkkMMMMMMMMMMMMMMMMRRRRRRRRRRRRZZMMkk      ");
		System.out.println("  ;;ZZww    wwMM              wwMMMMMMRRMMMMMMMMMMMMRRRRRRZZZZMMRR    ");
		System.out.println("JJkk        JJJJ              wwRRMMMMMMMMMMMMMMMMMMMMZZwwkkRRRRMM    ");
		System.out.println("              ww              wwww  JJ;;ttRRMMMMMMMMMM      ttRRMM    ");
		System.out.println("            ;;MM              ttZZ          tt;;kk  tttt      MMRR    ");
		System.out.println("              wwkk              MMMMMM;;  ;;;;;;;;          kkMMww    ");
		System.out.println("                JJRR    kkkkZZRRMMRRRRMMMMMMMMMMMMRRRRZZwwRRMMMMkk;;  ");
		System.out.println("                  MMMMRRZZZZwwZZwwwwwwZZZZZZZZZZZZRRRRMMMMRRZZRRMMMM  ");
		System.out.println("                wwZZZZkkwwkkkkwwkkkkkkwwwwwwwwwwwwZZMMZZwwwwwwwwwwZZ  ");
		System.out.println("                kkwwwwwwwwwwwwwwkkwwkkwwkkwwwwZZZZRRRRRRZZZZZZZZZZZZJJ");
		System.out.println("                ttwwZZRRZZZZkkkkkkkkkkwwwwwwwwkkkkkkJJkkwwZZZZZZZZJJ  ");
		System.out.println("                        ttJJJJJJttttJJkkkkwwkkkkJJttJJJJtttt          ");
		System.out.println("\nWelcome to Shelon's lair.\n");
		
		GraphInterface g = new GraphInterface(args[0]);
		Vector<GraphNode> result = new Vector<GraphNode>();
		Vector<GraphNode> user = new Vector<GraphNode>();
		try { 
			result = g.breadthFirstSearch(args[1], args[2]);
		} catch (Exception e) { 
			System.out.println(e);
		}
		
		//SELF CHECK:
		//  for (unsigned int i = 0; i < result.size(); i++) (result[i])->print(cout);
		
		boolean correct = true;
		String input = "";
		String error = "SPLAT!!!  You look nice in silk.";
		String success = "GOOD!  You safely find your way.";

		System.out.println("Please input the node sequence, 0 to end: ");
		try {
			input = in.readLine();
			while (!"0".equals(input)) {
				user.add(new GraphNode(input));
				input = in.readLine();
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		
		if(result.size() != user.size()) {
			correct = false;
		}
		else {
			for(int i = 0; i < result.size() && correct; i++) {
				if (!result.get(i).getName().equals(user.get(i).getName())) {
					correct = false;
				}
			}
		}
		System.out.println(correct ? success : error);
	}
}
