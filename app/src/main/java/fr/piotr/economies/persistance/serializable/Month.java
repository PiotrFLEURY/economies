package fr.piotr.economies.persistance.serializable;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Month {

	private BigDecimal	salaire;
	private String		label;
	private String		year;
	private Integer		id;
	private BigDecimal	rest;

	public Month(final BigDecimal aSalaire) {
		this();
		salaire = aSalaire;
		Date time = Calendar.getInstance().getTime();
		label = new SimpleDateFormat("MMMMMM", Locale.FRENCH).format(time);
		year = new SimpleDateFormat("yyyy", Locale.FRENCH).format(time);
	}

	public void setAnticiper(final int howManyMonth) {
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.MONTH, howManyMonth);
		Date time = instance.getTime();
		label = new SimpleDateFormat("MMMMMM", Locale.FRENCH).format(time);
		year = new SimpleDateFormat("yyyy", Locale.FRENCH).format(time);
	}

	public Month() {
		rest = BigDecimal.ZERO;
	}

	/**
	 * @return the monthLabel
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the monthYear
	 */
	public String getYear() {
		return year;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return label + " " + year;
	}

	/**
	 * @return the salaire
	 */
	public BigDecimal getSalaire() {
		return salaire;
	}

	/**
	 * @param salaire
	 *            the salaire to set
	 */
	public void setSalaire(BigDecimal aSalaire) {
		this.salaire = aSalaire;
	}

	/**
	 * @param alabel
	 *            the label to set
	 */
	public void setLabel(String alabel) {
		this.label = alabel;
	}

	/**
	 * @param aYear
	 *            the year to set
	 */
	public void setYear(String aYear) {
		this.year = aYear;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer anId) {
		this.id = anId;
	}

	/**
	 * @return the rest
	 */
	public BigDecimal getRest() {
		return rest;
	}

	/**
	 * @param rest
	 *            the rest to set
	 */
	public void setRest(BigDecimal aRest) {
		this.rest = aRest;
	}

}
