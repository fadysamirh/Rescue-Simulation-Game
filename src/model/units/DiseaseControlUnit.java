package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class DiseaseControlUnit extends MedicalUnit {

	public DiseaseControlUnit(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);
		Citizen target = (Citizen) getTarget();
		if (target.getHp() == 0) {
			jobsDone();
			return;
		} else if (target.getToxicity() > 0) {
			target.setToxicity(target.getToxicity() - getTreatmentAmount());
			if (target.getToxicity() == 0)
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getToxicity() == 0)
			heal();

	}

	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if (r instanceof Citizen) {
			Citizen a = (Citizen) r;
			if (!this.canTreat(r) || a.getToxicity() == 0)
				throw new CannotTreatException(this, r, "The target doesnot need treatment, howa zy el erd");
			// else if (!(a.getDisaster() instanceof Infection)) {
			// throw new IncompatibleTargetException(this, r,
			// "Disease control unit treats only Infected citizens, msh m7taga fakaka");
			// }

			else if (getTarget() != null && ((Citizen) getTarget()).getToxicity() > 0
					&& getState() == UnitState.TREATING)
				reactivateDisaster();
			finishRespond(r);
		} else {
			throw new IncompatibleTargetException(this, r, "Disease control unit treats only citizens, she2 badyhy");
		}
	}

	public String toString() {
		String info = " ";
		String id = "ID: " + this.getUnitID() + "\n";
		String type = "Disease Control Unit" + "\n";
		String location = "Location: " + this.getLocation() + " " + "\n";
		String stepsPerCycle = "stepsPerCycle: " + this.getStepsPerCycle() + " " + "\n";
		String targetType = "Target Type: null" + "\n";
		String targetLocation = "Target Location: null" + "\n";
		if (this.getTarget() != null) {
			targetType = "Target Type: Citizen" + "\n";
			targetLocation = "Target Location: " + this.getTarget().getLocation()  + "\n";
		}
		String state = "state: " + this.getState() + " ";
		info += id + type + location + stepsPerCycle + targetType + targetLocation + state;
		return info;

	}

}
