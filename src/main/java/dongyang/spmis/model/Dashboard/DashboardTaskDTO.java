package dongyang.spmis.model.Dashboard;

import lombok.Data;

import java.util.Date;
@Data
public class DashboardTaskDTO {
    private int task_id;
    private String project_name;
    private int project_id;
    private String task_subject;
    private Date task_create_date;
    private int views;
    private String kanban_status;
    private Date start_date;
    private Date final_date;
    private Date final_expect_date;
}
