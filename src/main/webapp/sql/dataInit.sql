DROP database IF EXISTS `alarm`;

create database alarm;

use alarm;

CREATE TABLE `module` (
  `mid` int(11) NOT NULL AUTO_INCREMENT,
  `mname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`mid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Records of module
-- ----------------------------
INSERT INTO `module` VALUES ('1', 'add');
INSERT INTO `module` VALUES ('2', 'delete');
INSERT INTO `module` VALUES ('3', 'query');
INSERT INTO `module` VALUES ('4', 'update');


-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `rid` int(11) NOT NULL AUTO_INCREMENT,
  `rname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'admin');
INSERT INTO `role` VALUES ('2', 'customer');


-- ----------------------------
-- Table structure for module_role
-- ----------------------------
DROP TABLE IF EXISTS `module_role`;
CREATE TABLE `module_role` (
  `rid` int(11) DEFAULT NULL,
  `mid` int(11) DEFAULT NULL,
  KEY `rid` (`rid`),
  KEY `mid` (`mid`),
  CONSTRAINT `mid` FOREIGN KEY (`mid`) REFERENCES `module` (`mid`),
  CONSTRAINT `rid` FOREIGN KEY (`rid`) REFERENCES `role` (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `module_role` VALUES ('1', '1');
INSERT INTO `module_role` VALUES ('1', '2');
INSERT INTO `module_role` VALUES ('1', '3');
INSERT INTO `module_role` VALUES ('1', '4');
INSERT INTO `module_role` VALUES ('2', '1');
INSERT INTO `module_role` VALUES ('2', '3');
-- ----------------------------
-- Records of user
-- ----------------------------

CREATE TABLE
    USER
    (
        uid INT NOT NULL AUTO_INCREMENT,
        username VARCHAR(255),
        password VARCHAR(255),
        PRIMARY KEY (uid)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `user` VALUES ('1', 'admin', 'admin123');
INSERT INTO `user` VALUES ('2', 'steven', 'steven123');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `uid` int(11) DEFAULT NULL,
  `rid` int(11) DEFAULT NULL,
  KEY `u_fk` (`uid`),
  KEY `r_fk` (`rid`),
  CONSTRAINT `r_fk` FOREIGN KEY (`rid`) REFERENCES `role` (`rid`),
  CONSTRAINT `u_fk` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1');
INSERT INTO `user_role` VALUES ('2', '2');

-- ----------------------------
-- whiteip 白名单
-- ----------------------------
CREATE TABLE
    whiteip
    (
        id INT NOT NULL AUTO_INCREMENT,
        ip VARCHAR(200),
        created TIMESTAMP NULL,
        wname VARCHAR(100),
        PRIMARY KEY (id)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- platforminfo 平台信息
-- ----------------------------
    CREATE TABLE
    platforminfo
    (
        id INT NOT NULL AUTO_INCREMENT,
        platform_url VARCHAR(200),
        alarm_url VARCHAR(200),
        pname VARCHAR(100),
        PRIMARY KEY (id)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

