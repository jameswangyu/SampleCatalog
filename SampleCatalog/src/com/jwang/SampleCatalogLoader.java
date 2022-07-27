package com.jwang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * 
 * @author james wang
 * 
 * This class loads a sample catalog file and parses the data into a sorted list
 * It validates data and print out validation failures
 * It uses a hashmap to avoid loading duplicate, but does not report duplicates.
 * It filters out any product whose type is not "Paper".
 */
public class SampleCatalogLoader {
	public static final String delimiter = ",";
	public static final int VALIDATION_ERROR_NO_VALUE = 1;
	public static final int VALIDATION_ERROR_FIELD_TOO_LONG = 2;
	public static final int VALIDATION_ERROR_PRICE_LT_ZERO = 3;
	public static final int VALIDATION_ERROR_PERCENT_GT_ONE = 4;
	public static final int VALIDATION_ERROR_NO_ERROR = 0;
	
	public void loadFile(String csvFile) {
		HashMap<Long, SampleCatalogDTO> catalogMap = new HashMap<Long, SampleCatalogDTO>();
		
	    try {
	         File file = new File(csvFile);
	         FileReader fr = new FileReader(file);
	         BufferedReader br = new BufferedReader(fr);
	         String line = "";
	         String[] tempArr;
	         boolean headerLine = true;
	         int rowCount = 1;
	         while((line = br.readLine()) != null) {
	        	 if (headerLine) {
	        		 headerLine = false;
	        		 continue;
	        	 }
		       
	        	rowCount ++;
	            tempArr = line.split(delimiter);
            
	            try {
	            	SampleCatalogDTO catalopDto = loadCatalogLine(tempArr);
	            	
	            	if ("Paper".equals(catalopDto.getProductType())) {
	            		catalogMap.put (Long.valueOf(catalopDto.getProductId()), catalopDto);
	            	}
	            	
	            } catch (ValidationException ex) {
	            	System.out.println("Row " + rowCount + " contains invalid data: " +ex.getValidationErrorMessage());
	            }

	         }
	         br.close();
	         
	        		 
    		 List<SampleCatalogDTO> catalogList =
    		    new ArrayList<SampleCatalogDTO>(catalogMap.values());
	         	         
	         Collections.sort( catalogList );
	         
	         for (SampleCatalogDTO cat : catalogList) {
	         		System.out.println(cat.toString());
	         }
	         
	     } catch(IOException ioe) {
	            ioe.printStackTrace();
	     }
	}
	
	/**
	 * Method loadCatalogLine
	 * 
	 * @param data array
	 * @return SampleCatalogDTO
	 * @throws ValidationException
	 */
	protected SampleCatalogDTO loadCatalogLine (String [] data) throws ValidationException {
		SampleCatalogDTO catalogdto = new SampleCatalogDTO();
		
		int validationResult = validateIdField(Long.valueOf(data[0]));
		if (validationResult == 0) {
		    catalogdto.setProductId(Long.valueOf(data[0]));
		} else {
			throw new ValidationException("Product Id", validationResult);
		}
		 
		validationResult = validateStringField(data[1], 50);
		if (validationResult == 0) {
		    catalogdto.setProductName (data[1]);
		} else {
			throw new ValidationException("Product Name", validationResult);
		}
		
		validationResult = validateStringField(data[2], 30);
		if (validationResult == 0) {
		    catalogdto.setProductType (data[2]);
		} else {
			throw new ValidationException("Product Type", validationResult);
		}
				
		catalogdto.setUnitOfMeasure(data[3]);
		
		validationResult = validatePriceField(Double.valueOf(data[4]));
		if (validationResult == 0) {
		    catalogdto.setUnitPrice (Double.valueOf(data[4]));
		} else {
			throw new ValidationException("Unit Price", validationResult);
		}
		
		validationResult = validatePercentField(Double.valueOf(data[5]));
		if (validationResult == 0) {
		    catalogdto.setDiscountPercent(Double.valueOf(data[5]));
		} else {
			throw new ValidationException("Discount", validationResult);
		}
		
		return catalogdto;
	}
	
	public int validateIdField (Long fieldValue) {
		if (fieldValue == null) {
			return VALIDATION_ERROR_NO_VALUE;
		}
		
		return VALIDATION_ERROR_NO_ERROR;
	}
	
	public int validateStringField (String fieldValue, int maxLength) {
		if (fieldValue == null || fieldValue.isEmpty()) {
			return VALIDATION_ERROR_NO_VALUE;
		} else if (fieldValue.length() > maxLength) {
			return VALIDATION_ERROR_FIELD_TOO_LONG;
		}
		
		return VALIDATION_ERROR_NO_ERROR;
	}
	
	public int validatePriceField (Double fieldValue) {
		if (fieldValue == null) {
			return VALIDATION_ERROR_NO_VALUE;
		} else if (fieldValue < 0) {
			return VALIDATION_ERROR_PRICE_LT_ZERO;
		}
		return VALIDATION_ERROR_NO_ERROR;
	}
	
	public int validatePercentField (Double fieldValue) {
		if (fieldValue == null) {
			return VALIDATION_ERROR_NO_VALUE;
		} else if (fieldValue > 1) {
			return VALIDATION_ERROR_PERCENT_GT_ONE;
		}
		return VALIDATION_ERROR_NO_ERROR;
	}
	   
	public static void main(String[] args) {
		SampleCatalogLoader loader = new SampleCatalogLoader();

	    String csvFile = "C:\\Users\\wangandshi\\eclipse-workspace\\SampleCatalog\\target\\classes\\com\\jwang\\SampleCatalog.csv";
	    loader.loadFile(csvFile);	   
	}
}
