alter table comment modify parent_id bigint not null comment '父类id。如果这条评论回复的是问题，那么它的父类就是问题。
如果这条评论回复的是评论，那么它的父类型就是评论
';
