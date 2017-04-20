package com.haifeiWu.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.util.ServletContextAware;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.haifeiWu.entity.PHCSMP_Activity_Record;
import com.haifeiWu.entity.PHCSMP_BelongingS;
import com.haifeiWu.entity.PHCSMP_Information_Collection;
import com.haifeiWu.entity.PHCSMP_Leave_Record;
import com.haifeiWu.entity.PHCSMP_Personal_Check;
import com.haifeiWu.entity.PHCSMP_Suspect;
import com.haifeiWu.entity.Temporary_Leave;
import com.haifeiWu.service.ActivityRecordService;
import com.haifeiWu.service.BelongingInforService;
import com.haifeiWu.service.InformationCollectionService;
import com.haifeiWu.service.LeaveRecodService;
import com.haifeiWu.service.PersonalCheckService;
import com.haifeiWu.service.SuspectService;
import com.haifeiWu.service.TemporaryLeaveService;
import com.haifeiWu.utils.PageBean;
import com.haifeiWu.utils.PropertiesReadUtils;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 嫌疑人信息管理action，待查嫌疑人信息，历史嫌疑人信息/demoone
 * 
 * @author wuhaifei
 * @d2016年10月17日
 */
@Controller
@Scope("prototype")
public class SuspectManageAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware, ServletContextAware {

	/**
	 * UUID
	 */
	private static final long serialVersionUID = 1L;

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ServletContext application;
	protected String detainTime;

	public ServletContext getApplication() {
		return application;
	}

	public void setApplication(ServletContext application) {
		this.application = application;
	}

	public String getDetainTime() {
		return detainTime;
	}

	public void setDetainTime(String detainTime) {
		this.detainTime = detainTime;
	}

	public BelongingInforService getBelongingInforService() {
		return belongingInforService;
	}

	public void setBelongingInforService(BelongingInforService belongingInforService) {
		this.belongingInforService = belongingInforService;
	}

	public PersonalCheckService getPersonalCheckService() {
		return personalCheckService;
	}

	public void setPersonalCheckService(PersonalCheckService personalCheckService) {
		this.personalCheckService = personalCheckService;
	}

	public ActivityRecordService getActivityRecordService() {
		return activityRecordService;
	}

	public void setActivityRecordService(ActivityRecordService activityRecordService) {
		this.activityRecordService = activityRecordService;
	}

	public InformationCollectionService getInformationCollectionService() {
		return informationCollectionService;
	}

	public void setInformationCollectionService(
			InformationCollectionService informationCollectionService) {
		this.informationCollectionService = informationCollectionService;
	}

	public LeaveRecodService getLeaveRecodService() {
		return leaveRecodService;
	}

	public void setLeaveRecodService(LeaveRecodService leaveRecodService) {
		this.leaveRecodService = leaveRecodService;
	}

	@Autowired
	private SuspectService suspectService;// 嫌疑人信息管理
	@Autowired
	private BelongingInforService belongingInforService;
	// 嫌疑人的人身检查信息
	@Autowired
	private PersonalCheckService personalCheckService;
	// 询问讯问记录信息登记
	@Autowired
	private ActivityRecordService activityRecordService;
	// 信息采集信息登记
	@Autowired
	private InformationCollectionService informationCollectionService;
	// 嫌疑人出区信息登记
	@Autowired
	private LeaveRecodService leaveRecodService;
	@Autowired
	private TemporaryLeaveService temporaryLeaveService;
	
	private int page; //当前页的页码

	/**
	 * 加载嫌疑人信息
	 * 
	 * @return
	 */
	public String loadInfor() {
		System.out.println("历史记录，待办信息");
		// 获取待查嫌疑人信息
		List<PHCSMP_Suspect> suspectCheckInfor = suspectService
				.getOnPoliceSuspect();
		// 获取出区嫌疑人数据
		List<PHCSMP_Suspect> suspectCheckedInfor = suspectService
				.getLeavePoliceSuspect();
		// List<PHCSMP_Leave_Record>
		if ((suspectCheckInfor == null) || (suspectCheckedInfor == null)) {
			return "NULL";
		}
		// 将信息放入到request中
		request.setAttribute("suspectCheckInfor", suspectCheckInfor);
		request.setAttribute("suspectCheckedInfor", suspectCheckedInfor);
		System.out.println("----------待查的" + suspectCheckInfor);
		System.out.println("----------历史的----" + suspectCheckedInfor);

		for (PHCSMP_Suspect phcsmp_Suspect : suspectCheckedInfor) {
			System.err.println(phcsmp_Suspect.toString());
		}

		// getDistanceTime(suspectCheckedInfor., suspectCheckedInfor.get(14));
		return "loadInfor";
	}

