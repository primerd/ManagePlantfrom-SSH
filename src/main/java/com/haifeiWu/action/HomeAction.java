package com.haifeiWu.action;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.haifeiWu.utils.PropertiesReadUtils;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 主页面 的设计
 * 
 * @author wuhaifei
 * 
 * @date 2016年8月9日
 */
@Controller
@Scope("prototype")
public class HomeAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware, ServletContextAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2941802033175754434L;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ServletContext application;

	// private static final String title = PropertiesReadUtils
	// .getTitleString("text");

	public String top() {
		// 读取配置文件中的公安局名称
		String title = PropertiesReadUtils.getTitleString("title");
		String name = PropertiesReadUtils.getTitleString("name");
		request.setAttribute("title", title);
		request.setAttribute("name", name);

		return "top";
	}

	public String left() {
		return "left";
	}

	public String index() throws IOException {
		// 获取其他页面错误信息
		// String errorMessage = (String) request.getAttribute("errorMessage");
		// System.out.println("----------------------" + errorMessage);
		// if (!(errorMessage == null || errorMessage.equals(""))) {
		// System.out.println("----------------------ffffff" + errorMessage);
		// response.getWriter().write(
		// "<script type='text/javascript'>alert(" + errorMessage
		// + ");</script>");
		// response.getWriter().flush();
		// return "index";
		// }
		// 向客户端输出cookie
		Cookie cookie = new Cookie("ip", request.getRemoteAddr());
		cookie.setMaxAge(24 * 60 * 60 * 7);// 七天
		response.addCookie(cookie);
		// 读取websocket的路径
		request.setAttribute("webSocket",
				PropertiesReadUtils.getRecordConfString("webSocket"));
		return "index";
	}

	public String main() {
		// 读取配置文件中的公安局名称
		String main = PropertiesReadUtils.getTitleString("main");
		request.setAttribute("main", main);
		return "main";
	}

	@Override
	public void setServletContext(ServletContext application) {
		this.application = application;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
