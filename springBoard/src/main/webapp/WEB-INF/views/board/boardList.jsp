<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>list</title>
</head>
<script type="text/javascript">
$j(document).ready(function(){
		$j("#total").on("click", function() {
			if ($j("#total").prop("checked")) {
				$j("input[name=CheckType]").prop("checked", true);
			} else {
				$j("input[name=CheckType]").prop("checked", false);
			}
		});
		
		$j(".CheckType").click(function(){
			$j("#total").prop("checked", false);
		});
});
		
</script>
<body>
	<table align="center">
		<tr>
			<td align="right">total : ${totalCnt}</td>
		</tr>
		<tr>
			<td>
				<table id="boardTable" border="1">
					<tr>
						<td width="80" align="center">Type</td>
						<td width="40" align="center">No</td>
						<td width="300" align="center">Title</td>
					</tr>
					<c:forEach items="${boardList}" var="list">
						<tr>
							<td align="center">${list.boardType}</td>
							<td>${list.boardNum}</td>
							<td><a
								href="/board/${list.boardType}/${list.boardNum}/boardView.do?pageNo=${pageNo}">${list.boardTitle}</a>
							</td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
		<tr>
			<td align="right"><a href="/board/boardWrite.do">글쓰기</a></td>
		</tr>
		<tr>
			<td align="left"><input type="checkbox" id="total">전체 <c:forEach
					var="check" items="${com_codeList}">
					<input name="CheckType" value="${check.codeId}" type="checkbox"
						class="CheckType">${check.codeName}
			</c:forEach> <input type="submit" value="검색"></td>
		</tr>
	</table>
</body>
</html>