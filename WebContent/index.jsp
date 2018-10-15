<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/script/jquery/jquery-3.1.0.js"></script>
<title>Insert title here</title>
</head>

<body>

	<h1>iPhoneX !!! 1元秒杀！！！</h1>


	<form id="msform" action="${pageContext.request.contextPath}/doseckill">
		
		<input type="hidden" id="prodid" name="prodid" value="0101" /> 
		
		<input type="button" id="miaosha_btn" name="seckill_btn" value="秒杀点我" />
		
	</form>

</body>

	
<script type="text/javascript">
$(function(){
	$("#miaosha_btn").click(function(){	 
		var url=$("#msform").attr("action");
	     $.post(url,$("#msform").serialize(),function(data){
     		if(data=="false"){
    			alert("抢光了" );
    			$("#miaosha_btn").attr("disabled",true);
    		}
		} );    
	})
})
</script>

</html>