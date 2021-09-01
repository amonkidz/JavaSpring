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
	$j(document).ready(function() {
		
		// 체크박스 전체선택, 전체해제
		$j("#total").click(function() {
			// 전체선택이 체크된 경우
			if ($j("#total").prop("checked")) {
				// 모든 항목 체크
				$j("input[name=CheckType]").prop("checked", true);
				console.log("전체 체크");
			} else {
				// 모든 항목 체크 해제 
				$j("input[name=CheckType]").prop("checked", false);
				console.log("전체 체크 해제");
			}
		});

		// 한개 체크 박스를 해제할 경우
		$j(".CheckSelect").click(function() {
			// 전체 체크 해제됨
			$j("#total").prop("checked", false);
			console.log("전체 체크 해제");
		});
		
		$j("#submit").click(function(){
			
			var len = $j("input[name=CheckType]:checked").length;
			console.log(len);
			
			var checkArr = [];
			$j("input[name=CheckType]:checked").each(function(){
				checkArr.push($j(this).val());
				console.log(checkArr);
			});
			
			$j.ajax({
				url : '/board/boardSelectAction.do'
				, type : 'POST'
				, dataType : 'text'
				, data : {
					checkArr: checkArr
				}, success : function(data) {
					console.log("전송성공");
				}
			});
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
					var="check" items="${comList}">
					<input name="CheckType" value="${check.codeId}" type="checkbox"
						class="CheckSelect">${check.codeName}
			</c:forEach> <input id="submit" type="button" value="검색"></td>
		</tr>
	</table>
</body>
</html>