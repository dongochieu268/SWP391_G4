-- Tài khoản Admin demo cho UC09 (chạy sau script tạo bảng)
USE mentora_db1;
GO

IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@mentora.edu')
BEGIN
    INSERT INTO users (full_name, email, password, role_id, status, email_verified)
    VALUES (N'Quản trị viên', 'admin@mentora.edu', 'admin123', 1, 'ACTIVE', 1);
END
GO

-- Dữ liệu học kỳ mẫu (tùy chọn)
IF NOT EXISTS (SELECT 1 FROM semesters WHERE name = N'Học kỳ 1 - 2025-2026')
BEGIN
    INSERT INTO semesters (name, start_date, end_date, status)
    VALUES (N'Học kỳ 1 - 2025-2026', '2025-09-01', '2026-01-15', 'ACTIVE');
END
GO
