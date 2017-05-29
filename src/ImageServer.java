import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.util.*;
import java.math.*;
import java.net.*;
import java.io.*;


public class ImageServer extends HttpServlet{
	public static final int PAGE_RESULT=10;
	public static final String indexDir="forIndex";
	public static final String picDir="";
	private ImageSearcher search=null;
	public ImageServer(){
		super();
		search=new ImageSearcher(new String(indexDir+"/index"));
		search.loadGlobals(new String(indexDir+"/global.txt"));
		
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
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		String queryString=request.getParameter("query");
		String pageString=request.getParameter("page");
		// System.out.println("pageString:" + pageString);
		int page=1;
		if(pageString!=null){
			page=Integer.parseInt(pageString);
		}
		System.out.println("pageString:"+pageString);
		if(queryString==null){
			System.out.println("null query");
			//request.getRequestDispatcher("/Image.jsp").forward(request, response);
		}else{
			//System.out.println(queryString);
			System.out.println(URLDecoder.decode(queryString,"utf-8"));
			//System.out.println(URLDecoder.decode(queryString,"gb2312"));
			String[] tags=null;
			String[] paths=null;
			Analyzer analyzer = new IKAnalyzer(true);
			StringReader reader = new StringReader(queryString);
			TokenStream tStream = analyzer.tokenStream("", reader);
			CharTermAttribute term = tStream.getAttribute(CharTermAttribute.class);
			float totalScore = 0;
			TopDocs results;
			Sort sort = new Sort();
			
			results = search.searchQuery(queryString, "name", 100);
			ScoreDoc[] resultscore = {};
			
			while(tStream.incrementToken())
			{
				System.out.println(term.toString());
				TopDocs tmpresults=search.searchQuery(term.toString(), "name", 100);
				if(tmpresults != null)
				{
					resultscore = concat(resultscore, tmpresults.scoreDocs);
				}
			}
			int l = resultscore.length;
			List<ScoreDoc> tmpList = new ArrayList<ScoreDoc>();
			for(int i=0;i<l;i++)
			{
				int size = tmpList.size();
				boolean flag = true;
				for(int j=0;j<size;j++)
				{
					if(resultscore[i].doc == tmpList.get(j).doc)
					{
						flag = false;
						break;
					}
				}
				if(flag)
					tmpList.add(resultscore[i]);
			}
			resultscore = new ScoreDoc[tmpList.size()];
			System.out.println(tmpList.size());
			for(int i=0;i<tmpList.size();i++)
			{
				resultscore[i] = tmpList.get(i);
			}
			
			
			if (resultscore != null) {
				ScoreDoc[] hits = showList(resultscore, page);
				System.out.println("length of hits:" + resultscore.length);
				if (hits != null) {
					tags = new String[hits.length];
					paths = new String[hits.length];
					for (int i = 0; i < hits.length && i < PAGE_RESULT; i++) {
						Document doc = search.getDoc(hits[i].doc);
						System.out.println("doc=" + hits[i].doc + " score="
								+ hits[i].score + " name= "
								+ doc.get("name")+ " tag= "+doc.get("intro"));
						tags[i] = doc.get("name");
						paths[i] = picDir + doc.get("intro");
					}

				} else {
					System.out.println("page null");
				}
			}else{
				System.out.println("result null");
			}
			
			request.setAttribute("currentQuery",queryString);
			request.setAttribute("currentPage", page);
			request.setAttribute("imgTags", tags);
			request.setAttribute("imgPaths", paths);
			request.getRequestDispatcher("/imageshow.jsp").forward(request,
					response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
}
