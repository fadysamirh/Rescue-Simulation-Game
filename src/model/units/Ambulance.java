package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class Ambulance extends MedicalUnit {

	public Ambulance(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);

		Citizen target = (Citizen) getTarget();
		if (target.getHp() == 0) {
			jobsDone();
			return;
		} else if (target.getBloodLoss() > 0) {
			target.setBloodLoss(target.getBloodLoss() - getTreatmentAmount());
			if (target.getBloodLoss() == 0)
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getBloodLoss() == 0)

			heal();

	}

	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if (r instanceof Citizen) {
			Citizen a = (Citizen) r;
			if (!this.canTreat(r) || a.getBloodLoss() == 0)
				throw new CannotTreatException(this, r, "The target doesnot need treatment, howa zy el erd");

			// else if(!(a.getDisaster() instanceof Injury)) {
			// throw new IncompatibleTargetException(this, r, "Ambulance treats Injury only,
			// msh m7taga fakaka");
			// }

			else if (getTarget() != null && ((Citizen) getTarget()).getBloodLoss() > 0
					&& getState() == UnitState.TREATING)
				reactivateDisaster();
			finishRespond(r);

		}

		else {
			throw new IncompatibleTargetException(this, r, "Ambulance treats citizens only,she2 badyhy");
		}

	}

	public String toString() {
		String info = " ";
		String id = "ID: " + this.getUnitID() + "\n";
		String type = "Unit Type: Ambulance" + "\n";
		String location = "Location: " + this.getLocation() + "\n";
		String stepsPerCycle = "Steps Per Cycle: " + this.getStepsPerCycle()  + "\n";
		String targetType = "Target Type: null" + "\n";
		String targetLocation = "Target Location: null" + "\n";
		if (this.getTarget() != null) {
			targetType = "Target Type: Citizen" + "\n";
			targetLocation = "Target Location: " + this.getTarget().getLocation()  + "\n";
		}
		String state = "state: " + this.getState() ;
		info += id + type + location + stepsPerCycle + targetType + targetLocation + state;
		return info;

	}
}
