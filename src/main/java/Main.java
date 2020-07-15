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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    private static final Path dataDir = Paths.get("dataset");
    private static List<String> sheetName = new ArrayList<>();
    private static Map<String, List<String>> header = new HashMap<>();
    private static List<String> products = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        Path fromPath = dataDir.resolve(args[0]);
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

                for(int rowIndex = 0; rowIndex < sheet.getLastRowNum(); rowIndex++){
                    Row row = sheet.getRow(rowIndex);
                    if(row == null)
                        continue;

                    for(int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++){
                        Integer mergedCellIndex = getIndexIfCellIsInMergedCells(sheet, rowIndex, colIndex);

                        if(mergedCellIndex != null){
                            CellRangeAddress cell = sheet.getMergedRegion(mergedCellIndex);
                            //my logic here

                            int lastIndexOfMergedCell = sheet.getMergedRegion(mergedCellIndex).getLastColumn();

                            //skip those merged cells
                            colIndex += lastIndexOfMergedCell - 1;

                        }
                        else{
                            Cell cell = row.getCell(colIndex, Row.RETURN_BLANK_AS_NULL);
                            if(cell == null)
                                continue;


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
