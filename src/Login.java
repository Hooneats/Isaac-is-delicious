import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class Login {
	String cfilePath = "."+File.separator+"client"+File.separator+"member.txt";
	String sfilePath = "."+File.separator+"store"+File.separator+"member.txt";
	IssacModule im;
	
	Login(IssacModule im){
		this.im = im;
	}
	
	String login(String line,String who) {	//return TRUE - 로그인 성공, FALSE - 로그인 싫패
		int valueIndex = line.indexOf("%");
		String loginValue = line.substring(valueIndex + 1,line.length());
		
		try {
			String[] loginContentList;
			List<String> memberList;
			String id="";
			String pw="";
			String subLine="";
			if(who.equals("U")) {
				loginContentList = loginValue.split("%");

				memberList = Files.readAllLines(Paths.get(cfilePath), Charset.forName("UTF-8")); //회원 정보 읽기

				for(int i=0;i<memberList.size();i++) {
					id = memberList.get(i).substring(0,memberList.get(i).indexOf("%"));
					subLine = memberList.get(i).substring(memberList.get(i).indexOf("%")+1,memberList.get(i).length());
					pw= subLine.substring(0,subLine.indexOf("%"));

					if(id.equals(loginContentList[0]) &&
					   pw.equals(loginContentList[1])) {
						IssacServer.userID.add(loginContentList[0]);

						//서버UI에 띄우기
						IssacSUI.stringBuffer.append(new Date()+" : "+loginContentList[0]+"님이 로그인하였습니다.\n");
						im.iser.textArea1.setText(IssacSUI.stringBuffer.toString());
						im.iser.jscrollPane.getVerticalScrollBar().setValue(im.iser.jscrollPane.getVerticalScrollBar().getMaximum());

						im.id = loginContentList[0];
						String[] loginM = memberList.get(i).split("%");
						IssacServer.userList.add(loginM);
						im.clientInfo = "CLIENT";
						return "TRUE";
					}
				}
			}else if(who.equals("M")) {
				loginContentList = loginValue.split("%");

				memberList = Files.readAllLines(Paths.get(sfilePath), Charset.forName("UTF-8"));


				for(int i=0;i<memberList.size();i++) {
					id = memberList.get(i).substring(0,memberList.get(i).indexOf("%"));

					subLine = memberList.get(i).substring(memberList.get(i).indexOf("%")+1,memberList.get(i).length());

					pw= subLine;


					if(id.equals(loginContentList[0]) &&
					   pw.equals(loginContentList[1])) {
						IssacServer.storeList.add(loginContentList[0]);

						//서버UI에 띄우기
						IssacSUI.stringBuffer.append(new Date()+" : "+loginContentList[0]+"님이 로그인하였습니다.\n");
						im.iser.textArea1.setText(IssacSUI.stringBuffer.toString());
						im.iser.jscrollPane.getVerticalScrollBar().setValue(im.iser.jscrollPane.getVerticalScrollBar().getMaximum());

						im.id = loginContentList[0];
						im.clientInfo = "STORE";
						return "TRUE";
					}
				}
			}
		}catch(IOException ie) {
			System.out.println("예외발생");
			return "FALSE";
		}
		return "FALSE";
	}
}
