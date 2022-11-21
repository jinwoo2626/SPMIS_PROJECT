package dongyang.spmis.model.Document;

import dongyang.spmis.model.Task.TaskDTO;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
public class DocumentDTO {

    //나의 활동내역출력
    private ArrayList<TaskDTO> tasklist;
    //task설정
    private TaskDTO task;
    //평균기여도
    private int avgcontribution;
    //프로젝트별 기여도
    private ArrayList<Integer> pjtcontribution;
    //작업별 기여도
    private ArrayList<KanbancontributionDTO> kanbancontribution;
    //지각한 작업
    private ArrayList<TaskDTO> tasklatelist;
    //프로젝트별 지각비율
    private ArrayList<Integer> tasklateRatio;
    //평균 지각비율
    private int avgtasklate;

    //활동내역체크
    private DocumentCheckDTO check;

    @Builder
    public DocumentDTO(ArrayList<TaskDTO> tasklist, TaskDTO task, int avgcontribution, ArrayList<Integer> pjtcontribution,
                       ArrayList<KanbancontributionDTO> kanbancontribution, ArrayList<TaskDTO> tasklatelist,
                       ArrayList<Integer> tasklateRatio, int avgtasklate, DocumentCheckDTO check) {
        this.tasklist = tasklist;
        this.task = task;
        this.avgcontribution = avgcontribution;
        this.pjtcontribution = pjtcontribution;
        this.kanbancontribution = kanbancontribution;
        this.tasklatelist = tasklatelist;
        this.tasklateRatio = tasklateRatio;
        this.avgtasklate = avgtasklate;
        this.check = check;
    }
}
