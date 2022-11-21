$('#modal_add_task').on('shown.bs.modal', function (event) { 
  var e = $(event.relatedTarget);    
  $('#kanban_id').val(e.data('kanbanid')) ;
    
});

$('#modal_edit_task').on('shown.bs.modal', function (event) { 
    var e = $(event.relatedTarget);   
    var task_id = { task_id : e.data('task_id') }
    $.ajax({
        type: "get" ,
        url: "task_comment",
        data: task_id,
        dataType: "text",
        success : function(res){
            console.log("success :: " + res);
        }
    }).fail(function(jqXHR){
                          console.log("fail")
                            console.log(jqXHR);
                  })
                  .done(function(result){
                  console.log("success1")
                  $("#comments").replaceWith(result);
                  console.log("success")
                      console.log(result);
                      console.log("t : "+task_id.task_id);
                      console.log("t2 : "+task_id);
                  })
    $('#task_name').html(e.data('task_name')) ;
    $('#create_nick').html(e.data('create_nick')) ;
    $('#create_time').html(e.data('create_time')) ;
    $('#start_nick').html(e.data('start_nick')) ;
    $('#start_user_email').html(e.data('start_user_email')) ;
    $('#taskid').val(task_id.task_id) ;
    $('#final_expect_date').html(e.data('final_expect_date')) ;
    $('#kanban_status').html(e.data('kanban_status')) ;
    tinymce.get('content').setContent(e.data('content'));
    
});

$('#modal_edit_group').on('shown.bs.modal', function (event) { 
  var e = $(event.relatedTarget);    
  $('#edit_user_con').html(e.data('user_email')) ;    $('#edit_user').html(e.data('user_email')) ;
    $('#role').val(e.data('role')) ;
    
});

$('#modal_del_group').on('shown.bs.modal', function (event) { 
  var e = $(event.relatedTarget);    
  $('#del_user').val(e.data('user_email')) ;
    $('#del_user_con').html(e.data('user_email')) ;
    
});

$('#modal_edit_status').on('shown.bs.modal', function (event) { 
  var e = $(event.relatedTarget);    
  $('#task_id').val(e.data('taskid')) ;
    $('#project_id').val(e.data('projectid')) ;
    
});



// $('#edit_task').on('click', function (event) { 
//     event.preventDefault();
//     $('#modal_edit_task').modal('show').find('.modal-body').load($(this).attr('href'));   
    
// });
