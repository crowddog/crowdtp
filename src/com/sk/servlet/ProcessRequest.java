package com.sk.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Constants;
import plan.StartPlan;
import entities.Route;

public class ProcessRequest extends HttpServlet{
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=utf-8");
		HttpSession session = req.getSession();
		String requirement = new String(req.getParameter("requirement"));
		String start = new String(req.getParameter("start"));
		int scenenum = Integer.parseInt(req.getParameter("scenenum"));
		Constants.userRequireSceneNum = scenenum;
		StartPlan startplan = new StartPlan(start);
		Route bestroute = startplan.startplan(requirement);
        System.out.println("最佳路线是："+bestroute);
        
        String encode = "UTF-8";
        resp.setContentType("text/xml;charset=" + encode);
        resp.setHeader("Cache-Control","no-store");
        resp.setHeader("Pragma","no-cache");
        resp.setDateHeader("Expires",0);   
		resp.getWriter().write(bestroute.toString());
//        resp.getWriter().write("鸟巢-水立方-奥林匹克公园-天安门广场");
	}
}
