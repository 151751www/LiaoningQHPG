package zhwy.Task;

/*@Component
@EnableScheduling
@EnableAsync*/
class MultithreadScheduleTask {

  /*  @Autowired
    private GeneralDaoImpl generalDao;

    @Async
    @PostConstruct
    @Scheduled(cron = "0 0 12 1 * ?") //每月1号12点定时执行更新上个月的月平均降水量和10分钟风速
    public void first() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
        date = calendar.getTime();
        String accDate = format.format(date);

        String updatePre="update surf_aws_month_data  set pre_month =(select val from preMonth p  where p.tim='"+accDate+"-01'and  " +
                "p.snum=surf_aws_month_data.station_num and p.tim=surf_aws_month_data.observe_date)" +
                "where EXISTS(select 1 from preMonth p where p.tim='"+accDate+"-01'and p.snum=surf_aws_month_data.station_num and " +
                "p.tim=surf_aws_month_data.observe_date)";
        String updateFS="update surf_aws_month_data  set win_s_avg_month =(select val from FSMonth p   where p.tim='"+accDate+"-01'and  " +
                "p.station_num=surf_aws_month_data.station_num and p.tim=surf_aws_month_data.observe_date)" +
                "where EXISTS(select 1 from FSMonth p where p.tim='"+accDate+"-01'and p.station_num=surf_aws_month_data.station_num and " +
                "p.tim=surf_aws_month_data.observe_date)";
        try {
            generalDao.excuSql(updatePre);
            generalDao.excuSql(updateFS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Async
    @PostConstruct
    @Scheduled(cron = "0 0 12 1 1 ? *")
    public void second() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1); // 设置为上一年
        date = calendar.getTime();
        String accDate = format.format(date);
        String updatePre="update surf_aws_year_data  set pre_year =(select val from preyear p  where p.tim='"+accDate+"-01-01'and  " +
                "p.snum=surf_aws_year_data.station_num and p.tim=surf_aws_year_data.observe_date)" +
                "where EXISTS(select 1 from preyear p where p.tim='"+accDate+"-01-01'and p.snum=surf_aws_year_data.station_num and " +
                "p.tim=surf_aws_year_data.observe_date)";
        String updateFS="update surf_aws_year_data  set win_s_10_year =(select val from FSYear p   where p.tim='"+accDate+"-01-01'and  " +
                "p.station_num=surf_aws_year_data.station_num and p.tim=surf_aws_year_data.observe_date)" +
                "where EXISTS(select 1 from FSYear p where p.tim='"+accDate+"-01-01'and p.station_num=surf_aws_year_data.station_num and " +
                "p.tim=surf_aws_year_data.observe_date)";
        try {
            generalDao.excuSql(updatePre);
            generalDao.excuSql(updateFS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
}