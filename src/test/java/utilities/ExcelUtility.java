package utilities;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class ExcelUtility {

    private static Workbook workbook;

    // Load the Excel file
    public static void loadExcel(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(new File("E:\\OrengeHRM.xlsx"));
        workbook = new XSSFWorkbook(fis);
    }

    // Get data from a specific sheet and row
    public static String getCellData(int sheetIndex, int rowNum, int colNum) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        Row row = sheet.getRow(rowNum);
        Cell cell = row.getCell(colNum);
        return cell.getStringCellValue();
    }

    // Get total number of rows
    public static int getRowCount(int sheetIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        return sheet.getPhysicalNumberOfRows();
    }

    // Close the workbook after use
    public static void closeWorkbook() throws IOException {
        workbook.close();
    }
}

