<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*,com.spring.odagada.member.model.vo.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
	Member m = (Member) request.getAttribute("logined");
	System.out.println(m);
    String eDate []=m.getEnrollDate().split(" ");
%>

<c:set var="path" value="${pageContext.request.contextPath}"/>
<jsp:include page="/WEB-INF/views/common/header.jsp">
   <jsp:param value="오다가다 타는 카풀" name="pageTitle"/>
</jsp:include>
<script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>

<style>

  div#enroll-container{width:400px; margin:0 auto;}
  div#enroll-container input, div#enroll-container select {margin-bottom:10px;}
  .menu{text-align:center; font-weight:bold;}
  .info{margin-top:50px; margin-bottom: 50px;}
  .alertS{padding:5px; font-size:12px; text-align:center;}
  
}
</style>



<div id="enroll-container">	
	<div class="info col-12">
		<div class="card-group">
			<div class="card">
			  <img class="card-img-top" src="${path }/resources/upload/profile/${logined.profileImageRe}" alt="Card image cap">
				  <table class="table table-hover">
				    <tr>
				      <th scope="row">아이디</th>
				      <td><c:out value="${logined.memberId }"></c:out></td>
				      <td></td>   
				    </tr>
				    <tr>
					   <th scope="row">이름</th>
				       <td><c:out value="${logined.memberName }"></c:out></td>
				       <td></td>     
				    </tr>
				    <tr>
				       <th scope="row">가입날짜</th>
			     	   <td><%=eDate[0] %></td>
			     	   <td></td>  
				    </tr>
				     <tr>
				       <th scope="row">E-mail</th>
				       <td><c:out value="${logined.email }"></c:out></td>
				      <td>
				      	<c:choose>
				      		<c:when test="${logined.isEmailAuth eq 'N'}">
				      			<a href="#" class="ck badge badge-danger">인증하기</a>
		      				</c:when>
				      		<c:when test="${logined.isEmailAuth eq 'Y'}">
				      			<div class="alertS alert alert-success text-success" role="alert">인증완료</div>
			      			</c:when>
				      	</c:choose> 
				      </td>				       
				    </tr>
				     <tr>				       
				      <th scope="row">Phone</th>
				        <td><c:out value="${logined.phone }"></c:out></td>
				      <td>		
			       	 	<c:choose>
			       	 		<c:when test="${logined.isPhoneAuth eq 'N'}">				      			
				      			<a href="#" class="ck badge badge-danger">인증하기</a>
				      		</c:when>
				      		<c:when test="${logined.isPhoneAuth eq 'Y'}">
				      			<div class="alertS alert alert-success text-success" role="alert">인증완료</div>
				      		</c:when>
			      		</c:choose>   
				      </td>
				    </tr>		 
				</table>		
			  <div class="menu card-body">
 			 	   <input type="button" id="withdrawal" onclick="updateCheck();" class="btn btn-outline-success" value="정보변경">
   			 	   <a href="#" id="withdrawal" onclick="withdrawal();" class="card-link text-secondary">회원탈퇴</a>
			 	   
			  
			<%--   	<form action="${path }/member/updateInfo.do" onsubmit="return updateCheck();" method="post">
			   		 <input type="hidden" name="memberPw"/>
			   		 <input type="submit" class="btn btn-outline-success" value="내 정보 변경">
		   		 </form>
		   		 <form>
			 	   <a href="#" id="withdrawal" onclick="withdrawal();" class="card-link text-secondary">회원 탈퇴</a>
			 	 </form>   --%>
			  </div>
			</div>
		</div>		
	</div>
</div>

<script>
function updateCheck() {
	var testTimes=1;
    var answer = prompt("다시 한 번 비밀번호 입력해주세요.");
    
    $.ajax({
    	url:"${path}/member/checkPw.do",
    	data:{"answer":answer},
    	success:function(data){
    		if(data.result.equals("N")){
    			history.go(-1);   			
				alert("비밀번호가 올바르지 않습니다.");		
    		}
    		else{   			
    			location.href="${path}/member/updateInfo.do";
    	  	}
    	}
    });  
  
}
    	




</script>






<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>