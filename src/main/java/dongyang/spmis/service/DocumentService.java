package dongyang.spmis.service;

import dongyang.spmis.model.Project.ProjectDTO;
import dongyang.spmis.model.User.UserDTO;

import java.util.ArrayList;

public interface DocumentService {

    //출력할 프로젝트 선택하기
    public ArrayList<ProjectDTO> select(UserDTO user);
}
