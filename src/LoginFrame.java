
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import model.User;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public LoginFrame() {
        // Cấu hình cửa sổ
        setTitle("Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));

        // Tạo thành phần giao diện
        JPanel panel1 = new JPanel();
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(15);
        panel1.add(emailLabel);
        panel1.add(emailField);

        JPanel panel2 = new JPanel();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        panel2.add(passwordLabel);
        panel2.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginButtonListener());

        messageLabel = new JLabel("", SwingConstants.CENTER);

        // Thêm các thành phần vào giao diện
        add(panel1);
        add(panel2);
        add(loginButton);
        add(messageLabel);

        // Hiển thị cửa sổ
        setVisible(true);
    }

    private class LoginButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            // Bước 1: Kết nối tới server và kiểm tra đăng nhập
            boolean loginSuccessful = checkLogin(email, password);

            if (loginSuccessful) {
                messageLabel.setText("Login successful!");

                // Ẩn giao diện Login và chuyển sang giao diện danh sách người dùng
                dispose(); // Đóng giao diện LoginFrame

                // Bước 2: Mở giao diện UserListFrame với danh sách người dùng nhận được
                new UserListFrame();
            } else {
                messageLabel.setText("Invalid email or password!");
            }
        }

        // Hàm để kết nối tới server và kiểm tra đăng nhập
        private boolean checkLogin(String email, String password) {
            boolean success = false;
            try {
                // Kết nối tới server (thay 'localhost' và 12345 bằng địa chỉ và cổng của server thực tế)
                Socket socket = new Socket("localhost", 12345);

                // Tạo luồng gửi và nhận dữ liệu với server
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Gửi yêu cầu đăng nhập tới server
                out.println("login");
                out.println(email);     // Gửi email
                out.println(password);  // Gửi password

                // Nhận kết quả đăng nhập từ server
                String response = in.readLine();
                if (response.equals("Login successful!")) {
                    success = true;
                }

                // Đóng kết nối sau khi nhận dữ liệu
                socket.close();

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi kết nối đến server.");
            }
            return success;
        }

    }
}
