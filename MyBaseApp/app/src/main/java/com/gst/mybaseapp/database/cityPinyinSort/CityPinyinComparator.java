package com.gst.mybaseapp.database.cityPinyinSort;
import android.text.TextUtils;

import java.util.Comparator;

/**
 * author: GuoSongtao on 2019/4/9 16:10
 * email: 157010607@qq.com
 * 2017-5-15 要求直辖市排在前面
 * String s1 = "abc";
 * String s2 = "abcd";
 * String s3 = "abcdfg";
 * String s4 = "1bcdfg";
 * String s5 = "cdfg";
 * System.out.println( s1.compareTo(s2) ); // -1 (前面相等,s1长度小1)
 * System.out.println( s1.compareTo(s3) ); // -3 (前面相等,s1长度小3)
 * System.out.println( s1.compareTo(s4) ); // 48 ("a"的ASCII码是97,"1"的的ASCII码是49,所以返回48)
 * System.out.println( s1.compareTo(s5) ); // -2 ("a"的ASCII码是97,"c"的ASCII码是99,所以返回-2)
 */

public class CityPinyinComparator implements Comparator<String> {
    private String[] starts={"北京","天津","上海","重庆"};
    private String[] startIndexs={"01","02","03","04"};
    static class Inner {
        private static CityPinyinComparator instence = new CityPinyinComparator();
    }

    private CityPinyinComparator() {
    }

    public static CityPinyinComparator getInstance() {
        return Inner.instence;
    }

    public int compare(String o1, String o2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
        CharacterParser parser = CharacterParser.getInstance();

        String one, two;
        one = dealMultiToneChar(o1, parser);
        two = dealMultiToneChar(o2, parser);

        if (TextUtils.isEmpty(one) || TextUtils.isEmpty(two)) return -1;

        if ("#".equals(two)) {
            return -1;
        } else if ("#".equals(one)) {
            return 1;
        } else {
            return one.compareTo(two);
        }
    }

    /**
     * @param o1
     * @param parser
     * @return
     */
    private String dealMultiToneChar(String o1, CharacterParser parser) {
        String one;
//        if ("重庆".equals(o1)) {//目前只处理了 重庆 因为目前只处理省份 重 -chong -zhong
//            one = "chong";
//        } else
        for(int i=0;i<starts.length;i++){
            if(starts[i].equals(o1)){
                return startIndexs[i];
            }
        }

        if ("大城".equals(o1)) {
            one = "dai";
        } else if ("阿城".equals(o1)) {
            one = "a";
        } else if ("番禺".equals(o1)) {
            one = "pan";
        } else if ("渑池".equals(o1)) {
            one = "mian";
        } else if ("泌阳".equals(o1)) {
            one = "bi";
        } else if ("牟平".equals(o1)) {
            one = "mu";
        } else if ("冠县".equals(o1)) {
            one = "guan";
        } else if ("莘县".equals(o1)) {
            one = "shen";
        } else if ("济南".equals(o1)) {
            one = "ji";
        } else if ("涡阳".equals(o1)) {
            one = "guo";
        } else if ("歙县".equals(o1)) {
            one = "she";
        } else if ("乐清".equals(o1)) {
            one = "yue";
        } else if ("尉犁".equals(o1)) {
            one = "yu";
        } else if ("犍为".equals(o1)) {
            one = "qian";
        } else if ("筠连".equals(o1)) {
            one = "jun";
        } else if ("尉氏".equals(o1)) {
            one = "wei";
        }else if("漯河".equals(o1)){
            one = "luo";
        }else if("濮阳".equals(o1)){
            one = "pu";
        } else if("毫州".equals(o1)){
            one = "bo";
        } else if("儋州".equals(o1)){
            one = "dan";
        } else if("胥浦".equals(o1)){
            one = "xu";
        } else if("泸州".equals(o1)){
            one = "lu";
        }else if("衢州".equals(o1)){
            one = "qu";
        } else {
            one = parser.convert(o1.substring(0, 1));
        }
        return one;
    }

    /**
     * 作者：匿名用户
     链接：https://www.zhihu.com/question/26813834/answer/36271683
     来源：知乎
     著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     全国县级以上行政区划地名多音异读正音表(注：来自百度文库，原出处部分有误，已作更正，并以粗体标识)
     序号    地名    所在省区    正确注音
     1       大城        河北        dài 代
     2        蔚县        河北        yù 预
     3        乐亭        河北        lào 烙
     4        洪洞        山西        tóng 同
     5        侯马        山西        hóu 猴
     6        长子        山西        zhǎng 掌
     7        穆棱        黑龙江        líng 灵
     8        绥棱        黑龙江        léng 楞
     9        阿城        黑龙江        ā 阿
     10        六合        江苏        lù 陆
     11        台州        浙江        tāi 胎
     12        天台        浙江        tāi 胎
     13        丽水        浙江        lí 离
     14        乐清        浙江        yuè 岳
     15        歙县        安徽        shè 社
     16        六安        安徽        lù 陆
     17        涡阳        安徽        guō 锅
     18        蚌埠        安徽        bèng 泵
     19        闽侯        福建        hòu 厚
     20        铅山        江西        yán 沿
     21        万载        江西        zài 在
     22        济南        山东        jǐ 挤
     23        单县        山东        shàn 善
     24        莘县        山东        shēn 申
     25        冠县        山东        guān 观
     26        东阿        山东        ē
     27        牟平        山东        mù 木
     28        尉氏        河南        wèi 卫
     29        泌阳        河南        bì 必
     30        渑池        河南        miǎn 免
     31        浚县        河南        xùn 训
     32        黄陂        湖北        pí 皮
     33        监利        湖北        jiàn 见
     34        番禺        广东        pān 潘
     35        大埔        广东        bù 布
     36        通什        海南        zá 杂
     37        筠连        四川        jūn 均
     38        犍为        四川        qián 钱
     39        吴堡        陕西        bǔ 补
     40        安塞        陕西        sài 赛
     41        子长        陕西        cháng 常
     42        尉犁        新疆        yù 预
     */
}