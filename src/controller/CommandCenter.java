package controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import exceptions.CannotTreatException;
import exceptions.DisasterException;
import exceptions.IncompatibleTargetException;
import model.disasters.Disaster;
import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import simulation.Rescuable;
import simulation.Simulator;
import view.GameView;

public class CommandCenter implements SOSListener, ActionListener {

	private Simulator engine;
	private ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<Citizen> visibleCitizens;
	private GameView view;
	private ArrayList<Unit> emergencyUnits;
	private JButton[][] buttonWorld;
	private ArrayList<JButton> unitButtons;
	private JButton nextCycle;
	private Stack<Object> respondStack;
	private JButton respondButton;
	private int respondNum = 0; // just a checker
	private ArrayList<JButton> citizenBuilding; // list of citizens gowa el building
	private ResidentialBuilding container; // el building el 3ayz ageeb el citizens el gowa
	private ArrayList<JButton> unitBuilding; // list of units 3and el building
	private ArrayList<Unit> unitAtBuilding;
	private ArrayList<JButton> unitCitizen; // list of units 3and el cit
	private ArrayList<JButton> unitsAtBase;
	private ArrayList<Unit> unitsatBaseN;
	private ArrayList<Citizen> cit; // visible citizen outside the building
	private ArrayList<JButton> visCit; // buttons ^
	private ArrayList<Citizen> citizenatBaseN;
	private ArrayList<JButton> citizenAtBase;
	private ArrayList<Citizen> deadCitizens;

	public CommandCenter() throws Exception {
		engine = new Simulator(this);
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		emergencyUnits = engine.getEmergencyUnits();
		respondStack = new Stack<Object>();
		unitsAtBase = new ArrayList<JButton>();
		unitsatBaseN = new ArrayList<Unit>();
		unitAtBuilding = new ArrayList<Unit>();
		cit = new ArrayList<Citizen>();
		citizenatBaseN = new ArrayList<Citizen>();
		visCit = new ArrayList<JButton>();
		citizenAtBase = new ArrayList<JButton>();
		deadCitizens = new ArrayList<Citizen>();

		respondButton = new JButton("Respond To By");
		respondButton.addActionListener(this);

		view = new GameView();
		view.updateDisaster("Disasters Count: 0");

		view.getBottomPanel().add(respondButton);

		citizenBuilding = new ArrayList<JButton>();
		unitBuilding = new ArrayList<JButton>();
		unitCitizen = new ArrayList<JButton>();

		nextCycle = view.getNextCycle();
		nextCycle.addActionListener(this);

		buttonWorld = view.getButtonWorld();
		updateVisibles();
		for (int row = 0; row < buttonWorld.length; row++) {
			for (int col = 0; col < buttonWorld[row].length; col++) {
				buttonWorld[row][col].addActionListener(this);
			}
		}

		unitButtons = new ArrayList<JButton>();

		for (int i = 0; i < emergencyUnits.size(); i++) {
			if (emergencyUnits.get(i) instanceof Ambulance) {
				JButton unit = new JButton("Ambulance");
				unit.addActionListener(this);
				unitButtons.add(unit);
				view.getButtonsPanel().add(unit);
			} else if (emergencyUnits.get(i) instanceof DiseaseControlUnit) {
				JButton unit = new JButton("DiseaseControlUnit");
				unit.addActionListener(this);
				unitButtons.add(unit);
				view.getButtonsPanel().add(unit);
			} else if (emergencyUnits.get(i) instanceof FireTruck) {
				JButton unit = new JButton("FireTruck");
				unit.addActionListener(this);
				unitButtons.add(unit);
				view.getButtonsPanel().add(unit);

			} else if (emergencyUnits.get(i) instanceof GasControlUnit) {
				JButton unit = new JButton("GasControlUnit");
				unit.addActionListener(this);
				unitButtons.add(unit);
				view.getButtonsPanel().add(unit);
			} else if (emergencyUnits.get(i) instanceof Evacuator) {
				JButton unit = new JButton("Evacuator");
				unit.addActionListener(this);
				unitButtons.add(unit);
				view.getButtonsPanel().add(unit);
			}
		}
		buttonWorld[0][0].setEnabled(true);
		buttonWorld[0][0].setText("BASE");
		;
	}

