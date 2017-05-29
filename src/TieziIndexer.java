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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import javax.xml.parsers.*; 

public class TieziIndexer {
	private Analyzer analyzer; 
    private IndexWriter indexWriter;
    private float averageLength=1.0f;
    
    public TieziIndexer(String indexDir) {
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
    		pw.println(averageLength);
    		pw.close();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
    public void indexSpecialFile(String filename){
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();   
			DocumentBuilder db = dbf.newDocumentBuilder();    
			org.w3c.dom.Document doc = db.parse(new File(filename));
			NodeList nodeList = doc.getElementsByTagName("pic");
			for(int i=0;i<nodeList.getLength();i++){
				Node node=nodeList.item(i);
				NamedNodeMap map=node.getAttributes();
				Node nickname=map.getNamedItem("nickname");
				Node text=map.getNamedItem("text");
				Node image=map.getNamedItem("image");
				Node favorite=map.getNamedItem("favorite");
				Node forwarding=map.getNamedItem("forwarding");
				Node comment=map.getNamedItem("comment");
				Node thumbup=map.getNamedItem("thumbup");
				Node has_zhuanfa=map.getNamedItem("has_zhuanfa");
				Node zf_nickname=map.getNamedItem("zf_nickname");
				Node zf_text=map.getNamedItem("zf_text");
				Node zf_image=map.getNamedItem("zf_image");
				
				String textString = text.getNodeValue()+"#$%"+zf_text.getNodeValue(); //#$%起到分隔原贴和转发贴的作用
				Document document = new Document();
				Field tieziTextField = new Field("tieziText", textString, Field.Store.YES, Field.Index.ANALYZED);
				Field nicknameField = new Field("nickname", nickname.getNodeValue(), Field.Store.YES, Field.Index.NO);
				Field imageField = new Field("image", image.getNodeValue(), Field.Store.YES, Field.Index.NO);
				Field favField = new Field("favorite", favorite.getNodeValue(), Field.Store.YES, Field.Index.NO);
				Field fwdField = new Field("forwarding", forwarding.getNodeValue(), Field.Store.YES, Field.Index.NO);
				Field cmtField = new Field("comment", comment.getNodeValue(), Field.Store.YES, Field.Index.NO);
				Field tbuField = new Field("thumbup", thumbup.getNodeValue(), Field.Store.YES, Field.Index.NO);
				Field hzfField = new Field("has_zf", has_zhuanfa.getNodeValue(), Field.Store.YES, Field.Index.NO);
				Field zfnicknameField = new Field("zf_nickname", zf_nickname.getNodeValue(), Field.Store.YES, Field.Index.NO);
				Field zfimageField = new Field("zf_image", zf_image.getNodeValue(), Field.Store.YES, Field.Index.NO);
				
				averageLength += textString.length();
				document.add(tieziTextField);
				document.add(nicknameField);
				document.add(imageField);
				document.add(favField);
				document.add(fwdField);
				document.add(cmtField);
				document.add(tbuField);
				document.add(hzfField);
				document.add(zfnicknameField);
				document.add(zfimageField);
				indexWriter.addDocument(document);
				if(i % 10000==0){
					System.out.println("process "+i);
				}
			}
			averageLength /= indexWriter.numDocs();
			System.out.println("average length = "+averageLength);
			System.out.println("total " + indexWriter.numDocs() + " documents");
			indexWriter.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		TieziIndexer indexer=new TieziIndexer("forIndex/tiezi_index");
		indexer.indexSpecialFile("input/tiezi.xml");
		indexer.saveGlobals("forIndex/tiezi_global.txt");
	}
}
