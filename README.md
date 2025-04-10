<p align=center>
  <a href="http://www.siamit.cn">
    <img src="./doc/images/gitee/logo.png" alt="暹罗记账" style="width:200px;height:200px">
  </a>
</p>

<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">暹罗记账 v1.0</h1>
<h4 align="center">基于SpringBoot开发的个人家庭理财记账系统</h4>

<p align="center">
<a target="_blank" href="https://gitee.com/siam1026/siam-finance">
    	<img src="https://img.shields.io/hexpm/l/plug.svg" ></img>
		<img src="https://img.shields.io/badge/JDK-1.8+-green.svg" ></img>
        <img src="https://img.shields.io/badge/nodejs-14.x-green" ></img>
        <img src="https://img.shields.io/badge/springboot-2.2.2.RELEASE-green" ></img>
        <img src="https://img.shields.io/badge/vue-2.5.17-green" ></img>
        <img src="https://img.shields.io/badge/swagger-3.0.0-brightgreen" ></img>
        <img src="https://img.shields.io/badge/mybatis--plus-3.1.2-green" ></img>
</a></p>


##  前言

**微信公众号【[暹罗Tech](https://gitee.com/siam1026/siam-finance/raw/master/doc/images/wechat/公众号.jpg)】**，未来将会在公众号上持续性的输出很多原创小知识以及学习资源，欢迎各位小伙伴关注我，和我一起共同学习，同时我也希望各位小伙伴能够给**暹罗记账**项目多多 **Star** 支持，您的**点赞**就是我维护的动力！

<p align=center>
    <img src="./doc/images/wechat/公众号.jpg" width="500" />
</p>

项目已有较详细的 [项目搭建文档](https://gitee.com/siam1026/siam-finance/wikis/pages?sort_id=9187815&doc_id=4850414) ，同时包括了 **Windows**、**Linux** 以及 **Docker** 环境下暹罗记账的搭建。在使用过程中遇到问题时，首先认真阅读**项目搭建文档**~

【提问】推荐使用 [Gitee issue](https://gitee.com/siam1026/siam-finance/issues) 进行提问，因为issue解决后能够保留解决记录，帮助其它小伙伴避坑。其次可以使用 <a href="https://gitee.com/siam1026/siam-finance#关注和交流">QQ群 </a>  或者 <a href="https://gitee.com/siam1026/siam-finance#关注和交流">微信群 </a> 进行提问。群里提问注意提问的时间，把遇到**问题的详细过程都描述清楚**，最好**配上图文信息**，这样能有利于更高效的解决问题。

## 项目介绍

暹罗记账是一款Java个人家庭理财记账系统，该系统包含数据中心、资金管理、基金投资、股票投资、可转债投资、系统配置等核心功能，支持OCR一键识别录入。

1. 数据中心
2. 资金管理，资金分类、动态资金分类记录菜单展示
3. 基金投资，统计分析、基金列表、短线、中长线
4. 股票投资，统计分析、基金列表、短线、中长线
5. 可转债投资，统计分析、基金列表、短线、中长线
6. 系统配置
7. OCR一键导入识别


## 软件架构

* 前端使用uni-app开发，可打包部署到微信小程序、APP、H5
* Web端使用vue + Element开发
* 服务端使用java语言开发，技术栈：Spring Boot + Redis + ELK + SpringBoot Admin

## 相关资料获取

1. 获取sql文件

[请加入QQ群或微信群获取](https://gitee.com/siam1026/siam-finance#关注和交流)，进群前先在右上角Star本项目

## 关联产品
暹罗外卖 - 多商户入驻的外卖配送系统，微服务架构：[https://gitee.com/siam1026/siam-cloud](https://gitee.com/siam1026/siam-cloud)  
暹罗记账 - 多门店连锁品牌的餐饮点餐系统，单体服务架构：[https://gitee.com/siam1026/siam-finance](https://gitee.com/siam1026/siam-finance)


## 系统模块

~~~
com.siam     
├── vue-siam-admin           // 前端-管理后台 [80]
├── siam-system             // web接口服务 [9200]
├── siam-monitor            // 监控服务 [9100]
├── siam-generator          // 代码生成框架
├── siam-common             // 通用模块
├── siam-weixin             // 微信模块
├── pom.xml                 // 父工程模块
~~~


## 站点地址

> 【用户中心web端】：http://106.12.130.163/finance-user
>
> 临时验证码：252525

## 项目中初始用户和密码
- **管理后台登录**：用户：admin，密码：123456

## 项目文档

文档地址：https://gitee.com/siam1026/siam-finance/wikis/pages?sort_id=9580601&doc_id=5164185

## 项目地址

目前项目托管在 **Gitee** 和 **Github** 平台上中，欢迎大家 **Star** 和 **Fork** 支持~

- Gitee地址：https://gitee.com/siam1026/siam-finance
- Github地址：https://github.com/siam1026/siam-finance


## 关注和交流

为了方便小伙伴们沟通交流，我创建了**微信群**（备注：**加群**），目前项目还存在很多不足之处，欢迎各位老哥进群进行技术交流，为了防止广告进入，希望加群的时候能添加备注，谢谢~

|                   微信群【备注：加群-理财系统】                   | QQ群（564717658）                       |
| :------------------------------------------------------: | :------------------------------------------------------: |
| <img src="https://gitee.com/siam1026/siam-finance/raw/master/doc/images/wechat/添加暹罗.jpg" width="200" /> | <img src="https://gitee.com/siam1026/siam-finance/raw/master/doc/images/qq/qqGroup2.jpg" width="200" /> |


## 网站截图

|                        管理后台                         |                                                       |
| :----------------------------------------------------: | :---------------------------------------------------: |
|      ![image text](./doc/images/vue-admin/fundTotalList.png)       |    ![image text](./doc/images/vue-admin/stockTotalList.png)    |
|      ![image text](./doc/images/vue-admin/convertibleBondTotalList.png)       |      |



## 赞赏

**服务器**和**域名**等服务的购买和续费都会**产生一定的费用**，为了**维持项目的正常运作**，如果觉得本项目**对您有帮助**的话，欢迎朋友能够**给予一些支持**，暹罗将用于**提升服务器配置**，感谢小伙伴们的支持（ **ps**: 小伙伴赞赏的时候可以备注一下下~）

|                       微信                       |                      支付宝                       |
| :----------------------------------------------: | :-----------------------------------------------: |
| <img src="./doc/images/wechat/wx_payment.png" width="200" /> | <img src="./doc/images/wechat/zfb_payment.png" width="200" /> |

