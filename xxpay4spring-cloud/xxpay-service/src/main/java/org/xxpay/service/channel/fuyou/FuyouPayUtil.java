package org.xxpay.service.channel.fuyou;

import com.github.pagehelper.StringUtil;
import com.google.common.collect.Maps;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class FuyouPayUtil {

    static OkHttpClient fuyou_http_client = null;

    static {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        fuyou_http_client = okHttpBuilder.connectionPool(new ConnectionPool(5,5, TimeUnit.SECONDS)).connectTimeout(10,TimeUnit.SECONDS).build();
    }

    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    public static String httpGet(String url){
        String result = "";
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = null;
        try {
            response = fuyou_http_client.newCall(request).execute();
            result = new String(response.body().bytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String httpPost(String url,String json){
        String result = "";
        RequestBody requestBody = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = null;
        try {
            response = fuyou_http_client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String httpFromPost(String url,Map<String,String> paramsMap){
        String result = "";
            OkHttpClient mOkHttpClient = new OkHttpClient();
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            Set<String> keySet = paramsMap.keySet();
            for(String key:keySet) {
                String value = paramsMap.get(key);
                formBodyBuilder.add(key,value);
            }
            FormBody formBody = formBodyBuilder.build();
            Request request = new Request
                    .Builder()
                    .post(formBody)
                    .url(url)
                    .build();
            try (Response response = mOkHttpClient.newCall(request).execute()) {
                result = response.body().string();
            }catch (Exception e){
                e.printStackTrace();
            }

            return result;

    }

    /**
     * 实体转字符串拼接
     */
    public static String ObjectToString(Object OiObj) throws IllegalAccessException {
        // 属性和属性值的拼接
        String nFieldAndValue = "";
        // 解析后的属性
        String nField = "";
        // 解析后的属性值
        String nValue = "";
        // 1.得到实体的所有属性数组
        Field[] fields = OiObj.getClass().getDeclaredFields();

        List<String> list = new ArrayList<>();
        for (Field f:fields){
            list.add(f.getName());
        }

        Collections.sort(list);


        // 2.循环取出属性
        // Field field : fields
        for (String item:list) {

            Field field = Arrays.stream(fields).filter(x->x.getName().equals(item)).findFirst().get();

            // 2.1设置些属性是可以访问的
            field.setAccessible(true);
            // 2.2得到字段
            nField = field.getName();

            if("sign".equals(nField) || "sign_type".equals(nField)) {
                continue;
            }

            // 2.3得到字段值
            nValue = (field.get(OiObj)) + "";
            // 2.4拼接属性和属性值
            if (nValue != null) {
                nFieldAndValue += nField + "=" + nValue + "&";
            }


        }
        // 3.返回时去掉最后面的逗号
        return nFieldAndValue.substring(0, nFieldAndValue.length() - 1);
    }

    /**
     * 实体转字符串拼接
     */
    public static Map<String, String> ObjectToString2(Object OiObj) throws IllegalAccessException {
        Map<String,String> map = new Hashtable<>();

        // 属性和属性值的拼接
        String nFieldAndValue = "";
        // 解析后的属性
        String nField = "";
        // 解析后的属性值
        String nValue = "";
        // 1.得到实体的所有属性数组
        Field[] fields = OiObj.getClass().getDeclaredFields();

        List<String> list = new ArrayList<>();
        for (Field f:fields){
            list.add(f.getName());
        }

        Collections.sort(list);


        // 2.循环取出属性
        // Field field : fields
        for (String item:list) {

            Field field = Arrays.stream(fields).filter(x->x.getName().equals(item)).findFirst().get();

            // 2.1设置些属性是可以访问的
            field.setAccessible(true);
            // 2.2得到字段
            nField = field.getName();

            // 2.3得到字段值
            nValue = (field.get(OiObj)) + "";
            // 2.4拼接属性和属性值
            if (nValue != null) {
                map.put(nField,nValue);
            }


        }
        // 3.返回时去掉最后面的逗号
        return map;
    }


    public static String OToString(Map<String,String> map){
        String result = "";
        Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
        while(entries.hasNext()){
            Map.Entry<String, String> entry = entries.next();

            if ("sign".equals(entry.getKey()) || "sign_type".equals(entry.getKey())) {
                continue;
            }

            if (!StringUtils.isBlank(entry.getValue())){
                result += entry.getKey() + "=" + entry.getValue() + "&";

            }
        }

        return result.substring(0, result.length() - 1);

    }

    public static List<Map.Entry<String, String>> sortMap(final Map<String, String> map) {
        final List<Map.Entry<String, String>> infos = new ArrayList<Map.Entry<String, String>>(map.entrySet());

        // 重写集合的排序方法：按字母顺序
        Collections.sort(infos, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(final Map.Entry<String, String> o1, final Map.Entry<String, String> o2) {
                return (o1.getKey().toString().compareTo(o2.getKey()));
            }
        });

        return infos;
    }


    public static Map convertBean(Object bean) throws Exception {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }

    /**
     * 按照key进行排序
     * @param map
     * @return
     */

    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }

    public static String MD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {

        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i=0; i<bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString().toLowerCase();
    }


}

class MapKeyComparator implements Comparator<String>{

    @Override
    public int compare(String str1, String str2) {

        return str1.compareTo(str2);
    }
}
