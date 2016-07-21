package org.wimi.jetty.tutorial;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletException;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.wimi.jetty.tutorial.internal.SimpleServlet;

@Component(name = "HttpServiceComponent")
public class HttpServiceServiceComponent
{
	private HttpService httpService;

	@Reference(name = "ConfigurationAdmin", policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, unbind = "unbindConfigAdmin")
	protected void bindConfigAdmin(ConfigurationAdmin configAdmin)
	{
		try
		{
			// Configuration[] listConfigurations = configAdmin.listConfigurations(null);
			// for (Configuration configuration : listConfigurations)
			// {
			// Dictionary<String, Object> properties = configuration.getProperties();
			// System.out.println(properties);
			//
			// }

			Configuration configBla = configAdmin.getConfiguration("bla.blub", null);
			Configuration configFelix = configAdmin.getConfiguration("org.apache.felix.http", null);
			Configuration configJetty = configAdmin.getConfiguration("org.eclipse.equinox.http.jetty.config", null);
			Configuration configHttpServlet = configAdmin.getConfiguration("org.eclipse.equinox.http.servlet", null);

			Dictionary<String, Object> props = configHttpServlet.getProperties();
			if (props == null)
			{
				props = new Hashtable<String, Object>();
				props.put("service.pid", "org.eclipse.equinox.http.servlet");
			}
			props.put("org.osgi.service.http.port", 8089);
			props.put("http.port", 8089);
			configHttpServlet.update(props);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void unbindConfigAdmin(ConfigurationAdmin configAdmin)
	{

	}

	@Reference(name = "SimpleHttpServlet", policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL, unbind = "unbindHttpService")
	protected void bindHttpService(HttpService service)
	{
		httpService = service;

		try
		{
			// Hashtable<String, String> ht = new Hashtable<>();
			// ht.put("org.osgi.service.http.port", "8081");
			// httpService.registerServlet("/simple", new SimpleServlet(), ht, null);
			httpService.registerServlet("/simple", new SimpleServlet(), null, null);

		}
		catch (ServletException e)
		{
			e.printStackTrace();
		}
		catch (NamespaceException e)
		{
			e.printStackTrace();
		}
	}

	protected void unbindHttpService(HttpService service)
	{
		httpService.unregister("/simple");
	}

	@Activate
	protected void activate()
	{
		System.out.println("activate");
	}

	@Deactivate
	protected void deactivate()
	{
		System.out.println("deactivate");
	}

	@Modified
	protected void modify(ComponentContext context)
	{
		System.out.println("modify");
		Dictionary<String, Object> dic = context.getProperties();
		if (dic == null)
		{
			System.out.println("dic is null");
		}
		else
		{
			Enumeration<String> e = dic.keys();
			while (e.hasMoreElements())
			{
				String k = e.nextElement();
				System.out.println(k + ": " + dic.get(k));
			}
		}
	}
}
