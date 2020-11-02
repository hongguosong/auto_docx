package com.example.demo.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * Ada代码分析器
 */
public class AdaAnalyzer {
	private static Logger LOGGER = LoggerFactory.getLogger(AdaAnalyzer.class);
	/**
	 * flag: 0:正常字符; 1:注释符 ; 2:双引号;
	 */
	public static final int FLAG_NORMAL = 0;
	public static final int FLAG_COMMENTS = 1;
	public static final int FLAG_DOUBLE_QUOT = 2;

	/**
	 * 0:正常字符; 1:空格; 2:运算符或界符;
	 */
	public static final int CHAR_TYPE_NORMAL = 0;
	public static final int CHAR_TYPE_BLANK = 1;
	public static final int CHAR_TYPE_OPERATOR_OR_DELIMITER = 2;

	public static String PRETREATMENT_DIR = "file\\";

	/**
	 * 运算符
	 */
	private static String[] operators = { "+", "-", "*", "/", "<", "<=", ">",
			">=", "=", "==", "!=", "^", "&", "&&", "|", "||", "%", "<<", ">>",
			"!", "?", ":", "**", "=>", ":=", "/=", "<>"};
	/**
	 * 界符
	 */
	private static String[] delimiters = { ";", "(", ")", ",", "[", "]", "{",
			"}" };

