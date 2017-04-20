<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<title>历史嫌疑人信息</title>
<link rel="stylesheet" href="./css/bootstrap.min.css" />
<link rel="stylesheet" href="./css/Suspect_info.css" />
<script type="text/javascript" src="./js/bootstrap.min.js"></script>
<script type="text/javascript" src="./js/jquery.min.js"></script>
<script type="text/javascript" src="./js/Suspect_mes.js"></script>

</head>
<script type="text/javascript">
	var now=new Date();
	var year=now.getFullYear();
	var month=now.getMonth();
	var day=now.getDate();
	var hours=now.getHours();
	var minutes=now.getMinutes();
	var seconds=now.getSeconds();
	document.getElementById("clock").innerHTML=""+year+"年"+month+"月"+day+"日 "+hours+":"+minutes+":"+seconds+"" ;
	/* alert(document.getElementById("clock").innerHTML); */
	
	function validate() {
		var page = document.getElementsByName("page")[0].value;

		if (page > <s:property value="#request.pageBean.totalPage"/>) {
			alert("你输入的页数大于最大页数，页面将跳转到首页！");

			window.document.location.href = "${pageContext.request.contextPath}/suspectManage_execute.action";

			return false;
		}

		return true;
	}
</script>



<body>
	<div class="row" id="row1" style="height:572px; overflow: hidden; ">
		<h4 id="checkedPerson"
			class="human_Mes col-lg-12 col-md-12 col-sm-12 col-xs-12">
			历史嫌疑人查询&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
			<font size="4">入区人员统计时间：</font> <font id="clock"></font>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
			<font style="text-align: center;">入区人员合计：
				${fn:length(suspectCheckedInfor)}人</font>
		</h4>
		<%-- <table>
				<tr>
					<td style="width: 150px;">入区人员统计时间：</td>
					<td id="clock"></td>
					<td style="text-align: center;">入区人员合计：
					${fn:length(suspectCheckedInfor)}人</td>
				</tr>
			</table> --%>
		<table class="All_total col-lg-12 col-md-10 col-sm-12">
			<tr style="background: #0070c0;">
				<td>序号</td>
				<td>嫌疑人姓名</td>
				<td>档案编号</td>
				<td>入区时间</td>
				<td>羁押时间</td>
				<td>入区事由</td>
				<td>出区原因</td>
				<td>随身物处理</td>
			</tr>
			<c:forEach items="${pageBean.list}" var="item"
				varStatus="status">
				<tr>
					<td>${status.index+1 }</td>
					<td style="width: 150px"><a
						href="./GR_loadInfor.action?personName=${item.suspect_Name }&suspectID=${item.suspect_ID}"
						style="color:#f69d1f;font-size: large;">${item.suspect_Name }</a></td>
					<td style="width: 238px"><a href="${pageContext.request.contextPath }/GR_loadInfor.action?suspectID=${item.suspect_ID}"  target="rightFrame">${item.suspect_ID }</td>
					<td>${item.enter_Time }</td>
					<td>${item.detain_Time }</td>
					<td>${item.suspected_Cause }</td>
					<td>教育释放</td>
					<td>全部反还</td>
				</tr>
			</c:forEach>
		</table>
		
		
<!-- ===========分頁============ -->
		<center>
			<font size="5">共<font color="red"><s:property
						value="#request.pageBean.totalPage" /></font>页
			</font> <font size="5">共<font color="red"><s:property
						value="#request.pageBean.allRows" /></font>条记录
			</font> <font size="5">当前页<font color="red"><s:property
						value="#request.pageBean.currentPage" /></font></font><br> <br>
			<s:if test="#request.pageBean.currentPage == 1">  
            首页   上一页  
        </s:if>
			<s:else>
				<a
					href="${pageContext.request.contextPath }/suspectManage_execute.action">首页</a>
				<a
					href="${pageContext.request.contextPath }/suspectManage_execute.action?page=<s:property value="#request.pageBean.currentPage - 1"/>">上一页</a>
			</s:else>
			<s:if
				test="#request.pageBean.currentPage != #request.pageBean.totalPage">
				<a
					href="${pageContext.request.contextPath}/suspectManage_execute.action?page=<s:property value="#request.pageBean.currentPage + 1"/>">下一页</a>
				<a
					href="${pageContext.request.contextPath}/suspectManage_execute.action?page=<s:property value="#request.pageBean.totalPage"/>">尾页</a>
			</s:if>

			<s:else>  
            下一页   尾页  
        </s:else>

		</center>
		<br>
		<center>

			<form action="suspectManage_execute" onsubmit="return validate();">
				<font size="4">跳转至</font> <input type="text" size="2" name="page">页
				<input type="submit" value="跳转">
			</form>

		</center>
		<p id="more">
				<a href="${pageContext.request.contextPath }/suspectManage_loadInfor.action" target="rightFrame">返回>></a>
		</p>
	</div>

	<div class="row_2 col-lg-12" style="height: 100px;"></div>
	</div>


</body>

</html>
