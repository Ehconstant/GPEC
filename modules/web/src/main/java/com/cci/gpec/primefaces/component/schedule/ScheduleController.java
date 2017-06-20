package com.cci.gpec.primefaces.component.schedule;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.SalarieBean;
import com.cci.gpec.commons.SalarieBeanExport;
import com.cci.gpec.commons.TypeAbsenceBean;
import com.cci.gpec.metier.implementation.AbsenceServiceImpl;
import com.cci.gpec.metier.implementation.EntrepriseServiceImpl;
import com.cci.gpec.metier.implementation.MetierServiceImpl;
import com.cci.gpec.metier.implementation.SalarieServiceImpl;
import com.cci.gpec.metier.implementation.ServiceImpl;
import com.cci.gpec.metier.implementation.TypeAbsenceServiceImpl;

public class ScheduleController {

	private static final long serialVersionUID = 1471445208603834854L;
	private Date dateDebut;
	private Date dateFin;
	private ArrayList<SalarieBeanExport> salarieBeanList;

	private static Color COLOR0 = Color.blue;
	private static Color COLOR1 = Color.cyan;
	private static Color COLOR2 = Color.green;
	private static Color COLOR3 = Color.magenta;
	private static Color COLOR4 = Color.pink;
	private static Color COLOR5 = Color.orange;
	private static Color COLOR6 = Color.red;
	private static Color COLOR7 = Color.yellow;

	// Bleu nuit
	private static Color COLOR8 = new Color(15, 5, 107);

	// Argent
	private static Color COLOR9 = new Color(206, 206, 206);

	// Framboise
	private static Color COLOR10 = new Color(199, 44, 72);

	// Vert pin
	private static Color COLOR11 = new Color(1, 121, 11);

	// Bleu cobalt
	private static Color COLOR12 = new Color(0, 71, 171);

	// Mauve (héraldique)
	private static Color COLOR13 = new Color(150, 85, 12);

	// Feu vif
	private static Color COLOR14 = new Color(255, 73, 1);

	// Marron
	private static Color COLOR15 = new Color(88, 41, 0);

	// Vert avocat
	private static Color COLOR16 = new Color(86, 130, 3);

	private static Color COLOR17 = Color.white;

	// Gris anthracite
	private static Color COLOR_DEFAULT = new Color(48, 48, 48);

	private TreeMap<String, Integer> treeMapTypeAbsence;
	private TreeMap<Integer, Color> treeMapColor;

	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext()
			.getSession(false);

	/**
	 * @throws Exception
	 */
	public ScheduleController() throws Exception {
		treeMapColor = new TreeMap<Integer, Color>();

		treeMapColor.put(0, COLOR0);
		treeMapColor.put(1, COLOR1);
		treeMapColor.put(2, COLOR2);
		treeMapColor.put(3, COLOR3);
		treeMapColor.put(4, COLOR4);
		treeMapColor.put(5, COLOR5);
		treeMapColor.put(6, COLOR6);
		treeMapColor.put(7, COLOR7);
		treeMapColor.put(8, COLOR8);
		treeMapColor.put(9, COLOR9);
		treeMapColor.put(10, COLOR10);
		treeMapColor.put(11, COLOR11);
		treeMapColor.put(12, COLOR12);
		treeMapColor.put(13, COLOR13);
		treeMapColor.put(14, COLOR14);
		treeMapColor.put(15, COLOR15);
		treeMapColor.put(16, COLOR16);
		treeMapColor.put(17, COLOR17);

		TypeAbsenceServiceImpl typeAbsenceService = new TypeAbsenceServiceImpl();

		treeMapTypeAbsence = new TreeMap<String, Integer>();
		int i = 0;

		ArrayList<TypeAbsenceBean> typeAbsenceBeanList = (ArrayList<TypeAbsenceBean>) typeAbsenceService
				.getTypeAbsenceList(Integer.parseInt(session.getAttribute(
						"groupe").toString()));

		for (TypeAbsenceBean typeAbsenceBean : typeAbsenceBeanList) {
			treeMapTypeAbsence.put(typeAbsenceBean.getNom(), i);
			i++;
		}

	}