	public static boolean isDelimiter(int current) {
		for (int i = 0; i < delimiters.length; i++) {
			if (current == delimiters[i].charAt(0)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isOperatorStart(int current){
		for (int i = 0; i < operators.length; i++) {
			if (current == operators[i].charAt(0)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isOperatorEnd(int pre, int current){
		for (int i = 0; i < operators.length; i++) {
			if(operators[i].length() == 2) {
				if (pre == operators[i].charAt(0) &&
						(current == operators[i].charAt(1))) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isOperatorOrDelimiter(int pre, int current) {
		for (int i = 0; i < operators.length; i++) {
			if (operators[i].length() == 1) {
				if (current == operators[i].charAt(0)) {
					return true;
				}
			} else if (operators[i].length() == 2) {
				if ((pre == operators[i].charAt(0))
						&& (current == operators[i].charAt(1))) {
					return true;
				}
			}
		}

		for (int i = 0; i < delimiters.length; i++) {
			if (current == delimiters[i].charAt(0)) {
				return true;
			}
		}

		return false;
	}

	private static boolean isBlankChar(char ch) {
		if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n') {
			return true;
		} else {
			return false;
		}
	}

	private static int getCurrentCharType(int pre, int current) {
		if (isBlankChar((char) current)) {
			return 1;
		}
		if (isOperatorOrDelimiter(pre, current)) {
			return 2;
		}

		return 0;
	}

	/**
	 * 获取文件当前字符类型
	 */
	private static int getFilePointerCharType(RandomAccessFile fos) {
		try {
			long pointer = fos.getFilePointer();

			int pre = 0;
			int current = 0;
			if (pointer - 2 >= 0) {
				fos.seek(pointer - 2);
				pre = fos.read();
			}
			if (pointer - 1 >= 0) {
				fos.seek(pointer - 1);
				current = fos.read();
			}
			fos.seek(pointer);

			return getCurrentCharType(pre, current);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return 0;
		}

	}

	/**
	 * 文件预处理
	 * @param fileName 源文件
	 * @param targetFileName 目标文件
	 * @param commentCache 注释缓存
	 */
	public static void pretreatment(String fileName, String targetFileName, CommentCache commentCache, LineFeedCache lineFeedCache) {
		try {
			File sourceFile = new File(fileName);
			if (!sourceFile.exists()) {
				System.out.println("文件不存在");
				return;
			}
			FileInputStream fis = new FileInputStream(sourceFile);
			File targetFile = new File(targetFileName);
			RandomAccessFile fos = new RandomAccessFile(targetFile, "rw");

			int pre = 0;
			int current = 0;
			int preCharType = CHAR_TYPE_NORMAL;
			int currentCharType = CHAR_TYPE_NORMAL;
			int flag = FLAG_NORMAL;

			List<Byte> byteList = new ArrayList<Byte>();
			Integer commentPos = -1;
			while ((current = fis.read()) != -1) {
				currentCharType = getCurrentCharType(pre, current);
				switch (flag) {
					case FLAG_COMMENTS:
						if (current == '\n') {
							if (preCharType != CHAR_TYPE_BLANK) {
								fos.write(' ');
								preCharType = CHAR_TYPE_BLANK;
							}
							commentPos = (int)fos.getFilePointer();
							byteList.add((byte)current);
							byte[] bytes = new byte[byteList.size()];
							for(int i=0;i<byteList.size();i++){
								bytes[i]=byteList.get(i);
							}
							commentCache.addComment(commentPos, new String(bytes, "UTF-8"));
							flag = FLAG_NORMAL;
						}
						else{
							byteList.add((byte)current);
						}
						break;
					case FLAG_DOUBLE_QUOT:
						fos.write((char) current);
						preCharType = currentCharType;
						if (current == '\"') {
							flag = FLAG_NORMAL;
						}
						break;
					default:
						if (preCharType == CHAR_TYPE_BLANK) {
							if (currentCharType != CHAR_TYPE_BLANK) {
								if (currentCharType == CHAR_TYPE_OPERATOR_OR_DELIMITER) {
									fos.seek(fos.getFilePointer() - 1);// 回移1位
								}
								fos.write((char) current);
								preCharType = currentCharType;
							}
						} else {
							if (currentCharType != CHAR_TYPE_BLANK) {
								fos.write((char) current);
								preCharType = currentCharType;
							} else {
								if (preCharType != CHAR_TYPE_OPERATOR_OR_DELIMITER) {
									fos.write(' ');
									preCharType = CHAR_TYPE_BLANK;
								}
							}
						}

						if (pre == '-' && current == '-') {
							// 检索到注释
							flag = FLAG_COMMENTS;
							fos.seek(fos.getFilePointer() - 2);// 回移2位
							preCharType = getFilePointerCharType(fos);
							byteList.clear();
						} else if (current == '\"') {
							// 检索到双引号(")
							flag = FLAG_DOUBLE_QUOT;
						}

						if(current == '\n'){
							lineFeedCache.addLineFeed((int) fos.getFilePointer()+1);
						}
						break;
				}
				pre = current;
			}

			commentCache.addFileLength((int)fos.getFilePointer());
			lineFeedCache.addFileLength((int)fos.getFilePointer());
			fos.setLength(fos.getFilePointer());
			fis.close();
			fos.close();
			System.out.println("源文件预处理成功:" + fileName + "->" + targetFileName);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * 分析Ada代码
	 * @param sourceForder 源文件目录
	 */
	public static AnalyzerDatabase analyzeAdaSource(String sourceForder){
		List<String> adaFiles = scanAdaFiles(sourceForder, ".adb");
		List<String> adsFiles = scanAdaFiles(sourceForder, ".ads");
		List<String> tempFiles = new ArrayList<String>();

		File directory = new File(PRETREATMENT_DIR);
		if(!directory.exists()){
			directory.mkdirs();
		}

		String fileName = null;
		CommentCache adsCommentCache = new CommentCache();
		LineFeedCache adsLineFeedCache = new LineFeedCache();
		for(int i=0;i<adsFiles.size();i++){
			fileName = PRETREATMENT_DIR + "file_" + i + ".txt";
			pretreatment(adsFiles.get(i), fileName, adsCommentCache, adsLineFeedCache);
			tempFiles.add(fileName);
		}

		List<String> withList = new ArrayList<>();
		List<String> useList = new ArrayList<>();
		AnalyzerDatabase db = new AnalyzerDatabase();

		WordAnalyzer wordAnalyzer = null;
		GlobalVarCache globalVarCache = new GlobalVarCache();
		for(int i=0;i<tempFiles.size();i++){
			wordAnalyzer = WordAnalyzer.create(tempFiles.get(i));
			if (wordAnalyzer != null){
				GlobalVarAnalyze(withList, useList, globalVarCache, wordAnalyzer);
			}
		}


		fileName = null;
		tempFiles.clear();
		CommentCache commentCache = new CommentCache();
		LineFeedCache lineFeedCache = new LineFeedCache();
		for(int i=0;i<adaFiles.size();i++){
			fileName = PRETREATMENT_DIR + "file_" + i + ".txt";
			pretreatment(adaFiles.get(i), fileName, commentCache, lineFeedCache);
			tempFiles.add(fileName);
		}

		for(int i=0;i<tempFiles.size();i++){
			wordAnalyzer = WordAnalyzer.create(tempFiles.get(i));
			if (wordAnalyzer != null){
				DeclareAnalyze(db, wordAnalyzer);
			}
		}

		for(String with: withList){
			db.addWith(with);
		}
		for(String use: useList){
			db.addUse(use);
		}

		// 源文件中的函数注释
//		Map<Integer, StringBuilder> commentMap = commentCache.getCommentMap();
//		for(Integer pos:commentMap.keySet()){
//			db.addComment(commentMap.get(pos).toString(), pos);
//		}

		// 源文件中的函数行数
		Map<Integer, Integer> lineFeedMap = lineFeedCache.getLineFeedMap();
		for(Integer pos:lineFeedMap.keySet()){
			db.addProcLines(lineFeedMap.get(pos), pos);
		}

		Map<String, List<String>> varMap = globalVarCache.getVarMap();
		for(String procKey:varMap.keySet()){
			for (String varStr : varMap.get(procKey)) {
				db.updateProcVar(procKey, varStr);
			}
		}

		for(int i=0;i<tempFiles.size();i++){
			wordAnalyzer = WordAnalyzer.create(tempFiles.get(i));
			if (wordAnalyzer != null){
				CallAnalyze(db, wordAnalyzer);
			}
		}

		// WriteResult(db, PRETREATMENT_DIR + "WordsOutput.txt");
		return db;
	}

	/**
	 * 扫描Ada源文件(*.adb)
	 * @param folderPath 源文件目录
	 * @return
	 */
	public static List<String> scanAdaFiles(String folderPath, String ext){
		File directory = new File(folderPath);
		ArrayList<String> scanFiles = new ArrayList<String>();
		if (directory.isDirectory()) {
			File[] filelist = directory.listFiles();
			for (int i = 0; i < filelist.length; i++) {
				if (filelist[i].isDirectory()) {
					scanFiles.addAll(scanAdaFiles(filelist[i].getAbsolutePath(), ext));
				}
				else {
					if(filelist[i].getName().toLowerCase().endsWith(ext)){ // Ada源文件
						scanFiles.add(filelist[i].getAbsolutePath());
					}
				}
			}
		}
		return scanFiles;
	}

	/**
	 * 分析结果写入文件
	 * @param db 分析结果
	 * @param targetFileName 目标文件
	 */
	public static void WriteResult(AnalyzerDatabase db, String targetFileName) {
		try {
			File targetFile = new File(targetFileName);
			RandomAccessFile fos = new RandomAccessFile(targetFile, "rw");
			fos.write(db.toResultString().getBytes("UTF-8"));
			fos.setLength(fos.getFilePointer());
			fos.close();
			System.out.println("文件分析成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * 全局变量定义分析
	 */
	public static void GlobalVarAnalyze(List<String> withList, List<String> useList, GlobalVarCache cache,
									  WordAnalyzer wordAnalyzer) {
		try {
			String word = null;
			int start;
			int end;
			String wordContentString;
			Stack<LexicalElement> procStack = new Stack<LexicalElement>();
			LexicalElement currentProc = null;
			String procName = null;
			String paramStr = null;
			String returnType = null;
			int declareStart = -1;
			wordAnalyzer.reset();
			while ((word = wordAnalyzer.getNextWord()) != null) {
				switch (word.toLowerCase()) {
					case "with":
						start = wordAnalyzer.getCIndex();
						while (!";".equals((word = wordAnalyzer.getNextWord()).toLowerCase())) {

						}
						end = wordAnalyzer.getCIndex() - ";".length();
						wordContentString = wordAnalyzer.getSubString(start, end);
						String[] withPkgs = wordContentString.split(",");
						for (String pkg : withPkgs) {
							if (pkg != null && !pkg.trim().isEmpty()) {
								withList.add(pkg.trim());
							}
						}
						break;
					case "use":
						start = wordAnalyzer.getCIndex();
						while (!";".equals((word = wordAnalyzer.getNextWord()))) {

						}
						end = wordAnalyzer.getCIndex() - ";".length();
						wordContentString = wordAnalyzer.getSubString(start, end);
						String[] usePkgs = wordContentString.split(",");
						for (String pkg : usePkgs) {
							if (pkg != null && !pkg.trim().isEmpty()) {
								useList.add(pkg.trim());
							}
						}
						break;
					case "package":
						start = wordAnalyzer.getCIndex() - word.length();
						word = wordAnalyzer.getNextWord();
						procName = word;
						paramStr = null;
						word = wordAnalyzer.getNextWord();
						if(word.toLowerCase().equals("is")){
							declareStart = wordAnalyzer.getCIndex();
							if(currentProc == null) {
								procStack.push(currentProc);
								currentProc = new LexicalElement(procName, LexicalElement.TYPE_PACKAGE);
							}
						}
						break;
					case "function":
						start = wordAnalyzer.getCIndex() - word.length();
						word = wordAnalyzer.getNextWord();
						procName = word;
						paramStr = null;
						word = wordAnalyzer.getNextWord();
						if(word.toLowerCase().equals("(")){
							start = wordAnalyzer.getCIndex();
							while (!")".equals((word = wordAnalyzer.getNextWord()).toLowerCase())) {

							}
							end = wordAnalyzer.getCIndex() - ")".length();
							paramStr = wordAnalyzer.getSubString(start, end);
							word = wordAnalyzer.getNextWord();
						}
						returnType = null;
						if(word.toLowerCase().equals("return")){
							word = wordAnalyzer.getNextWord();
							returnType = word;
							word = wordAnalyzer.getNextWord();
						}
						if(word.toLowerCase().equals(";")){
							declareStart = wordAnalyzer.getCIndex();
						}
						break;
					case "procedure":
						start = wordAnalyzer.getCIndex() - word.length();
						word = wordAnalyzer.getNextWord();
						procName = word;
						paramStr = null;
						word = wordAnalyzer.getNextWord();
						if(word.toLowerCase().equals("(")){
							start = wordAnalyzer.getCIndex();
							while (!")".equals((word = wordAnalyzer.getNextWord()).toLowerCase())) {

							}
							end = wordAnalyzer.getCIndex() - ")".length();
							paramStr = wordAnalyzer.getSubString(start, end);
							word = wordAnalyzer.getNextWord();
						}
						if(word.toLowerCase().equals(";")){
							declareStart = wordAnalyzer.getCIndex();
						}
						break;
					case "task":
						start = wordAnalyzer.getCIndex() - word.length();
						word = wordAnalyzer.getNextWord();
						procName = word;
						paramStr = null;
						word = wordAnalyzer.getNextWord();
						if(word.toLowerCase().equals(";")){
							declareStart = wordAnalyzer.getCIndex();
						}
						break;
					case "protected":
						start = wordAnalyzer.getCIndex() - word.length();
						word = wordAnalyzer.getNextWord();
						procName = word;
						paramStr = null;
						word = wordAnalyzer.getNextWord();
						if(word.toLowerCase().equals("is")){
							declareStart = wordAnalyzer.getCIndex();
							if(currentProc != null && currentProc.getType() == LexicalElement.TYPE_PACKAGE) {
								procStack.push(currentProc);
								currentProc = new LexicalElement(procName, LexicalElement.TYPE_DEFAULT);
							}
						}
						break;
					case "record":
						procName = "record";
						procStack.push(currentProc);
						currentProc = new LexicalElement(procName, LexicalElement.TYPE_DEFAULT);
						break;
					case "private":
						declareStart = wordAnalyzer.getCIndex();
						break;
					case "type":
						word = wordAnalyzer.getNextWord();//type name
						word = wordAnalyzer.getNextWord();
						if(word.toLowerCase().equals("is")){
							declareStart = wordAnalyzer.getCIndex();
						}
						break;
					case "begin":
						declareStart = -1;
						break;
					case "end":
						word = wordAnalyzer.getNextWord();
						String endName = word;
						word = wordAnalyzer.getNextWord();
						if (";".equals(word.toLowerCase())) {
							end = wordAnalyzer.getCIndex() - 1;
							if(currentProc != null && currentProc.getKeyName().equals(endName.toLowerCase())){
								currentProc = procStack.pop();
								declareStart = wordAnalyzer.getCIndex();
							}
						}
						break;
					case "declare":
						declareStart = wordAnalyzer.getCIndex();
						break;
					default:
						if(currentProc != null){
							if(declareStart >= 0){
								if (";".equals(word.toLowerCase())) {
									int declareEnd = wordAnalyzer.getCIndex();
									if(currentProc.getType() == LexicalElement.TYPE_PACKAGE) {
										cache.addGlobalVar(currentProc.getKey(), wordAnalyzer.getSubString(declareStart, declareEnd));
									}
									declareStart = declareEnd;
								}
							}
						}
						break;
				}
			}

			System.out.println("全局变量定义分析成功:" + wordAnalyzer.getFileName());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * 函数定义分析
	 */
	public static void DeclareAnalyze(AnalyzerDatabase db,
									  WordAnalyzer wordAnalyzer) {
		try {
			String word = null;
			int start;
			int end;
			Stack<LexicalElement> procStack = new Stack<LexicalElement>();
			LexicalElement currentProc = null;
			String procName = null;
			String paramStr = null;
			String returnType = null;
			int declareStart = -1;
			wordAnalyzer.reset();
			while ((word = wordAnalyzer.getNextWord()) != null) {
				switch (word.toLowerCase()) {
					case "package":
						start = wordAnalyzer.getCIndex() - word.length();
						word = wordAnalyzer.getNextWord();
						if ("body".equals(word.toLowerCase())) {
							word = wordAnalyzer.getNextWord();
						}
						procName = word;
						paramStr = null;
						word = wordAnalyzer.getNextWord();
						if(word.toLowerCase().equals("is")){
							declareStart = wordAnalyzer.getCIndex();
							if(currentProc == null) {
								db.addPackage(procName, start);
								procStack.push(currentProc);
								currentProc = new LexicalElement(procName, LexicalElement.TYPE_PACKAGE);
							}
						}
						break;
					case "function":
						start = wordAnalyzer.getCIndex() - word.length();
						word = wordAnalyzer.getNextWord();
						procName = word;
						paramStr = null;
						word = wordAnalyzer.getNextWord();
						if(word.toLowerCase().equals("(")){
							start = wordAnalyzer.getCIndex();
							while (!")".equals((word = wordAnalyzer.getNextWord()).toLowerCase())) {

							}
							end = wordAnalyzer.getCIndex() - ")".length();
							paramStr = wordAnalyzer.getSubString(start, end);
							word = wordAnalyzer.getNextWord();
						}
						returnType = null;
						if(word.toLowerCase().equals("return")){
							word = wordAnalyzer.getNextWord();
							returnType = word;
							word = wordAnalyzer.getNextWord();
						}
						if(word.toLowerCase().equals("is")){
							declareStart = wordAnalyzer.getCIndex();
							word = wordAnalyzer.getNextWord();
							if(!"new".equals(word.toLowerCase()) && !"separate".equals(word.toLowerCase())){
								//if(currentProc != null && currentProc.getType() == LexicalElement.TYPE_PACKAGE) {
									db.addFunction(procName, start, paramStr, returnType);
									procStack.push(currentProc);
									currentProc = new LexicalElement(procName, LexicalElement.TYPE_FUNC, paramStr, returnType);
								//}
							}else{
								declareStart = -1;
							}
						}
						break;
					case "procedure":
						start = wordAnalyzer.getCIndex() - word.length();
						word = wordAnalyzer.getNextWord();
						procName = word;
						paramStr = null;
						word = wordAnalyzer.getNextWord();
						if(word.toLowerCase().equals("(")){
							start = wordAnalyzer.getCIndex();
							while (!")".equals((word = wordAnalyzer.getNextWord()).toLowerCase())) {

							}
							end = wordAnalyzer.getCIndex() - ")".length();
							paramStr = wordAnalyzer.getSubString(start, end);
							word = wordAnalyzer.getNextWord();
						}
						if(word.toLowerCase().equals("is")){
							declareStart = wordAnalyzer.getCIndex();
							word = wordAnalyzer.getNextWord();
							if(!"new".equals(word.toLowerCase()) && !"separate".equals(word.toLowerCase())){
								//if(currentProc != null && currentProc.getType() == LexicalElement.TYPE_PACKAGE) {
									db.addProcedure(procName, start, paramStr);
									procStack.push(currentProc);
									currentProc = new LexicalElement(procName, LexicalElement.TYPE_PROC, paramStr, null);
								//}
							}else{
								declareStart = -1;
							}
						}
						break;
					case "task":
						start = wordAnalyzer.getCIndex() - word.length();
						word = wordAnalyzer.getNextWord();
						if ("body".equals(word.toLowerCase())) {
							word = wordAnalyzer.getNextWord();
						}
						procName = word;
						paramStr = null;
						word = wordAnalyzer.getNextWord();
						if(word.toLowerCase().equals("is")){
							declareStart = wordAnalyzer.getCIndex();
							//if(currentProc != null && currentProc.getType() == LexicalElement.TYPE_PACKAGE) {
								db.addTask(procName, start);
								procStack.push(currentProc);
								currentProc = new LexicalElement(procName, LexicalElement.TYPE_TASK);
							//}
						}
						break;
					case "protected":
						start = wordAnalyzer.getCIndex() - word.length();
						word = wordAnalyzer.getNextWord();
						if ("body".equals(word.toLowerCase())) {
							word = wordAnalyzer.getNextWord();
							procName = word;
							paramStr = null;
							word = wordAnalyzer.getNextWord();
							if(word.toLowerCase().equals("is")){
								declareStart = wordAnalyzer.getCIndex();
								//if(currentProc != null && currentProc.getType() == LexicalElement.TYPE_PACKAGE) {
								db.addProtected(procName, start);
								procStack.push(currentProc);
								currentProc = new LexicalElement(procName, LexicalElement.TYPE_PROT);
								//}
							}
						}
						break;
					case "record":
						procName = "record";
						procStack.push(currentProc);
						currentProc = new LexicalElement(procName, LexicalElement.TYPE_DEFAULT);
						break;
					case "private":
						declareStart = wordAnalyzer.getCIndex();
						break;
					case "type":
						word = wordAnalyzer.getNextWord();//type name
						word = wordAnalyzer.getNextWord();
						if(word.toLowerCase().equals("is")){
							declareStart = wordAnalyzer.getCIndex();
						}
						break;
					case "begin":
						declareStart = -1;
						break;
					case "end":
						word = wordAnalyzer.getNextWord();
						String endName = word;
						if(";".equals(endName)){
							end = wordAnalyzer.getCIndex() - 1;
							if(currentProc != null){
								if(currentProc.isProcType()) {
									db.updateEnd(currentProc.getKey(), end, wordAnalyzer, currentProc.getType());
								}
								currentProc = procStack.pop();
								declareStart = wordAnalyzer.getCIndex();
							}
						} else if(endName !=null && !endName.isEmpty() && !";".equals(endName)){
							word = wordAnalyzer.getNextWord();
							if (";".equals(word.toLowerCase())) {
								end = wordAnalyzer.getCIndex() - 1;
								if(currentProc != null && currentProc.getKeyName().equals(endName.toLowerCase())){
									if(currentProc.isProcType()) {
										db.updateEnd(currentProc.getKey(), end, wordAnalyzer, currentProc.getType());
									}
									currentProc = procStack.pop();
									declareStart = wordAnalyzer.getCIndex();
								}
							}
						}
						break;
					case "declare":
						word = wordAnalyzer.getNextWord();
						if(!";".equals(word)){
							procName = "declare";
							procStack.push(currentProc);
							currentProc = new LexicalElement(procName, LexicalElement.TYPE_DEFAULT);
						}
						declareStart = wordAnalyzer.getCIndex();
						break;
					default:
						if(currentProc != null){
							if(declareStart >= 0){
								if (";".equals(word.toLowerCase())) {
									int declareEnd = wordAnalyzer.getCIndex();
									if(currentProc.getType() == LexicalElement.TYPE_PACKAGE) {
										db.updateProcVar(currentProc.getKey(), wordAnalyzer.getSubString(declareStart, declareEnd));
									}
									declareStart = declareEnd;
								}
							}
						}
						break;
				}
			}

			db.finishWordAnalyzer(wordAnalyzer);
			System.out.println("函数定义分析成功:" + wordAnalyzer.getFileName());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
	}

	private static String getFullName(Stack<LexicalElement> procStack, LexicalElement currentProc){
        String fullName = "";
        for(int i=0; i<procStack.size(); i++){
            LexicalElement parent = procStack.get(i);
            if(parent != null){
                fullName +=  parent.getKey() + ".";
            }
        }
        if(currentProc != null){
            fullName += currentProc.getKey();
        }
        return fullName.toLowerCase();
    }

	/**
	 * 函数调用分析
	 */
	public static void CallAnalyze(AnalyzerDatabase db,
								   WordAnalyzer wordAnalyzer) {
		try {
			String word = null;
			int start;
			int end;
			String wordContentString;
			Stack<LexicalElement> procStack = new Stack<LexicalElement>();
			LexicalElement currentProc = null;
			String procName = null;
			String paramStr = null;
			String returnType = null;
			wordAnalyzer.reset();
			while ((word = wordAnalyzer.getNextWord()) != null) {
				switch (word.toLowerCase()) {
					case "with":
						start = wordAnalyzer.getCIndex();
						while (!";".equals((word = wordAnalyzer.getNextWord()).toLowerCase())) {

						}
						end = wordAnalyzer.getCIndex() - ";".length();
						wordContentString = wordAnalyzer.getSubString(start, end);
						String[] withPkgs = wordContentString.split(",");
						for (String pkg : withPkgs) {
							if (pkg != null && !pkg.trim().isEmpty()) {
								db.addWith(pkg.trim());
							}
						}
						break;
					case "use":
						start = wordAnalyzer.getCIndex();
						while (!";".equals((word = wordAnalyzer.getNextWord()))) {

						}
						end = wordAnalyzer.getCIndex() - ";".length();
						wordContentString = wordAnalyzer.getSubString(start, end);
						String[] usePkgs = wordContentString.split(",");
						for (String pkg : usePkgs) {
							if (pkg != null && !pkg.trim().isEmpty()) {
								db.addUse(pkg.trim());
							}
						}
						break;
					case "package":
						word = wordAnalyzer.getNextWord();
						if ("body".equals(word.toLowerCase())) {
							word = wordAnalyzer.getNextWord();
						}
						procName = word;
						paramStr = null;
						word = wordAnalyzer.getNextWord();
						if(word.toLowerCase().equals("is")){
							if(currentProc == null) {
								db.addWith(procName);
								db.addUse(procName);
								procStack.push(currentProc);
								currentProc = new LexicalElement(procName, LexicalElement.TYPE_PACKAGE);
							}
						}
						break;
					case "function":
						word = wordAnalyzer.getNextWord();
						procName = word;
						paramStr = null;
						word = wordAnalyzer.getNextWord();
						if(word.toLowerCase().equals("(")){
							start = wordAnalyzer.getCIndex();
							while (!")".equals((word = wordAnalyzer.getNextWord()).toLowerCase())) {

							}
							end = wordAnalyzer.getCIndex() - ")".length();
							paramStr = wordAnalyzer.getSubString(start, end);
							word = wordAnalyzer.getNextWord();
						}
						returnType = null;
						if(word.toLowerCase().equals("return")){
							word = wordAnalyzer.getNextWord();
							returnType = word;
							word = wordAnalyzer.getNextWord();
						}
						if(word.toLowerCase().equals("is")){
							word = wordAnalyzer.getNextWord();
							if(!"new".equals(word.toLowerCase()) && !"separate".equals(word.toLowerCase())){
								procStack.push(currentProc);
								currentProc = new LexicalElement(procName, LexicalElement.TYPE_FUNC, paramStr, returnType);
							}
						}
						break;
					case "procedure":
						word = wordAnalyzer.getNextWord();
						procName = word;
						paramStr = null;
						word = wordAnalyzer.getNextWord();
						if(word.toLowerCase().equals("(")){
							start = wordAnalyzer.getCIndex();
							while (!")".equals((word = wordAnalyzer.getNextWord()).toLowerCase())) {

							}
							end = wordAnalyzer.getCIndex() - ")".length();
							paramStr = wordAnalyzer.getSubString(start, end);
							word = wordAnalyzer.getNextWord();
						}
						if(word.toLowerCase().equals("is")){
							word = wordAnalyzer.getNextWord();
							if(!"new".equals(word.toLowerCase()) && !"separate".equals(word.toLowerCase())){
								procStack.push(currentProc);
								currentProc = new LexicalElement(procName, LexicalElement.TYPE_PROC, paramStr, null);
							}
						}
						break;
					case "task":
						start = wordAnalyzer.getCIndex() - word.length();
						word = wordAnalyzer.getNextWord();
						if ("body".equals(word.toLowerCase())) {
							word = wordAnalyzer.getNextWord();
						}
						procName = word;
						paramStr = null;
						word = wordAnalyzer.getNextWord();
						if(word.toLowerCase().equals("is")){
							// if(currentProc != null && currentProc.getType() == LexicalElement.TYPE_PACKAGE) {
								procStack.push(currentProc);
								currentProc = new LexicalElement(procName, LexicalElement.TYPE_TASK);
							// }
						}
						break;
					case "protected":
						start = wordAnalyzer.getCIndex() - word.length();
						word = wordAnalyzer.getNextWord();
						if ("body".equals(word.toLowerCase())) {
							word = wordAnalyzer.getNextWord();
							procName = word;
							paramStr = null;
							word = wordAnalyzer.getNextWord();
							if(word.toLowerCase().equals("is")){
//								if(currentProc != null && currentProc.getType() == LexicalElement.TYPE_PACKAGE) {
									procStack.push(currentProc);
									currentProc = new LexicalElement(procName, LexicalElement.TYPE_PROT);
//								}
							}
						}
						break;
					case "record":
						procName = "record";
						procStack.push(currentProc);
						currentProc = new LexicalElement(procName, LexicalElement.TYPE_DEFAULT);
						break;
					case "declare":
						word = wordAnalyzer.getNextWord();
						if(!";".equals(word)){
							procName = "declare";
							procStack.push(currentProc);
							currentProc = new LexicalElement(procName, LexicalElement.TYPE_DEFAULT);
						}
						break;
					case "end":
						word = wordAnalyzer.getNextWord();
						String endName = word;
						if(";".equals(endName)){
							if(currentProc != null){
								currentProc = procStack.pop();
							}
						} else if(endName !=null && !endName.isEmpty() && !";".equals(endName)){
							word = wordAnalyzer.getNextWord();
							if (";".equals(word.toLowerCase())) {
								if(currentProc != null && currentProc.getKeyName().equals(endName.toLowerCase())){
									currentProc = procStack.pop();
								}
							}
						}
						break;
					default:
						if (currentProc != null) {
							VarItem calledVar = db.findVar(word);
							if(calledVar != null){
								db.updateVarCalls(getFullName(procStack, currentProc), calledVar);
							}

							List<LexicalElement> calledProcList = db.findProc(word);
							if(calledProcList.size() > 0) {
								int paramNum = 0;
								LexicalElement calledProc = null;
								word = wordAnalyzer.getNextWord();
								Stack<Boolean> parenthesis = new Stack<>();
								if(word.toLowerCase().equals("(")){
									paramStr = null;
									start = wordAnalyzer.getCIndex();
									parenthesis.push(true);
									while (true) {
                                        word = wordAnalyzer.getNextWord();
                                        calledVar = db.findVar(word);
                                        if(calledVar != null){
                                            db.updateVarCalls(getFullName(procStack, currentProc), calledVar);
                                        }
                                        word = word.toLowerCase();
                                        if("(".equals(word)){
                                            parenthesis.push(true);
                                        } else if(")".equals(word)){
                                            parenthesis.pop();
                                        }
                                        if(parenthesis.size() == 0){
                                            break;
                                        }
									}
									end = wordAnalyzer.getCIndex() - ")".length();
									paramStr = wordAnalyzer.getSubString(start, end);
									String[] paramArr = paramStr.split(",");
									int paramCount = 0;
									for(String param: paramArr){
										if(param != null && !param.trim().isEmpty()){
											paramCount++;
										}
									}
									paramNum = paramCount;
								}

								for(LexicalElement called : calledProcList){
									if(called.getParams().size() == paramNum){ //参数个数相同
										calledProc = called;
										break;
									}
								}
								if(calledProc != null){
									db.updateCalls(getFullName(procStack, currentProc), calledProc);
								}
							}
						}
						break;
				}
			}

			System.out.println("函数调用分析成功:" + wordAnalyzer.getFileName());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
	}

}