package kz.kegoc.bln.ejb.interceptor;

import kz.kegoc.bln.exp.ftp.schedule.PeriodTimeValueExp;
import kz.kegoc.bln.imp.emcos.schedule.AutoAtTimeValueImp;
import kz.kegoc.bln.imp.emcos.schedule.AutoPeriodTimeValueImp;
import kz.kegoc.bln.imp.emcos.schedule.ManualPeriodTimeValueImp;

import javax.interceptor.AroundTimeout;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@ProducerMonitor
@Interceptor
public class ProducerInterceptor {
    private boolean autoAtTimeValueImp = true;
    private boolean autoPeriodTimeValueImp = true;
    private boolean manualAtTimeValueImp = true;
    private boolean manualPeriodTimeValueImp = true;
    private boolean periodTimeValueExp = true;

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

        if (flag) return ctx.proceed();
        return null;
    }
}
