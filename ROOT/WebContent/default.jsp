<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.apache.jena.rdf.model.ModelFactory"%>
<%@page import="org.apache.http.util.EntityUtils"%>
<%@page import="org.apache.http.HttpEntity"%>
<%@page import="org.apache.http.HttpResponse"%>
<%@page import="java.io.UnsupportedEncodingException"%>
<%@page import="org.apache.http.client.entity.UrlEncodedFormEntity"%>
<%@page import="org.apache.http.message.BasicNameValuePair"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.http.NameValuePair"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.http.client.methods.HttpPost"%>
<%@page import="org.apache.http.impl.client.DefaultHttpClient"%>
<%@page import="org.apache.http.client.HttpClient"%>
<%@page import="org.apache.jena.riot.Lang"%>
<%@page import="java.io.StringWriter"%>
<%@page import="org.apache.jena.rdf.model.Model"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.net.URL"%>
<%!public String writeRDFa(Model model) {
		String rdfa = "";
		try {
			StringWriter writer = new StringWriter();
			model.write(writer, Lang.RDFXML.getName());

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://rdf-translator.appspot.com/convert/detect/rdfa/content");

			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("content", writer.toString()));

			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null)
				rdfa = EntityUtils.toString(entity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rdfa;
	}%>
<%
	int port = request.getLocalPort();
	String scheme = request.getScheme();
	String serverName = request.getServerName();
	String django = scheme + "://" + serverName + ":4040";
	String rdfa = "";

	try {
		Model model = ModelFactory.createDefaultModel();
		model.read("http://localhost:" + port + "/void.ttl");
		rdfa = writeRDFa(model);
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="google-site-verification"
	content="ycmk3pZqoz4GPdQaQi7-tldtH0DoA0YN3nwj5_qnZXU" />
<title>IC/UFF Semantic Web Lab (SWLab)</title>
<link href="swlab.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<center>
		<h1>IC/UFF Semantic Web Lab (SWLab)</h1>
	</center>
	<br>
	<br>
	<br>
	<table align="center" border="0" style="width: 70%">
		<tr>
			<td style="width: 50%">
				<h2>Applications</h2>
				<ul>
					<li><p style="font-size: 22px; display: inline">
							<a href='<%=django + "/datasearch"%>'>Dataset Search</a>:
						</p> One of the design principles that can stimulate the growth and
						increase the usefulness of the Web of data is URIs linkage.
						However, the related URIs are typically in different datasets
						managed by different publishers. Hence, the designer of a new
						datasets must be aware of the existing datasets and inspect their
						content to define links. This application ranks known datasets <i>T<sub>i</sub></i>
						according to the probability that links between a dataset <i>S</i>
						to be published and <i>T<sub>i</sub></i> can be found. <details>
						<summary>details</summary>
						<div style="width: 90%; text-align: justify">
							<ol>
								<li>GOMES, Raphael do Vale A.; CASANOVA, Marco A.; LOPES,
									Giseli Rabello; LEME, Luiz Andr&eacute; P. CRAWLER-LD: A
									Multilevel Metadata Focused Crawler Framework for Linked Data.
									<a
									href="http://link.springer.com/chapter/10.1007/978-3-319-22348-3_17">Lecture
										Notes in Business Information Processing, v. 227, p. 302-319,
										2015</a>.
								</li>
								<li>GOMES, Raphael do Vale A.; CASANOVA, Marco A.; Leme,
									Luiz Andr&eacute; P. Paes; Lopes, Giseli Rabello. A Metadata
									Focused Crawler for Linked Data. In: 16th International
									Conference on Enterprise Information Systems, 2014, Lisbon. <a
									href="http://www.scitepress.org/DigitalLibrary/Link.aspx?doi=10.5220/0004867904890500">Proceedings
										of the 16th International Conference on Enterprise Information
										Systems. v. 1. p. 489-12.</a> (*** Best Paper Award)
								</li>
								<li>Lopes, Giseli Rabello; Nunes, Bernardo Pereira; Leme,
									Luiz Andr&eacute; P. Paes; CASANOVA, Marco A. RecLAK: Analysis
									and Recommendation of Interlinking Datasets. In: LAK Data
									Challenge, co-located with the LAK 2014 conference, 2014,
									Indianapolis, Indiana (US). <a
									href="http://ceur-ws.org/Vol-1137/">Proceedings of the LAK
										Data Challenge, co-located with the LAK 2014 conference, 2014.
										v. 1. p. 1-6.</a>
								</li>
								<li>Lopes, Giseli Rabello; Leme, Luiz Andre P. Paes; Nunes,
									Bernardo Pereira; CASANOVA, Marco A.; Dietze, Stefan. Two
									Approaches to the Dataset Interlinking Recommendation Problem.
									In: 15th International Conference on Web Information System
									Engineering (WISE'14), 2014, Thessaloniki, Greece. <a
									href="http://link.springer.com/chapter/10.1007/978-3-319-11749-2_25">Proceedings
										of the 15th International Conference on Web Information System
										Engineering (WISE'14). Switzerland: Springer International
										Publishing, 2014. v. 8786. p. 324-339.</a>
								</li>
								<li>LEME, Luiz Andr&eacute; P.; Lopes, Giseli Rabello;
									Nunes, Bernardo Pereira; CASANOVA, Marco A.; Dietze, Stefan.
									Identifying Candidate Datasets for Data Interlinking. In: 13th
									International Conference on Web Engineering (ICWE'13), 2013,
									Aalborg, Denmark. <a
									href="http://link.springer.com/chapter/10.1007/978-3-642-39200-9_29">Proceedings
										of the 13th International Conference on Web Engineering
										(ICWE'13). Heidelberg: Springer Berlin Heidelberg, 2013. v.
										7977. p. 354-366.</a>
								</li>
								<li>Lopes, Giseli Rabello; LEME, Luiz Andr&eacute; P. Paes;
									Nunes, Bernardo Pereira; CASANOVA, Marco A.; Dietze, Stefan.
									Recommending Tripleset Interlinking through a Social Network
									Approach. In: 14th International Conference on Web Information
									System Engineering (WISE'13), 2013, Nanjing, China. <a
									href="http://link.springer.com/chapter/10.1007/978-3-642-41230-1_13#page-1">Proceedings
										of the 14th International Conference on Web Information System
										Engineering (WISE'13). Heidelberg: Springer Berlin Heidelberg,
										2013. v. 8180. p. 149-161. </a>
								</li>
							</ol>

						</div>
						</details></li>
				</ul>
				<h2>Datasets</h2>
				<ul>
					<li><p style="font-size: 22px; display: inline">
							<a href="research">Dataset Research</a>: research data
						</p></li>
					<li>
						<p style="font-size: 22px; display: inline">
							<a href="tic10086">Dataset TIC10086</a>: examples.
						</p>
					</li>
				</ul>
			</td>

		</tr>
	</table>
	<%=rdfa%>

</body>
</html>