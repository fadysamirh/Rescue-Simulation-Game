package model.infrastructure;

import java.util.ArrayList;

import model.disasters.Disaster;
import model.events.SOSListener;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public class ResidentialBuilding implements Rescuable, Simulatable {

	private Address location;
	private int structuralIntegrity;
	private int fireDamage;
	private int gasLevel;
	private int foundationDamage;
	private ArrayList<Citizen> occupants;
	private Disaster disaster;
	private SOSListener emergencyService;

	public ResidentialBuilding(Address location) {
		this.location = location;
		this.structuralIntegrity = 100;
		occupants = new ArrayList<Citizen>();
	}

	public int getStructuralIntegrity() {
		return structuralIntegrity;
	}

	public void setStructuralIntegrity(int structuralIntegrity) {
		this.structuralIntegrity = structuralIntegrity;
		if (structuralIntegrity <= 0) {
			this.structuralIntegrity = 0;
			for (int i = 0; i < occupants.size(); i++)
				occupants.get(i).setHp(0);
		}
	}

	public int getFireDamage() {
		return fireDamage;
	}

	public void setFireDamage(int fireDamage) {
		this.fireDamage = fireDamage;
		if (fireDamage <= 0)
			this.fireDamage = 0;
		else if (fireDamage >= 100)
			this.fireDamage = 100;
	}

	public int getGasLevel() {
		return gasLevel;
	}

	public void setGasLevel(int gasLevel) {
		this.gasLevel = gasLevel;
		if (this.gasLevel <= 0)
			this.gasLevel = 0;
		else if (this.gasLevel >= 100) {
			this.gasLevel = 100;
			for (int i = 0; i < occupants.size(); i++) {
				occupants.get(i).setHp(0);
			}
		}
	}

	public int getFoundationDamage() {
		return foundationDamage;
	}

	public void setFoundationDamage(int foundationDamage) {
		this.foundationDamage = foundationDamage;
		if (this.foundationDamage >= 100) {

			setStructuralIntegrity(0);
		}

	}

	public Address getLocation() {
		return location;
	}

	public ArrayList<Citizen> getOccupants() {
		return occupants;
	}

	public Disaster getDisaster() {
		return disaster;
	}

	public void setEmergencyService(SOSListener emergency) {
		this.emergencyService = emergency;
	}

	public void cycleStep() {

		if (foundationDamage > 0) {

			int damage = (int) ((Math.random() * 6) + 5);
			setStructuralIntegrity(structuralIntegrity - damage);

		}
		if (fireDamage > 0 && fireDamage < 30)
			setStructuralIntegrity(structuralIntegrity - 3);
		else if (fireDamage >= 30 && fireDamage < 70)
			setStructuralIntegrity(structuralIntegrity - 5);
		else if (fireDamage >= 70)
			setStructuralIntegrity(structuralIntegrity - 7);

	}

	public void struckBy(Disaster d) {
		if (disaster != null)
			disaster.setActive(false);
		disaster = d;
		emergencyService.receiveSOSCall(this);
	}

	public String toString() {
		String info = " on Residential Building\n";
		String location = "Location:" + this.getLocation() + "" + "\n";
		String structuralIntegrity = "Structural Integrity: " + this.getStructuralIntegrity() + "" + "\n";
		String fireDamage = "Fire Damage: " + this.getFireDamage() + "" + "\n";
		String gasLevel = "Gas Level:" + this.getGasLevel() + "" + "\n";
		String foundationDamage = "Foundation Damage:" + this.getFoundationDamage() + "\n";
		String numberOfOccupants = "Number Of Occupants:" + this.getOccupants().size() + "\n";
//		String citizensInfo = null;
//		for (int i = 0; i < this.getOccupants().size(); i++) {
//			citizensInfo += ((Citizen) this.getOccupants().get(i)).toString() + "\n";
//		}
		String state = " BUILT" + "\n";
		if (this.getStructuralIntegrity() == 0) {
			state = "COLLAPSED" + "\n";
		}
		String disasterInfo = null;
		if (this.getDisaster() != null) {
			disasterInfo = this.getDisaster().toString();
		}
		String s = this.getDisaster().DisasterName() + "\n" + disasterInfo;
		if (this.fireDamage == 0 && this.gasLevel == 0 && this.getFoundationDamage() == 0)
			s = "None";
		info += location + structuralIntegrity + fireDamage + gasLevel + foundationDamage + state + numberOfOccupants
				+ "Disaster Type: " + s;
		return info;

	}

}
