package com.skula.iry.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.skula.iry.models.Person;

public class ExcelService {

	public static List<Person> importList(String fileName) {
		List<Person> list = new ArrayList<Person>();

		try {
			File file = new File(fileName);
			FileInputStream myInput = new FileInputStream(file);
			POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

			HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
			HSSFSheet mySheet = myWorkBook.getSheetAt(0);

			Iterator<Row> rowIter = mySheet.rowIterator();
			int cpt = 0;
			while (rowIter.hasNext()) {
				HSSFRow myRow = (HSSFRow) rowIter.next();
				if (cpt > 0) {
					Person p = new Person();
					p.setLastname(myRow.getCell(0).getStringCellValue());
					p.setFirstname(myRow.getCell(1).getStringCellValue());
					p.setBirthday(myRow.getCell(2).getStringCellValue());
					p.setAddress(myRow.getCell(3).getStringCellValue());
					p.setZip(myRow.getCell(4).getStringCellValue());
					p.setCity(myRow.getCell(5).getStringCellValue());
					p.setFixnum(myRow.getCell(6).getStringCellValue());
					p.setCellnum(myRow.getCell(7).getStringCellValue());
					p.setEmail(myRow.getCell(8).getStringCellValue());
					p.setGroup(myRow.getCell(9).getStringCellValue());
					list.add(p);
				}
				cpt++;
			}
		} catch (Exception e) {

		}

		return list;
	}

	public static void exportList(String fileName, List<Person> persons) {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet1 = wb.createSheet("carnet");

		CellStyle cs = wb.createCellStyle();
		cs.setAlignment(CellStyle.ALIGN_CENTER);
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setBorderBottom(CellStyle.BORDER_THIN);
		cs.setBorderRight(CellStyle.BORDER_THIN);

		HSSFFont defaultFont = (HSSFFont) wb.createFont();
		defaultFont.setFontHeightInPoints((short) 10);
		defaultFont.setFontName("Arial");
		defaultFont.setColor(IndexedColors.BLACK.getIndex());
		defaultFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		cs.setFont(defaultFont);

		// Entêtes
		Row row = sheet1.createRow(0);

		Cell c = row.createCell(0);
		c.setCellValue("Nom");
		c.setCellStyle(cs);

		c = row.createCell(1);
		c.setCellValue("Prénom");
		c.setCellStyle(cs);

		c = row.createCell(2);
		c.setCellValue("Naissance");
		c.setCellStyle(cs);

		c = row.createCell(3);
		c.setCellValue("Adresse");
		c.setCellStyle(cs);

		c = row.createCell(4);
		c.setCellValue("Code postal");
		c.setCellStyle(cs);

		c = row.createCell(5);
		c.setCellValue("Ville");
		c.setCellStyle(cs);

		c = row.createCell(6);
		c.setCellValue("Num. fixe");
		c.setCellStyle(cs);

		c = row.createCell(7);
		c.setCellValue("Num. portable");
		c.setCellStyle(cs);

		c = row.createCell(8);
		c.setCellValue("Adresse mail");
		c.setCellStyle(cs);

		c = row.createCell(9);
		c.setCellValue("Groupe");
		c.setCellStyle(cs);

		// Liste de personnes
		cs = wb.createCellStyle();
		cs.setAlignment(CellStyle.ALIGN_LEFT);
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setBorderBottom(CellStyle.BORDER_THIN);
		cs.setBorderRight(CellStyle.BORDER_THIN);
		defaultFont = (HSSFFont) wb.createFont();
		defaultFont.setFontHeightInPoints((short) 10);
		defaultFont.setFontName("Arial");
		defaultFont.setColor(IndexedColors.BLACK.getIndex());
		defaultFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		cs.setFont(defaultFont);

		int cpt = 1;
		for (Person p : persons) {
			row = sheet1.createRow(cpt);
			c = row.createCell(0);
			c.setCellValue(p.getLastname());
			c.setCellStyle(cs);

			c = row.createCell(1);
			c.setCellValue(p.getFirstname());
			c.setCellStyle(cs);

			c = row.createCell(2);
			c.setCellValue(p.getBirthday());
			c.setCellStyle(cs);

			c = row.createCell(3);
			c.setCellValue(p.getAddress());
			c.setCellStyle(cs);

			c = row.createCell(4);
			c.setCellValue(p.getZip());
			c.setCellStyle(cs);

			c = row.createCell(5);
			c.setCellValue(p.getCity());
			c.setCellStyle(cs);

			c = row.createCell(6);
			c.setCellValue(p.getFixnum());
			c.setCellStyle(cs);

			c = row.createCell(7);
			c.setCellValue(p.getCellnum());
			c.setCellStyle(cs);

			c = row.createCell(8);
			c.setCellValue(p.getEmail());
			c.setCellStyle(cs);

			c = row.createCell(9);
			c.setCellValue(p.getGroup());
			c.setCellStyle(cs);
			cpt++;
		}

		sheet1.setColumnWidth(0, (15 * 250));
		sheet1.setColumnWidth(1, (15 * 350));
		sheet1.setColumnWidth(2, (15 * 200));
		sheet1.setColumnWidth(3, (15 * 450));
		sheet1.setColumnWidth(4, (15 * 200));
		sheet1.setColumnWidth(5, (15 * 350));
		sheet1.setColumnWidth(6, (15 * 250));
		sheet1.setColumnWidth(7, (15 * 250));
		sheet1.setColumnWidth(8, (15 * 600));
		sheet1.setColumnWidth(9, (15 * 250));

		// Create a path where we will place our List of objects on external
		// storage
		File file = new File(fileName);
		FileOutputStream os = null;

		try {
			os = new FileOutputStream(file);
			wb.write(os);
		} catch (Exception e) {
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (Exception ex) {
			}
		}
	}
}
