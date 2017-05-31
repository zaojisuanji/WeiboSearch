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
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.util.*;
import java.math.*;
import java.net.*;
import java.io.*;

public class Try {
	
//	 public static void queryWords(String query) throws IOException {
////       Configuration cfg = DefaultConfig.getInstance();
////       System.out.println(cfg.getMainDictionary()); // 系统默认词库
////       System.out.println(cfg.getQuantifierDicionary());
//       List<String> list = new ArrayList<String>();
//       StringReader input = new StringReader(query.trim());
//       IKSegmentation ikSeg = new IKSegmentation(input, true);   // true 用智能分词 ，false细粒度
//       for (Lexeme lexeme = ikSeg.next(); lexeme != null; lexeme = ikSeg.next()) {
//           System.out.print(lexeme.getLexemeText()+"|");
//       }
//
//   }
	
	public static void main(String[] args) throws IOException
	{
//		String query = "JAVA是一个好语言，从到2015年12月1日它已经有20年的历史了";
//		List<String> list = new ArrayList<String>();
//       StringReader input = new StringReader(query.trim());
//       IKSegmentation ikSeg = new IKSegmentation(input, true);   // true 用智能分词 ，false细粒度
//       for (Lexeme lexeme = ikSeg.next(); lexeme != null; lexeme = ikSeg.next()) {
//           System.out.print(lexeme.getLexemeText()+"|");
//       }
//		String a = "12%%%";
//		String b[] = a.split("%%%");
//		
//		System.out.println(b[0]);
//		System.out.println(b[1]);
		String a = "   ";
		String b = a.replace(" ", "");
		System.out.println(b);
		System.out.println(a);
		System.out.println("end");
		if(b.isEmpty()) {
			System.out.println("empty");
		}
	}
	
   
}
