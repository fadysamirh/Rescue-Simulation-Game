package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class FireTruck extends FireUnit {

	public FireTruck(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			jobsDone();
			return;
		} else if (target.getFireDamage() > 0)

			target.setFireDamage(target.getFireDamage() - 10);

		if (target.getFireDamage() == 0)

			jobsDone();

	}

	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if (r instanceof ResidentialBuilding) {
			ResidentialBuilding a = (ResidentialBuilding) r;
			if (!this.canTreat(r) || a.getFireDamage() == 0)
				throw new CannotTreatException(this, r, "The target doesnot need treatment , eh ya3ma ya3ma");
			// else if (!(r.getDisaster() instanceof Fire)) {
			// throw new IncompatibleTargetException(this, r,
			// "Fire Truck only responds to Fire , wala enta ra2yak eh");
			// }
			else {
				super.respond(r);
			}
		} else {
			throw new IncompatibleTargetException(this, r, "Fire truck only responds to residential building");
		}
	}

	public String toString() {
		String info = " ";
		String id = "ID: " + this.getUnitID() + "\n";
		String type = "FireTruck" + "\n";
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
