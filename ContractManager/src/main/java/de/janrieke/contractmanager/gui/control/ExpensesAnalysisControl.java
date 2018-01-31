/*
 *   This file is part of ContractManager for Jameica.
 *   Copyright (C) 2010-2011  Jan Rieke
 *
 *   ContractManager is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   ContractManager is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Partially copied from Hibiscus/Syntax, (c) by willuhn.webdesign
 */
package de.janrieke.contractmanager.gui.control;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import de.janrieke.contractmanager.gui.parts.ExpensesAnalysisChartPart;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.SpinnerInput;

/**
 * Controller fuer den Dialog Lizenzinformationen.
 */
public class ExpensesAnalysisControl extends AbstractControl {

	private Map<String, Integer> monthNames;
	private SelectInput monthSelector;
	private SpinnerInput yearSelector;
	private ExpensesAnalysisChartPart chart = null;
	private CheckboxInput usePayDayCheckbox;

	/**
	 * ct.
	 *
	 * @param view
	 */
	public ExpensesAnalysisControl(AbstractView view) {
		super(view);
		Calendar cal = Calendar.getInstance();
		monthNames = cal.getDisplayNames(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
	}

	public Part getChartPart() {
		if (chart == null) {
			chart = new ExpensesAnalysisChartPart(this);
		}
		return chart;
	}

	public int getMonthNumber (String name) {
		return monthNames.get(name);
	}
	public void redrawChart() {
		if (chart == null) {
			getChartPart();
		}
		chart.redraw();
	}

	public SelectInput getMonthSelector() {
		if (monthSelector == null) {
			int curMonth = Calendar.getInstance().get(Calendar.MONTH);
			String curMonthName = null;
			List<String> list = new ArrayList<>();
			for (int i = 0; i<12; i++) {
				outer:
					for (Map.Entry<String, Integer> entry : monthNames.entrySet()) {
						if (entry.getValue().equals(i)) {
							if (curMonth == i) {
								curMonthName = entry.getKey();
							}
							list.add(entry.getKey());
							continue outer;
						}
					}
			}
			monthSelector = new SelectInput(list, curMonthName);
		}
		return monthSelector;
	}

	public SpinnerInput getYearSelector() {
		if (yearSelector == null) {
			yearSelector = new SpinnerInput(1900, 3000, Calendar.getInstance().get(Calendar.YEAR));
		}
		return yearSelector;
	}

	public CheckboxInput getPayDayCheckbox() {
		if (usePayDayCheckbox == null) {
			usePayDayCheckbox = new CheckboxInput(true);
		}
		return usePayDayCheckbox;
	}

	public void resetToCurrentMonth() {
		int curMonth = Calendar.getInstance().get(Calendar.MONTH);
		String currentMonth = monthNames.entrySet().stream()
				.filter(e -> e.getValue().equals(curMonth))
				.map(Entry::getKey)
				.findFirst().orElseGet(monthNames.keySet().iterator()::next);
		getMonthSelector().setValue(currentMonth);

		getYearSelector().setValue(Calendar.getInstance().get(Calendar.YEAR));
	}
}