import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;

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
    private static List<Hospital> result = new ArrayList<>();

    private static List<String> hospitalCategory = new ArrayList<>();
    private static List<String> productCategory = new ArrayList<>();

    private static Hospital currentItem;

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
                currentItem = new Hospital();
                currentItem.setId(UUID.randomUUID().toString());

                for(int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++){
                    Row row = sheet.getRow(rowIndex);

//                  check if the sheet is end
                    if(row == null || row.getCell(0) == null || row.getCell(0).getStringCellValue().equals("")){
                        continue;
                    }

//                  check if in NAMESROWSECTOR
//                  retrieve hospital name and coded products
                    if(rowIndex == NAMESROWSECTOR){
                        for(int colIndex = 0; colIndex < row.getLastCellNum(); colIndex+=COLUMNINTERVAL){
                            Integer mergedCellIndex = getIndexIfCellIsInMergedCells(sheet, rowIndex, colIndex);
                            CellRangeAddress cell = sheet.getMergedRegion(mergedCellIndex);

                            if(colIndex == 0){
                                String hospitalName = readContentFromMergedCells(sheet, cell);
                                currentItem.setName(hospitalName);
                                continue;
                            }

                            String product = readContentFromMergedCells(sheet, cell);
                            products.add(product);
                        }
                        continue;
                    }

//                  check if in CATEGORYROWSECTOR
//                  retrieve all categories
                    if(rowIndex == CATEGORYROWSECTOR){
                        for(int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++){
                            if(colIndex < COLUMNINTERVAL){
//                              hospital variables section
                                Cell cell = sheet.getRow(rowIndex).getCell(colIndex);
                                hospitalCategory.add(cell.getStringCellValue());
                            }
                            if(colIndex < 8){
                                Cell cell = sheet.getRow(rowIndex).getCell(colIndex);
                                productCategory.add(cell.getStringCellValue());
                            }
                        }
                        continue;
                    }

//                  fetch data
//                  should produce each department
                    Department department = new Department();
                    department.setId(UUID.randomUUID().toString());
                    for(int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++){
                        Cell cell = row.getCell(colIndex);
                        String content = cell.getStringCellValue();
                        if(colIndex < COLUMNINTERVAL){
                            String category = hospitalCategory.get(colIndex);

                            if(category.equals("구분")){
                                department.setCategory(content);
                            }else if(category.equals("부서")){
                                department.setName(content);
                            }else if(category.equals("위치")){
                                department.setLocation(content);
                            }else{
                                if(content.equals("") || content == null){
                                    department.setNurse("");
                                }
                                else
                                    department.setNurse(content);
                            }
                        }
                        else{
                            String category = productCategory.get(colIndex % 4);
                            if(category.equals("사용부서")){
                                department.setCategory(content);
                            }else if(category.equals("방문")){
                                department.setName(content);
                            }else if(category.equals("샘플")){
                                department.setLocation(content);
                            }else{
                                if(content.equals("관심도")){
                                    department.setNurse("");
                                }
                                else
                                    department.setNurse(content);
                            }
                        }
                    }
                    currentItem.addDepartment(department);
                }
            }

            JSONObject object = new JSONObject();
            
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
