package dongyang.spmis.model.Task;

import lombok.Data;

@Data
public class KanbanDTO {
	private int kanban_id;
	private String kanban_status;
	private int project_id;
	
}
