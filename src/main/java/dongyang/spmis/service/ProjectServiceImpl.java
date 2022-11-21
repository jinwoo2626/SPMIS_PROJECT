package dongyang.spmis.service;

import dongyang.spmis.mapper.DiscordMapper;
import dongyang.spmis.mapper.ProjectMapper;
import dongyang.spmis.mapper.UserMapper;
import dongyang.spmis.model.Dashboard.DashboardTaskDTO;
import dongyang.spmis.model.Project.*;
import dongyang.spmis.model.Task.*;
import dongyang.spmis.model.User.UserDTO;
import dongyang.spmis.properties.PSProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private DiscordMapper discordMapper;
	
	@Override
	public ArrayList<ProjectDTO> selectProjects(UserDTO user) {
		// TODO Auto-generated method stub
		return projectMapper.selectProjectList(user);
	}

	@Override
	public ArrayList<ProjectListDTO> selectProjectList(String user_email) {
		return projectMapper.findProjectListByUserEmail(user_email);
	}

	@Override
	public ArrayList<ProjectDTO> selectPublicProjects() {
		// TODO Auto-generated method stub
		return projectMapper.selectPublicProjectList();
	}
	
	@Override
	public int selectPublicProjectCnt() {
		return projectMapper.selectPublicProjectCnt();
	}
	
	
	
	@Override	
	public boolean createProject(ProjectDTO project) {
		// TODO Auto-generated method stub
		
		
		return projectMapper.createProject(project);
	}
	
	@Override
	// 프로젝트 조회
	public ProjectDTO selectOneProject(ProjectDTO project) {
		return projectMapper.selectOneProject(project);
	}
	
	// 프로젝트 조회
	@Override
	public ProjectDTO selectOneProjectToProjectJoin(ProjectJoinDTO join) {
		return projectMapper.selectOneProjectToProjectJoin(join.getProject_id());
	}
	
	@Override
	public ProjectDTO selectLatestProject() {
		return projectMapper.selectLatestProject();
	}

	@Override
	public boolean updatePrivacyScope(int project_id, String privacy_scope) {
		return projectMapper.updatePrivacyScope(project_id, privacy_scope);
	}

	public ArrayList<ProjectDTO> selectPagingProjects(int startIndex, int pageSize){
		
		return projectMapper.selectPagingProjectList(startIndex, pageSize);
	}
	
	@Override
	public boolean deleteProject(ProjectDTO project) {
		// TODO Auto-generated method stub
		return projectMapper.deleteProject(project);
	}
	
	@Override
	public boolean deleteProjectBoard(ProjectDTO project) {
		// TODO Auto-generated method stub		
		return projectMapper.deleteProjectBoard(project);
	}
	
	@Override
	public boolean deleteProjectKanban(ProjectDTO project) {
		// TODO Auto-generated method stub		
		return projectMapper.deleteProjectKanban(project);
	}

	@Override
	public ProjectTask findProjectTask(int task_id) {
		TaskJoinKanban task =  projectMapper.findTaskJoinKanbanByTaskID(task_id);
		ProjectTask projectTask = ProjectTask.builder()
				.task_id(task_id)
				.project_id(task.getProject_id())
				.task_subject(task.getTask_subject())
				.content(task.getContent())
				.task_create_date(task.getTask_create_date())
				.create_user_email(task.getCreate_user_email())
				.start_user_email(task.getStart_user_email())
				.views(task.getViews())
				.start_date(task.getStart_date())
				.final_date(task.getFinal_date())
				.final_expect_date(task.getFinal_expect_date())
				.kanban_id(task.getKanban_id())
				.kanban_status(task.getKanban_status())
				.build();
		projectTask.setComments(projectMapper.commentByTaskID(task_id));
		projectTask.setStart_user_name(userMapper.findUserNameByUserEmail(projectTask.getStart_user_email()));
		projectTask.setCreate_user_name(userMapper.findUserNameByUserEmail(projectTask.getCreate_user_email()));


		return projectTask;
	}

	@Override
	public boolean deleteProjectRule(ProjectDTO project) {
		// TODO Auto-generated method stub		
		return projectMapper.deleteProjectRule(project);
	}
	
	@Override
	public boolean deleteProjectJoin(ProjectDTO project) {
		// TODO Auto-generated method stub		
		return projectMapper.deleteProjectJoin(project);
	}

	@Override 
	public boolean createDefaultKanban(ProjectDTO project) {
		KanbanDTO kanban = new KanbanDTO();
		kanban.setProject_id(project.getProject_id());
		kanban.setKanban_status("TO DO");
		if(!projectMapper.createKanban(kanban)) return false;
		kanban.setKanban_status("DOING");
		if(!projectMapper.createKanban(kanban)) return false;
		kanban.setKanban_status("DONE");
		if(!projectMapper.createKanban(kanban)) return false;
		
		
		return true;
	}
	
	@Override
	public ArrayList<KanbanDTO> selectKanbanStatus(ProjectDTO project) {
		// TODO Auto-generated method stub
		return projectMapper.kanbanList(project);
	}

	@Override
	public boolean createKanbanStatus(KanbanDTO status) {
		// TODO Auto-generated method stub		
		return projectMapper.createKanban(status);
	}

	@Override
	public boolean deleteKanbanStatus(KanbanDTO status) {
		// TODO Auto-generated method stub		
		return projectMapper.deleteKanban(status);
	}

	// 프로젝트 task(board)와 user_name 같이 불러오기
	@Override
	public ArrayList<TaskJoinUserDTO> selectTaskJoinUsers(ProjectDTO project){
		ArrayList<TaskJoinUserDTO> boards = projectMapper.selectTaskJoinUsers(project);
		
		for(int i =0; i < boards.size(); i++) {
			// 보드 한개 가져와서 객체에 넣음
			TaskJoinUserDTO board = boards.get(i);
			
			// 유저 객체 생성
			UserDTO userInfo = new UserDTO();
			
			// create유저 name 검색
			userInfo.setUser_email(board.getCreate_user_email());
			userInfo = userMapper.getUser(userInfo);
			// board 객체에 create 유저 name 값 넣어주기
			board.setCreate_user_name(userInfo.getUser_name());
			
			// start유저 name 검색
			userInfo.setUser_email(board.getStart_user_email());
			userInfo = userMapper.getUser(userInfo);
			// board 객체에 start 유저 name 값 넣어주기
			board.setStart_user_name(userInfo.getUser_name());
			
			// 원래 위치에 board 객체 수정해서 넣어주기 			
			boards.set(i, board);	
		}
		// boards 객체 반환
		return boards;
	}

	@Override
	public ArrayList<TaskJoinKanbanUserDTO> selectTaskJoinKanbanUser(int project_id) {
		return projectMapper.selectTaskJoinKanbanUser(project_id);
	}

	@Override
	public ArrayList<TaskDTO> selectBoards(ProjectDTO project) {
		// TODO Auto-generated method stub
		return projectMapper.taskList(project);
	}

	@Override
	public ArrayList<TaskDTO> selectKanbanTasks(KanbanDTO project) {
		// TODO Auto-generated method stub
		return projectMapper.taskByKanban(project);
	}
	
	@Override
	public boolean createTask(TaskDTO task) {
		// TODO Auto-generated method stub
		System.out.println("tet: " +task);
		String kanban_status = projectMapper.selectKanbanStatus(task.getKanban_id()).getKanban_status();

		// board가 생성될때 To Do 단계면 마감기한만 입력
		if(kanban_status.equals("TO DO")) {
			return projectMapper.createTask(task);
		}
		else if(kanban_status.equals("DOING")) {// board가 생성될때 Doing 단계면 마감기한, 작업시작 날짜 입력
			return projectMapper.createDoingTask(task);
		} else if(kanban_status.equals("DONE")) {// board가 생성될때 Done 단계면 마감기한, 작업시작 날짜, 작업 종료 날짜 입력
			return projectMapper.createDoneTask(task);
		}
		return projectMapper.createTask(task);
	}

	@Override
	public boolean deleteTask(TaskDTO board) {
		// TODO Auto-generated method stub		
		return projectMapper.deleteTask(board);
	}

	@Override
	public boolean updateTask(TaskDTO task) {
		// TODO Auto-generated method stub
		return projectMapper.updateTask(task);
	}
	
	@Override
	public ArrayList<TaskJoinKanban> selectTaskJoinKanban(UserDTO user){
		return projectMapper.selectTaskJoinKanban(user);
	}

	@Override
	public ArrayList<DashboardTaskDTO> selectDashboardTask(String user_email) {
		return projectMapper.findDashboardTaskByUserEmail(user_email);
	}

	@Override
	public ArrayList<UserDTO> getuserlist(ProjectJoinDTO user) {
		return projectMapper.getuserlist(user);
	}

	@Override
	public ArrayList<KanbanDTO> getkanbanlist(KanbanDTO kanban) {
		return projectMapper.getkanbanlist(kanban);
	}

	@Override
	public ArrayList<Integer> gettasklist(TaskDTO task) {
		return projectMapper.gettasklist(task);
	}

	@Override
	public ArrayList<ProjectRuleDTO> selectRule(ProjectDTO project) {
		// TODO Auto-generated method stub
		return projectMapper.rule(project);
	}

	@Override
	public boolean createRule(ProjectRuleDTO rule) {
		// TODO Auto-generated method stub		
		return projectMapper.createRule(rule);
	}

	
	
	@Override
	public boolean deleteRule(ProjectRuleDTO rule) {
		// TODO Auto-generated method stub		
		return projectMapper.deleteRule(rule);
	}

	@Override
	public ArrayList<TaskCommentDTO> selectComment(int task_id) {
		// TODO Auto-generated method stub
		return projectMapper.commentByTaskID(task_id);
	}

	@Override
	public ArrayList<TaskCommentDTO> selectCommentByProjectID(int project_id) {

		return projectMapper.commentByProjectID(project_id);
	}

	@Override
	public boolean createComment(TaskCommentDTO comment) {
		// TODO Auto-generated method stub		
		return projectMapper.createComment(comment);
	}

	@Override
	public boolean deleteComment(TaskCommentDTO comment) {
		// TODO Auto-generated method stub		
		return projectMapper.deleteComment(comment);
	}

	@Override
	public ProjectJoinDTO selectGroupCheck(ProjectDTO project, UserDTO user) {
		return projectMapper.selectGroupCheck(project.getProject_id(), user.getUser_email());
	}
	
	// 프로젝트의 admin(관리자)인지 체크
	@Override	
	public ProjectJoinDTO selectGroupAdmin(ProjectDTO project, UserDTO user){
		return projectMapper.selectGroupAdminCheck(project.getProject_id(), user.getUser_email());
	}
	
	@Override
	public ArrayList<ProjectJoinDTO> selectGroup(ProjectDTO project) {
		// TODO Auto-generated method stub		
		return projectMapper.group(project.getProject_id());
	}

	public ArrayList<ProjectJoinDTO> selectInviteGroup(UserDTO user){
		
		return projectMapper.selectInviteGroup(user);
	}
	
	@Override
	public boolean insertGroup(ProjectJoinDTO join) {
		// TODO Auto-generated method stub		
		if(projectMapper.checkJoin(join) != null) {
			System.out.println("이미 등록된 사용자 있음");
			return false;
		}
		return projectMapper.insertGroup(join);
	}

	@Override
	public boolean deleteProjectUser(ProjectJoinDTO join) {
		// TODO Auto-generated method stub		
		return projectMapper.deleteGroup(join);
	}
	
	@Override
	public boolean updateGroup(ProjectJoinDTO join) {
		// TODO Auto-generated method stub		
		return projectMapper.updateGroup(join);
	}

	@Override
	public boolean updateRole(ProjectJoinDTO join) {
		
		return projectMapper.updateRole(join);
	}


	@Override
	public boolean addDiscordLink(DiscordLinkDTO discordLinkDTO) {
		return discordMapper.saveDiscordLink(discordLinkDTO);
	}

	@Override
	public DiscordLinkDTO FindDiscordLinkByProjectID(int project_id) {
		return discordMapper.discordLinkFindByProjectID(project_id);
	}

	@Override
	public ArrayList<ProjectJoinDiscordDTO> FindDiscordLinkByUserEmail(String user_email) {
		return discordMapper.discordLinkFindByUserEmail(user_email);
	}




	@Override
	public boolean addTaskComment(TaskCommentDTO taskCommentDTO) {
		return projectMapper.createComment(taskCommentDTO);
	}


	@Override
	public KanbanDTO selectKanbanStatus(TaskDTO task) {
		// TODO Auto-generated method stub
		
		return projectMapper.selectKanbanStatus(task.getKanban_id());
	}

	@Override
	public PSProperties updateProjectExpectFinal(ProjectFinalExpectDateDTO finalExpectDateDTO) {

		// 예상 종료시간이 프로젝트 시작시간보다 빠르거나 같으면 리턴
		Date createDate =  projectMapper.findProjectCreateDate(finalExpectDateDTO.getProject_id()) ;
		if(!createDate.before(finalExpectDateDTO.getProject_final_expect_date())){
			return PSProperties.FAST_THAN_START_TIME;
		}

		if(projectMapper.updateFinalExpectDate(finalExpectDateDTO)){
			return PSProperties.SUCCESS;
		}

		return PSProperties.FAILED;
	}

	@Override
	public PSProperties updateProjectFinal(int project_id) {
		if(projectMapper.updateFinalDate(project_id)){
			return PSProperties.SUCCESS;
		}
		return PSProperties.FAILED;
	}

	@Override
	public PSProperties cancelProjectFinal(int project_id) {
		if(projectMapper.cancelFinalDate(project_id)){
			return PSProperties.SUCCESS;
		}
		return PSProperties.FAILED;
	}


	@Override
	public PSProperties completeProject() {
		return null;
	}

	@Override
	public boolean updateTaskKanbanID(UpdateKanbanDTO kanban) {

		String kanban_status = projectMapper.selectKanbanStatus(kanban.getKanban_id()).getKanban_status();
		if(kanban_status.equals("DONE")){
			projectMapper.updateTaskFinal(kanban.getTask_id());
		}else if(kanban_status.equals("DONE")) {
			projectMapper.updateTaskStart(kanban.getTask_id());
			projectMapper.updateTaskNotFinal(kanban.getTask_id());
		}else {
			projectMapper.updateTaskNotFinal(kanban.getTask_id());
		}

		return projectMapper.updateTaskKanbanID(kanban.getKanban_id(), kanban.getTask_id());
	}

	@Override
	public ProjectDTO selectOneProject(int project_id) {
		// TODO Auto-generated method stub
		return projectMapper.selectOneProjectForProject_id(project_id);
	}

}
