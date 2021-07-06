import javax.swing.*;

public class IssacSUI extends JFrame{
    protected JPanel JPanel1;
    protected static StringBuffer stringBuffer = new StringBuffer();
    protected JTextArea textArea1;
    protected JRadioButton ONRadioButton;
    protected JRadioButton OFFRadioButton;
    protected JButton button1;
    protected JPanel JPanel2;
    protected JScrollPane jscrollPane;
    protected ButtonGroup ServerOnOff;

    public void IssacSUIStart() {

    }

    void setUi(){
        setContentPane(JPanel1);
        setSize(700, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        setTitle("서버관리자");
        setResizable(false);






    }

    public static void main(String[]args) {

    }

}
