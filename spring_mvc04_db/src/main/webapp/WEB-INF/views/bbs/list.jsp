<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	
<!DOCTYPE HTML>
<html>
<head>
<meta charset=UTF-8>
<title>Insert title here</title>
<style type="text/css">
#bbs table {
	width:700px;
	margin:0 auto;
	margin-top:20px;
	border: 1px solid black;
	border-collapse: collapse;
	font-size: 14px;
}

#bbs table caption {
	font-size: 20px;
	font-weight: bold;
	margin-bottom: 10px;
}

#bbs table th, #bbs table td {
	text-align: center;
	border: 1px solid black;
	padding: 4px 10px;
}

.no { width: 10% }
.subject { 	width: 30% }
.writer {	width: 20% }
.reg {	width: 20% }
.hit {	width: 10% }
.commcount  {	width: 10% }
.title {	background: lightsteelblue }
.odd {	background: silver }

/* paging */
table tfoot ol.paging {
	list-style: none;
}

table tfoot ol.paging li {
	float: left;
	margin-right: 8px;
}

table tfoot ol.paging li a {
	display: block;
	padding: 3px 7px;
	border: 1px solid #00B3DC;
	color: #2f313e;
	font-weight: bold;
}

table tfoot ol.paging li a:hover {
	background: #00B3DC;
	color: white;
	font-weight: bold;
}

.disable {
	padding: 3px 7px;
	border: 1px solid silver;
	color: silver;
}

.now {
	padding: 3px 7px;
	border: 1px solid #ff4aa5;
	background: #ff4aa5;
	color: white;
	font-weight: bold;
}
</style>
<script type="text/javascript">
	function write_go() {
		location.href = "/bbs_insertForm.do";
	}
</script>
</head>
<body>
	<div id="bbs" align="center">
		<table summary="게시판 목록">
			<caption>게시판 목록</caption>
			<thead>
				<tr class="title">
					<th class="no">번호</th>
					<th class="subject">제목</th>
					<th class="writer">글쓴이</th>
					<th class="reg">날짜</th>
					<th class="commcount">댓글수</th>
					<th class="hit">조회수</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${empty bbs_list}">
						<tr>
							<td colspan="6"><h2>자료가 존재하지 않습니다.</h2></td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="k" items="${bbs_list}" varStatus="vs">
							<tr>
								<td>${paging.totalRecord - ((paging.nowPage-1)*paging.numPerPage + vs.index)}</td>
								<c:choose>
									<c:when test="${k.status == 1}">
										<td style="color: gray"> 삭제된 게시물 입니다. </td>
									</c:when>
									<c:otherwise>
										<td><a href="/bbs_onelist.do?b_idx=${k.b_idx}&cPage=${paging.nowPage}">${k.subject}</a></td>
									</c:otherwise>
								</c:choose>
								<td>${k.writer}</td>
								<td>${k.write_date.substring(0,10)}</td>
								<td>${k.commcount}</td>
								<td>${k.hit}</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="5">
						<ol class="paging">
							<!-- 이전 버튼 -->
							<c:choose>
								<c:when test="${paging.beginBlock <= paging.pagePerBlock}">
									<li class="disable">이전으로</li>
								</c:when>
								<c:otherwise>
									<li><a href="/bbs_list.do?c_Page=${paging.beginBlock-paging.pagePerBlock}">이전으로</a></li>
								</c:otherwise>
							</c:choose>
							<!-- 페이지 번호들 
								for문으로 돌려서 번호블럭을 만들어주자. 
								현재 페이지는 링크가 없고, 나머지 페이지는 링크처리 해야한다. 
								그래야 해당 블럭의 페이지로 넘어가 해당 페이지에 포함된 게시글을 볼수있다.
							-->
							<c:forEach begin="${paging.beginBlock}" end="${paging.endBlock}" step="1" var="k">
								<!-- 현재 페이지는 링크 X, 나머지 페이지는 해당 페이지로 이동하게 링크 처리 -->
								<c:if test="${k == paging.nowPage}">
									<!--현재페이지와 같으면  -->
									<li class="now">${k}</li>
								</c:if>
								
								<c:if test="${k != paging.nowPage}">
									<li><a href="/bbs_list.do?cPage=${k}">${k}</a></li>
									<!-- 현재페이지가 아니면 링크를 걸고, 해당페이지로 가면 리스트 화면이 보이게한다.  -->
								</c:if>
							</c:forEach>
							<!-- 이후 버튼 -->	
							<c:choose>
								<c:when test="${paging.endBlock >= paging.totalPage}">
									<li class="disable">다음으로</li>
								</c:when>
								<c:otherwise>
									<li><a href="/bbs_list.do?cPage=${paging.beginBlock+paging.pagePerBlock}">다음으로</a></li>
									<!--다음으로는 끝블럭보다, 전체페이지가 크다면 disable
										그리고 +를 해주면된다.									
									 -->
								</c:otherwise>
							</c:choose>
						</ol>
					</td>
					<td>
						<input type="button" value="글쓰기" onclick="write_go()">
					</td>
				</tr>
			</tfoot>
		</table>
	</div>
</body>
</html>