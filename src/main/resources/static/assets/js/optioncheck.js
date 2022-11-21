
function CheckForm(){ 

 var isProjectChk = false;
        var arr_Project = document.getElementsByName("selectproject");
        for(var i=0;i<arr_Project.length;i++){
            if(arr_Project[i].checked == true) {
                isProjectChk = true;
                break;
            }
        }
    
        if(!isProjectChk){
            alert("활동내역을 출력할 프로젝트를 선택해주세요"); 
            return false;
        }
    var isActionChk = false;
        var arr_Option = document.getElementsByName("selectaction");
        for(var i=0;i<arr_Option.length;i++){
            if(arr_Option[i].checked == true) {
                isActionChk = true;
                break;
            }
        }
    
        if(!isActionChk){
            alert("출력할 옵션을 선택해주세요");
            return false;
        }
}
