package dongyang.spmis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

public interface CloudinaryMapper extends DefaultDBInfo{

    @Update("Update " + USER + " SET profile = #{url} WHERE user_emil=#{user_email}")
    boolean saveGifToDB(String user_email, String url);
}
