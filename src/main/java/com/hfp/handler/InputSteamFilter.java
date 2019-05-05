package com.hfp.handler;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;


@WebFilter(filterName="inputSteamFilter",urlPatterns="/*")
@Component      // 将此Filter交给Spring容器管理
//@Order(1)     // 指定过滤器的执行顺序,值越大越靠后执行
public class InputSteamFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(InputSteamFilter.class);
	
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	logger.info("***************** InputSteamFilter init *****************");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("***************** InputSteamFilter begin *****************");

        // 防止流读取一次后就没有了, 所以需要将流继续写出去
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest);

        filterChain.doFilter(requestWrapper, servletResponse);
        logger.info("***************** InputSteamFilter end *****************");
    }

    @Override
    public void destroy() {
    	logger.info("***************** InputSteamFilter destroy *****************");
    }

}

class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper{
	private static Logger logger = LoggerFactory.getLogger(BodyReaderHttpServletRequestWrapper.class);

	private final byte[] body;

	public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		String sessionStream = getBodyString(request);
		body = sessionStream.getBytes(Charset.forName("UTF-8"));
	}

	/**
	 * 获取请求Body
	 *
	 * @param request
	 * @return
	 */
	private String getBodyString(final ServletRequest request) {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		BufferedReader reader = null;
		try {
			inputStream = cloneInputStream(request.getInputStream());
			reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			logger.error("",e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("",e);
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("",e);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Description: 复制输入流</br>
	 *
	 * @param inputStream
	 * @return</br>
	 */
	private InputStream cloneInputStream(ServletInputStream inputStream) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = inputStream.read(buffer)) > -1) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
			byteArrayOutputStream.flush();
		} catch (IOException e) {
			logger.error("",e);
		}
		InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		return byteArrayInputStream;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {

		final ByteArrayInputStream bais = new ByteArrayInputStream(body);

		return new ServletInputStream() {

			@Override
			public int read() throws IOException {
				return bais.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
			}
		};
	}
}