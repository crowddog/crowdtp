package dataAnalyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import log.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import weiboSearch.GetWeiboContent;
import crawler.Crawler;

public class DataAnalyser {
	private static Map<String, List<String>> modifyCoeToWords = new HashMap<String, List<String>>();
	private static final String modifyCoeFileName = "initializeSrc/crawlerSrc/modifyCoe.txt";
	private static Map<String, Double> modifyNameToCoe = new HashMap<String, Double>();
	private static List<String> modifyName 	= new ArrayList<String>();
	
	private static Map<String, List<String>> degreeValueToWords = new HashMap<String, List<String>>();
	private static final String degreeValueFileName = "initializeSrc/crawlerSrc/degreeValue.txt";
	private static Map<String, Double> degreeNameToValue = new HashMap<String, Double>();
	private static List<String> degreeName 	= new ArrayList<String>();
	
	private static final int LEVEL_ONE 	= 1;
	private static final int LEVEL_TWO 	= 2;
	private static final int LEVEL_THREE= 3;
	private static final int LEVEL_FOUR	= 4;
	
	private static final String REGEXP_SentenceSeparator 	= "['\\.'|��|��|��|?|!]";
	private static final String REGEXP_PhraseSeparator 	= "[��|��|��|��|��|��|~|����|,|;|'|\"|:|`|~|_]";
	
	/**
	 * �ڵ���DataAnalyser֮ǰ��Ҫ��ʼ������ʼ��һ�μ��ɡ�
	 */
	public static void initilize()
	{
		readFile(modifyCoeFileName, modifyName, modifyNameToCoe, modifyCoeToWords);
		readFile(degreeValueFileName, degreeName, degreeNameToValue, degreeValueToWords);
	}
	
	/**
	 * ��ȡ·�ε�ӵ���ȼ����ȼ�Խ�ߣ�ӵ���̶�Խ��
	 * @param paras Ҫ���������ֶ��伯��
	 * @param keyword Ҫ�����Ĺؼ��֣������Ӧ��Ϊ�Ϸ���·��
	 * @return ӵ���̶ȵȼ�
	 */
	public static int getEstimatedLevel(List<String> paras, String keyword)
	{
		double estValue = DataAnalyser.getEstimatedValue(paras, keyword);
		return estValue<1.5 ?LEVEL_ONE :( estValue<3 ?LEVEL_TWO :(estValue<6 ?LEVEL_THREE :LEVEL_FOUR));
	}
	
	/**
	 * ��ȡ·�ε�ӵ������ָ��ÿ��ӵ���ȼ����ж�Ӧ��Χ������ָ��ָ��Խ��ӵ���̶�Խ��
	 * @param paras ͬ�ϣ�Ϊԭʼ���ּ���
	 * @param keyword ͬ�ϣ�Ϊ·��
	 * @return
	 */
	public static double getEstimatedValue(List<String> paras, String keyword)
	{
		double estValue = 1.0;
		double valueOfDegree = 1.0;
		double valueOfModify = 1.0;
		int keySentenceCount = 1;
		
		for(int i = 0; i < paras.size(); i++)
		{
			String para = paras.get(i);
			String[] sentences = para.split(REGEXP_SentenceSeparator);
			for(String sentence : sentences)
			{
				//��ÿ���ؼ���ӣ����Եõ�һ������ֵvalueOfDegree��һ��Ȩ��ϵ��valueOfModify,�õ�һ������ֵ
				if(sentence.contains(keyword)){
					valueOfDegree = getValue(sentence, degreeName, degreeNameToValue, degreeValueToWords);
					valueOfModify = getValueOfModify(sentence, keyword);
					
					estValue += valueOfDegree * valueOfModify;
					keySentenceCount++;
				}
			}
		}
		estValue /= keySentenceCount;
		return estValue;
	}
	
	/**
	 * �����Ӧ�Ķ�Ӧ��ϵ��������Ӧֵ�����������Ҫ�������㷨�ĵ���
	 * @param sentence
	 * @param keys
	 * @param keyToValue
	 * @param keyToWords
	 * @return
	 */
	private static double getValue(String sentence, List<String> keys, Map<String, Double> keyToValue, Map<String, List<String>> keyToWords)
	{
		double valueOfDegree = 1.0;
		for(int i = 0; i < keys.size(); i++)
		{
			String name = keys.get(i);
			List<String> words = keyToWords.get(name);
			for(int j = 0; j < words.size(); j++)
			{
				String word = words.get(j);
				if(sentence.contains(word))
					return valueOfDegree = keyToValue.get(name);
			}
		}
		return valueOfDegree;
	}
	
