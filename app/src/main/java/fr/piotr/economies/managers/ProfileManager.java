package fr.piotr.economies.managers;

import fr.piotr.economies.persistance.serializable.Month;
import fr.piotr.economies.persistance.serializable.Profile;

public class ProfileManager {

	private static ProfileManager instance;

	private Profile linkedProfile=null;
	private Month linkedMonth=null;

	public static ProfileManager getInstance(){
		if(instance==null){
			instance=new ProfileManager();
		}
		return instance;
	}

	public void linkOn(final Profile profile){
		linkedProfile=profile;
	}

	public Profile getLinkedProfile(){
		return linkedProfile;
	}

	public void linkonMonth(final Month month){
		linkedMonth=month;
	}

	public Month getLinkedMonth(){
		return linkedMonth;
	}

}
