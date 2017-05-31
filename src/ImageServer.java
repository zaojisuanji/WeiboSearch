import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.dic.Dictionary;

import java.util.*;
import java.math.*;
import java.net.*;
import java.io.*;


public class ImageServer extends HttpServlet{
	private IndexReader reader;
	private IndexSearcher searcher;
	public static final int PAGE_RESULT=10;
	public static final String indexDir="forIndex";
	private ImageSearcher search=null;
	
	public ImageServer(){
		super();
		search=new ImageSearcher(new String(indexDir+"/tiezi_index"));
		search.loadGlobals(new String(indexDir+"/global.txt"));
		try{
			reader = IndexReader.open(FSDirectory.open(new File(indexDir+"/tiezi_index")));
			searcher = new IndexSearcher(reader);
			searcher.setSimilarity(new SimpleSimilarity());
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public ScoreDoc[] showList(ScoreDoc[] results,int page){
		if(results==null || results.length<(page-1)*PAGE_RESULT){
			return null;
		}
		int start=Math.max((page-1)*PAGE_RESULT, 0);
		int docnum=Math.min(results.length-start,PAGE_RESULT);
		ScoreDoc[] ret=new ScoreDoc[docnum];
		for(int i=0;i<docnum;i++){
			ret[i]=results[start+i];
		}
		return ret;
	}
	
	static ScoreDoc[] concat(ScoreDoc[] a, ScoreDoc[] b) {  
		ScoreDoc[] c= new ScoreDoc[a.length+b.length];  
        System.arraycopy(a, 0, c, 0, a.length);  
        System.arraycopy(b, 0, c, a.length, b.length);  
        return c;  
     }  
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("doGet called~");
		
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		String queryString = request.getParameter("query");
		String pageString = request.getParameter("page");
		String wanttoString = request.getParameter("wantto");
		System.out.println(wanttoString);
		int page = 1;
		if(pageString != null){
			page = Integer.parseInt(pageString);
		}
		System.out.println("pageString:"+pageString);
		queryString = queryString.replace(" ", "");
		if(queryString.isEmpty()){
			System.out.println("null query");
			request.getRequestDispatcher("/imagesearch.jsp").forward(request, response);
		} else {
			if(wanttoString.equals("千度找贴")) {
				System.out.println(URLDecoder.decode(queryString,"utf-8"));
				String[] nicknames = null;
				String[] personlinks = null;
				String[] tiezitexts = null;
				String[] favorites = null;
				String[] forwardings = null;
				String[] comments = null;
				String[] thumbups = null;
				String[] images = null;
				String[] zf_images = null;
				
				Analyzer analyzer = new IKAnalyzer(true);
				StringReader reader = new StringReader(queryString);
				TokenStream tStream = analyzer.tokenStream("", reader);
				CharTermAttribute term = tStream.getAttribute(CharTermAttribute.class);
				
				ScoreDoc[] resultscore = {};
				List<String> tokens = new ArrayList<String>();
				while(tStream.incrementToken())
				{
					tokens.add(term.toString());
					System.out.println(term.toString());
				}
				
				String[] fields = {"nickname", "tieziText"};
				String[] queries = new String[tokens.size()];
				tokens.toArray(queries);
				BooleanClause.Occur[] clauses = { BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD };  
		        Query query;
				try {
					query = MultiFieldQueryParser.parse(Version.LUCENE_35, queryString, fields, clauses, new IKAnalyzer(true));
					
					//query = MultiFieldQueryParser.parse(Version.LUCENE_35, queryString, fields, clauses, new StandardAnalyzer(Version.LUCENE_35));
					TopDocs result = searcher.search(query, 150);  
			        resultscore = result.scoreDocs;
			        System.out.println("命中数:"+result.totalHits);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
				
				if (resultscore != null) {
					ScoreDoc[] hits = showList(resultscore, page);
					System.out.println("length of hits:" + resultscore.length);
					if (hits != null) {
						nicknames = new String[hits.length];
						personlinks = new String[hits.length];
						tiezitexts = new String[hits.length];
						favorites = new String[hits.length];
						forwardings = new String[hits.length];
						comments = new String[hits.length];
						thumbups = new String[hits.length];
						images = new String[hits.length];
						zf_images = new String[hits.length];
						for (int i = 0; i < hits.length && i < PAGE_RESULT; i++) {
							Document doc = search.getDoc(hits[i].doc);
							System.out.println("doc=" + hits[i].doc + " score="
									+ hits[i].score + " nickname= "
									+ doc.get("nickname")+ " text= "+doc.get("tieziText"));
							nicknames[i] = doc.get("nickname");
							personlinks[i] = doc.get("personlink");
							tiezitexts[i] = doc.get("tieziText");
							for (int j = 0; j < queries.length; j++) {
								nicknames[i] = nicknames[i].replace(queries[j], "<font color=\"#FF0000\">"+queries[j]+"</font>");
								tiezitexts[i] = tiezitexts[i].replace(queries[j], "<font color=\"#FF0000\">"+queries[j]+"</font>");
							}
							favorites[i] = doc.get("favorite");
							forwardings[i] = doc.get("forwarding");
							comments[i] = doc.get("comment");
							thumbups[i] = doc.get("thumbup");
							images[i] = doc.get("image");
							zf_images[i] = doc.get("zf_image");
						}
	
					} else {
						System.out.println("page null");
					}
				}else{
					System.out.println("result null");
				}
				
				request.setAttribute("currentQuery",queryString);
				request.setAttribute("currentPage", page);
				request.setAttribute("nickNames", nicknames);
				request.setAttribute("personlinks", personlinks);
				request.setAttribute("tieziTexts", tiezitexts);
				request.setAttribute("favorites", favorites);
				request.setAttribute("forwardings", forwardings);
				request.setAttribute("comments", comments);
				request.setAttribute("thumbups", thumbups);
				request.setAttribute("images", images);
				request.setAttribute("zf_images", zf_images);
				request.getRequestDispatcher("/imageshow.jsp").forward(request,
						response);
			} else {	//ToDo: 改成找人的后端
				request.getRequestDispatcher("/imagesearch.jsp").forward(request, response);
			}
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
}
