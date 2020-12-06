package edu.ufl.cise.cs1.controllers;

import game.controllers.AttackerController;
import game.models.*;
import java.util.List;

//HIGHEST: 5044.9

public final class StudentAttackerController implements AttackerController{

	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int update(Game game,long timeDue){

		int action = 0;

		//get available power pills
		List<Node> available_power_pills = game.getPowerPillList();

		//get available normal pills
		List<Node> available_pills = game.getPillList();

		//get the attacker object
		Attacker gator_dude = game.getAttacker();

		//get list of available defenders
		List<Defender> available_defenders_arraylist = game.getDefenders();

		//get the closet defender and distance from the attacker
		Actor closest_defender = gator_dude.getTargetActor(available_defenders_arraylist, true);

		//distance to closest defender
		int min_dist = gator_dude.getPathTo(closest_defender.getLocation()).size();

		if(min_dist == 1){ //when all enemies are in the prison thing

			Node nearest_pill = gator_dude.getTargetNode(available_pills, true); //nearest normal pill

			action = gator_dude.getNextDir(nearest_pill, true); //pursue the normal pill
//			System.out.println("Pursuing normal pill...");

		}else{ //otherwise...

//			System.out.println("START");

			Node nearest_pill = gator_dude.getTargetNode(available_pills, true); //nearest normal pill

			if(min_dist <= 10  && !(((Defender)closest_defender).isVulnerable())) { //if the closest defender is closer than 10 and the closest defender is not vulnerable


				if(closest_defender.getDirection() == gator_dude.getDirection() && available_pills.size() != 0 && available_power_pills.size() != 0){ //if the attacker and closest defender are facing in the same direction

//					System.out.println("Chase down...   ");

					int num_close_defenders = 0;

					for(Defender def : available_defenders_arraylist) {

						if(gator_dude.getPathTo(def.getLocation()).size() < 15){

							num_close_defenders ++;
						}

					}


					//if there are any vulnerable defenders
					if(game.getDefender(0).isVulnerable() || game.getDefender(1).isVulnerable() || game.getDefender(2).isVulnerable() || game.getDefender(3).isVulnerable()) {

						int distance = 10000;
						Defender closest_vulnerable_defender = ((Defender)closest_defender);

						//find the closest vulnerable defender
						for (Defender def : available_defenders_arraylist) {

							if (gator_dude.getPathTo(def.getLocation()).size() < distance && def.isVulnerable()) {

								distance = gator_dude.getPathTo(def.getLocation()).size();
								closest_vulnerable_defender = def;

							}

						}



						if(gator_dude.getNextDir(closest_vulnerable_defender.getLocation(), true) != gator_dude.getNextDir(closest_defender.getLocation(), false)){

							action = gator_dude.getNextDir(closest_vulnerable_defender.getLocation(), true); //
							return action;

						}else{
							action = gator_dude.getNextDir(closest_defender.getLocation(), false); //if all defenders are vulnerable, pursue nearest defender
							return action;
						}



					}else if(num_close_defenders > 2 && available_power_pills.size() != 0) {

						if(gator_dude.getNextDir( gator_dude.getTargetNode(available_power_pills, true), true) != gator_dude.getNextDir(closest_defender.getLocation(), true)) {
							action = gator_dude.getNextDir(gator_dude.getTargetNode(available_power_pills, true), true);
							//System.out.println("More than 2");
							return action;
						}else{
							action = gator_dude.getNextDir(nearest_pill, true);
							//	System.out.println("More than 2, normal");
							return action;
						}

					} else{

						if((gator_dude.getNextDir(nearest_pill, true) == closest_defender.getReverse() && gator_dude.getPathTo(closest_defender.getLocation()).size() < 10) || gator_dude.getNextDir(nearest_pill, true) == closest_defender.getNextDir(gator_dude.getLocation(), true) && gator_dude.getPathTo(closest_defender.getLocation()).size() < 10){ //if the nearest pill to the attacker is in the direction of the nearest defender

							action = gator_dude.getNextDir(closest_defender.getLocation(), false);
//							System.out.print("Nearest pill is towards defender, avoid closest defender...");

						}
						else{

							if(!((Defender) closest_defender).isVulnerable()){
								action = gator_dude.getNextDir(nearest_pill, true);
//								System.out.print("Pursuing nearest normal pill...");
							}else{
								action = gator_dude.getNextDir(closest_defender.getLocation(), true);

							}

						}

					}

					//System.out.println();

				}else{ //if the attacker and closest defender are not facing in the same direction

					action = gator_dude.getNextDir(closest_defender.getLocation(), false);
				}

			} else if( game.getDefender(0).isVulnerable() || game.getDefender(1).isVulnerable() || game.getDefender(2).isVulnerable() || game.getDefender(3).isVulnerable()){ //if any of the defenders are vulnerable

				if (game.getDefender(0).isVulnerable() && game.getDefender(1).isVulnerable() && game.getDefender(2).isVulnerable() && game.getDefender(3).isVulnerable()) { //if all of the characters are currently vulnerable, pursue

					action = gator_dude.getNextDir(closest_defender.getLocation(), true); //if all defenders are vulnerable, pursue nearest defender
//					System.out.println("YO");
					return action;
//					System.out.print("All defenders vulnerable, pursuing...");

				}else{

//					System.out.println("HERE");

					int distance = 10000;
					Defender closest_vulnerable_defender = ((Defender)closest_defender);

					if(!((Defender)closest_defender).isVulnerable()){
//						System.out.println("Closest defender not vulnerable");

						//find the closest vulnerable defender
						for (Defender def : available_defenders_arraylist) {

							if (gator_dude.getPathTo(def.getLocation()).size() < distance && def.isVulnerable()) {

								distance = gator_dude.getPathTo(def.getLocation()).size();
								closest_vulnerable_defender = def;

							}

						}

						if(gator_dude.getPathTo(closest_vulnerable_defender.getLocation()).size() < 20 && gator_dude.getNextDir(closest_defender.getLocation(), true) != gator_dude.getNextDir(closest_vulnerable_defender.getLocation(),true) ){

							action = gator_dude.getNextDir(closest_vulnerable_defender.getLocation(), true);
//							System.out.println("Pursing closest vulnerable defender...");

							return action;

						}else{
//							System.out.println("SUP");
							action = gator_dude.getNextDir(closest_defender.getLocation(), false);
							return action;

						}

					}else{ //if the closest defender is vulnerable, pursue

//						System.out.println("Closest defender vulnerable");
						boolean flee = false;
						//get the next closest defender
						for (Defender def : available_defenders_arraylist) {

							if (gator_dude.getPathTo(def.getLocation()).size() > gator_dude.getPathTo(closest_defender.getLocation()).size() + 3 && !def.isVulnerable() && gator_dude.getPathTo(closest_defender.getLocation()).size() < 10) {

								//if they are facing in the same direction and on the same axis
								if(gator_dude.getNextDir(def.getLocation(), true) == gator_dude.getNextDir(closest_defender.getLocation(), true) && (gator_dude.getLocation().getY() == def.getLocation().getY() || gator_dude.getLocation().getX() == def.getLocation().getX())) {
									flee = true;
									break;
								}

							}

						}

						if(flee){
							action = gator_dude.getNextDir(closest_vulnerable_defender.getLocation(), false);
//							System.out.println("Flee");
						}else{
							action = gator_dude.getNextDir(closest_vulnerable_defender.getLocation(), true);
//							System.out.println("Dont flee");

						}


					}
				}


			}else if(available_pills.size() > 0){ //if no defenders are vulnerable and there are available normal pills

					if(gator_dude.getPathTo(closest_defender.getLocation()).size() < 25){

						int close_prox_defs = 0; //stores # of close defenders

						for(Defender def : available_defenders_arraylist){ //get # of vulnerable defenders

							if(def.getPathTo(gator_dude.getLocation()).size() < 35 && def.getNextDir(gator_dude.getLocation(), true) == def.getDirection()){
								close_prox_defs ++;
							}

						}

						boolean purpsue_power_pill = false;

						if(close_prox_defs > 1){

							if(available_power_pills.size() != 0 && gator_dude.getNextDir(gator_dude.getTargetNode(available_power_pills, true), true) != gator_dude.getNextDir(closest_defender.getLocation(), true)){ //if the power pill is not in the same direction as the defender

								purpsue_power_pill = true; //if there are more than 1 defenders close by
//								System.out.println("Multiple defenders attacking, pursuing power pill");
							}

						} else if(available_power_pills.size() != 0 && gator_dude.getPathTo(gator_dude.getTargetNode(available_power_pills, true)).size() < 10){

							purpsue_power_pill = true;
//							System.out.print("Power pill detected on chase, pursuing...");
						}

						if(purpsue_power_pill){

							action = gator_dude.getNextDir(gator_dude.getTargetNode(available_power_pills, true), true); //if the attacker is getting chased and is close to a power pill, pursue the power pill
//							System.out.println("Pursuing power pill...");

						}

					}else if(gator_dude.getPathTo(closest_defender.getLocation()).size() > 25 && !((Defender)closest_defender).isVulnerable()) {

						action = gator_dude.getNextDir(nearest_pill, true);

					}else{

						action = gator_dude.getNextDir(closest_defender.getLocation(), true);

					}


			}else if(available_power_pills.size() > 0){ //else, pursue power pill if available

				action = gator_dude.getNextDir(gator_dude.getTargetNode(available_power_pills, true), true);

			}else{// no conditions satisfied

//				System.out.println("No conditions satisfied, pursuing nearest normal pill...");
				action = gator_dude.getNextDir(nearest_pill, true);

			}

		}

		return action;
	}

}
