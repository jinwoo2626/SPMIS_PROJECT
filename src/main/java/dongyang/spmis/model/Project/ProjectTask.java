package dongyang.spmis.model.Project;

import dongyang.spmis.model.Task.TaskCommentDTO;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/*
ajax로 내려줄 객체
 */
@Data
public class ProjectTask {
    private int task_id;
    private int project_id;
    private String task_subject;
    private String content;
    private Date task_create_date;
    private String create_user_email;
    private String start_user_email;
    private int views;
    private Date start_date;
    private Date final_date;
    private Date final_expect_date;
    private int kanban_id;
    private String kanban_status;

    @Builder
    public ProjectTask(int task_id, int project_id, String task_subject,
                       String content, Date task_create_date,
                       String create_user_email, String start_user_email,
                       int views, Date start_date, Date final_date,
                       Date final_expect_date, int kanban_id,
                       String kanban_status) {
        this.task_id = task_id;
        this.project_id = project_id;
        this.task_subject = task_subject;
        this.content = content;
        this.task_create_date = task_create_date;
        this.create_user_email = create_user_email;
        this.start_user_email = start_user_email;
        this.views = views;
        this.start_date = start_date;
        this.final_date = final_date;
        this.final_expect_date = final_expect_date;
        this.kanban_id = kanban_id;
        this.kanban_status = kanban_status;
    }
    ProjectTask(){};

    // 댓글 내용
    private List<TaskCommentDTO> comments;

    private String start_user_name;
    private String create_user_name;

}
