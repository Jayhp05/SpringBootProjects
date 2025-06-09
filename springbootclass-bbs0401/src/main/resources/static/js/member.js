$(function() {
	
	$("loginForm").submit(function() {
		let id = $("#uesrId").val();
		let pass = $("#userPass").val();
		
		/*그냥 로그인 했을 시*/
		if(id.length <= 0) {
			alert("아이디가 입력되지 않았습니다.\n아이디를 입력해주세요");
			$("#userId").focus();
			
			return false;
		}
		
		if(pass.length <= 0) {
			alert("비밀번호가 입력되지 않았습니다.\n비밀번호를 입력해주세요");
			$("#userPass").focus();
			
			return false;
		}
		
	})
	
		/*로그인 모달을 사욜 했을 시 유효성 검사*/
	$("#modalLoginForm").submit(function() {
		var id = $("#modalUserId").val();
		var pass = $("#modalUserPass").val();
		
		if(id.length <= 0) {
			alert("아이디가 입력되지 않았습니다.\n아이디를 입력해주세요");
			$("#modalUserId").focus();
			
			return false;
		}
		
		if(pass.length <= 0) {
			alert("비밀번호가 입력되지 않았습니다.\n비밀번호를 입력해주세요");
			$("#modalUserPass").focus();
			
			return false;
		}
		
	})
});