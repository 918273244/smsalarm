package com.xsxx.constants;

/**
 * AJAX 错误号，理论上需要按照：  错误号{5位} = 模块编号{2} + 错误编号{3}
 * 这里从110开始往后递增编号就可以了，
 */
public enum ErrorCode {
	NOT_LOGIN("未登录", -1),
	ERROR_ACCOUNT("账号信息错误",1),
	NOT_ALLOWED("账号权限不足",2),
	CODE("未定义错误", 110),
	LOGIN_PASSWORD_ERROR("用户名密码错误",111);

	private int code;
	private String msg;
	private ErrorCode(String msg, int code){
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}
