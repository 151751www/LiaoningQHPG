package zhwy.datatable;

/**
 * 数据列
 */
public class DataColumn
{
	/**
	 * 列名
	 */
	private String columnName;
	/**
	 * 数据类型
	 */
	private Class<?> dataType;
	/**
	 * 默认值
	 */
	private Object defaultValue;
	/**
	 * 数据列描述
	 */
	private String description;
	/**
	 * 数据表
	 */
	private DataTable table;
	/**
	 * 数据列标题
	 */
	private String Caption;

	/**
	 * 构造器定义 +2重载
	 */
	public DataColumn()
	{
	}
	/**
	 * 构造器定义
	 * 
	 * @param _ColumnName
	 *            列名
	 */
	public DataColumn(String _ColumnName)
	{
		columnName = _ColumnName.toUpperCase();
	}
	/**
	 * 构造器定义
	 * 
	 * @param _ColumnName
	 *            列名
	 * @param _Caption 标题
	 */
	public DataColumn(String _ColumnName,String _Caption)
	{
		columnName = _ColumnName.toUpperCase();
		Caption = _Caption;
	}
	/**
	 * 构造器定义
	 * 
	 * @param _ColumnName
	 *            列名
	 * @param _DataType
	 *            类型
	 */
	public DataColumn(String _ColumnName, Class<?> _DataType)
	{
		this(_ColumnName);
		dataType = _DataType;
	}
	/**
	 * 属性定义:ColumnName
	 * 
	 * @return 列名
	 */
	public String getColumnName()
	{
		return columnName;
	}
	/**
	 * 设置列名
	 * 
	 * @param _ColumnName
	 *            列名
	 */
	public void setColumnName(String _ColumnName)
	{
		columnName = _ColumnName;
	}
	/**
	 * 属性定义:DataType
	 * 
	 * @return 数据类型
	 */
	public Class<?> getDataType()
	{
		return dataType;
	}
	/**
	 * 设置数据类型
	 * 
	 * @param _DataType
	 *            数据类型
	 */
	public void setDataType(Class<?> _DataType)
	{
		dataType = _DataType;
	}
	/**
	 * 属性定义:DefaultValue
	 * 
	 * @return 默认值
	 */
	public Object getDefaultValue()
	{
		return defaultValue;
	}
	/**
	 * 设置 默认值
	 * 
	 * @param _DefaultValue
	 *            默认值
	 */
	public void setDefaultValue(Object _DefaultValue)
	{
		defaultValue = _DefaultValue;
	}
	/**
	 * 属性定义:Description
	 * 
	 * @return 描述
	 */
	public String getDescription()
	{
		return description;
	}
	/**
	 * 设置 描述
	 * 
	 * @param _Description
	 *            描述
	 */
	public void setDescription(String _Description)
	{
		description = _Description;
	}
	/**
	 * 属性定义:DataTable
	 * 
	 * @return DataTable
	 */
	public DataTable getTable()
	{
		return table;
	}
	/**
	 * 设置 DataTable
	 * 
	 * @param _Table
	 *            DataTable
	 */
	public void setTable(DataTable _Table)
	{
		this.table = _Table;
	}
	/**
	 * 属性定义:Ordinal
	 * 
	 * @return Ordinal
	 */
	public int getOrdinal()
	{
		if (this.table == null) return -1;
		return this.table.getColumns().IndexOf(this);
	}
	/**
	 * 设置Caption
	 * 
	 * @param _Caption
	 */
	public void setCaption(String _Caption)
	{
		Caption = _Caption;
	}
	/**
	 * 属性定义:Caption
	 * 
	 * @return Caption
	 */
	public String getCaption()
	{
		return Caption;
	}
}
