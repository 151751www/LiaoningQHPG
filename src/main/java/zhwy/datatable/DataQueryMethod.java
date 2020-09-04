package zhwy.datatable;

public class DataQueryMethod {
	/// <summary>
    /// 查询表通用函数
    /// </summary>
    /// <param name="beginDate">开始时间</param>
    /// <param name="endDate">结束时间</param>
    /// <param name="tableName">表名</param>
    /// <param name="arrColName">查询列名（中文）</param>
    /// <param name="arrColValue">查询列名（英文）</param>
    /// <param name="method">统计方式</param>
    /// <param name="function">运算方式</param>
    /// <param name="limitStation">限制站点</param>
    /// <returns></returns>
    public static String BuildQuerySQL(String beginDate, String endDate, String tableName, String[] arrColName, String[] arrColValue, String method, String function, String limitStation)
    {
        //构建运算符
        String part1 = "", part2 = "";
        if(function=="求平均")
        {
        	part1="Avg(";
        }
        if(function=="求最大")
        {
        	part1 = "Max(";
        }
        if(function=="求最小")
        {
            part1 = "Min(";
        }
        if(function=="求和")
        {
            part1 = "Sum(";
        }
        if (part1 != "")
        {
            part2 = ")";
        }
        //构建查询要素
        String sqlFields = "";
        for (int i = 0; i < arrColName.length; i++)
        {
            sqlFields += part1 + arrColValue[i] + part2 + " as " + arrColName[i] + ",";
        }
        sqlFields = sqlFields.substring(0,sqlFields.length()-1);

        //构建SQL
        String sql = "";
        limitStation = limitStation.replace("|", "','");
        if (method == "时段统计")
        {
            sql = "seelct " + sqlFields + " from " + tableName + " where 站号 in ('" + limitStation + "') and 观测时间>='" + beginDate + "' and 观测时间<='" + endDate + "'";

            if (function != "原始值")
            {
                sql += "group by 站号 ";
            }

            sql += "order by 站号 desc,时间 desc";
        }
        else if (method == "历年统计")
        {

        }

        return sql;

    }
}
