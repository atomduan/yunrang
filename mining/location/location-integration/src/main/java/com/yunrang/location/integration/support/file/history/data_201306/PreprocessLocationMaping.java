package com.yunrang.location.integration.support.file.history.data_201306;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;


/**
#拼音汉字对照表 GeoLiteCity_20121204
#标记为NG的是在GeoLiteCity库中没有对应city
Beijing,北京
Shanghai,上海
Tianjin,天津
Chongqing,重庆
Heilongjiang,黑龙江
Jilin,吉林
Liaoning,辽宁
Nei Mongol,内蒙古
Hebei,河北
Xinjiang,新疆
Gansu,甘肃
Qinghai,青海
Shaanxi,陕西
Ningxia,宁夏
Henan,河南
Shandong,山东
Shanxi,山西
Anhui,安徽
Hubei,湖北
Hunan,湖南
Jiangsu,江苏
Sichuan,四川
Guizhou,贵州
Yunnan,云南
Guangxi,广西
Xizang,西藏
Zhejiang,浙江
Jiangxi,江西
Guangdong,广东
Fujian,福建
Taiwan,台湾
Hainan,海南
Hong Kong,香港
Macau,澳门
#黑龙江
Harbin,哈尔滨
Qiqihar,齐齐哈尔
Daxinganling,大兴安岭,NG
Suihua,绥化
Heihe,黑河
Mudanjiang,牡丹江
Qitaihe,七台河
Jiamusi,佳木斯
Yichun,伊春
Daqing,大庆
Shuangyashan,双鸭山
Hegang,鹤岗
Jixi,鸡西
#吉林
Changchun,长春
Jilin,吉林
Yanbian,延边,NG
Baicheng,白城
Matsubara,松原,NG
Baishan,白山
Tonghua,通化
Liaoyüan,辽源
Siping,四平
 */

public class PreprocessLocationMaping implements HistoryFilesConfig.Processor {
    public void doProcess() throws Exception {
        ContextFileProcess processor = new ContextFileProcess();
        processor.doMutiThreadProcess(HistoryFilesConfig.yrDataPath_201306+"/locationMaping", new ContextFileProcess.LineProcessor(){
            public void processLine(String line) {
                //Ignore, this information has been processed in other place
            }
        });
    }
}
