package zhwy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="/api")
@Api(value = "/api",tags = {"api测试"})
public class demo {
    @GetMapping("/get")
    @ApiOperation(value="测试")
    public String demo(){

        return "11";
    }

    @ApiOperation(value = "查询用户",notes="查询用户")
    @GetMapping("/queryUserById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户编号", required = true)
            //可以添加多个@ApiImplicitParam
    })
    public String  queryUserById(@RequestParam("id") Integer id){
        return "小明";
    }
}
