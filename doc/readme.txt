Lerx CMS 5.5 build:20190507

开发：www.lerx.com
支持方：江苏科瑞达 www.coreder.com

代码：lzh
美工：小燕子

架构与环境

JDK && JRE ：1.8+
SpringMVC  : 4.0
Hibernate  ：5.5

本项目跨平台，支持Windows与Linux。

具体使用方法及其它信息，请登录官方网站查阅。

本产品为开源软件，受著作权法和国际条约保护。基于 Apache License 2.0开源协议。



----------------------------------------------------------------------------------

数据库建表请用utf8
create database portal default character set utf8 collate utf8_general_ci;

权限
GRANT ALL ON *.* to portal@'localhost' IDENTIFIED by 'ilovelerx'; 


-------------------- 5.0.x 升级到 5.1--------------------------------------------

RENAME TABLE templet_portal_sub_el TO templet_sub_el;

UPDATE `article_group` SET `hotn` = 0;

UPDATE `article` SET `hotn` = 0;

-------------------- 5.2 升级到 5.3--------------------------------------------

UPDATE `article_group` SET `coerce`=0

UPDATE `visitor_ip_record` SET `totalIn6Hours`=1;

-------------------------------------------------------------------------------------
升级时请运行 http://localhost/action_update/start 进行数据修复

V5新安装后，需要注意及修改的配置文件列表和说明
请参见以下文章

http://www.lerx.com/html/g10/2019/01-04/085853662-80.html
