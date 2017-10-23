package kz.kegoc.bln.webapi.filters;

import java.io.IOException;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import org.apache.commons.codec.binary.Base64;

@Provider
@PreMatching
public class BasicAuthentificationFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext ctx) throws IOException {		
		if (ctx.getUriInfo().getPath().contains("auth"))
			return;
				
		if (StringUtils.isEmpty(ctx.getHeaderString("Authorization")))
			throw new NotAuthorizedException("NOT AUTHORIZED");
	
		if (!ctx.getHeaderString("Authorization").startsWith("Basic "))
			throw new NotAuthorizedException("BASIC AUTHORIZATIN ONLY");

		String authHeader=  ctx.getHeaderString("Authorization").substring(6);				
		String[] auth = new String(Base64.decodeBase64(authHeader), "UTF-8").split(":");
		String user = auth[0];
		
		if (StringUtils.isEmpty(user))
			throw new NotAuthorizedException("EMPTY USER");
		
		JedisPool pool = null;
		try {
			pool = new JedisPool(new JedisPoolConfig(), "localhost");
			Jedis jedis = pool.getResource();
			
			String storedAuthHeader = jedis.get(user);
			if (StringUtils.isEmpty(storedAuthHeader))
				throw new NotAuthorizedException("USER IS NOT REGISTERED");
			
			if (!authHeader.equals(storedAuthHeader))
				throw new NotAuthorizedException("USER IS NOT REGISTERED");
			
			jedis.expire(user, 300);
		}
		finally {
			pool.close();
			pool.destroy();		
		}
	}
}
