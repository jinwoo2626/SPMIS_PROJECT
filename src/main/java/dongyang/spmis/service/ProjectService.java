package dongyang.spmis.service;

import dongyang.spmis.model.Dashboard.DashboardTaskDTO;
import dongyang.spmis.model.Project.*;
import dongyang.spmis.model.Task.*;
import dongyang.spmis.model.User.UserDTO;
import dongyang.spmis.properties.PSProperties;

import java.util.ArrayList;


public interface ProjectService {
	
	// 프로젝트
	
	// 프로젝트 목록 불러오기
	public ArrayList<ProjectDTO> selectProjects(UserDTO user);

	// 프로젝트 목록 불러오기
	public ArrayList<ProjectListDTO> selectProjectList(String user_email);
	
	// 공개범위가 public인 프로젝트 목록 불러오기
	public ArrayList<ProjectDTO> selectPublicProjects();
	
	// 공개범위가 public인 프로젝트 갯수  불러오기
	public int selectPublicProjectCnt();
	
	// 공개범위가 public이고 pagination처리가된 프로젝트 목록 불러오기
	public ArrayList<ProjectDTO> selectPagingProjects(int startIndex, int pageSize);
	
	// 새로운 프로젝트 생성
	public boolean createProject(ProjectDTO project); 
	
	// 프로젝트 삭제
	public boolean deleteProject(ProjectDTO project);
	
	// 프로젝트 삭제 시 task 삭제
	public boolean deleteProjectBoard(ProjectDTO project);	
	
	// 프로젝트 삭제 시 kanban 삭제
	public boolean deleteProjectKanban(ProjectDTO project);

	public ProjectTask findProjectTask(int task_id);
	
	// 프로젝트 삭제 시 Rule 삭제
	public boolean deleteProjectRule(ProjectDTO project);
	
	// 프로젝트 삭제 시 ProjectJoin 삭제
	public boolean deleteProjectJoin(ProjectDTO project);
	
	// 프로젝트 조회
	public ProjectDTO selectOneProject(ProjectDTO project);
	
	// 프로젝트 조회 (String형으로 Project_id하나만 받는경우)
	public ProjectDTO selectOneProject(int project_id);
	
	// 프로젝트 조회
	public ProjectDTO selectOneProjectToProjectJoin(ProjectJoinDTO join);
	
	// 가장 최신의 프로젝트 조회
	public ProjectDTO selectLatestProject();

	// 프로젝트 공개범위 수정
	public boolean updatePrivacyScope(int project_id, String privacy_scope);
	
	// default칸반 TO DO, DOING, DONE 칸반 생성
	public boolean createDefaultKanban(ProjectDTO project);
	
	// board의 kanban_id로 projectStatus테이블의 project_status 가져오기
	public KanbanDTO selectKanbanStatus(TaskDTO board);

	// 프로젝트 종료
	// 프로젝트 예상 종료시간 설정
	public PSProperties updateProjectExpectFinal(ProjectFinalExpectDateDTO finalExpectDateDTO);

	// 프로젝트 종료 시점 수정
	public PSProperties updateProjectFinal(int project_id);

	// 프로젝트 종료 취소
	public PSProperties cancelProjectFinal(int project_id);

	// 프로젝트 종료 저장
	public PSProperties completeProject();


	
	// task
	// task 종류(칸반) 불러오기
	public ArrayList<KanbanDTO> selectKanbanStatus(ProjectDTO project);
	
	// 새로운 칸반 생성
	public boolean createKanbanStatus(KanbanDTO status);
	
	// 칸반 삭제
	public boolean deleteKanbanStatus(KanbanDTO status);
	
	// board의 칸반 id 수정
	public boolean updateTaskKanbanID(UpdateKanbanDTO kanban);
	
	// 프로젝트 task(board) 불러오기 
	public ArrayList<TaskDTO> selectBoards(ProjectDTO project);
	
	// 프로젝트 칸반아이디별 task(board) 불러오기 
	public ArrayList<TaskDTO> selectKanbanTasks(KanbanDTO project);

	// 프로젝트 task(board)와 user_name 같이 불러오기
	public ArrayList<TaskJoinUserDTO> selectTaskJoinUsers(ProjectDTO project);


	public ArrayList<TaskJoinKanbanUserDTO> selectTaskJoinKanbanUser(int project_id);

	// 새로운 task(board) 생성
	public boolean createTask(TaskDTO board);
	
	// task 삭제
	public boolean deleteTask(TaskDTO board);
	
	// task 작업 내용 수정
	public boolean updateTask(TaskDTO board);
	
	// 유저별 작업 목록 가져오기 (board + boardstatus) 
	public ArrayList<TaskJoinKanban> selectTaskJoinKanban(UserDTO user);

	public ArrayList<DashboardTaskDTO> selectDashboardTask(String user_email);

	//차트
	//프로젝트별 유저리스트 불러오기
	public ArrayList<UserDTO> getuserlist(ProjectJoinDTO user);

	//프로젝트별 칸반아이디, 칸반상태 불러오기
	public ArrayList<KanbanDTO> getkanbanlist(KanbanDTO kanban);

	//프로젝트, 유저이메일별 칸반아이디 불러오기 / task수량체크용
	public ArrayList<Integer> gettasklist(TaskDTO task);
	
	
	// 프로젝트 룰
	// 프로젝트 룰 불러오기
	public ArrayList<ProjectRuleDTO> selectRule(ProjectDTO project);
	
	// 프로젝트 룰 생성
	public boolean createRule(ProjectRuleDTO rule);
	
	// 프로젝트 룰 삭제
	public boolean deleteRule(ProjectRuleDTO rule);


	// 댓글
	// 태스크별 댓글 불러오기
	public ArrayList<TaskCommentDTO> selectComment(int task_id);

	public ArrayList<TaskCommentDTO> selectCommentByProjectID(int task);
	
	// 댓글 입력
	public boolean createComment(TaskCommentDTO comment);
	
	// 댓글 삭제
	public boolean deleteComment(TaskCommentDTO comment);
	
	
	// 그룹
	// 그룹원인지 체크해서 제대로 가져오면 인증 null 값이면 되돌리기
	public ProjectJoinDTO selectGroupCheck(ProjectDTO project, UserDTO user);
	
	// 초대상태의 그룹원 목록 불러오기
	public ArrayList<ProjectJoinDTO> selectInviteGroup(UserDTO user);
	
	// 그룹원 목록 불러오기
	public ArrayList<ProjectJoinDTO> selectGroup(ProjectDTO project);
	
	// 프로젝트의 admin(관리자)인지 체크
	public ProjectJoinDTO selectGroupAdmin(ProjectDTO project, UserDTO user);
	
	// 그룹원 추가
	public boolean insertGroup(ProjectJoinDTO join); 
	
	// 그룹원 삭제
	public boolean deleteProjectUser(ProjectJoinDTO join);
	
	// 그룹원 요청 처리
	public boolean updateGroup(ProjectJoinDTO join);
	
	// 그룹원 역할명 변경 처리
	public boolean updateRole(ProjectJoinDTO join);

	// 디스코드 링크 추가
	public boolean addDiscordLink(DiscordLinkDTO discordLinkDTO);

	// 디스코드 링크 찾기
	public DiscordLinkDTO FindDiscordLinkByProjectID(int project_id);

	public ArrayList<ProjectJoinDiscordDTO> FindDiscordLinkByUserEmail(String user_email);





public boolean addTaskComment(TaskCommentDTO taskCommentDTO);
}
