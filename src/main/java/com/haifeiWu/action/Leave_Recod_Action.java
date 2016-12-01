package com.haifeiWu.action;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.haifeiWu.base.BaseAction;
import com.haifeiWu.entity.PHCSMP_Leave_Record;
import com.haifeiWu.entity.PHCSMP_Staff;
import com.haifeiWu.entity.PHCSMP_Suspect;
import com.haifeiWu.entity.Temporary_Leave;
import com.haifeiWu.service.LeaveRecodService;
import com.haifeiWu.service.SuspectService;

/**
 * 离开办案区的action
 * 
 * @author wuhaifei
 * @d2016年8月17日
 */
@Controller
@Scope("prototype")
public class Leave_Recod_Action extends BaseAction<PHCSMP_Leave_Record> {
	/**
	 * 序列化的字段
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private LeaveRecodService leaveRecodService;

	private List<Temporary_Leave> temporaryLeave = new ArrayList<Temporary_Leave>();
	// 嫌疑人信息
	@Autowired
	private SuspectService suspectService;

	private String personName;
	private String suspectID;

	/**
	 * 点击画面中的“下一步”，提交信息并转发到suspectManage_suspectInforSummary.action
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addLeaveRecordInfor() throws Exception {

		// 打印提交的单条信息
		System.out.println("单条信息：" + model.toString());

		PHCSMP_Suspect SuspectInfor = suspectService.findInfroByActiveCode(1);

		this.personName = URLDecoder.decode(SuspectInfor.getSuspect_Name(),
				"utf-8");
		this.suspectID = SuspectInfor.getSuspect_ID();

		// 打印list信息
		List<Temporary_Leave> temporaryLeaves = this.getTemporaryLeave();
		System.out.println("多条信息：" + temporaryLeaves.size());
		for (Temporary_Leave temporaryLeave : temporaryLeaves) {
			System.out.println(temporaryLeave.toString());
		}
		// service.saveLeaveRecordInfor(temporaryLeaves);
		// //通过反射加载离开办案区记录的类
		// Class<?> c = Class.forName(PHCSMP_Leave_Record.class.getName());
		//
		// int count = CompleteCheck.IsEqualsNull(model, c);
		// int fieldsNumber = CompleteCheck.getFieldsNumber(model, c);
		//
		// model.setFill_record(fieldsNumber-count-4);//设置已填写的字段数
		// model.setTotal_record(fieldsNumber-4);//设置应填写的字段
		// System.out.println("未填写的字段："+count);
		// System.out.println("总字段："+fieldsNumber);

		// LeaveRecodService service = new LeaveRecodServiceImple();
		// service.saveLeaveRecordInfor(model);

		System.out.println("Leave_Recod_Action:addLeaveRecordInfor");
		return "addLeaveRecordInfor";
	}

	/* 加载界面信息 */
	public String loadInfor() {
		PHCSMP_Staff user = (PHCSMP_Staff) request.getSession().getAttribute(
				"user");
		PHCSMP_Suspect SuspectInfor = suspectService.findInfroByActiveCode(1);
		// 将信息从数据库查找到之后，存入session，更新session
		request.setAttribute("SuspectInfor", SuspectInfor);

		// this.setPersonName(SuspectInfor.getSuspect_Name());
		// this.setSuspectID(SuspectInfor.getSuspect_ID());

		if (user == null) {
			return "unLoginState";
		} else {
			System.out.println("Leave_Recod_Action:loadInfor");
			return "loadInfor";
		}
	}

	// 未登录状态时
	public String unlogin_load() {
		return "unlogin_load";
	}

	public List<Temporary_Leave> getTemporaryLeave() {
		return temporaryLeave;
	}

	public void setTemporaryLeave(List<Temporary_Leave> temporaryLeave) {
		this.temporaryLeave = temporaryLeave;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getSuspectID() {
		return suspectID;
	}

	public void setSuspectID(String suspectID) {
		this.suspectID = suspectID;
	}
}