	public byte[] createGantChartForFiltreMultiC(String title,
			ArrayList<SalarieBean> salarieBeanList, Integer idMonthSelected,
			Integer dureeSelected) throws Exception {

		Date dateToday = new Date();
		GregorianCalendar calendarToday = new GregorianCalendar();
		calendarToday.setTime(dateToday);
		int year = calendarToday.get(Calendar.YEAR);
		// int month = calendarToday.get(Calendar.MONTH);
		dateDebut = date(0, idMonthSelected, year);
		dateFin = date(1, idMonthSelected + dureeSelected, year);
		Collections.sort(salarieBeanList);

		GregorianCalendar dateDebutG = new GregorianCalendar();
		dateDebutG.setTime(dateDebut);
		GregorianCalendar dateFinG = new GregorianCalendar();
		dateFinG.setTime(dateFin);

		int yearTmp = year;
		if (dateDebutG.get(Calendar.YEAR) < dateFinG.get(Calendar.YEAR)) {
			yearTmp = dateFinG.get(Calendar.YEAR);
		}

		final XYTaskDataset dataset = createSampleDatasetForFiltreMultiC(
				salarieBeanList, idMonthSelected, dureeSelected, year, yearTmp);

		SimpleDateFormat formaterDate = new SimpleDateFormat("MMMMMMMMMMMMMMMM");
		title = title
				+ " de "
				+ formaterDate.format(date(1, idMonthSelected, year))
				+ " \u00E0 "
				+ formaterDate.format(date(1, idMonthSelected
						+ (dureeSelected - 1), year));

		// create the chart...
		final JFreeChart chart = ChartFactory.createGanttChart(title, // chart
				// title
				"", // domain axis label
				"Date des absences", // range axis label
				dataset.getTasks(), // data
				true, // include legend
				true, // tooltips
				false // urls
				);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();

		DateAxis dateAxis = (DateAxis) plot.getRangeAxis();
		dateAxis.setRange(dateDebut, dateFin);
		dateAxis.setDateFormatOverride(new SimpleDateFormat("dd MMM"));
		DateTickUnit dateTickUnit = new DateTickUnit(DateTickUnitType.DAY, 1);
		dateAxis.setTickUnit(dateTickUnit);
		dateAxis.setTickMarkPosition(DateTickMarkPosition.START);
		dateAxis.setVerticalTickLabels(true);

		plot.getDomainAxis().setCategoryMargin(0);
		plot.getDomainAxis().setLowerMargin(0.05);
		plot.getDomainAxis().setUpperMargin(0.05);
		GanttRenderer renderer = (GanttRenderer) plot.getRenderer();
		renderer.setItemMargin(0);

		for (int i = 0; i < dataset.getTasks().getRowKeys().size(); i++) {
			String nomTypeAbsence = dataset.getTasks().getRowKey(i).toString();

			Color color_asign = treeMapColor.get(treeMapTypeAbsence
					.get(nomTypeAbsence));

			if (color_asign != null) {
				renderer.setSeriesPaint(i, color_asign);
			} else {
				renderer.setSeriesPaint(i, COLOR_DEFAULT);
			}
		}

		for (int i = 0; i < dureeSelected; i++) {
			ValueMarker valueMarker = new ValueMarker(date(0,
					idMonthSelected + (i), year).getTime(), Color.blue,
					new BasicStroke(1.0F));
			plot.addRangeMarker(valueMarker);

		}
		int nbSalarie = dataset.getTasks().getColumnCount();
		if (nbSalarie == 0) {
			nbSalarie = 1;
		}

		// permet de trier la liste des salariés par ordre alphabétique
		Collections.sort(dataset.getTasks().getColumnKeys());
		for (int index = 0; index < nbSalarie;) {
			if (dataset.getTasks().getColumnKeys() != null
					&& !dataset.getTasks().getColumnKeys().isEmpty()) {
				Object valueCategory = dataset.getTasks().getColumnKeys()
						.get(index);
				CategoryMarker categorieMarker = new CategoryMarker(
						valueCategory.toString());
				categorieMarker.setLabel("");
				categorieMarker.setPaint(Color.WHITE);
				categorieMarker.setAlpha(0.8F);
				plot.addDomainMarker(categorieMarker, Layer.BACKGROUND);

			}
			index = index + 2;
		}

		byte[] tabByte = new byte[1];

		tabByte = ChartUtilities.encodeAsPNG(chart.createBufferedImage(
				505 * dureeSelected, (25 * nbSalarie) + 200));

		return tabByte;
	}

