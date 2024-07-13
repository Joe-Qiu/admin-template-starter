package com.treeview.controller.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.treeview.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 基础控制器
 */
@Slf4j
public class SuperController {
    @Resource
    @Qualifier("gsonInst")
    protected Gson gson;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    protected HttpSession session;

    @Autowired
    protected ServletContext application;

    protected boolean isPost() {
        return HttpUtil.isPost(this.request);
    }

    protected boolean isGet() {
        return HttpUtil.isGet(this.request);
    }

    protected <T> Page<T> getPage(int pageNumber) {
        return this.getPage(pageNumber, 15);
    }

    protected <T> Page<T> getPage(int pageNumber, int pageSize) {
        return new Page(pageNumber, pageSize);
    }

    protected String redirectTo(String url) {
        StringBuffer rto = new StringBuffer("redirect:");
        rto.append(url);
        return rto.toString();
    }

    protected String toJson(Object object) {
        return gson.toJson(object);
    }
}