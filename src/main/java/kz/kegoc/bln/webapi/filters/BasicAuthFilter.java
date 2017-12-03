package kz.kegoc.bln.webapi.filters;

import java.io.IOException;
import java.security.Principal;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import kz.kegoc.bln.entity.adm.User;
import kz.kegoc.bln.webapi.common.CustomPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMapCache;
import org.apache.commons.codec.binary.Base64;

@Provider
@PreMatching
public class BasicAuthFilter implements ContainerRequestFilter {

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
		String userName = auth[0];
		String password = auth[1];

		if (StringUtils.isEmpty(userName))
			throw new NotAuthorizedException("EMPTY USER");

		String hash = Base64.encodeBase64String((userName + ":" + password).getBytes());
		User user = sessions.get(hash);
		if (user==null)
			throw new NotAuthorizedException("USER IS NOT REGISTERED");

		sessions.remove(userName);
		sessions.put(userName, user,30, TimeUnit.MINUTES);

		ctx.setSecurityContext(
			new SecurityContext() {
				@Override
				public boolean isUserInRole(String role) {
					return true;
				}

				@Override
				public boolean isSecure() {
					return false;
				}

				@Override
				public Principal getUserPrincipal() {
					return new CustomPrincipal(userName, user);
				}

				@Override
				public String getAuthenticationScheme() {
					return null;
				}
			}
		);
	}

	@Inject
	private RMapCache<String, User> sessions;
}
