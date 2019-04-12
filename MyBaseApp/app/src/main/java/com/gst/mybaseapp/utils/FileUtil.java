package com.gst.mybaseapp.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Xml;

import com.gst.mybaseapp.base.AppConfig;
import com.gst.mybaseapp.base.MyApp;
import com.gst.mybaseapp.been.XmlApp;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 文件操作
 * 图片放在了bitmap，这里只操作xml和txt等
 * <p>
 * author: GuoSongtao on 2019/4/9 11:59
 * email: 157010607@qq.com
 */
public class FileUtil {


    public String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 将Assets文件转化为文件流
     *
     * @param ctx
     * @param assetsSrc
     * @param des
     * @return
     */
    public static boolean copyAssetsToFilesystem(Context ctx, String assetsSrc, String des) {
        // LogUtils.i("Copy " + assetsSrc + " to " + des);
        InputStream istream = null;
        OutputStream ostream = null;
        try {
            AssetManager am = ctx.getAssets();
            istream = am.open(assetsSrc);
            ostream = new FileOutputStream(des);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = istream.read(buffer)) > 0) {
                ostream.write(buffer, 0, length);
            }
            istream.close();
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (istream != null)
                    istream.close();
                if (ostream != null)
                    ostream.close();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            return false;
        }
        return true;
    }


    /**
     * 读取特定行的数据
     *
     * @param fileName    文件名
     * @param indexLines  开始行
     * @param lengthLines 结束行
     * @return
     */
    public static String getStringByLines(String fileName, int indexLines, int lengthLines) {

        StringBuilder sb = new StringBuilder("");
        InputStreamReader inputStreamReader = null;
        try {
            if (fileName != null && fileName.startsWith(AppConfig.ASSET_LOAD_PATH)) {
                //处理asset中的文件  MyApp.getInstance()得到context
                //注意ASSET_LOAD_PATH  为我自定义的asset路径名 file:///android_asset/ 和webview的导入asset路径相同
                inputStreamReader = new InputStreamReader(MyApp.getInstance().getAssets().open(fileName.substring(AppConfig.ASSET_LOAD_PATH.length())));
            } else {
                inputStreamReader = new FileReader(fileName);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (inputStreamReader == null) return sb.toString();

        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        long currLines = 0;
        try {
            while ((line = reader.readLine()) != null) {
                if (indexLines > currLines) {
                    //如果读取的起始位置 还未达到
                    currLines++;
                    continue;
                }
                if (currLines >= indexLines + lengthLines) {
                    sb.append(line);
                    sb.append("\n");
                    break;
                } else {
                    sb.append(line);
                    sb.append("\n");
                }
                currLines++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

    /**
     * 读取txt文件 内容
     *
     * @param fileName 文件名
     * @param index    txt的起始位置
     * @param length   要读取字段的长度
     * @return
     */
    public static String getString(String fileName, long index, long length) {

        StringBuilder sb = new StringBuilder("");
        InputStreamReader inputStreamReader = null;
        try {
            if (fileName != null && fileName.startsWith(AppConfig.ASSET_LOAD_PATH)) {
                //处理asset中的文件  MyApp.getInstance()得到context
                //注意ASSET_LOAD_PATH  为我自定义的asset路径名 file:///android_asset/ 和webview的导入asset路径相同
                inputStreamReader = new InputStreamReader(MyApp.getInstance().getAssets().open(fileName.substring(AppConfig.ASSET_LOAD_PATH.length())));
            } else {
                inputStreamReader = new FileReader(fileName);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (inputStreamReader == null) return sb.toString();

        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        long currLen = 0;
        try {
            while ((line = reader.readLine()) != null) {
                int lineLen = line.length();
                if (index > currLen + lineLen) {
                    //如果读取的起始位置 还未达到
                    currLen += line.length() + 1;//多了 换行符
                    continue;
                }
                //逻辑小复杂  自己拿笔画一下
                if (currLen + lineLen >= index + length) {
                    //结束
                    int len = (int) (currLen + lineLen - index - length);
                    sb.append(line.substring(0, lineLen - len));
                    sb.append("\n");
                    break;
                } else if (index > currLen && currLen + lineLen > index) {
                    //开始
                    sb.append(line.substring((int) (index - currLen), lineLen));
                    sb.append("\n");
                } else {
                    //中途 currLen + lineLen < index + length
                    sb.append(line);
                    sb.append("\n");
                }
                currLen += line.length() + 1;//多了 换行符
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }


    /**
     * 读取txt文件内容
     */
    public static String getString(String fileName) {
        return getString(fileName, 0, Long.MAX_VALUE);
    }


    /***************************************解析xml数据 start ***********************************************/
    /**
     * XML数据的Sax解析
     * SAX是一个解析速度快并且占用内存少的xml解析器，SAX解析XML文件采用的是事件驱动，它并不需要解析完整个文档，而是按内容顺序解析文档的过程
     * sax2xml 不好封装成 工具类
     */
    public List<XmlApp> sax2xml(InputStream is) throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        //初始化Sax解析器
        SAXParser sp = spf.newSAXParser();
        //新建解析处理器
        MyHandler handler = new MyHandler();
        //将解析交给处理器
        sp.parse(is, handler);
        //返回List
        return handler.getList();
    }

    class MyHandler extends DefaultHandler {

        private List<XmlApp> list;
        private XmlApp student;
        //用于存储读取的临时变量
        private String tempString;

        /**
         * 解析到文档开始调用，一般做初始化操作
         *
         * @throws SAXException
         */
        @Override
        public void startDocument() throws SAXException {
            list = new ArrayList<>();
            super.startDocument();
        }

        /**
         * 解析到文档末尾调用，一般做回收操作
         *
         * @throws SAXException
         */
        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        /**
         * 每读到一个元素就调用该方法
         *
         * @param uri
         * @param localName
         * @param qName
         * @param attributes
         * @throws SAXException
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if ("app".equals(qName)) {
                //读到student标签
                student = new XmlApp();
            } else if ("name".equals(qName)) {
                //获取name里面的属性
                String sex = attributes.getValue("type");
                student.setType(sex);
            }
            super.startElement(uri, localName, qName, attributes);
        }

        /**
         * 读到元素的结尾调用
         *
         * @param uri
         * @param localName
         * @param qName
         * @throws SAXException
         */
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("app".equals(qName)) {
                list.add(student);
            }
            if ("name".equals(qName)) {
                student.setName(tempString);
            } else if ("id".equals(qName)) {
                student.setId(tempString);
            } else if ("version".equals(qName)) {
                student.setVersion(tempString);
            }
            super.endElement(uri, localName, qName);
        }

        /**
         * 读到属性内容调用
         *
         * @param ch
         * @param start
         * @param length
         * @throws SAXException
         */
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            tempString = new String(ch, start, length);
            super.characters(ch, start, length);
        }

        /**
         * 获取该List
         *
         * @return
         */
        public List<XmlApp> getList() {
            return list;
        }
    }


    /**
     * XML数据的Pull解析
     * Pull解析器的运行方式与 SAX 解析器相似。它提供了类似的事件，可以使用一个switch对感兴趣的事件进行处理
     */
    public List<XmlApp> pull2xml(InputStream is) throws Exception {
        List<XmlApp> list = null;
        XmlApp student = null;
        //创建xmlPull解析器
        XmlPullParser parser = Xml.newPullParser();
        ///初始化xmlPull解析器
        parser.setInput(is, "utf-8");
        //读取文件的类型
        int type = parser.getEventType();
        //无限判断文件类型进行读取
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                //开始标签
                case XmlPullParser.START_TAG:
                    if ("apps".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("app".equals(parser.getName())) {
                        student = new XmlApp();
                    } else if ("name".equals(parser.getName())) {
                        //获取sex属性
                        String sex = parser.getAttributeValue(null, "type");
                        student.setType(sex);
                        //获取name值
                        String name = parser.nextText();
                        student.setName(name);
                    } else if ("id".equals(parser.getName())) {
                        //获取nickName值
                        String nickName = parser.nextText();
                        student.setId(nickName);
                    } else if ("version".equals(parser.getName())) {
                        //获取nickName值
                        String nickName = parser.nextText();
                        student.setVersion(nickName);
                    }
                    break;
                //结束标签
                case XmlPullParser.END_TAG:
                    if ("student".equals(parser.getName())) {
                        list.add(student);
                    }
                    break;
            }
            //继续往下读取标签类型
            type = parser.next();
        }
        return list;
    }

    /***
     * dom解析
     * @param is
     * @return
     * @throws Exception
     */
    public List<XmlApp> dom2xml(InputStream is) throws Exception {
        //一系列的初始化
        List<XmlApp> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        //获得Document对象
        Document document = builder.parse(is);
        //获得student的List
        NodeList studentList = document.getElementsByTagName("student");
        //遍历student标签
        for (int i = 0; i < studentList.getLength(); i++) {
            //获得student标签
            Node node_student = studentList.item(i);
            //获得student标签里面的标签
            NodeList childNodes = node_student.getChildNodes();
            //新建student对象
            XmlApp student = new XmlApp();
            //遍历student标签里面的标签
            for (int j = 0; j < childNodes.getLength(); j++) {
                //获得name和nickName标签
                Node childNode = childNodes.item(j);
                //判断是name还是nickName
                if ("name".equals(childNode.getNodeName())) {
                    String name = childNode.getTextContent();
                    student.setName(name);
                    //获取name的属性
                    NamedNodeMap nnm = childNode.getAttributes();
                    //获取sex属性，由于只有一个属性，所以取0
                    Node n = nnm.item(0);
                    student.setType(n.getTextContent());
                } else if ("id".equals(childNode.getNodeName())) {
                    String nickName = childNode.getTextContent();
                    student.setId(nickName);
                } else if ("nickName".equals(childNode.getNodeName())) {
                    String nickName = childNode.getTextContent();
                    student.setVersion(nickName);
                }
            }
            //加到List中
            list.add(student);
        }
        return list;
    }
    /***************************************解析xml数据   end ***********************************************/
}
