
热更新文档位置：
https://bugly.qq.com/docs/user-guide/instruction-manual-android-hotfix-demo/
bugly-tinker完整接入流程
打基准包安装并上报联网（注：填写唯一的tinkerId）
对基准包的bug修复（可以是Java代码变更，资源的变更）
修改基准包路径、修改补丁包tinkerId、mapping文件路径（如果开启了混淆需要配置）、resId文件路径
执行buildTinkerPatchRelease打Release版本补丁包
选择app/build/outputs/patch目录下的补丁包并上传（注：不要选择tinkerPatch目录下的补丁包，不然上传会有问题）
编辑下发补丁规则，点击立即下发
杀死进程并重启基准包，请求补丁策略（SDK会自动下载补丁并合成）
再次重启基准包，检验补丁应用结果
查看页面，查看激活数据的变化

不支持java8的lambda
https://github.com/Tencent/tinker/issues/946