	public IntervalCategoryDataset createDataset(Integer[] idSelected,
			int idMonthSelected, int yearD, int yearF) throws Exception {

		TaskSeriesCollection dataset = new TaskSeriesCollection();

		SalarieServiceImpl salarieService = new SalarieServiceImpl();

		salarieBeanList = (ArrayList<SalarieBeanExport>) salarieService
				.getSalarieBeanListByIds(idSelected);
		Collections.sort(salarieBeanList);

		TaskSeries taskSeries = new TaskSeries("Périodes d'absences");

		for (SalarieBeanExport salarieBeanExport : salarieBeanList) {

			Collections.sort(salarieBeanExport.getAbsenceBeanList());
			AbsenceServiceImpl absenceService = new AbsenceServiceImpl();

			Calendar date1 = new GregorianCalendar();
			date1.set(1, 1, 1);
			Task t = new Task(salarieBeanExport.getNom() + " - "
					+ salarieBeanExport.getPrenom(), new SimpleTimePeriod(
					date1.getTime(), date1.getTime()));

			for (AbsenceBean absenceBean : absenceService
					.getAbsenceBeanListByIdSalarieAndYear(salarieBeanExport
							.getId())) {

				if (absenceBean.getDebutAbsence() != null
						&& absenceBean.getFinAbsence() != null) {
					GregorianCalendar dateDebutAbsenceG = new GregorianCalendar();
					dateDebutAbsenceG.setTime(absenceBean.getDebutAbsence());
					GregorianCalendar dateFinAbsenceG = new GregorianCalendar();
					dateFinAbsenceG.setTime(absenceBean.getFinAbsence());
					int monthDebutAbsence = dateDebutAbsenceG
							.get(Calendar.MONTH);
					int monthFinAbsence = dateFinAbsenceG.get(Calendar.MONTH);
					int yearFinAbsence = dateFinAbsenceG.get(Calendar.YEAR);
					int yearDebutAbsence = dateDebutAbsenceG.get(Calendar.YEAR);

					if ((yearDebutAbsence < yearF)
							|| (yearDebutAbsence == yearF && monthDebutAbsence <= idMonthSelected + 1)) {
						if ((yearFinAbsence > yearD)
								|| (yearFinAbsence == yearD && monthFinAbsence >= idMonthSelected)) {

							TypeAbsenceServiceImpl typeAbsenceService = new TypeAbsenceServiceImpl();
							List<TypeAbsenceBean> typeAbsenceBeanList = typeAbsenceService
									.getTypeAbsenceList(Integer
											.parseInt(session.getAttribute(
													"groupe").toString()));

							for (TypeAbsenceBean typeAbsenceBean : typeAbsenceBeanList) {

								if (typeAbsenceBean.getId() == absenceBean
										.getIdTypeAbsenceSelected().intValue()) {
									Task test;
									test = new Task(
											salarieBeanExport.getNom()
													+ " - "
													+ salarieBeanExport.getPrenom(),
											new SimpleTimePeriod(absenceBean
													.getDebutAbsence(),
													absenceBean.getFinAbsence()));
									t.addSubtask(test);
								} else {
									Task test = new Task(
											salarieBeanExport.getNom()
													+ " - "
													+ salarieBeanExport
															.getPrenom(),
											new SimpleTimePeriod(date1
													.getTime(), date1.getTime()));
									t.addSubtask(test);
								}

							}

						}
					}
				}
			}
			taskSeries.add(t);
		}
		TaskSeries taskSeries1 = new TaskSeries("");
		TaskSeries taskSeries2 = new TaskSeries("");
		dataset.add(taskSeries1);
		dataset.add(taskSeries1);
		dataset.add(taskSeries);
		dataset.add(taskSeries2);
		dataset.add(taskSeries2);

		return dataset;
	}

	class MyRenderer extends GanttRenderer {

		Map<Integer, Integer> listCol = new HashMap<Integer, Integer>();
		Map<Integer, Integer> listColOutline = new HashMap<Integer, Integer>();

		public MyRenderer() {
			super();
		}

		@Override
		public Paint getItemPaint(int row, int col) {
			if (listCol.get(col) == null) {
				listCol.put(col, 1);
			} else {
				listCol.put(col, listCol.get(col) + 1);
			}

			return treeMapColor.get((listCol.get(col) - 1) % 18);
		}

