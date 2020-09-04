package zhwy.dao;

public interface UserInfoDao {
    public String  addUser(String userName,String passWord,String relName,String mobile,String phone) throws Exception;

    public String updateUser(String userName, String passWord, String relName, String mobile, String phone) throws Exception;
    public String selectUser(String userName,String[] arr)throws Exception;
    public String delUser(String userName)throws Exception;
}
