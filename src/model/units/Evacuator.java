package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;

public class Evacuator extends PoliceUnit {

	public Evacuator(String unitID, Address location, int stepsPerCycle, WorldListener worldListener, int maxCapacity) {
		super(unitID, location, stepsPerCycle, worldListener, maxCapacity);

	}

	@Override
	public void treat() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0 || target.getOccupants().size() == 0) {
			jobsDone();
			return;
		}

		for (int i = 0; getPassengers().size() != getMaxCapacity() && i < target.getOccupants().size(); i++) {
			getPassengers().add(target.getOccupants().remove(i));
			i--;
		}

		setDistanceToBase(target.getLocation().getX() + target.getLocation().getY());

	}

	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if (r instanceof ResidentialBuilding) {
			ResidentialBuilding a = (ResidentialBuilding) r;
			if (!this.canTreat(r) || !(a.getDisaster() instanceof Collapse))
				throw new CannotTreatException(this, r, "The target doesnot need treatment ,eh et3myt f 3enek ");
			// else if (!(r.getDisaster() instanceof Collapse)) {
			// throw new IncompatibleTargetException(this, r,
			// "Evacuator is only called if there is a collapse, mat2rfnash ba2a");
			// }
			else {
				super.respond(r);
			}
		} else {
			throw new IncompatibleTargetException(this, r,
					"Evacuator only responds to residential building, rakz ba2a");
		}
	}

	public String toString() {
		String info = " ";
		String id = "ID: " + this.getUnitID() + "\n";
		String type = "Evacuator" + "\n";
		String location = "Location: " + this.getLocation()  + "\n";
		String stepsPerCycle = "stepsPerCycle: " + this.getStepsPerCycle() +  "\n";
		String targetType = "Target Type: null" + "\n";
		String targetLocation = "Target Location: null" + "\n";
		String passengersNumber = "Passengersnumber: " + this.getPassengers().size()  + "\n";
		String citizensInfo = "Citizen Info: null" + "\n";
		for (int i = 0; i < this.getPassengers().size(); i++) {
			citizensInfo += "Citizens' Info:" + ((Citizen) this.getPassengers().get(i)).toString() + "\n";
		}
		if (this.getTarget() != null) {
			targetType = "Residential Building" + "\n";
			targetLocation = "Building's Location: " + this.getTarget().getLocation()  + "\n";
		}

		String state = "state: " + this.getState()  + "\n";
		info += id + type + location + stepsPerCycle + targetType + targetLocation + state + passengersNumber
				+ citizensInfo;
		return info;

	}

}
