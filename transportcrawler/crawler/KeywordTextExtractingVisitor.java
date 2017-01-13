package crawler;

import java.util.ArrayList;
import java.util.List;

import log.Log;

import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.Translate;
import org.htmlparser.visitors.NodeVisitor;

/**
 * 对Parser的html网页中的节点进行遍历，如果碰到文字段落，则保存
 * @author Qinger
 *
 */
public class KeywordTextExtractingVisitor extends NodeVisitor {
	private List<String> 	keyWordTextList = null;
	private boolean			preTagBeingProcessed;
	private String			keyWord;
	
	public KeywordTextExtractingVisitor()
	{
		this("");
	}
	
	public KeywordTextExtractingVisitor(String keyWord)
	{
		this.keyWordTextList 		= new ArrayList<String>();
		this.preTagBeingProcessed	= false;
		this.keyWord = keyWord;
	}
	
	public List<String> getKeyWordText()
	{
		return this.keyWordTextList;
	}
	
	public void visitStringNode(Text stringNode) {
        String text = stringNode.getText();
        if (!preTagBeingProcessed) {
            text = Translate.decode(text);
            text = replaceNonBreakingSpaceWithOrdinarySpace(text);
        }
        if(text.contains(keyWord))
        	this.keyWordTextList.add(text);
    }

    private String replaceNonBreakingSpaceWithOrdinarySpace(String text) {
        return text.replace('\u00a0',' ').trim();
    }

    public void visitTag(Tag tag)
    {
        if (isPreTag(tag))
            preTagBeingProcessed = true;
    }

    public void visitEndTag(Tag tag)
    {
        if (isPreTag(tag))
            preTagBeingProcessed = false;
    }

    private boolean isPreTag(Tag tag) {
        return tag.getTagName().equals("PRE");
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Parser parser = null;
		try {
			parser = new Parser("http://www.bjjtgl.gov.cn/publish/portal0/tab65/info45975.htm");
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			Log.logInfo(e);
		}
		try {
			parser.setEncoding("UTF-8");
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			Log.logInfo(e);
		}
		KeywordTextExtractingVisitor keywordVisitor = new KeywordTextExtractingVisitor("知春路");
		try {
			parser.visitAllNodesWith(keywordVisitor);
			List<String> list = keywordVisitor.getKeyWordText();
			for(int i = 0; i < list.size(); i++)
			{
				System.out.println(list.get(i));
			}
			
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			Log.logInfo(e);
		}
	}

}

