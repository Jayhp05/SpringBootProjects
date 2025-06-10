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
	
	/* 회원가입 폼 유효성 검사 */
	$("#id").on("keyup", function() {
	 	// 아래와 같이 정규표현식을 이용해 영문 대소문자, 숫자만 입력되었는지 체크할 수 있다. 
		var regExp = /[^A-Za-z0-9]/gi;
	 	if(regExp.test($(this).val())) {
	 		alert("영문 대소문자, 숫자만 입력할 수 있습니다.");
	 		$(this).val($(this).val().replace(regExp, ""));
	 	}
	 });
	 
	 $("#pass1").on("keyup", inputCharReplace);
	 $("#pass2").on("keyup", inputCharReplace);
	 $("#emailId").on("keyup", inputCharReplace);
	 $("#emailDomain").on("keyup", inputEmailDomainReplace);
	 
	 $("#btnOverlapId").on("click", function() {
	  	var id = $("#id").val();
		
	  	let url="overlapIdCheck?id=" + id;
		
	  	if(id.length == 0) {
	  		alert("아이디를 입력해주세요");
	  		return false;
	  	}
		
	  	if(id.length < 5) {
	  		alert("아이디는 5자 이상 입력해주세요.");
	  		return false;
	  	}
		
	  	window.open(url, "idCheck", "toolbar=no, scrollbars=no, resizeable=no, " +  "status=no, memubar=no, width=500, height=330");
	 });
	 
	$("#idCheckForm").on("submit", function() {
	 	var id = $("#checkId").val();
		
	  	if(id.length == 0)  {
	  		alert("아이디를 입력해주세요");
	  		return false;
	  	}
		
	  	if(id.length < 5) {
	  		alert("아이디는 5자 이상 입력해주세요.");
	  		return false;
	  	}
	});
	
	/* 새 창으로 띄운 아이디 중복확인 창에서 "아이디 사용 버튼"이 클릭되면 
	* 새 창을 닫고 입력된 아이디를 부모창의 회원가입 폼에 입력해 주는 함수
	**/
	$("#btnIdCheckClose").on("click", function() {
	  	var id = $(this).attr("data-id-value");
	  	opener.document.joinForm.id.value = id;
	  	opener.document.joinForm.isIdCheck.value = true;
	 	window.close();
	});
	
	/* 회원 가입 폼과 회원정보 수정 폼에서 "우편번호 검색" 버튼의 클릭 이벤트 처리
	* findZipcode() 함수는 다음 우편번호 API를 사용해 우편번호를 검색하는 함수로
	* 두 페이지에서 사용되어 중복된 코드가 발생하므로 아래에 별도의 함수로 정의하였다.
	*/
	$("#btnZipcode").click(findZipcode);
	
	// 이메일 입력 셀렉트 박스에서 선택된 도메인을 설정하는 함수 
	$("#selectDomain").on("change", function() {
	  	var str = $(this).val();
		
	  	if(str == "직접입력") {
	  		$("#emailDomain").val("");
	  		$("#emailDomain").prop("readonly", false);
	  	} 
		else if(str == "네이버"){
	  		$("#emailDomain").val("naver.com");
	  		$("#emailDomain").prop("readonly", true);
	  	}
		else if(str == "다음") {
	  		$("#emailDomain").val("daum.net");
	  		$("#emailDomain").prop("readonly", true);
	  	} 
		else if(str == "한메일"){
	  		$("#emailDomain").val("hanmail.net");
	  		$("#emailDomain").prop("readonly", true);
	  	} 
		else if(str == "구글") {
	  		$("#emailDomain").val("gmail.com");
	  		$("#emailDomain").prop("readonly", true);
	  	}
	});
	
	// 회원 가입 폼이 서브밋 될 때 이벤트 처리 - 폼 유효성 검사
	$("#joinForm").on("submit", function() {
	/* 회원 가입 폼에서 서브밋 이벤트가 발생하면 true를 인수로 지정한다. 
	* 회원 가입 폼과 회원 정보 수정 폼 유효성 검사를 하는 기능도 중복 되는
	* 코드가 많으므로 joinFormCheck()라는 별도의 함수를 만들어서 사용하였다. 
	* joinFormChcek() 함수에서 폼 유효성 검사를 통과하지 못하면
	* false가 반환되기 때문에 그대로 반환하면 폼이 서브밋 되지 않는다. 
	**/
	  	return joinFormCheck(true); 
	});
	
});

