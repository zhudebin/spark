-- This file is automatically generated by LogicalPlanToSQLSuite.
SELECT * FROM t0 UNION SELECT * FROM t0
--------------------------------------------------------------------------------
SELECT `gen_attr` AS `id` FROM ((SELECT `gen_attr` FROM (SELECT `id` AS `gen_attr` FROM `default`.`t0`) AS gen_subquery_0) UNION DISTINCT (SELECT `gen_attr` FROM (SELECT `id` AS `gen_attr` FROM `default`.`t0`) AS gen_subquery_1)) AS t0
