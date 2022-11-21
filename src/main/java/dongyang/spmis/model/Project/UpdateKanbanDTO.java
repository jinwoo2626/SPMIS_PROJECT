package dongyang.spmis.model.Project;

import lombok.Data;

@Data
public class UpdateKanbanDTO {
    int project_id;
    int kanban_id;
    int task_id;
    int kanban_status;
}