/* 회원 가입 폼과 회원정보 수정 폼의 유효성 검사를 하는 함수
* 두 페이지에서 처리하는 코드가 중복되어 하나의 함수로 정의하였다. 
**/
function joinFormCheck(isJoinForm) {
	
	var name = $("#name").val();
	var id = $("#id").val();
	var pass1 = $("#pass1").val();
	var pass2 = $("#pass2").val();
	var zipcode = $("#zipcode").val();
	var address1 = $("#address1").val();
	var emailId = $("#emailId").val();
	var emailDomain = $("#emailDomain").val();
	var mobile2 = $("#mobile2").val();
	var mobile3 = $("#mobile2").val();
	var isIdCheck = $("#isIdCheck").val();
	
	if(name.length == 0) {
		alert("이름이 입력되지 않았습니다.\n이름을 입력해주세요");
	  	return false;
	}
	if(id.length == 0) {
	  	alert("아이디가 입력되지 않았습니다.\n아이디를 입력해주세요");
	  	return false;
	}
	if(isJoinForm && isIdCheck == 'false') {
	  	alert("아이디 중복 체크를 하지 않았습니다.\n아이디 중복 체크를 해주세요");
	  	return false;
	}
	if(pass1.length == 0) {
	  	alert("비밀번호가 입력되지 않았습니다.\n비밀번호를 입력해주세요");
	  	return false;
	}
	if(pass2.length == 0) {
	  	alert("비밀번호 확인이 입력되지 않았습니다.\n비밀번호 확인을 입력해주세요");
	  	return false;
	}
	if(pass1 != pass2) {
	  	alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
	  	return false;
	}
	if(zipcode.length == 0) { 
	 	alert("우편번호가 입력되지 않았습니다.\n우편번호를 입력해주세요");
		return false;
	}
	if(address1.length == 0) {
	  	alert("주소가 입력되지 않았습니다.\n주소를 입력해주세요");
	  	return false;
	}
	if(emailId.length == 0) {
	  	alert("이메일 아이디가 입력되지 않았습니다.\n이메일 아이디를 입력해주세요");
	  	return false;
	}
	if(emailDomain.length == 0) {
	  	alert("이메일 도메인이 입력되지 않았습니다.\n이메일 도메인을 입력해주세요");
	  	return false;
	}
	if(mobile2.length == 0 || mobile3.length == 0) {
	  	alert("휴대폰 번호가 입력되지 않았습니다.\n휴대폰 번호를 입력해주세요");
	  	return false;
	}
}

/* 우편번호 찾기 - daum 우편번소 찾기 API 이용
* 회원 가입 폼과 회원정보 수정 폼에서 "우편번호 검색" 버튼이 클릭되면 호출되는 함수
* 
* 새로운 5자리 우편번호로 회원 주소를 입력 받기 위해 daum.net에서
* 제공하는 우편번호 찾기 API를 사용하였다.
* 참고 사이트 : http://postcode.map.daum.net/guide
**/
function findZipcode() {
	new daum.Postcode({
		oncomplete: function(data) {
            // 우편번호 검색 결과 항목을 클릭했을때 실행할 코드를 여기에 작성한다.
            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고 항목 변수
			
            //사용자가 선택한 주소 타입과 상관없이 모두 도로명 주소로 처리            
            addr = data.roadAddress;
             
            // 법정동명이 있을 경우 추가한다. (법정리는 제외)
            // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
            if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                extraAddr += data.bname;
            }

            // 건물명이 있고, 공동주택일 경우 추가한다.
            if(data.buildingName !== '' && data.apartment === 'Y'){
                extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }
	             
	        // 표시할 참고 항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
	        if(extraAddr !== ''){
	        	extraAddr = ' (' + extraAddr + ')';
	        }
	        // 조합된 참고 항목을 상세주소에 추가한다.
	        addr += extraAddr;
	             
	 		// 우편번호와 주소 정보를 해당 입력상자에 출력한다.
	  		$("#zipcode").val(data.zonecode);
	  		$("#address1").val(addr);
	  		// 커서를 상세주소 입력상자로 이동한다.
	  		$("#address2").focus();
	  }
	  
	}).open();
}

// 아래 코드는 $(function() {}); 밖에 작성합시다.
/* 회원 아이디, 비밀번호, 비밀번호 확인, 이메일 아이디 폼 컨트롤에
* 사용자가 입력한 값이 영문 대소문자, 숫자 만 입력되도록 수정하는 함수
**/
function inputCharReplace() {
 	// 아래와 같이 정규표현식을 이용해 영문 대소문자, 숫자만 입력되었는지 체크할 수 있다. 
	var regExp = /[^A-Za-z0-9]/gi;
	
 	if(regExp.test($(this).val())) {
 		alert("영문 대소문자, 숫자만 입력할 수 있습니다.");
 		$(this).val($(this).val().replace(regExp, ""));
 	}
}

/* 이메일 도메인 입력 폼 컨트롤에 사용자가 입력한 값이 
* 영문 대소문자, 숫자, 점(.)만 입력되도록 수정하는 함수 
**/ 
function inputEmailDomainReplace() {
	var regExp = /[^a-z0-9\.]/gi;
	
 	if(regExp.test($(this).val())) {
 		alert("이메일 도메인은 영문 소문자, 숫자, 점(.)만 입력할 수 있습니다.");
 		$(this).val($(this).val().replace(regExp, ""));
 	}
}