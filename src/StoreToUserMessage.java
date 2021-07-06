import java.io.*;
import java.net.*;
import java.util.*;

class StoreToUserMessage {
	IssacModule im;
	StoreToUserMessage(IssacModule im){
		this.im = im;
	}
	void sendMSG(String line) {
		int valueIndex = line.indexOf("%");
		String msgValue = line.substring(valueIndex + 1,line.length());
		String[] msgContentList = msgValue.split("%");
		int cnt = 0;
		for(IssacModule module: im.iser.v) {
			if(module.id.equals(msgContentList[0])) {
				IssacModule target = im.iser.v.get(cnt);
				String sendMsg = "MMSG@#$%" + im.id + "%" + msgContentList[1];

				//서버UI에 띄우기
				IssacSUI.stringBuffer.append(im.id + msgContentList[1]);
				im.iser.textArea1.setText(IssacSUI.stringBuffer.toString());
				im.iser.jscrollPane.getVerticalScrollBar().setValue(im.iser.jscrollPane.getVerticalScrollBar().getMaximum());

				try {
					target.dos.writeUTF(sendMsg);
					target.dos.flush();
				}catch(IOException ie) {}
			}else
				cnt++;
		}
	}
}
