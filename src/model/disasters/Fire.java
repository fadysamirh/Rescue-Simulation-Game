package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.DisasterException;
import model.infrastructure.ResidentialBuilding;

public class Fire extends Disaster {

	public Fire(int startCycle, ResidentialBuilding target) {
		super(startCycle, target);

	}

	@Override
	public void strike() throws DisasterException {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			throw new BuildingAlreadyCollapsedException(this, "Building is already collapsed");
		} else {
			target.setFireDamage(target.getFireDamage() + 10);
			super.strike();
		}
	}

	public void cycleStep() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		target.setFireDamage(target.getFireDamage() + 10);

	}

//	public String toString() {
//		String info = " ";
//		String name = "name: " + "Fire" + "\n";
//		String startCycle = "StartCycle: " + this.getStartCycle() + "\n";
//		String target = "target: " + this.getTarget() + "\n";
//		String state = "active :" + this.isActive() + "\n";
//		info += name + startCycle + target + state;
//		return info;
//	}
	public String DisasterName() {
		return "Fire";
	}


}
