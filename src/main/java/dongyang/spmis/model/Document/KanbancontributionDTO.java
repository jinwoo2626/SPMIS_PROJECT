package dongyang.spmis.model.Document;

import lombok.Data;

@Data
public class KanbancontributionDTO {

    private String project_name;

    private int contribution;

    private String kanban_status;
}
