package kz.kegoc.bln.interceptor;

import kz.kegoc.bln.producer.emcos.hour.EmcosHourMeteringDataRawProducer;
import kz.kegoc.bln.producer.file.FileMeteringDataRawProducer;

import javax.interceptor.AroundTimeout;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@ProducerMonitor
@Interceptor
public class ProducerInterceptor {
    private boolean fileProducer = true;
    private boolean emcosProducer = false;

    @AroundTimeout
    public Object monitor(InvocationContext ctx) throws Exception {

        Class<?> aClass = ctx.getTarget().getClass();
        boolean flag = false;
        if (aClass == FileMeteringDataRawProducer.class)
            flag = fileProducer;
        else if (aClass == EmcosHourMeteringDataRawProducer.class)
            flag = emcosProducer;

        if (flag) {
            System.out.println(aClass.getCanonicalName() + " start");
            ctx.proceed();
            System.out.println(aClass.getCanonicalName() + " finish");
        }
        else
            System.out.println(aClass.getCanonicalName() + " is disabled");

        return null;
    }
}
