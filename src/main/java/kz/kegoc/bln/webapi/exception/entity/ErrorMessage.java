package kz.kegoc.bln.webapi.exception.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"errCode"})
public class ErrorMessage {	

	public ErrorMessage() { }
	
	public ErrorMessage(String errMsg) {
		super();
		this.errMsg = errMsg;
	}

	public ErrorMessage(String errCode, String errMsg) {
		super();
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
	
	private String errMsg;
	private String errCode;
}
