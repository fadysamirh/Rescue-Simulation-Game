package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class GasControlUnit extends FireUnit {

	public GasControlUnit(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	public void treat() {
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			jobsDone();
			return;
		} else if (target.getGasLevel() > 0)
			target.setGasLevel(target.getGasLevel() - 10);

		if (target.getGasLevel() == 0)
			jobsDone();

	}

	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if (r instanceof ResidentialBuilding) {
			ResidentialBuilding a = (ResidentialBuilding) r;
			if (!this.canTreat(r) || a.getGasLevel() == 0)
				throw new CannotTreatException(this, r, "The target doesnot need treatment");
			// else if(!(r.getDisaster() instanceof GasLeak)) {
			// throw new IncompatibleTargetException(this, r, "Gas control unit only
			// responds to Gas Leak");
			// }
			else {
				super.respond(r);
			}
		} else {
			throw new IncompatibleTargetException(this, r, "Gas control unit only responds to residential building");
		}
	}

	public String toString() {
		String info = " ";
		String id = "ID: " + this.getUnitID() + "\n";
		String type = "Gas Control Unit" + "\n";
		String location = "Location: " + this.getLocation() + " " + "\n";
		String stepsPerCycle = "stepsPerCycle: " + this.getStepsPerCycle() + " " + "\n";
		String targetType = null + "\n";
		String targetLocation = null + "\n";
		if (this.getTarget() != null) {
			targetType = "Residential Building" + "\n";
			targetLocation = "building's location: " + this.getTarget().getLocation() + " " + "\n";
		}
		String state = "state: " + this.getState() + " " + "\n";
		info += id + type + location + stepsPerCycle + targetType + targetLocation + state;
		return info;

	}

}
