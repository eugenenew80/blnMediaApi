package kz.kegoc.bln.ejb.interceptor;

import kz.kegoc.bln.producer.emcos.EmcosMeteringDataProducer;
import kz.kegoc.bln.producer.file.FileMeteringDataProducer;
import javax.interceptor.AroundTimeout;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@ProducerMonitor
@Interceptor
public class ProducerInterceptor {
    private boolean fileProducer = true;
    private boolean emcosProducer = true;

    @AroundTimeout
    public Object monitor(InvocationContext ctx) throws Exception {

        Class<?> aClass = ctx.getTarget().getClass();
        boolean flag = true;
        if (aClass == FileMeteringDataProducer.class)
            flag = fileProducer;
        else if (aClass == EmcosMeteringDataProducer.class)
            flag = emcosProducer;
        
        if (flag) return ctx.proceed();
        return null;
    }
}
