package dongyang.spmis.model.Project;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
public class ProjectFinalExpectDateDTO {
    int project_id;
    Date project_final_expect_date;

    @Builder
    public ProjectFinalExpectDateDTO(int project_id, Date project_final_expect_date) {
        this.project_id = project_id;
        this.project_final_expect_date = project_final_expect_date;
    }
}
