package com.jwang;

public class ValidationException extends Exception {
	String validationErrorMessage;
	
	public ValidationException (String fieldName, int validationErrorCode) {
		switch( validationErrorCode) {
		case 1:
			validationErrorMessage = fieldName + " has No value";
	        break;
		case 2:
			validationErrorMessage = fieldName + " Value is too long";
			break;
		case 3:
			validationErrorMessage = fieldName + " Value is negative";
			break;
		case 4: 
			validationErrorMessage = fieldName + " Value is not a percentage";
			break;
		case 5:
			validationErrorMessage = fieldName + " value is not a number";
			break;
	    default:
	    	validationErrorMessage = fieldName + " Unknown error";
		}
	}
	
	public String getValidationErrorMessage () {
		return validationErrorMessage;
	}
}
