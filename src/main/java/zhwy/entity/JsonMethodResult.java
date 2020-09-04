package zhwy.entity;

import zhwy.datatable.DataTable;

public class JsonMethodResult {

    private String Message ;
    private String FileName ;
    private String TimeDescription ;
    public DataTable FileData ;
    public DataTable AllData ;
    private String DataDescription ;

    public DataTable getFileData() {
        return FileData;
    }

    public void setFileData(DataTable fileData) {
        FileData = fileData;
    }

    public DataTable getAllData() {
        return AllData;
    }

    public void setAllData(DataTable allData) {
        AllData = allData;
    }

    private DataTable ColumnName ;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getTimeDescription() {
        return TimeDescription;
    }

    public void setTimeDescription(String timeDescription) {
        TimeDescription = timeDescription;
    }

    public String getDataDescription() {
        return DataDescription;
    }

    public void setDataDescription(String dataDescription) {
        DataDescription = dataDescription;
    }

    public DataTable getColumnName() {
        return ColumnName;
    }

    public void setColumnName(DataTable columnName) {
        ColumnName = columnName;
    }
}
