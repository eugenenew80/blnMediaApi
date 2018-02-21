package kz.kegoc.bln.ejb.interceptor;

import kz.kegoc.bln.imp.emcos.schefule.AutoAtTimeValueImp;

import javax.interceptor.AroundTimeout;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@ProducerMonitor
@Interceptor
public class ProducerInterceptor {
    private boolean emcosProducer = true;

    @AroundTimeout
    public Object monitor(InvocationContext ctx) throws Exception {

        Class<?> aClass = ctx.getTarget().getClass();
        boolean flag = true;
        if (aClass == AutoAtTimeValueImp.class)
            flag = emcosProducer;
        
        if (flag) return ctx.proceed();
        return null;
    }
}
