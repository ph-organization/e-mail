package com.puhui.email.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.puhui.email.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: 邹玉玺
 * @date: 2020/7/21-15:39
 */
@Slf4j
@Component
public class MessageUtil {
    //产品名称:云通信短信API产品,开发者无需替换
    private String product = "Dysmsapi";
    //产品域名,开发者无需替换
    private String domain = "dysmsapi.aliyuncs.com";
    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    @Value("${accessKeyId}")
    private String accessKeyId;
    @Value("${accessKeySecret}")
    private String accessKeySecret;
    // 签名
    @Value("${sign}")
    private String sign;
    @Value("${templateCode}")
    private String templateCode;


    public  Message sendMessage(Message message){
           log.info("发送短信中-------------");

        try {
            SendSmsResponse sendSmsResponse = sendSms(message.getTargetphone(), "123456");
            log.info(sendSmsResponse.getMessage());
        } catch (ClientException e) {
            log.error("短信发送失败"+e.getMessage());

        }
        log.info("短信发送完毕");
        
        //设置发送时间
        message.setSendtime(CommonUtil.getTimeUtil());
        return message;
    }



    /**
     * 发送短信的工具
     * @param mobile   手机号
     * @param msgCode   变量
     * @return
     * @throws ClientException
     */
    public  SendSmsResponse sendSms(String mobile, String msgCode) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(mobile);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(sign);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode( templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":\""+msgCode+"\"}");
        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        return sendSmsResponse;
    }


}
