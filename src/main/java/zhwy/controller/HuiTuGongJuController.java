package zhwy.controller;


import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zhwy.service.HuiTuService;
import zhwy.util.Common;

import java.net.URLDecoder;

@Api(position = 8,tags = "绘图工具")
@RestController
@RequestMapping("/huiTu")
public class HuiTuGongJuController {
	private static Logger logger = LoggerFactory.getLogger(HuiTuGongJuController.class);

	@Autowired
	HuiTuService huiTu;
	@Autowired
	private Common common;


	@PostMapping("/makePic")
	@ApiOperation(value = "分布图")
	@ApiImplicitParams({
			@ApiImplicitParam(name="latLonStr",value="站点信息与相关要素的值:[{'stationName':'','lat':'','lon':'','value':''},{},{}]",required=true,paramType="query",dataType="String"),
			@ApiImplicitParam(name="colorStr",value="颜色:[{\"colorMinValues\":\"0\",\"colorMaxValues\":\"18.5\",\"colorValues\":\"228,95,18\"},{\"colorMinValues\":\"18.6\",\"colorMaxValues\":\"19\",\"colorValues\":\"132,255,0\"},{\"colorMinValues\":\"19.1\",\"colorMaxValues\":\"19.5\",\"colorValues\":\"62,194,191\"},{\"colorMinValues\":\"19.6\",\"colorMaxValues\":\"21.5\",\"colorValues\":\"213,44,205\"},{},{}]",required=true,paramType="query",dataType="String"),
			@ApiImplicitParam(name="area",value="地区的经纬度范围:117.9677|126.6426|37.0619|45.2370....",required=true,paramType="query",dataType="String")
	})
	//latLonStr：站名:stationName   经度:lon   纬度:lat  要素的值:value
	public String makePic(String latLonStr, String colorStr, String area) throws Exception {

		//跨域
		common.getCrossOrigin();
		String picName;
		String[] arr= area.split("\\|");
		int length=arr.length;
		colorStr= URLDecoder.decode(colorStr,"utf-8");
		latLonStr=URLDecoder.decode(latLonStr,"utf-8");
		JSONArray json ;
		try {
			if(length!=4){
				picName="经纬度范围不合理，请输入正确的经纬度范围，如：117.9677|126.6426|37.0619|45.2370";
			}else{
				 json= (JSONArray) JSONArray.parse(latLonStr);
				 picName = huiTu.makePic(json,colorStr,area);
			}
			return   picName;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("生成图片失败："+e.getMessage());
			return "生成图片失败："+e.getMessage();
		}
	}
}
