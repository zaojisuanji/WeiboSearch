import java.io.*;
import java.util.*;

import org.w3c.dom.*;   
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexReader.FieldOption;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import javax.xml.parsers.*; 

public class PeopleIndexer {
	private Analyzer analyzer; 
    private IndexWriter indexWriter;
    private float nameLength=1.0f;
    private float introLength=1.0f;  //card + intro
    private float labelLength=1.0f;  //label + edu_info + pro_info
    
    public PeopleIndexer(String indexDir){
    	analyzer = new IKAnalyzer();
    	try{
    		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, analyzer);
    		Directory dir = FSDirectory.open(new File(indexDir));
    		indexWriter = new IndexWriter(dir,iwc);
    		indexWriter.setSimilarity(new SimpleSimilarity());
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
    
    public void saveGlobals(String filename){
    	try{
    		PrintWriter pw=new PrintWriter(new File(filename));
    		pw.println(nameLength);
    		pw.println(introLength);
    		pw.println(labelLength);
    		pw.close();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
	
	/** 
	 * <p>
	 * index sogou.xml 
	 * 
	 */
	public void indexSpecialFile(String filename){
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();   
			DocumentBuilder db = dbf.newDocumentBuilder();    
			org.w3c.dom.Document doc = db.parse(new File(filename));
			NodeList nodeList = doc.getElementsByTagName("pic");
			for(int i=0;i<nodeList.getLength();i++){
				Node node=nodeList.item(i);
				NamedNodeMap map=node.getAttributes();
				Node link=map.getNamedItem("link");
				Node name=map.getNamedItem("name");
				Node image=map.getNamedItem("image");
				Node card=map.getNamedItem("card");
				Node follow=map.getNamedItem("follow");
				Node fans=map.getNamedItem("fans");
				Node weibo_num = map.getNamedItem("weibonum");
				Node label = map.getNamedItem("label");
				Node edu_info = map.getNamedItem("edu_info");
				Node pro_info = map.getNamedItem("pro_info");
				Node introduction = map.getNamedItem("introduction");
				
				String nameString=name.getNodeValue();
				//System.out.println(i);
				//if(introduction.getNodeValue() == null)
					
				String introString = introduction.getNodeValue() + card.getNodeValue();
				
				String labelString = label.getNodeValue() + edu_info.getNodeValue() + pro_info.getNodeValue();
				
				nameLength += nameString.length();
				introLength += introString.length();
				labelLength += labelString.length();
				
				Document document = new  Document();
				Field nameField = new  Field( "name" ,nameString,Field.Store.YES, Field.Index.ANALYZED);
				Field introField = new  Field( "intro" ,introString,Field.Store.YES, Field.Index.ANALYZED);
				Field labelField = new Field("label", labelString,Field.Store.YES, Field.Index.ANALYZED);
				Field followField = new Field("follow", follow.getNodeValue(),Field.Store.YES, Field.Index.NO);
				Field fansField = new Field("fans", fans.getNodeValue(),Field.Store.YES, Field.Index.NO);
				Field weibonumField = new Field("weibonum", weibo_num.getNodeValue(),Field.Store.YES, Field.Index.NO);
				
				
				document.add(nameField);
				document.add(introField);
				document.add(labelField);
				document.add(followField);
				document.add(fansField);
				document.add(weibonumField);
				indexWriter.addDocument(document);
				if(i%100==0){
					System.out.println("process "+i);
				}
				//TODO: add other fields such as html title or html content 
				
			}
			nameLength /= indexWriter.numDocs();
			System.out.println("average length = "+nameLength);
			System.out.println("total "+indexWriter.numDocs()+" documents");
			indexWriter.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
    static String[] concat(String[] a, String[] b) {  
        String[] c= new String[a.length+b.length];  
        System.arraycopy(a, 0, c, 0, a.length);  
        System.arraycopy(b, 0, c, a.length, b.length);  
        return c;  
     }  
	public static void main(String[] args) {
		PeopleIndexer indexer=new PeopleIndexer("forIndex/index");
		indexer.indexSpecialFile("input/people.xml");
		indexer.saveGlobals("forIndex/global.txt");
	}
}
