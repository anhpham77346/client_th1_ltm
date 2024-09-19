import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import model.User;

public class UserListFrame extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton backButton;

    public UserListFrame() {
        // Cấu hình cửa sổ
        setTitle("User List");
        setSize(500, 350);  // Tăng chiều cao để có không gian cho nút "Trở về"
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tạo bảng dữ liệu cho các User
        String[] columnNames = {"Fullname", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);

        // Thêm bảng vào giao diện cuộn
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Tạo nút "Trở về Trang chủ"
        backButton = new JButton("Trở về Trang chủ");
        backButton.addActionListener(new BackButtonListener());
        
        // Thêm nút vào cuối giao diện
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Hiển thị cửa sổ
        setVisible(true);

        // Lấy danh sách người dùng từ server và tải vào bảng
        loadUsersFromServer();
    }

    // Hàm để lấy danh sách người dùng từ server
    private void loadUsersFromServer() {
        List<User> users = new ArrayList<>();
        try {
            // Kết nối tới server (thay 'localhost' và 12345 bằng địa chỉ và cổng của server thực tế)
            Socket socket = new Socket("localhost", 12345);

            // Tạo luồng gửi và nhận dữ liệu với server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Gửi yêu cầu lấy danh sách người dùng
            out.println("listUsers");

            // Nhận dữ liệu từ server và lưu vào danh sách người dùng
            String line;
            while (!(line = in.readLine()).equals("End of user list")) {
                // Giả sử server trả về tên người dùng và email trên cùng một dòng, cách nhau bởi dấu "-"
                String[] userData = line.split(" - ");
                if (userData.length == 2) {
                    String fullname = userData[0].replace("User: ", "");
                    String email = userData[1].replace("Email: ", "");
                    users.add(new User(fullname, email));
                }
            }

            // Đóng kết nối sau khi nhận dữ liệu
            socket.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối đến server.");
        }

        // Tải dữ liệu User vào bảng
        loadUsers(users);
    }

    // Hàm tải dữ liệu người dùng vào bảng
    private void loadUsers(List<User> users) {
        for (User user : users) {
            Object[] rowData = {user.getFullname(), user.getEmail()};
            tableModel.addRow(rowData);
        }
    }

    // Xử lý khi nhấn nút "Trở về Trang chủ"
    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Đóng giao diện danh sách người dùng
            dispose();
            // Mở lại giao diện trang chủ
            new HomeFrame(); // Giả sử bạn đã có lớp HomeFrame cho giao diện trang chủ
        }
    }
}
