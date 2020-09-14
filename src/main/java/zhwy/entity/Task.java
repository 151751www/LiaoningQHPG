package zhwy.entity;

public class Task {
    private String name;
    private String state;
    private String lastTime;
    private String nextTime;
    private String planFre;
    private String beginTime;
    private String stopTime;
    private String dataTime;
    private String exec;
    private String isstart;
    private String isdisable;

    public Task() {
    }

    public Task(String name, String state, String lastTime, String nextTime, String planFre, String beginTime,
                String stopTime, String dataTime, String exec, String isstart, String isdisable) {
        this.name = name;
        this.state = state;
        this.lastTime = lastTime;
        this.nextTime = nextTime;
        this.planFre = planFre;
        this.beginTime = beginTime;
        this.stopTime = stopTime;
        this.dataTime = dataTime;
        this.exec = exec;
        this.isstart = isstart;
        this.isdisable = isdisable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getNextTime() {
        return nextTime;
    }

    public void setNextTime(String nextTime) {
        this.nextTime = nextTime;
    }

    public String getPlanFre() {
        return planFre;
    }

    public void setPlanFre(String planFre) {
        this.planFre = planFre;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getExec() {
        return exec;
    }

    public void setExec(String exec) {
        this.exec = exec;
    }

    public String getIsstart() {
        return isstart;
    }

    public void setIsstart(String isstart) {
        this.isstart = isstart;
    }

    public String getIsdisable() {
        return isdisable;
    }

    public void setIsdisable(String isdisable) {
        this.isdisable = isdisable;
    }
}
