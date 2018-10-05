package com.tiny.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author: PeterPuff
 * @date : 2018-10-05 下午11:06
 */

@Slf4j
public class HttpUtils {
    // 创建默认http实例
    private CloseableHttpClient client = HttpClients.createDefault();
    private CloseableHttpResponse response;

    private HttpEntity DoGet (String url) throws Exception {
        // 创建一个get对象
        HttpGet get = new HttpGet(url);
        response = client.execute(get);

        // 创建一个实体
        HttpEntity entity =  response.getEntity();

        // 判断响应是否正常
        if(entity != null){
            log.info(String.format("HTTP正常, 状态码: %s", response.getStatusLine().getStatusCode()));
            log.info(String.format("响应文本 %s", EntityUtils.toString(entity)));
            return entity;
        }
        return null;

    }
}
