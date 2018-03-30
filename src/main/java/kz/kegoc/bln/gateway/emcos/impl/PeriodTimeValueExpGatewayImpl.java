package kz.kegoc.bln.gateway.emcos.impl;

import kz.kegoc.bln.entity.media.ConnectionConfig;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.gateway.emcos.PeriodTimeValueExpGateway;
import kz.kegoc.bln.registry.TemplateRegistry;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class PeriodTimeValueExpGatewayImpl implements PeriodTimeValueExpGateway {
    private static final Logger logger = LoggerFactory.getLogger(PeriodTimeValueExpGatewayImpl.class);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private List<MeteringPointCfg> points;
    private ConnectionConfig config;

    public PeriodTimeValueExpGateway points(List<MeteringPointCfg> points) {
        this.points = points;
        return this;
    }

    public PeriodTimeValueExpGateway config(ConnectionConfig config) {
        this.config = config;
        return this;
    }

    public void send() throws Exception {
        logger.info("send started");

        if (config ==null) {
            logger.warn("Config is empty, send stopped");
            return;
        }

        if (points ==null || points.isEmpty()) {
            logger.warn("List of points is empty, send stopped");
            return;
        }

        try {
            logger.info("Send http request for metering media...");
            String body = buildBody();
            if (StringUtils.isEmpty(body)) {
            	logger.info("Request body is empty, send stopped");
                return;
            }

            String answer = new HttpGatewayImpl.Builder()
                .url(new URL(config.getUrl()))
                .method("POST")
                .body(body)
                .build()
                .doRequest();

            logger.trace(answer);

            try (PrintWriter out = new PrintWriter("answer.xml")) {
                out.println(answer);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            logger.info("send competed");
        }

        catch (Exception e) {
            logger.error("send failed: " + e.toString());
            throw e;
        }
    }

    private String buildBody() {
    	logger.debug("buildBody started");

    	String strPoints = points.stream()
            .map( p-> buildPoint(p))
            .filter(p -> StringUtils.isNotEmpty(p))
            .collect(Collectors.joining());
        logger.trace("points: " + strPoints);

        if (StringUtils.isEmpty(strPoints)) {
        	logger.debug("List of points is empty, buildBody stopped");
            return "";
        }

        String data = templateRegistry.getTemplate("EMCOS_EEML_DATA")
        	.replace("#points#", strPoints);
        logger.trace("media: " + data);

        String property = templateRegistry.getTemplate("EMCOS_EEML_PROPERTY")
        	.replace("#user#", config.getUserName())
        	.replace("#isPacked#", "false")
        	.replace("#func#", "EEML")
        	.replace("#attType#", "1");
        logger.trace("property: " + property);

        String body = templateRegistry.getTemplate("EMCOS_EEML_BODY")
        	.replace("#property#", property)
        	.replace("#media#", Base64.encodeBase64String(data.getBytes()));
        logger.trace("body for request metering media: " + body);


        try (PrintWriter out = new PrintWriter("body.xml")) {
            out.println(body);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try (PrintWriter out = new PrintWriter("data.xml")) {
            out.println(data);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        logger.debug("buildBody completed");
        return body;
    }

    private String buildPoint(MeteringPointCfg point) {
        logger.info("buildPoint started");

        String str = ""
                + "<ROW PDA=\"" + point.getStartTime().toLocalDate().format(dateFormatter) + "\" "
                + "PBT=\"" + point.getStartTime().format(timeFormatter) + "\" "
                + "PET=\"" + point.getEndTime().format(timeFormatter) + "\" "
                + "PCODE=\"" + point.getSourceMeteringPointCode() + "\" "
                + "PML_ID=\"" + point.getSourceParamCode() + "\" "
                + "PVAL=\"" +  point.getVal() + "\" />";

        return str;
    }


    @Inject
    private TemplateRegistry templateRegistry;
}
