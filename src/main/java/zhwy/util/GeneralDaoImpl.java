package zhwy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
					obj.put(keys[i], rs.getObject(keys[i]));										
				}
				array.add(obj);
			}
		} catch (Exception e) {
			logger.error("GeneralDaoImplImpl--getDataBySql--遇到问题:" + e);
		}
		return array;
	}
}
