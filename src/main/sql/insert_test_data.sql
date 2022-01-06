INSERT IGNORE INTO students VALUES ('2020080176036', 'huangke', '黄可');
INSERT IGNORE INTO administrators VALUES ('guoxun', 'guoxun', '郭迅');
INSERT IGNORE INTO courses VALUES (0, '操作系统', '本课程主要介绍了操作系统的概论、进程管理、调度与死锁、存储器管理、设备管理、文件管理、操作系统安全与保护等内容。');
INSERT IGNORE INTO lessons VALUES (0, 0, '秦科', 0, 0, 1, 'A101');
INSERT IGNORE INTO lessons VALUES (0, 2, '秦科', 0, 2, 3, 'A101');
-- 这条注释掉用于演示选课效果
--INSERT IGNORE INTO course_selections VALUES('2020080176036', 0, 0);
