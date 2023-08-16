<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>spring MVC project</title>
<style type="text/css">
   table{
      width: 600px; margin-top: 50px; border-collapse: collapse;
   }
   table, th, td {
       border: 1px solid red; text-align: center;
    }
</style>
<link rel="icon" type="image/x-icon" href="resources/images/123.PNG">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		var loginChk = "${loginChk}";
		if(loginChk == "fail") {
			alert("비밀번호틀림");
			return ;
		}else if(loginChk == "ok") {
			$("#login").css("display","none");      // 감추기
			$("#login_ok").css("display", "block"); // 나타내기
		}
	});
</script>	
<script type="text/javascript">
	function go_members() {
		location.href= "/members_list.do"; 
	}
	function go_guestbook() {
		location.href= "/guestbook_list.do" ;
	}
	function go_guestbook2() {
		location.href= "/guestbook2_list.do" ;
	}
	function reg_add_go() {
		location.href= "/member_reg.do";
	}
	function member_logout_go() {
		location.href= "/member_logout.do";
	}
	function go_bbs() {
		location.href= "/bbs_list.do";
	}
	function go_board() {
		location.href= "/board_list.do";
	}
	function go_shop() {
		location.href= "/shop_list.do";
	}
	function go_email() {
		location.href= "/email_send.do";
	}
</script>
<script type="text/javascript">
	function kma_go() {
		location.href = "/kma_go.do";
	}
	
<script type="text/javascript">
	function kma_go2() {
		location.href = "/kma_go2.do";
	}
</script>

<!--jQuery, Ajax -->
<script type="text/javascript">
	$(document).ready(function () {
		$("#btn1").on("click", function() {
			$("result").empty();  	  //초기화
			$.ajax({
				url : "/test01.do",     //서버 주소
				method : "post",      //전달 방식
				dataType : "text",    //가져오는 결과 타입
				// data : "",         //서버에 보낼 때 같이 가는 데이터(파라미터) 
				// async : true,      //비동기통신(기본, 생략가능), 동기 통신(false)
				success : function(data) {
					//console.log(data);
					$("#result").append(data);
				},
				error : function() {
					alert("읽기 실패");
				}
			});
		});
		
		$("#btn2").on("click", function() {
	         $("#result").empty();
	         $.ajax({
	            url: "/test02.do",     //서버 주소       
	            method: "post",        //전달 방식
	            dataType: "xml",       //가져오는 데이터 결과 타입
	            //data: "",            //서버에 보낼때 같이가는 데이터 (즉, 파라미타)
	            //async: true ,        //비동기통신(기본, 생략가능) false: 동기
	            success: function(data) {
	               //console.log(data);
	               //태그로만 있을때 파싱
	               var  table = "<table>";
	               table += "<thead><tr><th>종류</th><th>가격</th></tr></thead>";
	               table += "<tbody>";
	               $(data).find("product").each(function() {
	                  var name = $(this).find("name").text();
	                  var price = $(this).find("price").text();
	                  table += "<tr>";
	                  table += "<td>" + name+ "</td>";
	                  table += "<td>" + price +"</td>";
	                  table += "</tr>";
	               });
	               table += "</tbody>";
	               table += "</table>";
	               $("#result").append(table);
	               
	            },
	            error: function() {
	               alert("읽기실패");

				}
			});
		});
		
		$("#btn3").on("click", function() {
	         $("#result").empty();
	         $.ajax({
	            url: "/test03.do",     //서버 주소       
	            method: "post",        //전달 방식
	            dataType: "xml",       //가져오는 데이터 결과 타입
	            //data: "",            //서버에 보낼때 같이가는 데이터 (즉, 파라미타)
	            //async: true ,        //비동기통신(기본, 생략가능) false: 동기
	            success: function(data) {
	               //console.log(data);
	               //태그로만 있을때 파싱
	               var  table = "<table>";
	               table += "<thead><tr><th>이름</th><th>수</th></tr></thead>";
	               table += "<tbody>";
	               
	               $(data).find("product").each(function() {
	                  var name = $(this).attr("name");
	                  var count = $(this).attr("count");
	                  table += "<tr>";
	                  table += "<td>" + name+ "</td>";
	                  table += "<td>" + count +"</td>";
	                  table += "</tr>";
	               });
	               table += "</tbody>";
	               table += "</table>";
	               $("#result").append(table);
	               
	            },
	            error: function() {
	               alert("읽기실패");

				}
			});
		});
		
		$("#btn4").on("click", function() {
	         $("#result").empty();
	         $.ajax({
	            url: "/test04.do",     //서버 주소       
	            method: "post",        //전달 방식
	            dataType: "xml",       //가져오는 데이터 결과 타입
	            //data: "",            //서버에 보낼때 같이가는 데이터 (즉, 파라미타)
	            //async: true ,        //비동기통신(기본, 생략가능) false: 동기
	            success: function(data) {
	               //console.log(data);
	               //태그로만 있을때 파싱
	               var  table = "<table>";
	               table += "<thead><tr><th>이름</th><th>수</th><th>회사명</th></tr></thead>";
	               table += "<tbody>";
	               
	               $(data).find("product").each(function() {
	                  var name = $(this).attr("name");
	                  var count = $(this).attr("count");
	                  var company = $(this).text();
	                  table += "<tr>";
	                  table += "<td>" + name+ "</td>";
	                  table += "<td>" + count +"</td>";
	                  table += "<td>" + company +"</td>";
	                  table += "</tr>";
	               });
	               table += "</tbody>";
	               table += "</table>";
	               $("#result").append(table);
	               
	            },
	            error: function() {
	               alert("읽기실패");

				}
			});
		});
		
		$("#btn5").on("click", function() {
	         $("#result").empty();
	         $.ajax({
	            url: "/test05.do",     //서버 주소       
	            method: "post",        //전달 방식
	            dataType: "xml",       //가져오는 데이터 결과 타입
	            //data: "",            //서버에 보낼때 같이가는 데이터 (즉, 파라미타)
	            //async: true ,        //비동기통신(기본, 생략가능) false: 동기
	            success: function(data) {
	               //console.log(data);
	               //태그로만 있을때 파싱
	               var  table = "<table>";
	               table += "<thead><tr><th>지역</th><th>온도</th><th>상태</th><th>아이콘</th></tr></thead>";
	               table += "<tbody>";
	               
	               $(data).find("local").each(function() {
	                  var local = $(this).text();
	                  var ta = $(this).attr("ta");
	                  var desc = $(this).attr("desc");
	                  var icon = $(this).attr("icon");
	                  table += "<tr>";
	                  table += "<td>" + local + "</td><td>" + ta +"</td><td>" + desc +
	                  "</td><td><img src='http://www.kma.go.kr/images/icon/NW/NB" + icon +".png'></td>";
	                  table += "</tr>";
	               });
	               table += "</tbody>";
	               table += "</table>";
	               $("#result").append(table);
	               
	            },
	            error: function() {
	               alert("읽기실패");

				}
			});
		});
	});
