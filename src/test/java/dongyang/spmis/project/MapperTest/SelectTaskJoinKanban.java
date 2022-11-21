package dongyang.spmis.project.MapperTest;

import dongyang.spmis.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SelectTaskJoinKanban {

    @Autowired
    private ProjectMapper projectMapper;

//    @Test
//    void FindTaskAndKanban(){
//        UserDTO user = new UserDTO();
//        user.setUser_email("test@gmail.com");
////        Assertions.assertThat(projectMapper.selectTaskJoinKanban(user)).isT;
//
//    }

}
