package fr.piotr.economies.persistance.serializable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Buy implements Cloneable, Serializable {

	private Integer					id;
	private String					label;
	private BigDecimal				amount;
	private boolean					monthly;
	private Date					date;

	private final SimpleDateFormat	sdf;

	public Buy(final String buyLabel, final BigDecimal buyAmount, final Integer theId,
			final boolean isMonthly, final Date aDate) {
		label = buyLabel;
		amount = buyAmount;
		id = theId;
		monthly = isMonthly;
		date = aDate;
		sdf = new SimpleDateFormat("dd/MM/yyyy");
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param newId
	 */
	public void setId(final Integer newId) {
		id = newId;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getLabel() + " " + getAmount().toPlainString() + "\n" + sdf.format(getDate());
	}

	/**
	 * @return the monthly
	 */
	public boolean isMonthly() {
		return monthly;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Buy clone() throws CloneNotSupportedException {
		return new Buy(getLabel(), getAmount(), getId(), isMonthly(), getDate());
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @param monthly
	 *            the monthly to set
	 */
	public void setMonthly(boolean monthly) {
		this.monthly = monthly;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}
