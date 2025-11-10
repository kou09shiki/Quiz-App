import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class GATEExam extends JFrame {
    private List<Question> questions = new ArrayList<>();
    private int currentIndex = 0, correct = 0, wrong = 0, points = 0, maxTime;
    private JLabel questionLabel, timerLabel;
    private JButton[] optionButtons = new JButton[4];
    private javax.swing.Timer quizTimer;
    private int timeLeft;

    public GATEExam() {
        setTitle("GATE Exam");
        setSize(600, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        int n = Integer.parseInt(JOptionPane.showInputDialog("How many questions?"));
        maxTime = Integer.parseInt(JOptionPane.showInputDialog("Enter exam time in seconds:"));
        timeLeft = maxTime;

        for (int i = 0; i < n; i++) {
            String qText = JOptionPane.showInputDialog("Enter Question " + (i + 1) + ":");
            String[] opts = new String[4];
            for (int j = 0; j < 4; j++)
                opts[j] = JOptionPane.showInputDialog("Option " + (char)('A' + j) + " for Question " + (i + 1) + ":");
            int correctOpt = Integer.parseInt(JOptionPane.showInputDialog("Enter correct option (0-3) for Question " + (i + 1) + ":"));
            questions.add(new Question(qText, opts, correctOpt));
        }

        timerLabel = new JLabel("Time left: " + maxTime, JLabel.CENTER);
        timerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        timerLabel.setForeground(Color.MAGENTA);
        add(timerLabel, BorderLayout.NORTH);

        questionLabel = new JLabel("", JLabel.CENTER);
        questionLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        questionLabel.setForeground(Color.BLUE);
        add(questionLabel, BorderLayout.CENTER);

        JPanel optPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JButton();
            optionButtons[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
            optionButtons[i].setForeground(new Color((i + 1) * 50, 50, 150 - (i * 30)));
            int idx = i;
            optionButtons[i].addActionListener(e -> checkAnswer(idx));
            optPanel.add(optionButtons[i]);
        }
        add(optPanel, BorderLayout.SOUTH);

        displayQuestion();

        quizTimer = new javax.swing.Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time left: " + timeLeft);
            if (timeLeft <= 0) {
                endExam();
                quizTimer.stop();
            }
        });
        quizTimer.start();

        setVisible(true);
    }

    private void displayQuestion() {
        if (currentIndex >= questions.size()) {
            endExam();
            quizTimer.stop();
            return;
        }
        Question q = questions.get(currentIndex);
        questionLabel.setText(q.text);
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText((char) ('A' + i) + ": " + q.options[i]);
            optionButtons[i].setEnabled(true);
        }
    }

    private void checkAnswer(int idx) {
        Question q = questions.get(currentIndex);
        for (JButton btn : optionButtons)
            btn.setEnabled(false);

        if (idx == q.correctOption) {
            correct++;
            points += 10;
            questionLabel.setForeground(Color.GREEN.darker());
            questionLabel.setText("Correct!");
        } else {
            wrong++;
            questionLabel.setForeground(Color.RED);
            questionLabel.setText("Wrong!");
        }
        currentIndex++;
        new javax.swing.Timer(800, e -> {
            questionLabel.setForeground(Color.BLUE);
            displayQuestion();
            ((javax.swing.Timer) e.getSource()).stop();
        }).start();
    }

    private void endExam() {
        String msg;
        String lollipop = "\uD83C\uDF6D"; // 🍭 emoji
        if (correct == questions.size())
            msg = "<html><center><span style='color:green'><b>Excellent! Here's a lollipop " + lollipop + "</b></span></center></html>";
        else if (correct >= questions.size() * 0.7)
            msg = "<html><center><span style='color:orange'><b>Well done! (" + points + " points)</b></span></center></html>";
        else
            msg = "<html><center><span style='color:red'><b>Keep trying! (" + points + " points)</b></span></center></html>";

        double gateScore = ((double) points / (questions.size() * 10)) * 100;

        String report = "<html><center>" +
                "Total Questions: " + questions.size() + "<br>" +
                "<span style='color:green'>Correct: " + correct + "</span><br>" +
                "<span style='color:red'>Wrong: " + wrong + "</span><br>" +
                "Total Points: " + points + "<br>" +
                "<b>GATE Score: " + String.format("%.2f", gateScore) + "%</b><br>" +
                msg +
                "</center></html>";
        questionLabel.setText(report);
        for (JButton btn : optionButtons)
            btn.setVisible(false);
        timerLabel.setText("Exam Finished!");
    }

    static class Question {
        String text;
        String[] options;
        int correctOption;

        Question(String t, String[] o, int c) {
            text = t;
            options = o;
            correctOption = c;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GATEExam());
    }
}
