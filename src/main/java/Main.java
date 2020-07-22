import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
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
    private static List<Product> products = new ArrayList<>();
    private static List<String> productNames = new ArrayList<>();
    private static List<Hospital> result = new ArrayList<>();

    private static List<String> hospitalCategory = new ArrayList<>();

    private static Hospital currentItem;
    static Path toPath = dataDir.resolve("data.json");
    static Path fromPath = dataDir.resolve("hostipals.xlsx");


    public static void main(String[] args) throws IOException {

        if(fromPath == null){
            System.out.println("no file found");
            return;
        }

        System.out.println(fromPath.toString());

        productNames.add("CSS");
        productNames.add("VALVE");
        productNames.add("AB");
        productNames.add("LINER");
        productNames.add("기타");

        try
        {
            File file = new File(fromPath.toString());
            //creating a new file instance
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file
            //creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);

//            produce products that have fixed size
            for(int i = 0; i < productNames.size(); i++){
                Product product = new Product();
                product.setName(productNames.get(i));
                products.add(product);
            }
            int index = 0;

            for(XSSFSheet sheet : wb){
                sheetName.add(sheet.getSheetName());
                System.out.println(sheet.getSheetName());
                currentItem = new Hospital();
                currentItem.setId(String.valueOf(index++));

                for(int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++){
                    Row row = sheet.getRow(rowIndex);
                    System.out.println(rowIndex);
//                  check if the sheet is end
                    if(row == null || rowIndex == 0 || row.getCell(0) == null || row.getCell(0).getStringCellValue().equals("")){
                        continue;
                    }

//                  check if in NAMESROWSECTOR
//                  retrieve hospital name and coded products
                    if(rowIndex == NAMESROWSECTOR){
                        for(int colIndex = 0; colIndex < row.getLastCellNum(); colIndex+=COLUMNINTERVAL){
                            if(colIndex > row.getLastCellNum())
                                break;
                            Integer mergedCellIndex = getIndexIfCellIsInMergedCells(sheet, rowIndex, colIndex);
                            System.out.println("row Index: " + rowIndex + "col Index: " + colIndex);
                            CellRangeAddress cell = sheet.getMergedRegion(mergedCellIndex);

                            if(colIndex == 0){
                                String hospitalName = readContentFromMergedCells(sheet, cell);
                                currentItem.setName(hospitalName);
                                continue;
                            }

                        }
                        continue;
                    }

//                  check if in CATEGORYROWSECTOR
//                  retrieve all categories
                    if(rowIndex == CATEGORYROWSECTOR){
                        for(int colIndex = 0; colIndex < COLUMNINTERVAL; colIndex++){
//                              hospital variables section
                            Cell cell = sheet.getRow(rowIndex).getCell(colIndex);
                            hospitalCategory.add(cell.getStringCellValue());
                        }
                        continue;
                    }

//                  fetch data
//                  should produce each department
                    Department department = new Department();
                    department.setId(String.valueOf(rowIndex-3));

                    for(int colIndex = 0; colIndex < COLUMNINTERVAL; colIndex++){
                        Cell cell = row.getCell(colIndex);
                        String content = cell.getStringCellValue();
                        if(content.equals("") || content == null)
                            content = "";

                        String category = hospitalCategory.get(colIndex);
                        System.out.println(category);
                        if(category.equals("구분")){
                            department.setCategory(content);
                            System.out.println(content + " is set");
                        }else if(category.equals("부서")){
                            department.setName(content);
                            System.out.println(content + " is set");
                        }else if(category.equals("위치")){
                            department.setLocation(content);
                            System.out.println(content + " is set");
                        }else{
                            if(content.equals("") || content == null){
                                department.setNurse("");
                                System.out.println(content + " is set");
                            }
                            else
                                department.setNurse(content);
                        }
                    }


                    department.addAllProducts(products);
                    System.out.println("End of row");
//                    if(department.getCodedProducts() == null){
//                        System.out.println("products are null");
//                    }else
//                        System.out.println("#of products: " + department.getCodedProducts().size());
                    System.out.println(department.toString());
                    currentItem.addDepartment(department);
                    if(currentItem.getDepartments().size() > 42)
                        System.out.println(currentItem.getDepartments().get(42));
                }
                System.out.println("#of dept: " + currentItem.getDepartments().size());
                hospitalCategory.clear();
                result.add(currentItem);

            }

//          Log
            System.out.println("hospital size: " + result.size());
            produceJson(result);
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void produceJson(List<Hospital> result){
        JSONObject finalCollection = new JSONObject();
        JSONObject rootCollection = new JSONObject();
        JSONArray hospitalArray = new JSONArray();


        for(int i = 0; i < result.size(); i++){
            System.out.println("hospital index: " + i);
            Hospital currentItem = result.get(i);
            JSONObject hospitalObject = new JSONObject();
            JSONArray departmentArray = new JSONArray();
            for(int j = 0; j < currentItem.getDepartments().size(); j++){
                System.out.println("department index: " + j);
                Department department = currentItem.getDepartments().get(j);
                JSONObject departmentObj = new JSONObject();
                JSONArray productArray = new JSONArray();
                for(int k = 0; k < department.getCodedProducts().size(); k++){
                    System.out.println("product index: " + k);
                    Product product = department.getCodedProducts().get(k);
                    JSONObject obj = new JSONObject();
                    obj.put("contract", product.getContract());
                    obj.put("sample", product.getSample());
                    obj.put("intro", product.getIntro());
                    obj.put("name", product.getName());
                    obj.put("revisit", product.getRevisit());
                    obj.put("id", String.valueOf(k));
                    productArray.add(obj);
                }

                departmentObj.put("category", department.getCategory());
                departmentObj.put("location", department.getLocation());
                departmentObj.put("id", department.getId());
                departmentObj.put("nurse", department.getNurse());
                departmentObj.put("name", department.getName());
                JSONObject productCollection = new JSONObject();
                productCollection.put("products", productArray);
                departmentObj.put("__collections__", productCollection);
                departmentArray.add(departmentObj);
            }
            hospitalObject.put("id", currentItem.getId());
            hospitalObject.put("name", currentItem.getName());
            JSONObject departmentCollection = new JSONObject();
            departmentCollection.put("departments", departmentArray);
            hospitalObject.put("__collections__", departmentCollection);
            hospitalArray.add(hospitalObject);
        }
        rootCollection.put("testHospitals", hospitalArray);
        finalCollection.put("__collections__", rootCollection);

//        System.out.println(finalCollection);
        try{
            FileWriter file = new FileWriter(toPath.toString());
            file.write(finalCollection.toString());
            file.flush();

        }catch (Exception e){
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