	public void updateVisibles() {
		for (int i = 0; i < visibleBuildings.size(); i++) {
			ResidentialBuilding b = (ResidentialBuilding) visibleBuildings.get(i);
			int x = b.getLocation().getX();
			int y = b.getLocation().getY();
			buttonWorld[x][y].setEnabled(true);
			buttonWorld[x][y].setText("Residential Building");
		}
		for (int i = 0; i < visibleCitizens.size(); i++) {
			Citizen c = (Citizen) visibleCitizens.get(i);
			int x = c.getLocation().getX();
			int y = c.getLocation().getY();
			buttonWorld[x][y].setEnabled(true);
			buttonWorld[x][y].setText("Citizen");
			if (c.getState().equals(CitizenState.DECEASED)) {
				if (deadCitizens.contains(c) == false) {
					System.out.println("dead");
					deadCitizens.add(c);
					JTextArea deadcit = new JTextArea(c.getName() + " died in  " + c.getLocation() + " ");
					view.getExec().add(deadcit);
				}
			}

		}
		for (ResidentialBuilding building : visibleBuildings) {
			ArrayList<Citizen> citizens = building.getOccupants();
			for (Citizen citizen : citizens) {
				if (citizen.getState().equals(CitizenState.DECEASED)) {
					if (deadCitizens.contains(citizen) == false) {
						System.out.println("dead");
						deadCitizens.add(citizen);
						JTextArea deadcit = new JTextArea(
								citizen.getName() + " died in  " + citizen.getLocation() + " ");
						view.getExec().add(deadcit);
					}
				}
			}
			citizenatBaseN.clear();
			citizenAtBase.clear();
			for (int i = 0; i < engine.getCitizens().size(); i++) {
				Citizen citizen = engine.getCitizens().get(i);
				if (citizen.getLocation().getX() == 0 && citizen.getLocation().getY() == 0) {
					citizenatBaseN.add(citizen);
				}
			}

		}
		view.changeCurrent(engine.getCurrentCycle());
//		for (int i = 0; i < emergencyUnits.size(); i++) {
//			Unit c = emergencyUnits.get(i);
//			int x = c.getLocation().getX();
//			int y = c.getLocation().getX();
//			buttonWorld[x][y].setEnabled(true);
//		}
	}

	public void receiveSOSCall(Rescuable r) {

		if (r instanceof ResidentialBuilding) {

			if (!visibleBuildings.contains(r))
				visibleBuildings.add((ResidentialBuilding) r);

		} else {

			if (!visibleCitizens.contains(r))
				visibleCitizens.add((Citizen) r);
		}

	}

