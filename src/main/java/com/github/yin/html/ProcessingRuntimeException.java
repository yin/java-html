package com.github.yin.html;

public class ProcessingRuntimeException extends RuntimeException {
	public ProcessingRuntimeException() {
	}

	public ProcessingRuntimeException(String message) {
		super(message);
	}

	public ProcessingRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProcessingRuntimeException(Throwable cause) {
		super(cause);
	}

	public ProcessingRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
