Lerx CMS 5.5 build:20190507

������www.lerx.com
֧�ַ������տ���� www.coreder.com

���룺lzh
������С����

�ܹ��뻷��

JDK && JRE ��1.8+
SpringMVC  : 4.0
Hibernate  ��5.5

����Ŀ��ƽ̨��֧��Windows��Linux��

����ʹ�÷�����������Ϣ�����¼�ٷ���վ���ġ�

����ƷΪ��Դ�����������Ȩ���͹�����Լ���������� Apache License 2.0��ԴЭ�顣



----------------------------------------------------------------------------------

���ݿ⽨������utf8
create database portal default character set utf8 collate utf8_general_ci;

Ȩ��
GRANT ALL ON *.* to portal@'localhost' IDENTIFIED by 'ilovelerx'; 


-------------------- 5.0.x ������ 5.1--------------------------------------------

RENAME TABLE templet_portal_sub_el TO templet_sub_el;

UPDATE `article_group` SET `hotn` = 0;

UPDATE `article` SET `hotn` = 0;

-------------------- 5.2 ������ 5.3--------------------------------------------

UPDATE `article_group` SET `coerce`=0

UPDATE `visitor_ip_record` SET `totalIn6Hours`=1;

-------------------------------------------------------------------------------------
����ʱ������ http://localhost/action_update/start ���������޸�

V5�°�װ����Ҫע�⼰�޸ĵ������ļ��б��˵��
��μ���������

http://www.lerx.com/html/g10/2019/01-04/085853662-80.html
