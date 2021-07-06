import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Stream;

class Register {
	String filePath = "."+File.separator+"client"+File.separator+"member.txt";

	Register(){}
	
	String register(String line) {
		int registerCommand = line.indexOf("%");
		String registerInfo = line.substring(registerCommand+1, line.length());
		String[] registerInfoList = registerInfo.split("%");
		try { // 등록정보 읽어서 중복 확인하기
			List<String> readFileMembers = Files.readAllLines(Paths.get(filePath), Charset.forName("UTF-8"));
			String subId = registerInfoList[0];
			String equalId = "";
			for (int i = 0; i<readFileMembers.size(); i++) {
				equalId = readFileMembers.get(i).substring(0, readFileMembers.get(i).indexOf("%"));
				if (equalId.equals(subId)) {
					return "FALSE";
				}
			}

			String saveInfo = registerInfo+"\n";


			// 새로운 등록정보 저장
			BufferedWriter writeInfo = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filePath),true),"UTF-8"));
			writeInfo.write(saveInfo);
			writeInfo.close();
			//서버UI에 띄우기
			IssacSUI.stringBuffer.append("\n"+new Date()+" : 새로운 회원가입 정보가 저장되었습니다.\n -> ");
			for (String info : registerInfoList) {
					IssacSUI.stringBuffer.append(info + " / ");
			}
			IssacSUI.stringBuffer.append("\n");

			
		}catch(IOException ie) {
			return "FALSE";
		}
		return "TRUE";
	}

	public static void main(String[] args) {
		new Register().register("UREGISTER%kdsfsgask%1234%나무%dsfsdf@naver.com%010-1111-1111%FDSFDS%SDFDSF");
	}
}