	/**
	 * 根据姓名或者档案编号查找嫌疑人信息
	 * 
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public String searchsuspectInfor() throws ParseException, IOException {
		String searchInfor = request.getParameter("searchInfor");
		/*
		 * 通过正则表达式来区分嫌疑人姓名
		 */
		List<PHCSMP_Suspect> suspect = null;
		List<PHCSMP_Suspect> suspectNow = null;
		Pattern p = Pattern.compile("^[\u4E00-\u9FA5]+$");
		Matcher m = p.matcher(searchInfor);
		boolean result = m.find();
		char fir = searchInfor.charAt(0);
		if (fir < 130) {
			if (fir < 60) {
				// 非数组类型的身份证号查找
				 suspect = suspectService
						.findByCardId(searchInfor);
				 suspectNow = suspectService
						.findByCardIdNow(searchInfor);
				System.out.println("身份证号：" + searchInfor);
				request.setAttribute("suspect", suspect);
				request.setAttribute("suspectNow", suspectNow);
				System.out.println(suspect);
			} else {
				// 根据档案编号查询嫌疑人信息
				 suspect = suspectService
						.serachInforBySuspectId(searchInfor);
				 suspectNow = suspectService
						.serachInforBySuspectIdNow(searchInfor);
				System.out.println("档案编号：" + searchInfor);
				request.setAttribute("suspect", suspect);
				request.setAttribute("suspectNow", suspectNow);
				System.out.println(suspect);
				System.out.println(suspectNow);
			}
		}
		else if (result) {
			 suspect = suspectService
					.findBySuspectName(searchInfor);
			 suspectNow = suspectService
					.finBySuspectNameNow(searchInfor);
			request.setAttribute("suspect", suspect);
			request.setAttribute("suspectNow", suspectNow);
			System.out.println("嫌疑人姓名：" + searchInfor);
			System.out.println(suspect);
			System.out.println(suspectNow);
		}
		if(suspect.isEmpty() && (!suspectNow.isEmpty() && suspectNow.size()==1)){
			return toGR(suspectNow.get(0).getSuspect_ID());
		}
		else{
			return "searchsuspectInfor";
		}
	}

	/***
	 * 录像下载失败的嫌疑人信息
	 * 
	 * @return
	 */

	public String videoDownFailList() {
		List<String> leaveTimeList = new ArrayList<String>();
		try {
			List<PHCSMP_Suspect> suspectList = suspectService
					.findAllVideoDownloadFailSuspectInfor();
			for (int i = 0; i < suspectList.size(); i++) {

				String suspectId = suspectList.get(i).getSuspect_ID();
				System.out.println("suspectId" + suspectId);
				String leavaTime = leaveRecodService.findLeaveRecordInfor(
						suspectId).getLeave_Time();
				System.out.println("leavetime=" + leavaTime);
				leaveTimeList.add(leavaTime);
				System.out.println("leavetime=" + leaveTimeList.get(i));
			}
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

			for (int i = 0; i < suspectList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("suspect_Name", suspectList.get(i).getSuspect_Name());
				map.put("suspect_ID", suspectList.get(i).getSuspect_ID());
				map.put("enter_Time", suspectList.get(i).getEnter_Time());
				map.put("identifyCard_Number", suspectList.get(i)
						.getIdentifyCard_Number());
				map.put("leave_Time", leaveTimeList.get(i));
				list.add(map);

			}
			request.setAttribute("suspect", list);
			return "videoDownFail";
		} catch (Exception e) {
			e.printStackTrace();
			return "videoDownFail";
		}

	}

	public String videoDownSuccessList() {
		List<String> leaveTimeList = new ArrayList<String>();
		try {
			List<PHCSMP_Suspect> suspectList = suspectService
					.findAllByIsRecordVedio();
			for (int i = 0; i < suspectList.size(); i++) {

				String suspectId = suspectList.get(i).getSuspect_ID();
				System.out.println("suspectId" + suspectId);
				String leavaTime = leaveRecodService.findLeaveRecordInfor(
						suspectId).getLeave_Time();
				System.out.println("leavetime=" + leavaTime);
				leaveTimeList.add(leavaTime);
				System.out.println("leavetime=" + leaveTimeList.get(i));
			}
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

			for (int i = 0; i < suspectList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("suspect_Name", suspectList.get(i).getSuspect_Name());
				map.put("suspect_ID", suspectList.get(i).getSuspect_ID());
				map.put("enter_Time", suspectList.get(i).getEnter_Time());
				map.put("identifyCard_Number", suspectList.get(i)
						.getIdentifyCard_Number());
				map.put("leave_Time", leaveTimeList.get(i));
				map.put("vedio_number", suspectList.get(i).getVedio_Number());
				list.add(map);

			}
			request.setAttribute("suspect", list);
			return "videoDownSuccess";
		} catch (Exception e) {
			e.printStackTrace();
			return "videoDownSuccess";
		}

	}
	
	/**
	 * 历史嫌疑人分页显示
	 */
	@Override
	public String execute() throws Exception {
		//获取历史嫌疑人数据，在页面显示数量
		List<PHCSMP_Suspect> suspectCheckedInfor = suspectService
				.getLeavePoliceSuspect();
		request.setAttribute("suspectCheckedInfor", suspectCheckedInfor);
		// 表示每页显示5条记录，page表示当前网页
		PageBean pageBean = suspectService.getPageBean(10, page);

		HttpServletRequest request = ServletActionContext.getRequest();

		request.setAttribute("pageBean", pageBean);

		return SUCCESS;
	}
	
	/**
	 * 跳转到入区人员信息总汇
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	private String toGR(String suspectId) throws ParseException, IOException {
		try {
			// System.out.println("嫌疑人入区信息报告");
			//
			// System.out.println("嫌疑人姓名：" +
			// request.getParameter("personName"));
			// System.out.println("档案编号：" + request.getParameter("suspectID"));

			/*
			 * 加载当前嫌疑人的所有的信息
			 */
			// 获取档案编号
			// String suspectId = (String) request.getParameter("suspectID");
			// if (suspectId == null) {
			// return "NULL";
			// }
			// 查找嫌疑人入区信息
			PHCSMP_Suspect suspect = suspectService.findBySuspetcId(suspectId);
			// 嫌疑人随身所有物品检查信息s
			List<PHCSMP_BelongingS> belongingS = belongingInforService
					.selectBelongInfor(suspectId);
			// 嫌疑人人身检查信息
			PHCSMP_Personal_Check personal_Check = personalCheckService
					.findInforBySuspetcId(suspectId);
			// 嫌疑人所有的办案区记录信息
			List<PHCSMP_Activity_Record> activity_Record = activityRecordService
					.selectActivityRecordInfor(suspectId);

			// 嫌疑人信息采集记录
			PHCSMP_Information_Collection information_Collection = informationCollectionService
					.findInforBySuspetcId(suspectId);
			System.out.println("information_Collection="
					+ information_Collection);

			// 嫌疑人离区信息记录
			PHCSMP_Leave_Record leave_Record = leaveRecodService
					.findInforBySuspetcId(suspectId);
			// 暂时出区
			List<Temporary_Leave> temporaryLeaves = temporaryLeaveService
					.findTempLeaveListBySuspectID(suspectId);
			// 犯人羁押时间
			// DateTimeFormatter format = DateTimeFormat
			// .forPattern("yyyy-MM-dd HH:mm");
			// DateTime startTime = DateTime.parse(suspect.getEnter_Time());
			// DateTime endTime = DateTime.parse(leave_Record.getLeave_Time());

			// int prisonHour = Hours.hoursBetween(startTime,
			// endTime).getHours();

			String reportCreateTime = new DateTime()
					.toString("yyyy-MM-dd HH:mm");

			// if ((suspect == null) && (belongingS == null)
			// && (personal_Check == null) && (activity_Record == null)
			// && (information_Collection == null) && (leave_Record == null)
			// && (temporaryLeaves == null)) {
			// return "NULL";
			// }

			// 将查找到的信息放入request中，然后从页面加载
			request.setAttribute("suspect", suspect);
			request.setAttribute("belongingS", belongingS);
			request.setAttribute("personal_Check", personal_Check);
			request.setAttribute("activity_Record", activity_Record);
			request.setAttribute("information_Collection",
					information_Collection);
			request.setAttribute("leave_Record", leave_Record);
			request.setAttribute("temporaryLeaves", temporaryLeaves);
			// request.setAttribute("prisonHour", prisonHour);
			request.setAttribute("reportCreateTime", reportCreateTime);
			request.setAttribute("pdfFilePath",
					PropertiesReadUtils.getRecordConfString("uploadDir") + "//"
							+ suspectId + ".pdf");
			// request.setAttribute("detainTime", suspect.getDetain_Time());
			System.out.println("detainTime=" + detainTime);

			// 生成PDF

			// 获取pdf的临时保存路径,getServletContext是Tomcat容器的路径,也就是服务器的路径
			// System.out.println(request.getSession().getServletContext()
			// .getRealPath("/pdf"));
			// String pdfPath = request.getSession().getServletContext()
			// .getRealPath("/pdf")
			// + suspectId + ".pdf";
			// // 判断是否存在该文件，存在则不生成，不存在则生成
			// if (!new File(pdfPath).exists()) {
			// // String path = PropertiesReadUtils.getPDFString("sourcePath")
			// // + request.getParameter("suspectId");
			// String path = PropertiesReadUtils.getPDFString("sourcePath")
			// + "LB-HB-20170317005";
			// if (HtmlToPdf.convert(path, pdfPath)) {
			// // response.sendRedirect(request.getContextPath() + "/tmp/"
			// // +
			// // pdfName);
			// request.setAttribute("pdfPath", pdfPath);
			// System.out.println("pdf成功生成！");
			// }
			// }
			return "toGR";
		} catch (Exception e) {
			response.getWriter()
					.write("<script type='text/javascript'>alert('页面加载失败，可能是pdf配置失败');</script>");
			response.getWriter().flush();
			return "success";
		}
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
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
