跑测试时需要的sql
##柔性事务
-- 增加列
ALTER TABLE ds_0.t_order_0 ADD COLUMN not_existed_column INTEGER ;
ALTER TABLE ds_0.t_order_1 ADD COLUMN not_existed_column INTEGER ;
ALTER TABLE ds_1.t_order_0 ADD COLUMN not_existed_column INTEGER ;
ALTER TABLE ds_1.t_order_1 ADD COLUMN not_existed_column INTEGER ;

SELECT * FROM transaction_log

-- 删除列：
ALTER TABLE ds_0.t_order_0 DROP  COLUMN not_existed_column  ;
ALTER TABLE ds_0.t_order_1 DROP  COLUMN not_existed_column  ;
ALTER TABLE ds_1.t_order_0 DROP  COLUMN not_existed_column  ;
ALTER TABLE ds_1.t_order_1 DROP  COLUMN not_existed_column  ;
UPDATE ds_0.t_order_0 SET STATUS='INIT' WHERE order_id=1000;
UPDATE ds_1.t_order_0 SET STATUS='INIT' WHERE order_id=1100;
DELETE FROM transaction_log

SELECT * FROM ds_0.t_order_0;
SELECT * FROM ds_0.t_order_1;
SELECT * FROM ds_1.t_order_0;
SELECT * FROM ds_1.t_order_1;

SELECT * FROM trans_log.transaction_log t ;

##动态表
CREATE TABLE IF NOT EXISTS `ds_1`.`t_order_5` (`order_id` BIGINT NOT NULL, `user_id` INT NOT NULL, `status` VARCHAR(50), PRIMARY KEY (`order_id`));
SELECT * FROM `ds_1`.`t_order_5`;
DROP TABLE `ds_1`.`t_order_5`;
