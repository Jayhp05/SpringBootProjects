// DOM(Document Object Model)이 준비되면
$(function() {
	
	// 추천/땡큐 Ajax
	$(".btnCommend").click(function() {
		
		var com = $(this).attr("id");
		console.log("com : " + com);
		
		$.ajax({			
			url: "recommend.ajax",
			
			// type을 지정하지 않으면 기본은 get 방식 요청이다.
			type: "post",
			
			// 파라미터로 보낼 데이터를 객체 리터럴로 작성
			data : { recommend: com, no : $("#no").val()},

			/* 응답 데이터를 json 형식으로 받기 위해서 dataType을 json으로 
			 * 지정했다. 서버에서 {"recommend": "10", "thank": "10"}와
			 * 같이 json 문자열로 응답했기 때문에 아래에서 Ajax 통신이 성공하면
			 * 실행될 콜백 함수의 첫 번째 인수로 지정한 data에는 자바스크립트
			 * 객체가 넘어오기 때문에 닷(.) 연산자를 이용해 접근할 수 있다. 
			 **/
			dataType: "json",
			success: function(data) {	
				/* 추천/땡큐가 반영된 것을 사용자에게 알리고 
				 * 응답으로 받은 갱신된 추천하기 데이터를 화면에 표시한다.
				 **/ 
				var msg = com == 'commend' ? "추천이" : "땡큐가";
				alert(msg + " 반영 되었습니다.");
				$("#commend > .recommend").text(" (" + data.recommend + ")");
				$("#thank > .recommend").text(" (" + data.thank + ")");				
			},
			error: function(xhr, status, error) {
				alert("error : " + xhr.statusText + ", " + status + ", " + error);
			}
		});
	});	


	// 댓글 쓰기가 클릭되었을 때 이벤트 처리 
	$("#replyWrite").on("click", function() {
		
		// 화면에 보이는 상태인지 체크
		console.log($("#replyForm").css("display"));
		console.log($("#replyForm").is(":visible"));
		
		// 댓글 쓰기 폼이 화면에 보이는 상태라면
		if($("#replyForm").is(":visible")) {
			
			/* 댓글 쓰기 폼이 현재 보이는 상태이고 댓글 쓰기 위치가 아닌
			 * 댓글 수정에 있으면 댓글 쓰기 폼을 슬라이드 업 하고 댓글 쓰기
			 * 위치로 이동시켜 0.3초 후에 슬라이드 다운을 한다. 
			 **/
			var $prev = $("#replyTitle").prev();
			if(! $prev.is("#replyForm")) {
				$("#replyForm").slideUp(300);
			}
			setTimeout(function(){
				$("#replyForm").insertBefore("#replyTitle").slideDown(300);
			}, 300);	
					
		} else {	// 댓글 쓰기 폼이 보이지 않는 상태라면		
			$("#replyForm").removeClass("d-none")
				.css("display", "none").insertBefore("#replyTitle").slideDown(300);			
		}
		
		/* 댓글 쓰기 폼과 댓글 수정 폼을 같이 사용하기 때문에 아래와 같이 id를
		 * 동적으로 댓글 쓰기 폼으로 변경하고 댓글 수정 버튼이 클릭될 때 추가한 
		 * data-no라는 속성을 삭제 한다.
		 **/
		$("#replyForm").find("form")
			.attr("id", "replyWriteForm").removeAttr("data-no");
		$("#replyContent").val("");
		$("#replyWriteButton").val("댓글쓰기");
		
	});
	
		
	/* 댓글 쓰기 폼이 submit 될 때
	 * 최초 한 번은 완성된 html 문서가 화면에 출력되지만 댓글 쓰기를 한 번
	 * 이상하게 되면 ajax 통신을 통해 받은 데이터를 이전 화면과 동일하게
	 * 출력하기 위해 그때 그때 동적으로 요소를 생성하기 때문에 delegate 방식의
	 * 이벤트 처리가 필요하다. 댓글 쓰기, 수정 또는 삭제하기를 한 후에
	 * 결과 데이터를 화면에 출력하면서 자바스크립트를 이용해 html 요소를
	 * 동적으로 생성하기 때문에 이벤트 처리가 제대로 동작하지 않을 수 있다.
	 * 이럴 때는 아래와 같이 delegate 방식의 이벤트 처리가 필요하다.
	 **/
	$(document).on("submit", "#replyWriteForm", function(e) {
 		 	
 		if($("#replyContent").val().length < 5) {
 			alert("댓글은 5자 이상 입력해야 합니다.");
 			return false;
 		}
 		
 		var params = $(this).serialize();
 		console.log(params);	
 		
 		$.ajax({
 			"url": "replyWrite.ajax",
 			"data": params,
 			"type": "post",
 			"dataType": "json",
 			"success": function(resData) {
 				console.log(resData);
 				
 				// 기존에 화면에 출력된 내용은 삭제한다.
 				$("#replyList").empty(); 			
 				
				// 반복문을 통해서 - html 형식으로 작성
				$.each(resData, function(i, v) {
 				
 					// v.regData == 1672300816000
 					var date = new Date(v.regDate);
 					var strDate = date.getFullYear() + "-" + ((date.getMonth() + 1 < 10) 
 								? "0" + (date.getMonth() + 1) : (date.getMonth() + 1)) + "-"
 								+ (date.getDate() < 10 ? "0" + date.getDate() : date.getDate()) +  " " 
 								+ (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":"
 								+ (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes()) + ":"
 								+ (date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds());
 					 
	 				var result = 
	 					'<div class="row border border-top-0 replyRow">'
						+ '<div class="col">'
						+ '	<div class="row bg-light p-2">'									
						+ '		<div class="col-4">'						
						+ '			<span>' + v.replyWriter + '</span>'
						+ '		</div>'
						+ '		<div class="col-8 text-end">'
						+ '			<span class="me-3">' + strDate + "</span>"
						+ '			<button class="modifyReply btn btn-outline-success btn-sm" data-no="' + v.no + '">'
						+ '				<i class="bi bi-journal-text">수정</i>'								
						+ '			</button>'
						+ '			<button class="deleteReply btn btn-outline-warning btn-sm" data-no="' + v.no + '">'
						+ '				<i class="bi bi-trash">삭제</i>'	
						+ '			</button>'
						+ '			<button class="btn btn-outline-danger btn-sm" onclick="reportReply(\'' + v.no + '\')">'
						+ '				<i class="bi bi-telephone-outbound">신고</i>'								
						+ '			</button>'
						+ '		</div>'
						+ '	</div>'
						+ ' 	<div class="row">'						
						+ '		<div class="col p-3">'
						+ '			<pre>' + v.replyContent + '</pre>'
						+ '		</div>'
						+ ' 	</div>'
						+ '</div>'
					+ '</div>'
	 					 				
	 				$("#replyList").append(result);
	 				$("#replyList").removeClass("text-center");
	 				$("#replyList").removeClass("p-5");
 				 				
 				}); // end $.each()				
 				
 				// 댓글 쓰기가 완료되면 폼을 숨긴다.
 				$("#replyForm").slideUp(300)
 					.add("#replyContent").val(""); 				
 			},
 			"error": function(xhr, status) {
 				console.log("error : " + status);
 			}
 		});
	
		// 폼이 submit 되는 것을 취소한다.
		return false;
 	});
		
		
	/* 댓글 수정 버튼이 클릭되면
	 * 댓글을 수정한 후에 동적으로 요소를 생성하기 때문에 delegate 방식으로 이벤트를
	 * 등록해야 한다. 만약 $(".modifyReply").click(function() {}); 형식으로 이벤트를
	 * 등록했다면 새로운 댓글을 등록하거나, 수정 또는 삭제한 후에는 클릭 이벤트가
	 * 제대로 동작되지 않을 수 있기 때문에 delegate 방식의 이벤트 처리가 필요하다.
	 **/
	$(document).on("click", ".modifyReply", function() {
				
		// 화면에 보이는 상태인지 체크
		console.log($("#replyForm").css("display"));
		console.log($("#replyForm").is(":visible"));
		
		// 수정 버튼이 클릭된 최상의 부모를 구한다.
		console.log($(this).parents(".replyRow"));
		var $replyRow = $(this).parents(".replyRow");
		
		// 댓글 쓰기 폼이 화면에 보이는 상태라면
		if($("#replyForm").is(":visible")) {
			
			/* 댓글 쓰기 폼이 현재 보이는 상태이고 현재 클릭된 수정 버튼의
			 * 부모 요소의 다음 요소가 아니라면 댓글 쓰기 폼을 슬라이드 업 하고
			 * 댓글 수정 위치로 이동시켜 0.3초 후에 슬라이드 다운을 한다. 
			 **/
			var $next = $replyRow.next();
			if(! $next.is("#replyForm")) {
				$("#replyForm").slideUp(300);
			}
			setTimeout(function(){
				$("#replyForm").insertAfter($replyRow).slideDown(300);
			}, 300);	
					
		} else { // 댓글 쓰기 폼이 화면에 보이지 않는 상태라면			
			$("#replyForm").removeClass("d-none")
				.css("display", "none").insertAfter($replyRow).slideDown(300);			
		}
		
		/* 댓글 쓰기 폼과 댓글 수정 폼을 같이 사용하기 때문에 아래와 같이 동적으로
		 * id를 replyUpdateForm으로 변경하고 댓글 수정 버튼에서 data-no라는
		 * 속성의 값을 읽어와 댓글 수정 폼의 data-no라는 속성을 추가한다.
		 **/
		$("#replyForm").find("form")
			.attr({"id": "replyUpdateForm", "data-no": $(this).attr("data-no") });		
		$("#replyWriteButton").val("댓글수정");
		
		// 현재 클릭된 수정 버튼이 있는 댓글을 읽어와 수정 폼의 댓글 입력란에 출력한다.
		var reply = $(this).parent().parent().next().find("pre").text();
		$("#replyContent").val($.trim(reply));
				
	});
		
		
	// 댓글 수정 폼이 submit 될 때	 
	$(document).on("submit", "#replyUpdateForm", function() {	
	//$("#replyUpdateForm").on("submit", function() {	
	
		if($("#replyContent").val().length <= 5) {
			alert("댓글은 5자 이상 입력해야 합니다.");
			// Ajax 요청을 취소한다.
			return false;
		}
 				
		/* 아래에서 $("#replyTable").empty(); 가 호출되면 댓글 쓰기
		 * 폼도 같이 문서에서 삭제되기 때문에 폼을 원래 위치로 이동시킨다.
		 **/				
		$("#global-content > div").append($("#replyForm").slideUp(300));
		
		/* replyNo는 최초 폼이 출력될 때 설정되지 않았다.
		 * 댓글 쓰기 폼과 댓글 수정 폼을 하나로 처리하기 때문에 댓글 번호는
		 * 동적으로 구하여 요청 파라미터에 추가해 줘야 한다. 댓글 수정시
		 * 댓글 번호를 서버로 전송해야 댓글 번호에 해당하는 댓글을 수정할 수 있다. 
		 **/
		var params = $(this).serialize() + "&no=" + $(this).attr("data-no");
		console.log(params);
				
		$.ajax({
			url: "replyUpdate.ajax",
			type: "patch",
			data: params,
			dataType: "json",
			success: function(resData, status, xhr) {								
	
				console.log(resData);
 				
 				// 반복문을 통해서 - html 형식으로 작성
 				$("#replyList").empty();
 				$.each(resData, function(i, v) {
 				
 					// v.regData == 1672300816000
 					var date = new Date(v.regDate);
 					var strDate = date.getFullYear() + "-" + ((date.getMonth() + 1 < 10) 
 								? "0" + (date.getMonth() + 1) : (date.getMonth() + 1)) + "-"
 								+ (date.getDate() < 10 ? "0" + date.getDate() : date.getDate()) +  " " 
 								+ (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":"
 								+ (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes()) + ":"
 								+ (date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds());
 					 
	 				var result = 
	 					'<div class="row border border-top-0 replyRow">'
						+ '<div class="col">'
						+ '	<div class="row bg-light p-2">'									
						+ '		<div class="col-4">'						
						+ '			<span>' + v.replyWriter + '</span>'
						+ '		</div>'
						+ '		<div class="col-8 text-end">'
						+ '			<span class="me-3">' + strDate + "</span>"
						+ '			<button class="modifyReply btn btn-outline-success btn-sm" data-no="' + v.no + '">'
						+ '				<i class="bi bi-journal-text">수정</i>'								
						+ '			</button>'
						+ '			<button class="deleteReply btn btn-outline-warning btn-sm" data-no="' + v.no + '">'
						+ '				<i class="bi bi-trash">삭제</i>'	
						+ '			</button>'
						+ '			<button class="btn btn-outline-danger btn-sm" onclick="reportReply(\'' + v.no + '\')">'
						+ '				<i class="bi bi-telephone-outbound">신고</i>'								
						+ '			</button>'
						+ '		</div>'
						+ '	</div>'
						+ ' 	<div class="row">'						
						+ '		<div class="col p-3">'
						+ '			<pre>' + v.replyContent + '</pre>'
						+ '		</div>'
						+ ' 	</div>'
						+ '</div>'
					+ '</div>'
	 					 				
	 				$("#replyList").append(result);
 				 				
 				}); // end $.each()
 				
 				// 댓글 수정하기가 완료되면 폼에 작성된 댓글 내용을 지운다.
 				$("#replyContent").val("");
 				
			},
			error: function(xhr, status, error) {
				alert("ajax 실패 : " + status + " - " + xhr.status);
			}
			
		});
		
		// 폼이 submit 되는 것을 취소한다.
		return false;
	});


	/* 댓글 삭제 버튼이 클릭되면
	 * 댓글을 삭제한 후에 동적으로 요소를 생성하기 때문에 delegate 방식으로 이벤트를
	 * 처리를 해야 한다. 만약 $(".deleteReply").click(function() {}); 와 같이 이벤트를 
	 * 등록했다면 댓글을 삭제한 후에는 클릭 이벤트가 제대로 동작되지 않을 수 있기 때문에
	 * 아래 코드와 같이 delegate 방식의 이벤트 처리가 필요하다.
	 **/
	$(document).on("click", ".deleteReply", function() {	
		
 		/* 아래에서 $("#replyTable").empty(); 가 호출되면 댓글 쓰기
		 * 폼도 같이 문서에서 삭제되기 때문에 폼을 원래 위치로 이동시킨다.
		 **/	
 		$("#global-content > div").append($("#replyForm").slideUp(300));
 		$("#replyContent").val("");		
		
		var no = $(this).attr("data-no");
		var writer = $(this).parent().prev().find("span").text();
		var bbsNo = $("#replyForm input[name=bbsNo]").val();
		var params = "no=" + no + "&bbsNo=" + bbsNo;	
		console.log(params);
		
		var result = confirm(writer + "님이 작성한 " + no +"번 댓글을 삭제하시겠습니까?");
		
		if(result) {
			$.ajax({
				url: "replyDelete.ajax",
				type: "delete",
				data: params,
				dataType: "json",
				success: function(resData, status, xhr) {					
					console.log(resData);
	 				$("#replyList").empty();
	 				
	 				// 반복문을 통해서 - html 형식으로 작성
	 				$.each(resData, function(i, v) {
	 				
	 					// v.regData == 1672300816000
	 					var date = new Date(v.regDate);
	 					var strDate = date.getFullYear() + "-" + ((date.getMonth() + 1 < 10) 
	 								? "0" + (date.getMonth() + 1) : (date.getMonth() + 1)) + "-"
	 								+ (date.getDate() < 10 ? "0" + date.getDate() : date.getDate()) +  " " 
	 								+ (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":"
	 								+ (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes()) + ":"
	 								+ (date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds());
	 					 
		 				var result = 
		 					'<div class="row border border-top-0 replyRow">'
							+ '<div class="col">'
							+ '	<div class="row bg-light p-2">'									
							+ '		<div class="col-4">'						
							+ '			<span>' + v.replyWriter + '</span>'
							+ '		</div>'
							+ '		<div class="col-8 text-end">'
							+ '			<span class="me-3">' + strDate + "</span>"
							+ '			<button class="modifyReply btn btn-outline-success btn-sm" data-no="' + v.no + '">'
							+ '				<i class="bi bi-journal-text">수정</i>'								
							+ '			</button>'
							+ '			<button class="deleteReply btn btn-outline-warning btn-sm" data-no="' + v.no + '">'
							+ '				<i class="bi bi-trash">삭제</i>'	
							+ '			</button>'
							+ '			<button class="btn btn-outline-danger btn-sm" onclick="reportReply(\'' + v.no + '\')">'
							+ '				<i class="bi bi-telephone-outbound">신고</i>'								
							+ '			</button>'
							+ '		</div>'
							+ '	</div>'
							+ ' 	<div class="row">'						
							+ '		<div class="col p-3">'
							+ '			<pre>' + v.replyContent + '</pre>'
							+ '		</div>'
							+ ' 	</div>'
							+ '</div>'
						+ '</div>'
		 					 				
		 				$("#replyList").append(result);
	 				 				
	 				}); // end $.each()
				},
				error: function(xhr, status, error) {
					alert("ajax 실패 : " + status + " - " + xhr.status);
				}
			});
		}
		// 앵커 태그에 의해 페이지가 이동되는 것을 취소한다.
		return false;
	});	
});

/* 아래는 신고하기 버튼을 임시로 연결한 함수
 * DOM(Document Object Model)이 준비되면 호출되는 콜백 함수 밖에 작성
 **/
function reportReply(elemId) {
	var result = confirm("이 댓글을 신고하시겠습니까?");
	if(result == true) {
		alert("report - " + result);
	}	
}