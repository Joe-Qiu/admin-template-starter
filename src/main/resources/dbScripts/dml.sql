INSERT INTO department(id, dept_name, dept_desc) VALUES (1, '研发部', '1号会员店产品研发部');

INSERT INTO user_info values(1, 1, 'adminlte', 'Joe', '00', 'joe.qiu@outlook.com', '15888888888', '0', '', '45bb715c927611ca0546153036ab9c6c', 'cb)d([FOo7', '1', sysdate(), update_time);

INSERT INTO user_role(id, user_id, role_id) values (1, 1, 1);

INSERT INTO role_define (id, role_name, role_desc, state, create_time) VALUES (1, '超级管理员', '超级管理员', 1, '2017-09-14 15:02:16');

INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (1,'系统管理',0,NULL,'fa fa-cogs',1,1,'01',NULL);
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (2,'用户管理',1,'/system/user/list/1','fa-user',1,2,'0101','user');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (3,'角色管理',1,'/system/role/list/1','fa-users',2,2,'0102','role');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (4,'菜单管理',1,'/system/menu/list/1','fa-list',3,2,'0103','menu');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (5,'部门管理',1,'/system/dept/list/1','fa-graduation-cap',4,2,'0104','dept');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (6,'业务日志',1,'/system/log/list/1','fa-info-circle',5,2,'0105','log');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (10,'查看用户列表',2,NULL,NULL,0,3,'010100','listUser');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (11,'新增用户',2,NULL,NULL,1,3,'010101','addUser');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (12,'编辑用户',2,NULL,NULL,2,3,'010102','editUser');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (13,'删除用户',2,NULL,NULL,3,3,'010103','deleteUser');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (20,'查看角色列表',3,NULL,NULL,0,3,'010200','listRole');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (21,'新增角色',3,NULL,NULL,1,3,'010201','addRole');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (22,'编辑角色',3,NULL,NULL,2,3,'010202','editRole');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (23,'删除角色',3,NULL,NULL,3,3,'010203','deleteRole');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (24,'角色授权',3,NULL,NULL,4,3,'010204','authRole');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (25,'批量删除角色',3,NULL,NULL,5,3,'010205','deleteBatchRole');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (30,'查看菜单列表',4,NULL,NULL,0,3,'010300','listMenu');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (31,'创建菜单',4,NULL,NULL,1,3,'010301','addMenu');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (32,'编辑菜单',4,NULL,NULL,2,3,'010302','editMenu');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (33,'删除菜单',4,NULL,NULL,3,3,'010303','deleteMenu');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (40,'查看部门列表',5,NULL,NULL,0,3,'010400','listDept');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (41,'新增部门',5,NULL,NULL,1,3,'010401','addDept');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (42,'编辑部门',5,NULL,NULL,2,3,'010402','editDept');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (43,'删除部门',5,NULL,NULL,3,3,'010403','deleteDept');
INSERT INTO menu_config (id,name,pid,url,icon,sort,deep,code,resource) VALUES (50,'查看日志列表',6,NULL,NULL,0,3,'010500','listLog');

INSERT INTO role_menu (id,role_id,menu_id) VALUES (1,1,1);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (2,1,2);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (3,1,10);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (4,1,11);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (5,1,12);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (6,1,13);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (7,1,3);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (8,1,20);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (9,1,21);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (10,1,22);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (11,1,23);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (12,1,24);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (13,1,25);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (14,1,4);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (15,1,30);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (16,1,31);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (17,1,32);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (18,1,33);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (19,1,5);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (20,1,40);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (21,1,41);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (22,1,42);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (23,1,43);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (24,1,6);
INSERT INTO role_menu (id,role_id,menu_id) VALUES (25,1,50);