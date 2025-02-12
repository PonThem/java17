package com.valuelab.common.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.validation.ObjectError;

public class ValueLabForm {

	// システムエラーコード
	protected List<String> systemErrorCodes = new ArrayList<String>();

	public List<String> getSystemErrorCodes() {
		return this.systemErrorCodes;
	}

	public void addSystemErrorCode(String systemErrorCode) {
		this.systemErrorCodes.add(systemErrorCode);
	}

	public Boolean getHasSystemError() {
		return !this.systemErrorCodes.isEmpty();
	}

	// エラーコード
	protected List<String> errorCodes = new ArrayList<String>();

	public List<String> getErrorCodes() {
		return this.errorCodes;
	}

	public void addErrorCode(String errorCode) {
		this.errorCodes.add(errorCode);
	}

	public void addErrorCodes(List<ObjectError> errors) {
		for (ObjectError error : errors) {
			this.errorCodes.add(error.getCode());
		}
	}

	public Boolean getHasError() {
		return !this.errorCodes.isEmpty();
	}

	protected Map<String, String> paramCheckErrorCodes = new HashMap<String, String>();

	public Map<String, String> getParamCheckErrorCodes() {
		return this.paramCheckErrorCodes;
	}

	public void setParamCheckErrorCode(Map<String, String> paramCheckErrorCode) {
		this.paramCheckErrorCodes = paramCheckErrorCode;
	}
}
