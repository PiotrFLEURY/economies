package fr.piotr.economies.persistance.serializable;

import java.math.BigDecimal;

public class Profile{

	private final Integer id;
	private final String name;
	private final BigDecimal restOfCurrentMonth;

	public Profile(final Integer anId,final String profileName,final BigDecimal aRest){
		id=anId;
		name=profileName;
		restOfCurrentMonth=aRest;
	}

	/**
	 * @return the profileName
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the restOfCurrentMonth
	 */
	public BigDecimal getRestOfCurrentMonth() {
		return restOfCurrentMonth;
	}

}
