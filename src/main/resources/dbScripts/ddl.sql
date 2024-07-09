DROP TABLE IF EXISTS department;

CREATE TABLE department (
    id            bigint(20) unsigned     NOT NULL auto_increment COMMENT '主键',
    dept_name     varchar(100)            NOT NULL COMMENT '部门名称',
    dept_desc     varchar(300)            DEFAULT NULL COMMENT '描述',
    PRIMARY KEY (id)
)engine=innodb DEFAULT charset=utf8mb4 auto_increment=10 COMMENT='部门表';

DROP TABLE IF EXISTS user_info;
CREATE TABLE user_info (
    id                bigint(20)      NOT NULL auto_increment    comment '用户ID',
    dept_id           bigint(20)      DEFAULT NULL               comment '部门ID',
    user_name         varchar(30)     NOT NULL                   comment '登录账号',
    nick_name         varchar(30)     DEFAULT ''                 comment '用户昵称',
    user_type         varchar(2)      DEFAULT '00'               comment '用户类型（00内部用户 01外部用户）',
    email             varchar(50)     DEFAULT ''                 comment '用户邮箱',
    phone             varchar(11)     DEFAULT ''                 comment '手机号码',
    gender            char(1)         DEFAULT '0'                comment '用户性别（0男 1女 2未知）',
    avatar            varchar(100)    DEFAULT ''                 comment '头像路径',
    password          varchar(50)     DEFAULT ''                 comment '密码',
    salt              varchar(20)     DEFAULT ''                 comment '盐加密',
    status            char(1)         DEFAULT '0'                comment '帐号状态（1正常 0停用）',
    create_time       datetime        DEFAULT NULL               comment '创建时间',
    update_time       datetime        DEFAULT NULL               comment '更新时间',
    primary key (id),
    key idx_user_name(user_name)
) engine=innodb DEFAULT charset=utf8mb4 auto_increment=10 comment = '用户信息表';

DROP TABLE IF EXISTS user_role;
CREATE TABLE user_role (
    id            bigint(20) unsigned     NOT NULL auto_increment COMMENT '主键',
    user_id        bigint(20) unsigned     NOT NULL COMMENT '用户主键',
    role_id       bigint(20) unsigned     NOT NULL COMMENT '角色主键',
    PRIMARY KEY (id)
) engine=innodb DEFAULT charset=utf8mb4 auto_increment=10 COMMENT='用户角色关联表';

DROP TABLE IF EXISTS role_define;
CREATE TABLE role_define (
    id            bigint(20) unsigned     NOT NULL auto_increment COMMENT '主键',
    role_name     varchar(50)             NOT NULL COMMENT '角色名称',
    role_desc     varchar(300)            DEFAULT NULL COMMENT '角色描述',
    state         int(2)                  DEFAULT '1' COMMENT '状态,1-启用,-1禁用',
    create_time   datetime                DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (id)
) engine=innodb DEFAULT charset=utf8mb4 auto_increment=10 COMMENT='角色表';

DROP TABLE IF EXISTS app_logs;
CREATE TABLE app_logs (
    id            bigint(20) unsigned     NOT NULL auto_increment COMMENT '主键',
    username      varchar(50)             NOT NULL COMMENT '用户',
    details       varchar(300)            DEFAULT NULL COMMENT '日志',
    url           varchar(300)            DEFAULT NULL COMMENT '地址',
    params        text                    DEFAULT NULL COMMENT '参数',
    create_time   datetime                DEFAULT NULL COMMENT '日志时间',
    PRIMARY KEY (id)
) engine=innodb DEFAULT charset=utf8mb4 auto_increment=10 COMMENT='日志表';

DROP TABLE IF EXISTS menu_config;
CREATE TABLE menu_config (
    id            bigint(20) unsigned     NOT NULL auto_increment COMMENT '主键',
    name          varchar(50)             NOT NULL COMMENT '菜单名称',
    pid           bigint(20) unsigned     NOT NULL COMMENT '父级菜单ID',
    url           varchar(255)            DEFAULT NULL COMMENT '连接地址',
    icon          varchar(50)             DEFAULT NULL COMMENT '图标',
    sort          int(11)                 DEFAULT 0 COMMENT '排序',
    deep          int(11)                 DEFAULT NULL COMMENT '深度',
    code          varchar(50)             DEFAULT NULL COMMENT '编码',
    resource      varchar(50)             DEFAULT NULL COMMENT '资源名称',
    PRIMARY KEY (id)
) engine=innodb DEFAULT charset=utf8mb4 auto_increment=10 COMMENT='菜单表';

DROP TABLE IF EXISTS role_menu;
CREATE TABLE role_menu (
    id            bigint(20) unsigned     NOT NULL auto_increment COMMENT '主键',
    role_id       bigint(20) unsigned     NOT NULL COMMENT '角色主键',
    menu_id       bigint(20) unsigned     NOT NULL COMMENT '角色主键',
    PRIMARY KEY (id)
) engine=innodb DEFAULT charset=utf8mb4 auto_increment=10 COMMENT='角色菜单关联表';

DROP TABLE IF EXISTS online_info;
CREATE TABLE online_info (
    id                  bigint(20)     NOT NULL auto_increment   COMMENT 'ID',
    user_name          varchar(50)    DEFAULT ''                COMMENT '登录账号',
    session             varchar(255)   DEFAULT ''                COMMENT 'SESSION ID',
    ip_addr             varchar(128)   DEFAULT ''                COMMENT '登录IP地址',
    browser             varchar(50)    DEFAULT ''                COMMENT '浏览器类型',
    os_name             varchar(50)    DEFAULT ''                COMMENT '操作系统',
    last_login_time     datetime       DEFAULT NULL              COMMENT '最后登录时间',
    last_visit_time     datetime       DEFAULT NULL              COMMENT '最后访问时间',
    primary key (id),
    key idx_session(session)
) engine=innodb DEFAULT charset=utf8mb4 auto_increment=10 COMMENT = '系统访问记录';
