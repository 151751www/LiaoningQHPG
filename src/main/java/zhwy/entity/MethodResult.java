package zhwy.entity;

import zhwy.datatable.DataTable;

public class MethodResult {
    private String Message ;
    private String MethodName ;
    private String TimeDescription ;
    private int TimeYear ;
    private int TimeHour ;
    private String DataDescription ;
    private String ResultValue ;
    private DataTable FileData;
    private DataTable ColumnName;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMethodName() {
        return MethodName;
    }

    public void setMethodName(String methodName) {
        MethodName = methodName;
    }

    public String getTimeDescription() {
        return TimeDescription;
    }

    public void setTimeDescription(String timeDescription) {
        TimeDescription = timeDescription;
    }

    public int getTimeYear() {
        return TimeYear;
    }

    public void setTimeYear(int timeYear) {
        TimeYear = timeYear;
    }

    public int getTimeHour() {
        return TimeHour;
    }

    public void setTimeHour(int timeHour) {
        TimeHour = timeHour;
    }

    public String getDataDescription() {
        return DataDescription;
    }

    public void setDataDescription(String dataDescription) {
        DataDescription = dataDescription;
    }

    public String getResultValue() {
        return ResultValue;
    }

    public void setResultValue(String resultValue) {
        ResultValue = resultValue;
    }

    public DataTable getFileData() {
        return FileData;
    }

    public void setFileData(DataTable fileData) {
        FileData = fileData;
    }

    public DataTable getColumnName() {
        return ColumnName;
    }

    public void setColumnName(DataTable columnName) {
        ColumnName = columnName;
    }
}
