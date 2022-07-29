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
 * It filters out any product whose description or category does not contain word "Paper".
 */
public class SampleCatalogLoader {
	public static final String delimiter = ",";
	public static final int VALIDATION_ERROR_NO_VALUE = 1;
	public static final int VALIDATION_ERROR_FIELD_TOO_LONG = 2;
	public static final int VALIDATION_ERROR_PRICE_LT_ZERO = 3;
	public static final int VALIDATION_ERROR_PERCENT_GT_ONE = 4;
	public static final int VALIDATION_ERROR_IS_NOT_A_NUMBER = 5;
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
	            	
	            	if (catalopDto.getDescription().toUpperCase().contains("PAPER") 
	            			|| catalopDto.getCategory().toUpperCase().contains("PAPER")) {
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
	 * Gets data array and load it into a DTO.
	 * 
	 * @param data array
	 * @return SampleCatalogDTO
	 * @throws ValidationException
	 */
	protected SampleCatalogDTO loadCatalogLine (String [] data) throws ValidationException {
		SampleCatalogDTO catalogdto = new SampleCatalogDTO();
		
		int validationResult = validateIdField(data[0]);
		if (validationResult == 0) {
		    catalogdto.setProductId(Long.valueOf(data[0]));
		} else {
			throw new ValidationException("Product Id", validationResult);
		}
		 
		
		validationResult = validateStringField(data[1], 150);
		if (validationResult == 0) {
		    catalogdto.setDescription (data[1]);
		} else {
			throw new ValidationException("Description", validationResult);
		}
		
		validationResult = validateStringField(data[2], 80);
		if (validationResult == 0) {
		    catalogdto.setCategory(data[2]);
		} else {
			throw new ValidationException("Category", validationResult);
		}
				
		catalogdto.setUnitOfMeasure(data[3]);
		
		validationResult = validatePriceField(data[4]);
		if (validationResult == 0) {
		    catalogdto.setUnitPrice (Double.valueOf(data[4]));
		} else {
			throw new ValidationException("Unit Price", validationResult);
		}
		
		//Last element may be empty. treat it as zero
		String discoountField;
		if (data.length < 6) {
			discoountField = "0";
		} else {
			discoountField = data[5];
		}
		
		validationResult = validatePercentField(discoountField);
		if (validationResult == 0) {
			Double discount = convertToDecimalNumber(discoountField);
		    catalogdto.setDiscountPercent(discount);
		} else {
			throw new ValidationException("Discount", validationResult);
		}
		
		return catalogdto;
	}
	
	/**
	 * Validate ID field: cannot be empty and must be a number
	 * 
	 * @param fieldValue
	 * @return
	 */
	public int validateIdField (String fieldValue) {
		if (fieldValue == null || fieldValue.isEmpty()) {
			return VALIDATION_ERROR_NO_VALUE;
		} else if (!fieldValue.matches("^[0-9]+$")) {
			return  VALIDATION_ERROR_IS_NOT_A_NUMBER;
		} 
		
		return VALIDATION_ERROR_NO_ERROR;
	}
	
	/**
	 * Validate text field: cannot be empty and exceed a max length.
	 * 
	 * @param fieldValue
	 * @param maxLength
	 * @return
	 */
	public int validateStringField (String fieldValue, int maxLength) {
		if (fieldValue == null || fieldValue.isEmpty()) {
			return VALIDATION_ERROR_NO_VALUE;
		} else if (fieldValue.length() > maxLength) {
			return VALIDATION_ERROR_FIELD_TOO_LONG;
		}
		
		return VALIDATION_ERROR_NO_ERROR;
	}
	
	/**
	 * Validate price field: must be a positive number
	 * 
	 * @param fieldValue
	 * @return
	 */
	public int validatePriceField (String fieldValue) {
		 
		if (fieldValue == null || fieldValue.isEmpty()) {
			return VALIDATION_ERROR_NO_VALUE;
		} else  {
			if (fieldValue.matches("\\d+(\\.\\d+)?")	) {
				Double price = Double.valueOf(fieldValue);
				if (price < 0) {
					return VALIDATION_ERROR_PRICE_LT_ZERO;
				}
			} else {
				return VALIDATION_ERROR_IS_NOT_A_NUMBER;
			}
			
		}
		return VALIDATION_ERROR_NO_ERROR;
	}
	
	/**
	 * Validate percent: must be a number less than one.
	 * 
	 * @param fieldValue
	 * @return
	 */
	public int validatePercentField (String fieldValue) {
		if (fieldValue == null || fieldValue.isEmpty()) {
			return VALIDATION_ERROR_NO_VALUE;
		} else {
			if (fieldValue.endsWith("%")) {
				fieldValue = fieldValue.replace("%", "");
			}
			
			if (fieldValue.matches("\\d+(\\.\\d+)?")	) {
				Double discount = Double.valueOf(fieldValue)/100;
				if (discount > 1) {
					return VALIDATION_ERROR_PERCENT_GT_ONE;
				}
			} else {
				return VALIDATION_ERROR_IS_NOT_A_NUMBER;
			}

		}
		return VALIDATION_ERROR_NO_ERROR;
	}
	
	public Double convertToDecimalNumber(String discountStr) {
		discountStr = discountStr.replace("%", "");
		return Double.valueOf(discountStr) / 100;
	}
	   
	public static void main(String[] args) {
		SampleCatalogLoader loader = new SampleCatalogLoader();

	    String csvFile = "C:\\Users\\wangandshi\\eclipse-workspace\\SampleCatalog\\src\\com\\jwang\\catalog.csv";
	    loader.loadFile(csvFile);	   
	}
}
