package uff.ic.geocataloguing;

import uff.ic.geocataloguing.model.DatasetDescription;
import uff.ic.geocataloguing.model.Gazetteer;
import uff.ic.geocataloguing.model.InvalidDatasetDescException;
import uff.ic.geocataloguing.model.Parser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Luiz Andr�
 * @version 1.0
 * @since 1.0
 * @alias ADLGazeteer
 */
public class ADLGazeteer extends Gazetteer {

    /**
     *
     */
    private static final long serialVersionUID = 3323921253085456807L;

    public ADLGazeteer(String url) {
        super(url);
    }

    /**
     * Busca no gazetteer ADL as features contidas na �rea geogr�fica coberta pela imagem.
     *
     * @since 1.0
     * @version 1.0
     * @param uri URI da imagem para a qual se deseja recuperar informa��es no dicion�rio ADL. Com
     * essa URI a imagem � recuperada do cat�logo tempor�rio e suas coordenadas s�o utilizadas na
     * consulta ao gazetteer.
     * @return GeoImage contendo as features contidas na �rea coberta pela imagem.
     * @exception IOException
     * @exception ClassNotFoundException
     * @exception InvalidDatasetDescException
     * @exception ParserConfigurationException
     * @exception SAXException
     */
    public DatasetDescription getDescription(Object obj) throws IOException,
        InvalidDatasetDescException, ParserConfigurationException,
        SAXException {
        DatasetDescription datasetDescription = (DatasetDescription) obj;
        Parser reader = null;
        String query = null;
        // GeoImage image = (GeoImage) getTempCatalog().get(uri);
        OOISO19115GeoImage imageDescription = (OOISO19115GeoImage) datasetDescription;
        if (imageDescription != null) {
            /*
			 * Criar consulta ao gazetteer para recuperar features contidas na
			 * �rea coberta pelas coordenadas da imagem. Documenta��o completa
			 * sobre os servi�os do gazetteer ADL e seus esquemas XML pode ser
			 * obtido em http://www.alexandria.ucsb.edu/gazetteer/protocol/
             */
            query = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "
                + "<gazetteer-service "
                + "xmlns=\"http://www.alexandria.ucsb.edu/gazetteer\" "
                + "xmlns:gml=\"http://www.opengis.net/gml\" "
                + "version=\"1.2\"> " + "<query-request> "
                + "<gazetteer-query> "
                + "<footprint-query operator=\"within\"> " + "<gml:Box> "
                + "<gml:coordinates>"
                + imageDescription.getCornerPointsX(0) + ","
                + imageDescription.getCornerPointsY(0) + ","
                + imageDescription.getCornerPointsX(2) + ","
                + imageDescription.getCornerPointsY(2)
                + "</gml:coordinates> " + "</gml:Box> "
                + "</footprint-query> " + "</gazetteer-query> "
                + "<report-format>standard</report-format> "
                + "</query-request> " + "</gazetteer-service> ";

            /*
			 * Abrir conex�o HTTP com o gazetteer ADL
             */
            URL u = new URL(getUrl());
            HttpURLConnection connection = (HttpURLConnection) u
                .openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            /*
			 * Escrever na conex�o HTTP criada um conte�do XML contendo a
			 * consulta desejada
             */
            connection.setRequestProperty("Content-Type",
                "text/xml; charset=UTF-8");
            OutputStreamWriter writer = new OutputStreamWriter(connection
                .getOutputStream(), "UTF-8");
            writer.write(query);
            writer.flush();

            /*
			 * Recuperar resposta do gazetteer
             */
            InputStream input = connection.getInputStream();

            /*
			 * Criar leitor da resposta para ser enviado ao GeoXMLReader
             */
            InputStreamReader stream = new InputStreamReader(input);
            if (getParsers() != null) {
                reader = (Parser) getParsers().get("xml");
                if (reader != null)
                    /*
					 * Decodifica��o da resposta pelo GeoXMLReader
                     */
                    reader.parse(new InputSource(stream));
            }
        }
        if (getBuilder() != null) {
            /*
			 * Recupare��o de GeoImage montada pelo GazetteerFeauturesBuidler a
			 * partir do vetor MetadataElement[] gerado pelo GeoXMLReader
             */
            imageDescription = (OOISO19115GeoImage) getBuilder()
                .getDatasetDescription();
            if (imageDescription != null)
                imageDescription.setUri(datasetDescription.getUri());
            // getTempCatalog().put(image);
            return imageDescription;
        }
        return null;
    }

}
