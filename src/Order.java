import java.io.*;
import java.net.*;
import java.util.*;

class Order {
	IssacModule im;
	Vector<IssacModule> v;
	String sendString;
	
	Order(IssacModule im){
		this.im = im;
		v=im.iser.v;
	}
	
	void sendOrder(String line){
		System.out.println("들어왔나요?");
		int valueIndex = line.indexOf("%");
		String orderValue = line.substring(valueIndex+1,line.length());
		String[] orderContentList = orderValue.split("%");
		//id: 0, mname: 1, 배달/포장: :2, 총금액: 3, 주문리스트: 4 + 고객주소추가해야함

		for(String store: IssacServer.storeList) {

			if(store.equals(orderContentList[1])) {
				sendString = "UTOMORDER@#$%" + orderContentList[0] + "%"
													+ orderContentList[2] + "%"
													+ orderContentList[3] + "%"
													+ orderContentList[4];

				//서버UI에 띄우기
				IssacSUI.stringBuffer.append(new Date()+" : ID : "+orderContentList[0]+"님이 "+orderContentList[1]+"매장으로 "
										+orderContentList[2]+"주문을 하였습니다.\n"
										+"총금액 : "+orderContentList[3]+"주문리스트 : "+orderContentList[4]+"\n");
				im.iser.textArea1.setText(IssacSUI.stringBuffer.toString());
				im.iser.jscrollPane.getVerticalScrollBar().setValue(im.iser.jscrollPane.getVerticalScrollBar().getMaximum());

				for(String items[]: IssacServer.userList){
					if(items[0].equals(im.id)){
						sendString = sendString + "%" + items[5];
					}
				}
				try {
					for (IssacModule module : v) {
						if (module.id.equals(orderContentList[1])) {
							module.dos.writeUTF(sendString);
							module.dos.flush();
						}
					}
					im.dos.writeUTF("ORDER@#$%TRUE");
					im.dos.flush();
				}catch(IOException ie) {
					try {
						im.dos.writeUTF("ORDER@#$%FALSE");
						im.dos.flush();
					}catch(IOException ie2) {}
				}
			}
		}
	}
}