</script>
</head>
<body>
	<button onclick="go_members()">Members</button>
	<button onclick="go_guestbook()">GuestBook</button>
	<button onclick="go_guestbook2()">GuestBook2</button>
	<button onclick="go_bbs()">BBS</button>
	<button onclick="go_board()">Board</button>
	<button onclick="go_shop()">Shop</button>
	<button onclick="go_email()">Email</button>
	<hr>
	
	<!-- 
		div로 감싼이유는 로그인성공시 이 화면단이 안보이게 하려고함. 로그인하면 다시 인덱스로 와야해서.
	 -->
	<div id="login" style="margin: 30px;">
		<form action="/member_login.do" method="post">
			<p>ID: <input type="text" name="m_id" required></p>
			<p>PW: <input type="password" name="m_pw" required></p>
			<input type="submit" value="로그인">
		</form>	
	</div>
	<div id="login_ok" style="display:none;">
		<h2>${mvo.m_id}님 로그인 성공</h2>
		<button onclick="member_logout_go()">로그아웃</button>
	</div>
	<div id="btns" style="margin: 30px;">
	<button onclick="reg_add_go()">회원가입</button>
	<button onclick="id_find_go()">아이디 찾기</button>
	<button onclick="pw_find_go()">비번 찾기</button>
	</div>
	
	
	<hr>
	<h2>Spring Ajax</h2>
	<hr>
	<button id="btn1">TEXT</button>
	<button id="btn2">XML</button>
	<button id="btn3">XML3</button>
	<button id="btn4">XML4</button>
	<button id="btn5">날씨_XML1</button>
	<button onclick="kma_go()">날씨_XML2</button>
	<button onclick="kma_go2()">날씨_XML3</button>
	
	<hr>
	<div id="result"></div>
</body>
</html>