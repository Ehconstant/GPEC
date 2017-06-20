package com.cci.gpec.primefaces.component.schedule;

import java.util.ArrayList;
import java.util.Date;

import com.cci.gpec.commons.AbsenceBean;
import com.cci.gpec.commons.GanttChartBean;
import com.cci.gpec.commons.SalarieBeanExport;

public class GanttChartBeanMaker {
	
	public ArrayList<GanttChartBean> getGanttChartData(ArrayList<SalarieBeanExport> salarieBeanList) {
		ArrayList<GanttChartBean> ganttChartDataList = new ArrayList<GanttChartBean>();
		
		
//		Calendar calendar = Calendar.getInstance();
//		int i = 0;
		for (SalarieBeanExport salarieBeanExport: salarieBeanList) {
			
			for(AbsenceBean absenceBean :salarieBeanExport.getAbsenceBeanList()){
//				calendar.set(Calendar.HOUR_OF_DAY,);
	
				Date startDate = absenceBean.getDebutAbsence();
	
//				calendar.set(Calendar.HOUR_OF_DAY, i * 3);
	
				Date endDate = absenceBean.getFinAbsence();
	
//				String series = (i % 2 == 0) ? "High Priority" : "Normal";
	
				String series = absenceBean.getNomTypeAbsence();
				
				ganttChartDataList.add(create(series, salarieBeanExport.getNom()+" - "+salarieBeanExport.getPrenom(), startDate,
						endDate,absenceBean.getNomTypeAbsence()));
			}	
			
		}

		return ganttChartDataList;
	}

	private GanttChartBean create(String series, String task, Date startDate,
			Date endDate, String subtask) {
		GanttChartBean ganttChartBean = new GanttChartBean();

		ganttChartBean.setSeries(series);
		ganttChartBean.setTask(task);
		ganttChartBean.setSubtask(subtask);
		ganttChartBean.setStartDate(startDate);
		ganttChartBean.setEndDate(endDate);

		return ganttChartBean;
	}
}
