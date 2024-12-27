package com.gl.ceir.panel.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class CustomContainerConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

	@Override
	public void customize(TomcatServletWebServerFactory factory) {
		/*final RewriteValve valve = new RewriteValve() {

            @Override
            protected synchronized void startInternal() throws LifecycleException {
                super.startInternal();

                try {
                    InputStream resource = new ClassPathResource("rewrite.config").getInputStream();

                    InputStreamReader resourceReader = new InputStreamReader(resource);
                    BufferedReader buffer = new BufferedReader(resourceReader);

                    parse(buffer);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        valve.setEnabled(true);

        factory.addContextValves(valve);*/
	}

}
