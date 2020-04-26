package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.DisasterException;
import model.infrastructure.ResidentialBuilding;

public class Collapse extends Disaster {

	public Collapse(int startCycle, ResidentialBuilding target) {
		super(startCycle, target);

	}

	public void strike() throws DisasterException {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			throw new BuildingAlreadyCollapsedException(this, "Building already collapsed");
		} else {
			target.setFoundationDamage(target.getFoundationDamage() + 10);
			super.strike();
		}
	}

	public void cycleStep() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		target.setFoundationDamage(target.getFoundationDamage() + 10);
	}

//	public String toString() {
//		String info = " ";
//		String name = "name: " + "Collapse" + "\n";
//		String startCycle = "StartCycle: " + this.getStartCycle() + "\n";
//		String target = "target: " + this.getTarget() + "\n";
//		String state = "active :" + this.isActive() + "\n";
//		info += name + startCycle + target + state;
//		return info;
//	}
	public String DisasterName() {
		return "Collapse";
	}

}
