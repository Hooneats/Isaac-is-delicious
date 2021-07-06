import java.io.*;
import java.net.*;
import java.util.Date;

class IssacModule extends Thread{
	IssacServer iser;
	Socket s;
	InputStream is;
	OutputStream os;
	DataInputStream dis;
	DataOutputStream dos;
	
	String clientInfo="GUEST";
	//GUEST - 비로그인, CLIENT - 고객(일반손님), STORE - 매장(M)
	String id;
	
	IssacModule(IssacServer iser){
		this.iser = iser;
		this.s = iser.s;
		try {
			is = s.getInputStream();
            os = s.getOutputStream();
            dis = new DataInputStream(is);
            dos = new DataOutputStream(os);
        }catch(IOException ie){}
	}

	public void run() {
		listen();
	}
	void listen() {	//clients->server
		System.out.println(Thread.currentThread().getName());
		String line = "";
		try {
			while(true) {
				line = dis.readUTF();
				System.out.println(line);
				if(clientInfo.equals("GUEST")) {	//비로그인 상태
					if(line.contains("ULOGIN@#$")) {
						String loginResult = "";
						loginResult = new Login(this).login(line,"U");
						if(loginResult.equals("TRUE")){
							dos.writeUTF("LOGIN@#$%TRUE");	//로그인 성공 메시지
							dos.flush();
						}else if(loginResult.equals("FALSE")) {
							dos.writeUTF("LOGIN@#$%FALSE");	//로그인 실패 메시지
							dos.flush();
						}
					}else if(line.contains("MLOGIN@#$")) {
						String loginResult = "";
						loginResult = new Login(this).login(line, "M");
						if(loginResult.equals("TRUE")){
							dos.writeUTF("MLOGINS@#$%TRUE");
							dos.flush();
						}else if(loginResult.equals("FALSE")) {
							dos.writeUTF("MLOGINS@#$%FALSE");
							dos.flush();
						}
					}else if(line.contains("UREGISTER@#$")) {
						String registerResult = "";
						registerResult= new Register().register(line);
						if(registerResult.equals("TRUE")) {
							//서버UI에 띄우기
							iser.textArea1.setText(IssacSUI.stringBuffer.toString());
							iser.jscrollPane.getVerticalScrollBar().setValue(iser.jscrollPane.getVerticalScrollBar().getMaximum());

							dos.writeUTF("REGISTER@#$%TRUE");
							dos.flush();
						}else if(registerResult.equals("FALSE")) {
							dos.writeUTF("REGISTER@#$%FALSE");
							dos.flush();
						}
					}
				}else if(clientInfo.equals("CLIENT")) {	//고객 로그인 상태
					if(line.contains("UORDER@#$")) {
						Order ord = new Order(this);
						ord.sendOrder(line);
					} else if (line.contains("STORELIST@#$")) {
						String sendList="STORELIST@#$";
						int i = 0;
						for (String store : IssacServer.storeList) {
							sendList = sendList + "%" + store;
						}
						dos.writeUTF(sendList);
						dos.flush();

					} else if (line.contains("ULOGOUT@#$")) {
						int cnt = 0;
						for (String item : IssacServer.userID) {
							if (item.equals(id)) {
								IssacServer.userID.remove(cnt);

								//서버UI에 띄우기
								IssacSUI.stringBuffer.append(new Date()+" : "+item+"님이 로그아웃했습니다.\n");
								iser.textArea1.setText(IssacSUI.stringBuffer.toString());
								iser.jscrollPane.getVerticalScrollBar().setValue(iser.jscrollPane.getVerticalScrollBar().getMaximum());
								break;
							} else
								cnt++;
						}
						cnt=0;
						for(String[] items:IssacServer.userList){
							if(items[0].equals(id)){
								IssacServer.userList.remove(cnt);
								break;
							}else
								cnt++;
						}
						id = null;
						clientInfo = "GUEST";
						dos.writeUTF("ULOGOUT@#$");
						dos.flush();
					}
				}else if(clientInfo.equals("STORE")) {	//매장 로그인 상태
					if(line.contains("MTOUMSG@#$")) {
						StoreToUserMessage stm = new StoreToUserMessage(this);
						stm.sendMSG(line);
					}else if(line.contains("MLOGOUTS@#$")) {
						int cnt=0;
						for(String item: IssacServer.storeList) {
							if(item.equals(id)) {
								IssacServer.storeList.remove(cnt);

								//서버UI에 띄우기
								IssacSUI.stringBuffer.append(new Date()+" : "+item+"님이 로그아웃했습니다.\n");
								iser.textArea1.setText(IssacSUI.stringBuffer.toString());
								iser.jscrollPane.getVerticalScrollBar().setValue(iser.jscrollPane.getVerticalScrollBar().getMaximum());
								break;
							}else
								cnt++;
						}
						id=null;
						clientInfo="GUEST";
						dos.writeUTF("MLOGOUTS");
						dos.flush();
					}
				}
			}
		}catch(IOException ie) {
			//연결이 끊김
			int cnt=0;
			for(String item: IssacServer.userID) {
				if(item.equals(id)) {
					IssacServer.userID.remove(cnt);

					//서버UI에 띄우기
					IssacSUI.stringBuffer.append(new Date()+" : "+item+"님이 로그아웃했습니다.\n");
					iser.textArea1.setText(IssacSUI.stringBuffer.toString());
					iser.jscrollPane.getVerticalScrollBar().setValue(iser.jscrollPane.getVerticalScrollBar().getMaximum());
					break;
				}else
					cnt++;
			}
			cnt=0;
			for(String[] items:IssacServer.userList){
				if(items[0].equals(id)){
					IssacServer.userList.remove(cnt);
					break;
				}else
					cnt++;
			}
			cnt=0;
			for(String item: IssacServer.storeList) {
				if(item.equals(id)) {
					IssacServer.storeList.remove(cnt);

					//서버UI에 띄우기
					IssacSUI.stringBuffer.append(new Date()+" : "+item+"님이 로그아웃했습니다.\n");
					iser.textArea1.setText(IssacSUI.stringBuffer.toString());
					iser.jscrollPane.getVerticalScrollBar().setValue(iser.jscrollPane.getVerticalScrollBar().getMaximum());
					break;
				}else
					cnt++;
			}
			cnt=0;
			for(IssacModule item: iser.v) {
				if(item==this) {
					iser.v.remove(cnt);
					break;
				}else
					cnt++;
			}
			closeAll();
		}
	}
	
	void closeAll() {
		try {
			if(dis != null) dis.close();
			if(dos != null) dos.close();
			if(is != null) is.close();
			if(os != null) os.close();
			if(s != null) s.close();
			iser.jscrollPane.getVerticalScrollBar().setValue(iser.jscrollPane.getVerticalScrollBar().getMaximum());
			/*System.out.println(Thread.currentThread().getName()+"끝2");
			if(iser.executorService != null)iser.executorService.shutdown();*/
		}catch(IOException ie) {}
	}
}
