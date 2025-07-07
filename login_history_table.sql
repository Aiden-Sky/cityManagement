-- 创建登录历史表
CREATE TABLE IF NOT EXISTS login_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account VARCHAR(50) NOT NULL COMMENT '账户名',
    loginTime DATETIME NOT NULL COMMENT '登录时间',
    ip VARCHAR(50) COMMENT 'IP地址',
    device VARCHAR(255) COMMENT '设备信息',
    status VARCHAR(20) COMMENT '状态(成功/失败)'
) COMMENT='登录历史记录表';

-- 添加索引
CREATE INDEX idx_account ON login_history(account);
CREATE INDEX idx_loginTime ON login_history(loginTime); 