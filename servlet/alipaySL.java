package com.alipay.servlet;

import java.io.UnsupportedEncodingException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;   
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.config.AlipayConfig;

/*
 * 参数说明：
 * WIDout_trade_no：商户订单号  例如：使用年份到毫秒的时间戳
 * WIDsubject：订单名称 例如：用于启动飞涤洗车机
 * WIDtotal_amount：钱数 单位为元
 * WIDbody：订单描述 例如：飞涤洗车费用
 * 
 * 商户订单号生成代码（JS）：
 *  var vNow = new Date();
	var sNow = "";
	sNow += String(vNow.getFullYear());
	sNow += String(vNow.getMonth() + 1);
	sNow += String(vNow.getDate());
	sNow += String(vNow.getHours());
	sNow += String(vNow.getMinutes());
	sNow += String(vNow.getSeconds());
	sNow += String(vNow.getMilliseconds());
 */
@WebServlet("/AlipaySL")
public class alipaySL extends HttpServlet{
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		doPost(request,response);
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		String out_trade_no = new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		String subject = new String(request.getParameter("WIDsubject").getBytes("ISO-8859-1"),"UTF-8");
		String total_amount = new String(request.getParameter("WIDtotal_amount").getBytes("ISO-8859-1"),"UTF-8");
		String body = new String(request.getParameter("WIDbody").getBytes("ISO-8859-1"),"UTF-8");
		String timeout_express = "2m";
		String product_code= "QUICK_WAP_WAY";
		
		AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL,
													  AlipayConfig.APPID,
													  AlipayConfig.RSA_PRIVATE_KEY,
													  AlipayConfig.FORMAT,
													  AlipayConfig.CHARSET,
													  AlipayConfig.ALIPAY_PUBLIC_KEY,
													  AlipayConfig.SIGNTYPE);
		AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();
		
		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setOutTradeNo(out_trade_no);
		model.setSubject(subject);
		model.setTotalAmount(total_amount);
		model.setBody(body);
		model.setTimeoutExpress(timeout_express);
		model.setProductCode(product_code);
		alipay_request.setBizModel(model);
		alipay_request.setNotifyUrl(AlipayConfig.notify_url);
		alipay_request.setReturnUrl(AlipayConfig.return_url);
		
		
		String form = "";
		try {
			form = client.pageExecute(alipay_request).getBody();
			response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
			response.getWriter().write(form);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}