	public static void main(String[] args) throws Exception {
		CommandCenter center = new CommandCenter();
		String s = "1,2";
		System.out.print(s.charAt(0) - '0');
		String name = JOptionPane.showInputDialog("What is your name ?");
		JOptionPane.showMessageDialog(null, "EHRAAAAAB YA " + name, "Eh el gabak hena", JOptionPane.PLAIN_MESSAGE);

		// JButton a=(JButton)center.view.getRescuePanel().getComponentAt(200, 200);
		// a.setText("6*6");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton pressed = (JButton) e.getSource();
		if (nextCycle.equals(pressed)) {
			try {
				engine.nextCycle();
				view.clearall();
				updateVisibles();
				String s = "Total Disasters: " + engine.getExecutedDisasters().size();
				view.updateDisaster(s);
				view.updateCasualities(engine.calculateCasualties());
			} catch (DisasterException e1) {
				JOptionPane.showMessageDialog(null, "The disaster cannot strike this target ", "Rakz",
						JOptionPane.PLAIN_MESSAGE);
			}
			updateVisibles();
			updateActiveDisasters();
			updateExecutedDisasters();
			if (engine.checkGameOver()) {
				JFrame over = new JFrame();
				over.setTitle("3aash");
				over.setVisible(true);
				// over.setDefaultCloseOperation(EXIT_ON_CLOSE);
				over.setSize(1000, 500);
				JPanel m = new JPanel();
				JLabel n = new JLabel("leh sbtohom ymoto " + engine.calculateCasualties());
				n.setFont(new Font("Serif", 1, 20));
				m.add(n);
				over.getContentPane().add(m);
				over.validate();
				view.dispose();
			}
		} else if (buttonWorld[0][0].equals(pressed)) {
			view.removeCitizenPanel();
			unitsAtBase.clear();
			unitsatBaseN.clear();
			for (int i = 0; i < emergencyUnits.size(); i++) {
				if (emergencyUnits.get(i).getLocation().getX() == 0
						&& emergencyUnits.get(i).getLocation().getY() == 0) {
					JButton a = new JButton(unitButtons.get(i).getText());
					a.addActionListener(this);
					unitsAtBase.add(a);
					unitsatBaseN.add(emergencyUnits.get(i));
					view.addCitizenPanel(a);
				}
			}
			int i = 1;
			for (Citizen citizen : citizenatBaseN) {
				JButton a = new JButton("Citizen " + i + " At Base");
				a.addActionListener(this);
				view.addCitizenPanel(a);
				citizenAtBase.add(a);
				i++;
			}
		} else if (citizenAtBase.contains(pressed)) {
			int index = citizenAtBase.indexOf(pressed);
			Citizen citizen = this.citizenatBaseN.get(index);
			view.updateInfo(citizen.toString());

		} else if (unitsAtBase.contains(pressed)) {
			int index = unitsAtBase.indexOf(pressed);
			Unit u = this.unitsatBaseN.get(index);
			view.getUnitInfo().setText(u.toString());

		} else if (citizenBuilding.contains(pressed)) {
			int index = citizenBuilding.indexOf(pressed);
			view.updateInfo(container.getOccupants().get(index).toString());
		} else if (unitBuilding.contains(pressed)) {
			int index = unitBuilding.indexOf(pressed);
			Unit u = this.unitAtBuilding.get(index);
			view.getUnitInfo().setText(u.toString());
		} else if (unitCitizen.contains(pressed)) {
			System.out.print("haho");
			int index = unitCitizen.size() - 1;
			Unit u = this.emergencyUnits.get(index);
			view.getUnitInfo().setText(u.toString());
		} else if (visCit.contains(pressed)) {
			int index = visCit.indexOf(pressed);
			Citizen citizen = cit.get(index);
			view.updateInfo(citizen.toString());
			if (respondStack.size() == 2) {
				respondStack.pop();
				respondStack.push(citizen);
			}

		} else if (unitButtons.contains(pressed)) {
			int index = unitButtons.indexOf(pressed);
			Unit u = this.emergencyUnits.get(index);
			view.getUnitInfo().setText(u.toString());
			if (respondStack.size() == 2) {
				if (respondStack.get(1) instanceof ResidentialBuilding) {
					ResidentialBuilding building = (ResidentialBuilding) respondStack.get(1);
					try {
						u.respond(building);
					} catch (IncompatibleTargetException | CannotTreatException e1) {
						JOptionPane.showMessageDialog(null, "The unit cannot treat this target ", "Rakz",
								JOptionPane.PLAIN_MESSAGE);
					}
				} else {
					Citizen citizen = (Citizen) respondStack.get(1);
					try {
						u.respond(citizen);
					} catch (IncompatibleTargetException e1) {
						JOptionPane.showMessageDialog(null, "The unit cannot treat this target ", "Rakz",
								JOptionPane.PLAIN_MESSAGE);
					} catch (CannotTreatException e1) {
						JOptionPane.showMessageDialog(null, "The unit cannot treat this target ", "Rakz",
								JOptionPane.PLAIN_MESSAGE);
					}

				}
				while (!respondStack.isEmpty())
					respondStack.pop();
				respondNum = 0;
			}
			view.getUnitInfo().setText(u.toString());

		} else if (respondButton.equals(pressed)) {
			if (respondStack.isEmpty()) {
				respondNum++;
				respondStack.add(respondButton);
			}
			System.out.print(respondNum);
		} else if (!getIndex(pressed).equals(null)) {
			String s = getIndex(pressed);
			int x = s.charAt(0) - '0';
			int y = s.charAt(2) - '0';
			boolean flag = true;
			view.removeCitizenPanel();
			for (ResidentialBuilding building : visibleBuildings) {
				// boolean flagb=false;
				cit.clear();
				visCit.clear();
				if (building.getLocation().getX() == x && building.getLocation().getY() == y) {
					citizenBuilding.clear();
					unitBuilding.clear();
					if (respondStack.size() == 1) {
						respondNum++;
						respondStack.push(building);
					} else if (respondStack.size() == 2) {
						respondStack.pop();
						respondStack.push(building);

					}
					int i = 1;
					for (Citizen citizen : building.getOccupants()) {
						container = building;
						JButton button = new JButton("Citizen " + i + " Inside");
						button.addActionListener(this);
						citizenBuilding.add(button);
						view.addCitizenPanel(button);
						i++;
						if (visibleCitizens.contains(citizen)) {
							button.setBackground(Color.RED);
							visCit.add(button);
							cit.add(citizen);
							view.addCitizenPanel(button);
							view.validate();
						}
					}
					unitAtBuilding.clear();
					unitBuilding.clear();
					for (Unit u : emergencyUnits) {
						System.out.println("haha");
						if (u.getLocation().getX() == x && u.getLocation().getY() == y) {
							JButton button = new JButton("Unit");
							button.addActionListener(this);
							unitBuilding.add(button);
							unitAtBuilding.add(u);
							view.addCitizenPanel(button);
							view.validate();
						}
					}

					for (Citizen citizen : visibleCitizens) {
						if (citizen.getLocation().getX() == x && citizen.getLocation().getY() == y
								&& !containsCitizen(citizen) && sameLoc(citizen)) {
							JButton button;
							if (citizen.getState() != CitizenState.DECEASED) {
								button = new JButton("Deceased Citizen Outside");
							} else {
								button = new JButton("Visible Citizen Outside");
							}
							button.addActionListener(this);
							cit.add(citizen);
							visCit.add(button);

						}
					}

					view.updateInfo(building.toString());

					flag = false;
					break;
				}
			}

			for (Citizen citizen : visibleCitizens) {
				// citizenBuilding.clear();
				// view.removeCitizenPanel();
				// unitBuilding.clear();
				// boolean flagc=false;
				unitCitizen.clear();
				if (citizen.getLocation().getX() == x && citizen.getLocation().getY() == y && !containsCitizen(citizen)
						&& !sameLoc(citizen)) {

					if (respondStack.size() == 1) {
						respondNum++;
						respondStack.push(citizen);
					} else {
						respondNum = 0;
						while (!respondStack.isEmpty())
							respondStack.pop();

					}
					for (Unit u : emergencyUnits) {
						JButton button = new JButton("unit");
						button.addActionListener(this);
						unitCitizen.add(button);
						if (u.getLocation().getX() == x && u.getLocation().getY() == y) {
							view.addCitizenPanel(button);
							// flag=true;
							break;
						}
					}
//					if(flag==false) {
//						view.removeCitizenPanel();
//					}
					view.updateInfo(citizen.toString());
					break;
				}
			}

			System.out.print(respondNum);
		}

	}

