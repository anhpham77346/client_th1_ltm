import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeFrame extends JFrame {
    private JButton loginButton;

    public HomeFrame() {
        // Cấu hình cửa sổ
        setTitle("Trang Chủ");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        // Tạo nút Đăng nhập
        loginButton = new JButton("Đăng nhập");
        loginButton.addActionListener(new LoginButtonListener());

        // Thêm nút vào giao diện
        add(loginButton);

        // Hiển thị cửa sổ
        setVisible(true);
    }

    // Xử lý sự kiện khi bấm nút Đăng nhập
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Ẩn cửa sổ trang chủ và mở cửa sổ đăng nhập
            dispose(); // Đóng cửa sổ trang chủ
            new LoginFrame(); // Mở cửa sổ đăng nhập
        }
    }

    // Hàm main để chạy giao diện trang chủ
    public static void main(String[] args) {
        new HomeFrame();
    }
}
