package com.haifeiWu.action;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.haifeiWu.base.BaseAction;
import com.haifeiWu.entity.PHCSMP_Staff;
import com.haifeiWu.service.UserService;

/**
 * 
 * @author wuhaifei
 * 
 * @date 2016年8月9日
 */
@Controller
@Scope("prototype")
public class UserAction extends BaseAction<PHCSMP_Staff> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1489295867312851923L;

	private Logger logger = Logger.getLogger(UserAction.class);

	@Autowired
	private UserService userService;

	/**
	 * 用户登录功能的实现
	 * 
	 * @return
	 */
	public String login() {
		PHCSMP_Staff user = userService.findUserByStaffNameAndPwd(
				model.getStaff_Name(), model.getPassWord());

		if (user != null) {
			request.getSession().setAttribute("user", user);
			System.out.println("用户登录……\n\n");

			logger.info("用户 " + user.getStaff_Name() + " 登录系统，时间："
					+ new DateTime().toString("yyyy-MM-dd hh:mm a E"));

			return "login";
		} else {
			addFieldError("loginError", "用户名或密码不正确！");
			System.out.println("用户名或密码不正确！");
			return "loginError";
		}
	}

	/**
	 * 用户安全退出
	 * 
	 * @return
	 */
	public String logout() {
		request.getSession().removeAttribute("users");
		logger.info("用户 " + " 注销系统，时间："
				+ new DateTime().toString("yyyy-MM-dd hh:mm a E"));
		return "logout";
	}
}