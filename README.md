# 学习GitHub上的开源项目问题社区
哈哈哈 加油

## 资料
[Spring WEB文档](https://spring.io/guides/gs/serving-web-content/)

[Spring 文档](https://spring.io/guides)

[原项目GitHub地址](https://github.com/codedrinker/community)

[增加GitHub登录功能](https://docs.github.com/en/developers/apps/building-oauth-apps)

[BootStrap 文档](https://v3.bootcss.com/getting-started/)

[thymeleaf 文档](https://www.thymeleaf.org/index.html)

[okhttp 文档](https://square.github.io/okhttp/)

[springboot整合mybatis 文档](https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)

## 工具
git
github

## 功能
### 使用GitHub账号登录
![img.png](img.png)

## MySQL数据库
```sql
create table user
(
	ID int auto_increment
		primary key,
	account_id varchar(100) null,
	name varchar(50) null,
	token char(36) null,
	gmt_create bigint null,
	gmt_modified bigint null
);
```