		@Override
		public Paint getItemOutlinePaint(int row, int col) {

			return treeMapColor.get((listCol.get(col) - 1) % 18);
		}

	}

	private static Date makeDate(final int day, final int month, final int year) {

		final Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		final Date result = calendar.getTime();
		return result;

	}

	public LegendItemSource getSource() {
		return new LegendItemSource() {
			public LegendItemCollection getLegendItems() {
				LegendItemCollection lic = new LegendItemCollection();
				TypeAbsenceServiceImpl typeAbsenceService = new TypeAbsenceServiceImpl();
				List<TypeAbsenceBean> typeAbsenceBeanList;
				try {
					typeAbsenceBeanList = typeAbsenceService
							.getTypeAbsenceList(Integer.parseInt(session
									.getAttribute("groupe").toString()));
					int i = 0;
					for (TypeAbsenceBean typeAbsenceBean : typeAbsenceBeanList) {
						LegendItem newItem = new LegendItem(
								typeAbsenceBean.getNom(), null, null, null,
								new Rectangle(10, 10), treeMapColor.get(i % 18));
						lic.add(newItem);
						i++;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return lic;
			}
		};
	}

	public LegendTitle getNewLegend(LegendItemSource source) {
		LegendTitle legendTitle = new LegendTitle(source);
		legendTitle.setItemFont(new Font("font", Font.PLAIN, 10));
		legendTitle.setLegendItemGraphicPadding(new RectangleInsets(0, 10, 0,
				10));
		legendTitle.setPosition(RectangleEdge.BOTTOM);
		legendTitle.setHorizontalAlignment(HorizontalAlignment.CENTER);
		legendTitle.setPadding(10, 10, 10, 10);
		legendTitle.setBorder(new BlockBorder());
		return legendTitle;
	}

	/**
	 * Creates a Gantt chart based on input data set
	 */
	public byte[] createGantChart(String title, Integer[] idSelected,
			Integer idMonthSelected, Integer idYearSelected) throws Exception {

		int year = idYearSelected;
		if (idMonthSelected == 0 || idMonthSelected == 1
				|| idMonthSelected == 2 || idMonthSelected == 3
				|| idMonthSelected == 5 || idMonthSelected == 7
				|| idMonthSelected == 8 || idMonthSelected == 10) {
			if (idMonthSelected == 0) {
				dateDebut = date(1, idMonthSelected, year);
				dateFin = date(0, idMonthSelected + 2, year);
			}
			if (idMonthSelected == 1) {
				dateDebut = date(1, idMonthSelected, year);
				dateFin = date(0, idMonthSelected + 2, year);
			}
			if (idMonthSelected == 2) {
				dateDebut = date(1, idMonthSelected, year);
				dateFin = date(0, idMonthSelected + 2, year);
			}
			if (idMonthSelected == 3) {
				dateDebut = date(1, idMonthSelected, year);
				dateFin = date(0, idMonthSelected + 2, year);
			}
			if (idMonthSelected == 5) {
				dateDebut = date(1, idMonthSelected, year);
				dateFin = date(0, idMonthSelected + 2, year);
			}
			if (idMonthSelected == 7) {
				dateDebut = date(1, idMonthSelected, year);
				dateFin = date(0, idMonthSelected + 2, year);
			}
			if (idMonthSelected == 8) {
				dateDebut = date(1, idMonthSelected, year);
				dateFin = date(0, idMonthSelected + 2, year);
			}
			if (idMonthSelected == 10) {
				dateDebut = date(1, idMonthSelected, year);
				dateFin = date(0, idMonthSelected + 2, year);
			}
		} else {
			dateDebut = date(0, idMonthSelected, year);
			dateFin = date(0, idMonthSelected + 2, year);
		}

		GregorianCalendar dateDebutG = new GregorianCalendar();
		dateDebutG.setTime(dateDebut);
		GregorianCalendar dateFinG = new GregorianCalendar();
		dateFinG.setTime(dateFin);

		int yearTmp = year;
		if (dateDebutG.get(Calendar.YEAR) < dateFinG.get(Calendar.YEAR)) {
			yearTmp = dateFinG.get(Calendar.YEAR);
		}

		IntervalCategoryDataset dataset = createDataset(idSelected,
				idMonthSelected, year, yearTmp);

		SimpleDateFormat formaterDateMonth = new SimpleDateFormat(
				"MMMMMMMMMMMMMMMM");
		EntrepriseServiceImpl entServ = new EntrepriseServiceImpl();
		ServiceImpl servServ = new ServiceImpl();
		MetierServiceImpl metServ = new MetierServiceImpl();

		String nomEntreprise = "";
		String nomService = "";
		String nomMetier = "";

		if (idSelected[0] != null && idSelected[0] > 0) {
			nomEntreprise = entServ.getEntrepriseBeanById(idSelected[0])
					.getNom();
			if (idSelected[1] != null && idSelected[1] > 0) {
				nomService = " - "
						+ servServ.getServiceBeanById(idSelected[1]).getNom();
				if (idSelected[2] != null && idSelected[2] > 0)
					nomMetier = " - "
							+ metServ.getMetierBeanById(idSelected[2]).getNom();
			}
		}
		String filtre = "\n" + nomEntreprise + nomService + nomMetier + "";

		title = title + " "
				+ formaterDateMonth.format(date(1, idMonthSelected, 2010))
				+ "-"
				+ formaterDateMonth.format(date(1, idMonthSelected + 1, 2010))
				+ " " + year + filtre;

		final JFreeChart chart = ChartFactory.createGanttChart(title, // chart
				// title
				"", // domain axis label
				" ", // range axis label
				dataset, // data
				true, // include legend
				true, // tooltips
				false // urls
				);

		chart.removeLegend();
		chart.addLegend(getNewLegend(getSource()));

		CategoryPlot plot = (CategoryPlot) chart.getPlot();

		DateAxis dateAxis = (DateAxis) plot.getRangeAxis();
		dateAxis.setRange(dateDebut, dateFin);
		dateAxis.setDateFormatOverride(new SimpleDateFormat("dd"));
		DateTickUnit dateTickUnit = new DateTickUnit(DateTickUnitType.DAY, 5);
		dateAxis.setTickUnit(dateTickUnit);
		dateAxis.setTickMarkPosition(DateTickMarkPosition.START);
		dateAxis.setVerticalTickLabels(true);

		plot.getDomainAxis().setCategoryMargin(0);
		plot.getDomainAxis().setLowerMargin(0.05);
		plot.getDomainAxis().setUpperMargin(0.05);
		plot.setOutlinePaint(null);
		plot.setOutlineVisible(true);

		plot.setRenderer(new MyRenderer());
		GanttRenderer renderer = (GanttRenderer) plot.getRenderer();
		renderer.setItemMargin(0);
		renderer.setDrawBarOutline(true);
		renderer.setMinimumBarLength(100.0);
		renderer.setOutlineStroke(new BasicStroke(4.0F));
		renderer.setShadowVisible(false);

		renderer.setSeriesPaint(0, Color.WHITE);
		renderer.setSeriesOutlinePaint(0, Color.WHITE);

		renderer.setSeriesPaint(1, Color.WHITE);
		renderer.setSeriesOutlinePaint(1, Color.WHITE);

		renderer.setSeriesPaint(3, Color.WHITE);
		renderer.setSeriesOutlinePaint(3, Color.WHITE);

		renderer.setSeriesPaint(4, Color.WHITE);
		renderer.setSeriesOutlinePaint(4, Color.WHITE);

		SimpleDateFormat formaterDate = new SimpleDateFormat("MMMMMMMMM yyyy");
		ValueMarker valueMarker = new ValueMarker(date(0, idMonthSelected + 1,
				year).getTime(), Color.blue, new BasicStroke(1.0F));
		plot.addRangeMarker(valueMarker);

		int nbSalarie = dataset.getColumnCount();
		if (nbSalarie == 0) {
			nbSalarie = 1;
		}
		// permet de trier la liste des salariés par ordre alphabétique
		Collections.sort(dataset.getColumnKeys());
		for (int index = 0; index < nbSalarie;) {
			if (dataset.getColumnKeys() != null
					&& !dataset.getColumnKeys().isEmpty()) {
				Object valueCategory = dataset.getColumnKeys().get(index);
				CategoryMarker categorieMarker = new CategoryMarker(
						valueCategory.toString());
				categorieMarker.setLabel("");
				categorieMarker.setPaint(Color.WHITE);
				categorieMarker.setAlpha(0.8F);
				plot.addDomainMarker(categorieMarker, Layer.BACKGROUND);
			}
			index = index + 2;
		}

		byte[] tabByte = new byte[1];
		tabByte = ChartUtilities.encodeAsPNG(chart.createBufferedImage(1010,
				(25 * dataset.getColumnCount()) + 200));
		return tabByte;

	}

	public void saveChart(JFreeChart chart, String fileLocation) {
		String fileName = fileLocation;
		try {
			/**
			 * This utility saves the JFreeChart as a JPEG First Parameter:
			 * FileName Second Parameter: Chart To Save Third Parameter: Height
			 * Of Picture Fourth Parameter: Width Of Picture
			 */
			ChartUtilities.saveChartAsJPEG(new File(fileName), chart, 800, 600);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Problem occurred creating chart.");
		}
	}

	/**
	 * Testing the Gantt Chart Creation
	 */

	/**
	 * @param dataset
	 * @param test
	 * @param salarieBeanExport
	 * @param absenceBean
	 */
	private void constructGantt(TaskSeriesCollection dataset, boolean test,
			SalarieBean salarieBeanExport, AbsenceBean absenceBean) {
		int rowIndex = dataset.getRowIndex(absenceBean.getNomTypeAbsence());
		if (rowIndex != -1) {
			Task taskTmp = null;

			TaskSeries taskSeries = dataset.getSeries(rowIndex);
			for (int i = 0; i < taskSeries.getTasks().size(); i++) {
				taskTmp = (Task) taskSeries.getTasks().get(i);

				if (!taskTmp.getDescription().equals(
						salarieBeanExport.getNom() + " - "
								+ salarieBeanExport.getPrenom())) {

					taskTmp = new Task(salarieBeanExport.getNom() + " - "
							+ salarieBeanExport.getPrenom(),
							new SimpleTimePeriod(absenceBean.getDebutAbsence(),
									absenceBean.getFinAbsence()));
					taskSeries.add(taskTmp);

					test = true;

				} else {

					Task task = taskTmp;

					Date dateStart = task.getDuration().getStart();
					Date dateEnd = task.getDuration().getEnd();

					if (taskSeries.getTasks().size() == 1) {
						task.addSubtask(new Task(salarieBeanExport.getNom()
								+ " - " + salarieBeanExport.getPrenom(),
								new SimpleTimePeriod(dateStart, dateEnd)));
					}
					task.addSubtask(new Task(salarieBeanExport.getNom() + " - "
							+ salarieBeanExport.getPrenom(),
							new SimpleTimePeriod(absenceBean.getDebutAbsence(),
									absenceBean.getFinAbsence())));
				}

			}

			if (!test) {
				if (taskSeries.getTasks().size() == 0) {
					taskSeries.add(new Task(salarieBeanExport.getNom() + " - "
							+ salarieBeanExport.getPrenom(),
							new SimpleTimePeriod(absenceBean.getDebutAbsence(),
									absenceBean.getFinAbsence())));
				} else {
					Task task = taskTmp;

					Date dateStart = task.getDuration().getStart();
					Date dateEnd = task.getDuration().getEnd();

					if (taskSeries.getTasks().size() == 1) {
						task.addSubtask(new Task(salarieBeanExport.getNom()
								+ " - " + salarieBeanExport.getPrenom(),
								new SimpleTimePeriod(dateStart, dateEnd)));
					}
					task.addSubtask(new Task(salarieBeanExport.getNom() + " - "
							+ salarieBeanExport.getPrenom(),
							new SimpleTimePeriod(absenceBean.getDebutAbsence(),
									absenceBean.getFinAbsence())));
				}
			}
		}
	}

	private XYTaskDataset createSampleDatasetForFiltreMultiC(
			ArrayList<SalarieBean> salarieBeanList, int idMonthSelected,
			int dureeSelected, int yearD, int yearF) throws Exception {

		TaskSeriesCollection dataset = new TaskSeriesCollection();

		boolean test = false;
		for (SalarieBean salarieBean : salarieBeanList) {
			Collections.sort(salarieBean.getAbsenceBeanList());
			AbsenceServiceImpl absenceService = new AbsenceServiceImpl();

			for (AbsenceBean absenceBean : absenceService
					.getAbsenceBeanListByIdSalarieAndYear(salarieBean.getId(),
							yearD, yearF)) {
				GregorianCalendar dateDebutAbsenceG = new GregorianCalendar();
				dateDebutAbsenceG.setTime(absenceBean.getDebutAbsence());
				GregorianCalendar dateFinAbsenceG = new GregorianCalendar();
				dateFinAbsenceG.setTime(absenceBean.getFinAbsence());
				int monthDebutAbsence = dateDebutAbsenceG.get(Calendar.MONTH);
				int yearDebutAbsence = dateDebutAbsenceG.get(Calendar.YEAR);
				int monthFinAbsence = dateFinAbsenceG.get(Calendar.MONTH);

				if ((idMonthSelected + dureeSelected > 12)
						&& (yearD < yearDebutAbsence && yearDebutAbsence <= yearF)) {
					if ((0 <= monthDebutAbsence && monthDebutAbsence < idMonthSelected
							+ dureeSelected - 11)) {
						if (dataset
								.getRowIndex(absenceBean.getNomTypeAbsence()) == -1) {
							dataset.add(new TaskSeries(absenceBean
									.getNomTypeAbsence()));
						}
					}
				} else {
					// Ajout de "(idMonthSelected <= monthFinAbsence &&
					// monthFinAbsence <idMonthSelected+2)"
					// Pour prendre en compte les absences qui auraient
					// commencees un ou des mois anterieur(s) par rapport au
					// mois selectionne
					if ((idMonthSelected <= monthFinAbsence && monthFinAbsence < idMonthSelected + 2)
							|| (idMonthSelected <= monthDebutAbsence && monthDebutAbsence < (idMonthSelected + dureeSelected))) {
						if (dataset
								.getRowIndex(absenceBean.getNomTypeAbsence()) == -1) {
							dataset.add(new TaskSeries(absenceBean
									.getNomTypeAbsence()));
						}
					}
				}

				if (absenceService.getAbsenceBeanListByIdSalarieAndYear(
						salarieBean.getId(), yearD, yearF).isEmpty()) {
					dataset.add(new TaskSeries(""));
				}

			}

			for (AbsenceBean absenceBean : salarieBean.getAbsenceBeanList()) {
				test = false;
				GregorianCalendar dateDebutAbsenceG = new GregorianCalendar();
				dateDebutAbsenceG.setTime(absenceBean.getDebutAbsence());
				GregorianCalendar dateFinAbsenceG = new GregorianCalendar();
				dateFinAbsenceG.setTime(absenceBean.getFinAbsence());
				int monthFinAbsence = dateFinAbsenceG.get(Calendar.MONTH);
				int monthDebutAbsence = dateDebutAbsenceG.get(Calendar.MONTH);
				int yearDebutAbsence = dateDebutAbsenceG.get(Calendar.YEAR);

				if ((idMonthSelected + dureeSelected > 12)
						&& (yearD < yearDebutAbsence && yearDebutAbsence <= yearF)) {
					if ((0 <= monthDebutAbsence && monthDebutAbsence < idMonthSelected
							+ dureeSelected - 11)) {
						constructGantt(dataset, test, salarieBean, absenceBean);
					}
				} else {
					// Ajout de "(idMonthSelected <= monthFinAbsence &&
					// monthFinAbsence <idMonthSelected+2)"
					// Pour prendre en compte les absences qui auraient
					// commencees un ou des mois anterieur(s) par rapport au
					// mois selectionne
					if ((idMonthSelected <= monthFinAbsence && monthFinAbsence < idMonthSelected + 2)
							|| (idMonthSelected <= monthDebutAbsence && monthDebutAbsence < (idMonthSelected + dureeSelected))) {
						constructGantt(dataset, test, salarieBean, absenceBean);
					}
				}
			}
			if (salarieBean.getAbsenceBeanList().isEmpty()) {
				constructGantt(dataset, test, salarieBean, null);
			}
		}

		XYTaskDataset xYTaskDataset = new XYTaskDataset(dataset);
		return xYTaskDataset;

	}

	/**
	 * Utility method for creating <code>Date</code> objects.
	 * 
	 * @param day
	 *            the date.
	 * @param month
	 *            the month.
	 * @param year
	 *            the year.
	 * 
	 * @return a date.
	 */
	private static Date date(int day, int month, int year) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		Date result = calendar.getTime();
		return result;

	}

}
