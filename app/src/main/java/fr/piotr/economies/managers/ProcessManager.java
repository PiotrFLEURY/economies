package fr.piotr.economies.managers;

import java.math.BigDecimal;
import java.util.List;

import fr.piotr.economies.persistance.serializable.Buy;
import fr.piotr.economies.persistance.serializable.Month;

public abstract class ProcessManager {

	/**
	 * @param aMonth
	 * @param buys
	 * @return the calculated rest of the month
	 */
	public static BigDecimal getRestant(Month aMonth, List<Buy> buys) {
		BigDecimal somme = aMonth.getSalaire();
		for (final Buy buy : buys) {
			somme = somme.subtract(buy.getAmount());
		}
		return somme;
	}

	public static BigDecimal getTotal(List<Buy> buys) {
		BigDecimal somme = BigDecimal.ZERO;
		for (final Buy buy : buys) {
			somme = somme.add(buy.getAmount());
		}
		return somme;
	}

	public static String formatAmount(final BigDecimal amount) {
		String toStr = amount.toString();
		int index = toStr.indexOf('.');
		if (index == -1) {
			toStr += ",00";
		} else {
			String decimals = toStr.substring(index);
			while (decimals.length() < 3) {
				toStr = toStr + "0";
				decimals = toStr.substring(index);
			}
			if (decimals.length() > 3) {
				toStr = toStr.substring(0, index + 3);
			}
		}
		return toStr;
	}

}
