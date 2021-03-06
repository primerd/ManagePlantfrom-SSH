package com.haifeiWu.service;

import com.haifeiWu.entity.PHCSMP_Staff;

/**
 * 用户service
 * 
 * @author wuhaifei
 * @d2016年11月28日
 */
public interface UserService {

	/**
	 * 根据用户名密码查找用户，用于用户登录
	 * 
	 * @param staff_Name
	 *            用户名
	 * @param passWord
	 *            密码
	 * @return
	 */
	PHCSMP_Staff findUserByStaffNameAndPwd(String staff_Name, String passWord);

}
