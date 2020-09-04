package zhwy.entity;


import java.util.Date;

public class ScheduleJob {
    private static final long serialVersionUID = 1L;

    private String jobId;

    private String jobName;

    private String jobGroup;


    private String jobStatus;
//启动间隔时间
    private String cronExpression;

    private String desc;

    private String interfaceName;
    private Date starttime;

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public String getJobId() {
        return jobId;
    }
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    public String getJobName() {
        return jobName;
    }
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobStatus() {
        return jobStatus;
    }
    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }
    public String getCronExpression() {
        return cronExpression;
    }
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getInterfaceName() {
        return interfaceName;
    }
    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }
    @Override
    public String toString() {
        return "ScheduleJob [jobId=" + jobId + ", jobName=" + jobName + ", jobStatus="
                + jobStatus + ", cronExpression=" + cronExpression + ", desc=" + desc + ", interfaceName="
                + interfaceName + "]";
    }
}
