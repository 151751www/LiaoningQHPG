package zhwy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GeneralDaoImpl{
	private static Logger logger = LoggerFactory.getLogger(GeneralDaoImpl.class);

	@Autowired
	private JdbcTemplate nrietjdbcTemplate;
	
	
	public JSONArray getDataBySql(String sql, String[] keys) throws Exception {
		JSONArray array = new JSONArray();
		try {
			SqlRowSet rs =  nrietjdbcTemplate.queryForRowSet(sql);
			JSONObject obj;
			while(rs.next()){
				obj = new JSONObject(true);
				for(int i=0;i<keys.length;i++){
					if(rs.getObject(keys[i])==null){
						obj.put(keys[i], "");
					}else{
						obj.put(keys[i], rs.getObject(keys[i]));
					}
				}
				array.add(obj);
			}
		} catch (Exception e) {
			logger.error("GeneralDaoImplImpl--getDataBySql--遇到问题:" + e);
			e.printStackTrace();
		}
		return array;
	}
	public int updateSql(String sql,Object [] objects)throws Exception{
		int num=0;
		try {
			num=nrietjdbcTemplate.update(sql,objects);
		}catch (Exception e ){
			logger.error("GeneralDaoImplImpl--updateSql--遇到问题:" + e);
			e.printStackTrace();
		}
		return num;
	}
	public Map<String,Object> getDataMap(String sql)throws Exception{
		Map<String,Object> result=new HashMap<String,Object>();
		try {
			result=nrietjdbcTemplate.queryForMap(sql);
		}catch (Exception e ){
			logger.error("GeneralDaoImplImpl--getDataMap--遇到问题:" + e);
			e.printStackTrace();
		}
		return result;
	}

	public List<Map<String,Object>> getDataList(String sql)throws Exception{
		List<Map<String,Object>> result=new ArrayList<Map<String, Object>>();
		try {
			result=nrietjdbcTemplate.queryForList(sql);
		}catch (Exception e ){
			logger.error("GeneralDaoImplImpl--getDataMap--遇到问题:" + e);
			e.printStackTrace();
		}
		return result;
	}
}
