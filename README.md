# CRUDFirestore

Một ứng dụng Android đơn giản minh họa các thao tác CRUD (Thêm, Xem, Sửa, Xóa) sử dụng Firebase Firestore và Jetpack Compose.

## Tính năng
- **Thêm người dùng**: Lưu thông tin tên và email vào Firestore.
- **Xem danh sách**: Hiển thị danh sách người dùng được cập nhật thời gian thực từ Firestore.
- **Cập nhật**: Chỉnh sửa thông tin người dùng đã tồn tại.
- **Xóa**: Xóa người dùng khỏi cơ sở dữ liệu.

## Công nghệ sử dụng
- **Ngôn ngữ**: Kotlin
- **UI Framework**: Jetpack Compose
- **Cơ sở dữ liệu**: Firebase Firestore
- **Kiến trúc**: Material Design 3

## Hướng dẫn thiết lập
1. Sao chép dự án này về máy.
2. Tạo một dự án mới trên [Firebase Console](https://console.firebase.google.com/).
3. Thêm ứng dụng Android vào dự án Firebase với package name: `com.example.crudfirestore`.
4. Tải file `google-services.json` và đặt vào thư mục `app/`.
5. Bật **Firestore Database** trong bảng điều khiển Firebase và thiết lập quy tắc bảo mật (Rules) cho phép đọc/ghi.
6. Chạy ứng dụng trên trình giả lập hoặc thiết bị thật.

---
Dự án này được tạo ra nhằm mục đích học tập về tích hợp Firebase trong Jetpack Compose.
