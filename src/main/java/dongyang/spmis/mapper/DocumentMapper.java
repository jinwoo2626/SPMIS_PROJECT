package dongyang.spmis.mapper;

import dongyang.spmis.model.Project.ProjectDTO;
import dongyang.spmis.model.Project.ProjectJoinDTO;
import dongyang.spmis.model.Task.KanbanDTO;
import dongyang.spmis.model.Task.TaskDTO;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

public interface DocumentMapper extends DefaultDBInfo {

	@Select("SELECT p.* FROM " + PROJECTJOIN + " pj, "  + PROJECT + " p WHERE user_email = #{user_email} and p.project_id = pj.project_id")
	ArrayList<ProjectDTO> select(ProjectJoinDTO user);

	@Select("SELECT * FROM " + PROJECT + " WHERE project_id = #{project_id}")
	ProjectDTO select2(int project_id);
	//평균기여도
	@Select("SELECT count(*) FROM " + TASK + " WHERE project_id =#{project_id} and start_user_email =#{start_user_email}")
	double cntusertask(TaskDTO task);

	@Select("SELECT count(*) FROM " + TASK + " WHERE project_id =#{project_id}")
	double cntwholetask(TaskDTO task);

	@Select("SELECT * FROM " + KANBAN + " WHERE project_id =#{project_id}")
	ArrayList<KanbanDTO> getkanbanid (int project_id);

	@Select("SELECT * FROM " + TASK + " WHERE project_id =#{project_id} and start_user_email =#{start_user_email} order by kanban_id")
	ArrayList<TaskDTO> documentinfo(TaskDTO task);

	@Select("SELECT count(*) FROM " + TASK + " WHERE project_id =#{project_id} and kanban_id =#{kanban_id}")
	double cntwholetaskkanban(TaskDTO task);

	@Select("SELECT count(*) FROM " + TASK + " WHERE project_id =#{project_id} and start_user_email = #{start_user_email} and kanban_id =#{kanban_id}")
	double cntusertaskkanban(TaskDTO task);

	@Select("SELECT project_name FROM " + PROJECT + " WHERE project_id =#{project_id}")
	String getproject_name(String project_id);
}
