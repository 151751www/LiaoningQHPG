package zhwy.entity;

import zhwy.datatable.DataTable;

public class JsonLiShiShuJuXuLieYanChang {
    private String Message ;
    private DataTable dtResultData ;
    private DataTable ColumnName ;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public DataTable getDtResultData() {
        return dtResultData;
    }

    public void setDtResultData(DataTable dtResultData) {
        this.dtResultData = dtResultData;
    }

    public DataTable getColumnName() {
        return ColumnName;
    }

    public void setColumnName(DataTable columnName) {
        ColumnName = columnName;
    }
}
