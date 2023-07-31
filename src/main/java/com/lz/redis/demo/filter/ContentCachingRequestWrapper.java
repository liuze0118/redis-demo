package com.lz.redis.demo.filter;

import com.google.common.io.ByteStreams;
import org.apache.catalina.util.ParameterMap;
import org.apache.tomcat.util.http.Parameters;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Map;

/**
 * spring的ContentCachingRequestWrapper的局限性在于
 * 必须要在doFilter之后使用，因为要先让Servlet读了才会缓存下来
 * ***************************************************
 * 此wrapper在初始化时就读出来缓存
 */
public class ContentCachingRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    private BufferedReader reader;

    private ServletInputStream inputStream;

    private Parameters parameters;

    protected ParameterMap<String, String[]> parameterMap = new ParameterMap<>();

    public ContentCachingRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        // 读一次 然后缓存起来
        body = ByteStreams.toByteArray(request.getInputStream());
        inputStream = new ContentCachingRequestWrapper.RequestCachingInputStream(body);
        parameters = new Parameters();
        parameters.processParameters(body, 0, request.getContentLength());
    }

    public byte[] getContentAsByteArray() {
        return body;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (inputStream != null) {
            return inputStream;
        }
        return super.getInputStream();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(inputStream, getCharacterEncoding()));
        }
        return reader;
    }

    @Override
    public String getParameter(String name) {
        return parameters.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (parameterMap.isLocked()) {
            return parameterMap;
        }

        Enumeration<String> enumeration = getParameterNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String[] values = getParameterValues(name);
            parameterMap.put(name, values);
        }

        parameterMap.setLocked(true);

        return parameterMap;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return parameters.getParameterNames();
    }

    @Override
    public String[] getParameterValues(String name) {
        return parameters.getParameterValues(name);
    }

    // 代理一下ServletInputStream 里面内容为当前缓存的bytes
    private static class RequestCachingInputStream extends ServletInputStream {

        private final ByteArrayInputStream inputStream;

        public RequestCachingInputStream(byte[] bytes) {
            inputStream = new ByteArrayInputStream(bytes);
        }
        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public boolean isFinished() {
            return inputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readlistener) {
        }
    }
}

