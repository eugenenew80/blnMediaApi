package kz.kegoc.bln.ejb.interceptor;

import kz.kegoc.bln.exp.ftp.schedule.PeriodTimeValueExp;
import kz.kegoc.bln.imp.emcos.schedule.AutoAtTimeValueImp;
import kz.kegoc.bln.imp.emcos.schedule.AutoPeriodTimeValueImp;
import kz.kegoc.bln.imp.emcos.schedule.ManualPeriodTimeValueImp;
import kz.kegoc.bln.imp.oic.schedule.AutoOicDataImp;

import javax.interceptor.AroundTimeout;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@ProducerMonitor
@Interceptor
public class ProducerInterceptor {
    private boolean autoAtTimeValueImp = true;
    private boolean autoPeriodTimeValueImp = true;
    private boolean manualAtTimeValueImp = false;
    private boolean manualPeriodTimeValueImp = false;
    private boolean periodTimeValueExp = false;
    private boolean autoOicDataImp = false;

    @AroundTimeout
    public Object monitor(InvocationContext ctx) throws Exception {
        Class<?> aClass = ctx.getTarget().getClass();
        boolean flag = true;

        if (aClass == AutoAtTimeValueImp.class)
            flag = autoAtTimeValueImp;
        
        if (aClass == AutoPeriodTimeValueImp.class)
            flag = autoPeriodTimeValueImp;

        if (aClass == ManualPeriodTimeValueImp.class)
            flag = manualAtTimeValueImp;

        if (aClass == ManualPeriodTimeValueImp.class)
            flag = manualPeriodTimeValueImp;

        if (aClass == PeriodTimeValueExp.class)
            flag = periodTimeValueExp;

        if (aClass == AutoOicDataImp.class)
            flag = autoOicDataImp;

        if (flag) return ctx.proceed();
        return null;
    }
}
