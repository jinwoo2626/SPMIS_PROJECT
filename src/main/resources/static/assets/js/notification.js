//window.addEventListener("beforeunload", (event) => {
//  event.preventDefault();
//});

//window.onbeforeunload = function (e) {
//    console.log("reload");
//
//   $.ajax({
//    type: "get" ,
//    url: "noticeCnt",
//    dataType: "json"
//    }).fail(function(jqXHR, textStatus, errorThrown){
//            console.log(jqXHR);  //응답 메시지
//            console.log(textStatus); //"error"로 고정인듯함
//            console.log(errorThrown);
//    })
//    .done(function(result){
//        console.log("success");
//        console.log(result);
//        $("#notification").html(result);
//    })
//};
   window.addEventListener('beforeunload', call_unload);

    function call_unload() {
        $.ajax({
            type: "get" ,
            url: "noticeCnt",
            dataType: "json",
            async: false
            }).fail(function(jqXHR, textStatus, errorThrown){
                    console.log(jqXHR);  //응답 메시지
                    console.log(textStatus); //"error"로 고정인듯함
                    console.log(errorThrown);
            })
            .done(function(result){
                console.log("success");
                $("#notification").html(result);
            })
    }

