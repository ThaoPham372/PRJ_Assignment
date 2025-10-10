package vn.fpt.prj301.teamx.gym;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import util.ConfigUtil;

@WebListener
public class AppInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        String clientId = ConfigUtil.get("google.client.id");
        if (clientId != null && !clientId.isEmpty()) {
            ctx.setAttribute("googleClientId", clientId);
        }
    }
}


