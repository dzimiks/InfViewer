package report;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import models.Warehouse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class ReportActionListenerKaca implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		ParamDialogKaca paramsDialog = new ParamDialogKaca();
		paramsDialog.setVisible(true);

		Integer id = paramsDialog.getId();
		Connection conn = Warehouse.getInstance().getDbConnection();
		JasperDesign jasperDesign;
		Map<String, Object> mpParams = new HashMap<String, Object>();
		mpParams.put("Identifikator", id);

		try {
			jasperDesign = JRXmlLoader.load("reports/reportKata.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, mpParams, conn);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "reports/reportKata.pdf");
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "reports/reportKata.pdf");
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
			e1.printStackTrace();
		}
	}

}
