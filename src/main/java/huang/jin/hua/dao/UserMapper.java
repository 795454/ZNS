package huang.jin.hua.dao;


import huang.jin.hua.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    User getUserByUsername(String username);

}
