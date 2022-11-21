package dongyang.spmis.model.Project;

import lombok.Data;

@Data
public class NoticeCheck {
    private int check_id;
    private String user_email;
    private int notice_id;
    private boolean confirm;

    private String notice;

    private String project_name;
    private String project_id;
}
