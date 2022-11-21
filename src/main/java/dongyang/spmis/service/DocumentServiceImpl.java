package dongyang.spmis.service;

import dongyang.spmis.mapper.DocumentMapper;
import dongyang.spmis.model.Project.ProjectDTO;
import dongyang.spmis.model.Project.ProjectJoinDTO;
import dongyang.spmis.model.User.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;


@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    DocumentMapper documentMapper;

    public DocumentServiceImpl(DocumentMapper documentMapper) {
        this.documentMapper = documentMapper;
    }

    //출력할 프로젝트 선택하기
    @Override
    public ArrayList<ProjectDTO> select(UserDTO user) {
        ProjectJoinDTO join = new ProjectJoinDTO();
        join.setUser_email(user.getUser_email());

        ArrayList<ProjectDTO> projectlist = documentMapper.select(join);

        return projectlist;
    }


}
