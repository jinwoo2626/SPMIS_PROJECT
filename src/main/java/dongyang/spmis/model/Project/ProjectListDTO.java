package dongyang.spmis.model.Project;

import lombok.Data;

@Data
public class ProjectListDTO {
    int project_id;
    String user_email;
    String project_name;
    String project_des;
    String privacy_scope;
    String join_status;
    String role;
}