	/**
	 * ��ȡ������ϵ��
	 * @param sentence �ؼ����
	 * @param keyword �ؼ���
	 * @return ������ϵ��
	 */
	private static double getValueOfModify(String sentence, String keyword)
	{
		double valueOfModify = 1.0;
		String[] phrases = sentence.split(REGEXP_PhraseSeparator);
		int keyPhraseCount = 1;
		for(String phrase: phrases)
		{
			if(phrase.contains(keyword)){
				phrase = cleanPhrase(phrase);
				valueOfModify += getValue(phrase, modifyName, modifyNameToCoe, modifyCoeToWords);
				keyPhraseCount++;
			}
		}
		valueOfModify /= keyPhraseCount;
		return valueOfModify;
	}
	
	/**
	 * �ӹؼ��������������ӵ���̶ȵ����۴ʣ�������ж����δʲ�����š�
	 * @param phrase �ؼ����
	 * @return �������Ĺؼ����
	 */
	private static String cleanPhrase(String phrase)
	{
		for(int i = 0; i < degreeName.size(); i++)
		{
			String name = degreeName.get(i);
			List<String> words = degreeValueToWords.get(name);
			for(int j = 0; j < words.size(); j++)
			{
				String word = words.get(j);
				phrase = phrase.replace(word, "");
			}
		}
		return phrase;
	}
	
	/**
	 * �ѳ�ʼ����ݴ��ļ��ж�ȡ������
	 * @param fileName
	 * @param names
	 * @param nameToValue
	 * @param map
	 */
	private static void readFile(String fileName, List<String> names, Map<String, Double> nameToValue, Map<String, List<String>> map)
	{
		StringBuffer stringBuffer = new StringBuffer();
		String line = null;
		try
		{ 
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			while((line = br.readLine()) != null)
				stringBuffer.append(line);
			br.close();
		}catch(FileNotFoundException e)
		{
			Log.logInfo(e);
		}catch(IOException e){
			Log.logInfo(e);
		}
		try
		{
			JSONObject jsonObject = new JSONObject(stringBuffer.toString());
			JSONArray types = jsonObject.getJSONArray("type");
			JSONArray attributes = null;
			String typeName = null;
			Double value	= null;
			for(int i = 0; i < types.length(); i++)
			{
				typeName 	= types.getJSONObject(i).getString("name");
				value		= types.getJSONObject(i).getDouble("value"); 	
				names.add(typeName);
				nameToValue.put(typeName, value);
				attributes = types.getJSONObject(i).getJSONArray("attribute");
				List<String> attrList = new ArrayList<String>();
				for(int j = 0; j < attributes.length(); j++)
				{
					String attri = attributes.getString(j);
					attrList.add(attri);
				}
				map.put(typeName, attrList);
			}			 
		}catch(Exception e) {
			Log.logInfo(e);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String para = "��ǰ���н�ͨ������������ĩ�ڼ䣬����վ�ܱߵ�·�Ľ�ͨѹ����Ȼͻ����������վ���㳡�������У���������·���򶫷���ȫ�쳵�л��������������в���Ҳ�������α����Ϸ�����ʻ���� ������ƽ��ʱ�Σ���ͯҽԺ��������ͬ��ҽԺ������Լ��У���ʹ�ܱߵ�·������ʻ������";
		Date begin = new Date();
		String keyword = "����";
		Crawler crawler = new Crawler(keyword);
		crawler.setTimeInterval(24 * 365);
		
		GetWeiboContent getweiboContent = new GetWeiboContent();
		List<String> paras = crawler.getRelevantPara();
		Date getParas = new Date();
		System.out.println("Crawler needs time: " + ((getParas.getTime() - begin.getTime())/1000));
		
		List<String> weibos= getweiboContent.getWeiboContent(keyword);
		Date getweibo = new Date();
		System.out.println("GetWeibo needs time: " + ((getweibo.getTime() - getParas.getTime()) / 1000));
		
		paras.addAll(weibos);
		
		DataAnalyser.initilize();
		System.out.println(keyword + " EstimatedValue: " + DataAnalyser.getEstimatedValue(paras, keyword));
		Date end = new Date();
		System.out.println("Need TIME: " + ((end.getTime() - begin.getTime()) /1000));
	}
	
	public double returnValues(String keyword){
		Date begin = new Date();
		
		Crawler crawler = new Crawler(keyword);
		crawler.setTimeInterval(24 * 365);
		
		GetWeiboContent getweiboContent = new GetWeiboContent();
		List<String> paras = crawler.getRelevantPara();
		Date getParas = new Date();
		System.out.println("Crawler needs time: " + ((getParas.getTime() - begin.getTime())/1000));
		
		List<String> weibos= getweiboContent.getWeiboContent(keyword);
		Date getweibo = new Date();
		System.out.println("GetWeibo needs time: " + ((getweibo.getTime() - getParas.getTime()) / 1000));
		
		paras.addAll(weibos);
		
		DataAnalyser.initilize();
		System.out.println(keyword + " EstimatedValue: " + (new Random(47).nextDouble()*5));
		Date end = new Date();
		
		return DataAnalyser.getEstimatedValue(paras, keyword);
	}
	

}
