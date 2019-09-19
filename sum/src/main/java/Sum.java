import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class Sum {

    public Sum() {
        JFrame frame = new JFrame("Sum");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int startX = dimension.width / 2 - 250;
        int startY = dimension.height / 2 -150;
        frame.setLocation(startX, startY);
        TextField input = new TextField();
        input.setColumns(15);
        TextField output = new TextField();
        output.setColumns(15);
        JPanel backGround = new JPanel();

        Button start = new Button("Start");
        start.addActionListener((event) -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(input.getText()));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(output.getText()))) {

                Set<String> set = new HashSet<>();

                reader.lines()
                        .map(s -> s.split(" "))
                        .forEach(strings -> {
                            for (String s : strings)
                                set.add(s);
                        });

                String sum = set.stream()
                        .map(s -> Integer.parseInt(s))
                        .reduce(0, (a, b) -> a + b)
                        .toString();

                writer.write(sum);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Box nameBox = new Box(BoxLayout.Y_AXIS);
        nameBox.add(new Label("input file path:"));
        nameBox.add(new Label("output file path:"));

        Box fieldBox = new Box(BoxLayout.Y_AXIS);
        fieldBox.add(input);
        fieldBox.add(output);

        backGround.add(BorderLayout.WEST, nameBox);
        backGround.add(BorderLayout.CENTER, fieldBox);
        backGround.add(BorderLayout.EAST, start);

        frame.add(backGround);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        new Sum();
    }
}

