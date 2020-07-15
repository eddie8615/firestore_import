import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    private static final int COLUMNINTERVAL = 4;
    private static final int NAMESROWSECTOR = 1;
    private static final int CATEGORYROWSECTOR = 2;

    private static final Path dataDir = Paths.get("dataset");
    private static List<String> sheetName = new ArrayList<>();
    private static Map<String, List<String>> header = new HashMap<>();
    private static List<String> products = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        Path fromPath = dataDir.resolve("hostipals.xlsx");
        if(fromPath == null){
            System.out.println("no file found");
            return;
        }

        System.out.println(fromPath.toString());

        try
        {
            File file = new File(fromPath.toString());
            //creating a new file instance
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file
            //creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            for(XSSFSheet sheet : wb){
                sheetName.add(sheet.getSheetName());

                for(int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++){
                    Row row = sheet.getRow(rowIndex);

//                  check if the sheet is end
                    if(row == null || row.getCell(0) == null || row.getCell(0).getStringCellValue().equals("")){
                        continue;
                    }

//                  check if in NAMESROWSECTOR
                    if(rowIndex == NAMESROWSECTOR){
                        
                    }

//                  check if in CATEGORYROWSECTOR
                    if(rowIndex == CATEGORYROWSECTOR){

                    }

//                  fetch data
                    for(int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++){
                        Integer mergedCellIndex = getIndexIfCellIsInMergedCells(sheet, rowIndex, colIndex);
                        System.out.println("Row:" + rowIndex + ", Column: " + colIndex);
                        if(mergedCellIndex != null){
                            System.out.println(mergedCellIndex);

                            CellRangeAddress cell = sheet.getMergedRegion(mergedCellIndex);
//                          Debugging

                            System.out.println("merged region first row: " + cell.getFirstRow());
                            System.out.println("merged region last row: " + cell.getLastRow());
                            System.out.println("merged region first column: " + cell.getFirstColumn());
                            System.out.println("merged region last column: " + cell.getLastColumn());

                            System.out.println("Content: " + readContentFromMergedCells(sheet, cell));
                            System.out.println("end");
                            //my logic here

                            int sizeOfMergedCell = sheet.getMergedRegion(mergedCellIndex).getLastColumn() - sheet.getMergedRegion(mergedCellIndex).getFirstColumn();

                            //skip those merged cells
                            colIndex += sizeOfMergedCell;

                        }
                        else{
                            Cell cell = row.getCell(colIndex, Row.RETURN_BLANK_AS_NULL);
                            if(cell == null)
                                continue;

                            System.out.println(cell.getStringCellValue());

                        }
                    }
                }
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static Integer getIndexIfCellIsInMergedCells(Sheet sheet, int row, int column) {
        int numberOfMergedRegions = sheet.getNumMergedRegions();

        for (int i = 0; i < numberOfMergedRegions; i++) {
            CellRangeAddress mergedCell = sheet.getMergedRegion(i);


            if (mergedCell.isInRange(row, column)) {
                return i;
            }
        }

        return null;
    }

    private static String readContentFromMergedCells(Sheet sheet, CellRangeAddress mergedCells) {

        if (mergedCells.getFirstRow() != mergedCells.getLastRow()) {
            return null;
        }

        return sheet.getRow(mergedCells.getFirstRow()).getCell(mergedCells.getFirstColumn()).getStringCellValue();
    }
}
