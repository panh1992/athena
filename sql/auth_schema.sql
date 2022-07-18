/*
 Navicat Premium Data Transfer

 Source Server         : postgres
 Source Server Type    : PostgreSQL
 Source Server Version : 110002
 Source Host           : localhost:5432
 Source Catalog        : test
 Source Schema         : auth

 Target Server Type    : PostgreSQL
 Target Server Version : 110002
 File Encoding         : 65001

 Date: 03/11/2019 19:59:41
*/


-- ----------------------------
-- Table structure for resource
-- ----------------------------
DROP TABLE IF EXISTS "auth"."resource";
CREATE TABLE "auth"."resource" (
  "resource_id" int8 NOT NULL,
  "uri" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "method" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamptz(6) NOT NULL,
  "modify_time" timestamptz(6),
  "permission" varchar(128) COLLATE "pg_catalog"."default",
  "description" varchar(255) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "auth"."resource" OWNER TO "test";
COMMENT ON COLUMN "auth"."resource"."resource_id" IS '主键';
COMMENT ON COLUMN "auth"."resource"."uri" IS '请求路径';
COMMENT ON COLUMN "auth"."resource"."method" IS '请求方式';
COMMENT ON COLUMN "auth"."resource"."create_time" IS '创建时间';
COMMENT ON COLUMN "auth"."resource"."modify_time" IS '修改时间';
COMMENT ON COLUMN "auth"."resource"."permission" IS '资源标识';
COMMENT ON COLUMN "auth"."resource"."description" IS '权限描述';
COMMENT ON TABLE "auth"."resource" IS '资源表';

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS "auth"."role";
CREATE TABLE "auth"."role" (
  "role_id" int8 NOT NULL,
  "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamptz(6) NOT NULL,
  "modify_time" timestamptz(6),
  "description" varchar(255) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "auth"."role" OWNER TO "test";
COMMENT ON COLUMN "auth"."role"."role_id" IS '主键';
COMMENT ON COLUMN "auth"."role"."name" IS '角色名称';
COMMENT ON COLUMN "auth"."role"."create_time" IS '创建时间';
COMMENT ON COLUMN "auth"."role"."modify_time" IS '更新时间';
COMMENT ON COLUMN "auth"."role"."description" IS '角色描述';
COMMENT ON TABLE "auth"."role" IS '角色表';

-- ----------------------------
-- Table structure for role_resource_relation
-- ----------------------------
DROP TABLE IF EXISTS "auth"."role_resource_relation";
CREATE TABLE "auth"."role_resource_relation" (
  "role_id" int8 NOT NULL,
  "resource_id" int8 NOT NULL
)
;
ALTER TABLE "auth"."role_resource_relation" OWNER TO "test";
COMMENT ON COLUMN "auth"."role_resource_relation"."role_id" IS '角色id';
COMMENT ON COLUMN "auth"."role_resource_relation"."resource_id" IS '资源id';
COMMENT ON TABLE "auth"."role_resource_relation" IS '角色与资源关系表';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS "auth"."user";
CREATE TABLE "auth"."user" (
  "user_id" int8 NOT NULL,
  "username" varchar(64) COLLATE "pg_catalog"."default",
  "nickname" varchar(64) COLLATE "pg_catalog"."default",
  "password" varchar(128) COLLATE "pg_catalog"."default",
  "email" varchar(128) COLLATE "pg_catalog"."default",
  "mobile" varchar(32) COLLATE "pg_catalog"."default",
  "profile" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamptz(6)
)
;
ALTER TABLE "auth"."user" OWNER TO "test";
COMMENT ON COLUMN "auth"."user"."user_id" IS '用户主键';
COMMENT ON COLUMN "auth"."user"."username" IS '用户名称';
COMMENT ON COLUMN "auth"."user"."nickname" IS '用户昵称';
COMMENT ON COLUMN "auth"."user"."password" IS '用户密码';
COMMENT ON COLUMN "auth"."user"."email" IS '邮箱';
COMMENT ON COLUMN "auth"."user"."mobile" IS '手机号';
COMMENT ON COLUMN "auth"."user"."profile" IS '个人简介';
COMMENT ON COLUMN "auth"."user"."create_time" IS '创建时间';
COMMENT ON TABLE "auth"."user" IS '用户表';

-- ----------------------------
-- Primary Key structure for table resource
-- ----------------------------
ALTER TABLE "auth"."resource" ADD CONSTRAINT "resource_pkey" PRIMARY KEY ("resource_id");

-- ----------------------------
-- Primary Key structure for table role
-- ----------------------------
ALTER TABLE "auth"."role" ADD CONSTRAINT "role_pkey" PRIMARY KEY ("role_id");

-- ----------------------------
-- Indexes structure for table role_resource_relation
-- ----------------------------
CREATE INDEX "role_resource_relation_resource_id_idx" ON "auth"."role_resource_relation" USING btree (
  "resource_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
CREATE INDEX "role_resource_relation_role_id_idx" ON "auth"."role_resource_relation" USING btree (
  "role_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table user
-- ----------------------------
ALTER TABLE "auth"."user" ADD CONSTRAINT "users_pkey" PRIMARY KEY ("user_id");