	public boolean containsCitizen(Citizen citizen) {
		for (ResidentialBuilding building : visibleBuildings) {
			if (building.getOccupants().contains(citizen))
				return true;
		}
		return false;
	}

	public boolean sameLoc(Citizen citizen) {
		for (ResidentialBuilding building : visibleBuildings) {
			if (building.getLocation().equals(citizen.getLocation()))
				return true;
		}
		return false;
	}

	public String getIndex(JButton button) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (buttonWorld[i][j].equals(button)) {
					return "" + i + "," + j;
				}
			}
		}
		return null;
	}

	public void updateActiveDisasters() {
		ArrayList<Disaster> exec = engine.getExecutedDisasters();
		for (int i = 0; i < exec.size(); i++) {
			if (exec.get(i).isActive()) {
				JTextArea dis = new JTextArea();

				dis.setText(exec.get(i).DisasterName() + exec.get(i).getTarget().toString());
				view.getDisaster().add(dis);
			}
		}

	}

	public void updateExecutedDisasters() {
		ArrayList<Disaster> exec = engine.getExecutedDisasters();
		view.getExcutedDisaster().removeAll();
		JTextArea text = new JTextArea("Excuted Disasters: ");
		view.getExcutedDisaster().add(text);
		for (int i = 0; i < exec.size(); i++) {
			if (!(exec.get(i).isActive())) {
				JTextArea dis = new JTextArea();

				dis.setText(exec.get(i).DisasterName() + exec.get(i).getTarget());
				view.getExcutedDisaster().add(dis);
			}
		}

	}
}
