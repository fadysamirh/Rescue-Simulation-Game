package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.DisasterException;
import model.infrastructure.ResidentialBuilding;

public class GasLeak extends Disaster {

	public GasLeak(int startCycle, ResidentialBuilding target) {
		super(startCycle, target);
	}

	@Override
	public void strike() throws DisasterException {

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			throw new BuildingAlreadyCollapsedException(this, "Building already collapsed");
		} else {
			target.setGasLevel(target.getGasLevel() + 10);
			super.strike();
		}
	}

	@Override
	public void cycleStep() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		target.setGasLevel(target.getGasLevel() + 15);

	}

//	public String toString() {
//		String info = " ";
//		String name = "name: " + "GasLeak" + "\n";
//		String startCycle = "StartCycle: " + this.getStartCycle() + "\n";
//		String target = "target: " + this.getTarget() + "\n";
//		String state = "active :" + this.isActive() + "\n";
//		info += name + startCycle + target + state;
//		return info;
//	}
	public String DisasterName() {
		return "Gas Leak";
	}
}
