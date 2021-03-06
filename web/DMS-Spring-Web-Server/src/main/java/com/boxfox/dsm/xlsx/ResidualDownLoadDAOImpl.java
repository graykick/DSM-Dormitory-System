package com.boxfox.dsm.xlsx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.boxfox.dms.mapper.UserMapper;

import org.apache.ibatis.session.SqlSession;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import com.boxfox.dms.users.dto.UserDataDTO;

//2016.03.12
//need refactoring

@Repository
public class ResidualDownLoadDAOImpl {
	private static final String FORMAT_XLSX_FILE = "�ܷ���������.xlsx";
	private static final String FILE_PATH = "files/";
	private static final String[] RESIDUAL_TYPE = new String[] { "�ݿ�Ͱ�", "���Ͱ�", "���ͻ�", "�ܷ�" };

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private SqlSession sqlSession;

	private HashMap<String, String> map = new HashMap();

	public InputStream readExcel(String date) {
		initResidualMaps(date);
		try {
			File xlsxFile = resourceLoader.getResource("classpath:�ܷ���������.xlsx").getFile();
			XSSFWorkbook workbook = processXlsx(xlsxFile);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private XSSFWorkbook processXlsx(File input) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(input));
		int rowindex = 0;
		int columnindex = 0;
		XSSFSheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		for (rowindex = 1; rowindex < rows; rowindex++) {
			XSSFRow row = sheet.getRow(rowindex);
			if (row != null) {
				int cells = row.getPhysicalNumberOfCells();
				for (columnindex = 0; columnindex <= cells; columnindex++) {
					XSSFCell cell = row.getCell(columnindex);
					if (cell != null && cell.getCellType() == 0) {
						String sNum = (String.valueOf((int)cell.getNumericCellValue()));
						if (sNum != null) {
							cell = row.getCell(++columnindex);
							if (cell.getCellType() == 1) {
								String type = map.get(sNum);
								if (type != null) {
									cell = row.getCell(++columnindex);
									cell.setCellValue(type);
								}
							}
						}
					}
				}
			}
		}
		return workbook;
	}

	private void initResidualMaps(String date) {
		UserMapper userMapper = (UserMapper) sqlSession.getMapper(UserMapper.class);
		List<UserDataDTO> list = userMapper.residual();
		
		for (int i = 0; i < list.size(); i++) {
			String typeStr = null;
			Integer type = userMapper.residualAtWeek(list.get(i).getId(), date);
			if (type == null) {
				type = list.get(i).getResidualDefault();
			}
			typeStr = RESIDUAL_TYPE[type - 1];
			map.put(list.get(i).getNumber() + "", typeStr);
		}
	}

}