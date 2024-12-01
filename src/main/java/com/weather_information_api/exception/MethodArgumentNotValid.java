package com.weather_information_api.exception;

public class MethodArgumentNotValid extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MethodArgumentNotValid(String message) {
        super(message);
    }

}
