package com.example.demo;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.analyzer.AdaAnalyzer;
import com.example.demo.analyzer.AnalyzerDatabase;
import com.example.demo.security.dao.PersonRepository;
import com.example.demo.security.entity.Person;
import com.example.demo.security.service.OdmPersonRepo;
import com.example.demo.service.ProcedureService;
import com.example.demo.util.Encode;
import com.example.demo.util.UUIDUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DashflatApplicationTests {
	@Autowired
	private ProcedureService procedureService;

	@Test
	public void contextLoads() {
//		String str="this is 'Tom' and 'Eric'， this is 'Bruce lee', he is a chinese, name is '李小龙'。";
//		Pattern p=Pattern.compile("'(.*?)'");
//		Matcher m=p.matcher(str);
//		int i=0;
//		while(m.find())
//		{
//			str=str.replace(m.group(),""+(i++));
//			System.out.println(m.group());
//		}
//		System.out.println(str);
//		JSONArray jsonColumns = JSONArray.parseArray("[\"id\",\"name\",\"longName\",\"fullName\",\"type\",\"version\",\"hashValue\",\"returnType\",\"lineCount\",\"parentId\",\"creatTime\",\"updateTime\"]");
//		System.out.println(jsonColumns);
//
//		String text = "Statement (TER1) = 100 %";
//		text = text.substring(text.lastIndexOf("=")+1, text.lastIndexOf("%")).trim();
//
//		String text2 = "xxt";
//		String[] paramChild = text2.split(",");
//		int num = paramChild.length;
	}
//	@Test
//	public void encoder() {
//		String password = "123456";
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
//		String enPassword = encoder.encode(password);
//		System.out.println(enPassword);
//
//		Calendar now = Calendar.getInstance();
//		String reportName = File.separator+"report_"+now.get(Calendar.YEAR)+"_"+(now.get(Calendar.MONTH)+1)+"_"+now.get(Calendar.DAY_OF_MONTH)+".docx";
//
//		String intS = "23";
//		for(int i=0; i<intS.length(); i++){
//			System.out.println(intS.charAt(i));
//			if(intS.charAt(i)==50){ //1的assic码为49
//				System.out.println(intS.charAt(i));
//			}
//		}
//
//		String uuid = UUIDUtils.getUUID(40);
//
//		String path = "C/doc/ghu/op/ffg/gyu.txt";
//		String base ="C/doc/ghu";
//		String baoPath = path.replace(base,"");
//		baoPath = baoPath.substring(0,baoPath.lastIndexOf(File.separator));
//		String baoName = baoPath.replace(File.separator,".");
//		if(baoName.startsWith(".")){
//			baoName = baoName.substring(1,baoName.length());
//		}
//		System.out.println(baoName);
//	}

//	@Test
//	public void findResultFromHtml() throws IOException {
//		List<Map<String, Object>> result = new ArrayList<>();
//		File input = new File("C:/auto_docx/upload/bac51473-9509-45a8-9409-0ebe71d56532/bsp-onchipbus_76.dyn.htm");
//		Document doc = Jsoup.parse(input, "UTF-8");
//		Elements tr = doc.select("h4:has(a[name='procedure_table'])+br+br+br+center table table tr:has(td)");
////		Elements tt9 = doc.select("tt:contains(Procedure)");
////		Elements table6 = tt9.parents().parents().parents();
////		Elements cc = center.next().next().next();
////		Elements tables = doc.select("table table");
////		Element table = null;
////		lable2:
////		for(int i=0; i<tables.size(); i++){
////			Elements tr = tables.get(i).select("tr");
////			for(int j=0; j<tr.size(); j++){
////				Elements th = tr.get(j).select("th");
////				for(int k=0; k<th.size(); k++){
////					Elements tt = th.get(k).select("tt");
////					for(int l=0; l<tt.size(); l++){
////						Element element = tt.get(l);
////						String text = element.text();
////						System.out.println(text);
////						if(text.trim().equals("Procedure")){
////							table = tables.get(i);
////							break lable2;
////						}
////					}
////				}
////			}
////		}
//		for(int i=0; i<tr.size(); i++){
//			Elements td = tr.get(i).select("td");
//			Map<String, Object> tableValue = new HashMap<>();
//			for(int j=0; j<td.size(); j++) {
//				if (j == 0) {
//					Element element = td.get(j);
//					String text = element.text().trim();
//					tableValue.put("name", text);
//				}else if(j == 2) {
//					Element element = td.get(j);
//					String text = element.text().trim();
//					tableValue.put("statement", text);
//				}else if(j == 4) {
//					Element element = td.get(j);
//					String text = element.text().trim();
//					tableValue.put("branch", text);
//				}
//			}
//			result.add(tableValue);
//		}
//		System.out.println(result);
//	}
//
//	@Test
//	public void test(){
//		String name = "branch_aspRead";
//		String res = name.substring(name.lastIndexOf("_")+1, name.length());
//		System.out.println(res);
//	}
//
//	@Test
//	public void testAdaAnalyzer() {
//		AdaAnalyzer.PRETREATMENT_DIR = "C://auto_docx//pretreatment//";
//		AnalyzerDatabase db = AdaAnalyzer.analyzeAdaSource("C://auto_docx//code//"+"bsp"+"//");
//		AdaAnalyzer.WriteResult(db, AdaAnalyzer.PRETREATMENT_DIR + "WordsOutput.txt");
//
//		Integer projectId = 7;
//		String version = "3ea7240f5e1a10e902aca3368de9c7fc4ca4c6c1";
//		procedureService.updateProcedureDatabase(projectId, version, db);
//	}

//	@Autowired
//	private PersonRepository personRepository;
//	@Autowired
//    private OdmPersonRepo odmPersonRepo;
//	@Test
//	public void findAll() throws Exception {
////		Iterable<Person> list = personRepository.findAll();
////		list.forEach(p -> {
////			System.out.println(p);
////		});
////        List<Person> list = odmPersonRepo.findAll();
//        Person p = odmPersonRepo.findByCn("didi");
//        StringBuilder password = new StringBuilder();
//        String temp = p.getUserPassword();
//        String[] psList = temp.split(",");
//        if(psList != null && psList.length > 0){
//            for(int i=0; i<psList.length; i++){
//                char c = (char) Integer.valueOf(psList[i]).intValue();
//                password.append(c);
//            }
//        }
//        System.out.println(password);
//	}
//
//	@Test
//    public void ty(){
//        String inputPassword = "123456";
//        LdapShaPasswordEncoder en = new LdapShaPasswordEncoder();
//        inputPassword = en.encode(inputPassword);
//        System.out.println(inputPassword);
//    }

//    @Test
//    public void tioy(){
//        String a = "123456789";
//        String b = Encode.encode(a);
//        String c = Encode.decode(b);
//        System.out.println(b);
//        System.out.println(c);
//    }
}
