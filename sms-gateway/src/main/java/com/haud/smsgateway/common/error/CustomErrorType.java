package com.haud.smsgateway.common.error;

public class CustomErrorType {
	private String message;
	private String code;
	private String error;
	private String status;
	private String resource;

	public CustomErrorType(String message, String code, String error, String status, String resource) {
		this.message = message;
		this.code = code;
		this.error = error;
		this.status = status;
		this.resource = resource;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
}