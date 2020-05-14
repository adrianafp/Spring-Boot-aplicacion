package com.adrianaf.springBoot_aplicacion.Exceptions;

public class CustomFieldValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2361090119769482085L;
	private String fieldName;
	
	public CustomFieldValidationException(String message, String fieldName) {
		super(message);
		this.fieldName=fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}
}
