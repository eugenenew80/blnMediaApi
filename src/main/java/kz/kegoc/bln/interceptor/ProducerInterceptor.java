package kz.kegoc.bln.interceptor;

import kz.kegoc.bln.producer.emcos.day.EmcosDayMeteringBalanceRawProducer;
import kz.kegoc.bln.producer.emcos.hour.EmcosHourMeteringDataRawProducer;
import kz.kegoc.bln.producer.file.FileMeteringDataRawProducer;

import javax.interceptor.AroundTimeout;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@ProducerMonitor
@Interceptor
public class ProducerInterceptor {
    private boolean fileProducer = false;
    private boolean emcosHourDataProducer = true;
    private boolean emcosDayBalanceProducer = true;

    @AroundTimeout
    public Object monitor(InvocationContext ctx) throws Exception {

        Class<?> aClass = ctx.getTarget().getClass();
        boolean flag = true;
        if (aClass == FileMeteringDataRawProducer.class)
            flag = fileProducer;
        else if (aClass == EmcosHourMeteringDataRawProducer.class)
            flag = emcosHourDataProducer;
        else if (aClass == EmcosDayMeteringBalanceRawProducer.class)
            flag = emcosDayBalanceProducer;
        
        if (flag)
            ctx.proceed();

        return null;
    }
}
