package com.hfp.entity;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UserInfo implements Serializable{
	@NotNull(message="姓名不能为空")
	@Length(max = 256, message = "姓名长度不得大于{max}位")
	private String name;
	
	@NotNull
    @Length(min = 8, max = 8, message = "日期格式为YYYYMMDD")
	private String birthday;
	
	
	@NotNull
	@Digits(integer = 9, fraction = 0, message = "金额格式不对")
    private String payAmt;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPayAmt() {
		return payAmt;
	}

	public void setPayAmt(String payAmt) {
		this.payAmt = payAmt;
	}

	@Override
	public String toString() {
		return "UserInfo [name=" + name + ", birthday=" + birthday + ", payAmt=" + payAmt + "]";
	}
	
	
	
}
