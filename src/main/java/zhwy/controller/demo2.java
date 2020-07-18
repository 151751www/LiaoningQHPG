package zhwy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/demo2")
@Api(value = "/demo2" ,tags = "接口2")
public class demo2 {
    @GetMapping(value = "/getName")
    @ApiOperation(value = "获取名称",notes = "名称")//value  接口名称   notes 接口描述
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户编号", required = true)//name  参数名称  value 参数说明  required 参数是否必须
            //可以添加多个@ApiImplicitParam
    })
    public String getName(@RequestParam("id") Integer id){
        return "小名";
    }

}
