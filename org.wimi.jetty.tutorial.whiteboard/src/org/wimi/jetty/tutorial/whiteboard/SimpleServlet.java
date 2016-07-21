package org.wimi.jetty.tutorial.whiteboard;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

@Component(name = "SimpleServlet", service = Servlet.class, property = {
	"osgi.http.whiteboard.servlet.pattern=/white" }, properties = { "configuration/SimpleServlet.cfg" })
// , "org.osgi.service.http.port=8089" })
public class SimpleServlet extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		super.doGet(req, resp);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		resp.getWriter().println("Hello Servlet, Hello OSGi, Hello Http Whitebard");
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
