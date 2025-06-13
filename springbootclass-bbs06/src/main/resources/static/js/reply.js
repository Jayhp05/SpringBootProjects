$(function() {
// 추천/땡큐 Ajax
	$(".btnCommend").click(function() {
		var com = $(this).attr("id");
		console.log("com : " + com);
		$.ajax({
			url: "recommend.ajax",
			type: "post",
			data : { recommend: com, no : $("#no").val()},
 			dataType: "json",
			success: function(data) {
				/* 추천/땡큐가 반영된 것을 사용자에게 알리고 
				* 응답으로 받은 갱신된 추천하기 데이터를 화면에 표시한다.- 151 
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
	$("#replyWrite").on("click", function() {
		// 화면에 보이는 상태인지 체크
		console.log($("#replyForm").css("display"));
		console.log($("#replyForm").is(":visible"));
		
		// 댓글 쓰기 폼이 화면에 보이는 상태라면
		if($("#replyForm").is(":visible")) {
			var $prev = $("#replyTitle").prev();
			if(! $prev.is("#replyForm")) {
			$("#replyForm").slideUp(300);
		}
		setTimeout(function(){
			$("#replyForm").insertBefore("#replyTitle").slideDown(300);
			}, 300);
		} 
		else { // 댓글 쓰기 폼이 보이지 않는 상태라면
			$("#replyForm").removeClass("d-none")
			.css("display", "none").insertBefore("#replyTitle").slideDown(300);
		}
	
		$("#replyForm").find("form").attr("id", "replyWriteForm").removeAttr("data-no");
		$("#replyContent").val("");
		$("#replyWriteButton").val("댓글쓰기");
	});
	 
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
							+ ' 	<div class="col-4">'
							+ '			<span>' + v.replyWriter + '</span>'
							+ ' 	</div>'
							+ ' 	<div class="col-8 text-end">'
							+ '			<span class="me-3">' + strDate + "</span>"
							+ '			<button class="modifyReply btn btn-outline-success btn-sm" data-no="' + v.no + '">'
							+ '				<i class="bi bi-journal-text">수정</i>'
							+ '			</button>'
							+ '			<button class="deleteReply btn btn-outline-warning btn-sm" data-no="' + v.no + '">'
							+ '				<i class="bi bi-trash">삭제</i>'
							+ '			</button>'
							+ '			<button class="btn-outline-danger btn-sm onclick="reportReply(\'' + v.no + '\')">'
							+ '				<i class="bi bi-telephone-outbound">신고</i>'
							+ '			</button>'
							+ ' 	</div>'
							+ ' </div>'
							+ ' <div class="row">'
							+ ' 	<div class="col p-3">'
							+ '			<pre>' + v.replyContent + '</pre>'
							+ ' 	</div>'
							+ ' </div>'
							+ '</div>'
						+ '</div>'
					$("#replyList").append(result);
					$("#replyList").removeClass("text-center");
					$("#replyList").removeClass("p-5");
				}); // end $.each()
				
				// 댓글 쓰기가 완료되면 폼을 숨긴다.
				$("#replyForm").slideUp(300).add("#replyContent").val(""); 
			},
			"error": function(xhr, status) {
				console.log("error : " + status);
			}
		});
		// 폼이 submit 되는 것을 취소한다.
		return false;
	});
});