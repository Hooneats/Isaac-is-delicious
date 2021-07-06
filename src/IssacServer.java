import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

class IssacServer extends IssacSUI implements Runnable{
	ExecutorService executorService = Executors.newCachedThreadPool();
	ServerSocket ss;
	Socket s;
	int port = 5000;
	Vector<IssacModule> v = new Vector<IssacModule>();
	IssacModule im;
	static Vector<String[]> userList = new Vector<String[]>();
	static Vector<String> userID = new Vector<String>();
	static Vector<String> storeList = new Vector<String>();


	IssacServer(){
		setUi();
		ONRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Runnable Srn = new Runnable() {
					@Override
					public void run() {
						IssacServerStart();
					}
				};
				executorService.submit(Srn);
			}
		});
		OFFRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (!ONRadioButton.isSelected()) {
						IssacSUI.stringBuffer.append(new Date()+" : "+"서버를 종료하였습니다.\n");
						textArea1.setText(IssacSUI.stringBuffer.toString());
						jscrollPane.getVerticalScrollBar().setValue(jscrollPane.getVerticalScrollBar().getMaximum());
					}

					if (s != null) s.close();
					if (ss != null) ss.close();

				} catch (IOException exception) {
				}
			}
		});
	}

	void IssacServerStart(){
		try {
			
			ss = new ServerSocket(port);
			pln(port + "번 포트에서 멀티서버 대기중...");
			IssacSUI.stringBuffer.append(new Date()+" : "+port + "번 포트에서 멀티서버 대기중...\n");
			textArea1.setText(IssacSUI.stringBuffer.toString());
			jscrollPane.getVerticalScrollBar().setValue(jscrollPane.getVerticalScrollBar().getMaximum());

//			executorService.submit(this);
			
			while(true) {	//연결 대기
				s = ss.accept();
				im = new IssacModule(this);
				v.add(im);
				executorService.submit(im);
			}
		}catch(IOException ie) {
			if (ONRadioButton.isSelected()) {
				OFFRadioButton.setSelected(true);
				IssacSUI.stringBuffer.append(new Date()+" : "+"서버를 종료하였습니다.\n");
				textArea1.setText(IssacSUI.stringBuffer.toString());
				jscrollPane.getVerticalScrollBar().setValue(jscrollPane.getVerticalScrollBar().getMaximum());
			}

			try {
				if(ss != null) ss.close();
				for (IssacModule rm : v) {
					if(rm.s != null) rm.s.close();
				}

			}catch(IOException ide) {}

		}finally {
			try {
				if(ss != null) ss.close();
				jscrollPane.getVerticalScrollBar().setValue(jscrollPane.getVerticalScrollBar().getMaximum());
			}catch(IOException ie) {}
		}
	}

	public void run() {
		speak();	//미구현
	}
	void speak() {	//server->clients
		while(true) {
			
		}
	}
	
	void pln(String str) {System.out.println(str);}
	void p(String str) {System.out.println(str);}
	
	public static void main(String[] args) {
		new IssacServer();
	}
}
